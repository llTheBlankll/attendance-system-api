package com.pshs.attendancesystem.config;

import java.sql.Time;
import java.time.LocalTime;

public class APIConfiguration {

	public static final String SPRING_DOCS_TITLE = "Attendance System API";
	public static final String SPRING_DOCS_DESCRIPTION = "The Spring Boot REST API for the attendance system is a robust and scalable solution designed to streamline attendance management. Leveraging Spring Security for authentication and authorization, the API provides secure endpoints for retrieving, adding, and modifying attendance records. Real-time updates are facilitated through technologies like WebSocket, ensuring immediate reflection of changes in attendance status. The API supports report generation and analytics, allowing users to glean insights into attendance patterns. With configurable settings, comprehensive documentation, and adherence to security best practices, this API serves as a versatile and secure backend for applications seeking to automate attendance tracking and integrate attendance data seamlessly with other business systems.";

	private APIConfiguration() {
	}

	public static class Attendance {
		public static final LocalTime flagCeremonyTime = Time.valueOf("06:30:00").toLocalTime();
		public static final LocalTime lateTimeArrival = Time.valueOf("07:00:00").toLocalTime();
		public static final LocalTime onTimeArrival = Time.valueOf("05:00:00").toLocalTime();

		private Attendance() {
		}
	}

	public static class Pagination {
		public static final int DEFAULT_PAGE_SIZE = 10;

		private Pagination() {
		}
	}

}
