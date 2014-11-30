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
import com.bluepandora.therap.donatelife.jsonperser.BloodGroupJson;
import com.bluepandora.therap.donatelife.jsonperser.BloodRequestJson;
import com.bluepandora.therap.donatelife.jsonperser.DistrictJson;
import com.bluepandora.therap.donatelife.jsonperser.DonationRecordJson;
import com.bluepandora.therap.donatelife.debug.LogMessageJson;
import com.bluepandora.therap.donatelife.jsonperser.HospitalJson;
import com.bluepandora.therap.donatelife.jsonperser.RequestNameAdderJson;
import com.bluepandora.therap.donatelife.jsonsender.SendJsonData;
import com.bluepandora.therap.donatelife.jsonperser.UserProfileJson;
import com.bluepandora.therap.donatelife.validation.DataValidation;

import java.io.IOException;
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
public class DataService {

    private static DatabaseService dbService = new DatabaseService(
            DbUser.DRIVER_NAME,
            DbUser.DATABASEURL,
            DbUser.USERNAME,
            DbUser.PASSWORD
    );

    public static void getBloodGroupList(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, JSONException {

        String query = GetQuery.getBloodGroupListQuery();
        ResultSet result = dbService.getResultSet(query);
        JSONObject jsonObject = BloodGroupJson.getJsonBloodGroup(result);
        jsonObject = RequestNameAdderJson.setRequestNameInJson(jsonObject, request.getParameter("requestName"));
        SendJsonData.sendJsonData(request, response, jsonObject);

    }

    public static void getHospitalList(HttpServletRequest request, HttpServletResponse response) throws JSONException {

        String query = GetQuery.getHospitalListQuery();
        ResultSet result = dbService.getResultSet(query);
        JSONObject jsonObject = HospitalJson.getHospitalJson(result);
        jsonObject = RequestNameAdderJson.setRequestNameInJson(jsonObject, request.getParameter("requestName"));
        SendJsonData.sendJsonData(request, response, jsonObject);
    }

    public static void getDistrictList(HttpServletRequest request, HttpServletResponse response) throws JSONException {
        String query = GetQuery.getDistrictListQuery();
        ResultSet result = dbService.getResultSet(query);
        JSONObject jsonObject = DistrictJson.getDistrictJson(result);
        jsonObject = RequestNameAdderJson.setRequestNameInJson(jsonObject, request.getParameter("requestName"));
        SendJsonData.sendJsonData(request, response, jsonObject);
    }

    public static void getBloodRequestList(HttpServletRequest request, HttpServletResponse response) throws JSONException {
        String query = GetQuery.getBloodRequestListQuery();
        ResultSet result = dbService.getResultSet(query);
        JSONObject jsonObject = BloodRequestJson.getBloodRequestJson(result);
        jsonObject = RequestNameAdderJson.setRequestNameInJson(jsonObject, request.getParameter("requestName"));
        SendJsonData.sendJsonData(request, response, jsonObject);
    }

    public static void getUserProfile(HttpServletRequest request, HttpServletResponse response) throws JSONException {

        if (request.getParameter("mobileNumber") != null && request.getParameter("keyWord") != null) {
            String mobileNumber = request.getParameter("mobileNumber");
            String keyWord = request.getParameter("keyWord");

            if (DataValidation.isValidMobileNumber(mobileNumber) && DataValidation.isValidKeyWord(keyWord)) {
                String query = GetQuery.getUserProfileQuery(mobileNumber, keyWord);
                Debug.debugLog("UserProfile: ", query);
                ResultSet result = dbService.getResultSet(query);
                JSONObject jsonObject = UserProfileJson.getUserProfileJson(result);
                jsonObject = RequestNameAdderJson.setRequestNameInJson(jsonObject, request.getParameter("requestName"));
                SendJsonData.sendJsonData(request, response, jsonObject);

            } else {
                JSONObject jsonObject = LogMessageJson.getLogMessageJson(Enum.ERROR, Enum.MESSAGE_INVALID_VALUE);
                jsonObject = RequestNameAdderJson.setRequestNameInJson(jsonObject, request.getParameter("requestName"));
                SendJsonData.sendJsonData(request, response, jsonObject);
            }

        } else {
            JSONObject jsonObject = LogMessageJson.getLogMessageJson(Enum.ERROR, Enum.MESSAGE_LESS_PARAMETER);
            jsonObject = RequestNameAdderJson.setRequestNameInJson(jsonObject, request.getParameter("requestName"));
            SendJsonData.sendJsonData(request, response, jsonObject);
        }
    }

    public static void getUserDonationRecord(HttpServletRequest request, HttpServletResponse response) throws JSONException {


        if (request.getParameter("mobileNumber") != null) {
            String mobileNumber = request.getParameter("mobileNumber");

            if (DataValidation.isValidMobileNumber(mobileNumber)) {
               
                String query = GetQuery.getDonationRecordQuery(mobileNumber);
                ResultSet result = dbService.getResultSet(query);
                JSONObject jsonObject = DonationRecordJson.getDonationRecordJson(result);
                jsonObject = RequestNameAdderJson.setRequestNameInJson(jsonObject, request.getParameter("requestName"));
                SendJsonData.sendJsonData(request, response, jsonObject);
                
            } else {
                JSONObject jsonObject = LogMessageJson.getLogMessageJson(Enum.ERROR, Enum.MESSAGE_INVALID_VALUE);
                jsonObject = RequestNameAdderJson.setRequestNameInJson(jsonObject, request.getParameter("requestName"));
                SendJsonData.sendJsonData(request, response, jsonObject);
            }
            
        } else {
            JSONObject jsonObject = LogMessageJson.getLogMessageJson(Enum.ERROR, Enum.MESSAGE_LESS_PARAMETER);
            jsonObject = RequestNameAdderJson.setRequestNameInJson(jsonObject, request.getParameter("requestName"));
            SendJsonData.sendJsonData(request, response, jsonObject);
        }
    }

    public static void getDonatorMobileNumber(HttpServletRequest request, HttpServletResponse response) {
        String requestName = request.getParameter("requestName");
        
        if (request.getParameter("mobileNumber") != null
                && request.getParameter("keyWord") != null
                && request.getParameter("groupId") != null
                && request.getParameter("distId") != null
                && request.getParameter("numDonator") != null) {
            
            String mobileNumber = request.getParameter("mobileNumber");
            String keyWord = request.getParameter("keyWord");
            String groupId = request.getParameter("groupId");
            String distId = request.getParameter("distId");
            String numDonator = request.getParameter("numDonator");

            if (DataValidation.isValidMobileNumber(mobileNumber)
                && DataValidation.isValidKeyWord(keyWord)
                && DataValidation.isValidString(groupId)
                && DataValidation.isValidString(distId)
                && DataValidation.isValidString(numDonator)) {
                boolean validUser = CheckService.isValidUser(mobileNumber, keyWord);
                if (validUser) {

                } else {

                }
            }else{
                
            }
        } else {
            
        }
    }

    public static void unknownHit(HttpServletRequest request, HttpServletResponse response) throws JSONException {
        JSONObject jsonObject = LogMessageJson.getLogMessageJson(Enum.ERROR, "UNKNOWN REQUEST TO SERVER!");
        SendJsonData.sendJsonData(request, response, jsonObject);
    }

}
