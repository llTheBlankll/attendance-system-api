package com.pshs.attendancesystem.impl;

import com.pshs.attendancesystem.AttendanceSystemConfiguration;
import com.pshs.attendancesystem.entities.Attendance;
import com.pshs.attendancesystem.entities.Student;
import com.pshs.attendancesystem.enums.Status;
import com.pshs.attendancesystem.messages.AttendanceMessages;
import com.pshs.attendancesystem.messages.StudentMessages;
import com.pshs.attendancesystem.repositories.AttendanceRepository;
import com.pshs.attendancesystem.repositories.StudentRepository;
import com.pshs.attendancesystem.services.AttendanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
            LocalTime onTimeArrival = AttendanceSystemConfiguration.Attendance.onTimeArrival;

            Time currentTime = new Time(System.currentTimeMillis());
            LocalTime currentLocalTime = currentTime.toLocalTime();

            // Get Student Data from the database.

            // Flag Ceremony Time
            if (isTodayMonday()) {
                lateArrivalTime = AttendanceSystemConfiguration.Attendance.flagCeremonyTime;
            } else {
                lateArrivalTime = AttendanceSystemConfiguration.Attendance.lateTimeArrival;
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
        // Check for the existence of Student LRN
        if (!studentRepository.existsById(studentLrn)) {
            logger.info(StudentMessages.STUDENT_LRN_NOT_EXISTS);
            return false;
        }

        Optional<Attendance> attendance = this.attendanceRepository.findByStudent_LrnAndDate(studentLrn, LocalDate.now());

        if (attendance.isPresent() && attendance.get().getTimeOut() == null) {
            Attendance getAttendance = attendance.get();
            this.attendanceRepository.studentAttendanceOut(LocalTime.now(), getAttendance.getId());
            logger.info("The student {} is out, Time left: {}", studentLrn, getAttendance.getTimeOut());
            return true;
        }

        return false;
    }

    /**
     * Retrieves the attendance record for a student based on their LRN (Learner Reference Number) for today.
     *
     * @param studentLrn the LRN of the student
     * @return the attendance record for the student today, or null if the student does not exist
     */
    @Override
    public Attendance studentTodayAttendance(Long studentLrn) {
        Optional<Attendance> attendance = this.attendanceRepository.findByStudent_LrnAndDate(studentLrn, LocalDate.now());
        if (attendance.isPresent()) {
            return attendance.orElse(null);
        }

        return null;
    }

    @Override
    public String deleteAllAttendance() {
        this.attendanceRepository.deleteAll();
        return AttendanceMessages.ATTENDANCE_DELETED;
    }

    /**
     * Retrieves all attendance records between a given start date and end date, filtered by status.
     *
     * @param startDate the start date of the attendance records to retrieve
     * @param endDate   the end date of the attendance records to retrieve
     * @param status    the status to filter the attendance records by
     * @return an iterable collection of attendance records
     */
    @Override
    public Iterable<Attendance> getAllAttendanceBetweenDateWithStatus(LocalDate startDate, LocalDate endDate, Status status) {
        return attendanceRepository.findByDateGreaterThanEqualAndDateLessThanEqualAndAttendanceStatus(startDate, endDate, status);
    }

    /**
     * Retrieves all attendance records between the specified start date and end date.
     *
     * @param startDate the start date for the range of attendance records
     * @param endDate   the end date for the range of attendance records
     * @return an iterable collection of attendance records between the start and end dates
     */
    @Override
    public Iterable<Attendance> getAllAttendanceBetweenDate(LocalDate startDate, LocalDate endDate) {
        return attendanceRepository.findAttendancesByDateGreaterThanEqualAndDateLessThanEqual(startDate, endDate);
    }

    /**
     * Returns the total count of attendance records between the given start date and end date,
     * filtered by the specified attendance status.
     *
     * @param startDate the start date of the attendance records
     * @param endDate   the end date of the attendance records
     * @param status    the attendance status to filter by
     * @return the total count of attendance records between the start date and end date,
     * filtered by the specified attendance status
     */
    @Override
    public long getAllCountOfAttendanceBetweenDate(LocalDate startDate, LocalDate endDate, Status status) {
        return attendanceRepository.countByDateGreaterThanEqualAndDateLessThanEqualAndAttendanceStatus(startDate, endDate, status);
    }

    /**
     * Retrieves the attendance records of a student between a specified start and end date,
     * filtered by the attendance status.
     *
     * @param studentLrn the LRN (Learner Reference Number) of the student
     * @param startDate  the start date of the attendance records
     * @param endDate    the end date of the attendance records
     * @param status     the attendance status to filter the records by
     * @return an iterable collection of Attendance objects representing
     * the attendance records of the student
     */
    @Override
    public Iterable<Attendance> getStudentAttendanceBetweenDateWithAttendanceStatus(long studentLrn
        , LocalDate startDate, LocalDate endDate, Status status) {
        return attendanceRepository.findByStudentLrnAndDateGreaterThanEqualAndDateLessThanEqualAndAttendanceStatus(studentLrn, startDate, endDate, status);
    }

    /**
     * Retrieves the attendance records of a student between the specified start and end dates.
     *
     * @param studentLrn the LRN (Learner Reference Number) of the student
     * @param startDate  the start date of the attendance records
     * @param endDate    the end date of the attendance records
     * @return an iterable collection of Attendance objects representing the student's attendance between the specified dates
     */
    @Override
    public Iterable<Attendance> getStudentAttendanceBetweenDate(long studentLrn, LocalDate startDate, LocalDate endDate) {
        return this.attendanceRepository.findByStudentLrnAndDateGreaterThanEqualAndDateLessThanEqual(studentLrn, startDate, endDate);
    }

    /**
     * Retrieves the total count of student attendance records between the specified dates.
     *
     * @param studentLrn the LRN (Learner Reference Number) of the student
     * @param startDate  the start date of the range
     * @param endDate    the end date of the range
     * @param status     the attendance status to filter by
     * @return the total count of student attendance records
     */
    @Override
    public long getAllCountOfStudentAttendanceBetweenDate(long studentLrn, LocalDate startDate, LocalDate endDate, Status status) {
        return attendanceRepository.countByStudentLrnAndDateGreaterThanEqualAndDateLessThanEqualAndAttendanceStatus(studentLrn, startDate, endDate, status);
    }

    /**
     * Retrieves the attendance records of a student in a specific section.
     *
     * @param sectionId the ID of the section
     * @return an iterable of Attendance objects representing the student's attendance records
     */
    @Override
    public Iterable<Attendance> getStudentAttendanceInSectionId(Integer sectionId) {
        return attendanceRepository.findAttendancesByStudent_StudentSection_SectionId(sectionId);
    }

    /**
     * Retrieves the student attendance records for a given section ID, attendance status, and date range.
     *
     * @param sectionId        the ID of the section
     * @param attendanceStatus the desired attendance status
     * @param startDate        the start date of the date range
     * @param endDate          the end date of the date range
     * @return an iterable collection of Attendance objects representing the student attendance records
     */
    @Override
    public Iterable<Attendance> getStudentAttendanceInSectionIdByAttendanceStatusBetweenDate(Integer sectionId, Status attendanceStatus, LocalDate startDate, LocalDate endDate) {
        return this.attendanceRepository.findAttendancesByStudent_StudentSection_SectionIdAndDateGreaterThanEqualAndDateLessThanEqualAndAttendanceStatus(sectionId, startDate, endDate, attendanceStatus);
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
    public long countStudentAttendanceInSectionIdByAttendanceStatusAndDate(Integer sectionId, Status attendanceStatus, LocalDate date) {
        return this.attendanceRepository.countByStudent_StudentSection_SectionIdAndAttendanceStatusAndDate(sectionId, attendanceStatus, date);
    }

    /**
     * Counts the number of student attendance records in a given section, filtered by attendance status and date range.
     *
     * @param sectionId        the ID of the section to count attendance records for
     * @param attendanceStatus the attendance status to filter by
     * @param startDate        the start date of the date range
     * @param endDate          the end date of the date range
     * @return the number of student attendance records that match the given criteria
     */
    @Override
    public long countStudentAttendanceIBySectionIdByAttendanceStatusBetweenDate(Integer sectionId, Status attendanceStatus, LocalDate startDate, LocalDate endDate) {
        return this.attendanceRepository.countByStudent_StudentSection_SectionIdAndDateGreaterThanEqualAndDateLessThanEqualAndAttendanceStatus(sectionId, startDate, endDate, attendanceStatus);
    }

    // TODO: implement this.
    @Override
    public long countStudentAttendanceInSectionByDate(Integer sectionId, LocalDate date) {
        return 0;
    }

    /**
     * Counts the number of attendances between two given dates.
     *
     * @param startDate the start date of the period
     * @param endDate   the end date of the period
     * @return the number of attendances between the start and end dates
     */
    @Override
    public long countAttendancesBetweenDate(LocalDate startDate, LocalDate endDate) {
        return this.attendanceRepository.countByDateGreaterThanEqualAndDateLessThanEqual(startDate, endDate);
    }

    /**
     * Counts the number of attendances for a student between two given dates.
     *
     * @param studentLrn the LRN (Learner Reference Number) of the student
     * @param startDate  the start date of the attendance period
     * @param endDate    the end date of the attendance period
     * @return the number of attendances for the student within the specified period
     */
    @Override
    public long countStudentAttendancesBetweenDate(Long studentLrn, LocalDate startDate, LocalDate endDate) {
        return this.attendanceRepository.countByStudentLrnAndDateGreaterThanEqualAndDateLessThanEqual(studentLrn, startDate, endDate);
    }
}
