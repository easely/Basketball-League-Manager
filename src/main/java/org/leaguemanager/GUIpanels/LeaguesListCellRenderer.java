package org.leaguemanager.GUIpanels;

import org.leaguemanager.dbObjects.League;

import javax.swing.*;
import java.awt.*;

public class LeaguesListCellRenderer extends DefaultListCellRenderer {

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if (value instanceof League) {
            value = ((League) value).getName();
        }
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        return this;
    }
}
