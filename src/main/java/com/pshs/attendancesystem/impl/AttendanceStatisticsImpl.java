package com.pshs.attendancesystem.impl;

import com.pshs.attendancesystem.dto.AttendanceStatisticsOverAllDTO;
import com.pshs.attendancesystem.entities.Gradelevel;
import com.pshs.attendancesystem.entities.Section;
import com.pshs.attendancesystem.entities.Strand;
import com.pshs.attendancesystem.entities.statistics.DateRange;
import com.pshs.attendancesystem.enums.Status;
import com.pshs.attendancesystem.repositories.AttendanceRepository;
import com.pshs.attendancesystem.repositories.StudentRepository;
import com.pshs.attendancesystem.services.AttendanceStatisticsService;
import com.pshs.attendancesystem.services.StudentService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class AttendanceStatisticsImpl implements AttendanceStatisticsService {

	private final StudentService studentService;
	private final AttendanceRepository attendanceRepository;
	private final Logger logger = LogManager.getLogger(this.getClass());
	private final StudentRepository studentRepository;

	public AttendanceStatisticsImpl(StudentService studentService, AttendanceRepository attendanceRepository,
	                                StudentRepository studentRepository) {
		this.studentService = studentService;
		this.attendanceRepository = attendanceRepository;
		this.studentRepository = studentRepository;
	}

	@Override
	public Long getTotalNumberOfStudents() {
		return studentService.getAllStudentsCount();
	}

	@Override
	public Long getTotalNumberOfStudents(Gradelevel gradelevel) {
		return studentService.getAllStudentsCount(gradelevel);
	}

	@Override
	public Long getTotalNumberOfStudents(Strand strand) {
		return studentService.getAllStudentsCount(strand);
	}

	@Override
	public Long getTotalNumberOfStudents(Section section) {
		return studentService.getAllStudentsCount(section);
	}

	@Override
	public Long getTotalAbsences() {
		LocalDate today = LocalDate.now();
		LocalDate startDate = today.withYear(today.getYear() - 1);
		LocalDate endDate = today.withYear(today.getYear() + 1);

		return attendanceRepository.countAttendanceByDateRangeStatus(startDate, endDate, Status.ABSENT);
	}

	@Override
	public Long getTotalPresents() {
		LocalDate today = LocalDate.now();
		LocalDate startDate = today.withYear(today.getYear() - 1);
		LocalDate endDate = today.withYear(today.getYear() + 1);

		// TODO: Might need a revision to reduce the number of queries.
		return attendanceRepository.countAttendanceByDateRangeStatus(startDate, endDate, Status.ONTIME) +
			attendanceRepository.countAttendanceByDateRangeStatus(startDate, endDate, Status.LATE);
	}

	@Override
	public Long getTotalLate() {
		LocalDate today = LocalDate.now();
		LocalDate startDate = today.withYear(today.getYear() - 1);
		LocalDate endDate = today.withYear(today.getYear() + 1);

		return attendanceRepository.countAttendanceByDateRangeStatus(startDate, endDate, Status.LATE);
	}

	@Override
	public Long getTotalOnTime() {
		LocalDate today = LocalDate.now();
		LocalDate startDate = today.withYear(today.getYear() - 1);
		LocalDate endDate = today.withYear(today.getYear() + 1);

		return attendanceRepository.countAttendanceByDateRangeStatus(startDate, endDate, Status.ONTIME);
	}

	@Override
	public Double calculateAverageAttendancePercentage() {
		/*
		 ! This is inaccurate because the total number of students might change over time.
		 ! Calculating the percentage based on the total number of students is not accurate.
		 ! Because there is no record of total students each day.
		 ! For example, if there's a student enrolled in the school within 365 days. The total number of students will be increased
		 ! And the calculation will be inaccurate because the new student will be added to the counting.
		 TODO : Implement the calculation based on the total number of students each day.
		 * Crappy explanation ain't it?
		 */

		// @ Getting all the present students in an entire year.
		long presentStudents = getTotalPresents();

		// @ Since the number of days is a constant, we can use the total number of students to calculate the percentage.
		long totalStudents = getTotalNumberOfStudents() * 365L;

		return ((double) presentStudents / (double) totalStudents) * 100;
	}

	@Override
	public AttendanceStatisticsOverAllDTO getAttendanceStatisticsOverAll() {
		AttendanceStatisticsOverAllDTO overAllStatistic = new AttendanceStatisticsOverAllDTO();
		overAllStatistic.setAverageAttendancePercentage(calculateAverageAttendancePercentage());
		overAllStatistic.setTotalAbsents(getTotalAbsences());
		overAllStatistic.setTotalPresents(getTotalPresents());
		overAllStatistic.setTotalLate(getTotalLate());
		overAllStatistic.setTotalOnTime(getTotalOnTime());
		overAllStatistic.setTotalNumberOfStudents(getTotalNumberOfStudents());

		return overAllStatistic;
	}

	@Override
	public Long getTotalAbsencesBetweenDate(DateRange dateRange) {
		return attendanceRepository.countAttendanceByDateRangeStatus(dateRange.getStartDate(), dateRange.getEndDate(), Status.ABSENT);
	}

	@Override
	public Long getTotalPresentsBetweenDate(DateRange dateRange) {
		return attendanceRepository.countAttendanceByDateRangeStatus(dateRange.getStartDate(), dateRange.getEndDate(), Status.ONTIME) +
			attendanceRepository.countAttendanceByDateRangeStatus(dateRange.getStartDate(), dateRange.getEndDate(), Status.LATE);
	}

	@Override
	public Long getTotalLateBetweenDate(DateRange dateRange) {
		return attendanceRepository.countAttendanceByDateRangeStatus(dateRange.getStartDate(), dateRange.getEndDate(), Status.LATE);
	}

	@Override
	public Long getTotalOnTimeBetweenDate(DateRange dateRange) {
		return attendanceRepository.countAttendanceByDateRangeStatus(dateRange.getStartDate(), dateRange.getEndDate(), Status.ONTIME);
	}

	@Override
	public Double calculateAverageAttendancePercentageBetweenDate(DateRange dateRange) {
		if (dateRange.getStartDate() == null || dateRange.getEndDate() == null) {
			return -1D;
		}

		Long days = ChronoUnit.DAYS.between(dateRange.getStartDate(), dateRange.getEndDate());

		return (
			(double) getTotalPresentsBetweenDate(dateRange)
			/
			(double) (getTotalNumberOfStudents() * ((days == 0) ? 1L : days))
		) * 100;
	}

	@Override
	public AttendanceStatisticsOverAllDTO getAttendanceStatisticsOverAllDateRange(DateRange dateRange) {
		if (dateRange.getStartDate() == null || dateRange.getEndDate() == null) {
			logger.info("Date Range is null.");
		}

		AttendanceStatisticsOverAllDTO overAllStatistic = new AttendanceStatisticsOverAllDTO();
		overAllStatistic.setAverageAttendancePercentage(calculateAverageAttendancePercentageBetweenDate(dateRange));
		overAllStatistic.setTotalAbsents(getTotalAbsencesBetweenDate(dateRange));
		overAllStatistic.setTotalPresents(getTotalPresentsBetweenDate(dateRange));
		overAllStatistic.setTotalLate(getTotalLateBetweenDate(dateRange));
		overAllStatistic.setTotalOnTime(getTotalOnTimeBetweenDate(dateRange));
		overAllStatistic.setTotalNumberOfStudents(getTotalNumberOfStudents());

		return overAllStatistic;
	}

	// * GRADE LEVEL

	@Override
	public AttendanceStatisticsOverAllDTO getAttendanceStatisticsOverAllDateRange(DateRange dateRange, Gradelevel gradeLevel) {
		AttendanceStatisticsOverAllDTO statistics = new AttendanceStatisticsOverAllDTO();

		statistics.setAverageAttendancePercentage(calculateAverageAttendancePercentageBetweenDate(dateRange));
		statistics.setTotalAbsents(getTotalAbsencesBetweenDate(dateRange, gradeLevel));
		statistics.setTotalPresents(getTotalPresentsBetweenDate(dateRange, gradeLevel));
		statistics.setTotalLate(getTotalLateBetweenDate(dateRange, gradeLevel));
		statistics.setTotalOnTime(getTotalOnTimeBetweenDate(dateRange, gradeLevel));
		statistics.setTotalNumberOfStudents(getTotalNumberOfStudents(gradeLevel));

		return statistics;
	}

	@Override
	public Long getTotalAbsencesBetweenDate(DateRange dateRange, Gradelevel gradeLevel) {
		return attendanceRepository.countAttendanceByDateRangeAndGradeLevelAndStatus(dateRange.getStartDate(), dateRange.getEndDate(), gradeLevel, Status.ABSENT);
	}

	@Override
	public Long getTotalPresentsBetweenDate(DateRange dateRange, Gradelevel gradeLevel) {
		return attendanceRepository.countAttendanceByDateRangeAndGradeLevelAndStatus(dateRange.getStartDate(), dateRange.getEndDate(), gradeLevel, Status.ONTIME) +
			attendanceRepository.countAttendanceByDateRangeAndGradeLevelAndStatus(dateRange.getStartDate(), dateRange.getEndDate(), gradeLevel, Status.LATE);
	}

	@Override
	public Long getTotalLateBetweenDate(DateRange dateRange, Gradelevel gradeLevel) {
		return attendanceRepository.countAttendanceByDateRangeAndGradeLevelAndStatus(dateRange.getStartDate(), dateRange.getEndDate(), gradeLevel, Status.LATE);
	}

	@Override
	public Long getTotalOnTimeBetweenDate(DateRange dateRange, Gradelevel gradeLevel) {
		return attendanceRepository.countAttendanceByDateRangeAndGradeLevelAndStatus(dateRange.getStartDate(), dateRange.getEndDate(), gradeLevel, Status.ONTIME);
	}

	@Override
	public Double calculateAverageAttendancePercentageBetweenDate(DateRange dateRange, Gradelevel gradeLevel) {
		if (dateRange.getStartDate() == null || dateRange.getEndDate() == null) {
			return -1D;
		}

		Long days = ChronoUnit.DAYS.between(dateRange.getStartDate(), dateRange.getEndDate());
		return (
			(double) getTotalPresentsBetweenDate(dateRange, gradeLevel) /
			(double) (getTotalNumberOfStudents(gradeLevel) * days)
		) * 100;
	}

	// * SECTION

	@Override
	public AttendanceStatisticsOverAllDTO getAttendanceStatisticsOverAllDateRange(DateRange dateRange, Section section) {
		AttendanceStatisticsOverAllDTO statistics = new AttendanceStatisticsOverAllDTO();

		statistics.setAverageAttendancePercentage(calculateAverageAttendancePercentageBetweenDate(dateRange, section));
		statistics.setTotalAbsents(getTotalAbsencesBetweenDate(dateRange, section));
		statistics.setTotalPresents(getTotalPresentsBetweenDate(dateRange, section));
		statistics.setTotalLate(getTotalLateBetweenDate(dateRange, section));
		statistics.setTotalOnTime(getTotalOnTimeBetweenDate(dateRange, section));
		statistics.setTotalNumberOfStudents(getTotalNumberOfStudents(section));

		return statistics;
	}

	@Override
	public Long getTotalAbsencesBetweenDate(DateRange dateRange, Section section) {
		return attendanceRepository.countAttendanceByDateRangeAndSectionAndStatus(dateRange.getStartDate(), dateRange.getEndDate(), section, Status.ABSENT);
	}

	@Override
	public Long getTotalPresentsBetweenDate(DateRange dateRange, Section section) {
		return attendanceRepository.countAttendanceByDateRangeAndSectionAndStatus(dateRange.getStartDate(), dateRange.getEndDate(), section, Status.ONTIME) +
			attendanceRepository.countAttendanceByDateRangeAndSectionAndStatus(dateRange.getStartDate(), dateRange.getEndDate(), section, Status.LATE);
	}

	@Override
	public Long getTotalLateBetweenDate(DateRange dateRange, Section section) {
		return attendanceRepository.countAttendanceByDateRangeAndSectionAndStatus(dateRange.getStartDate(), dateRange.getEndDate(), section, Status.LATE);
	}

	@Override
	public Long getTotalOnTimeBetweenDate(DateRange dateRange, Section section) {
		return attendanceRepository.countAttendanceByDateRangeAndSectionAndStatus(dateRange.getStartDate(), dateRange.getEndDate(), section, Status.ONTIME);
	}

	@Override
	public Double calculateAverageAttendancePercentageBetweenDate(DateRange dateRange, Section section) {
		if (dateRange.getStartDate() == null || dateRange.getEndDate() == null) {
			return -1D;
		}

		Long days = ChronoUnit.DAYS.between(dateRange.getStartDate(), dateRange.getEndDate());
		return (
			(double) getTotalPresentsBetweenDate(dateRange, section) /
			(double) (getTotalNumberOfStudents(section) * days)
		) * 100;
	}

	// * STRAND

	@Override
	public AttendanceStatisticsOverAllDTO getAttendanceStatisticsOverAllDateRange(DateRange dateRange, Strand strand) {
		AttendanceStatisticsOverAllDTO statistics = new AttendanceStatisticsOverAllDTO();
		statistics.setTotalAbsents(getTotalAbsencesBetweenDate(dateRange, strand));
		statistics.setTotalPresents(getTotalPresentsBetweenDate(dateRange, strand));
		statistics.setTotalLate(getTotalLateBetweenDate(dateRange, strand));
		statistics.setTotalOnTime(getTotalOnTimeBetweenDate(dateRange, strand));
		statistics.setTotalNumberOfStudents(getTotalNumberOfStudents(strand));
		statistics.setAverageAttendancePercentage(calculateAverageAttendancePercentageBetweenDate(dateRange, strand));

		return statistics;
	}

	@Override
	public Long getTotalAbsencesBetweenDate(DateRange dateRange, Strand strand) {
		return attendanceRepository.countAttendanceByDateRangeAndStrandAndStatus(dateRange.getStartDate(), dateRange.getEndDate(), strand, Status.ABSENT);
	}

	@Override
	public Long getTotalPresentsBetweenDate(DateRange dateRange, Strand strand) {
		return attendanceRepository.countAttendanceByDateRangeAndStrandAndStatus(dateRange.getStartDate(), dateRange.getEndDate(), strand, Status.ONTIME) +
			attendanceRepository.countAttendanceByDateRangeAndStrandAndStatus(dateRange.getStartDate(), dateRange.getEndDate(), strand, Status.LATE);
	}

	@Override
	public Long getTotalLateBetweenDate(DateRange dateRange, Strand strand) {
		return attendanceRepository.countAttendanceByDateRangeAndStrandAndStatus(dateRange.getStartDate(), dateRange.getEndDate(), strand, Status.LATE);
	}

	@Override
	public Long getTotalOnTimeBetweenDate(DateRange dateRange, Strand strand) {
		return attendanceRepository.countAttendanceByDateRangeAndStrandAndStatus(dateRange.getStartDate(), dateRange.getEndDate(), strand, Status.ONTIME);
	}

	@Override
	public Double calculateAverageAttendancePercentageBetweenDate(DateRange dateRange, Strand strand) {
		if (dateRange.getStartDate() == null || dateRange.getEndDate() == null) {
			return -1D;
		}

		Long days = ChronoUnit.DAYS.between(dateRange.getStartDate(), dateRange.getEndDate());
		return (
			(double) getTotalPresentsBetweenDate(dateRange, strand) /
			(double) (getTotalNumberOfStudents(strand) * days)
		) * 100;
	}
}
