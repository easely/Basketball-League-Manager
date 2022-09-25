package org.leaguemanager.GUIpanels;

import org.leaguemanager.GUIpanels.tableModels.FixturesTableModel;
import org.leaguemanager.Main;
import org.leaguemanager.dbObjects.DBConnection;
import org.leaguemanager.dbObjects.Fixture;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

// a class implementing the GUI of the fixtures display panel
public class FixturesPanel extends JPanel {
    private DBConnection database;
    private JLabel headerText;
    private JScrollPane scrollPane;
    private JTable fixturesTable;
    private JPanel buttonRow;
    private JButton recordFixture;
    private JButton deleteFixture;
    private FixturesTableModel model;
    private int league;
    private JPanel recordFixturePanel;
    private JTextField dateField;
    private JTextField homeTeamField;
    private JTextField awayTeamField;
    private JTextField homeTeamScoreField;
    private JTextField awayTeamScoreField;
    private int validatedHomeTeamID;
    private int validatedAwayTeamID;
    private int validatedHomeTeamScore;
    private int validatedAwayTeamScore;

    public FixturesPanel(DBConnection database) {
        this.database = database;
        league = Main.NO_SELECTION;

        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBorder(BorderFactory.createLineBorder(Color.black));

        headerText = new JLabel("Fixtures");
        headerText.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerText.setFont(new Font(null, Font.PLAIN, 18));
        add(headerText);

        add(Box.createRigidArea(new Dimension(0, 10)));

        // create row of buttons for data manipulation
        buttonRow = new JPanel();
        buttonRow.setLayout(new BoxLayout(buttonRow, BoxLayout.X_AXIS));
        recordFixture = new JButton("Record new fixture");
        deleteFixture = new JButton("   Delete fixture   ");
        buttonRow.add(recordFixture);
        buttonRow.add(deleteFixture);
        add(buttonRow);

        add(Box.createRigidArea(new Dimension(0, 10)));

        fixturesTable = new JTable();
        updateFixtures();

        scrollPane = new JScrollPane(fixturesTable);
        add(scrollPane);

        // create text fields for record fixture
        generateAddFixtureInputBox();

        // add new fixture upon press of record fixture button
        recordFixture.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // guard clause in case no league is selected
                if (league == Main.NO_SELECTION) {
                    JOptionPane.showMessageDialog(FixturesPanel.this, "Select a league first.");
                    return;
                }

                // clear text fields for new input
                dateField.setText("");
                homeTeamField.setText("");
                awayTeamField.setText("");
                homeTeamScoreField.setText("");
                awayTeamScoreField.setText("");

                int ans = JOptionPane.showConfirmDialog(FixturesPanel.this,
                        recordFixturePanel,
                        "Record new fixture",
                        JOptionPane.OK_CANCEL_OPTION);

                if (ans == JOptionPane.YES_OPTION) {
                    // ensure data entered in fields is valid
                    if (!validateInputFields()) {
                        return;
                    }

                    // if all validation checks pass, add to database
                    database.addFixture(league, dateField.getText(), validatedHomeTeamID, validatedAwayTeamID,
                                        validatedHomeTeamScore, validatedAwayTeamScore);

                    JOptionPane.showMessageDialog(FixturesPanel.this, "Fixture added.");
                }

                updateFixtures();
            }
        });

        // delete selected fixture upon press of delete button
        deleteFixture.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // guard clause in case no fixture is selected
                if (fixturesTable.getSelectionModel().isSelectionEmpty()) {
                    JOptionPane.showMessageDialog(FixturesPanel.this, "Select a fixture first.");
                    return;
                }

                int ans = JOptionPane.showConfirmDialog(FixturesPanel.this,
                        "Are you sure you want to delete this fixture?",
                        "Delete fixture", JOptionPane.YES_NO_OPTION);

                if (ans == JOptionPane.YES_OPTION) {
                    database.deleteFixture(model.getFixtureID(fixturesTable.getSelectedRow()));

                    JOptionPane.showMessageDialog(FixturesPanel.this, "Fixture deleted.");
                }

                updateFixtures();
            }
        });
    }

    // change the league in which fixtures are displayed
    public void changeLeague(int newLeague) {
        league = newLeague;
        updateFixtures();
    }

    // update and re-render the fixtures displayed in this panel based on information from the database
    private void updateFixtures() {
        ArrayList<Fixture> fixtures = new ArrayList<>();
        for (Fixture fixture: database.getFixturesByLeague(league)) fixtures.add(fixture);
        model = new FixturesTableModel(fixtures);
        fixturesTable.setModel(model);
    }

    // lay out the input box to add a new player
    private void generateAddFixtureInputBox() {

        recordFixturePanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        dateField = new JTextField(Main.TEXT_FIELD_WIDTH);
        homeTeamField = new JTextField(Main.TEXT_FIELD_WIDTH);
        awayTeamField = new JTextField(Main.TEXT_FIELD_WIDTH);
        homeTeamScoreField = new JTextField(Main.TEXT_FIELD_WIDTH);
        awayTeamScoreField = new JTextField(Main.TEXT_FIELD_WIDTH);

        gbc.gridx = 0;
        gbc.gridy = 0;
        recordFixturePanel.add(new JLabel("Date: "), gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        recordFixturePanel.add(dateField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        recordFixturePanel.add(new JLabel("Home Team: "), gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        recordFixturePanel.add(homeTeamField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        recordFixturePanel.add(new JLabel("Away Team: "), gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        recordFixturePanel.add(awayTeamField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        recordFixturePanel.add(new JLabel("Home Team Score: "), gbc);
        gbc.gridx = 1;
        gbc.gridy = 3;
        recordFixturePanel.add(homeTeamScoreField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 4;
        recordFixturePanel.add(new JLabel("Away Team Score: "), gbc);
        gbc.gridx = 1;
        gbc.gridy = 4;
        recordFixturePanel.add(awayTeamScoreField, gbc);
    }

    // validate all fields of input when adding a fixture
    private boolean validateInputFields() {

        // attempt to validate entered date
        try {
            DateFormat df = new SimpleDateFormat(Main.DATE_FORMAT);
            df.setLenient(false);
            df.parse(dateField.getText());
        } catch (ParseException error) {
            JOptionPane.showMessageDialog(FixturesPanel.this, "Date must be a valid date in this format:\n" +
                    Main.DATE_FORMAT);
            return false;
        }

        validatedHomeTeamID = database.getTeamIDByName(homeTeamField.getText());
        validatedAwayTeamID = database.getTeamIDByName(awayTeamField.getText());

        // disallow non-existent teams from being entered
        if (validatedHomeTeamID == 0 || validatedAwayTeamID == 0) {
            JOptionPane.showMessageDialog(FixturesPanel.this, "Teams must exist in the league.");
            return false;
        }

        // disallow teams playing themselves
        if (validatedHomeTeamID == validatedAwayTeamID) {
            JOptionPane.showMessageDialog(FixturesPanel.this, "Teams cannot play themselves.");
            return false;
        }

        // attempt to convert entered scores into integers
        try {
            validatedHomeTeamScore = Integer.parseInt(homeTeamScoreField.getText());
            validatedAwayTeamScore = Integer.parseInt(awayTeamScoreField.getText());
        } catch (NumberFormatException error) {
            validatedHomeTeamScore = -1;
            validatedAwayTeamScore = -1;
        }

        // validate possible scores
        if (validatedHomeTeamScore < 0 || validatedAwayTeamScore < 0) {
            JOptionPane.showMessageDialog(FixturesPanel.this, "Score must be a non-negative integer.");
            return false;
        }

        return true;
    }
}
