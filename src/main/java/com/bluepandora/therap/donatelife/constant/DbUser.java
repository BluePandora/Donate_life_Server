/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluepandora.therap.donatelife.constant;

/**
 *
 * @author Biswajit Debnath
 */
public class DbUser {

    /**
     * This is for local access
     */
//    public static final String USERNAME = "root";
//    public static final String PASSWORD = "coderbd";
//    public static final String DRIVER_NAME = "com.mysql.jdbc.Driver";
//    public static final String DATABASE_URL = "jdbc:mysql://localhost:3306/life?useUnicode=true&characterEncoding=UTF-8";
//  
    
    /**
     * This is for openshift database access parameter
     */
   
    public static final String USERNAME = "";
    public static final String PASSWORD = "";
    public static final String DRIVER_NAME = "com.mysql.jdbc.Driver";
    public static final String OPENSHIFT_MYSQL_DB_HOST= System.getenv("OPENSHIFT_MYSQL_DB_HOST");
    public static final String OPENSHIFT_MYSQL_DB_PORT= System.getenv("OPENSHIFT_MYSQL_DB_PORT");
    public static final String DATABASE_URL = "jdbc:mysql://"+OPENSHIFT_MYSQL_DB_HOST+":"+OPENSHIFT_MYSQL_DB_PORT+"/life?useUnicode=true&characterEncoding=UTF-8";

}
