package com.pshs.attendancesystem.controllers.attendance.statistics.chart;

import com.pshs.attendancesystem.entities.Gradelevel;
import com.pshs.attendancesystem.entities.Section;
import com.pshs.attendancesystem.entities.Strand;
import com.pshs.attendancesystem.entities.statistics.BetweenDate;
import com.pshs.attendancesystem.enums.Status;
import com.pshs.attendancesystem.services.AttendanceService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/v1/attendance/stats/chart")
@Tag(name = "Attendance Chart", description = "Get attendance chart data, gives List of Integers")
public class AttendanceChartController {

	private final AttendanceService attendanceService;

	LocalDate today = LocalDate.now();
	LocalDate startYear = today.with(TemporalAdjusters.firstDayOfYear());
	LocalDate endYear = today.with(TemporalAdjusters.lastDayOfYear());
	LocalDate startMonth = today.with(TemporalAdjusters.firstDayOfMonth());
	LocalDate endMonth = today.with(TemporalAdjusters.lastDayOfMonth());
	LocalDate startWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
	LocalDate endWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

	public AttendanceChartController(AttendanceService attendanceService) {
		this.attendanceService = attendanceService;
	}

	private List<Long> countSectionAttendance(@RequestBody Section section, BetweenDate dateRange, Status status) {
		List<Long> countAttendances = new ArrayList<>();
		for (LocalDate date = dateRange.getStartDate(); date.isBefore(dateRange.getEndDate().plusDays(1)); date = date.plusDays(1)) {
			switch (status) {
				case ONTIME -> countAttendances.add(
					attendanceService.countAttendanceInSection(section.getSectionId(), new BetweenDate(date, date), Status.ONTIME)
				);


				case LATE -> countAttendances.add(
					attendanceService.countAttendanceInSection(section.getSectionId(), new BetweenDate(date, date), Status.LATE)
				);


				default -> countAttendances.add(0L);
			}
		}

		return countAttendances;
	}

	private List<Long> countStrandAttendance(@RequestBody Strand strand, BetweenDate dateRange, Status status) {
		List<Long> countAttendances = new ArrayList<>();
		for (LocalDate date = dateRange.getStartDate(); date.isBefore(dateRange.getEndDate().plusDays(1)); date = date.plusDays(1)) {
			countAttendances.add(attendanceService.countByStudentStrandAndDate(strand, date, status));
		}

		return countAttendances;
	}

	private List<Long> getChartGradeLevelWeek(@RequestBody Gradelevel gradeLevel, BetweenDate dateRange, Status status) {
		List<Long> countAttendances = new ArrayList<>();
		for (LocalDate date = dateRange.getStartDate();
		     date.isBefore(dateRange.getEndDate().plusDays(1));
		     date = date.plusDays(1)) {
			countAttendances.add(attendanceService.countByStudentGradeLevelByStatusAndDate(gradeLevel, status, date));
		}

		return countAttendances;
	}


	@PostMapping("/section/week")
	public List<Long> getChartSectionWeek(@RequestBody Section section, @RequestParam Status status) {
		return countSectionAttendance(
			section,
			new BetweenDate(startWeek, endWeek),
			status
		);
	}

	@PostMapping("/section/month")
	public List<Long> getChartSectionMonth(@RequestBody Section section, @RequestParam Status status) {
		return countSectionAttendance(
			section,
			new BetweenDate(startMonth, endMonth),
			status
		);
	}

	@PostMapping("/section/year")
	public List<Long> getChartSectionYear(@RequestBody Section section, @RequestParam Status status) {
		return countSectionAttendance(
			section,
			new BetweenDate(startYear, endYear),
			status
		);
	}

	@PostMapping("/strand/week")
	public List<Long> getChartStrandWeek(@RequestBody Strand strand, @RequestParam Status status) {
		return countStrandAttendance(
			strand,
			new BetweenDate(startWeek, endWeek),
			status
		);
	}

	@PostMapping("/strand/month")
	public List<Long> getChartStrandMonth(@RequestBody Strand strand, @RequestParam Status status) {
		return countStrandAttendance(
			strand,
			new BetweenDate(startMonth, endMonth),
			status
		);
	}

	@PostMapping("/strand/year")
	public List<Long> getChartStrandYear(@RequestBody Strand strand, @RequestParam Status status) {
		return countStrandAttendance(
			strand,
			new BetweenDate(startYear, endYear),
			status
		);
	}

	@PostMapping("/gradelevel/week")
	public List<Long> getChartGradeLevelWeek(@RequestBody Gradelevel gradeLevel, @RequestParam Status status) {
		return getChartGradeLevelWeek(
			gradeLevel,
			new BetweenDate(startWeek, endWeek),
			status
		);
	}

	@PostMapping("/gradelevel/month")
	public List<Long> getChartGradeLevelMonth(@RequestBody Gradelevel gradeLevel, @RequestParam Status status) {
		return getChartGradeLevelWeek(
			gradeLevel,
			new BetweenDate(startMonth, endMonth),
			status
		);
	}

	@PostMapping("/gradelevel/year")
	public List<Long> getChartGradeLevelYear(@RequestBody Gradelevel gradeLevel, @RequestParam Status status) {
		return getChartGradeLevelWeek(
			gradeLevel,
			new BetweenDate(startYear, endYear),
			status
		);
	}
}
