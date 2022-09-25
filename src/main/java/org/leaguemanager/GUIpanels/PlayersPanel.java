package org.leaguemanager.GUIpanels;

import org.leaguemanager.GUIpanels.tableModels.PlayersTableModel;
import org.leaguemanager.Main;
import org.leaguemanager.dbObjects.DBConnection;
import org.leaguemanager.dbObjects.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Set;

// a class implementing the GUI of the players display panel
public class PlayersPanel extends JPanel {
    private DBConnection database;
    private JLabel headerText;
    private JScrollPane scrollPane;
    private JTable playersTable;
    private JPanel buttonRow;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton transferButton;
    private PlayersTableModel model;
    private int team;
    private JPanel enterPlayerPanel;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField ageField;
    private JTextField positionField;
    private Set<String> positions;
    private int validatedAge;
    private JPanel transferTeamPanel;
    private JTextField newTeamField;

    public PlayersPanel(DBConnection database) {
        this.database = database;
        team = Main.NO_SELECTION;
        positions = Set.of("PG", "SG", "SF", "PF", "C");

        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBorder(BorderFactory.createLineBorder(Color.black));

        headerText = new JLabel("Players");
        headerText.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerText.setFont(new Font(null, Font.PLAIN, 18));
        add(headerText);

        add(Box.createRigidArea(new Dimension(0, 10)));

        // create row of buttons for data manipulation
        buttonRow = new JPanel();
        buttonRow.setLayout(new BoxLayout(buttonRow, BoxLayout.X_AXIS));
        addButton = new JButton("Add player");
        editButton = new JButton("Edit player");
        deleteButton = new JButton("Delete player");
        buttonRow.add(addButton);
        buttonRow.add(editButton);
        buttonRow.add(deleteButton);
        add(buttonRow);

        add(Box.createRigidArea(new Dimension(0, 10)));

        playersTable = new JTable();
        updatePlayers();
        playersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        scrollPane = new JScrollPane(playersTable);
        add(scrollPane);

        // create text popups
        generateEnterPlayerInputBox();
        generateTransferTeamInputBox();

        transferButton = new JButton("Transfer team");
        transferButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(transferButton);

        // add new player upon press of add button
        addButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                // guard clause in case no team is selected
                if (team == Main.NO_SELECTION) {
                    JOptionPane.showMessageDialog(PlayersPanel.this, "Select a team first.");
                    return;
                }

                // clear text fields for new input
                firstNameField.setText("");
                lastNameField.setText("");
                ageField.setText("");
                positionField.setText("");

                int ans = JOptionPane.showConfirmDialog(PlayersPanel.this,
                        enterPlayerPanel,
                        "Add player",
                        JOptionPane.OK_CANCEL_OPTION);

                if (ans == JOptionPane.YES_OPTION) {
                    // ensure data entered in fields is valid
                    if (!validateInputFields()) {
                        return;
                    }

                    // if all validation checks pass, add to database
                    database.addPlayer(team, firstNameField.getText(), lastNameField.getText(), validatedAge, positionField.getText());

                    JOptionPane.showMessageDialog(PlayersPanel.this, "Player added.");
                }

                updatePlayers();
            }
        });

        // edit selected player upon press of edit button
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // guard clause in case no player is selected
                if (playersTable.getSelectionModel().isSelectionEmpty()) {
                    JOptionPane.showMessageDialog(PlayersPanel.this, "Select a player first.");
                    return;
                }

                // get selected player's data to edit
                Player selectedPlayer = model.getPlayer(playersTable.getSelectedRow());
                firstNameField.setText(selectedPlayer.getFirstName());
                lastNameField.setText(selectedPlayer.getLastName());
                ageField.setText(Integer.toString(selectedPlayer.getAge()));
                positionField.setText(selectedPlayer.getPosition());

                int ans = JOptionPane.showConfirmDialog(PlayersPanel.this,
                        enterPlayerPanel,
                        "Edit player",
                        JOptionPane.OK_CANCEL_OPTION);

                if (ans == JOptionPane.YES_OPTION) {
                    // ensure data entered in fields is valid
                    if (!validateInputFields()) {
                        return;
                    }

                    database.editPlayer(selectedPlayer.getId(), team, firstNameField.getText(), lastNameField.getText(),
                                        validatedAge, positionField.getText());

                    JOptionPane.showMessageDialog(PlayersPanel.this, "Player updated.");
                }

                updatePlayers();
            }
        });

        // delete selected player upon press of delete button
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // guard clause in case no player is selected
                if (playersTable.getSelectionModel().isSelectionEmpty()) {
                    JOptionPane.showMessageDialog(PlayersPanel.this, "Select a player first.");
                    return;
                }

                int ans = JOptionPane.showConfirmDialog(PlayersPanel.this,
                        "Are you sure you want to delete this player?",
                        "Delete player", JOptionPane.YES_NO_OPTION);

                if (ans == JOptionPane.YES_OPTION) {
                    Player selectedPlayer = model.getPlayer(playersTable.getSelectedRow());
                    database.deletePlayer(selectedPlayer.getId());

                    JOptionPane.showMessageDialog(PlayersPanel.this, "Player deleted.");
                }

                updatePlayers();
            }
        });

        // transfer selected player to a new team
        transferButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // guard clause in case no player is selected
                if (playersTable.getSelectionModel().isSelectionEmpty()) {
                    JOptionPane.showMessageDialog(PlayersPanel.this, "Select a player first.");
                    return;
                }

                int ans = JOptionPane.showConfirmDialog(PlayersPanel.this,
                        transferTeamPanel,
                        "Transfer player",
                        JOptionPane.OK_CANCEL_OPTION);

                if (ans == JOptionPane.YES_OPTION) {

                    int newTeam = database.getTeamIDByName(newTeamField.getText());
                    if (newTeam == 0) {
                        JOptionPane.showMessageDialog(PlayersPanel.this, "New team must be an existing team.");
                        return;
                    }

                    // update selected player with new team
                    Player selectedPlayer = model.getPlayer(playersTable.getSelectedRow());
                    database.editPlayer(selectedPlayer.getId(), newTeam, selectedPlayer.getFirstName(), selectedPlayer.getLastName(),
                                        selectedPlayer.getAge(), selectedPlayer.getPosition());

                    JOptionPane.showMessageDialog(PlayersPanel.this, "Player transferred.");
                }

                updatePlayers();
            }
        });
    }

    // change the team in which players are displayed
    public void changeTeam(int newTeam) {
        team = newTeam;
        updatePlayers();
    }

    // update and re-render the players displayed in this panel based on information from the database
    private void updatePlayers() {
        ArrayList<Player> players = new ArrayList<>();
        for (Player player: database.getPlayersFromTeam(team)) players.add(player);
        model = new PlayersTableModel(players);
        playersTable.setModel(model);
    }

    // lay out the input box to add a new player
    private void generateEnterPlayerInputBox() {

        enterPlayerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        firstNameField = new JTextField(Main.TEXT_FIELD_WIDTH);
        lastNameField = new JTextField(Main.TEXT_FIELD_WIDTH);
        ageField = new JTextField(Main.TEXT_FIELD_WIDTH);
        positionField = new JTextField(Main.TEXT_FIELD_WIDTH);

        gbc.gridx = 0;
        gbc.gridy = 0;
        enterPlayerPanel.add(new JLabel("First Name: "), gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        enterPlayerPanel.add(firstNameField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        enterPlayerPanel.add(new JLabel("Last Name: "), gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        enterPlayerPanel.add(lastNameField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        enterPlayerPanel.add(new JLabel("Age: "), gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        enterPlayerPanel.add(ageField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        enterPlayerPanel.add(new JLabel("Position: "), gbc);
        gbc.gridx = 1;
        gbc.gridy = 3;
        enterPlayerPanel.add(positionField, gbc);

    }

    // lay out the input box to transfer player to different team
    private void generateTransferTeamInputBox() {

        transferTeamPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        newTeamField = new JTextField(Main.TEXT_FIELD_WIDTH);

        gbc.gridx = 0;
        gbc.gridy = 0;
        transferTeamPanel.add(new JLabel("Enter new team: "), gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        transferTeamPanel.add(newTeamField, gbc);
    }

    // validate all fields of input when adding or editing player
    private boolean validateInputFields() {

        // attempt to convert entered age into integer
        try {
            validatedAge = Integer.parseInt(ageField.getText());
        } catch (NumberFormatException error) {
            validatedAge = 0;
        }

        // validate possible ages
        if (validatedAge < 1) {
            JOptionPane.showMessageDialog(PlayersPanel.this, "Player age must be a positive integer.");
            return false;
        }

        // validate possible positions
        if (!positions.contains(positionField.getText())) {
            JOptionPane.showMessageDialog(PlayersPanel.this, "Position must be a valid position:\n" +
                    "'PG', 'SG', 'SF', 'PF', or 'C'");
            return false;
        }

        return true;
    }
}
