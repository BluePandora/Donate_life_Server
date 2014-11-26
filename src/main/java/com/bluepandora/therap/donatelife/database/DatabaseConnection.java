/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluepandora.therap.donatelife.database;


import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;

/**
 *
 * @author Biswajit Debnath
 */
public class DatabaseConnection {
    
    private String databaseType;
    private String databaseURL;
    private String userName;
    private String passWord;

    public DatabaseConnection(String userName, String passWord) {
        setDatabaseInfo(userName, passWord);
    }

    public DatabaseConnection(String databaseURL, String userName, String passWord) {
        setDatabaseInfo(databaseURL, userName, passWord);
    }

    public DatabaseConnection(String databaseType, String databaseURL, String userName, String passWord) {
        setDatabaseInfo(databaseType, databaseURL, userName, passWord);
    }

    public void setDatabaseInfo(String userName, String passWord) {
        setUserName(userName);
        setPassWord(passWord);
    }

    public void setDatabaseInfo(String databaseURL, String userName, String passWord) {
        setDatabaseURL(databaseURL);
        setUserName(userName);
        setPassWord(passWord);
    }

    public void setDatabaseInfo(String databaseType, String databaseURL, String userName, String passWord) {
        setDatabaseType(databaseType);
        setDatabaseURL(databaseURL);
        setUserName(userName);
        setPassWord(passWord);
    }

    public Connection getDatabaseConnection() {
        try {
            Class.forName(databaseType).newInstance();
            System.out.println("Database is connecting...");
            Connection connection = DriverManager.getConnection(databaseURL, userName, passWord);
            System.out.println("Database Connection Successful!");
            return connection;
        } catch (Exception err) {
            System.out.println("SQL ERROR: " + err);
            System.out.println("Database couldn't connect!");
            return null;
        }
    }

    private void setDatabaseType(String databaseType) {
        this.databaseType = databaseType;
       // System.out.println("Type:" + databaseType);
    }

    private void setDatabaseURL(String databaseURL) {
        this.databaseURL = databaseURL;
       // System.out.println("DatabaseURL:" + databaseURL);
    }

    private void setUserName(String userName) {
        this.userName = userName;
        //System.out.println("Username:" + userName);
    }

    private void setPassWord(String passWord) {
        this.passWord = passWord;
       // System.out.println("Password:" + passWord);
    }

}
