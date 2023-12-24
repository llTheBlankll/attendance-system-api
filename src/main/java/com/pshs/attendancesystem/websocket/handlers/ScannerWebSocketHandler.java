package com.pshs.attendancesystem.websocket.handlers;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pshs.attendancesystem.entities.Guardian;
import com.pshs.attendancesystem.entities.RfidCredentials;
import com.pshs.attendancesystem.entities.Student;
import com.pshs.attendancesystem.enums.Status;
import com.pshs.attendancesystem.messages.AttendanceMessages;
import com.pshs.attendancesystem.services.AttendanceService;
import com.pshs.attendancesystem.services.RfidService;
import com.pshs.attendancesystem.threading.SMSThread;
import com.pshs.attendancesystem.websocket.communication.FrontEndCommunicationService;
import com.pshs.attendancesystem.websocket.communication.SectionCommunicationService;
import com.pshs.attendancesystem.websocket.entities.WSSectionCommunicationServiceResponse;
import com.pshs.attendancesystem.websocket.entities.WebSocketData;
import com.pshs.attendancesystem.websocket.entities.WebSocketResponse;
import io.sentry.Sentry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.List;

@Component
public class ScannerWebSocketHandler extends TextWebSocketHandler {

	// * Services
	private final AttendanceService attendanceService;
	private final RfidService rfidService;

	// * WebSocket Communication Service
	private final FrontEndCommunicationService frontEndCommunicationService;
	private final SectionCommunicationService sectionCommunicationService;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	ObjectMapper mapper = new ObjectMapper();
	WebSocketResponse response = new WebSocketResponse();

	public ScannerWebSocketHandler(RfidService rfidService, FrontEndCommunicationService frontEndCommunicationService, AttendanceService attendanceService, SectionCommunicationService sectionCommunicationService) {
		this.rfidService = rfidService;
		this.frontEndCommunicationService = frontEndCommunicationService;
		this.attendanceService = attendanceService;
		this.sectionCommunicationService = sectionCommunicationService;
		mapper.registerModule(new JavaTimeModule());
		mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
	}

	@Async
	protected void sessionSendMessage(WebSocketSession session, String message) throws IOException {
		session.sendMessage(new TextMessage(message));
	}

	private WSSectionCommunicationServiceResponse getSectionWSResponse(Student student, Status status, LocalTime time) {
		return new WSSectionCommunicationServiceResponse(
			student,
			status,
			time
		);
	}

	@Override
	protected void handleTextMessage(@NonNull WebSocketSession session, TextMessage message) throws IOException {
		// Validate input.
		String textMessage = message.getPayload();
		if (textMessage.isEmpty()) {
			return;
		}

		// Get the current time
		Time currentTime = new Time(System.currentTimeMillis());
		LocalTime currentLocalTime = currentTime.toLocalTime();

		try {
			// * Read the input of scanner websocket and convert it into WebSocketData class
			WebSocketData webSocketData = mapper.readValue(textMessage, WebSocketData.class);

			// * Get the hashed LRN.
			String hashedLrn = webSocketData.getHashedLrn();

			// * Get RFID Credentials of Student with the given hashed LRN.
			RfidCredentials credentials = this.rfidService.getRfidCredentialByHashedLrn(hashedLrn).orElse(null);

			// @ Check if credentials is null.
			if (credentials == null) {
				response.setMessage("Invalid LRN!");
				sessionSendMessage(session, mapper.writeValueAsString(response));
				return;
			}

			// * Handle the mode, if "in" then check in, else check out.
			String mode = webSocketData.getMode();
			if (mode.equals("in")) {
				handleInMode(session, credentials, currentLocalTime);
			} else if (mode.equals("out") && credentials.getLrn() != null) {
				handleOutMode(session, credentials, currentLocalTime);
			}
		} catch (JsonParseException e) {
			logger.error(e.getMessage());
			Sentry.captureException(e);
		}
	}

	private void handleInMode(@NonNull WebSocketSession session, @NonNull RfidCredentials credentials, LocalTime currentLocalTime) {
		try {
			Student student = credentials.getStudent();

			// Check if student already arrived.
			if (student.getLrn() != null && attendanceService.isAlreadyArrived(student.getLrn())) {
				Status attendanceStatus = attendanceService.getStatusToday(student.getLrn());
				response.setMessage("Already arrived!");
				response.setStudentLrn(credentials.getLrn());
				response.setTime(Time.valueOf(LocalTime.now()));
				response.setStudent(student);
				response.setStatus(attendanceStatus);
				sessionSendMessage(session, mapper.writeValueAsString(response));
				return;
			}

			// Configure the response to be sent.
			Status attendanceStatus = attendanceService.getStatus();
			response.setMessage("You are " + attendanceStatus);
			response.setStatus(attendanceStatus);
			response.setStudentLrn(student.getLrn());
			response.setTime(Time.valueOf(LocalTime.now()));
			response.setStudent(student);
			response.setStatus(attendanceStatus);

			// @ Create attendance for student.
			attendanceService.createAttendance(student.getLrn());

			if (attendanceStatus == Status.LATE) {
				String parentMessage = AttendanceMessages.onLateAttendanceMessage(getFullName(student), currentLocalTime.toString());
				informGuardian(student, parentMessage);
			} else if (attendanceStatus == Status.ONTIME) {
				String parentMessage = AttendanceMessages.onTimeAttendanceMessage(getFullName(student), currentLocalTime.toString());
				informGuardian(student, parentMessage);
			}

			// @ Send the response.
			sessionSendMessage(session, mapper.writeValueAsString(response));
			sectionCommunicationService.sendMessage(
				mapper.writeValueAsString(
					getSectionWSResponse(
						student,
						attendanceStatus,
						currentLocalTime
					)
				)
			);

			// @ Send message to Front End server the attendance update.
			frontEndCommunicationService.sendMessageToAllFrontEnd(mapper.writeValueAsString(response));
		} catch (IOException | IllegalArgumentException e) {
			logger.error(e.getMessage());
		}
	}


	private void handleOutMode(WebSocketSession session, RfidCredentials credentials, LocalTime currentLocalTime) {
		try {
			// @ Get student object from credentials.
			Student student = credentials.getStudent();

			// @ Check if student is already out.
			Status attendanceExistence = attendanceService.isAlreadyOut(credentials.getLrn());

			// * Check if student is already out.
			if (attendanceExistence == Status.OUT) {
				// ^^ If it is already out then send a message.
				Status attendanceStatus = attendanceService.getStatusToday(credentials.getLrn());
				response.setMessage("Already check out");
				response.setStatus(attendanceStatus);
				response.setStudentLrn(credentials.getLrn());
				response.setTime(Time.valueOf(LocalTime.now()));
				response.setStudent(student);
				sessionSendMessage(session, mapper.writeValueAsString(response));
				return;
			} else if (attendanceExistence == Status.NOT_FOUND) {
				// ^ If it is not found then send a message that the student need to check in.
				response.setMessage("Check in first");
				sessionSendMessage(session, mapper.writeValueAsString(response));
				logger.warn("Check in first: {}", credentials.getLrn());
				return;
			} else {
				// ^ If it is not out then check the student out.
				attendanceService.attendanceOut(student.getLrn());
			}

			logger.info("Student {} has left at {}", credentials.getLrn(), Time.valueOf(LocalTime.now()));

			// @ Set response
			response.setMessage("Bye Bye :)");
			response.setStudentLrn(credentials.getLrn());
			response.setTime(Time.valueOf(LocalTime.now()));
			response.setStatus(Status.OUT);
			response.setStudent(student);

			// @ Send response
			sessionSendMessage(session, mapper.writeValueAsString(response));
			frontEndCommunicationService.sendMessageToAllFrontEnd(mapper.writeValueAsString(response));
			sectionCommunicationService.sendMessage(
				mapper.writeValueAsString(
					getSectionWSResponse(
						student,
						Status.OUT,
						currentLocalTime
					)
				)
			);

			// @ Inform Guardians
			String parentMessage = AttendanceMessages.studentOutOfFacility(getFullName(student), currentLocalTime.toString());
			informGuardian(student, parentMessage);
		} catch (IOException | IllegalArgumentException e) {
			logger.error(e.getMessage());
		}
	}

	private void informGuardian(Student student, String parentMessage) {
		if (student.getGuardian() == null) {
			logger.error("No guardian found");
			logger.error("Student: {}, name: {}", student.getLrn(), getFullName(student));
			return;
		}

		List<Guardian> guardianSet = student.getGuardian();
		for (Guardian guardian : guardianSet) {
			if (guardian.getContactNumber().equals("null")) {
				logger.error("No contact number found");
				return;
			}

			SMSThread smsThread = new SMSThread(parentMessage, guardian);
			smsThread.start();
		}
	}

	private String getFullName(Student student) {
		return String.format("%s %s %s", student.getLastName(), student.getFirstName(), student.getMiddleName());
	}
}
