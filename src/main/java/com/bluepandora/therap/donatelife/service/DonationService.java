/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluepandora.therap.donatelife.service;


import com.bluepandora.therap.donatelife.constant.DbUser;
import com.bluepandora.therap.donatelife.constant.Enum;
import com.bluepandora.therap.donatelife.database.DatabaseService;
import com.bluepandora.therap.donatelife.database.GetQuery;
import com.bluepandora.therap.donatelife.debug.Debug;
import com.bluepandora.therap.donatelife.debug.LogMessageJson;
import com.bluepandora.therap.donatelife.jsonsender.SendJsonData;
import com.bluepandora.therap.donatelife.validation.DataValidation;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Biswajit Debnath
 */
public class DonationService extends DbUser{
    public static void addDonationRecord(HttpServletRequest request, HttpServletResponse response) throws JSONException {
        DatabaseService dbService = new DatabaseService(DRIVER_NAME, DATABASE_URL, USERNAME, PASSWORD);
        dbService.databaseOpen();

        String requestName = request.getParameter("requestName");
        JSONObject jsonObject = null;

        if (request.getParameter("mobileNumber") != null
                && request.getParameter("donationDate") != null
                && request.getParameter("donationDetail") != null) {

            String mobileNumber = request.getParameter("mobileNumber");
            String donationDate = request.getParameter("donationDate");
            String donationDetail = request.getParameter("donationDetail");

            Debug.debugLog("MobileNumber: ", mobileNumber, "Date: ", donationDate, "Details: ", donationDetail);

            if (DataValidation.isValidMobileNumber(mobileNumber) && DataValidation.isValidString(donationDate) && DataValidation.isValidString(donationDetail)) {
                String query = GetQuery.addDonationRecordQuery(mobileNumber, donationDate, donationDetail);
                //    Debug.debugLog("Add Donation Record Query: ", query);
                boolean done = dbService.queryExcute(query);
                if (done) {
                    jsonObject = LogMessageJson.getLogMessageJson(Enum.CORRECT, Enum.MESSAGE_DONATION_ADDED, requestName);
                    Debug.debugLog("MOBILE NUMBER: ", mobileNumber, " ADD DONATION SUCCESS");
                } else {
                    jsonObject = LogMessageJson.getLogMessageJson(Enum.ERROR, Enum.MESSAGE_ERROR, requestName);
                }
            } else {
                jsonObject = LogMessageJson.getLogMessageJson(Enum.ERROR, Enum.MESSAGE_INVALID_VALUE, requestName);
            }
        } else {
            jsonObject = LogMessageJson.getLogMessageJson(Enum.ERROR, Enum.MESSAGE_LESS_PARAMETER, requestName);
        }
        SendJsonData.sendJsonData(request, response, jsonObject);
        dbService.databaseClose();
    }
    
    public static void removeDonationRecord(HttpServletRequest request, HttpServletResponse response) throws JSONException {
        DatabaseService dbService = new DatabaseService(DRIVER_NAME, DATABASE_URL, USERNAME, PASSWORD);
        dbService.databaseOpen();
        String requestName = request.getParameter("requestName");
        JSONObject jsonObject = null;

        if (request.getParameter("mobileNumber") != null && request.getParameter("donationDate") != null) {
            String mobileNumber = request.getParameter("mobileNumber");
            String donationDate = request.getParameter("donationDate");
            if (DataValidation.isValidMobileNumber(mobileNumber) && DataValidation.isValidString(donationDate)) {
                String query = GetQuery.removeDonationRecordQuery(mobileNumber, donationDate);
                boolean done = dbService.queryExcute(query);
                if (done) {
                    jsonObject = LogMessageJson.getLogMessageJson(Enum.CORRECT, Enum.MESSAGE_DONATION_REMOVED, requestName);
                    Debug.debugLog("MOBILE NUMBER: ", mobileNumber, " REMOVE DONATION SUCCESS");
                } else {
                    jsonObject = LogMessageJson.getLogMessageJson(Enum.ERROR, Enum.MESSAGE_ERROR, requestName);
                }
            } else {
                jsonObject = LogMessageJson.getLogMessageJson(Enum.ERROR, Enum.MESSAGE_INVALID_VALUE, requestName);
            }
        } else {
            jsonObject = LogMessageJson.getLogMessageJson(Enum.ERROR, Enum.MESSAGE_LESS_PARAMETER, requestName);

        }
        SendJsonData.sendJsonData(request, response, jsonObject);
        dbService.databaseClose();
    }
}
