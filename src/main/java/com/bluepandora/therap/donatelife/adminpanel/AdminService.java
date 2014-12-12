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

        if (request.getParameter("idUser") != null && request.getParameter("accessKey") != null) {
            String idUser = request.getParameter("idUser");
            String accessKey = request.getParameter("accessKey");
            idUser = idUser.replace(" ", "");
            accessKey = accessKey.replace(" ", "");
            String hashKey = DataValidation.encryptTheKeyWord(accessKey);
            String query = AdminQuery.adminLoginQuery(idUser, hashKey);
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

    public static void addHospital(HttpServletRequest request, HttpServletResponse response) throws JSONException {
        DatabaseService dbService = new DatabaseService(DRIVER_NAME, DATABASE_URL, USERNAME, PASSWORD);
        dbService.databaseOpen();
        String requestName = request.getParameter("requestName");
        JSONObject jsonObject = null;
        if (request.getParameter("distId") != null && request.getParameter("hospitalName") != null && request.getParameter("hospitalBName") != null) {
            String distId = request.getParameter("distId");
            String hospitalName = request.getParameter("hospitalName");
            String hospitalBName = request.getParameter("hospitalBName");

            String query = AdminQuery.addHospitalQuery(distId, hospitalName, hospitalBName);
            boolean done = dbService.queryExcute(query);
            if (done) {
                jsonObject = LogMessageJson.getLogMessageJson(Enum.CORRECT, Enum.MESSAGE_HOSPITAL_ADDED, requestName);
            } else {
                jsonObject = LogMessageJson.getLogMessageJson(Enum.ERROR, Enum.MESSAGE_ERROR, requestName);
            }
        } else {
            jsonObject = LogMessageJson.getLogMessageJson(Enum.ERROR, Enum.MESSAGE_LESS_PARAMETER, requestName);
        }

        SendJsonData.sendJsonData(request, response, jsonObject);
        dbService.databaseClose();
    }

    public static void removeHospital(HttpServletRequest request, HttpServletResponse response) throws JSONException {
        DatabaseService dbService = new DatabaseService(DRIVER_NAME, DATABASE_URL, USERNAME, PASSWORD);
        dbService.databaseOpen();
        String requestName = request.getParameter("requestName");
        JSONObject jsonObject = null;
        if (request.getParameter("hospitalId") != null) {
            String hospitalId = request.getParameter("hospitalId");
            String query = AdminQuery.removeHospitalQuery(hospitalId);
            boolean done = dbService.queryExcute(query);
            if (done) {
                jsonObject = LogMessageJson.getLogMessageJson(Enum.CORRECT, Enum.MESSAGE_HOSPITAL_REMOVED, requestName);
            } else {
                jsonObject = LogMessageJson.getLogMessageJson(Enum.ERROR, Enum.MESSAGE_ERROR, requestName);
            }
        } else {
            jsonObject = LogMessageJson.getLogMessageJson(Enum.ERROR, Enum.MESSAGE_LESS_PARAMETER, requestName);
        }
        SendJsonData.sendJsonData(request, response, jsonObject);
        dbService.databaseClose();
    }
    
    public static void removeFeedBack(HttpServletRequest request, HttpServletResponse response) throws JSONException {
        DatabaseService dbService = new DatabaseService(DRIVER_NAME, DATABASE_URL, USERNAME, PASSWORD);
        dbService.databaseOpen();
        String requestName = request.getParameter("requestName");
        JSONObject jsonObject = null;
        if (request.getParameter("idUser") != null && request.getParameter("reqTime")!=null) {
            String idUser = request.getParameter("idUser");
            String reqTime = request.getParameter("reqTime");
            String query = AdminQuery.removeFeedBackQuery(idUser, reqTime);
            boolean done = dbService.queryExcute(query);
            if (done) {
                jsonObject = LogMessageJson.getLogMessageJson(Enum.CORRECT, Enum.MESSAGE_FEEDBACK_REMOVED, requestName);
            } else {
                jsonObject = LogMessageJson.getLogMessageJson(Enum.ERROR, Enum.MESSAGE_ERROR, requestName);
            }
        } else {
            jsonObject = LogMessageJson.getLogMessageJson(Enum.ERROR, Enum.MESSAGE_LESS_PARAMETER, requestName);
        }
        SendJsonData.sendJsonData(request, response, jsonObject);
        dbService.databaseClose();
    }
}
