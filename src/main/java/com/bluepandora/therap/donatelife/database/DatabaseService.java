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

    public DatabaseService(String driverName, String databaseURL, String userName, String passWord) {
        databaseConnection = new DatabaseConnection(driverName, databaseURL, userName, passWord);
    }

    public ResultSet getResultSet(String query) {
        try {
            statement = (Statement) connection.createStatement();
            resultSet = statement.executeQuery(query);
        } catch (SQLException error) {
            System.out.println("SQL Exception Occurs in getResultSet Method!");
        }
        return resultSet;
    }

    public boolean queryExcute(String query) {
        try {
            statement = (Statement) connection.createStatement();
            statement.execute(query);
            return true;
        } catch (SQLException error) {
            System.out.println("SQL Exception Occurs in queryExcute Method!");
            return false;
        }
    }

    public void databaseOpen() {
        System.out.println("Database Opening");
        connection = databaseConnection.getDatabaseConnection();
        System.out.println("Database Opened");
    }
    
    public void databaseClose(){
        System.out.println("Closing Database");
        closeConnection();
        closeResultSet();
        closeStatement();
        System.out.println("Database Closed");
    }
    
    private void closeConnection() {
        
        try {
            if (connection != null) {
//                System.out.println("Closing Connection!");
                connection.close();
                connection=null;
//                System.out.println("Connection Closed!");
            }
            
        } catch (SQLException error) {
            System.out.println("Problem occurs in closing database connection!" + error);
        }
    }

    private void closeResultSet() {
        try {
            if (resultSet != null) {
//                System.out.println("Closing ResultSet!");
                resultSet.close();
                resultSet = null;
//                System.out.println("ResultSet Closed!");
            }
        } catch (SQLException error) {
            System.out.println("Problem occurs in closing resultset! " + error);
        }
    }

    private void closeStatement() {
        try {
            if (statement != null) {
//                System.out.println("Closing Statement!");
                statement.close();
                statement=null;
//                System.out.println("Statement Closed!");
            }
        } catch (SQLException error) {
            System.out.println("Problem occurs in closing Statement! " + error);
        }
    }
}
