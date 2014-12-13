/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluepandora.therap.donatelife.service;

import  com.bluepandora.therap.donatelife.constant.DbUser;
import  com.bluepandora.therap.donatelife.constant.Enum;
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
public class FeedBackService extends DbUser{
    
     public static void addFeedback(HttpServletRequest request, HttpServletResponse response) throws JSONException {

        DatabaseService dbService = new DatabaseService(DRIVER_NAME, DATABASE_URL, USERNAME, PASSWORD);
        dbService.databaseOpen();

        String requestName = request.getParameter("requestName");
        JSONObject jsonObject = null;

        if (request.getParameter("idUser") != null && request.getParameter("subject") != null && request.getParameter("comment") != null) {

            String idUser = request.getParameter("idUser");
            String subject = request.getParameter("subject");
            String comment = request.getParameter("comment");
            if (DataValidation.isValidString(idUser) && DataValidation.isValidString(subject) && DataValidation.isValidString(comment) && DataValidation.isValidLength(comment, 300)) {
                String query = GetQuery.addFeedback(idUser, subject, comment);
                boolean done = dbService.queryExcute(query);
                if (done) {
                    jsonObject = LogMessageJson.getLogMessageJson(Enum.CORRECT, Enum.MESSAGE_FEEDBACK_THANKS);
                    Debug.debugLog(idUser, " FEEDBACK ADDING SUCCESS");
                } else {
                    jsonObject = LogMessageJson.getLogMessageJson(Enum.ERROR, Enum.MESSAGE_ERROR);
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
