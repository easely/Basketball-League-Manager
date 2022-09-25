package org.leaguemanager.GUIpanels.tableModels;

import org.leaguemanager.dbObjects.Team;

import javax.swing.table.AbstractTableModel;
import java.util.List;

// this class allows me to display the relevant team data in a JTable
public class TeamsTableModel extends AbstractTableModel {

    private static final int NAME_COL = 0;

    private String[] columnNames = {"Team Name"};
    private List<Team> teams;

    public TeamsTableModel(List<Team> teams) {
        this.teams = teams;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public int getRowCount() {
        return teams.size();
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    @Override
    public Object getValueAt(int row, int col) {

        Team team = teams.get(row);

        return team.getName();
    }

    public int getTeamID(int row) {
        Team team = teams.get(row);

        return team.getId();
    }

    @Override
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }
}
