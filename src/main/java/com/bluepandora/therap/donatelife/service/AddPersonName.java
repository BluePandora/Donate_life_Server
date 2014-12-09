/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluepandora.therap.donatelife.service;

import com.bluepandora.therap.donatelife.database.DatabaseService;
import com.bluepandora.therap.donatelife.database.GetQuery;

/**
 *
 * @author Biswajit Debnath
 */
public class AddPersonName {

    public static void addPersonName(String firstName, String lastName, DatabaseService dbService) {
        boolean nameTaken = CheckService.isNameAlreadyAdded(firstName, lastName, dbService);
        if (nameTaken == false) {
            String query = GetQuery.addPersonNameQuery(firstName, lastName);
            dbService.queryExcute(query);
        }
    }
}
