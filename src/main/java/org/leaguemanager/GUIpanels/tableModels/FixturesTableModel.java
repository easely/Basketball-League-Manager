package org.leaguemanager.GUIpanels.tableModels;

import org.leaguemanager.dbObjects.Fixture;

import javax.swing.table.AbstractTableModel;
import java.util.List;

// this class allows me to display the relevant fixture data in a JTable
public class FixturesTableModel extends AbstractTableModel {

    private static final int DATE_COL = 0;
    private static final int TEAM1_COL = 1;
    private static final int TEAM2_COL = 2;
    private static final int TEAM1SCORE_COL = 3;

    private String[] columnNames = {"Date", "Home Team", "Away Team", "Home Team Score", "Away Team Score"};
    private List<Fixture> fixtures;

    public FixturesTableModel(List<Fixture> fixtures) {
        this.fixtures = fixtures;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public int getRowCount() {
        return fixtures.size();
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    @Override
    public Object getValueAt(int row, int col) {

        Fixture fixture  = fixtures.get(row);

        switch (col) {
            case DATE_COL -> {return fixture.getDate();}
            case TEAM1_COL -> {return fixture.getTeam1Name();}
            case TEAM2_COL -> {return fixture.getTeam2Name();}
            case TEAM1SCORE_COL -> {return fixture.getTeam1Score();}
            default -> {return fixture.getTeam2Score();}
        }
    }

    public int getFixtureID(int row) {
        Fixture fixture = fixtures.get(row);

        return fixture.getId();
    }

    @Override
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }
}
