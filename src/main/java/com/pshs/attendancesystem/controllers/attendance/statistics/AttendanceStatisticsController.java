package com.pshs.attendancesystem.controllers.attendance.statistics;

import com.pshs.attendancesystem.documentation.AttendanceStatisticsDocumentation;
import com.pshs.attendancesystem.dto.statistics.AttendanceStatisticsOverAllDTO;
import com.pshs.attendancesystem.dto.statistics.StudentAttendanceStatistics;
import com.pshs.attendancesystem.entities.Gradelevel;
import com.pshs.attendancesystem.entities.Section;
import com.pshs.attendancesystem.entities.Strand;
import com.pshs.attendancesystem.entities.Student;
import com.pshs.attendancesystem.entities.statistics.DateRange;
import com.pshs.attendancesystem.enums.Status;
import com.pshs.attendancesystem.services.AttendanceStatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Tag(name = "Attendance Statistics", description = "Manages attendance statistics.")
@RestController
@RequestMapping("${api.root}/attendance/stats")
@SecurityRequirement(name = "JWT Authentication")
public class AttendanceStatisticsController {
	private final AttendanceStatisticsService statisticsService;

	public AttendanceStatisticsController(AttendanceStatisticsService statisticsService) {
		this.statisticsService = statisticsService;
	}

	@Operation(
		summary = "Get overall attendance statistics",
		description = AttendanceStatisticsDocumentation.GET_OVERALL_ATTENDANCE_STATISTICS
	)
	@GetMapping("/overall")
	public AttendanceStatisticsOverAllDTO getOverallAttendanceStatistics() {
		return statisticsService.getAttendanceStatisticsOverAll();
	}

	@Operation(
		summary = "Get overall attendance statistics by date range",
		description = AttendanceStatisticsDocumentation.GET_OVERALL_ATTENDANCE_STATISTICS_BY_DATE_RANGE
	)
	@PostMapping("/overall/date-range")
	public AttendanceStatisticsOverAllDTO getOverallAttendanceStatisticsByDateRange(@RequestBody DateRange dateRange) {
		return statisticsService.getAttendanceStatisticsOverAllDateRange(dateRange);
	}

	@Operation(
		summary = "Get attendance statistics month",
		description = AttendanceStatisticsDocumentation.GET_ATTENDANCE_STATISTICS_MONTH
	)
	// Region: DAY
	@GetMapping("/month")
	public AttendanceStatisticsOverAllDTO getAttendanceStatisticsMonth() {
		LocalDate today = LocalDate.now();
		return statisticsService.getAttendanceStatisticsOverAllDateRange(
			new DateRange(
				today.withDayOfMonth(1),
				today.withDayOfMonth(today.lengthOfMonth())
			)
		);
	}

	@Operation(
		summary = "Get attendance statistics week",
		description = AttendanceStatisticsDocumentation.GET_ATTENDANCE_STATISTICS_WEEK
	)
	@GetMapping("/week")
	public AttendanceStatisticsOverAllDTO getAttendanceStatisticsWeek() {
		LocalDate today = LocalDate.now();
		return statisticsService.getAttendanceStatisticsOverAllDateRange(
			new DateRange(
				today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)),
				today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
			)
		);
	}

	@Operation(
		summary = "Get attendance statistics today",
		description = AttendanceStatisticsDocumentation.GET_ATTENDANCE_STATISTICS_TODAY
	)
	@GetMapping("/today")
	public AttendanceStatisticsOverAllDTO getAttendanceStatisticsToday() {
		LocalDate today = LocalDate.now();
		return statisticsService.getAttendanceStatisticsOverAllDateRange(
			new DateRange(
				today,
				today
			)
		);
	}
	// End

	// Region: Section

	@Operation(
		summary = "Get attendance statistics by date range",
		description = AttendanceStatisticsDocumentation.GET_ATTENDANCE_STATISTICS_MONTH_BY_SECTION
	)
	@GetMapping("/month/section")
	public AttendanceStatisticsOverAllDTO getAttendanceStatisticsMonthBySection(@RequestBody Section section) {
		LocalDate today = LocalDate.now();
		return statisticsService.getAttendanceStatisticsOverAllDateRange(
			new DateRange(
				today.withDayOfMonth(1),
				today.withDayOfMonth(today.lengthOfMonth())
			),
			section
		);
	}

	@Operation(
		summary = "Get attendance statistics week by section",
		description = AttendanceStatisticsDocumentation.GET_ATTENDANCE_STATISTICS_WEEK_BY_SECTION
	)
	@GetMapping("/week/section")
	public AttendanceStatisticsOverAllDTO getAttendanceStatisticsWeekBySection(@RequestBody Section section) {
		LocalDate today = LocalDate.now();
		return statisticsService.getAttendanceStatisticsOverAllDateRange(
			new DateRange(
				today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)),
				today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
			),
			section
		);
	}

	@Operation(
		summary = "Get attendance statistics today by section",
		description = AttendanceStatisticsDocumentation.GET_ATTENDANCE_STATISTICS_TODAY_BY_SECTION
	)
	@GetMapping("/today/section")
	public AttendanceStatisticsOverAllDTO getAttendanceStatisticsTodayBySection(@RequestBody Section section) {
		LocalDate today = LocalDate.now();
		return statisticsService.getAttendanceStatisticsOverAllDateRange(
			new DateRange(
				today,
				today
			),
			section
		);
	}

	// End Section

	// Region: Grade level
	@Operation(
		summary = "Get attendance statistics month by grade level",
		description = AttendanceStatisticsDocumentation.GET_ATTENDANCE_STATISTICS_MONTH_BY_GRADELEVEL
	)
	@GetMapping("/month/gradelevel")
	public AttendanceStatisticsOverAllDTO getAttendanceStatisticsMonthByGradelevel(@RequestBody Gradelevel gradelevel) {
		LocalDate today = LocalDate.now();
		return statisticsService.getAttendanceStatisticsOverAllDateRange(
			new DateRange(
				today.withDayOfMonth(1),
				today.withDayOfMonth(today.lengthOfMonth())
			),
			gradelevel
		);
	}

	@Operation(
		summary = "Get attendance statistics week by grade level",
		description = AttendanceStatisticsDocumentation.GET_ATTENDANCE_STATISTICS_WEEK_BY_GRADELEVEL
	)
	@GetMapping("/week/gradelevel")
	public AttendanceStatisticsOverAllDTO getAttendanceStatisticsWeekByGradelevel(@RequestBody Gradelevel gradelevel) {
		LocalDate today = LocalDate.now();
		return statisticsService.getAttendanceStatisticsOverAllDateRange(
			new DateRange(
				today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)),
				today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
			),
			gradelevel
		);
	}

	@Operation(
		summary = "Get attendance statistics today by grade level",
		description = AttendanceStatisticsDocumentation.GET_ATTENDANCE_STATISTICS_TODAY_BY_GRADELEVEL
	)
	@GetMapping("/today/gradelevel")
	public AttendanceStatisticsOverAllDTO getAttendanceStatisticsTodayByGradelevel(@RequestBody Gradelevel gradelevel) {
		LocalDate today = LocalDate.now();
		return statisticsService.getAttendanceStatisticsOverAllDateRange(
			new DateRange(
				today,
				today
			),
			gradelevel
		);
	}

	// End Gradelevel

	// Region: Strand

	@Operation(
		summary = "Get attendance statistics month by strand",
		description = AttendanceStatisticsDocumentation.GET_ATTENDANCE_STATISTICS_MONTH_BY_STRAND
	)
	@GetMapping("/month/strand")
	public AttendanceStatisticsOverAllDTO getAttendanceStatisticsMonthByStrand(@RequestBody Strand strand) {
		LocalDate today = LocalDate.now();
		return statisticsService.getAttendanceStatisticsOverAllDateRange(
			new DateRange(
				today.withDayOfMonth(1),
				today.withDayOfMonth(today.lengthOfMonth())
			),
			strand
		);
	}

	@Operation(
		summary = "Get attendance statistics week by strand",
		description = AttendanceStatisticsDocumentation.GET_ATTENDANCE_STATISTICS_WEEK_BY_STRAND
	)
	@GetMapping("/week/strand")
	public AttendanceStatisticsOverAllDTO getAttendanceStatisticsWeekByStrand(@RequestBody Strand strand) {
		LocalDate today = LocalDate.now();
		return statisticsService.getAttendanceStatisticsOverAllDateRange(
			new DateRange(
				today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)),
				today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
			),
			strand
		);
	}

	@Operation(
		summary = "Get attendance statistics today by strand",
		description = AttendanceStatisticsDocumentation.GET_ATTENDANCE_STATISTICS_TODAY_BY_STRAND
	)
	@GetMapping("/today/strand")
	public AttendanceStatisticsOverAllDTO getAttendanceStatisticsTodayByStrand(@RequestBody Strand strand) {
		LocalDate today = LocalDate.now();
		return statisticsService.getAttendanceStatisticsOverAllDateRange(
			new DateRange(
				today,
				today
			),
			strand
		);
	}

	// End Strand

	// Region: Student

	@Operation(
		summary = "Get attendance statistics month by student",
		description = AttendanceStatisticsDocumentation.GET_ATTENDANCE_STATISTICS_MONTH_BY_STUDENT
	)
	@GetMapping("/today/student")
	public StudentAttendanceStatistics getStudentAttendanceStatisticsToday(@RequestParam Long lrn) {
		LocalDate today = LocalDate.now();
		return statisticsService.getStudentAttendanceStatistics(
			new DateRange(
				today,
				today
			), lrn);
	}

	@Operation(
		summary = "Get attendance statistics week by student",
		description = AttendanceStatisticsDocumentation.GET_ATTENDANCE_STATISTICS_WEEK_BY_STUDENT
	)
	@GetMapping("/month/student")
	public StudentAttendanceStatistics getStudentAttendanceStatisticsMonth(@RequestParam Long lrn) {
		LocalDate today = LocalDate.now();
		return statisticsService.getStudentAttendanceStatistics(
			new DateRange(
				today.withDayOfMonth(1),
				today.withDayOfMonth(today.lengthOfMonth())
			), lrn);
	}

	@Operation(
		summary = "Get attendance statistics today by student",
		description = AttendanceStatisticsDocumentation.GET_ATTENDANCE_STATISTICS_TODAY_BY_STUDENT
	)
	@GetMapping("/week/student")
	public StudentAttendanceStatistics getStudentAttendanceStatisticsWeek(@RequestParam Long lrn) {
		LocalDate today = LocalDate.now();
		return statisticsService.getStudentAttendanceStatistics(
			new DateRange(
				today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)),
				today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
			), lrn);
	}

	// End Student

	// Region: Student Lists

	@Operation(
		summary = "Get student list with status by section",
		description = AttendanceStatisticsDocumentation.GET_STUDENT_LIST_WITH_STATUS_BY_SECTION
	)
	@PostMapping("/students/{status}/section")
	public List<Student> getStudents(@RequestBody Section section, @PathVariable("status") Status status, @RequestParam("start-date") LocalDate startDate, @RequestParam("end-date") LocalDate endDate) {
		return statisticsService.getStudentsStatus(status, section, new DateRange(
			startDate,
			endDate
		));
	}

	@Operation(
		summary = "Get student list with status by grade level",
		description = AttendanceStatisticsDocumentation.GET_STUDENT_LIST_WITH_STATUS_BY_GRADELEVEL
	)
	@PostMapping("/students/{status}/gradelevel")
	public List<Student> getStudents(@RequestBody Gradelevel gradelevel, @PathVariable("status") Status status, @RequestParam("start-date") LocalDate startDate, @RequestParam("end-date") LocalDate endDate) {
		return statisticsService.getStudentsStatus(status, gradelevel, new DateRange(
			startDate,
			endDate
		));
	}

	@Operation(
		summary = "Get student list with status by strand",
		description = AttendanceStatisticsDocumentation.GET_STUDENT_LIST_WITH_STATUS_BY_STRAND
	)
	@PostMapping("/students/{status}/strand")
	public List<Student> getStudents(@RequestBody Strand strand, @PathVariable("status") Status status, @RequestParam("start-date") LocalDate startDate, @RequestParam("end-date") LocalDate endDate) {
		return statisticsService.getStudentsStatus(status, strand, new DateRange(
			startDate,
			endDate
		));
	}

	// End Student Lists


}

