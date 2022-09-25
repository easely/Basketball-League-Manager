package org.leaguemanager.GUIpanels.tableModels;

import org.leaguemanager.dbObjects.Player;

import javax.swing.table.AbstractTableModel;
import java.util.List;

// this class allows me to display the relevant player data in a JTable
public class PlayersTableModel extends AbstractTableModel {

    private static final int FIRSTNAME_COL = 0;
    private static final int LASTNAME_COL = 1;
    private static final int AGE_COL = 2;
    private static final int POSITION_COL = 3;

    private String[] columnNames = {"First Name", "Last Name", "Age", "Position"};
    private List<Player> players;

    public PlayersTableModel(List<Player> players) {
        this.players = players;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public int getRowCount() {
        return players.size();
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    @Override
    public Object getValueAt(int row, int col) {

        Player player = players.get(row);

        switch (col) {
            case FIRSTNAME_COL -> {return player.getFirstName();}
            case LASTNAME_COL -> {return player.getLastName();}
            case AGE_COL -> {return player.getAge();}
            default -> {return player.getPosition();}
        }
    }

    public Player getPlayer(int row) {

        return players.get(row);
    }

    @Override
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }
}
