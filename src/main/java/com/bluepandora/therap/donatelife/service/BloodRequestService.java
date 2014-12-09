/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluepandora.therap.donatelife.service;


import  com.bluepandora.therap.donatelife.constant.Enum;
import  com.bluepandora.therap.donatelife.constant.DbUser;
import com.bluepandora.therap.donatelife.database.DatabaseService;
import com.bluepandora.therap.donatelife.database.GetQuery;
import com.bluepandora.therap.donatelife.debug.Debug;
import com.bluepandora.therap.donatelife.debug.LogMessageJson;
import com.bluepandora.therap.donatelife.gcmservice.GcmService;
import com.bluepandora.therap.donatelife.jsonsender.SendJsonData;
import com.bluepandora.therap.donatelife.validation.DataValidation;
import java.text.ParseException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Biswajit Debnath
 */
public class BloodRequestService extends DbUser{

    public static void addBloodRequest(HttpServletRequest request, HttpServletResponse response) throws JSONException, ParseException {
        
        DatabaseService dbService = new DatabaseService(DRIVER_NAME, DATABASE_URL, USERNAME, PASSWORD);
        dbService.databaseOpen();
        
        deleteRequestTracker(Enum.MAX_DAY, dbService);
        
        String requestName = request.getParameter("requestName");
        JSONObject jsonObject;
        Debug.debugLog("RequestName: ", requestName);
        if (request.getParameter("mobileNumber") != null
                && request.getParameter("groupId") != null
                && request.getParameter("hospitalId") != null
                && request.getParameter("amount") != null
                && request.getParameter("emergency") != null
                && request.getParameter("keyWord") != null
                && request.getParameter("reqTime") != null) {

            String mobileNumber = request.getParameter("mobileNumber");
            String groupId = request.getParameter("groupId");
            String hospitalId = request.getParameter("hospitalId");
            String amount = request.getParameter("amount");
            String emergency = request.getParameter("emergency");
            String keyWord = request.getParameter("keyWord");
            String reqTime = request.getParameter("reqTime");
            String date = reqTime.substring(0, 10);

            if (DataValidation.isValidMobileNumber(mobileNumber) && DataValidation.isValidKeyWord(keyWord)) {
                String hashKey = DataValidation.encryptTheKeyWord(keyWord);
                boolean validUser = CheckService.isValidUser(mobileNumber, hashKey, dbService);
                if (validUser) {
                    int userRequest = CheckService.requestTracker(mobileNumber, date, dbService);
                    Debug.debugLog("USER REQUEST: ", userRequest);
                    if (userRequest < com.bluepandora.therap.donatelife.constant.Enum.MAX_REQUEST) {
                        boolean validGroup = CheckService.isDuplicateBloodGroup(mobileNumber, groupId, dbService);
                        Debug.debugLog("VALID GROUP: ", validGroup);
                        if (validGroup) {
                            boolean validHospital = CheckService.isDuplicateHospital(mobileNumber, hospitalId, dbService);
                            if (validHospital) {
                                Debug.debugLog("VALID HOSPITAL: ", validHospital);
                                String query = GetQuery.addBloodRequestQuery(reqTime, mobileNumber, groupId, amount, hospitalId, emergency);
                                boolean done = dbService.queryExcute(query);
                                Debug.debugLog("ADD BR: ", query);
                                if (done) {
                                    query = GetQuery.addBloodRequestTrackerQuery(mobileNumber, reqTime);

                                    Debug.debugLog("REQUEST TRACKER ADDING: ", query);
                                    dbService.queryExcute(query);
                                    GcmService.giveGCMService(request, response, dbService);
                                    jsonObject = LogMessageJson.getLogMessageJson(Enum.CORRECT, Enum.MESSAGE_BLOOD_REQUEST_ADDED, requestName);
                                    Debug.debugLog("MOBILE NUMBER: ", mobileNumber, " ADD BLOOD REQUEST SUCCESS");
                                } else {
                                    jsonObject = LogMessageJson.getLogMessageJson(Enum.ERROR, Enum.MESSAGE_ERROR, requestName);
                                }
                            } else {
                                jsonObject = LogMessageJson.getLogMessageJson(Enum.ERROR, Enum.MESSAGE_DUPLICATE_HOSPITAL, requestName);
                            }
                        } else {
                            jsonObject = LogMessageJson.getLogMessageJson(Enum.ERROR, Enum.MESSAGE_DUPLICATE_BLOOD_GROUP, requestName);
                        }
                    } else {
                        jsonObject = LogMessageJson.getLogMessageJson(Enum.ERROR, Enum.MESSAGE_CROSSED_LIMIT, requestName);
                    }
                } else {
                    jsonObject = LogMessageJson.getLogMessageJson(Enum.ERROR, Enum.MESSAGE_INVALID_USER, requestName);
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

    public static void removePersonBloodRequestTracker(HttpServletRequest request, HttpServletResponse response) throws JSONException {
        DatabaseService dbService = new DatabaseService(DRIVER_NAME, DATABASE_URL, USERNAME, PASSWORD);
        dbService.databaseOpen();
        JSONObject jsonObject;
        String requestName = request.getParameter("requestName");

        if (request.getParameter("mobileNumber") != null) {
            String mobileNumber = request.getParameter("mobileNumber");

            if (DataValidation.isValidMobileNumber(mobileNumber)) {
                String query = GetQuery.removePersonBloodRequestTrackerQuery(mobileNumber);
                dbService.queryExcute(query);
                jsonObject = LogMessageJson.getLogMessageJson(Enum.CORRECT, mobileNumber + Enum.MESSAGE_BLOOD_REQUEST_TRACKER_REMOVED, requestName);
                Debug.debugLog("MOBILE NUMBER: ", mobileNumber, " REMOVE TRACKER SUCCESS");
            } else {
                jsonObject = LogMessageJson.getLogMessageJson(Enum.ERROR, Enum.MESSAGE_INVALID_VALUE, requestName);
            }
        } else {
            jsonObject = LogMessageJson.getLogMessageJson(Enum.ERROR, Enum.MESSAGE_LESS_PARAMETER, requestName);
        }
        SendJsonData.sendJsonData(request, response, jsonObject);
        dbService.databaseClose();
    }

    public static void removeBloodRequest(HttpServletRequest request, HttpServletResponse response) throws JSONException {
        DatabaseService dbService = new DatabaseService(DRIVER_NAME, DATABASE_URL, USERNAME, PASSWORD);
        dbService.databaseOpen();
        String requestName = request.getParameter("requestName");
        JSONObject jsonObject = null;

        if (request.getParameter("mobileNumber") != null && request.getParameter("reqTime") != null) {
            String mobileNumber = request.getParameter("mobileNumber");
            String reqTime = request.getParameter("reqTime");
            if (DataValidation.isValidMobileNumber(mobileNumber) && DataValidation.isValidString(reqTime)) {
                String query = GetQuery.removeBloodRequestQuery(mobileNumber, reqTime);
                // Debug.debugLog("DELETE BLOOD REQUEST QUERY:", query);
                boolean done = dbService.queryExcute(query);
                if (done) {
                    jsonObject = LogMessageJson.getLogMessageJson(Enum.CORRECT, Enum.MESSAGE_REMOVED_BLOOD_REQUEST, requestName);
                    Debug.debugLog("MOBILE NUMBER: ", mobileNumber, " REMOVE BLOOD REQUEST SUCCESS");
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

    public static void deleteRequestTracker(int day, DatabaseService dbService) {
        String query = GetQuery.deleteBloodRequestTrackerQuery(day);
        boolean done = dbService.queryExcute(query);
        if (done) {
            Debug.debugLog("REQUEST TRACKER BEFORE  " + com.bluepandora.therap.donatelife.constant.Enum.MAX_DAY + " DAYS IS DELETED");
        } else {
            Debug.debugLog("REQUEST TRACKER DELETION OCCURS ERROR!");
        }
    }
}
