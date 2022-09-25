package org.leaguemanager.GUIpanels;

import org.leaguemanager.GUIpanels.tableModels.TeamsTableModel;
import org.leaguemanager.Main;
import org.leaguemanager.dbObjects.DBConnection;
import org.leaguemanager.dbObjects.Team;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

// a class implementing the GUI of the teams display panel
public class TeamsPanel extends JPanel {
    private DBConnection database;
    private JLabel headerText;
    private JScrollPane scrollPane;
    private JTable teamsTable;
    private JButton confirmationButton;
    private JPanel buttonRow;
    private JButton addButton;
    private JButton deleteButton;
    private TeamsTableModel model;
    private int league;
    private JPanel addTeamPanel;
    private JTextField nameField;

    public TeamsPanel(DBConnection database, PlayersPanel playersPanel, FixturesPanel fixturesPanel) {
        this.database = database;
        league = Main.NO_SELECTION;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createLineBorder(Color.black));

        headerText = new JLabel("Teams");
        headerText.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerText.setFont(new Font(null, Font.PLAIN, 18));
        add(headerText);

        add(Box.createRigidArea(new Dimension(0, 10)));

        confirmationButton = new JButton("Select team");
        confirmationButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(confirmationButton);

        add(Box.createRigidArea(new Dimension(0, 10)));

        teamsTable = new JTable();
        updateTeams();
        teamsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(teamsTable);

        scrollPane = new JScrollPane(teamsTable);
        add(scrollPane);

        // create row of buttons for data manipulation
        buttonRow = new JPanel();
        buttonRow.setLayout(new BoxLayout(buttonRow, BoxLayout.X_AXIS));
        addButton = new JButton("Add team");
        deleteButton = new JButton("Delete team");
        buttonRow.add(addButton);
        buttonRow.add(deleteButton);
        add(buttonRow);

        // create text fields for add team button
        generateAddTeamInputBox();

        // change selected team upon press of confirmation button
        confirmationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // guard clause in case no team is selected
                if (teamsTable.getSelectionModel().isSelectionEmpty()) {
                    return;
                }

                playersPanel.changeTeam(model.getTeamID(teamsTable.getSelectedRow()));
            }
        });

        // add new team on press of add button
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // guard clause in case no league is selected
                if (league == Main.NO_SELECTION) {
                    JOptionPane.showMessageDialog(TeamsPanel.this, "Select a league first.");
                    return;
                }

                nameField.setText("");

                int ans = JOptionPane.showConfirmDialog(TeamsPanel.this,
                        addTeamPanel,
                        "Add team",
                        JOptionPane.OK_CANCEL_OPTION);

                if (ans == JOptionPane.YES_OPTION) {
                    database.addTeam(league, nameField.getText());

                    JOptionPane.showMessageDialog(TeamsPanel.this, "Team added.");
                }

                updateTeams();
            }
        });

        // delete selected team upon press of delete button
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // guard clause in case no team is selected
                if (teamsTable.getSelectionModel().isSelectionEmpty()) {
                    JOptionPane.showMessageDialog(TeamsPanel.this, "Select a team first.");
                    return;
                }

                int ans = JOptionPane.showConfirmDialog(TeamsPanel.this,
                        "Are you sure you want to delete this team?\n" +
                                "This will also delete any players and fixtures associated with this team.",
                        "Delete team", JOptionPane.YES_NO_OPTION);

                if (ans == JOptionPane.YES_OPTION) {
                    database.deleteTeam(model.getTeamID(teamsTable.getSelectedRow()));

                    // update other two panels after delete
                    playersPanel.changeTeam(Main.NO_SELECTION);
                    fixturesPanel.changeLeague(league);

                    JOptionPane.showMessageDialog(TeamsPanel.this, "Team deleted.");
                }

                updateTeams();
            }
        });
    }

    // change the league in which teams are displayed
    public void changeLeague(int newLeague) {
        league = newLeague;
        updateTeams();
    }

    // update and re-render the teams displayed in this panel based on information from the database
    private void updateTeams() {
        ArrayList<Team> teams = new ArrayList<>();
        for (Team team: database.getTeamsFromLeague(league)) teams.add(team);
        model = new TeamsTableModel(teams);
        teamsTable.setModel(model);
    }

    // lay out the input box to add a new team
    private void generateAddTeamInputBox() {

        addTeamPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        nameField = new JTextField(Main.TEXT_FIELD_WIDTH);

        gbc.gridx = 0;
        gbc.gridy = 0;
        addTeamPanel.add(new JLabel("Team Name: "), gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        addTeamPanel.add(nameField, gbc);

    }
}
