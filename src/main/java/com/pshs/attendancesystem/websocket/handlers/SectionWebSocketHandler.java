package com.pshs.attendancesystem.websocket.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pshs.attendancesystem.entities.Section;
import com.pshs.attendancesystem.enums.Status;
import com.pshs.attendancesystem.services.AttendanceService;
import com.pshs.attendancesystem.services.SectionService;
import com.pshs.attendancesystem.websocket.communication.SectionCommunicationService;
import com.pshs.attendancesystem.websocket.entities.WSSectionAttendanceResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.text.SimpleDateFormat;
import java.time.LocalDate;

@Component
public class SectionWebSocketHandler extends TextWebSocketHandler {
	private final AttendanceService attendanceService;
	private final SectionService sectionService;
	private final SectionCommunicationService sectionCommunicationService;
	private final ObjectMapper mapper = new ObjectMapper();

	public SectionWebSocketHandler(AttendanceService attendanceService, SectionService sectionService, SectionCommunicationService sectionCommunicationService) {
		this.attendanceService = attendanceService;
		this.sectionService = sectionService;
		this.sectionCommunicationService = sectionCommunicationService;

		mapper.registerModule(new JavaTimeModule());
		mapper.setDateFormat(
			new SimpleDateFormat("yyyy-MM-dd")
		);
	}

	private WSSectionAttendanceResponse getResponse(Integer sectionId) {
		LocalDate today = LocalDate.now();
		WSSectionAttendanceResponse response = new WSSectionAttendanceResponse();
		Section section = sectionService.getSectionBySectionId(sectionId);
		response.setSection(section);
		response.setOut(
			attendanceService.countAttendanceInSectionByStatusAndDate(
				sectionId,
				Status.OUT,
				today
			)
		);

		response.setAbsent(
			attendanceService.countAttendanceInSectionByStatusAndDate(
				sectionId,
				Status.ABSENT,
				today
			)
		);

		response.setPresent(
			attendanceService.countAttendanceInSectionByStatusAndDate(
				sectionId,
				Status.ONTIME,
				today
			) + attendanceService.countAttendanceInSectionByStatusAndDate(
				sectionId,
				Status.LATE,
				today
			) + attendanceService.countAttendanceInSectionByStatusAndDate(
				sectionId,
				Status.OUT,
				today
			)
		);

		response.setLate(
			attendanceService.countAttendanceInSectionByStatusAndDate(
				sectionId,
				Status.LATE,
				today
			)
		);

		response.setOnTime(
			attendanceService.countAttendanceInSectionByStatusAndDate(
				sectionId,
				Status.ONTIME,
				today
			)
		);

		return response;
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		sectionCommunicationService.registerSession(session);
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		sectionCommunicationService.removeSession(session);
	}

	@Override
	protected void handleTextMessage(@NonNull WebSocketSession session, TextMessage message) throws Exception {
		super.handleTextMessage(session, message);
		int sectionId = 0;
		try {
			sectionId = Integer.parseInt(message.getPayload());
		} catch (NumberFormatException e) {
			session.sendMessage(
				new TextMessage(
					mapper.writeValueAsString("Invalid Input")
				)
			);
			return;
		}

		if (sectionId > 0) {
			// Check if section exist.
			if (sectionService.isSectionExistById(sectionId)) {
				WSSectionAttendanceResponse response = getResponse(sectionId);
				session.sendMessage(
					new TextMessage(
						mapper.writeValueAsString(response)
					)
				);
			}
		} else {
			session.sendMessage(
				new TextMessage(
					"Invalid Input"
				)
			);
		}
	}
}
