 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluepandora.therap.donatelife.service;

import com.bluepandora.therap.donatelife.constant.Enum;
import com.bluepandora.therap.donatelife.constant.DbUser;
import com.bluepandora.therap.donatelife.database.DatabaseService;
import com.bluepandora.therap.donatelife.database.GetQuery;
import com.bluepandora.therap.donatelife.debug.Debug;
import com.bluepandora.therap.donatelife.debug.LogMessageJson;
import com.bluepandora.therap.donatelife.jsonbuilder.RequestNameAdderJson;
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
public class GcmProfileService extends DbUser {

    public static void updateGCMKey(HttpServletRequest request, HttpServletResponse response) throws JSONException {
        DatabaseService dbService = new DatabaseService(DRIVER_NAME, DATABASE_URL, USERNAME, PASSWORD);
        dbService.databaseOpen();

        String requestName = request.getParameter("requsetName");
        JSONObject jsonObject = null;
        if (request.getParameter("mobileNumber") != null && request.getParameter("gcmId") != null) {
            String mobileNumber = request.getParameter("mobileNumber");
            String gcmId = request.getParameter("gcmId");

            if (gcmId == null || gcmId.equals("NULL") || gcmId.equals("null")) {
                gcmId = "";
            }

            if (DataValidation.isValidMobileNumber(mobileNumber)) {
                String query = GetQuery.updateGCMIdQuery(mobileNumber, gcmId);
                boolean done = dbService.queryExcute(query);
                if (done) {
                    jsonObject = LogMessageJson.getLogMessageJson(Enum.CORRECT, Enum.MESSAGE_GCM_ID_UPDATED);
                    Debug.debugLog("Mobile Number: ", mobileNumber, " GCM UPDATION SUCCESS");
                } else {
                    jsonObject = LogMessageJson.getLogMessageJson(Enum.ERROR, Enum.MESSAGE_GCM_ID_NOT_UPDATED);
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
