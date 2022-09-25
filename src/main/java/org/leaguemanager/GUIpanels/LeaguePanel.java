package org.leaguemanager.GUIpanels;

import org.leaguemanager.Main;
import org.leaguemanager.dbObjects.DBConnection;
import org.leaguemanager.dbObjects.League;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

// a class implementing the GUI of the leagues display panel
public class LeaguePanel extends JPanel {
    private DBConnection database;
    private JComboBox leagues;
    private JLabel headerText;
    private JLabel leagueText;
    private JButton confirmationButton;
    private JPanel buttonRow;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JPanel addLeaguePanel;
    private JTextField nameField;
    public LeaguePanel(DBConnection database, TeamsPanel teamsPanel, PlayersPanel playersPanel, FixturesPanel fixturesPanel) {
        this.database = database;
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBorder(BorderFactory.createLineBorder(Color.black));

        headerText = new JLabel("Basketball League Manager");
        headerText.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerText.setFont(new Font(null, Font.PLAIN, 32));
        add(headerText);

        add(Box.createRigidArea(new Dimension(0, 15)));

        leagueText = new JLabel("Current League");
        leagueText.setAlignmentX(Component.CENTER_ALIGNMENT);
        leagueText.setFont(new Font(null, Font.PLAIN, 18));
        add(leagueText);

        add(Box.createRigidArea(new Dimension(0, 10)));

        leagues = new JComboBox();
        updateLeagues();
        leagues.setRenderer(new LeaguesListCellRenderer());
        leagues.setMaximumSize(new Dimension(200, 100));
        add(leagues);

        add(Box.createRigidArea(new Dimension(0, 10)));

        confirmationButton = new JButton("Confirm");
        confirmationButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(confirmationButton);

        add(Box.createRigidArea(new Dimension(0, 5)));

        buttonRow = new JPanel();
        buttonRow.setLayout(new BoxLayout(buttonRow, BoxLayout.X_AXIS));
        addButton = new JButton("  Add league ");
        editButton = new JButton("Rename league");
        deleteButton = new JButton("Delete league");
        buttonRow.add(addButton);
        buttonRow.add(editButton);
        buttonRow.add(deleteButton);
        add(buttonRow);

        add(Box.createRigidArea(new Dimension(0, 5)));

        // create text fields for add league button
        generateAddLeagueInputBox();

        // change selected league upon press of confirmation button
        confirmationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                League selectedLeague = (League) leagues.getSelectedItem();

                teamsPanel.changeLeague(selectedLeague.getId());
                fixturesPanel.changeLeague(selectedLeague.getId());
                playersPanel.changeTeam(Main.NO_SELECTION);
            }
        });

        // add new league upon press of add button
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                nameField.setText("");

                int ans = JOptionPane.showConfirmDialog(LeaguePanel.this,
                        addLeaguePanel,
                        "Add league",
                        JOptionPane.OK_CANCEL_OPTION);

                if (ans == JOptionPane.YES_OPTION) {
                    database.addLeague(nameField.getText());

                    JOptionPane.showMessageDialog(LeaguePanel.this, "League added.");
                }

                updateLeagues();

            }
        });

        // rename selected league upon press of edit button
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                League selectedLeague = (League) leagues.getSelectedItem();

                // guard clause in case no league is selected
                if (selectedLeague.getId() == Main.NO_SELECTION) {
                    JOptionPane.showMessageDialog(LeaguePanel.this, "Select a league first.");
                    return;
                }

                nameField.setText(selectedLeague.getName());

                int ans = JOptionPane.showConfirmDialog(LeaguePanel.this,
                        addLeaguePanel,
                        "Rename league",
                        JOptionPane.OK_CANCEL_OPTION);

                if (ans == JOptionPane.YES_OPTION) {
                    database.editLeague(selectedLeague.getId(), nameField.getText());

                    JOptionPane.showMessageDialog(LeaguePanel.this, "League renamed.");
                }

                updateLeagues();
            }
        });

        // delete selected league upon press of delete button
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                League selectedLeague = (League) leagues.getSelectedItem();

                // guard clause in case no league is selected
                if (selectedLeague.getId() == Main.NO_SELECTION) {
                    JOptionPane.showMessageDialog(LeaguePanel.this, "Select a league first.");
                    return;
                }

                int ans = JOptionPane.showConfirmDialog(LeaguePanel.this,
                        "Are you sure you want to delete this league?\n" +
                                "This will also delete any teams, players and fixtures associated with this league.",
                        "Delete league", JOptionPane.YES_NO_OPTION);

                if (ans == JOptionPane.YES_OPTION) {
                    database.deleteLeague(selectedLeague.getId());

                    // update other panels after delete
                    teamsPanel.changeLeague(Main.NO_SELECTION);
                    playersPanel.changeTeam(Main.NO_SELECTION);
                    fixturesPanel.changeLeague(Main.NO_SELECTION);

                    JOptionPane.showMessageDialog(LeaguePanel.this, "League deleted.");
                }

                updateLeagues();
            }
        });
    }

    // update and re-render the leagues displayed in this panel based on information from the database
    private void updateLeagues() {
        ArrayList<League> leaguesList = new ArrayList<>();
        leaguesList.add(new League(0, "Select League"));
        for (League l: database.getLeagues()) leaguesList.add(l);
        leagues.setModel(new DefaultComboBoxModel(leaguesList.toArray()));
    }

    // lay out the input box to add a new league
    private void generateAddLeagueInputBox() {

        addLeaguePanel = new JPanel();
        GridBagConstraints gbc = new GridBagConstraints();
        nameField = new JTextField(Main.TEXT_FIELD_WIDTH);

        gbc.gridx = 0;
        gbc.gridy = 0;
        addLeaguePanel.add(new JLabel("League Name: "), gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        addLeaguePanel.add(nameField, gbc);
    }
}
