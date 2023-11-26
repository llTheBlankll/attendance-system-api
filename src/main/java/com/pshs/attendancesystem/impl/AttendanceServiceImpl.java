package com.pshs.attendancesystem.impl;

import com.pshs.attendancesystem.config.APIConfiguration;
import com.pshs.attendancesystem.entities.Attendance;
import com.pshs.attendancesystem.entities.Gradelevel;
import com.pshs.attendancesystem.entities.Strand;
import com.pshs.attendancesystem.entities.Student;
import com.pshs.attendancesystem.entities.statistics.BetweenDate;
import com.pshs.attendancesystem.enums.Status;
import com.pshs.attendancesystem.messages.AttendanceMessages;
import com.pshs.attendancesystem.messages.StudentMessages;
import com.pshs.attendancesystem.repositories.AttendanceRepository;
import com.pshs.attendancesystem.repositories.StudentRepository;
import com.pshs.attendancesystem.services.AttendanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Component
public class AttendanceServiceImpl implements AttendanceService {

	private final AttendanceRepository attendanceRepository;
	private final StudentRepository studentRepository;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public AttendanceServiceImpl(AttendanceRepository attendanceRepository, StudentRepository studentRepository) {
		this.attendanceRepository = attendanceRepository;
		this.studentRepository = studentRepository;
	}

	/**
	 * Checks if the student has already arrived by iterating through their attendances and comparing the date.
	 *
	 * @param student the student object to check
	 * @return true if the student has already arrived, false otherwise
	 */
	@Override
	public boolean checkIfAlreadyArrived(Student student) {

		// Iterate each attendance and get the attendance with the current date time,
		// If a row exists, return false because the student has already arrived.
		for (Attendance currentAttendance : student.getAttendances()) {
			if (currentAttendance.getDate().equals(LocalDate.now())) {
				logger.info("Student {} already arrived", student.getLrn());
				return true;
			}
		}

		return false;
	}

	/**
	 * Checks if today is Monday.
	 *
	 * @return true if today is Monday, false otherwise
	 */
	private boolean isTodayMonday() {
		return LocalDate.now().getDayOfWeek().equals(DayOfWeek.MONDAY);
	}

	/**
	 * Checks if the student with the given LRN has already checked out for the day.
	 *
	 * @param studentLrn the LRN (Learner Reference Number) of the student
	 * @return true if the student has already checked out, false otherwise
	 */
	@Override
	public boolean checkIfAlreadyOut(Long studentLrn) {
		Optional<Attendance> attendance = this.attendanceRepository.findByStudent_LrnAndDate(studentLrn, LocalDate.now());
		if (attendance.isPresent() && attendance.get().getTimeOut() != null) {
			logger.info("Student {} already left", studentLrn);
			return true;
		}

		return false;
	}


	@Override
	public Status getAttendanceStatusToday(Long studentLrn) {
		Optional<Attendance> attendance = this.attendanceRepository.findByStudent_LrnAndDate(studentLrn, LocalDate.now());
		return attendance.map(Attendance::getAttendanceStatus).orElse(null);
	}

	@Override
	public Iterable<Attendance> getAllAttendances() {
		return attendanceRepository.findAll();
	}

	/**
	 * Creates an attendance record for a student.
	 *
	 * @param studentLrn the LRN (Learner Reference Number) of the student
	 * @return the status of the attendance record (ONTIME, LATE, or EARLY)
	 */
	@Override
	public Status createAttendance(Long studentLrn) {
		try {
			Optional<Student> student = this.studentRepository.findById(studentLrn);
			// Check for the existence of Student LRN
			if (student.isEmpty()) {
				logger.info(StudentMessages.STUDENT_LRN_NOT_EXISTS);
				return null;
			}

			LocalTime lateArrivalTime;
			LocalTime onTimeArrival = APIConfiguration.Attendance.onTimeArrival;

			Time currentTime = new Time(System.currentTimeMillis());
			LocalTime currentLocalTime = currentTime.toLocalTime();

			// Get Student Data from the database.

			// Flag Ceremony Time
			if (isTodayMonday()) {
				lateArrivalTime = APIConfiguration.Attendance.flagCeremonyTime;
			} else {
				lateArrivalTime = APIConfiguration.Attendance.lateTimeArrival;
			}

			// Check if the data is valid.
			Attendance attendance = new Attendance();
			attendance.setStudent(student.get());
			Status status;

			if (currentLocalTime.isBefore(lateArrivalTime) && currentLocalTime.isAfter(onTimeArrival)) {
				attendance.setAttendanceStatus(Status.ONTIME);
				status = Status.ONTIME;
			} else if (currentLocalTime.isAfter(lateArrivalTime)) {
				attendance.setAttendanceStatus(Status.LATE);
				status = Status.LATE;
			} else {
				status = Status.ONTIME;// ADD CODE HERE FOR EARLY ARRIVAL.
			}

			attendance.setTime(Time.valueOf(LocalTime.now()));
			attendance.setDate(LocalDate.now());
			attendance.setSection(student.get().getStudentSection());

			this.attendanceRepository.save(attendance);

			logger.info("The student {} is {}, Time arrived: {}", student.get().getLrn(), attendance.getAttendanceStatus(), currentTime);
			return status;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public String deleteAttendance(Integer attendanceId) {
		Optional<Attendance> attendance = this.attendanceRepository.findById(attendanceId);
		if (attendance.isEmpty()) {
			return AttendanceMessages.ATTENDANCE_NOT_FOUND;
		} else {
			attendance.ifPresent(attendanceRepository::delete);
			return AttendanceMessages.ATTENDANCE_DELETED;
		}
	}

	@Override
	public String updateAttendance(Attendance attendance) {
		if (attendance.getId() != null) {
			return AttendanceMessages.ATTENDANCE_NOT_FOUND;
		} else {
			this.attendanceRepository.save(attendance);
			return AttendanceMessages.ATTENDANCE_UPDATED;
		}
	}

	/**
	 * Checks the attendance status of a student and marks them as "out" if they are currently "in".
	 *
	 * @param studentLrn the LRN (Learner Reference Number) of the student
	 * @return true if the student's attendance was successfully marked as "out", false otherwise
	 */
	@Override
	public boolean attendanceOut(Long studentLrn) {
		if (studentLrn == null) {
			return false;
		}
		// Check for the existence of Student LRN
		if (!studentRepository.existsById(studentLrn)) {
			logger.info(StudentMessages.STUDENT_LRN_NOT_EXISTS);
			return false;
		}

		Optional<Attendance> attendance = this.attendanceRepository.findByStudent_LrnAndDate(studentLrn, LocalDate.now());

		if (attendance.isPresent() && attendance.get().getTimeOut() == null) {
			Attendance getAttendance = attendance.get();
			logger.info("The student {} is out, Time left: {}", studentLrn, LocalTime.now());
			this.attendanceRepository.studentAttendanceOut(LocalTime.now(), getAttendance.getId());
			return true;
		}

		return false;
	}

	@Override
	public String deleteAllAttendance() {
		this.attendanceRepository.deleteAll();
		return AttendanceMessages.ATTENDANCE_DELETED;
	}

	/**
	 * Retrieves all attendance records between a given start date and end date, filtered by status.
	 *
	 * @param dateRange the date range to filter the attendance records by
	 * @param status    the status to filter the attendance records by
	 * @return an iterable collection of attendance records
	 */
	@Override
	public Iterable<Attendance> getAllAttendanceBetweenDateWithStatus(BetweenDate dateRange, Status status) {
		return attendanceRepository.findByDateGreaterThanEqualAndDateLessThanEqualAndAttendanceStatus(dateRange.getStartDate(), dateRange.getEndDate() , status);
	}

	/**
	 * Retrieves all attendance records between the specified start date and end date.
	 *
	 * @param dateRange the date range to filter the attendance records by
	 * @return an iterable collection of attendance records between the start and end dates
	 */
	@Override
	public Iterable<Attendance> getAllAttendanceBetweenDate(BetweenDate dateRange) {
		return attendanceRepository.findAttendancesByDateBetween(dateRange.getStartDate(), dateRange.getEndDate());
	}

	/**
	 * Returns the total count of attendance records between the given start date and end date,
	 * filtered by the specified attendance status.
	 *
	 * @param dateRange The date range between two time.
	 * @param status    the attendance status to filter by
	 * @return the total count of attendance records between the start date and end date,
	 * filtered by the specified attendance status
	 */
	@Override
	public long getAllCountOfAttendanceBetweenDate(BetweenDate dateRange, Status status) {
		return attendanceRepository.countByDateBetweenAndAttendanceStatus(dateRange.getStartDate(), dateRange.getEndDate(), status);
	}

	/**
	 * Retrieves the attendance records of a student between a specified start and end date,
	 * filtered by the attendance status.
	 *
	 * @param studentLrn the LRN (Learner Reference Number) of the student
	 * @param dateRange  the range of dates to filter the records by
	 * @param status     the attendance status to filter the records by
	 * @return an iterable collection of Attendance objects representing
	 * the attendance records of the student
	 */
	@Override
	public Iterable<Attendance> getStudentAttendanceBetweenDateWithAttendanceStatus(long studentLrn, BetweenDate dateRange, Status status) {
		return attendanceRepository.findByStudentLrnAndDateBetweenAndAttendanceStatus(studentLrn, dateRange.getStartDate(), dateRange.getEndDate(), status);
	}

	/**
	 * Retrieves the attendance records of a student between the specified start and end dates.
	 *
	 * @param studentLrn the LRN (Learner Reference Number) of the student
	 * @param dateRange  the start and end dates of the range
	 * @return an iterable collection of Attendance objects representing the student's attendance between the specified dates
	 */
	@Override
	public Iterable<Attendance> getAttendanceBetweenDate(long studentLrn, BetweenDate dateRange) {
		return this.attendanceRepository.findByStudentLrnAndDateBetween(studentLrn, dateRange.getStartDate(), dateRange.getEndDate());
	}

	/**
	 * Retrieves the total count of student attendance records between the specified dates.
	 *
	 * @param studentLrn the LRN (Learner Reference Number) of the student
	 * @param dateRange  the start and end dates of the range
	 * @param status     the attendance status to filter by
	 * @return the total count of student attendance records
	 */
	@Override
	public long getAllCountOfAttendanceBetweenDate(long studentLrn, BetweenDate dateRange, Status status) {
		return attendanceRepository.countByStudentLrnAndDateBetweenAndAttendanceStatus(studentLrn, dateRange.getStartDate(), dateRange.getEndDate(), status);
	}

	/**
	 * Retrieves the attendance records of a student in a specific section.
	 *
	 * @param sectionId the ID of the section
	 * @return an iterable of Attendance objects representing the student's attendance records
	 */
	@Override
	public Iterable<Attendance> getAttendanceInSectionId(Integer sectionId) {
		return attendanceRepository.findAttendancesByStudent_StudentSection_SectionId(sectionId);
	}

	/**
	 * Retrieves the student attendance records for a given section ID, attendance status, and date range.
	 *
	 * @param sectionId        the ID of the section
	 * @param attendanceStatus the desired attendance status
	 * @param dateRange        the date range
	 * @return an iterable collection of Attendance objects representing the student attendance records
	 */
	@Override
	public Iterable<Attendance> getAttendanceInSectionByStatusBetweenDate(Integer sectionId, Status attendanceStatus, BetweenDate dateRange) {
		return this.attendanceRepository.findAttendancesByStudent_StudentSection_SectionIdAndDateBetweenAndAttendanceStatus(sectionId, dateRange.getStartDate(), dateRange.getEndDate(), attendanceStatus);
	}

	@Override
	public Iterable<Attendance> getAttendanceInSectionByDate(Integer sectionId, LocalDate date) {
		return attendanceRepository.findByStudent_StudentSection_SectionIdAndDateBetween(sectionId, date, date);
	}

	@Override
	public Iterable<Attendance> getStudentAttendanceInSectionBetweenDate(@NonNull Integer sectionId, BetweenDate betweenDate) {
		return this.attendanceRepository.findByStudent_StudentSection_SectionIdAndDateBetween(sectionId, betweenDate.getStartDate(), betweenDate.getEndDate());
	}

	@Override
	public Iterable<Attendance> getAttendanceInSection(@NonNull Integer sectionId, @NonNull BetweenDate dateRange, Status status) {
		switch (status) {
			case ONTIME -> {
				return this.getAttendanceInSectionByStatusBetweenDate(sectionId, Status.ONTIME, dateRange);
			}

			case LATE -> {
				return this.getAttendanceInSectionByStatusBetweenDate(sectionId, Status.LATE, dateRange);
			}

			default -> {
				return this.getStudentAttendanceInSectionBetweenDate(sectionId, dateRange);
			}
		}
	}

	@Override
	public Iterable<Attendance> getAttendanceInSection(Integer sectionId, BetweenDate dateRange) {
		return this.getStudentAttendanceInSectionBetweenDate(sectionId, dateRange);
	}

	@Override
	public long countAttendanceInSection(@NonNull Integer sectionId, BetweenDate dateRange, Status status) {
		switch (status) {
			case ONTIME -> {
				return this.countAttendanceInSectionByStatusAndBetweenDate(sectionId, Status.ONTIME, dateRange);
			}

			case LATE -> {
				return this.countAttendanceInSectionByStatusAndBetweenDate(sectionId, Status.LATE, dateRange);
			}

			default -> {
				return this.attendanceRepository.countByStudent_StudentSection_SectionIdAndDateBetween(sectionId, dateRange.getStartDate(), dateRange.getEndDate());
			}
		}
	}

	/**
	 * Counts the number of student attendances in a section based on the attendance status and date.
	 *
	 * @param sectionId        the ID of the section
	 * @param attendanceStatus the status of the attendance
	 * @param date             the date of the attendance
	 * @return the number of student attendances
	 */
	@Override
	public long countAttendanceInSectionByStatusAndDate(@NonNull Integer sectionId, Status attendanceStatus, LocalDate date) {
		return this.attendanceRepository.countByStudent_StudentSection_SectionIdAndAttendanceStatusAndDate(sectionId, attendanceStatus, date);
	}

	/**
	 * Counts the number of student attendance records in a given section, filtered by attendance status and date range.
	 *
	 * @param sectionId        the ID of the section to count attendance records for
	 * @param attendanceStatus the attendance status to filter by
	 * @param dateRange        the date range to filter by
	 * @return the number of student attendance records that match the given criteria
	 */
	@Override
	public long countAttendanceInSectionByStatusAndBetweenDate(@NonNull Integer sectionId, Status attendanceStatus, BetweenDate dateRange) {
		return this.attendanceRepository.countByStudent_StudentSection_SectionIdAndDateBetweenAndAttendanceStatus(sectionId, dateRange.getStartDate(), dateRange.getEndDate(), attendanceStatus);
	}

	@Override
	public long countAttendanceBySectionAndDate(Integer sectionId, LocalDate date) {
		return this.attendanceRepository.countByStudent_StudentSection_SectionIdAndDate(sectionId, date);
	}

	/**
	 * Counts the number of attendances between two given dates.
	 *
	 * @param dateRange The start and end dates of the range
	 * @return the number of attendances between the start and end dates
	 */
	@Override
	public long countAttendanceBetweenDate(BetweenDate dateRange) {
		return this.attendanceRepository.countByDateBetween(dateRange.getStartDate(), dateRange.getEndDate());
	}

	/**
	 * Counts the number of attendances for a student between two given dates.
	 *
	 * @param studentLrn the LRN (Learner Reference Number) of the student
	 * @param dateRange  the start and end dates of the range
	 * @return the number of attendances for the student within the specified period
	 */
	@Override
	public long countStudentAttendanceBetweenDate(Long studentLrn, BetweenDate dateRange) {
		return this.attendanceRepository.countByStudentLrnAndDateBetween(studentLrn, dateRange.getStartDate(), dateRange.getEndDate());
	}

	@Override
	public long countByStudentStrandAndDate(Strand strand, LocalDate date, Status status) {
		return this.attendanceRepository.countByStudent_StudentSection_StrandAndDateAndAttendanceStatus(strand, date, status);
	}

	@Override
	public long countByStudentGradeLevelByStatusAndDate(Gradelevel gradeLevel, Status status, LocalDate date) {
		return this.attendanceRepository.countByStudent_StudentGradeLevelAndDateAndAttendanceStatus(gradeLevel, date, status);
	}
}
