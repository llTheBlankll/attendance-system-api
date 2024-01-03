package com.pshs.attendancesystem.services;

import com.pshs.attendancesystem.dto.statistics.AttendanceStatisticsOverAllDTO;
import com.pshs.attendancesystem.dto.statistics.StudentAttendanceStatistics;
import com.pshs.attendancesystem.entities.Gradelevel;
import com.pshs.attendancesystem.entities.Section;
import com.pshs.attendancesystem.entities.Strand;
import com.pshs.attendancesystem.entities.Student;
import com.pshs.attendancesystem.entities.statistics.DateRange;
import com.pshs.attendancesystem.enums.Status;

import java.util.List;

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

	// Region: Date Ranges, no objects
	Long getTotalAbsencesBetweenDate(DateRange dateRange);

	Long getTotalPresentsBetweenDate(DateRange dateRange);

	Long getTotalLateBetweenDate(DateRange dateRange);

	Long getTotalOnTimeBetweenDate(DateRange dateRange);

	Double calculateAverageAttendancePercentageBetweenDate(DateRange dateRange);

	AttendanceStatisticsOverAllDTO getAttendanceStatisticsOverAllDateRange(DateRange dateRange);

	// End

	// Region: Section

	AttendanceStatisticsOverAllDTO getAttendanceStatisticsOverAllDateRange(DateRange dateRange, Section section);

	Long getTotalAbsencesBetweenDate(DateRange dateRange, Section section);

	Long getTotalPresentsBetweenDate(DateRange dateRange, Section section);

	Long getTotalLateBetweenDate(DateRange dateRange, Section section);

	Long getTotalOnTimeBetweenDate(DateRange dateRange, Section section);

	Double calculateAverageAttendancePercentageBetweenDate(DateRange dateRange, Section section);

	// End

	// Region: Grade level
	AttendanceStatisticsOverAllDTO getAttendanceStatisticsOverAllDateRange(DateRange dateRange, Gradelevel gradeLevel);

	Long getTotalAbsencesBetweenDate(DateRange dateRange, Gradelevel gradeLevel);

	Long getTotalPresentsBetweenDate(DateRange dateRange, Gradelevel gradeLevel);

	Long getTotalLateBetweenDate(DateRange dateRange, Gradelevel gradeLevel);

	Long getTotalOnTimeBetweenDate(DateRange dateRange, Gradelevel gradeLevel);

	Double calculateAverageAttendancePercentageBetweenDate(DateRange dateRange, Gradelevel gradeLevel);

	// End

	// Region: STRAND

	AttendanceStatisticsOverAllDTO getAttendanceStatisticsOverAllDateRange(DateRange dateRange, Strand strand);

	Long getTotalAbsencesBetweenDate(DateRange dateRange, Strand strand);

	Long getTotalPresentsBetweenDate(DateRange dateRange, Strand strand);

	Long getTotalLateBetweenDate(DateRange dateRange, Strand strand);

	Long getTotalOnTimeBetweenDate(DateRange dateRange, Strand strand);

	Double calculateAverageAttendancePercentageBetweenDate(DateRange dateRange, Strand strand);

	// End

	// Region: STUDENTS

	StudentAttendanceStatistics getStudentAttendanceStatistics(DateRange dateRange, Long lrn);

	Long getTotalAbsences(DateRange dateRange, Long lrn);

	Long getTotalPresents(DateRange dateRange, Long lrn);

	Long getTotalLate(DateRange dateRange, Long lrn);

	Long getTotalOnTime(DateRange dateRange, Long lrn);

	Double calculateAverageAttendancePercentage(DateRange dateRange, Long lrn);

	// End

	// Region: Get Student Status By Section, Strand, Grade level

	List<Student> getStudentsStatus(Status status, Section section, DateRange dateRange);

	List<Student> getStudentsStatus(Status status, Strand strand, DateRange dateRange);

	List<Student> getStudentsStatus(Status status, Gradelevel gradelevel, DateRange dateRange);

	// End
}
