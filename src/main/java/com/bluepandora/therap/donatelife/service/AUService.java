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
import com.bluepandora.therap.donatelife.gcmservice.GcmService;
import com.bluepandora.therap.donatelife.jsonperser.RequestNameAdderJson;
import com.bluepandora.therap.donatelife.jsonsender.SendJsonData;
import static com.bluepandora.therap.donatelife.service.CheckService.isMobileNumberTaken;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Biswajit Debnath
 */
public class AUService {

    private static DatabaseService dbService = new DatabaseService(
            DbUser.DRIVER_NAME,
            DbUser.DATABASEURL,
            DbUser.USERNAME,
            DbUser.PASSWORD
    );

    private static void addPersonName(String firstName, String lastName) {
        boolean nameTaken = CheckService.isNameAlreadyAdded(firstName, lastName);
        if (nameTaken == false) {
            String query = GetQuery.addPersonNameQuery(firstName, lastName);
            dbService.queryExcute(query);
        }
    }

    public static void registerUser(HttpServletRequest request, HttpServletResponse response) throws JSONException {

        String requestName = request.getParameter("requestName");

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

            boolean mobileNumberTaken = CheckService.isMobileNumberTaken(mobileNumber);

            if (mobileNumberTaken == false) {
                addPersonName(firstName, lastName);
                String query = GetQuery.addPersonInfo(mobileNumber, groupId, distId, keyWord, firstName, lastName);
                boolean done = dbService.queryExcute(query);
                if (done) {
                    JSONObject jsonObject = LogMessageJson.getLogMessageJson(Enum.CORRECT, Enum.MESSAGE_REG_SUCCESS);
                    jsonObject = RequestNameAdderJson.setRequestNameInJson(jsonObject, requestName);
                    SendJsonData.sendJsonData(request, response, jsonObject);
                } else {
                    JSONObject jsonObject = LogMessageJson.getLogMessageJson(Enum.ERROR, Enum.MESSAGE_ERROR);
                    jsonObject = RequestNameAdderJson.setRequestNameInJson(jsonObject, requestName);
                    SendJsonData.sendJsonData(request, response, jsonObject);
                }
            } else {
                JSONObject jsonObject = LogMessageJson.getLogMessageJson(Enum.ERROR, Enum.MESSAGE_MOBILE_NUMBER_TAKEN);
                jsonObject = RequestNameAdderJson.setRequestNameInJson(jsonObject, requestName);
                SendJsonData.sendJsonData(request, response, jsonObject);
            }

        } else {
            JSONObject jsonObject = LogMessageJson.getLogMessageJson(Enum.ERROR, Enum.MESSAGE_LESS_PARAMETER);
            jsonObject = RequestNameAdderJson.setRequestNameInJson(jsonObject, requestName);
            SendJsonData.sendJsonData(request, response, jsonObject);
        }
    }

    public static void addFeedback(HttpServletRequest request, HttpServletResponse response) throws JSONException {

        String requestName = request.getParameter("requestName");
        if (request.getParameter("username") != null && request.getParameter("comments") != null) {
            String username = request.getParameter("username");
            String comments = request.getParameter("comments");
            String query = GetQuery.addFeedback(username, comments);
            dbService.queryExcute(query);
            JSONObject jsonObject = LogMessageJson.getLogMessageJson(Enum.CORRECT, Enum.MESSAGE_FEEDBACK_THANKS);
            jsonObject = RequestNameAdderJson.setRequestNameInJson(jsonObject, requestName);
            SendJsonData.sendJsonData(request, response, jsonObject);

        } else {
            JSONObject jsonObject = LogMessageJson.getLogMessageJson(Enum.ERROR, Enum.MESSAGE_LESS_PARAMETER);
            jsonObject = RequestNameAdderJson.setRequestNameInJson(jsonObject, requestName);
            SendJsonData.sendJsonData(request, response, jsonObject);
        }
    }

    public static void updateGCMKey(HttpServletRequest request, HttpServletResponse response) throws JSONException {

        String requestName = request.getParameter("requsetName");
        if (request.getParameter("mobileNumber") != null && request.getParameter("gcmId") != null) {
            String mobileNumber = request.getParameter("mobileNumber");
            String gcmId = request.getParameter("gcmId");

            if (mobileNumber.equals("")) {
                mobileNumber = null;
            }
            if (gcmId.equals("")) {
                gcmId = null;
            }

            if (mobileNumber != null && gcmId != null) {
                String query = GetQuery.updateGCMIdQuery(mobileNumber, gcmId);
                dbService.queryExcute(query);
                JSONObject jsonObject = LogMessageJson.getLogMessageJson(Enum.CORRECT, Enum.MESSAGE_GCM_ID_UPDATED);
                jsonObject = RequestNameAdderJson.setRequestNameInJson(jsonObject, requestName);
                SendJsonData.sendJsonData(request, response, jsonObject);
            } else {
                JSONObject jsonObject = LogMessageJson.getLogMessageJson(Enum.ERROR, Enum.MESSAGE_INVALID_VALUE);
                jsonObject = RequestNameAdderJson.setRequestNameInJson(jsonObject, requestName);
                SendJsonData.sendJsonData(request, response, jsonObject);
            }
        } else {
            JSONObject jsonObject = LogMessageJson.getLogMessageJson(Enum.ERROR, Enum.MESSAGE_LESS_PARAMETER);
            jsonObject = RequestNameAdderJson.setRequestNameInJson(jsonObject, requestName);
            SendJsonData.sendJsonData(request, response, jsonObject);
        }
    }

    public static void userRegistrationCheck(HttpServletRequest request, HttpServletResponse response) throws JSONException {
        JSONObject jsonObject;
        String requestName = request.getParameter("requestName");

        if (request.getParameter("mobileNumber") != null) {
            String mobileNumber = request.getParameter("mobileNumber");
            if (mobileNumber.equals("")) {
                mobileNumber = null;
            }

            if (mobileNumber != null) {
                boolean mobileNumberTaken = isMobileNumberTaken(mobileNumber);
                if (mobileNumberTaken) {
                    jsonObject = LogMessageJson.getLogMessageJson("requestName", requestName, "reg", Enum.CORRECT, "done", Enum.CORRECT);

                } else {
                    jsonObject = LogMessageJson.getLogMessageJson("requestName", requestName, "reg", Enum.ERROR, "done", Enum.CORRECT);

                }
            } else {
                jsonObject = LogMessageJson.getLogMessageJson("requestName", requestName, "done", Enum.ERROR, "message", Enum.MESSAGE_INVALID_VALUE);

            }
        } else {
            jsonObject = LogMessageJson.getLogMessageJson("requestName", requestName, "done", Enum.ERROR, "message", Enum.MESSAGE_LESS_PARAMETER);

        }

        SendJsonData.sendJsonData(request, response, jsonObject);
    }

    public static void addBloodRequest(HttpServletRequest request, HttpServletResponse response) throws JSONException, ParseException {

        String requestName = request.getParameter("requestName");
        JSONObject jsonObject;

        if (request.getParameter("mobileNumber") != null
                && request.getParameter("groupId") != null
                && request.getParameter("hospitalId") != null
                && request.getParameter("amount") != null
                && request.getParameter("emergency") != null
                && request.getParameter("keyWord") != null
                && request.getParameter("reqTime")!=null){

            String mobileNumber = request.getParameter("mobileNumber");
            String groupId = request.getParameter("groupId");
            String hospitalId = request.getParameter("hospitalId");
            String amount = request.getParameter("amount");
            String emergency = request.getParameter("emergency");
            String keyWord = request.getParameter("keyWord");
            String reqTime = request.getParameter("reqTime");
            String date = reqTime.substring(0,10);
            
            Debug.debugLog("REQUEST DATE: ", date);
            
            boolean validUser = CheckService.isValidUser(mobileNumber, keyWord);

            if (validUser) {
                int userRequest = CheckService.requestTracker(mobileNumber, date);
                Debug.debugLog("USER REQUEST: ", userRequest);
                if (userRequest < 3) {
                    boolean validGroup = CheckService.isDuplicateBloodGroup(mobileNumber, groupId);
                    Debug.debugLog("VALID GROUP: ", validGroup);
                    if (validGroup) {
                        boolean validHospital = CheckService.isDuplicateHospital(mobileNumber, hospitalId);
                        if (validHospital) {
                            Debug.debugLog("VALID HOSPITAL: ", validHospital);
                            String query = GetQuery.addBloodRequestQuery(reqTime, mobileNumber, groupId, amount, hospitalId, emergency);
                            boolean done = dbService.queryExcute(query);

                            if (done) {
                                query = GetQuery.addBloodRequestTrackerQuery(mobileNumber, reqTime);
                                Debug.debugLog("REQUEST TRACKER ADDING: ", query);
                                dbService.queryExcute(query);
                                GcmService.giveGCMService(request, response);
                                jsonObject = LogMessageJson.getLogMessageJson(Enum.CORRECT, Enum.MESSAGE_BLOOD_REQUEST_ADDED, requestName);
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
                jsonObject = LogMessageJson.getLogMessageJson(Enum.ERROR, Enum.MESSAGE_INVALID_VALUE, requestName);
            }
        } else {
            jsonObject = LogMessageJson.getLogMessageJson(Enum.ERROR, Enum.MESSAGE_LESS_PARAMETER, requestName);
        }
        SendJsonData.sendJsonData(request, response, jsonObject);
    }

    public static void removePersonBloodRequestTracker(HttpServletRequest request, HttpServletResponse response) throws JSONException {
        JSONObject jsonObject;
        String requestName = request.getParameter("requestName");

        if (request.getParameter("mobileNumber") != null) {
            String mobileNumber = request.getParameter("mobileNumber");
            if (mobileNumber.equals("")) {
                mobileNumber = null;
            }

            if (mobileNumber != null) {
                String query = GetQuery.removePersonBloodRequestTrackerQuery(mobileNumber);
                dbService.queryExcute(query);
                jsonObject = LogMessageJson.getLogMessageJson(Enum.CORRECT, mobileNumber + Enum.MESSAGE_BLOOD_REQUEST_TRACKER_REMOVED, requestName);
            } else {
                jsonObject = LogMessageJson.getLogMessageJson(Enum.ERROR, Enum.MESSAGE_INVALID_VALUE, requestName);
            }
        } else {
            jsonObject = LogMessageJson.getLogMessageJson(Enum.ERROR, Enum.MESSAGE_LESS_PARAMETER, requestName);
        }
        SendJsonData.sendJsonData(request, response, jsonObject);
    }

    public static void addDonationRecord(HttpServletRequest request, HttpServletResponse response) throws JSONException {

        String requestName = request.getParameter("requestName");

        if (request.getParameter("mobileNumber") != null
                && request.getParameter("donationDate") != null
                && request.getParameter("donationDetail") != null) {
            
            String mobileNumber = request.getParameter("mobileNumber");
            String donationDate = request.getParameter("donationDate");
            String donationDetail = request.getParameter("donationDetail");

            if (mobileNumber.equals("")) {
                mobileNumber = null;
            }
            if (donationDate.equals("")) {
                donationDate = null;
            }
            if (donationDetail.equals("")) {
                donationDetail = null;
            }
            Debug.debugLog("MobileNumber: ", mobileNumber, "Date: ", donationDate, "Details: ", donationDetail);
            if (mobileNumber != null && donationDate != null && donationDetail != null) {
                String query = GetQuery.addDonationRecordQuery(mobileNumber, donationDate, donationDetail);
                Debug.debugLog("Add Donation Record Query: " , query);
                boolean done = dbService.queryExcute(query);
                if (done) {
                    JSONObject jsonObject = LogMessageJson.getLogMessageJson(Enum.CORRECT, Enum.MESSAGE_DONATION_ADDED, requestName);
                    SendJsonData.sendJsonData(request, response, jsonObject);
                } else {
                    JSONObject jsonObject = LogMessageJson.getLogMessageJson(Enum.ERROR, Enum.MESSAGE_ERROR, requestName);
                    SendJsonData.sendJsonData(request, response, jsonObject);
                }
            } else {
                JSONObject jsonObject = LogMessageJson.getLogMessageJson(Enum.ERROR, Enum.MESSAGE_INVALID_VALUE, requestName);
                SendJsonData.sendJsonData(request, response, jsonObject);
            }

        } else {
            JSONObject jsonObject = LogMessageJson.getLogMessageJson(Enum.ERROR, Enum.MESSAGE_LESS_PARAMETER, requestName);
            SendJsonData.sendJsonData(request, response, jsonObject);
        }
    }

    public static void removeDonationRecord(HttpServletRequest request, HttpServletResponse response) throws JSONException {
        String requestName = request.getParameter("requestName");

        if (request.getParameter("mobileNumber") != null && request.getParameter("donationDate") != null) {
            String mobileNumber = request.getParameter("mobileNumber");
            String donationDate = request.getParameter("donationDate");

            if (mobileNumber.equals("")) {
                mobileNumber = null;
            }
            if (donationDate.equals("")) {
                donationDate = null;
            }

            if (mobileNumber != null && donationDate != null) {
                String query = GetQuery.removeDonationRecordQuery(mobileNumber, donationDate);
                boolean done = dbService.queryExcute(query);
                 Debug.debugLog("Delete Donation Query: ", query);
                if (done) {
                    JSONObject jsonObject = LogMessageJson.getLogMessageJson(Enum.CORRECT, Enum.MESSAGE_DONATION_REMOVED, requestName);
                    SendJsonData.sendJsonData(request, response, jsonObject);
                } else {
                    JSONObject jsonObject = LogMessageJson.getLogMessageJson(Enum.ERROR, Enum.MESSAGE_ERROR, requestName);
                    SendJsonData.sendJsonData(request, response, jsonObject);
                }
            } else {
                JSONObject jsonObject = LogMessageJson.getLogMessageJson(Enum.ERROR, Enum.MESSAGE_INVALID_VALUE, requestName);
                SendJsonData.sendJsonData(request, response, jsonObject);

            }
        } else {
            JSONObject jsonObject = LogMessageJson.getLogMessageJson(Enum.ERROR, Enum.MESSAGE_LESS_PARAMETER, requestName);
            SendJsonData.sendJsonData(request, response, jsonObject);
        }
    }

    public static void removeBloodRequest(HttpServletRequest request, HttpServletResponse response) throws JSONException {
        String requestName = request.getParameter("requestName");

        if (request.getParameter("mobileNumber") != null && request.getParameter("reqTime") != null) {
            String mobileNumber = request.getParameter("mobileNumber");
            String reqTime = request.getParameter("reqTime");

            if (mobileNumber.equals("")) {
                mobileNumber = null;
            }
            if (reqTime.equals("")) {
                reqTime = null;
            }

            if (mobileNumber != null && reqTime != null) {
                String query = GetQuery.removeBloodRequestQuery(mobileNumber, reqTime);
                Debug.debugLog("DELETE BLOOD REQUEST QUERYL:", query);
                boolean done = dbService.queryExcute(query);

                if (done) {
                    JSONObject jsonObject = LogMessageJson.getLogMessageJson(Enum.CORRECT, Enum.MESSAGE_REMOVED_BLOOD_REQUEST, requestName);
                    SendJsonData.sendJsonData(request, response, jsonObject);
                } else {
                    JSONObject jsonObject = LogMessageJson.getLogMessageJson(Enum.ERROR, Enum.MESSAGE_ERROR, requestName);
                    SendJsonData.sendJsonData(request, response, jsonObject);
                }

            } else {
                JSONObject jsonObject = LogMessageJson.getLogMessageJson(Enum.ERROR, Enum.MESSAGE_INVALID_VALUE, requestName);
                SendJsonData.sendJsonData(request, response, jsonObject);
            }
        } else {
            JSONObject jsonObject = LogMessageJson.getLogMessageJson(Enum.ERROR, Enum.MESSAGE_LESS_PARAMETER, requestName);
            SendJsonData.sendJsonData(request, response, jsonObject);
        }
    }

    public static void updateUserPersonalInfo(HttpServletRequest request, HttpServletResponse response) throws JSONException {
        String requestName = request.getParameter("requestName");

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

            boolean validUser = CheckService.isValidUser(mobileNumber, keyWord);
            if (validUser) {
                addPersonName(firstName, lastName);
                String query = GetQuery.updatePersonInfoQuery(mobileNumber, keyWord, firstName, lastName, groupId, distId);
                boolean done = dbService.queryExcute(query);
                if (done) {
                    JSONObject jsonObject = LogMessageJson.getLogMessageJson(Enum.CORRECT, Enum.MESSAGE_INFO_UPDATED);
                    jsonObject = RequestNameAdderJson.setRequestNameInJson(jsonObject, requestName);
                    SendJsonData.sendJsonData(request, response, jsonObject);
                } else {
                    JSONObject jsonObject = LogMessageJson.getLogMessageJson(Enum.ERROR, Enum.MESSAGE_ERROR);
                    jsonObject = RequestNameAdderJson.setRequestNameInJson(jsonObject, requestName);
                    SendJsonData.sendJsonData(request, response, jsonObject);
                }
            } else {
                JSONObject jsonObject = LogMessageJson.getLogMessageJson(Enum.ERROR, Enum.MESSAGE_INVALID_USER);
                jsonObject = RequestNameAdderJson.setRequestNameInJson(jsonObject, requestName);
                SendJsonData.sendJsonData(request, response, jsonObject);
            }
        } else {
            JSONObject jsonObject = LogMessageJson.getLogMessageJson(Enum.ERROR, Enum.MESSAGE_LESS_PARAMETER);
            jsonObject = RequestNameAdderJson.setRequestNameInJson(jsonObject, requestName);
            SendJsonData.sendJsonData(request, response, jsonObject);
        }
    }

    public static void deleteBloodRequestBefore() {
        String query = GetQuery.deleteBloodRequestBeforeQuery();
        boolean done = dbService.queryExcute(query);

        if (done) {
            Debug.debugLog("BLOOD REQUEST BEFORE " + Enum.MAX_DAY + " DAYS IS DELETED");
        } else {
            Debug.debugLog("BLOOD REQUEST DELETION OCCURS ERROR!");
        }
    }
}
