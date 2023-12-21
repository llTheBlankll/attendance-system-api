package com.pshs.attendancesystem.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.time.LocalTime;

@Configuration
public class APIConfig {

	@Value("${attendance.flag-ceremony-time}")
	private LocalTime flagCeremonyTime;

	@Value("${attendance.late-time-arrival}")
	private LocalTime lateTimeArrival;

	@Value("${attendance.on-time-arrival}")
	private LocalTime onTimeArrival;

	@Value("${attendance.absent-schedule}")
	private String absentSchedule;

	@Value("${attendance.absent-schedule}")
	private String previousAbsentSchedule;

	@Value("${api.security.requirement}")
	private String securityRequirement;

	@Value("${redis.username}")
	private String redisUsername;

	@Value("${redis.password}")
	private String redisPassword;

	@Value("${redis.host}")
	private String redisHost;

	@Value("${redis.port}")
	private int redisPort;

	@Value("${api.version}")
	private String API_VERSION;
	private String SPRING_DOCS_TITLE = "Attendance System API";
	private String SPRING_DOCS_DESCRIPTION = "The Spring Boot REST API for the attendance system is a robust and scalable solution designed to streamline attendance management. Leveraging Spring Security for authentication and authorization, the API provides secure endpoints for retrieving, adding, and modifying attendance records. Real-time updates are facilitated through technologies like WebSocket, ensuring immediate reflection of changes in attendance status. The API supports report generation and analytics, allowing users to glean insights into attendance patterns. With configurable settings, comprehensive documentation, and adherence to security best practices, this API serves as a versatile and secure backend for applications seeking to automate attendance tracking and integrate attendance data seamlessly with other business systems.";

	public int getRedisPort() {
		return redisPort;
	}

	public void setRedisPort(int redisPort) {
		this.redisPort = redisPort;
	}

	public String getRedisUsername() {
		return redisUsername;
	}

	public void setRedisUsername(String redisUsername) {
		this.redisUsername = redisUsername;
	}

	public String getRedisPassword() {
		return redisPassword;
	}

	public void setRedisPassword(String redisPassword) {
		this.redisPassword = redisPassword;
	}

	public String getRedisHost() {
		return redisHost;
	}

	public void setRedisHost(String redisHost) {
		this.redisHost = redisHost;
	}

	public String getSecurityRequirement() {
		return securityRequirement;
	}

	public void setSecurityRequirement(String securityRequirement) {
		this.securityRequirement = securityRequirement;
	}

	public String getPreviousAbsentSchedule() {
		return previousAbsentSchedule;
	}

	public void setPreviousAbsentSchedule(String previousAbsentSchedule) {
		this.previousAbsentSchedule = previousAbsentSchedule;
	}

	public String getSPRING_DOCS_TITLE() {
		return SPRING_DOCS_TITLE;
	}

	public void setSPRING_DOCS_TITLE(String SPRING_DOCS_TITLE) {
		this.SPRING_DOCS_TITLE = SPRING_DOCS_TITLE;
	}

	public String getAPI_VERSION() {
		return API_VERSION;
	}

	public void setAPI_VERSION(String API_VERSION) {
		this.API_VERSION = API_VERSION;
	}

	public String getSPRING_DOCS_DESCRIPTION() {
		return SPRING_DOCS_DESCRIPTION;
	}

	public void setSPRING_DOCS_DESCRIPTION(String SPRING_DOCS_DESCRIPTION) {
		this.SPRING_DOCS_DESCRIPTION = SPRING_DOCS_DESCRIPTION;
	}

	public LocalTime getFlagCeremonyTime() {
		return flagCeremonyTime;
	}

	public void setFlagCeremonyTime(LocalTime flagCeremonyTime) {
		this.flagCeremonyTime = flagCeremonyTime;
	}

	public LocalTime getLateTimeArrival() {
		return lateTimeArrival;
	}

	public void setLateTimeArrival(LocalTime lateTimeArrival) {
		this.lateTimeArrival = lateTimeArrival;
	}

	public LocalTime getOnTimeArrival() {
		return onTimeArrival;
	}

	public void setOnTimeArrival(LocalTime onTimeArrival) {
		this.onTimeArrival = onTimeArrival;
	}

	public String getAbsentSchedule() {
		return absentSchedule;
	}

	public void setAbsentSchedule(String absentSchedule) {
		this.absentSchedule = absentSchedule;
	}
}
