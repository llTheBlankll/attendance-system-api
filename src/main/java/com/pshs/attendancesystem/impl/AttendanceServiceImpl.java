package com.pshs.attendancesystem.impl;

import com.pshs.attendancesystem.entities.Attendance;
import com.pshs.attendancesystem.entities.Gradelevel;
import com.pshs.attendancesystem.entities.Strand;
import com.pshs.attendancesystem.entities.Student;
import com.pshs.attendancesystem.entities.statistics.DateRange;
import com.pshs.attendancesystem.enums.Status;
import com.pshs.attendancesystem.messages.AttendanceMessages;
import com.pshs.attendancesystem.messages.StudentMessages;
import com.pshs.attendancesystem.repositories.AttendanceRepository;
import com.pshs.attendancesystem.repositories.StudentRepository;
import com.pshs.attendancesystem.services.AttendanceService;
import io.sentry.Sentry;
import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@CacheConfig(cacheNames = "attendance")
public class AttendanceServiceImpl implements AttendanceService {

  private final AttendanceRepository attendanceRepository;
  private final StudentRepository studentRepository;
  private final ConfigurationService configurationService;
  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  public AttendanceServiceImpl(
      AttendanceRepository attendanceRepository,
      StudentRepository studentRepository,
      ConfigurationService configurationService) {
    this.attendanceRepository = attendanceRepository;
    this.studentRepository = studentRepository;
    this.configurationService = configurationService;
  }

  /**
   * Checks if the student has already arrived by iterating through their attendances and comparing
   * the date.
   *
   * @param lrn the LRN (Learner Reference Number) of the student
   * @return true if the student has already arrived, false otherwise
   */
  @Override
  @Cacheable(key = "#lrn.toString()")
  public boolean isAlreadyArrived(Long lrn) {
    return attendanceRepository.isLrnAndDateExist(lrn, LocalDate.now());
  }

  /**
   * Checks if today is Monday.
   *
   * @return true if today is Monday, false otherwise
   */
  private boolean isTodayMonday() {
    return LocalDate.now().getDayOfWeek().equals(DayOfWeek.MONDAY);
  }

  @Override
  public boolean isAttendanceExist(Integer attendanceId) {
    return attendanceRepository.existsById(attendanceId);
  }

  /**
   * Checks if the student with the given LRN has already checked out for the day.
   *
   * @param studentLrn the LRN (Learner Reference Number) of the student
   * @return true if the student has already checked out, false otherwise
   */
  @Override
  @Cacheable(key = "#studentLrn.toString()")
  public Status isAlreadyOut(Long studentLrn) {
    Optional<Attendance> attendanceOptional =
        this.attendanceRepository.findByStudent_LrnAndDate(studentLrn, LocalDate.now());
    // Check for the existence of Student LRN and if TimeOut is not null, which mean
    // that the student has already checked out.
    if (attendanceOptional.isPresent()) {
      Attendance attendance = attendanceOptional.get();
      if (attendance.getTimeOut() != null) {
        return Status.OUT;
      } else {
        return Status.NOT_OUT;
      }
    }

    return Status.NOT_FOUND;
  }

  @Override
  @Cacheable(key = "#studentLrn")
  public Status getStatusToday(Long studentLrn) {
    Optional<Attendance> attendance =
        this.attendanceRepository.findByStudent_LrnAndDate(studentLrn, LocalDate.now());
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
   */
  @Override
  @CachePut(key = "#studentLrn")
  @Async
  public void createAttendance(Long studentLrn) {
    try {
      // Get Student
      Optional<Student> studentOptional = this.studentRepository.findById(studentLrn);

      // Check for the existence of Student LRN
      if (studentOptional.isEmpty()) {
        logger.info(StudentMessages.STUDENT_LRN_NOT_EXISTS);
        return;
      }

      // Get Student and Attendance Status
      Status status = getStatus();
      Student student = studentOptional.get();

      // Set attendance info.
      Attendance attendance = new Attendance();
      attendance.setStudent(student);
      attendance.setAttendanceStatus(status);
      attendance.setTime(Time.valueOf(LocalTime.now()));
      attendance.setDate(LocalDate.now());

      // Save attendance.
      this.attendanceRepository.save(attendance);
      logger.debug(
          "Student #{} is {}, Time arrived: {}",
          student.getLrn(),
          attendance.getAttendanceStatus(),
          attendance.getTime());
    } catch (Exception e) {
      logger.error(e.getMessage());
      Sentry.captureException(e);
    }
  }

  @Override
  @CacheEvict(key = "#attendanceId")
  public String deleteAttendance(Integer attendanceId) {
    Optional<Attendance> attendance = this.attendanceRepository.findById(attendanceId);

    if (attendance.isPresent()) {
      this.attendanceRepository.delete(attendance.get());
      return AttendanceMessages.ATTENDANCE_DELETED;
    } else {
      return AttendanceMessages.ATTENDANCE_NOT_FOUND;
    }
  }

  @Override
  @CachePut(key = "#attendance.id")
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
   */
  @Override
  @CacheEvict(key = "#studentLrn")
  @Async
  public void attendanceOut(Long studentLrn) {
    if (studentLrn == null) {
      return;
    }

    // Check for the existence of Student LRN
    if (!studentRepository.existsById(studentLrn)) {
      logger.info(StudentMessages.STUDENT_LRN_NOT_EXISTS);
      return;
    }

    Optional<Attendance> attendance =
        this.attendanceRepository.findByStudent_LrnAndDate(studentLrn, LocalDate.now());

    if (attendance.isPresent() && attendance.get().getTimeOut() == null) {
      Attendance getAttendance = attendance.get();
      logger.debug("The student {} is out, Time left: {}", studentLrn, LocalTime.now());
      this.attendanceRepository.studentAttendanceOut(LocalTime.now(), getAttendance.getId());
    }
  }

  @Override
  @CacheEvict(allEntries = true)
  @Async
  public void deleteAllAttendance() {
    this.attendanceRepository.deleteAll();
  }

  /**
   * Retrieves all attendance records between a given start date and end date, filtered by status.
   *
   * @param dateRange the date range to filter the attendance records by
   * @param status the status to filter the attendance records by
   * @return an iterable collection of attendance records
   */
  @Override
  @Cacheable(key = "#dateRange + '-' + #status")
  public Iterable<Attendance> getAllAttendanceBetweenDateWithStatus(
      DateRange dateRange, Status status) {
    return attendanceRepository.searchBetweenDateAndStatus(
        dateRange.getStartDate(), dateRange.getEndDate(), status);
  }

  /**
   * Retrieves all attendance records between the specified start date and end date.
   *
   * @param dateRange the date range to filter the attendance records by
   * @return an iterable collection of attendance records between the start and end dates
   */
  @Override
  @Cacheable(key = "#dateRange")
  public Iterable<Attendance> getAllAttendanceBetweenDate(DateRange dateRange) {
    return attendanceRepository.searchBetweenDate(dateRange.getStartDate(), dateRange.getEndDate());
  }

  /**
   * Returns the total count of attendance records between the given start date and end date,
   * filtered by the specified attendance status.
   *
   * @param dateRange The date range between two time.
   * @param status the attendance status to filter by
   * @return the total count of attendance records between the start date and end date, filtered by
   *     the specified attendance status
   */
  @Override
  @Cacheable(key = "#dateRange + '-' + #status")
  public long getAllCountOfAttendanceBetweenDate(DateRange dateRange, Status status) {
    return attendanceRepository.countBetweenDateAndStatus(
        dateRange.getStartDate(), dateRange.getEndDate(), status);
  }

  /**
   * Retrieves the attendance records of a student between a specified start and end date, filtered
   * by the attendance status.
   *
   * @param studentLrn the LRN (Learner Reference Number) of the student
   * @param dateRange the range of dates to filter the records by
   * @param status the attendance status to filter the records by
   * @return an iterable collection of Attendance objects representing the attendance records of the
   *     student
   */
  @Override
  @Cacheable(key = "#studentLrn + '-' + #dateRange + '-' + #status")
  public Iterable<Attendance> getStudentAttendanceBetweenDateStatus(
      long studentLrn, DateRange dateRange, Status status) {
    return attendanceRepository.searchLrnBetweenDateAndStatus(
        studentLrn, dateRange.getStartDate(), dateRange.getEndDate(), status);
  }

  /**
   * Retrieves the attendance records of a student between the specified start and end dates.
   *
   * @param studentLrn the LRN (Learner Reference Number) of the student
   * @param dateRange the start and end dates of the range
   * @return an iterable collection of Attendance objects representing the student's attendance
   *     between the specified dates
   */
  @Override
  @Cacheable(key = "#studentLrn + '-' + #dateRange")
  public Iterable<Attendance> getAttendanceBetweenDate(long studentLrn, DateRange dateRange) {
    return this.attendanceRepository.searchLrnBetwenDate(
        studentLrn, dateRange.getStartDate(), dateRange.getEndDate());
  }

  /**
   * Retrieves the total count of student attendance records between the specified dates.
   *
   * @param studentLrn the LRN (Learner Reference Number) of the student
   * @param dateRange the start and end dates of the range
   * @param status the attendance status to filter by
   * @return the total count of student attendance records
   */
  @Override
  @Cacheable(key = "#studentLrn + '-' + #dateRange + '-' + #status")
  public long getAllCountOfAttendanceBetweenDate(
      long studentLrn, DateRange dateRange, Status status) {
    return attendanceRepository.countLrnBetweenDateAndStatus(
        studentLrn, dateRange.getStartDate(), dateRange.getEndDate(), status);
  }

  /**
   * Retrieves the student attendance records for a given section ID, attendance status, and date
   * range.
   *
   * @param sectionId the ID of the section
   * @param attendanceStatus the desired attendance status
   * @param dateRange the date range
   * @return an iterable collection of Attendance objects representing the student attendance
   *     records
   */
  @Override
  @Cacheable(key = "#sectionId + '-' + #attendanceStatus + '-' + #dateRange")
  public Iterable<Attendance> getAttendanceInSectionByStatusBetweenDate(
      Integer sectionId, Status attendanceStatus, DateRange dateRange) {
    return this.attendanceRepository.searchSectionIdBetweenDateAndStatus(
        sectionId, dateRange.getStartDate(), dateRange.getEndDate(), attendanceStatus);
  }

  @Override
  @Cacheable(key = "#sectionId + '-' + #dateRange")
  public Iterable<Attendance> getStudentAttendanceInSectionBetweenDate(
      @NonNull Integer sectionId, DateRange dateRange) {
    return this.attendanceRepository.searchSectionIdBetweenDate(
        sectionId, dateRange.getStartDate(), dateRange.getEndDate());
  }

  @Override
  @Cacheable(key = "#sectionId + '-' + #dateRange + '-' + #status")
  public Iterable<Attendance> getAttendanceInSection(
      @NonNull Integer sectionId, @NonNull DateRange dateRange, Status status) {
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
  @Cacheable(key = "#sectionId + '-' + #dateRange")
  public Iterable<Attendance> getAttendanceInSection(Integer sectionId, DateRange dateRange) {
    return this.getStudentAttendanceInSectionBetweenDate(sectionId, dateRange);
  }

  @Override
  @Cacheable(key = "#sectionId + '-' + #dateRange + '-' + #status")
  public long countAttendanceInSection(
      @NonNull Integer sectionId, DateRange dateRange, Status status) {
    switch (status) {
      case ONTIME -> {
        return this.countAttendanceInSectionByStatusAndBetweenDate(
            sectionId, Status.ONTIME, dateRange);
      }

      case LATE -> {
        return this.countAttendanceInSectionByStatusAndBetweenDate(
            sectionId, Status.LATE, dateRange);
      }

      default -> {
        return this.attendanceRepository.countSectionIdBetweenDate(
            sectionId, dateRange.getStartDate(), dateRange.getEndDate());
      }
    }
  }

  /**
   * Counts the number of student attendances in a section based on the attendance status and date.
   *
   * @param sectionId the ID of the section
   * @param attendanceStatus the status of the attendance
   * @param date the date of the attendance
   * @return the number of student attendances
   */
  @Override
  @Cacheable(key = "#sectionId + '-' + #attendanceStatus + '-' + #date")
  public long countAttendanceInSectionByStatusAndDate(
      @NonNull Integer sectionId, Status attendanceStatus, LocalDate date) {
    return this.attendanceRepository.countSectionIdDateAndStatus(sectionId, attendanceStatus, date);
  }

  /**
   * Counts the number of student attendance records in a given section, filtered by attendance
   * status and date range.
   *
   * @param sectionId the ID of the section to count attendance records for
   * @param attendanceStatus the attendance status to filter by
   * @param dateRange the date range to filter by
   * @return the number of student attendance records that match the given criteria
   */
  @Override
  @Cacheable(key = "#sectionId + '-' + #attendanceStatus + '-' + #dateRange")
  public long countAttendanceInSectionByStatusAndBetweenDate(
      @NonNull Integer sectionId, Status attendanceStatus, DateRange dateRange) {
    return this.attendanceRepository.countSectionIdBetweenDateAndStatus(
        sectionId, dateRange.getStartDate(), dateRange.getEndDate(), attendanceStatus);
  }

  @Override
  @Cacheable(key = "#sectionId + '-' + #date")
  public long countAttendanceBySectionAndDate(Integer sectionId, LocalDate date) {
    return this.attendanceRepository.countSectionIdAndDate(sectionId, date);
  }

  /**
   * Counts the number of attendances between two given dates.
   *
   * @param dateRange The start and end dates of the range
   * @return the number of attendances between the start and end dates
   */
  @Override
  @Cacheable(key = "#dateRange")
  public long countAttendanceBetweenDate(DateRange dateRange) {
    return this.attendanceRepository.countBetweenDate(
        dateRange.getStartDate(), dateRange.getEndDate());
  }

  /**
   * Counts the number of attendances for a student between two given dates.
   *
   * @param studentLrn the LRN (Learner Reference Number) of the student
   * @param dateRange the start and end dates of the range
   * @return the number of attendances for the student within the specified period
   */
  @Override
  @Cacheable(key = "#studentLrn + '-' + #dateRange")
  public long countStudentAttendanceBetweenDate(Long studentLrn, DateRange dateRange) {
    return this.attendanceRepository.countLrnBetweenDate(
        studentLrn, dateRange.getStartDate(), dateRange.getEndDate());
  }

  @Override
  @Cacheable(key = "#strand.id + '-' + #date")
  public long countByStudentStrandAndDate(Strand strand, LocalDate date, Status status) {
    return this.attendanceRepository.countSectionStrandAndStatus(strand, date, status);
  }

  @Override
  @Cacheable(key = "#gradeLevel.id + '-' + #date")
  public long countByStudentGradeLevelByStatusAndDate(
      Gradelevel gradeLevel, Status status, LocalDate date) {
    return this.attendanceRepository.countStudentGradeLevelAndDateAndStatus(
        gradeLevel, date, status);
  }

  @Override
  public boolean isLrnAndDateExist(Long studentLrn, LocalDate date) {
    return this.attendanceRepository.isLrnAndDateExist(studentLrn, date);
  }

  @Override
  @CacheEvict(key = "#student.lrn + '-' + #date")
  public void setAsAbsent(Student student, LocalDate date) {
    Attendance attendance = new Attendance();
    attendance.setDate(date);
    attendance.setStudent(student);
    attendance.setAttendanceStatus(Status.ABSENT);
    this.attendanceRepository.save(attendance);
  }

  @Override
  public void absentAllNoAttendanceToday() {
    LocalDate today = LocalDate.now();
    Iterable<Student> students = studentRepository.findAll();
    logger.info("Absent all students that has no attendance today");
    students.forEach(
        student -> {
          if (isLrnAndDateExist(student.getLrn(), today)) {
            setAsAbsent(student, today);
          }
        });
  }

  @Override
  public Status getStatus() {
    LocalTime lateArrivalTime;
    LocalTime onTimeArrival = configurationService.getOnTimeArrival();

    Time currentTime = new Time(System.currentTimeMillis());
    LocalTime currentLocalTime = currentTime.toLocalTime();

    // Flag Ceremony Time
    if (isTodayMonday()) {
      lateArrivalTime = configurationService.getFlagCeremonyTime();
    } else {
      lateArrivalTime = configurationService.getLateTimeArrival();
    }

    if (currentLocalTime.isBefore(lateArrivalTime) && currentLocalTime.isAfter(onTimeArrival)) {
      return Status.ONTIME;
    } else if (currentLocalTime.isAfter(lateArrivalTime)) {
      return Status.LATE;
    } else {
      return Status.ONTIME;
    }
  }
}
