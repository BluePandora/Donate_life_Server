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
 * @author Biswajit Debnath
 */
public class Debug {

    public static void debugLog(Object... logMessage) {
        for (Object log : logMessage) {
            System.out.print(log + " ");
        }
        System.out.println("");
    }

    public static void debugURL(HttpServletRequest request, HttpServletResponse response) throws IOException {

        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            System.out.print(paramName + ": ");
            String[] paramValues = request.getParameterValues(paramName);
            for (int index = 0; index < paramValues.length; index++) {
                String paramValue = paramValues[index];
                System.out.print(paramValue + " ");
            }
            System.out.println("");
        }
    }

}
