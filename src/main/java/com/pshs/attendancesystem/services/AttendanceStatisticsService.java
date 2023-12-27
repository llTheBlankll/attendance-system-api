package com.pshs.attendancesystem.services;

import com.pshs.attendancesystem.dto.AttendanceStatisticsOverAllDTO;
import com.pshs.attendancesystem.entities.Gradelevel;
import com.pshs.attendancesystem.entities.Section;
import com.pshs.attendancesystem.entities.Strand;
import com.pshs.attendancesystem.entities.statistics.DateRange;

public interface AttendanceStatisticsService {
	Long getTotalNumberOfStudents();
	Long getTotalNumberOfStudents(Gradelevel gradelevel);
	Long getTotalNumberOfStudents(Strand strand);
	Long getTotalNumberOfStudents(Section section);

	// Additional attendance statistics methods
	Long getTotalAbsences();

	Long getTotalPresents();

	Long getTotalLate();

	Long getTotalOnTime();

	Double calculateAverageAttendancePercentage();

	AttendanceStatisticsOverAllDTO getAttendanceStatisticsOverAll();

	// * DATE RANGES
	Long getTotalAbsencesBetweenDate(DateRange dateRange);
	Long getTotalPresentsBetweenDate(DateRange dateRange);
	Long getTotalLateBetweenDate(DateRange dateRange);
	Long getTotalOnTimeBetweenDate(DateRange dateRange);
	Double calculateAverageAttendancePercentageBetweenDate(DateRange dateRange);
	AttendanceStatisticsOverAllDTO getAttendanceStatisticsOverAllDateRange(DateRange dateRange);

	// * SECTION
	AttendanceStatisticsOverAllDTO getAttendanceStatisticsOverAllDateRange(DateRange dateRange, Section section);
	Long getTotalAbsencesBetweenDate(DateRange dateRange, Section section);
	Long getTotalPresentsBetweenDate(DateRange dateRange, Section section);
	Long getTotalLateBetweenDate(DateRange dateRange, Section section);
	Long getTotalOnTimeBetweenDate(DateRange dateRange, Section section);
	Double calculateAverageAttendancePercentageBetweenDate(DateRange dateRange, Section section);

	// * Grade Level
	AttendanceStatisticsOverAllDTO getAttendanceStatisticsOverAllDateRange(DateRange dateRange, Gradelevel gradeLevel);
	Long getTotalAbsencesBetweenDate(DateRange dateRange, Gradelevel gradeLevel);
	Long getTotalPresentsBetweenDate(DateRange dateRange, Gradelevel gradeLevel);
	Long getTotalLateBetweenDate(DateRange dateRange, Gradelevel gradeLevel);
	Long getTotalOnTimeBetweenDate(DateRange dateRange, Gradelevel gradeLevel);
	Double calculateAverageAttendancePercentageBetweenDate(DateRange dateRange, Gradelevel gradeLevel);

	// * Strand
	AttendanceStatisticsOverAllDTO getAttendanceStatisticsOverAllDateRange(DateRange dateRange, Strand strand);
	Long getTotalAbsencesBetweenDate(DateRange dateRange, Strand strand);
	Long getTotalPresentsBetweenDate(DateRange dateRange, Strand strand);
	Long getTotalLateBetweenDate(DateRange dateRange, Strand strand);
	Long getTotalOnTimeBetweenDate(DateRange dateRange, Strand strand);
	Double calculateAverageAttendancePercentageBetweenDate(DateRange dateRange, Strand strand);

}
