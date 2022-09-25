package org.leaguemanager.dbObjects;

public class Player {
    private int id;
    private int team;
    private String firstName;
    private String lastName;
    private int age;
    private String position;

    public Player(int id, int team, String firstName, String lastName, int age, String position) {
        this.id = id;
        this.team = team;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.position = position;
    }

    public int getId() {
        return id;
    }

    public int getTeam() {
        return team;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getAge() {
        return age;
    }

    public String getPosition() {
        return position;
    }
}
