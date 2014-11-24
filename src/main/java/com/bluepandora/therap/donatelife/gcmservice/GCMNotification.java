/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package com.bluepandora.therap.donatelife.gcmservice;
//
///**
// *
// * @author Biswajit Debnath
// */
//public class GcmService {
//
//    public static void gcmService() {
//
//    }
//
//}
package com.bluepandora.therap.donatelife.gcmservice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Sender;

/**
 * Servlet implementation class GCMBroadcast
 */
@WebServlet("/GCMBroadcast")
public class GCMNotification extends HttpServlet {

    private static final String GOOGLE_API_KEY = "AIzaSyDQFvp7jHnkwZlDrEqcOoQNGq9tPpgmqps";
    private static final String MESSAGE_KEY = "message";
    private List<String> androidTargets = new ArrayList<String>();

    public GCMNotification() {
        super();
    }

    // This doPost() method is called from the form in our index.jsp file.
    // It will broadcast the passed "Message" value.
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // We'll collect the "CollapseKey" and "Message" values from our JSP page
        String collapseKey = "";
        String userMessage = "";

        try {
            userMessage = request.getParameter("Message");
            collapseKey = request.getParameter("CollapseKey");
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        // Instance of com.android.gcm.server.Sender, that does the
        // transmission of a Message to the Google Cloud Messaging service.
        Sender sender = new Sender(GOOGLE_API_KEY);

        // This Message object will hold the data that is being transmitted
        // to the Android client devices.  For this demo, it is a simple text
        // string, but could certainly be a JSON object.
        Message message = new Message.Builder()
                // If multiple messages are sent using the same .collapseKey()
                // the android target device, if it was offline during earlier message
                // transmissions, will only receive the latest message for that key when
                // it goes back on-line.
                .collapseKey(collapseKey)
                .timeToLive(30)
                .delayWhileIdle(true)
                .addData("Message", userMessage)
                .build();

        try {
            // use this for multicast messages.  The second parameter
            // of sender.send() will need to be an array of register ids.
            MulticastResult result = sender.send(message, androidTargets, 1);

            if (result.getResults() != null) {
                int canonicalRegId = result.getCanonicalIds();
                if (canonicalRegId != 0) {

                }
            } else {
                int error = result.getFailure();
                System.out.println("Broadcast failure: " + error);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
