package com.pshs.attendancesystem.schedules;

import com.pshs.attendancesystem.entities.Student;
import com.pshs.attendancesystem.impl.ConfigurationService;
import com.pshs.attendancesystem.services.AttendanceService;
import com.pshs.attendancesystem.services.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class AbsentStudents {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final StudentService studentService;
	private final AttendanceService attendanceService;
	private final ConfigurationService configurationService;
	@Value("#{APIConfig.absentSchedule}")
	private String test;

	public AbsentStudents(StudentService studentService, AttendanceService attendanceService, ConfigurationService configurationService) {
		this.studentService = studentService;
		this.attendanceService = attendanceService;
		this.configurationService = configurationService;
	}

	@Scheduled(cron = "#{APIConfig.}") // run every minute
	public void setAbsentStudentYesterday() {
		logger.info(test);
		LocalDate yesterday = LocalDate.now();
		Iterable<Student> students = studentService.getAllStudent();
		logger.info("Giving attendance to those students who are absent.");
		students.forEach(student -> {
			if (!attendanceService.existsByStudentLrnAndDate(student.getLrn(), yesterday)) {
				attendanceService.setAsAbsent(student, yesterday);
			}
		});
	}
}
