package com.pshs.attendancesystem.threading;

import com.pshs.attendancesystem.entities.Guardian;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
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

    private void sendSMSMessage() {
        try {
            // Specify the URL
            URL url = new URL("http://192.168.1.1/api/sms/send-sms"); // replace with your actual URL
//            URL url = new URL("http://localhost:8182"); // replace with your actual URL

            // Open a connection to the URL
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set the request method to POST
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Cookie", "_TESTCOOKIESUPPORT=1; SID=2eb3a6304ad02831d6bd8458a96f6a0f");
            connection.setRequestProperty("DNT", "1");
            connection.setRequestProperty("Host", "192.168.1.1");
            connection.setRequestProperty("Origin", " http://192.168.1.1");
            connection.setRequestProperty("Referer", "http://192.168.1.1/small/html/smssnew.htm?history=smss.htm");
            connection.setRequestProperty("User-Agent", " Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/117.0.0.0 Safari/537.36");

            // Enable input/output streams
            connection.setDoOutput(true);

            // Set request headers
//            connection.setRequestProperty("Content-Type", "x-www-form-urlencoded");

            // Set LocalDateTime
            LocalDateTime dateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedTime = dateTime.format(formatter);

            // Define the XML data to be sent in the request body
//            String xmlData = String.format("<?xml version=\"1.0\" encoding=\"UTF-8\" ?><request><Index>-1</Index><Phones><Phone>%s</Phone></Phones><Sca></Sca><Content>%s</Content><Length>%d</Length><Reserved>1</Reserved><Date>%s</Date></request>", message, phoneNumber, message.length(), formattedTime);
            String xmlData = String.format("<?xml version=\"1.0\" encoding=\"UTF-8\" ?><request><Index>-1</Index><Phones><Phone>%s</Phone></Phones><Sca></Sca><Content>%s</Content><Length>%d</Length><Reserved>1</Reserved><Date>%s</Date></request>", this.guardian.getContactNumber(), this.parentMessage, parentMessage.length(), formattedTime);

            // Get the output stream and write the XML data to it
            try (DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream())) {
                outputStream.writeBytes(xmlData);
                outputStream.flush();
            }

            StringBuilder builder = new StringBuilder();
            String line;
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

            reader.close();

//            // Get the response code
//            int responseCode = connection.getResponseCode();
//            System.out.println("Response Code: " + responseCode);
//            System.out.println("Response Received: " + builder.toString());
//            System.out.println(xmlData);


            // Handle the response, if needed
            // You can use connection.getInputStream() to read the response

            // Close the connection
            connection.disconnect();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void run() {
        sendSMSMessage();
    }
}
