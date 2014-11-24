/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluepandora.therap.donatelife.gcmservice;

import com.bluepandora.therap.donatelife.constant.DbUser;
import com.bluepandora.therap.donatelife.database.DatabaseService;
import com.bluepandora.therap.donatelife.database.GetQuery;
import com.bluepandora.therap.donatelife.debug.Debug;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Biswajit Debnath
 */
public class FindDonator {

    private static DatabaseService dbService = new DatabaseService(
            DbUser.DATABASETYPE,
            DbUser.DATABASEURL,
            DbUser.USERNAME,
            DbUser.PASSWORD
    );

    /**
     * This method for finding GCM-ID from the Donate Life's database matched
     * with the Blood Group and District
     *
     * @param groupId
     * @param hospitalId
     */
    public static List findDonator(String groupId, String hospitalId) {
        String query = GetQuery.getGcmIdOfDonatorQuery(groupId, hospitalId);
        Debug.debugLog("FIND DONATOR: ", query);
        ResultSet result = dbService.getResultSet(query);
        List donatorList = new ArrayList<Donator>();

        try {
            while (result.next()) {
                String gcmId = result.getString("gcm_id");
                String mobileNumber = result.getString("mobile_number");
                donatorList.add(new Donator(mobileNumber, gcmId));
            }
        } catch (SQLException error) {
            Debug.debugLog("FINDING DONATOR SQL EXCEPTION!");
        }
        return donatorList;

    }

    public static List findDonatorGCMId(String groupId, String hospitalId) {
        String query = GetQuery.getGcmIdOfDonatorQuery(groupId, hospitalId);
        Debug.debugLog("FIND DONATOR: ", query);
        ResultSet result = dbService.getResultSet(query);
        List donatorList = new ArrayList<String>();

        try {
            while (result.next()) {
                String gcmId = result.getString("gcm_id");
                donatorList.add(gcmId);
            }
        } catch (SQLException error) {
            Debug.debugLog("FINDING DONATOR SQL EXCEPTION!");
        }
        return donatorList;
    }
}
