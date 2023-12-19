package com.pshs.attendancesystem.controllers.attendance.statistics;

import com.pshs.attendancesystem.entities.Attendance;
import com.pshs.attendancesystem.entities.Section;
import com.pshs.attendancesystem.entities.statistics.*;
import com.pshs.attendancesystem.enums.Status;
import com.pshs.attendancesystem.impl.AttendanceServiceImpl;
import com.pshs.attendancesystem.impl.ConfigurationService;
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
@RequestMapping("${api.root}/attendance/stats")
public class AttendanceStatisticsController {
	private final AttendanceService attendanceService;
	private final Logger logger = LoggerFactory.getLogger(getClass());

	public AttendanceStatisticsController(AttendanceRepository attendanceRepository,
	                                      StudentRepository studentRepository, ConfigurationService configurationService) {
		attendanceService = new AttendanceServiceImpl(attendanceRepository, studentRepository, configurationService);
	}

	private long countAttendanceBetweenDateStatus(DateRangeWithStatus dateRangeStatus) {
		if (dateRangeStatus.getStatus() == Status.LATE) {
			return attendanceService.getAllCountOfAttendanceBetweenDate(
				dateRangeStatus.getDateRange(),
				Status.LATE
			);
		} else if (dateRangeStatus.getStatus() == Status.ONTIME) {
			return attendanceService.getAllCountOfAttendanceBetweenDate(
				dateRangeStatus.getDateRange(),
				Status.ONTIME
			);
		} else {
			return attendanceService.countAttendanceBetweenDate(dateRangeStatus.getDateRange());
		}
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
	@GetMapping("/student") // TODO: Make it between date.
	public Iterable<Attendance> getStudentAttendanceBetweenDate(@RequestParam("q") Long studentLrn) {
		if (studentLrn == null) {
			logger.info(AttendanceMessages.ATTENDANCE_NULL);
			return null;
		}

		DateRange startEndYear = new DateRange(LocalDate.now().withDayOfYear(1), LocalDate.now().withDayOfYear(LocalDate.now().lengthOfYear()));

		return attendanceService.getAttendanceBetweenDate(
			studentLrn,
			startEndYear);
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
	@PostMapping("/all/date")
	public Iterable<Attendance> getAllStudentsAttendanceByDate(@RequestBody DateRangeWithStatus dateStatus) {
		if (dateStatus.getDateRange() == null) {
			logger.info(AttendanceMessages.ATTENDANCE_NULL);
			return null;
		}

		Status status = dateStatus.getStatus();
		DateRange dateRange = dateStatus.getDateRange();

		if (status == Status.LATE) {
			return attendanceService.getAllAttendanceBetweenDateWithStatus(
				dateRange,
				Status.LATE
			);
		} else if (status == Status.ONTIME) {
			return attendanceService.getAllAttendanceBetweenDateWithStatus(
				dateRange,
				Status.ONTIME
			);
		} else {
			return attendanceService.getAllAttendanceBetweenDate(dateRange);
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
	@PostMapping("/all/time")
	public Iterable<Attendance> getAllStudentsAttendanceByTime(@RequestBody DayWithStatus timeStatus) {
		Status status = timeStatus.getStatus();
		String time = timeStatus.getTime();

		if (time == null) {
			logger.info(AttendanceMessages.ATTENDANCE_NULL);
			return null;
		}

		LocalDate today = LocalDate.now();
		DateRange START_END_WEEK = new DateRange(
			today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)),
			today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
		);

		DateRange START_END_MONTH = new DateRange(
			today.withDayOfMonth(1),
			today.withDayOfMonth(today.lengthOfMonth())
		);

		DateRange START_END_TODAY = new DateRange(
			today,
			today
		);

		switch (time) {
			case "month":
				if (status == Status.LATE) {
					return attendanceService.getAllAttendanceBetweenDateWithStatus(
						START_END_MONTH,
						Status.LATE
					);
				} else if (status == Status.ONTIME) {
					return attendanceService.getAllAttendanceBetweenDateWithStatus(
						START_END_MONTH,
						Status.ONTIME
					);
				} else {
					return attendanceService.getAllAttendanceBetweenDate(START_END_MONTH);
				}

			case "week":
				if (status == Status.LATE) {
					return attendanceService.getAllAttendanceBetweenDateWithStatus(
						START_END_WEEK,
						Status.LATE
					);
				} else if (status == Status.ONTIME) {
					return attendanceService.getAllAttendanceBetweenDateWithStatus(
						START_END_WEEK,
						Status.ONTIME
					);
				} else {
					return attendanceService.getAllAttendanceBetweenDate(START_END_WEEK);
				}

			case "today":
				if (status == Status.LATE) {
					return attendanceService.getAllAttendanceBetweenDateWithStatus(
						START_END_TODAY,
						Status.LATE
					);
				} else if (status == Status.ONTIME) {
					return attendanceService.getAllAttendanceBetweenDateWithStatus(
						START_END_TODAY,
						Status.ONTIME
					);
				} else {
					return attendanceService.getAllAttendanceBetweenDate(START_END_TODAY);
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
	public long countAttendanceByDate(@RequestBody DateRangeWithStatus dateStatus) {
		if (dateStatus.getDateRange() == null) {
			logger.info(AttendanceMessages.ATTENDANCE_NULL);
			return -3;
		}

		Status status = dateStatus.getStatus();
		DateRange dateRange = dateStatus.getDateRange();

		if (status == Status.LATE) {
			return attendanceService.getAllCountOfAttendanceBetweenDate(
				dateRange,
				Status.LATE
			);
		} else if (status == Status.ONTIME) {
			return attendanceService.getAllCountOfAttendanceBetweenDate(
				dateRange,
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
	public long countAttendanceByDay(@RequestBody DayWithStatus timeStatus) {
		Status status = timeStatus.getStatus();
		String time = timeStatus.getTime();

		if (status == null || time == null) {
			logger.info(AttendanceMessages.ATTENDANCE_NULL);
			return -3;
		}

		LocalDate today = LocalDate.now();
		DateRange START_END_WEEK = new DateRange(
			today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)),
			today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
		);

		DateRange START_END_MONTH = new DateRange(
			today.withDayOfMonth(1),
			today.withDayOfMonth(today.lengthOfMonth())
		);

		DateRange START_END_TODAY = new DateRange(
			today,
			today
		);

		switch (time) {
			case "month" -> {
				return countAttendanceBetweenDateStatus(new DateRangeWithStatus(START_END_MONTH, status));
			}

			case "week" -> {
				return countAttendanceBetweenDateStatus(new DateRangeWithStatus(START_END_WEEK, status));
			}

			case "today" -> {
				return countAttendanceBetweenDateStatus(new DateRangeWithStatus(START_END_TODAY, status));
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
	@PostMapping("/student/date")
	public long getStudentCountByDate(@RequestBody DateRangeWithStatusLrn dateStatus) {
		if (dateStatus.getDateRange() == null) {
			logger.info(AttendanceMessages.ATTENDANCE_NULL);
			return -2;
		}

		Status status = dateStatus.getStatus();
		DateRange dateRange = dateStatus.getDateRange();
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
	public long countStudentAttendanceByTime(@RequestBody TimeWithStatusLrn timeStatus) {
		Status status = timeStatus.getStatus();
		String time = timeStatus.getTime();
		Long studentLrn = timeStatus.getStudentLrn();

		LocalDate today = LocalDate.now();
		DateRange START_END_WEEK = new DateRange(
			today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)),
			today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
		);

		DateRange START_END_MONTH = new DateRange(
			today.withDayOfMonth(1),
			today.withDayOfMonth(today.lengthOfMonth())
		);

		DateRange START_END_TODAY = new DateRange(
			today,
			today
		);

		switch (time) {
			case "month" -> {
				return getStudentCountByTime(status, studentLrn, START_END_MONTH);
			}

			case "week" -> {
				return getStudentCountByTime(status, studentLrn, START_END_WEEK);
			}

			case "today" -> {
				if (status == Status.LATE) {
					return attendanceService.getAllCountOfAttendanceBetweenDate(
						studentLrn,
						START_END_TODAY,
						Status.LATE
					);
				} else if (status == Status.ONTIME) {
					return attendanceService.getAllCountOfAttendanceBetweenDate(
						studentLrn,
						START_END_TODAY,
						Status.ONTIME
					);
				} else {
					return attendanceService.countStudentAttendanceBetweenDate(studentLrn, START_END_TODAY);
				}
			}

			default -> {
				return -1;
			}
		}
	}

	private long getStudentCountByTime(Status status, Long studentLrn, DateRange dateRange) {
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
		DateRange dateRange = dateRangeWithStatusLrn.getDateRange();
		Long studentLrn = dateRangeWithStatusLrn.getStudentLrn();

		if (status == Status.LATE) {
			return attendanceService.getStudentAttendanceBetweenDateStatus(
				studentLrn,
				dateRange,
				Status.LATE
			);
		} else if (status == Status.ONTIME) {
			return attendanceService.getAllAttendanceBetweenDateWithStatus(
				dateRange,
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
	@PostMapping("/student/all/time")
	public Iterable<Attendance> getStudentAttendanceByTime(@RequestBody TimeWithStatusLrn timeWithStatus) {
		Status status = timeWithStatus.getStatus();
		String time = timeWithStatus.getTime();
		Long studentLrn = timeWithStatus.getStudentLrn();

		LocalDate today = LocalDate.now();
		DateRange START_END_WEEK = new DateRange(
			today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)),
			today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
		);

		DateRange START_END_MONTH = new DateRange(
			today.withDayOfMonth(1),
			today.withDayOfMonth(today.lengthOfMonth())
		);

		DateRange START_END_TODAY = new DateRange(
			today,
			today
		);

		switch (time) {
			case "month" -> {
				if (status == Status.LATE) {
					return attendanceService.getStudentAttendanceBetweenDateStatus(
						studentLrn,
						START_END_MONTH,
						Status.LATE
					);
				} else if (status == Status.ONTIME) {
					return attendanceService.getStudentAttendanceBetweenDateStatus(
						studentLrn,
						START_END_MONTH,
						Status.ONTIME
					);
				} else {
					return attendanceService.getAttendanceBetweenDate(studentLrn, START_END_MONTH);
				}
			}

			case "week" -> {
				if (status == Status.LATE) {
					return attendanceService.getStudentAttendanceBetweenDateStatus(
						studentLrn,
						START_END_WEEK,
						Status.LATE
					);
				} else if (status == Status.ONTIME) {
					return attendanceService.getStudentAttendanceBetweenDateStatus(
						studentLrn,
						START_END_WEEK,
						Status.ONTIME
					);
				} else {
					return attendanceService.getAttendanceBetweenDate(studentLrn, START_END_WEEK);
				}
			}

			case "today" -> {
				if (status == Status.LATE) {
					return attendanceService.getStudentAttendanceBetweenDateStatus(
						studentLrn,
						START_END_TODAY,
						Status.LATE
					);
				} else if (status == Status.ONTIME) {
					return attendanceService.getStudentAttendanceBetweenDateStatus(
						studentLrn,
						START_END_TODAY,
						Status.ONTIME
					);
				} else {
					return attendanceService.getAttendanceBetweenDate(studentLrn, new DateRange(LocalDate.now(), LocalDate.now()));
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
		LocalDate today = LocalDate.now();

		DateRange START_END_TODAY = new DateRange(
			today,
			today
		);

		return attendanceService.getAllAttendanceBetweenDate(
			START_END_TODAY
		);
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
		LocalDate today = LocalDate.now();
		DateRange START_END_WEEK = new DateRange(
			today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)),
			today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
		);

		return attendanceService.getAllAttendanceBetweenDate(
			START_END_WEEK
		);
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
		LocalDate today = LocalDate.now();

		DateRange START_END_TODAY = new DateRange(
			today,
			today
		);

		return attendanceService.getAllAttendanceBetweenDateWithStatus(START_END_TODAY, Status.ONTIME);
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
		LocalDate today = LocalDate.now();
		DateRange START_END_WEEK = new DateRange(
			today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)),
			today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
		);

		return attendanceService.getAllAttendanceBetweenDateWithStatus(START_END_WEEK, Status.ONTIME);
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
		LocalDate today = LocalDate.now();

		DateRange START_END_TODAY = new DateRange(
			today,
			today
		);
		return attendanceService.getAllAttendanceBetweenDateWithStatus(START_END_TODAY, Status.LATE);
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
		LocalDate today = LocalDate.now();
		DateRange START_END_WEEK = new DateRange(
			today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)),
			today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
		);

		return attendanceService.getAllAttendanceBetweenDateWithStatus(START_END_WEEK, Status.LATE);
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
		LocalDate today = LocalDate.now();

		DateRange START_END_MONTH = new DateRange(
			today.withDayOfMonth(1),
			today.withDayOfMonth(today.lengthOfMonth())
		);

		return attendanceService.getAllAttendanceBetweenDate(
			START_END_MONTH
		);
	}

	@Operation(
		summary = "Get Month's On-Time Attendance",
		description = "Retrieves the on-time attendance records for the current month."
	)
	@GetMapping("/month/ontime")
	public Iterable<Attendance> getMonthOnTimeAttendance() {
		LocalDate today = LocalDate.now();

		DateRange START_END_MONTH = new DateRange(
			today.withDayOfMonth(1),
			today.withDayOfMonth(today.lengthOfMonth())
		);

		return attendanceService.getAllAttendanceBetweenDateWithStatus(START_END_MONTH, Status.ONTIME);
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
		LocalDate today = LocalDate.now();

		DateRange START_END_MONTH = new DateRange(
			today.withDayOfMonth(1),
			today.withDayOfMonth(today.lengthOfMonth())
		);

		return attendanceService.getAllAttendanceBetweenDateWithStatus(START_END_MONTH, Status.LATE);
	}

	@Operation(
		summary = "Get Today's Section Attendance",
		description = "Retrieves the attendance records for today."
	)
	@PostMapping("/section/today")
	public Iterable<Attendance> getSectionAttendanceToday(@NonNull @RequestBody Section section) {
		LocalDate today = LocalDate.now();

		DateRange START_END_TODAY = new DateRange(
			today,
			today
		);
		return (section.getSectionId() <= 0)
			? Collections.emptyList()
			: attendanceService.getAttendanceInSection(section.getSectionId(), START_END_TODAY);
	}

	@Operation(
		summary = "Get Week's Section Attendance",
		description = "Retrieves the attendance records for the current week."
	)
	@PostMapping("/section/week")
	public Iterable<Attendance> getSectionAttendanceWeek(@NonNull @RequestBody Section section) {
		LocalDate today = LocalDate.now();
		DateRange START_END_WEEK = new DateRange(
			today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)),
			today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
		);

		return (section.getSectionId() <= 0)
			? Collections.emptyList()
			: attendanceService.getAttendanceInSection(section.getSectionId(), START_END_WEEK);
	}

	@Operation(
		summary = "Get Month's Section Attendance",
		description = "Retrieves the attendance records for the current month."
	)
	@PostMapping("/section/month")
	public Iterable<Attendance> getSectionAttendanceMonth(@NonNull @RequestBody Section section) {
		LocalDate today = LocalDate.now();

		DateRange START_END_MONTH = new DateRange(
			today.withDayOfMonth(1),
			today.withDayOfMonth(today.lengthOfMonth())
		);

		return attendanceService.getAttendanceInSection(section.getSectionId(), START_END_MONTH);
	}

	@Operation(
		summary = "Get Section's Attendance with date range",
		description = "Retrieves the attendance records for between the date you provided with Status filtering.",
		parameters = {
			@Parameter(name = "section-id", description = "The ID of the section.", required = true),
			@Parameter(name = "status", description = "The status to filter the attendance. If provided null, no late and on time filtering.")
		}
	)
	@PostMapping("/section")
	public Iterable<Attendance> getAttendanceInSection(@RequestParam("section-id") Integer sectionId, @RequestParam Status status, @RequestBody DateRange dateRange) {
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
			@Parameter(name = "section-id", description = "The ID of the section.", required = true),
			@Parameter(name = "status", description = "The status to filter the attendance.", required = true)
		}
	)
	@PostMapping("/count/section")
	public long getAttendanceCountInSection(@RequestParam("section-id") Integer sectionId, @RequestParam Status status, @RequestBody DateRange dateRange) {
		return attendanceService.countAttendanceInSection(sectionId, dateRange, status);
	}

	@Operation(
		summary = "Get Section's Attendance Count between date",
		description = "Retrieves the attendance records for the date you provided. NOTE: If status is null, " +
			"the attendance count will be returned without the filter of ONTIME or LATE and date should not be null.",
		parameters = {
			@Parameter(name = "section-id", description = "The ID of the section.")
		}
	)
	@PostMapping("/count/section/date")
	public long getAttendanceCountInSectionByStatusAndDate(@RequestParam("section-id") Integer sectionId, @RequestBody DateWithStatus dateStatus) {
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

