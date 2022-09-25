package org.leaguemanager.dbObjects;

import java.sql.*;
import java.util.ArrayList;

// this class contains the JDBC object for connection with my sqlite database
public class DBConnection {

    private static String databaseURL = "jdbc:sqlite:database/BasketballLeagueManager.db";

    public DBConnection() {
        System.out.println("Created database connection object.");
    }

    // below here exist methods representing all the database CRUD operations my application requires
    public ArrayList<League> getLeagues() {

        Connection connection = null;
        ArrayList<League> leagues = new ArrayList<>();

        try {
            connection = DriverManager.getConnection(databaseURL);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = "SELECT * FROM Leagues";

            ResultSet results = statement.executeQuery(query);

            while (results.next()) {
                int leagueID = results.getInt("ID");
                String leagueName = results.getString("Name");

                leagues.add(new League(leagueID, leagueName));
            }

            statement.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }

        return leagues;
    }

    public ArrayList<Team> getTeamsFromLeague(int league) {

        Connection connection = null;
        ArrayList<Team> teams = new ArrayList<>();

        try {
            connection = DriverManager.getConnection(databaseURL);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = "SELECT * FROM Teams WHERE League == " + league;

            ResultSet results = statement.executeQuery(query);

            while (results.next()) {
                int teamID = results.getInt("ID");
                int leagueID = results.getInt("League");
                String teamName = results.getString("Name");

                teams.add(new Team(teamID, leagueID, teamName));
            }

            statement.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }

        return teams;
    }

    public ArrayList<Player> getPlayersFromTeam(int team) {

        ArrayList<Player> players = new ArrayList<>();
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(databaseURL);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = "SELECT * FROM Players WHERE Team == " + team;

            ResultSet results = statement.executeQuery(query);

            while (results.next()) {
                int playerID = results.getInt("ID");
                int teamID = results.getInt("Team");
                String firstName = results.getString("FirstName");
                String lastName = results.getString("LastName");
                int age = results.getInt("Age");
                String position = results.getString("Position");

                players.add(new Player(playerID, teamID, firstName, lastName, age, position));
            }

            statement.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }

        return players;
    }

    public ArrayList<Fixture> getFixturesByLeague(int league) {

        ArrayList<Fixture> fixtures = new ArrayList<>();
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(databaseURL);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = "SELECT t1.Name team1Name, t1.ID team1ID, t2.Name team2Name, t2.ID team2ID, f.ID, f.Date, f.Team1Score, f.Team2Score, f.League " +
                    "FROM Teams t1, Teams t2, Fixtures f WHERE f.Team1=t1.ID AND f.Team2=t2.ID " +
                    "AND f.League == " + league;

            ResultSet results = statement.executeQuery(query);

            while (results.next()) {
                int fixtureID = results.getInt("ID");
                int leagueID = results.getInt("League");
                String date = results.getString("Date");
                int team1 = results.getInt("team1ID");
                int team2 = results.getInt("team2ID");
                String team1Name = results.getString("team1Name");
                String team2Name = results.getString("team2Name");
                int team1Score = results.getInt("Team1Score");
                int team2Score = results.getInt("Team2Score");

                fixtures.add(new Fixture(fixtureID, leagueID, date, team1, team2, team1Name, team2Name, team1Score, team2Score));
            }

            statement.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }

        return fixtures;
    }

    public int addLeague(String name) {

        Connection connection = null;
        int rowsInserted = 0;

        try {
            connection = DriverManager.getConnection(databaseURL);

            String query = "INSERT INTO Leagues (Name) VALUES(?)";

            PreparedStatement ps = null;

            ps = connection.prepareStatement(query);
            ps.setString(1, name);

            rowsInserted = ps.executeUpdate();

            ps.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }

        return rowsInserted;
    }

    public int addTeam(int league, String name) {

        Connection connection = null;
        int rowsInserted = 0;

        try {
            connection = DriverManager.getConnection(databaseURL);

            String query = "INSERT INTO Teams (League, Name) VALUES(?, ?)";

            PreparedStatement ps = null;

            ps = connection.prepareStatement(query);
            ps.setInt(1, league);
            ps.setString(2, name);

            rowsInserted = ps.executeUpdate();

            ps.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }

        return rowsInserted;
    }

    public int addPlayer(int team, String firstName, String lastName, int age, String position) {

        Connection connection = null;
        int rowsInserted = 0;

        try {
            connection = DriverManager.getConnection(databaseURL);

            String query = "INSERT INTO Players (Team, FirstName, LastName, Age, Position) VALUES(?, ?, ?, ?, ?)";

            PreparedStatement ps = null;

            ps = connection.prepareStatement(query);
            ps.setInt(1, team);
            ps.setString(2, firstName);
            ps.setString(3, lastName);
            ps.setInt(4, age);
            ps.setString(5, position);

            rowsInserted = ps.executeUpdate();

            ps.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }

        return rowsInserted;
    }

    public int addFixture(int league, String date, int team1, int team2, int team1Score, int team2Score) {

        Connection connection = null;
        int rowsInserted = 0;

        try {
            connection = DriverManager.getConnection(databaseURL);

            String query = "INSERT INTO Fixtures (League, Date, Team1, Team2, Team1Score, Team2Score) VALUES(?, ?, ?, ?, ?, ?)";

            PreparedStatement ps = null;

            ps = connection.prepareStatement(query);
            ps.setInt(1, league);
            ps.setString(2, date);
            ps.setInt(3, team1);
            ps.setInt(4, team2);
            ps.setInt(5, team1Score);
            ps.setInt(6, team2Score);

            rowsInserted = ps.executeUpdate();

            ps.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }

        return rowsInserted;
    }

    public int deleteLeague(int id) {

        Connection connection = null;
        int rowsDeleted = 0;

        try {
            connection = DriverManager.getConnection(databaseURL);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = "DELETE FROM Leagues WHERE ID == " + id;

            rowsDeleted = statement.executeUpdate(query);

            // delete all fixtures affiliated with the league
            query = "DELETE FROM Fixtures WHERE League == " + id;
            statement.executeUpdate(query);

            // now find all teams affiliated with league, and delete all their players
            ArrayList<Integer> teamIDs = new ArrayList<>();
            query = "SELECT * FROM Teams WHERE League == " + id;
            ResultSet results = statement.executeQuery(query);

            while (results.next()) {
                teamIDs.add(results.getInt("ID"));
            }
            results.close();

            for (int i = 0; i < teamIDs.size(); ++i) {
                query = "DELETE FROM Players WHERE Team == " + teamIDs.get(i);
                statement.executeUpdate(query);
            }

            // now delete all teams
            query = "DELETE FROM Teams WHERE League == " + id;
            statement.executeUpdate(query);

            statement.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }

        return rowsDeleted;
    }

    public int deleteTeam(int id) {

        Connection connection = null;
        int rowsDeleted = 0;

        try {
            connection = DriverManager.getConnection(databaseURL);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = "DELETE FROM Teams WHERE ID == " + id;

            rowsDeleted = statement.executeUpdate(query);

            // delete all players and fixtures affiliated with the team as well
            query = "DELETE FROM Players WHERE Team == " + id;
            statement.executeUpdate(query);
            query = "DELETE FROM Fixtures WHERE Team1 == " + id + " OR Team2 == " + id;
            statement.executeUpdate(query);

            statement.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }

        return rowsDeleted;
    }

    public int deletePlayer(int id) {

        Connection connection = null;
        int rowsDeleted = 0;

        try {
            connection = DriverManager.getConnection(databaseURL);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = "DELETE FROM Players WHERE ID == " + id;

            rowsDeleted = statement.executeUpdate(query);

            statement.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }

        return rowsDeleted;
    }

    public int deleteFixture(int id) {

        Connection connection = null;
        int rowsDeleted = 0;

        try {
            connection = DriverManager.getConnection(databaseURL);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = "DELETE FROM Fixtures WHERE ID == " + id;

            rowsDeleted = statement.executeUpdate(query);

            statement.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }

        return rowsDeleted;
    }

    public int getTeamIDByName(String name) {

        Connection connection = null;
        int teamID = 0;

        try {
            connection = DriverManager.getConnection(databaseURL);

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = "SELECT * FROM Teams WHERE Name == '" + name + "'";

            ResultSet results = statement.executeQuery(query);

            while (results.next()) {
                teamID = results.getInt("ID");
            }

            statement.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }

        return teamID;
    }

    public int editLeague(int id, String name) {

        Connection connection = null;
        int rowsUpdated = 0;

        try {
            connection = DriverManager.getConnection(databaseURL);

            String query = "UPDATE Leagues SET Name=? WHERE ID == " + id;

            PreparedStatement ps = null;

            ps = connection.prepareStatement(query);
            ps.setString(1, name);

            rowsUpdated = ps.executeUpdate();

            ps.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }

        return rowsUpdated;
    }

    public int editPlayer(int id, int team, String firstName, String lastName, int age, String position) {

        Connection connection = null;
        int rowsUpdated = 0;

        try {
            connection = DriverManager.getConnection(databaseURL);

            String query = "UPDATE Players SET Team=?, FirstName=?, LastName=?, Age=?, Position=? WHERE ID == " + id;

            PreparedStatement ps = null;

            ps = connection.prepareStatement(query);
            ps.setInt(1, team);
            ps.setString(2, firstName);
            ps.setString(3, lastName);
            ps.setInt(4, age);
            ps.setString(5, position);

            rowsUpdated = ps.executeUpdate();

            ps.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }

        return rowsUpdated;
    }
}
