/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluepandora.therap.donatelife.adminpanel;

import com.bluepandora.therap.donatelife.debug.Debug;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Biswajit Debnath
 */
public class JsonBuilder {

    private static final String jsDONE = "done";
    private static final String jsMobileDetailList = "details";
    private static final String jsDonatorList = "donators";
    private static final String jsfeedBackList = "feedbacks";
    private static final String jsAdminList = "admins";

    private static final String jsMobileNumber = "mobileNumber";
    private static final String jsAdminName = "adminName";
    private static final String jsEmail = "email";
    private static final String jsDistName = "distName";
    private static final String jsGroupName = "groupName";
    private static final String jsFirstName = "firstName";
    private static final String jsLastName = "lastName";
    private static final String jsGcmId = "gcmId";
    private static final String jsKeyWord = "keyWord";
    private static final String jsIdUser = "idUser";
    private static final String jsReqTime = "reqTime";
    private static final String jsSubject = "subject";
    private static final String jsComment = "comment";
    private static final String jsHospitalId = "hospitalId";
    private static final String jsHospitalName = "hospitalName";
    private static final String jsHospitalBName = "hospitalBName";

    private static final String dbMobileNumber = "mobile_number";
    private static final String dbAdminName = "admin_name";
    private static final String dbEmail = "email";
    private static final String dbDistName = "dist_name";
    private static final String dbGroupName = "group_name";
    private static final String dbFirstName = "first_name";
    private static final String dbLastName = "last_name";
    private static final String dbGcmId = "gcm_id";
    private static final String dbKeyWord = "key_word";
    private static final String dbIdUser = "id_user";
    private static final String dbReqTime = "req_time";
    private static final String dbSubject = "subject";
    private static final String dbComment = "comment";
    private static final String dbHospitalId = "hospital_id";
    private static final String dbHospitalName = "hospital_name";
    private static final String dbHospitalBName = "hospital_bname";

    public static JSONObject getDonatorListJson(ResultSet result) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject;
        try {
            while (result.next()) {
                jsonObject = new JSONObject();
                jsonObject.put(jsMobileNumber, result.getString(dbMobileNumber));
                jsonObject.put(jsFirstName, result.getString(dbFirstName));
                jsonObject.put(jsLastName, result.getString(dbLastName));
                jsonObject.put(jsGroupName, result.getString(dbGroupName));
                jsonObject.put(jsDistName, result.getString(dbDistName));
                jsonObject.put(jsGcmId, result.getString(dbGcmId));
                jsonArray.put(jsonObject);
            }
            jsonObject = new JSONObject();
            jsonObject.put(jsDonatorList, jsonArray);
            jsonObject.put(jsDONE, 1);
        } catch (SQLException error) {
            Debug.debugLog("DONATOR RESULT SET: ", error);
            jsonObject = new JSONObject();
            jsonObject.put(jsDONE, 0);
        }
        return jsonObject;
    }

    public static JSONObject getAdminListJson(ResultSet result) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject;
        try {
            while (result.next()) {
                jsonObject = new JSONObject();
                jsonObject.put(jsFirstName, result.getString(dbFirstName));
                jsonObject.put(jsLastName, result.getString(dbLastName));
                jsonObject.put(jsMobileNumber, result.getString(dbMobileNumber));
                jsonObject.put(jsEmail, result.getString(dbEmail));
                jsonArray.put(jsonObject);
            }
            jsonObject = new JSONObject();
            jsonObject.put(jsAdminList, jsonArray);
            jsonObject.put(jsDONE, 1);
        } catch (SQLException error) {
            Debug.debugLog("ADMIN RESULT SET: ", error);
            jsonObject = new JSONObject();
            jsonObject.put(jsDONE, 0);
        }
        return jsonObject;
    }

    public static JSONObject getFeedBackJson(ResultSet result) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject;
        try {

            while (result.next()) {
                jsonObject = new JSONObject();
                jsonObject.put(jsIdUser, result.getString(dbIdUser));
                jsonObject.put(jsReqTime, result.getString(dbReqTime));
                jsonObject.put(jsSubject, result.getString(dbSubject));
                jsonObject.put(jsComment, result.getString(dbComment));
                jsonArray.put(jsonObject);
            }

            jsonObject = new JSONObject();
            jsonObject.put(jsfeedBackList, jsonArray);
            jsonObject.put(jsDONE, 1);
        } catch (SQLException error) {
            Debug.debugLog("FEEDBACK RESULT SET: ", error);
            jsonObject = new JSONObject();
            jsonObject.put(jsDONE, 0);
        }
        return jsonObject;
    }

    public static JSONObject getHospitalListJson(ResultSet result) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = null;
        try {
            while (result.next()) {
                jsonObject = new JSONObject();
                jsonObject.put(jsHospitalId, result.getString(dbHospitalId));
                jsonObject.put(jsDistName, result.getString(dbDistName));
                jsonObject.put(jsHospitalName, result.getString(dbHospitalName));
                jsonObject.put(jsHospitalBName, result.getString(dbHospitalBName));
                jsonArray.put(jsonObject);
            }

            if (jsonArray.length() != 0) {
                jsonObject = new JSONObject();
                jsonObject.put("hospitalList", jsonArray);
                jsonObject.put(jsDONE, 1);
            } else {
                jsonObject = new JSONObject();
                jsonObject.put("message", "NO HOSPITAL LIST FOUND!");
                jsonObject.put(jsDONE, 0);
            }
        } catch (SQLException error) {
            Debug.debugLog("Hospital List: ", error);
            jsonObject = new JSONObject();
            jsonObject.put(jsDONE, 0);
        }
        return jsonObject;
    }
    
    public static JSONObject adminProfile(ResultSet result) throws JSONException{
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = null;
        try {
            while (result.next()) {
                jsonObject = new JSONObject();
                jsonObject.put(jsFirstName, result.getString(dbFirstName));
                jsonObject.put(jsLastName, result.getString(dbLastName));
                jsonObject.put(jsEmail, result.getString(dbEmail));
                jsonObject.put(jsMobileNumber, result.getString(dbMobileNumber));
                jsonArray.put(jsonObject);
            }

            if (jsonArray.length() != 0) {
                jsonObject = new JSONObject();
                jsonObject.put("admin", jsonArray);
                jsonObject.put(jsDONE, 1);
            } else {
                jsonObject = new JSONObject();
                jsonObject.put("message", "NO VALID ADMIN FOUND!");
                jsonObject.put(jsDONE, 0);
            }
        } catch (SQLException error) {
            Debug.debugLog("Admin SQL : ", error);
            jsonObject = new JSONObject();
            jsonObject.put(jsDONE, 0);
        }
        return jsonObject;
    }
}
