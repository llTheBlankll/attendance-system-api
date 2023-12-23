package com.pshs.attendancesystem.threading;

import com.pshs.attendancesystem.entities.Guardian;
import com.squareup.okhttp.*;
import io.sentry.Sentry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class SMSThread extends Thread {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	String parentMessage;
	Guardian guardian;

	public SMSThread(String parentMessage, Guardian guardian) {
		this.parentMessage = parentMessage;
		this.guardian = guardian;
	}

	private void sendSMSMessage() {
		try {
			OkHttpClient client = new OkHttpClient();
			MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
			RequestBody body = RequestBody.create(mediaType, "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><request><Index>-1</Index><Phones><Phone>09998216556</Phone></Phones><Sca></Sca><Content>Ambos Clarrise Jolia G. is on time</Content><Length>6</Length><Reserved>1</Reserved><Date>2023-11-13 23:32:14</Date></request>");
			Request request = new Request.Builder()
				.url("http://192.168.1.1/api/sms/send-sms")
				.method("POST", body)
				.addHeader("Content-Type", "application/x-www-form-urlencoded")
				.addHeader("Cookie", "_TESTCOOKIESUPPORT=1; SID=2eb3a6304ad02831d6bd8458a96f6a0f")
				.addHeader("DNT", "1")
				.addHeader("Host", "192.168.1.1")
				.addHeader("Origin", " http://192.168.1.1")
				.addHeader("Referer", " http://192.168.1.1/small/html/smssnew.htm?history=smss.htm")
				.addHeader("User-Agent", " Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/117.0.0.0 Safari/537.36")
				.build();
			Response response = client.newCall(request).execute();
			if (response.isSuccessful()) {
				logger.info("SMS sent successfully.");
			}
		} catch (IOException | IllegalArgumentException e) {
			logger.error("Error sending SMS: {}", e.getMessage());
			Sentry.captureException(e);
		}
	}

	@Override
	public void run() {
		sendSMSMessage();
	}
}
