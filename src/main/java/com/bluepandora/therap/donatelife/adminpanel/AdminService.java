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
import com.bluepandora.therap.donatelife.jsonperser.RequestNameAdderJson;
import com.bluepandora.therap.donatelife.jsonsender.SendJsonData;
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
public class AdminService {
    
    private static DatabaseService dbService = new DatabaseService(
            DbUser.DATABASETYPE,
            DbUser.DATABASEURL,
            DbUser.USERNAME,
            DbUser.PASSWORD
    );
    
    private static boolean isAccessKeyMatched(String accessKey) {
        String query = AdminQuery.getAccessKeyInfoQuery(accessKey);
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
        String requestName = request.getParameter("requestName");
        
        if (request.getParameter("accessKey") != null && request.getParameter("mobileNumber") != null) {
            String accessKey = request.getParameter("accessKey");
            String mobileNumber = request.getParameter("mobileNumber");
            if (isAccessKeyMatched(accessKey)) {
                String query = AdminQuery.getMobileDetailQuery(mobileNumber);
                Debug.debugLog("MobileNumber Detail QUERY : ", query);
                ResultSet result = dbService.getResultSet(query);
                JSONObject jsonObject = JsonBuilder.getMobileDetailJson(result);
                jsonObject = RequestNameAdderJson.setRequestNameInJson(jsonObject, requestName);
                SendJsonData.sendJsonData(request, response, jsonObject);
            } else {
                JSONObject jsonObject = LogMessageJson.getLogMessageJson(Enum.ERROR, Enum.MESSAGE_ACCESS_KEY_NOT_MATCHED);
                jsonObject = RequestNameAdderJson.setRequestNameInJson(jsonObject, requestName);
                SendJsonData.sendJsonData(request, response, jsonObject);
            }
            
        } else {
            JSONObject jsonObject = LogMessageJson.getLogMessageJson(Enum.ERROR, Enum.MESSAGE_LESS_PARAMETER);
            jsonObject = RequestNameAdderJson.setRequestNameInJson(jsonObject, requestName);
            SendJsonData.sendJsonData(request, response, jsonObject);
        }
    }
    
    public static void getDonatorList(HttpServletRequest request, HttpServletResponse response) throws JSONException {
        String requestName = request.getParameter("requestName");
        
        if (request.getParameter("distName")!=null
                && request.getParameter("accessKey") != null
                && request.getParameter("groupName")!=null) {
            
            String accessKey = request.getParameter("accessKey");
            String groupName = request.getParameter("groupName");
            String distName = request.getParameter("distName");
            
            Debug.debugLog("GroupName: ", groupName, "Dist: ", distName);
            
            if (isAccessKeyMatched(accessKey)) {
                
                String query = AdminQuery.getDonatorListQuery(groupName, distName);
                Debug.debugLog("DonatorList QUERY : ", query);
                ResultSet result = dbService.getResultSet(query);
                JSONObject jsonObject = JsonBuilder.getDonatorListJson(result);
                jsonObject = RequestNameAdderJson.setRequestNameInJson(jsonObject, requestName);
                SendJsonData.sendJsonData(request, response, jsonObject);
                
            } else {
                JSONObject jsonObject = LogMessageJson.getLogMessageJson(Enum.ERROR, Enum.MESSAGE_ACCESS_KEY_NOT_MATCHED);
                jsonObject = RequestNameAdderJson.setRequestNameInJson(jsonObject, requestName);
                SendJsonData.sendJsonData(request, response, jsonObject);
            }
        } else {
            JSONObject jsonObject = LogMessageJson.getLogMessageJson(Enum.ERROR, Enum.MESSAGE_LESS_PARAMETER);
            jsonObject = RequestNameAdderJson.setRequestNameInJson(jsonObject, requestName);
            SendJsonData.sendJsonData(request, response, jsonObject);
        }
    }
    
    public static void getAdminList(HttpServletRequest request, HttpServletResponse response) throws JSONException {
        String requestName = request.getParameter("requestName");
        if (request.getParameter("accessKey") != null) {
            
            String accessKey = request.getParameter("accessKey");
            
            if (isAccessKeyMatched(accessKey)) {
                
                String query = AdminQuery.getAdminListQuery();
                Debug.debugLog("AdminList  QUERY : ", query);
                ResultSet result = dbService.getResultSet(query);
                JSONObject jsonObject = JsonBuilder.getAdminListJson(result);
                jsonObject = RequestNameAdderJson.setRequestNameInJson(jsonObject, requestName);
                SendJsonData.sendJsonData(request, response, jsonObject);
                
            } else {
                JSONObject jsonObject = LogMessageJson.getLogMessageJson(Enum.ERROR, Enum.MESSAGE_ACCESS_KEY_NOT_MATCHED);
                jsonObject = RequestNameAdderJson.setRequestNameInJson(jsonObject, requestName);
                SendJsonData.sendJsonData(request, response, jsonObject);
            }
        } else {
            JSONObject jsonObject = LogMessageJson.getLogMessageJson(Enum.ERROR, Enum.MESSAGE_LESS_PARAMETER);
            jsonObject = RequestNameAdderJson.setRequestNameInJson(jsonObject, requestName);
            SendJsonData.sendJsonData(request, response, jsonObject);
        }
    }
    
    public static void getFeedBackList(HttpServletRequest request, HttpServletResponse response) throws JSONException {
        String requestName = request.getParameter("requestName");
        if (request.getParameter("accessKey") != null) {
            
            String accessKey = request.getParameter("accessKey");
            
            if (isAccessKeyMatched(accessKey)) {
                
                String query = AdminQuery.getFeedBackQuery();
                Debug.debugLog("FeedBack QUERY : ", query);
                ResultSet result = dbService.getResultSet(query);
                JSONObject jsonObject = JsonBuilder.getFeedBackJson(result);
                jsonObject = RequestNameAdderJson.setRequestNameInJson(jsonObject, requestName);
                SendJsonData.sendJsonData(request, response, jsonObject);
                
            } else {
                JSONObject jsonObject = LogMessageJson.getLogMessageJson(Enum.ERROR, Enum.MESSAGE_ACCESS_KEY_NOT_MATCHED);
                jsonObject = RequestNameAdderJson.setRequestNameInJson(jsonObject, requestName);
                SendJsonData.sendJsonData(request, response, jsonObject);
            }
        } else {
            JSONObject jsonObject = LogMessageJson.getLogMessageJson(Enum.ERROR, Enum.MESSAGE_LESS_PARAMETER);
            jsonObject = RequestNameAdderJson.setRequestNameInJson(jsonObject, requestName);
            SendJsonData.sendJsonData(request, response, jsonObject);
        }
    }
}
