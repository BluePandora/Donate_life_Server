/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluepandora.therap.donatelife.servlet;

import com.bluepandora.therap.donatelife.constant.Request;
import com.bluepandora.therap.donatelife.debug.Debug;
import com.bluepandora.therap.donatelife.debug.LogMessageJson;
import com.bluepandora.therap.donatelife.jsonsender.SendJsonData;
import com.bluepandora.therap.donatelife.service.AUService;
import com.bluepandora.therap.donatelife.service.DataService;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Biswajit Debnath
 */
@WebServlet(name = "Service", urlPatterns = {"/service"})
public class ServiceServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        Debug.debugLog("SERVICE SERVLET HITTED FROM OUTSIDE!");
        String requestName = request.getParameter("requestName");

        try {
            if (requestName != null) {

                AUService.deleteBloodRequestBefore();

                if (requestName.equals(Request.BLOOD_GROUP_LIST)) {
                    DataService.getBloodGroupList(request, response);
                } else if (requestName.equals(Request.HOSPITAL_LIST)) {
                    DataService.getHospitalList(request, response);
                } else if (requestName.equals(Request.DISTRICT_LIST)) {
                    DataService.getDistrictList(request, response);
                } else if (requestName.equals(Request.GET_BLOOD_REQUEST)) {
                    DataService.getBloodRequestList(request, response);
                } else if (requestName.equals(Request.USER_INFO)) {
                    DataService.getUserProfile(request, response);
                } else if (requestName.equals(Request.GET_DONATION_RECORD)) {
                    DataService.getUserDonationRecord(request, response);
                } else if (requestName.equals(Request.REGISTER)) {
                    AUService.registerUser(request, response);
                } else if (requestName.equals(Request.FEEDBACK)) {
                    AUService.addFeedback(request, response);
                } else if (requestName.equals(Request.GCM_UPDATE)) {
                    AUService.updateGCMKey(request, response);
                } else if (requestName.equals(Request.IS_REGISTER)) {
                    AUService.userRegistrationCheck(request, response);
                } else if (requestName.equals(Request.ADD_BLOOD_REQUEST)) {
                    AUService.addBloodRequest(request, response);
                } else if (requestName.equals(Request.REMOVE_TRACKER)) {
                    AUService.removePersonBloodRequestTracker(request, response);
                } else if (requestName.equals(Request.ADD_DONATION_RECORD)) {
                    AUService.addDonationRecord(request, response);
                } else if (requestName.equals(Request.REMOVE_DONATION_RECORD)) {
                    AUService.removeDonationRecord(request, response);
                } else if (requestName.equals(Request.DELETE_BLOOD_REQUET_IN_KEY)) {
                    AUService.removeBloodRequest(request, response);
                } else if (requestName.equals(Request.UPDATE_USER_INFO)) {
                    AUService.updateUserPersonalInfo(request, response);
                } else {
                    DataService.unknownHit(request, response);
                }
            } else {
                DataService.unknownHit(request, response);
            }
        } catch (Exception error) {
            JSONObject jsonObject = LogMessageJson.getLogMessageJson(com.bluepandora.therap.donatelife.constant.Enum.ERROR, "PROBLEM ARISE WITH YOUR REQUEST!");
            SendJsonData.sendJsonData(request, response, jsonObject);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
