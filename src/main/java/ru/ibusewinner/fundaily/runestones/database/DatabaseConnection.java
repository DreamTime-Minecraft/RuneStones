package ru.ibusewinner.fundaily.runestones.database;

import java.sql.*;

public class DatabaseConnection {
    private String reason;
    public Connection con;
    public Statement statement;

    public DatabaseConnection(final String reason) {
        this.reason = reason;
        this.connect();
    }

    public void connect() {
        try {
            this.con = DriverManager.getConnection("jdbc:mysql://", "", "");
            this.statement = this.con.createStatement();
        }
        catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Failed To Create Connection. [" + this.reason.toUpperCase() + "]");
            System.out.println("Unable to connect. Please check to ensure connection is working, and MySQL information is correct.");
            System.out.println("No longer trying to do anything database related to prevent further errors. Restart the server when you believe that the problem has been fixed.");
        }
    }

    public void disconnect() {
        try {
            this.con.close();
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void close() {
        this.disconnect();
    }

    public String getReason() {
        return this.reason;
    }

    public ResultSet executeQuery(final String str) {
        try {
            return this.statement.executeQuery(str);
        }
        catch (SQLException ex) {
            System.out.println("Failed to execute query. [" + this.reason.toUpperCase() + "] [" + str + "]");
            ex.printStackTrace();
            return null;
        }
    }

    public void executeCommand(final String str) {
        try {
            final PreparedStatement prepareStatement = this.con.prepareStatement(str);
            prepareStatement.executeUpdate();
            prepareStatement.close();
        }
        catch (SQLException ex) {
            System.out.println("Failed to execute update. [" + this.reason.toUpperCase() + "] [" + str + "]");
            ex.printStackTrace();
        }
    }

    public void execute(final String str) {
        try {
            final PreparedStatement prepareStatement = this.con.prepareStatement(str);
            prepareStatement.execute();
            prepareStatement.close();
        }
        catch (SQLException ex) {
            System.out.println("Failed to execute update. [" + this.reason.toUpperCase() + "] [" + str + "]");
            ex.printStackTrace();
        }
    }
}
