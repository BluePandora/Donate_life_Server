/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluepandora.therap.donatelife.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 *
 * @author Biswajit Debnath
 */
public class DatabaseService {

    DatabaseConnection databaseConnection;
    Statement statement;
    ResultSet resultSet;
    Connection connection;

    public DatabaseService() {
        databaseConnection = new DatabaseConnection("root", "root");
    }

    public DatabaseService(String username, String password) {
        databaseConnection = new DatabaseConnection(username, password);
    }

    public DatabaseService(String databaseType, String databaseURL, String userName, String passWord) {
        databaseConnection = new DatabaseConnection(databaseType, databaseURL, userName, passWord);
    }

    public ResultSet getResultSet(String query) {
        
        if (connection == null) {
            connectToDatabase();
        }
        
        try {
            statement = (Statement) connection.createStatement();
            resultSet = statement.executeQuery(query);
        } catch (SQLException error) {
            System.out.println("SQL Exception Occurs in getResultSet Method!");
        }
        return resultSet;
    }

    public boolean queryExcute(String query) {
        
        if (connection == null) {
            connectToDatabase();
        }
        
        try {
            statement = (Statement) connection.createStatement();
            statement.execute(query);
            return true;
        } catch (SQLException error) {
            System.out.println("SQL Exception Occurs in queryExcute Method!");
            return false;
        }
    }

    private void connectToDatabase() {
        connection = databaseConnection.getDatabaseConnection();
    }

    private void closeDatabaseConnection() {

        try {
            if (connection != null) {
                connection.close();
                System.out.println("Database Connection Closed!");
            }

        } catch (SQLException error) {
            System.out.println("Database Connection Closing Problem! " + error);
        }
    }
}
