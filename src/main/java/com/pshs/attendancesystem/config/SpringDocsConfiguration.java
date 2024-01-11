package com.pshs.attendancesystem.config;

import com.pshs.attendancesystem.impl.ConfigurationService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
	name = "JWT Authentication",
	type = SecuritySchemeType.HTTP,
	bearerFormat = "JWT",
	scheme = "bearer",
	in = SecuritySchemeIn.HEADER
)
@OpenAPIDefinition(
	servers = {
		@Server(url = "/", description = "Default Server URL")
	}
)
public class SpringDocsConfiguration {

	private final ConfigurationService configurationService;

	public SpringDocsConfiguration(ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}

	@Bean
	public OpenAPI openAPI() {
		Info info = new Info();
		Contact contact = new Contact();

		contact.setEmail("llTheBlankll@gmail.com");
		contact.setName("Vince Angelo Batecan");
		contact.setUrl("https://www.facebook.com/Nytriii");
		info.setTitle(configurationService.getSPRING_DOCS_TITLE());
		info.setDescription(configurationService.getSPRING_DOCS_DESCRIPTION());
		info.setVersion(configurationService.getAPIVersion());
		info.setContact(contact);
		return new OpenAPI()
			.info(info)
			.addSecurityItem(
				new SecurityRequirement()
					.addList(configurationService.getSecurityRequirement())
			);
	}

	@Bean
	public GroupedOpenApi attendanceChartControllerGroup() {
		return GroupedOpenApi.builder()
			.group("Attendance Chart")
			.packagesToScan("com.pshs.attendancesystem.controllers.attendance.statistics.chart")
			.build();
	}

	@Bean
	public GroupedOpenApi configControllerGroup() {
		return GroupedOpenApi.builder()
			.group("Config")
			.packagesToScan("com.pshs.attendancesystem.controllers.config")
			.build();
	}

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
			.packagesToScan("com.pshs.attendancesystem.controllers.rfid")
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

	@Bean
	public GroupedOpenApi subjectControllerGroup() {
		return GroupedOpenApi.builder()
			.group("Subject")
			.packagesToScan("com.pshs.attendancesystem.controllers.subject")
			.build();
	}

	@Bean
	public GroupedOpenApi userControllerGroup() {
		return GroupedOpenApi.builder()
			.group("User")
			.packagesToScan("com.pshs.attendancesystem.controllers.user")
			.build();
	}

	@Bean
	public GroupedOpenApi authenticationControllerGroup() {
		return GroupedOpenApi.builder()
			.group("Authentication")
			.packagesToScan("com.pshs.attendancesystem.controllers.auth")
			.build();
	}
}
