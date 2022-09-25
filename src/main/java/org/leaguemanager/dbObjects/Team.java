package org.leaguemanager.dbObjects;

public class Team {
    private int id;
    private int league;
    private String name;

    public Team(int id, int league, String name) {
        this.id = id;
        this.league = league;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public int getLeague() {
        return league;
    }

    public String getName() {
        return name;
    }
}
