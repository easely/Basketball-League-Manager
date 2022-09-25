package org.leaguemanager.dbObjects;

public class Fixture {
    private int id;
    private int league;
    private String date;
    private int team1;
    private int team2;
    private int team1Score;
    private int team2Score;
    private String team1Name;
    private String team2Name;

    public Fixture(int id, int league, String date, int team1, int team2, String team1Name, String team2Name, int team1Score, int team2Score) {
        this.id = id;
        this.league = league;
        this.date = date;
        this.team1 = team1;
        this.team2 = team2;
        this.team1Name = team1Name;
        this.team2Name = team2Name;
        this.team1Score = team1Score;
        this.team2Score = team2Score;
    }

    public int getId() {
        return id;
    }

    public int getLeague() {
        return league;
    }

    public String getDate() {
        return date;
    }

    public int getTeam1() {
        return team1;
    }

    public int getTeam2() {
        return team2;
    }

    public int getTeam1Score() {
        return team1Score;
    }

    public int getTeam2Score() {
        return team2Score;
    }

    public String getTeam1Name() {
        return team1Name;
    }

    public String getTeam2Name() {
        return team2Name;
    }
}
