package com.pshs.attendancesystem.threading;

import com.pshs.attendancesystem.entities.Guardian;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SMSThread extends Thread {

    String parentMessage;
    Guardian guardian;

    public SMSThread(String parentMessage, Guardian guardian) {
        this.parentMessage = parentMessage;
        this.guardian = guardian;
    }

    private void sendSMSMessage(String phoneNumber, String message) {
        try {
            // Specify the URL
//            URL url = new URL("http://192.168.1.1/api/sms/send-sms"); // replace with your actual URL
            URL url = new URL("http://localhost:8182"); // replace with your actual URL

            // Open a connection to the URL
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set the request method to POST
            connection.setRequestMethod("POST");

            // Enable input/output streams
            connection.setDoOutput(true);

            // Set request headers
            connection.setRequestProperty("Content-Type", "application/xml");

            // Set LocalDateTime
            LocalDateTime dateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedTime = dateTime.format(formatter);

            // Define the XML data to be sent in the request body
            String xmlData = String.format("<?xml version=\"1.0\" encoding=\"UTF-8\" ?><request><Index>-1</Index><Phones><Phone>%s</Phone></Phones><Sca></Sca><Content>%s</Content><Length>%d</Length><Reserved>1</Reserved><Date>%s</Date></request>", message, phoneNumber, message.length(), formattedTime);

            // Get the output stream and write the XML data to it
            try (DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream())) {
                outputStream.writeBytes(xmlData);
                outputStream.flush();
            }

            // Get the response code
            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            // Handle the response, if needed
            // You can use connection.getInputStream() to read the response

            // Close the connection
            connection.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        sendSMSMessage(this.parentMessage, this.guardian.getContactNumber());
    }
}
