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
import com.bluepandora.therap.donatelife.jsonbuilder.RequestNameAdderJson;
import com.bluepandora.therap.donatelife.jsonsender.SendJsonData;
import static com.bluepandora.therap.donatelife.service.CheckService.isMobileNumberTaken;
import com.bluepandora.therap.donatelife.validation.DataValidation;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Biswajit Debnath
 */
public class UserRegistrationService extends DbUser{

     public static void registerUser(HttpServletRequest request, HttpServletResponse response) throws JSONException {
        DatabaseService dbService = new DatabaseService(DRIVER_NAME, DATABASE_URL, USERNAME, PASSWORD);
        dbService.databaseOpen();
        String requestName = request.getParameter("requestName");
        JSONObject jsonObject = null;
        if (request.getParameter("firstName") != null
                && request.getParameter("lastName") != null
                && request.getParameter("distId") != null
                && request.getParameter("groupId") != null
                && request.getParameter("keyWord") != null
                && request.getParameter("mobileNumber") != null) {

            String firstName = request.getParameter("firstName").toUpperCase();
            String lastName = request.getParameter("lastName").toUpperCase();
            String distId = request.getParameter("distId");
            String groupId = request.getParameter("groupId");
            String keyWord = request.getParameter("keyWord");
            String mobileNumber = request.getParameter("mobileNumber");

            if (DataValidation.isValidMobileNumber(mobileNumber) && DataValidation.isValidKeyWord(keyWord)) {
                String hashKey = DataValidation.encryptTheKeyWord(keyWord);
                boolean mobileNumberTaken = CheckService.isMobileNumberTaken(mobileNumber, dbService);
                if (mobileNumberTaken == false) {
                    AddPersonName.addPersonName(firstName, lastName, dbService);
                    String query = GetQuery.addPersonInfo(mobileNumber, groupId, distId, hashKey, firstName, lastName);
                    boolean done = dbService.queryExcute(query);
                    if (done) {
                        jsonObject = LogMessageJson.getLogMessageJson(Enum.CORRECT, Enum.MESSAGE_REG_SUCCESS);
                        Debug.debugLog("MOBILE NUMBER: ", mobileNumber, " REG SUCCESS");
                    } else {
                        jsonObject = LogMessageJson.getLogMessageJson(Enum.ERROR, Enum.MESSAGE_ERROR);
                    }
                } else {
                    jsonObject = LogMessageJson.getLogMessageJson(Enum.ERROR, Enum.MESSAGE_MOBILE_NUMBER_TAKEN);
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

   public static void userRegistrationCheck(HttpServletRequest request, HttpServletResponse response) throws JSONException {
        DatabaseService dbService = new DatabaseService(DRIVER_NAME, DATABASE_URL, USERNAME, PASSWORD);
        dbService.databaseOpen();
        JSONObject jsonObject;
        String requestName = request.getParameter("requestName");

        if (request.getParameter("mobileNumber") != null) {
            String mobileNumber = request.getParameter("mobileNumber");

            if (DataValidation.isValidMobileNumber(mobileNumber)) {
                boolean mobileNumberTaken = isMobileNumberTaken(mobileNumber, dbService);
                if (mobileNumberTaken) {
                    jsonObject = LogMessageJson.getLogMessageJson("requestName", requestName, "reg", Enum.CORRECT, "done", Enum.CORRECT);
                    Debug.debugLog("MOBILE NUMBER: ", mobileNumber, " REG CHECKING SUCCESS");
                } else {
                    jsonObject = LogMessageJson.getLogMessageJson("requestName", requestName, "reg", Enum.ERROR, "done", Enum.CORRECT);
                }
            } else {
                jsonObject = LogMessageJson.getLogMessageJson("requestName", requestName, "done", Enum.ERROR, "message", Enum.MESSAGE_INVALID_VALUE);
            }
        } else {
            jsonObject = LogMessageJson.getLogMessageJson("requestName", requestName, "done", Enum.ERROR, "message", Enum.MESSAGE_LESS_PARAMETER);
        }

        jsonObject = RequestNameAdderJson.setRequestNameInJson(jsonObject, requestName);
        SendJsonData.sendJsonData(request, response, jsonObject);
        dbService.databaseClose();
    }
    
    public static void updateUserPersonalInfo(HttpServletRequest request, HttpServletResponse response) throws JSONException {
        DatabaseService dbService = new DatabaseService(DRIVER_NAME, DATABASE_URL, USERNAME, PASSWORD);
        dbService.databaseOpen();
        String requestName = request.getParameter("requestName");
        JSONObject jsonObject = null;

        if (request.getParameter("mobileNumber") != null
                && request.getParameter("groupId") != null
                && request.getParameter("distId") != null
                && request.getParameter("firstName") != null
                && request.getParameter("lastName") != null
                && request.getParameter("keyWord") != null) {

            String firstName = request.getParameter("firstName").toUpperCase();
            String lastName = request.getParameter("lastName").toUpperCase();
            String distId = request.getParameter("distId");
            String groupId = request.getParameter("groupId");
            String keyWord = request.getParameter("keyWord");
            String mobileNumber = request.getParameter("mobileNumber");

            if (DataValidation.isValidMobileNumber(mobileNumber) && DataValidation.isValidKeyWord(keyWord)) {

                String hashKey = DataValidation.encryptTheKeyWord(keyWord);
                boolean validUser = CheckService.isValidUser(mobileNumber, hashKey, dbService);
                if (validUser) {
                    AddPersonName.addPersonName(firstName, lastName, dbService);
                    String query = GetQuery.updatePersonInfoQuery(mobileNumber, hashKey, firstName, lastName, groupId, distId);
                    boolean done = dbService.queryExcute(query);
                    if (done) {
                        jsonObject = LogMessageJson.getLogMessageJson(Enum.CORRECT, Enum.MESSAGE_INFO_UPDATED);
                        Debug.debugLog("MOBILE NUMBER: ", mobileNumber, " UPDATE USER PROFILE SCCEUSS");
                    } else {
                        jsonObject = LogMessageJson.getLogMessageJson(Enum.ERROR, Enum.MESSAGE_ERROR);
                    }
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
}
