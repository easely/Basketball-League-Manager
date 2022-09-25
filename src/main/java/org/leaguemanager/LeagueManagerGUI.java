package org.leaguemanager;

import org.leaguemanager.GUIpanels.FixturesPanel;
import org.leaguemanager.GUIpanels.LeaguePanel;
import org.leaguemanager.GUIpanels.PlayersPanel;
import org.leaguemanager.GUIpanels.TeamsPanel;
import org.leaguemanager.dbObjects.DBConnection;

import javax.swing.*;
import java.awt.*;

// a class representing the frame that holds all other GUI display panels
public class LeagueManagerGUI extends JFrame {
    private DBConnection database;
    private JPanel masterPanel;
    private JPanel leaguePanel;
    private JPanel teamsPanel;
    private JPanel playersPanel;
    private JPanel fixturesPanel;

    public LeagueManagerGUI() {

        database = new DBConnection();
        masterPanel = new JPanel();
        masterPanel.setLayout(new BorderLayout());

        // create players and fixtures panels
        playersPanel = new PlayersPanel(database);
        playersPanel.setPreferredSize(new Dimension(400, 400));
        fixturesPanel = new FixturesPanel(database);
        fixturesPanel.setPreferredSize(new Dimension(600, 400));

        // create teams and leagues panels
        teamsPanel = new TeamsPanel(database, (PlayersPanel) playersPanel, (FixturesPanel) fixturesPanel);
        teamsPanel.setPreferredSize(new Dimension(200, 400));
        leaguePanel = new LeaguePanel(database, (TeamsPanel) teamsPanel, (PlayersPanel) playersPanel, (FixturesPanel) fixturesPanel);

        // add all panels to frame
        masterPanel.add(leaguePanel, BorderLayout.PAGE_START);
        masterPanel.add(teamsPanel, BorderLayout.LINE_START);
        masterPanel.add(playersPanel, BorderLayout.CENTER);
        masterPanel.add(fixturesPanel, BorderLayout.LINE_END);

        // render display
        add(masterPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("League Manager");
        pack();
        setVisible(true);
    }
}
