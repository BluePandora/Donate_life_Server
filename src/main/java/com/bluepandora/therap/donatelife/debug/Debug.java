/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluepandora.therap.donatelife.debug;

import java.io.IOException;
import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Biswajit Debnath For debugging these class provide some method
 */
public class Debug {

    /**
     *
     * @param logMessage This method print all kind of value as a string for
     * debugging
     */
    public static void debugLog(Object... logMessage) {
        for (Object log : logMessage) {
            System.out.print(log + " ");
        }
        System.out.println("");
    }

    /**
     *
     * @param request
     * @param response
     * @throws IOException This method print all the URL parameter for debug
     * purpose
     */
    public static void debugURL(HttpServletRequest request, HttpServletResponse response) throws IOException {

        boolean parameterFound = false;

        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            parameterFound = true;
            System.out.print("\t" + paramName + ": ");

            if (paramName.equals("keyWord") || paramName.equals("accessKey")) {
                continue;
            }

            String[] paramValues = request.getParameterValues(paramName);
            for (int index = 0; index < paramValues.length; index++) {
                String paramValue = paramValues[index];
                System.out.print(paramValue + " ");
            }
            System.out.println("");
        }
        if (parameterFound == false) {
            System.out.println("NO PARAMTER FOUND IN URL!");
        }
    }

}
