package com.pshs.attendancesystem.controllers.attendance.statistics;

import com.pshs.attendancesystem.impl.AttendanceServiceImpl;
import com.pshs.attendancesystem.impl.ConfigurationService;
import com.pshs.attendancesystem.repositories.AttendanceRepository;
import com.pshs.attendancesystem.repositories.StudentRepository;
import com.pshs.attendancesystem.services.AttendanceService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Attendance Statistics", description = "Manages attendance statistics.")
@RestController
@RequestMapping("${api.root}/attendance/stats")
@SecurityRequirement(name = "JWT Authentication")
public class AttendanceStatisticsController {
	private final AttendanceService attendanceService;
	private final Logger logger = LoggerFactory.getLogger(getClass());

	public AttendanceStatisticsController(AttendanceRepository attendanceRepository,
	                                      StudentRepository studentRepository, ConfigurationService configurationService) {
		attendanceService = new AttendanceServiceImpl(attendanceRepository, studentRepository, configurationService);
	}


}

