package com.pshs.attendancesystem.controllers.attendance.statistics;

import com.pshs.attendancesystem.dto.statistics.AttendanceStatisticsOverAllDTO;
import com.pshs.attendancesystem.dto.statistics.StudentAttendanceStatistics;
import com.pshs.attendancesystem.entities.Gradelevel;
import com.pshs.attendancesystem.entities.Section;
import com.pshs.attendancesystem.entities.Strand;
import com.pshs.attendancesystem.entities.Student;
import com.pshs.attendancesystem.entities.statistics.DateRange;
import com.pshs.attendancesystem.enums.Status;
import com.pshs.attendancesystem.services.AttendanceStatisticsService;
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

	@GetMapping("/overall")
	public AttendanceStatisticsOverAllDTO getOverallAttendanceStatistics() {
		return statisticsService.getAttendanceStatisticsOverAll();
	}

	@PostMapping("/overall/date-range")
	public AttendanceStatisticsOverAllDTO getOverallAttendanceStatisticsByDateRange(@RequestBody DateRange dateRange) {
		return statisticsService.getAttendanceStatisticsOverAllDateRange(dateRange);
	}


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

	@GetMapping("/today/student")
	public StudentAttendanceStatistics getStudentAttendanceStatisticsToday(@RequestParam Long lrn) {
		LocalDate today = LocalDate.now();
		return statisticsService.getStudentAttendanceStatistics(
			new DateRange(
				today,
				today
			), lrn);
	}

	@GetMapping("/month/student")
	public StudentAttendanceStatistics getStudentAttendanceStatisticsMonth(@RequestParam Long lrn) {
		LocalDate today = LocalDate.now();
		return statisticsService.getStudentAttendanceStatistics(
			new DateRange(
				today.withDayOfMonth(1),
				today.withDayOfMonth(today.lengthOfMonth())
			), lrn);
	}

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
	@PostMapping("/students/{status}/section")
	public List<Student> getStudents(@RequestBody Section section, @PathVariable("status") Status status , @RequestParam("start-date") LocalDate startDate, @RequestParam("end-date") LocalDate endDate) {
		return statisticsService.getStudentsStatus(status, section, new DateRange(
			startDate,
			endDate
		));
	}

	@PostMapping("/students/{status}/gradelevel")
	public List<Student> getStudents(@RequestBody Gradelevel gradelevel, @PathVariable("status") Status status, @RequestParam("start-date") LocalDate startDate, @RequestParam("end-date") LocalDate endDate) {
		return statisticsService.getStudentsStatus(status, gradelevel, new DateRange(
			startDate,
			endDate
		));
	}

	@PostMapping("/students/{status}/strand")
	public List<Student> getStudents(@RequestBody Strand strand, @PathVariable("status") Status status, @RequestParam("start-date") LocalDate startDate, @RequestParam("end-date") LocalDate endDate) {
		return statisticsService.getStudentsStatus(status, strand, new DateRange(
			startDate,
			endDate
		));
	}



	// End Student Lists

	
}

