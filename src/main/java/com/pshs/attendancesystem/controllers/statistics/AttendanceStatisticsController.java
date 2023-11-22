package com.pshs.attendancesystem.controllers.statistics;

import com.pshs.attendancesystem.entities.Attendance;
import com.pshs.attendancesystem.entities.Section;
import com.pshs.attendancesystem.entities.statistics.*;
import com.pshs.attendancesystem.enums.Status;
import com.pshs.attendancesystem.impl.AttendanceServiceImpl;
import com.pshs.attendancesystem.messages.AttendanceMessages;
import com.pshs.attendancesystem.repositories.AttendanceRepository;
import com.pshs.attendancesystem.repositories.StudentRepository;
import com.pshs.attendancesystem.services.AttendanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Collections;

@Tag(name = "Attendance Statistics", description = "Manages attendance statistics.")
@RestController
@RequestMapping("/api/v1/attendance/statistics")
public class AttendanceStatisticsController {

	private final AttendanceService attendanceService;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	LocalDate today = LocalDate.now();

	public AttendanceStatisticsController(AttendanceRepository attendanceRepository,
	                                      StudentRepository studentRepository) {
		attendanceService = new AttendanceServiceImpl(attendanceRepository, studentRepository);
	}

	/**
	 * Retrieves the attendance records for a specific student.
	 *
	 * @param studentLrn the learning reference number of the student
	 * @return an iterable collection of Attendance objects representing the student's attendance records
	 */
	@Operation(
		summary = "Get Student Attendance",
		description = "Retrieves the attendance records for a specific student.",
		parameters = {
			@Parameter(name = "lrn", description = "The learning reference number of the student")
		}
	)
	@GetMapping("/student")
	public Iterable<Attendance> getStudentAttendanceBetweenDate(@RequestParam("lrn") Long studentLrn) {
		if (studentLrn == null) {
			logger.info(AttendanceMessages.ATTENDANCE_NULL);
			return null;
		}

		LocalDate firstDayOfYear = LocalDate.now().withDayOfYear(1);
		LocalDate lastDayOfYear = today.withDayOfYear(today.lengthOfYear());
		BetweenDate dates = new BetweenDate();
		dates.setStartDate(firstDayOfYear);
		dates.setEndDate(lastDayOfYear);

		return this.attendanceService.getAttendanceBetweenDate(
			studentLrn,
			dates);
	}

	/**
	 * Retrieves the attendance records for students based on a given date and status.
	 *
	 * @param dateStatus object containing the date and status to filter the attendance records
	 * @return an iterable collection of Attendance objects representing the attendance records
	 */
	@Operation(
		summary = "Get Students Attendance",
		description = "Retrieves the attendance records for students based on a given date and status.",
		parameters = {
			@Parameter(name = "date", description = "The date to filter the attendance records"),
		}
	)
	@GetMapping("/get/date")
	public Iterable<Attendance> getStudentsAttendanceByDate(@RequestBody DateWithStatus dateStatus) {
		if (dateStatus.getDateRange() == null) {
			logger.info(AttendanceMessages.ATTENDANCE_NULL);
			return null;
		}

		Status status = dateStatus.getStatus();
		BetweenDate betweenDate = dateStatus.getDateRange();

		if (status == Status.LATE) {
			return this.attendanceService.getAllAttendanceBetweenDateWithStatus(
				betweenDate.getStartDate(),
				betweenDate.getEndDate(),
				Status.LATE
			);
		} else if (status == Status.ONTIME) {
			return this.attendanceService.getAllAttendanceBetweenDateWithStatus(
				betweenDate.getStartDate(),
				betweenDate.getEndDate(),
				Status.ONTIME
			);
		} else {
			return this.attendanceService.getAllAttendanceBetweenDate(betweenDate.getStartDate(), betweenDate.getEndDate());
		}
	}

	/**
	 * Retrieves the attendance of students based on a specified time period and status.
	 *
	 * @param timeStatus an object that contains the time period and status to filter the attendance
	 * @return the list of attendance records that match the specified time period and status
	 */
	@Operation(
		summary = "Get Students Attendance",
		description = "Retrieves the attendance of students based on a specified time period and status.",
		parameters = {
			@Parameter(name = "time", description = "The time period to filter the attendance. This can either be 'month', 'week', or 'today'"),
		}
	)
	@GetMapping("/get/time")
	public Iterable<Attendance> getStudentsAttendanceByTime(@RequestBody TimeWithStatus timeStatus) {
		Status status = timeStatus.getStatus();
		String time = timeStatus.getTime();

		if (time == null) {
			logger.info(AttendanceMessages.ATTENDANCE_NULL);
			return null;
		}

		LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
		LocalDate lastDayOfMonth = today.withDayOfMonth(today.lengthOfMonth());

		LocalDate firstDayOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
		LocalDate lastDayOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

		switch (time) {
			case "month":
				if (status == Status.LATE) {
					return this.attendanceService.getAllAttendanceBetweenDateWithStatus(
						firstDayOfMonth,
						lastDayOfMonth,
						Status.LATE
					);
				} else if (status == Status.ONTIME) {
					return this.attendanceService.getAllAttendanceBetweenDateWithStatus(
						firstDayOfMonth,
						lastDayOfMonth,
						Status.ONTIME
					);
				} else {
					return this.attendanceService.getAllAttendanceBetweenDate(firstDayOfMonth, lastDayOfMonth);
				}

			case "week":
				if (status == Status.LATE) {
					return this.attendanceService.getAllAttendanceBetweenDateWithStatus(
						firstDayOfWeek,
						lastDayOfWeek,
						Status.LATE
					);
				} else if (status == Status.ONTIME) {
					return this.attendanceService.getAllAttendanceBetweenDateWithStatus(
						firstDayOfWeek,
						lastDayOfWeek,
						Status.ONTIME
					);
				} else {
					return this.attendanceService.getAllAttendanceBetweenDate(firstDayOfWeek, lastDayOfWeek);
				}

			case "today":
				if (status == Status.LATE) {
					return this.attendanceService.getAllAttendanceBetweenDateWithStatus(
						LocalDate.now(),
						LocalDate.now(),
						Status.LATE
					);
				} else if (status == Status.ONTIME) {
					return this.attendanceService.getAllAttendanceBetweenDateWithStatus(
						LocalDate.now(),
						LocalDate.now(),
						Status.ONTIME
					);
				} else {
					return this.attendanceService.getAllAttendanceBetweenDate(LocalDate.now(), LocalDate.now());
				}

			default:
				return null;
		}
	}

	/**
	 * Retrieves the attendance count by date.
	 *
	 * @param dateStatus the DateWithStatus object containing the date and status
	 * @return the count of attendance for the given date and status
	 */
	@Operation(
		summary = "Get Attendance Count",
		description = "Retrieves the attendance count by date. Returns -3 if the date range is null.",
		parameters = {
			@Parameter(name = "date", description = "The date to filter the attendance."),
		}
	)
	@GetMapping("/count/date")
	public long getAttendanceCountByDate(@RequestBody DateWithStatus dateStatus) {
		if (dateStatus.getDateRange() == null) {
			logger.info(AttendanceMessages.ATTENDANCE_NULL);
			return -3;
		}

		Status status = dateStatus.getStatus();
		BetweenDate dateRange = dateStatus.getDateRange();

		if (status == Status.LATE) {
			return this.attendanceService.getAllCountOfAttendanceBetweenDate(
				dateRange.getStartDate(),
				dateRange.getEndDate(),
				Status.LATE
			);
		} else if (status == Status.ONTIME) {
			return this.attendanceService.getAllCountOfAttendanceBetweenDate(
				dateRange.getStartDate(),
				dateRange.getEndDate(),
				Status.ONTIME
			);
		} else {
			return this.attendanceService.countAttendanceBetweenDate(dateRange);
		}
	}

	/**
	 * Retrieves the attendance count based on the given time and status.
	 *
	 * @param timeWithStatus The time and status to filter the attendance count.
	 * @return The count of attendance based on the given time and status.
	 */
	@Operation(
		summary = "Get Attendance Count",
		description = "Retrieves the attendance count based on the given time and status. Returns -3 if the time or status is null.",
		parameters = {
			@Parameter(name = "time", description = "The time period to filter the attendance."),
		}
	)
	@GetMapping("/count/time")
	public long getAttendanceCountByTime(@RequestBody TimeWithStatus timeWithStatus) {
		LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
		LocalDate lastDayOfMonth = today.withDayOfMonth(today.lengthOfMonth());

		LocalDate firstDayOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
		LocalDate lastDayOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

		Status status = timeWithStatus.getStatus();
		String time = timeWithStatus.getTime();

		if (status == null || time == null) {
			logger.info(AttendanceMessages.ATTENDANCE_NULL);
			return -3;
		}

		switch (time) {
			case "month" -> {
				if (status == Status.LATE) {
					return this.attendanceService.getAllCountOfAttendanceBetweenDate(
						firstDayOfMonth,
						lastDayOfMonth,
						Status.LATE
					);
				} else if (status == Status.ONTIME) {
					return this.attendanceService.getAllCountOfAttendanceBetweenDate(
						firstDayOfMonth,
						lastDayOfMonth,
						Status.ONTIME
					);
				} else {
					return this.attendanceService.countAttendanceBetweenDate(new BetweenDate(firstDayOfMonth, lastDayOfMonth));
				}
			}

			case "week" -> {
				if (status == Status.LATE) {
					return this.attendanceService.getAllCountOfAttendanceBetweenDate(
						firstDayOfWeek,
						lastDayOfWeek,
						Status.LATE
					);
				} else if (status == Status.ONTIME) {
					return this.attendanceService.getAllCountOfAttendanceBetweenDate(
						firstDayOfWeek,
						lastDayOfWeek,
						Status.ONTIME
					);
				} else {
					return this.attendanceService.countAttendanceBetweenDate(new BetweenDate(firstDayOfWeek, lastDayOfWeek));
				}
			}

			case "today" -> {
				if (status == Status.LATE) {
					return this.attendanceService.getAllCountOfAttendanceBetweenDate(
						LocalDate.now(),
						LocalDate.now(),
						Status.LATE
					);
				} else if (status == Status.ONTIME) {
					return this.attendanceService.getAllCountOfAttendanceBetweenDate(
						LocalDate.now(),
						LocalDate.now(),
						Status.ONTIME
					);
				} else {
					return this.attendanceService.countAttendanceBetweenDate(new BetweenDate(LocalDate.now(), LocalDate.now()));
				}
			}

			default -> {
				return -1;
			}
		}
	}

	/**
	 * Retrieves the count of students based on the given date and status.
	 *
	 * @param dateStatus the date and status for filtering the student count
	 * @return the count of students based on the given date and status
	 */
	@Operation(
		summary = "Get Student Count",
		description = "Retrieves the count of students based on the given date and status. Returns -2 if the date or status is null.",
		parameters = {
			@Parameter(name = "date", description = "The date to filter the student count"),
		}
	)
	@GetMapping("/student/date")
	public long getStudentCountByDate(@RequestBody DateWithStatusLrn dateStatus) {
		if (dateStatus.getDateRange() == null) {
			logger.info(AttendanceMessages.ATTENDANCE_NULL);
			return -2;
		}

		Status status = dateStatus.getStatus();
		BetweenDate dateRange = dateStatus.getDateRange();
		Long studentLrn = dateStatus.getStudentLrn();

		if (status == Status.LATE) {
			return this.attendanceService.getAllCountOfAttendanceBetweenDate(
				studentLrn,
				dateRange,
				Status.LATE
			);
		} else if (status == Status.ONTIME) {
			return this.attendanceService.getAllCountOfAttendanceBetweenDate(
				studentLrn,
				dateRange,
				Status.ONTIME
			);
		} else {
			return this.attendanceService.countStudentAttendanceBetweenDate(studentLrn, dateRange);
		}
	}

	/**
	 * Retrieves the count of student attendance based on the specified time and status.
	 *
	 * @param timeStatus the time and status information for the attendance count
	 * @return the count of student attendance based on the specified time and status,
	 * or -1 if an error occurs or the specified time is invalid
	 */
	@Operation(
		summary = "Get Student Count",
		description = "Retrieves the count of student attendance based on the specified time and status. Returns -1 if an error occurs or the specified time is invalid.",
		parameters = {
			@Parameter(name = "time", description = "The time period to filter the attendance count"),
		}
	)
	@GetMapping("/student/time")
	public long getStudentCountByTime(@RequestBody TimeWithStatusLrn timeStatus) {
		Status status = timeStatus.getStatus();
		String time = timeStatus.getTime();
		Long studentLrn = timeStatus.getStudentLrn();

		LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
		LocalDate lastDayOfMonth = today.withDayOfMonth(today.lengthOfMonth());

		LocalDate firstDayOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
		LocalDate lastDayOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

		LocalDate now = LocalDate.now();

		switch (time) {
			case "month" -> {
				return getStudentCountByTime(status, studentLrn, firstDayOfMonth, lastDayOfMonth);
			}

			case "week" -> {
				return getStudentCountByTime(status, studentLrn, firstDayOfWeek, lastDayOfWeek);
			}

			case "today" -> {
				if (status == Status.LATE) {
					return this.attendanceService.getAllCountOfAttendanceBetweenDate(
						studentLrn,
						new BetweenDate(now, now),
						Status.LATE
					);
				} else if (status == Status.ONTIME) {
					return this.attendanceService.getAllCountOfAttendanceBetweenDate(
						studentLrn,
						new BetweenDate(now, now),
						Status.ONTIME
					);
				} else {
					return this.attendanceService.countStudentAttendanceBetweenDate(studentLrn, new BetweenDate(LocalDate.now(), LocalDate.now()));
				}
			}

			default -> {
				return -1;
			}
		}
	}

	private long getStudentCountByTime(Status status, Long studentLrn, LocalDate firstDayOfMonth, LocalDate lastDayOfMonth) {
		if (status == Status.LATE) {
			return this.attendanceService.getAllCountOfAttendanceBetweenDate(
				studentLrn,
				new BetweenDate(firstDayOfMonth, lastDayOfMonth),
				Status.LATE
			);
		} else if (status == Status.ONTIME) {
			return this.attendanceService.getAllCountOfAttendanceBetweenDate(
				studentLrn,
				new BetweenDate(firstDayOfMonth, lastDayOfMonth),
				Status.ONTIME
			);
		} else {
			return this.attendanceService.countStudentAttendanceBetweenDate(studentLrn, new BetweenDate(firstDayOfMonth, lastDayOfMonth));
		}
	}

	/**
	 * Retrieves the student attendance between two dates with a specific attendance status.
	 *
	 * @param dateWithStatusLrn the object containing the start and end dates, attendance status, and student LRN
	 * @return an iterable collection of Attendance objects representing the student attendance
	 */
	@Operation(
		summary = "Get Student Attendance",
		description = "Retrieves the student attendance between two dates with a specific attendance status.",
		parameters = {
			@Parameter(name = "Date with Status and LRN", description = "The start and end dates, attendance status, and student LRN. Need to pass DateWithStatusLrn Object."),
		}
	)
	@GetMapping("/student/get/date")
	public Iterable<Attendance> getStudentAttendanceBetweenDate(@RequestBody DateWithStatusLrn dateWithStatusLrn) {
		if (dateWithStatusLrn.getDateRange() == null) {
			logger.info(AttendanceMessages.ATTENDANCE_NULL);
			return null;
		}

		Status status = dateWithStatusLrn.getStatus();
		BetweenDate dateRange = dateWithStatusLrn.getDateRange();
		Long studentLrn = dateWithStatusLrn.getStudentLrn();

		if (status == Status.LATE) {
			return this.attendanceService.getStudentAttendanceBetweenDateWithAttendanceStatus(
				studentLrn,
				dateRange,
				Status.LATE
			);
		} else if (status == Status.ONTIME) {
			return this.attendanceService.getAllAttendanceBetweenDateWithStatus(
				dateRange.getStartDate(),
				dateRange.getEndDate(),
				Status.ONTIME
			);
		} else {
			return this.attendanceService.getAttendanceBetweenDate(studentLrn, dateRange);
		}
	}

	@Operation(
		summary = "Get Student Attendance by Time",
		description = "Retrieves the student attendance between two dates with a specific attendance status.",
		parameters = {
			@Parameter(name = "Time with Status and LRN", description = "The start and end dates, attendance status, and student LRN. Need to pass TimeWithStatusLrn Object."),
		}
	)
	@GetMapping("/student/get/time")
	public Iterable<Attendance> getStudentAttendanceByTime(@RequestBody TimeWithStatusLrn timeWithStatus) {
		Status status = timeWithStatus.getStatus();
		String time = timeWithStatus.getTime();
		Long studentLrn = timeWithStatus.getStudentLrn();

		LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
		LocalDate lastDayOfMonth = today.withDayOfMonth(today.lengthOfMonth());

		LocalDate firstDayOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
		LocalDate lastDayOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

		LocalDate now = LocalDate.now();

		switch (time) {
			case "month" -> {
				if (status == Status.LATE) {
					return this.attendanceService.getStudentAttendanceBetweenDateWithAttendanceStatus(
						studentLrn,
						new BetweenDate(firstDayOfMonth, lastDayOfMonth),
						Status.LATE
					);
				} else if (status == Status.ONTIME) {
					return this.attendanceService.getStudentAttendanceBetweenDateWithAttendanceStatus(
						studentLrn,
						new BetweenDate(firstDayOfMonth, lastDayOfMonth),
						Status.ONTIME
					);
				} else {
					return this.attendanceService.getAttendanceBetweenDate(studentLrn, new BetweenDate(firstDayOfMonth, lastDayOfMonth));
				}
			}

			case "week" -> {
				if (status == Status.LATE) {
					return this.attendanceService.getStudentAttendanceBetweenDateWithAttendanceStatus(
						studentLrn,
						new BetweenDate(firstDayOfWeek, lastDayOfWeek),
						Status.LATE
					);
				} else if (status == Status.ONTIME) {
					return this.attendanceService.getStudentAttendanceBetweenDateWithAttendanceStatus(
						studentLrn,
						new BetweenDate(firstDayOfWeek, lastDayOfWeek),
						Status.ONTIME
					);
				} else {
					return this.attendanceService.getAttendanceBetweenDate(studentLrn, new BetweenDate(firstDayOfWeek, lastDayOfWeek));
				}
			}

			case "today" -> {
				if (status == Status.LATE) {
					return this.attendanceService.getStudentAttendanceBetweenDateWithAttendanceStatus(
						studentLrn,
						new BetweenDate(now, now),
						Status.LATE
					);
				} else if (status == Status.ONTIME) {
					return this.attendanceService.getStudentAttendanceBetweenDateWithAttendanceStatus(
						studentLrn,
						new BetweenDate(now, now),
						Status.ONTIME
					);
				} else {
					return this.attendanceService.getAttendanceBetweenDate(studentLrn, new BetweenDate(LocalDate.now(), LocalDate.now()));
				}
			}

			default -> {
				return null;
			}
		}
	}

	/**
	 * Retrieves the attendance records for today.
	 *
	 * @return an iterable collection of Attendance objects representing the attendance records for today.
	 */
	@Operation(
		summary = "Get Today's Attendance",
		description = "Retrieves the attendance records for today."
	)
	@GetMapping("/today")
	public Iterable<Attendance> getTodayAttendance() {
		LocalDate now = LocalDate.now();
		return this.attendanceService.getAllAttendanceBetweenDate(now, now);
	}

	/**
	 * Retrieves the attendance records for the current week.
	 *
	 * @return an Iterable of Attendance objects representing the attendance records for the week
	 */
	@Operation(
		summary = "Get Week's Attendance",
		description = "Retrieves the attendance records for the current week."
	)
	@GetMapping("/week")
	public Iterable<Attendance> getWeekAttendance() {
		LocalDate firstDayOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
		LocalDate lastDayOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
		return this.attendanceService.getAllAttendanceBetweenDate(firstDayOfWeek, lastDayOfWeek);
	}

	/**
	 * Retrieves today's on-time attendance.
	 *
	 * @return an iterable collection of Attendance objects representing the on-time attendance for today.
	 */
	@Operation(
		summary = "Get Today's On-Time Attendance",
		description = "Retrieves today's on-time attendance."
	)
	@GetMapping("/today/ontime")
	public Iterable<Attendance> getTodayOnTimeAttendance() {
		LocalDate now = LocalDate.now();
		return this.attendanceService.getAllAttendanceBetweenDateWithStatus(now, now, Status.ONTIME);
	}

	/**
	 * Retrieves the on-time attendance records for the current week.
	 *
	 * @return An iterable collection of Attendance objects representing the on-time attendance records.
	 */
	@Operation(
		summary = "Get Week's On-Time Attendance",
		description = "Retrieves the on-time attendance records for the current week."
	)
	@GetMapping("/week/ontime")
	public Iterable<Attendance> getWeekOnTimeAttendance() {
		LocalDate firstDayOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
		LocalDate lastDayOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
		return this.attendanceService.getAllAttendanceBetweenDateWithStatus(firstDayOfWeek, lastDayOfWeek, Status.ONTIME);
	}

	/**
	 * Retrieves the late attendance records for today.
	 *
	 * @return an iterable collection of Attendance objects representing the late attendance records for today.
	 */
	@Operation(
		summary = "Get Today's Late Attendance",
		description = "Retrieves the late attendance records for today."
	)
	@GetMapping("/today/late")
	public Iterable<Attendance> getTodayLateAttendance() {
		LocalDate now = LocalDate.now();
		return this.attendanceService.getAllAttendanceBetweenDateWithStatus(now, now, Status.LATE);
	}

	/**
	 * Retrieves the late attendance records for the current week.
	 *
	 * @return An iterable collection of Attendance objects representing the late attendance records for the week.
	 */
	@Operation(
		summary = "Get Week's Late Attendance",
		description = "Retrieves the late attendance records for the current week."
	)
	@GetMapping("/week/late")
	public Iterable<Attendance> getWeekLateAttendance() {
		LocalDate firstDayOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
		LocalDate lastDayOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
		return this.attendanceService.getAllAttendanceBetweenDateWithStatus(firstDayOfWeek, lastDayOfWeek, Status.LATE);
	}

	/**
	 * Retrieves the attendance records for the current month.
	 *
	 * @return An iterable collection of Attendance objects representing the attendance records for the month.
	 */
	@Operation(
		summary = "Get Month's Attendance",
		description = "Retrieves the attendance records for the current month."
	)
	@GetMapping("/month")
	public Iterable<Attendance> getMonthAttendance() {
		LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
		LocalDate lastDayOfMonth = today.withDayOfMonth(today.lengthOfMonth());
		return this.attendanceService.getAllAttendanceBetweenDate(firstDayOfMonth, lastDayOfMonth);
	}

	@Operation(
		summary = "Get Month's On-Time Attendance",
		description = "Retrieves the on-time attendance records for the current month."
	)
	@GetMapping("/month/ontime")
	public Iterable<Attendance> getMonthOnTimeAttendance() {
		LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
		LocalDate lastDayOfMonth = today.withDayOfMonth(today.lengthOfMonth());
		return this.attendanceService.getAllAttendanceBetweenDateWithStatus(firstDayOfMonth, lastDayOfMonth, Status.ONTIME);
	}

	/**
	 * Retrieves the late attendance records for the current month.
	 *
	 * @return An iterable collection of Attendance objects representing the late attendance records for the current month.
	 */
	@Operation(
		summary = "Get Month's Late Attendance",
		description = "Retrieves the late attendance records for the current month."
	)
	@GetMapping("/month/late")
	public Iterable<Attendance> getMonthLateAttendance() {
		LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
		LocalDate lastDayOfMonth = today.withDayOfMonth(today.lengthOfMonth());
		return this.attendanceService.getAllAttendanceBetweenDateWithStatus(firstDayOfMonth, lastDayOfMonth, Status.LATE);
	}

	@Operation(
		summary = "Get Today's Section Attendance",
		description = "Retrieves the attendance records for today."
	)
	@GetMapping("/section/today")
	public Iterable<Attendance> getSectionAttendanceToday(@NonNull @RequestBody Section section) {
		LocalDate dateNow = LocalDate.now();
		return (section.getSectionId() <= 0) ? Collections.emptyList() : this.attendanceService.getAttendanceInSection(section.getSectionId(), new BetweenDate(dateNow, dateNow));
	}

	@Operation(
		summary = "Get Week's Section Attendance",
		description = "Retrieves the attendance records for the current week."
	)
	@GetMapping("/section/week")
	public Iterable<Attendance> getSectionAttendanceWeek(@NonNull @RequestBody Section section) {
		LocalDate firstDayWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
		LocalDate lastDayWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
		return (section.getSectionId() <= 0) ? Collections.emptyList() : this.attendanceService.getAttendanceInSection(section.getSectionId(), new BetweenDate(firstDayWeek, lastDayWeek));
	}

	@Operation(
		summary = "Get Month's Section Attendance",
		description = "Retrieves the attendance records for the current month."
	)
	@GetMapping("/section/month")
	public Iterable<Attendance> getSectionAttendanceMonth(@NonNull @RequestBody Section section) {
		LocalDate firstDayMonth = today.withDayOfMonth(1);
		LocalDate lastDayMonth = today.withDayOfMonth(today.lengthOfMonth());
		return this.attendanceService.getAttendanceInSection(section.getSectionId(), new BetweenDate(firstDayMonth, lastDayMonth));
	}
}

