package com.pshs.attendancesystem.controllers.attendance.statistics;

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
@RequestMapping("/v1/attendance/stats/")
public class AttendanceStatisticsController {
	private final AttendanceService attendanceService;
	private final Logger logger = LoggerFactory.getLogger(getClass());
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

		return attendanceService.getAttendanceBetweenDate(
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
	@PostMapping("/get/date")
	public Iterable<Attendance> getStudentsAttendanceByDate(@RequestBody DateRangeWithStatus dateStatus) {
		if (dateStatus.getDateRange() == null) {
			logger.info(AttendanceMessages.ATTENDANCE_NULL);
			return null;
		}

		Status status = dateStatus.getStatus();
		BetweenDate betweenDate = dateStatus.getDateRange();

		if (status == Status.LATE) {
			return attendanceService.getAllAttendanceBetweenDateWithStatus(
				betweenDate.getStartDate(),
				betweenDate.getEndDate(),
				Status.LATE
			);
		} else if (status == Status.ONTIME) {
			return attendanceService.getAllAttendanceBetweenDateWithStatus(
				betweenDate.getStartDate(),
				betweenDate.getEndDate(),
				Status.ONTIME
			);
		} else {
			return attendanceService.getAllAttendanceBetweenDate(betweenDate.getStartDate(), betweenDate.getEndDate());
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
	@PostMapping("/get/time")
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
					return attendanceService.getAllAttendanceBetweenDateWithStatus(
						firstDayOfMonth,
						lastDayOfMonth,
						Status.LATE
					);
				} else if (status == Status.ONTIME) {
					return attendanceService.getAllAttendanceBetweenDateWithStatus(
						firstDayOfMonth,
						lastDayOfMonth,
						Status.ONTIME
					);
				} else {
					return attendanceService.getAllAttendanceBetweenDate(firstDayOfMonth, lastDayOfMonth);
				}

			case "week":
				if (status == Status.LATE) {
					return attendanceService.getAllAttendanceBetweenDateWithStatus(
						firstDayOfWeek,
						lastDayOfWeek,
						Status.LATE
					);
				} else if (status == Status.ONTIME) {
					return attendanceService.getAllAttendanceBetweenDateWithStatus(
						firstDayOfWeek,
						lastDayOfWeek,
						Status.ONTIME
					);
				} else {
					return attendanceService.getAllAttendanceBetweenDate(firstDayOfWeek, lastDayOfWeek);
				}

			case "today":
				if (status == Status.LATE) {
					return attendanceService.getAllAttendanceBetweenDateWithStatus(
						LocalDate.now(),
						LocalDate.now(),
						Status.LATE
					);
				} else if (status == Status.ONTIME) {
					return attendanceService.getAllAttendanceBetweenDateWithStatus(
						LocalDate.now(),
						LocalDate.now(),
						Status.ONTIME
					);
				} else {
					return attendanceService.getAllAttendanceBetweenDate(LocalDate.now(), LocalDate.now());
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
		description = "Retrieves the attendance count by date. Returns -3 if the date range is null."
	)
	@PostMapping("/count/date")
	public long getAttendanceCountByDate(@RequestBody DateRangeWithStatus dateStatus) {
		if (dateStatus.getDateRange() == null) {
			logger.info(AttendanceMessages.ATTENDANCE_NULL);
			return -3;
		}

		Status status = dateStatus.getStatus();
		BetweenDate dateRange = dateStatus.getDateRange();

		if (status == Status.LATE) {
			return attendanceService.getAllCountOfAttendanceBetweenDate(
				dateRange.getStartDate(),
				dateRange.getEndDate(),
				Status.LATE
			);
		} else if (status == Status.ONTIME) {
			return attendanceService.getAllCountOfAttendanceBetweenDate(
				dateRange.getStartDate(),
				dateRange.getEndDate(),
				Status.ONTIME
			);
		} else {
			return attendanceService.countAttendanceBetweenDate(dateRange);
		}
	}

	/**
	 * Retrieves the attendance count based on the given time and status.
	 *
	 * @param timeStatus The time and status to filter the attendance count.
	 * @return The count of attendance based on the given time and status.
	 */
	@Operation(
		summary = "Get Attendance Count",
		description = "Retrieves the attendance count based on the given time and status. Returns -3 if the time or status is null.",
		parameters = {
			@Parameter(name = "timeStatus", description = "The time to filter the attendance."),
		}
	)
	@PostMapping(value = "/count/time")
	public long getAttendanceCountByTime(@RequestBody TimeWithStatus timeStatus) {
		LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
		LocalDate lastDayOfMonth = today.withDayOfMonth(today.lengthOfMonth());

		LocalDate firstDayOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
		LocalDate lastDayOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

		Status status = timeStatus.getStatus();
		String time = timeStatus.getTime();

		if (status == null || time == null) {
			logger.info(AttendanceMessages.ATTENDANCE_NULL);
			return -3;
		}

		switch (time) {
			case "month" -> {
				return countOfAttendanceByTime(firstDayOfMonth, lastDayOfMonth, status);
			}

			case "week" -> {
				return countOfAttendanceByTime(firstDayOfWeek, lastDayOfWeek, status);
			}

			case "today" -> {
				if (status == Status.LATE) {
					return attendanceService.getAllCountOfAttendanceBetweenDate(
						LocalDate.now(),
						LocalDate.now(),
						Status.LATE
					);
				} else if (status == Status.ONTIME) {
					return attendanceService.getAllCountOfAttendanceBetweenDate(
						LocalDate.now(),
						LocalDate.now(),
						Status.ONTIME
					);
				} else {
					return attendanceService.countAttendanceBetweenDate(new BetweenDate(LocalDate.now(), LocalDate.now()));
				}
			}

			default -> {
				return -1;
			}
		}
	}

	private long countOfAttendanceByTime(LocalDate firstDayOfMonth, LocalDate lastDayOfMonth, Status status) {
		if (status == Status.LATE) {
			return attendanceService.getAllCountOfAttendanceBetweenDate(
				firstDayOfMonth,
				lastDayOfMonth,
				Status.LATE
			);
		} else if (status == Status.ONTIME) {
			return attendanceService.getAllCountOfAttendanceBetweenDate(
				firstDayOfMonth,
				lastDayOfMonth,
				Status.ONTIME
			);
		} else {
			return attendanceService.countAttendanceBetweenDate(new BetweenDate(firstDayOfMonth, lastDayOfMonth));
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
	@PostMapping("/student/date")
	public long getStudentCountByDate(@RequestBody DateRangeWithStatusLrn dateStatus) {
		if (dateStatus.getDateRange() == null) {
			logger.info(AttendanceMessages.ATTENDANCE_NULL);
			return -2;
		}

		Status status = dateStatus.getStatus();
		BetweenDate dateRange = dateStatus.getDateRange();
		Long studentLrn = dateStatus.getStudentLrn();

		if (status == Status.LATE) {
			return attendanceService.getAllCountOfAttendanceBetweenDate(
				studentLrn,
				dateRange,
				Status.LATE
			);
		} else if (status == Status.ONTIME) {
			return attendanceService.getAllCountOfAttendanceBetweenDate(
				studentLrn,
				dateRange,
				Status.ONTIME
			);
		} else {
			return attendanceService.countStudentAttendanceBetweenDate(studentLrn, dateRange);
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
	@PostMapping("/student/time")
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
					return attendanceService.getAllCountOfAttendanceBetweenDate(
						studentLrn,
						new BetweenDate(now, now),
						Status.LATE
					);
				} else if (status == Status.ONTIME) {
					return attendanceService.getAllCountOfAttendanceBetweenDate(
						studentLrn,
						new BetweenDate(now, now),
						Status.ONTIME
					);
				} else {
					return attendanceService.countStudentAttendanceBetweenDate(studentLrn, new BetweenDate(LocalDate.now(), LocalDate.now()));
				}
			}

			default -> {
				return -1;
			}
		}
	}

	private long getStudentCountByTime(Status status, Long studentLrn, LocalDate firstDayOfMonth, LocalDate lastDayOfMonth) {
		if (status == Status.LATE) {
			return attendanceService.getAllCountOfAttendanceBetweenDate(
				studentLrn,
				new BetweenDate(firstDayOfMonth, lastDayOfMonth),
				Status.LATE
			);
		} else if (status == Status.ONTIME) {
			return attendanceService.getAllCountOfAttendanceBetweenDate(
				studentLrn,
				new BetweenDate(firstDayOfMonth, lastDayOfMonth),
				Status.ONTIME
			);
		} else {
			return attendanceService.countStudentAttendanceBetweenDate(studentLrn, new BetweenDate(firstDayOfMonth, lastDayOfMonth));
		}
	}

	/**
	 * Retrieves the student attendance between two dates with a specific attendance status.
	 *
	 * @param dateRangeWithStatusLrn the object containing the start and end dates, attendance status, and student LRN
	 * @return an iterable collection of Attendance objects representing the student attendance
	 */
	@Operation(
		summary = "Get Student Attendance",
		description = "Retrieves the student attendance between two dates with a specific attendance status.",
		parameters = {
			@Parameter(name = "Date with Status and LRN", description = "The start and end dates, attendance status, and student LRN. Need to pass DateWithStatusLrn Object."),
		}
	)
	@PostMapping("/student/get/date")
	public Iterable<Attendance> getStudentAttendanceBetweenDate(@RequestBody DateRangeWithStatusLrn dateRangeWithStatusLrn) {
		if (dateRangeWithStatusLrn.getDateRange() == null) {
			logger.info(AttendanceMessages.ATTENDANCE_NULL);
			return null;
		}

		Status status = dateRangeWithStatusLrn.getStatus();
		BetweenDate dateRange = dateRangeWithStatusLrn.getDateRange();
		Long studentLrn = dateRangeWithStatusLrn.getStudentLrn();

		if (status == Status.LATE) {
			return attendanceService.getStudentAttendanceBetweenDateWithAttendanceStatus(
				studentLrn,
				dateRange,
				Status.LATE
			);
		} else if (status == Status.ONTIME) {
			return attendanceService.getAllAttendanceBetweenDateWithStatus(
				dateRange.getStartDate(),
				dateRange.getEndDate(),
				Status.ONTIME
			);
		} else {
			return attendanceService.getAttendanceBetweenDate(studentLrn, dateRange);
		}
	}

	@Operation(
		summary = "Get Student Attendance by Time",
		description = "Retrieves the student attendance between two dates with a specific attendance status.",
		parameters = {
			@Parameter(name = "Time with Status and LRN", description = "The start and end dates, attendance status, and student LRN. Need to pass TimeWithStatusLrn Object."),
		}
	)
	@PostMapping("/student/get/time")
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
					return attendanceService.getStudentAttendanceBetweenDateWithAttendanceStatus(
						studentLrn,
						new BetweenDate(firstDayOfMonth, lastDayOfMonth),
						Status.LATE
					);
				} else if (status == Status.ONTIME) {
					return attendanceService.getStudentAttendanceBetweenDateWithAttendanceStatus(
						studentLrn,
						new BetweenDate(firstDayOfMonth, lastDayOfMonth),
						Status.ONTIME
					);
				} else {
					return attendanceService.getAttendanceBetweenDate(studentLrn, new BetweenDate(firstDayOfMonth, lastDayOfMonth));
				}
			}

			case "week" -> {
				if (status == Status.LATE) {
					return attendanceService.getStudentAttendanceBetweenDateWithAttendanceStatus(
						studentLrn,
						new BetweenDate(firstDayOfWeek, lastDayOfWeek),
						Status.LATE
					);
				} else if (status == Status.ONTIME) {
					return attendanceService.getStudentAttendanceBetweenDateWithAttendanceStatus(
						studentLrn,
						new BetweenDate(firstDayOfWeek, lastDayOfWeek),
						Status.ONTIME
					);
				} else {
					return attendanceService.getAttendanceBetweenDate(studentLrn, new BetweenDate(firstDayOfWeek, lastDayOfWeek));
				}
			}

			case "today" -> {
				if (status == Status.LATE) {
					return attendanceService.getStudentAttendanceBetweenDateWithAttendanceStatus(
						studentLrn,
						new BetweenDate(now, now),
						Status.LATE
					);
				} else if (status == Status.ONTIME) {
					return attendanceService.getStudentAttendanceBetweenDateWithAttendanceStatus(
						studentLrn,
						new BetweenDate(now, now),
						Status.ONTIME
					);
				} else {
					return attendanceService.getAttendanceBetweenDate(studentLrn, new BetweenDate(LocalDate.now(), LocalDate.now()));
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
		return attendanceService.getAllAttendanceBetweenDate(now, now);
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
		return attendanceService.getAllAttendanceBetweenDate(firstDayOfWeek, lastDayOfWeek);
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
		return attendanceService.getAllAttendanceBetweenDateWithStatus(now, now, Status.ONTIME);
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
		return attendanceService.getAllAttendanceBetweenDateWithStatus(firstDayOfWeek, lastDayOfWeek, Status.ONTIME);
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
		return attendanceService.getAllAttendanceBetweenDateWithStatus(now, now, Status.LATE);
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
		return attendanceService.getAllAttendanceBetweenDateWithStatus(firstDayOfWeek, lastDayOfWeek, Status.LATE);
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
		return attendanceService.getAllAttendanceBetweenDate(firstDayOfMonth, lastDayOfMonth);
	}

	@Operation(
		summary = "Get Month's On-Time Attendance",
		description = "Retrieves the on-time attendance records for the current month."
	)
	@GetMapping("/month/ontime")
	public Iterable<Attendance> getMonthOnTimeAttendance() {
		LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
		LocalDate lastDayOfMonth = today.withDayOfMonth(today.lengthOfMonth());
		return attendanceService.getAllAttendanceBetweenDateWithStatus(firstDayOfMonth, lastDayOfMonth, Status.ONTIME);
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
		return attendanceService.getAllAttendanceBetweenDateWithStatus(firstDayOfMonth, lastDayOfMonth, Status.LATE);
	}

	@Operation(
		summary = "Get Today's Section Attendance",
		description = "Retrieves the attendance records for today."
	)
	@PostMapping("/section/today")
	public Iterable<Attendance> getSectionAttendanceToday(@NonNull @RequestBody Section section) {
		LocalDate dateNow = LocalDate.now();
		return (section.getSectionId() <= 0) ? Collections.emptyList() : attendanceService.getAttendanceInSection(section.getSectionId(), new BetweenDate(dateNow, dateNow));
	}

	@Operation(
		summary = "Get Week's Section Attendance",
		description = "Retrieves the attendance records for the current week."
	)
	@PostMapping("/section/week")
	public Iterable<Attendance> getSectionAttendanceWeek(@NonNull @RequestBody Section section) {
		LocalDate firstDayWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
		LocalDate lastDayWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
		return (section.getSectionId() <= 0) ? Collections.emptyList() : attendanceService.getAttendanceInSection(section.getSectionId(), new BetweenDate(firstDayWeek, lastDayWeek));
	}

	@Operation(
		summary = "Get Month's Section Attendance",
		description = "Retrieves the attendance records for the current month."
	)
	@PostMapping("/section/month")
	public Iterable<Attendance> getSectionAttendanceMonth(@NonNull @RequestBody Section section) {
		LocalDate firstDayMonth = today.withDayOfMonth(1);
		LocalDate lastDayMonth = today.withDayOfMonth(today.lengthOfMonth());
		return attendanceService.getAttendanceInSection(section.getSectionId(), new BetweenDate(firstDayMonth, lastDayMonth));
	}

	@Operation(
		summary = "Get Section's Attendance with date range",
		description = "Retrieves the attendance records for between the date you provided with Status filtering.",
		parameters = {
			@Parameter(name = "sectionId", description = "The ID of the section.", required = true),
			@Parameter(name = "status", description = "The status to filter the attendance. If provided null, no late and on time filtering."),
			@Parameter(name = "dateRange", description = "The date range to filter the attendance.", required = true),
		}
	)
	@PostMapping("/section")
	public Iterable<Attendance> getAttendanceInSection(@RequestParam Integer sectionId, @RequestParam Status status, @RequestBody BetweenDate dateRange) {
		if (status == null) {
			return attendanceService.getAttendanceInSection(sectionId, dateRange);
		} else {
			return attendanceService.getAttendanceInSection(sectionId, dateRange, status);
		}
	}

	@Operation(
		summary = "Get Section's Attendance Count with date range",
		description = "Retrieves the attendance records for between the date you provided with Status filtering.",
		parameters = {
			@Parameter(name = "sectionId", description = "The ID of the section.", required = true),
			@Parameter(name = "status", description = "The status to filter the attendance.", required = true)
		}
	)
	@PostMapping("/count/section")
	public long getAttendanceCountInSection(@RequestParam Integer sectionId, @RequestParam Status status, @RequestBody BetweenDate dateRange) {
		return attendanceService.countAttendanceInSection(sectionId, dateRange, status);
	}

	@Operation(
		summary = "Get Section's Attendance Count between date",
		description = "Retrieves the attendance records for the date you provided. NOTE: If status is null, " +
			"the attendance count will be returned without the filter of ONTIME or LATE and date should not be null.",
		parameters = {
			@Parameter(name = "sectionId", description = "The ID of the section.")
		}
	)
	@PostMapping("/count/section/date")
	public long getAttendanceCountInSectionByStatusAndDate(@RequestParam Integer sectionId, @RequestBody DateWithStatus dateStatus) {
		if (dateStatus.getDate() == null) {
			logger.info(AttendanceMessages.ATTENDANCE_NULL);
			return -2;
		}

		if (dateStatus.getStatus() == null) {
			return attendanceService.countAttendanceBySectionAndDate(sectionId, dateStatus.getDate());
		} else {
			return attendanceService.countAttendanceInSectionByStatusAndDate(sectionId, dateStatus.getStatus(), dateStatus.getDate());
		}
	}
}

