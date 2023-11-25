package com.pshs.attendancesystem.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocsConfiguration {

	@Bean
	public GroupedOpenApi allControllersGroup() {
		return GroupedOpenApi.builder()
			.group("All")
			.displayName("All Endpoints")
			.packagesToScan("com.pshs.attendancesystem.controllers")
			.build();
	}

	@Bean
	public GroupedOpenApi attendanceStatisticsControllerGroup() {
		return GroupedOpenApi.builder()
			.group("Attendance Statistics")
			.packagesToScan("com.pshs.attendancesystem.controllers.attendance.statistics")
			.build();
	}

	@Bean
	public GroupedOpenApi attendanceControllerGroup() {
		return GroupedOpenApi.builder()
			.group("Attendance")
			.packagesToExclude("com.pshs.attendancesystem.controllers.attendance.statistics")
			.packagesToScan("com.pshs.attendancesystem.controllers.attendance")
			.build();
	}

	@Bean
	public GroupedOpenApi studentControllerGroup() {
		return GroupedOpenApi.builder()
			.group("Student")
			.packagesToScan("com.pshs.attendancesystem.controllers.student")
			.build();
	}

	@Bean
	public GroupedOpenApi fingerprintControllerGroup() {
		return GroupedOpenApi.builder()
			.group("Fingerprint")
			.packagesToScan("com.pshs.attendancesystem.controllers.fingerprint")
			.build();
	}

	@Bean
	public GroupedOpenApi guardianControllerGroup() {
		return GroupedOpenApi.builder()
			.group("Guardian")
			.packagesToScan("com.pshs.attendancesystem.controllers.guardian")
			.build();
	}

	@Bean
	public GroupedOpenApi teacherControllerGroup() {
		return GroupedOpenApi.builder()
			.group("Teacher")
			.packagesToScan("com.pshs.attendancesystem.controllers.teacher")
			.build();
	}

	@Bean
	public GroupedOpenApi sectionControllerGroup() {
		return GroupedOpenApi.builder()
			.group("Section")
			.packagesToScan("com.pshs.attendancesystem.controllers.section")
			.build();
	}

	@Bean
	public GroupedOpenApi rfidCredentialsControllerGroup() {
		return GroupedOpenApi.builder()
			.group("RFID Credentials")
			.packagesToScan("com.pshs.attendancesystem.controllers.rfidcredentials")
			.build();
	}

	@Bean
	public GroupedOpenApi strandControllerGroup() {
		return GroupedOpenApi.builder()
			.group("Strand")
			.packagesToScan("com.pshs.attendancesystem.controllers.strand")
			.build();
	}

	@Bean
	public GroupedOpenApi gradeLevelControllerGroup() {
		return GroupedOpenApi.builder()
			.group("Grade Level")
			.packagesToScan("com.pshs.attendancesystem.controllers.gradelevel")
			.build();
	}
}