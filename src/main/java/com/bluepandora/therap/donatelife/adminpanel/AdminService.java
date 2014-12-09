/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluepandora.therap.donatelife.adminpanel;

import com.bluepandora.therap.donatelife.constant.DbUser;
import com.bluepandora.therap.donatelife.constant.Enum;
import com.bluepandora.therap.donatelife.database.DatabaseService;
import com.bluepandora.therap.donatelife.debug.Debug;
import com.bluepandora.therap.donatelife.debug.LogMessageJson;
import com.bluepandora.therap.donatelife.jsonbuilder.RequestNameAdderJson;
import com.bluepandora.therap.donatelife.jsonsender.SendJsonData;
import com.bluepandora.therap.donatelife.validation.DataValidation;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Biswajit Debnath
 */
public class AdminService extends DbUser {

    private static boolean isAccessKeyMatched(String hashKey, DatabaseService dbService) {
        System.out.println("HASH KEY: " + hashKey);
        String query = AdminQuery.getAccessKeyInfoQuery(hashKey);
        Debug.debugLog("KEY USER CHECK QUERY: " + query);
        ResultSet result = dbService.getResultSet(query);
        boolean adminFound = false;
        try {
            while (result.next()) {
                adminFound = true;
            }
        } catch (SQLException error) {
            adminFound = false;
        } catch (Exception error) {
            adminFound = false;
        }
        return adminFound;
    }

    public static void getMobileNumberDetail(HttpServletRequest request, HttpServletResponse response) throws JSONException {
        DatabaseService dbService = new DatabaseService(DRIVER_NAME, DATABASE_URL, USERNAME, PASSWORD);
        dbService.databaseOpen();

        String requestName = request.getParameter("requestName");
        JSONObject jsonObject = null;
        if (request.getParameter("accessKey") != null && request.getParameter("mobileNumber") != null) {
            String accessKey = request.getParameter("accessKey");
            String mobileNumber = request.getParameter("mobileNumber");
            if (DataValidation.isValidMobileNumber(mobileNumber) && DataValidation.isValidKeyWord(accessKey)) {
                String hashKey = DataValidation.encryptTheKeyWord(accessKey);
                if (isAccessKeyMatched(hashKey, dbService)) {
                    String query = AdminQuery.getMobileDetailQuery(mobileNumber);
                    Debug.debugLog("MobileNumber Detail QUERY : ", query);
                    ResultSet result = dbService.getResultSet(query);
                    jsonObject = JsonBuilder.getMobileDetailJson(result);
                } else {
                    jsonObject = LogMessageJson.getLogMessageJson(Enum.ERROR, Enum.MESSAGE_INVALID_USER);
                }
            } else {
                jsonObject = LogMessageJson.getLogMessageJson(Enum.ERROR, Enum.MESSAGE_INVALID_VALUE);
            }
        } else {
            jsonObject = LogMessageJson.getLogMessageJson(Enum.ERROR, Enum.MESSAGE_LESS_PARAMETER);

        }
        jsonObject = RequestNameAdderJson.setRequestNameInJson(jsonObject, requestName);
        SendJsonData.sendJsonData(request, response, jsonObject);
        dbService.databaseClose();
    }

    public static void getDonatorList(HttpServletRequest request, HttpServletResponse response) throws JSONException {
        DatabaseService dbService = new DatabaseService(DRIVER_NAME, DATABASE_URL, USERNAME, PASSWORD);
        dbService.databaseOpen();
        String requestName = request.getParameter("requestName");
        JSONObject jsonObject = null;
        if (request.getParameter("accessKey") != null) {
            String accessKey = request.getParameter("accessKey");
            String groupName = request.getParameter("groupName");
            String distName = request.getParameter("distName");

            if (DataValidation.isValidKeyWord(accessKey)) {
                String hashKey = DataValidation.encryptTheKeyWord(accessKey);
                if (isAccessKeyMatched(hashKey,dbService)) {
                    String query = AdminQuery.getDonatorListQuery(groupName, distName);
                    Debug.debugLog("DonatorList QUERY : ", query);
                    ResultSet result = dbService.getResultSet(query);
                    jsonObject = JsonBuilder.getDonatorListJson(result);
                } else {
                    jsonObject = LogMessageJson.getLogMessageJson(Enum.ERROR, Enum.MESSAGE_ACCESS_KEY_NOT_MATCHED);
                }
            } else {
                jsonObject = LogMessageJson.getLogMessageJson(Enum.ERROR, Enum.MESSAGE_INVALID_VALUE);
            }
        } else {
            jsonObject = LogMessageJson.getLogMessageJson(Enum.ERROR, Enum.MESSAGE_LESS_PARAMETER);
        }

        jsonObject = RequestNameAdderJson.setRequestNameInJson(jsonObject, requestName);
        SendJsonData.sendJsonData(request, response, jsonObject);
        dbService.databaseClose();
    }

    public static void getAdminList(HttpServletRequest request, HttpServletResponse response) throws JSONException {
        DatabaseService dbService = new DatabaseService(DRIVER_NAME, DATABASE_URL, USERNAME, PASSWORD);
        dbService.databaseOpen();
        String requestName = request.getParameter("requestName");
        JSONObject jsonObject = null;
        if (request.getParameter("accessKey") != null) {
            String accessKey = request.getParameter("accessKey");
            if (DataValidation.isValidKeyWord(accessKey)) {

                String hashKey = DataValidation.encryptTheKeyWord(accessKey);
                if (isAccessKeyMatched(hashKey,dbService)) {

                    String query = AdminQuery.getAdminListQuery();
                    Debug.debugLog("AdminList  QUERY : ", query);
                    ResultSet result = dbService.getResultSet(query);
                    jsonObject = JsonBuilder.getAdminListJson(result);
                } else {
                    jsonObject = LogMessageJson.getLogMessageJson(Enum.ERROR, Enum.MESSAGE_ACCESS_KEY_NOT_MATCHED);
                }
            } else {
                jsonObject = LogMessageJson.getLogMessageJson(Enum.ERROR, Enum.MESSAGE_INVALID_VALUE);
            }
        } else {
            jsonObject = LogMessageJson.getLogMessageJson(Enum.ERROR, Enum.MESSAGE_LESS_PARAMETER);
        }
        jsonObject = RequestNameAdderJson.setRequestNameInJson(jsonObject, requestName);
        SendJsonData.sendJsonData(request, response, jsonObject);
        dbService.databaseClose();
    }

    public static void getFeedBackList(HttpServletRequest request, HttpServletResponse response) throws JSONException {
        DatabaseService dbService = new DatabaseService(DRIVER_NAME, DATABASE_URL, USERNAME, PASSWORD);
        dbService.databaseOpen();
        String requestName = request.getParameter("requestName");
        JSONObject jsonObject = null;
        if (request.getParameter("accessKey") != null) {
            String accessKey = request.getParameter("accessKey");
            if (DataValidation.isValidKeyWord(accessKey)) {
                String hashKey = DataValidation.encryptTheKeyWord(accessKey);
                if (isAccessKeyMatched(hashKey,dbService)) {

                    String query = AdminQuery.getFeedBackQuery();
                    Debug.debugLog("FeedBack QUERY : ", query);
                    ResultSet result = dbService.getResultSet(query);
                    jsonObject = JsonBuilder.getFeedBackJson(result);
                } else {
                    jsonObject = LogMessageJson.getLogMessageJson(Enum.ERROR, Enum.MESSAGE_ACCESS_KEY_NOT_MATCHED);
                }
            } else {
                jsonObject = LogMessageJson.getLogMessageJson(Enum.ERROR, Enum.MESSAGE_INVALID_VALUE);
            }
        } else {
            jsonObject = LogMessageJson.getLogMessageJson(Enum.ERROR, Enum.MESSAGE_LESS_PARAMETER);
        }

        jsonObject = RequestNameAdderJson.setRequestNameInJson(jsonObject, requestName);
        SendJsonData.sendJsonData(request, response, jsonObject);
        dbService.databaseClose();
    }
}
