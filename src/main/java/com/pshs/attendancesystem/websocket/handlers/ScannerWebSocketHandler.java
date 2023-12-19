package com.pshs.attendancesystem.websocket.handlers;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pshs.attendancesystem.entities.Guardian;
import com.pshs.attendancesystem.entities.RfidCredentials;
import com.pshs.attendancesystem.entities.Section;
import com.pshs.attendancesystem.entities.Student;
import com.pshs.attendancesystem.enums.Status;
import com.pshs.attendancesystem.messages.AttendanceMessages;
import com.pshs.attendancesystem.messages.RfidMessages;
import com.pshs.attendancesystem.messages.StudentMessages;
import com.pshs.attendancesystem.services.AttendanceService;
import com.pshs.attendancesystem.services.RfidService;
import com.pshs.attendancesystem.threading.SMSThread;
import com.pshs.attendancesystem.websocket.communication.FrontEndCommunicationService;
import com.pshs.attendancesystem.websocket.communication.SectionCommunicationService;
import com.pshs.attendancesystem.websocket.entities.WSSectionCommunicationServiceResponse;
import com.pshs.attendancesystem.websocket.entities.WebSocketData;
import com.pshs.attendancesystem.websocket.entities.WebSocketResponse;
import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.net.SocketException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Component
public class ScannerWebSocketHandler extends TextWebSocketHandler {
	private final AttendanceService attendanceService;
	private final RfidService rfidService;
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


	private void sendErrorMessage(WebSocketSession session, TextMessage textMessage) throws IOException {
		session.sendMessage(textMessage);
	}

	private void sendMessage(WebSocketSession session, String message) throws IOException {
		session.sendMessage(new TextMessage(message));
	}

	private WSSectionCommunicationServiceResponse getSectionWSResponse(Section section, Student student, Status status, LocalTime time) {
		return new WSSectionCommunicationServiceResponse(
			section,
			student,
			status,
			time
		);
	}

	@Override
	protected void handleTextMessage(@NonNull WebSocketSession session, TextMessage message) throws IOException {
		String textMessage = message.getPayload();
		if (textMessage.isEmpty()) {
			return;
		}

		String hashedLrn;
		Optional<RfidCredentials> rfidCredentials;
		Student student;
		RfidCredentials credentials;

		// Get the current time
		Time currentTime = new Time(System.currentTimeMillis());
		LocalTime currentLocalTime = currentTime.toLocalTime();

		try {
			WebSocketData webSocketData = mapper.readValue(textMessage, WebSocketData.class);
			hashedLrn = webSocketData.getHashedLrn();

			rfidCredentials = this.rfidService.getRfidCredentialByHashedLrn(hashedLrn);
			if (rfidCredentials.isPresent()) {
				student = rfidCredentials.get().getStudent();
				credentials = rfidCredentials.get();
			} else {
				response.setMessage("Invalid");
				sendMessage(session, mapper.writeValueAsString(response));
				return;
			}

			// Check if student already arrived.

			if (webSocketData.getMode().equals("in")) {
				handleInMode(session, credentials, student, currentLocalTime);
			} else if (webSocketData.getMode().equals("out") && credentials.getLrn() != null) {
				handleOutMode(session, credentials, student, currentLocalTime);
			}
		} catch (JsonParseException e) {
			logger.error(e.getMessage());
		} catch (SocketException exception) {
			logger.error("Socket error: {}", exception.getMessage());
		}
	}

	private void handleInMode(WebSocketSession session, @Nonnull RfidCredentials credentials, Student student, LocalTime currentLocalTime) {
		try {
			if (credentials.getHashedLrn() != null && !this.rfidService.isHashedLrnExist(credentials.getHashedLrn())) {
				response.setMessage("Invalid");
				sendErrorMessage(session, new TextMessage(
					mapper.writeValueAsString(response)
				));
				logger.warn("Hashed LRN does not exist in the database.");
				return;
			}

			if (student.getLrn() != null && attendanceService.isAlreadyArrived(student.getLrn())) {
				Status attendanceStatus = attendanceService.getStatusToday(student.getLrn());
				response.setMessage("Already arrived!");
				response.setStudentLrn(credentials.getLrn());
				response.setTime(Time.valueOf(LocalTime.now()));
				response.setStudent(student);
				response.setStatus(attendanceStatus);
				sendMessage(session, mapper.writeValueAsString(response));
				return;
			}

			// Check if no matching lrn was found.
			if (credentials.getLrn() != null) {
				// Change flag ceremony time if today is monday.
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(calendar.getTime());

				// Now add attendance.
				Status attendanceStatus = attendanceService.createAttendance(credentials.getLrn());
				response.setMessage("You are " + attendanceStatus);
				response.setStatus(attendanceStatus);
				response.setStudentLrn(credentials.getLrn());
				response.setTime(Time.valueOf(LocalTime.now()));
				response.setStudent(student);
				response.setStatus(attendanceStatus);

				if (attendanceStatus == Status.LATE) {
					String parentMessage = AttendanceMessages.onLateAttendanceMessage(getFullName(student), currentLocalTime.toString());
					informGuardian(student, parentMessage);
				} else if (attendanceStatus == Status.ONTIME) {
					String parentMessage = AttendanceMessages.onTimeAttendanceMessage(getFullName(student), currentLocalTime.toString());
					informGuardian(student, parentMessage);
				}

				sendMessage(session, mapper.writeValueAsString(response));

				sectionCommunicationService.sendUpdate(
					mapper.writeValueAsString(
						getSectionWSResponse(
							student.getSection(),
							student,
							attendanceStatus,
							currentLocalTime
						)
					)
				);
				frontEndCommunicationService.sendMessageToAllFrontEnd(mapper.writeValueAsString(response));
			} else {
				// Send a warning message, because there might be an error in scanner.
				TextMessage invalidLrnMessage = new TextMessage(StudentMessages.STUDENT_INVALID_LRN);
				session.sendMessage(invalidLrnMessage);
				logger.warn(RfidMessages.HASHED_LRN_NOT_FOUND);
			}
		} catch (IOException | IllegalArgumentException e) {
			logger.error(e.getMessage());
		}
	}


	private void handleOutMode(WebSocketSession session, RfidCredentials credentials, Student student, LocalTime currentLocalTime) {
		try {
			if (attendanceService.isAlreadyOut(credentials.getLrn())) {
				Status attendanceStatus = attendanceService.getStatusToday(credentials.getLrn());
				response.setMessage("Already check out");
				response.setStatus(attendanceStatus);
				response.setStudentLrn(credentials.getLrn());
				response.setTime(Time.valueOf(LocalTime.now()));
				response.setStudent(student);
				sendMessage(session, mapper.writeValueAsString(response));
				return;
			}

			if (!attendanceService.attendanceOut(credentials.getLrn())) {
				response.setMessage("Check in first");
				sendMessage(session, mapper.writeValueAsString(response));
				logger.warn("Check in first: {}", credentials.getLrn());
				return;
			}
			logger.info("Student {} has left at {}", credentials.getLrn(), Time.valueOf(LocalTime.now()));

			// Get Attendance
			response.setMessage("Bye Bye :)");
			response.setStudentLrn(credentials.getLrn());
			response.setTime(Time.valueOf(LocalTime.now()));
			response.setStatus(Status.OUT);
			response.setStudent(student);

			// Send response
			sendMessage(session, mapper.writeValueAsString(response));
			frontEndCommunicationService.sendMessageToAllFrontEnd(mapper.writeValueAsString(response));
			sectionCommunicationService.sendUpdate(
				mapper.writeValueAsString(
					getSectionWSResponse(
						student.getSection(),
						student,
						Status.OUT,
						currentLocalTime
					)
				)
			);

			// Inform Guardians
			String parentMessage = AttendanceMessages.studentOutOfFacility(getFullName(student), currentLocalTime.toString());
			informGuardian(student, parentMessage);
		} catch (IOException | IllegalArgumentException e) {
			logger.error(e.getMessage());
		}
	}

	private void informGuardian(Student student, String parentMessage) {
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
