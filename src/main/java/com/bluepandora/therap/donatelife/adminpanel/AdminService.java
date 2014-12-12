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

    public static void adminLogin(HttpServletRequest request, HttpServletResponse response) throws JSONException {
        DatabaseService dbService = new DatabaseService(DRIVER_NAME, DATABASE_URL, USERNAME, PASSWORD);
        dbService.databaseOpen();
        String requestName = request.getParameter("requestName");
        JSONObject jsonObject = null;

        if (request.getParameter("username") != null && request.getParameter("accessKey") != null) {
            String username = request.getParameter("username");
            String accessKey = request.getParameter("accessKey");
            username = username.replace(" ", "");
            accessKey = accessKey.replace(" ", "");
            String hashKey = DataValidation.encryptTheKeyWord(accessKey);
            String query = AdminQuery.adminLoginQuery(username, hashKey);
            ResultSet result = dbService.getResultSet(query);
            jsonObject = JsonBuilder.adminProfile(result);
        } else {
            jsonObject = LogMessageJson.getLogMessageJson(Enum.ERROR, Enum.MESSAGE_LESS_PARAMETER);
        }
        jsonObject = RequestNameAdderJson.setRequestNameInJson(jsonObject, requestName);
        SendJsonData.sendJsonData(request, response, jsonObject);
        dbService.databaseClose();
    }

    public static void getHospitalList(HttpServletRequest request, HttpServletResponse response) throws JSONException {
        DatabaseService dbService = new DatabaseService(DRIVER_NAME, DATABASE_URL, USERNAME, PASSWORD);
        dbService.databaseOpen();
        String requestName = request.getParameter("requestName");
        JSONObject jsonObject = null;
        String query = AdminQuery.getHospitalListQuery();
        Debug.debugLog("HospitalList QUERY : ", query);
        ResultSet result = dbService.getResultSet(query);
        jsonObject = JsonBuilder.getHospitalListJson(result);
        jsonObject = RequestNameAdderJson.setRequestNameInJson(jsonObject, requestName);
        SendJsonData.sendJsonData(request, response, jsonObject);
        dbService.databaseClose();
    }

    public static void getDonatorList(HttpServletRequest request, HttpServletResponse response) throws JSONException {
        DatabaseService dbService = new DatabaseService(DRIVER_NAME, DATABASE_URL, USERNAME, PASSWORD);
        dbService.databaseOpen();
        String requestName = request.getParameter("requestName");
        JSONObject jsonObject = null;
        String query = AdminQuery.getDonatorListQuery();
        Debug.debugLog("DonatorList QUERY : ", query);
        ResultSet result = dbService.getResultSet(query);
        jsonObject = JsonBuilder.getDonatorListJson(result);
        jsonObject = RequestNameAdderJson.setRequestNameInJson(jsonObject, requestName);
        SendJsonData.sendJsonData(request, response, jsonObject);
        dbService.databaseClose();
    }

    public static void getAdminList(HttpServletRequest request, HttpServletResponse response) throws JSONException {
        DatabaseService dbService = new DatabaseService(DRIVER_NAME, DATABASE_URL, USERNAME, PASSWORD);
        dbService.databaseOpen();
        String requestName = request.getParameter("requestName");
        JSONObject jsonObject = null;
        String query = AdminQuery.getAdminListQuery();
        Debug.debugLog("AdminList  QUERY : ", query);
        ResultSet result = dbService.getResultSet(query);
        jsonObject = JsonBuilder.getAdminListJson(result);
        jsonObject = RequestNameAdderJson.setRequestNameInJson(jsonObject, requestName);
        SendJsonData.sendJsonData(request, response, jsonObject);
        dbService.databaseClose();
    }

    public static void getFeedBackList(HttpServletRequest request, HttpServletResponse response) throws JSONException {
        DatabaseService dbService = new DatabaseService(DRIVER_NAME, DATABASE_URL, USERNAME, PASSWORD);
        dbService.databaseOpen();
        String requestName = request.getParameter("requestName");
        JSONObject jsonObject = null;
        String query = AdminQuery.getFeedBackQuery();
        Debug.debugLog("FeedBack QUERY : ", query);
        ResultSet result = dbService.getResultSet(query);
        jsonObject = JsonBuilder.getFeedBackJson(result);
        jsonObject = RequestNameAdderJson.setRequestNameInJson(jsonObject, requestName);
        SendJsonData.sendJsonData(request, response, jsonObject);
        dbService.databaseClose();
    }
}
