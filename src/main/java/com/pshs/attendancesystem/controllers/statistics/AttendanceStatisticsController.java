package com.pshs.attendancesystem.controllers.statistics;

import com.pshs.attendancesystem.entities.Attendance;
import com.pshs.attendancesystem.entities.Student;
import com.pshs.attendancesystem.enums.Status;
import com.pshs.attendancesystem.impl.ManipulateAttendance;
import com.pshs.attendancesystem.repositories.AttendanceRepository;
import com.pshs.attendancesystem.repositories.StudentRepository;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/attendance/statistics")
public class AttendanceStatisticsController {

    private final ManipulateAttendance manipulateAttendance;
    LocalDate today = LocalDate.now();
    public AttendanceStatisticsController(AttendanceRepository attendanceRepository,
                                          StudentRepository studentRepository) {
        manipulateAttendance = new ManipulateAttendance(attendanceRepository, studentRepository);
    }

    /**
     * Retrieves the attendance records for a specific student.
     *
     * @param studentLrn the learning reference number of the student
     * @return an iterable collection of Attendance objects representing the student's attendance records
     */
    @GetMapping("/student/{studentLrn}")
    public Iterable<Attendance> getStudentAttendanceBetweenDate(@PathVariable Long studentLrn) {
        return this.manipulateAttendance.getStudentAttendanceBetweenDate(
                studentLrn,
                LocalDate.now(),
                LocalDate.now());
    }

    /**
     * Retrieves the list of students who were marked as late this month.
     *
     * @return An iterable of Attendance objects representing the late students.
     */
    @GetMapping("/late/month")
    public Iterable<Attendance> getLateStudentsThisMonth() {
        LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate lastDayOfMonth = today.withDayOfMonth(today.lengthOfMonth());

        return this.manipulateAttendance.getAllAttendanceBetweenDate(firstDayOfMonth, lastDayOfMonth,
                Status.LATE);
    }

    /**
     * Retrieves the attendance records of students who arrived on time during the current month.
     *
     * @return an Iterable of Attendance objects representing the on-time attendance records
     */
    @GetMapping("/ontime/month")
    public Iterable<Attendance> getOnTimeStudentsThisMonth() {

        LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate lastDayOfMonth = today.withDayOfMonth(today.lengthOfMonth());

        return this.manipulateAttendance.getAllAttendanceBetweenDate(
                firstDayOfMonth,
                lastDayOfMonth,
                Status.ONTIME);
    }

    /**
     * Retrieves the list of late students for the current week.
     *
     * @return The list of late students for the current week.
     */
    @GetMapping("/late/week")
    public Iterable<Attendance> getLateStudentsThisWeek() {
        LocalDate firstDayOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate lastDayOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        return this.manipulateAttendance.getAllAttendanceBetweenDate(
                firstDayOfWeek,
                lastDayOfWeek,
                Status.LATE);
    }

    /**
     * Retrieves the list of students who have attended classes on time this week.
     *
     * @return An iterable collection of Attendance objects representing the students who attended on time.
     */
    @GetMapping("/ontime/week")
    public Iterable<Attendance> getOnTimeStudentsThisWeek() {
        LocalDate firstDayOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate lastDayOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        return this.manipulateAttendance.getAllAttendanceBetweenDate(
                firstDayOfWeek,
                lastDayOfWeek,
                Status.ONTIME);
    }

    /**
     * Retrieves the list of late students for today.
     *
     * @return An iterable of Attendance objects representing the late students for today.
     */
    @GetMapping("/late/today")
    public Iterable<Attendance> getLateStudentsToday() {
        return this.manipulateAttendance.getAllAttendanceBetweenDate(
                LocalDate.now(),
                LocalDate.now(),
                Status.LATE);
    }

    /**
     * Retrieves a list of students who were on time today.
     *
     * @return an iterable collection of Attendance objects representing the on time students today
     */
    @GetMapping("/ontime/today")
    public Iterable<Attendance> getOnTimeStudentsToday() {
        return this.manipulateAttendance.getAllAttendanceBetweenDate(
                LocalDate.now(),
                LocalDate.now(),
                Status.ONTIME);
    }

    /**
     * Retrieves the count of late students for the current month.
     *
     * @return the count of late students for the current month
     */
    @GetMapping("/late/month/count")
    public long getLateStudentCountThisMonth() {
        LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate lastDayOfMonth = today.withDayOfMonth(today.lengthOfMonth());

        return this.manipulateAttendance.getAllCountOfAttendanceBetweenDate(
                firstDayOfMonth,
                lastDayOfMonth,
                Status.LATE);
    }

    /**
     * Retrieves the count of on-time students for the current month.
     *
     * @return The count of on-time students for the current month.
     */
    @GetMapping("/ontime/month/count")
    public long getOnTimeStudentCountThisMonth() {
        LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate lastDayOfMonth = today.withDayOfMonth(today.lengthOfMonth());

        return this.manipulateAttendance.getAllCountOfAttendanceBetweenDate(
                firstDayOfMonth,
                lastDayOfMonth,
                Status.ONTIME);
    }

    /**
     * Retrieves the count of late students for the current week.
     *
     * @return the count of late students for the current week
     */
    @GetMapping("/late/week/count")
    public long getLateStudentCountThisWeek() {
        LocalDate firstDayOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate lastDayOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        return this.manipulateAttendance.getAllCountOfAttendanceBetweenDate(
                firstDayOfWeek,
                lastDayOfWeek,
                Status.LATE);
    }

    /**
     * Retrieves the number of on-time students for the current week.
     *
     * @return the count of on-time students for the current week
     */
    @GetMapping("/ontime/week/count")
    public long getOnTimeStudentCountThisWeek() {
        LocalDate firstDayOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate lastDayOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        return this.manipulateAttendance.getAllCountOfAttendanceBetweenDate(
                firstDayOfWeek,
                lastDayOfWeek,
                Status.ONTIME);
    }

    /**
     * Retrieves the number of on-time student attendances for the current month.
     *
     * @param studentLrn the learning reference number of the student
     * @return the count of on-time student attendances for the current month
     */
    @GetMapping("/student/{studentLrn}/month/ontime")
    public long getStudentOnTimeThisMonth(@PathVariable Long studentLrn) {
        LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate lastDayOfMonth = today.withDayOfMonth(today.lengthOfMonth());

        return this.manipulateAttendance.getAllCountOfStudentAttendanceBetweenDate(
                studentLrn,
                firstDayOfMonth,
                lastDayOfMonth,
                Status.ONTIME);
    }

    /**
     * Retrieves the total count of late attendances for a specific student in the current month.
     *
     * @param studentLrn the learning resource number (LRN) of the student
     * @return the total count of late attendances for the student this month
     */
    @GetMapping("/student/{studentLrn}/month/late")
    public long getStudentLateThisMonth(@PathVariable Long studentLrn) {
        LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate lastDayOfMonth = today.withDayOfMonth(today.lengthOfMonth());

        return this.manipulateAttendance.getAllCountOfStudentAttendanceBetweenDate(
                studentLrn,
                firstDayOfMonth,
                lastDayOfMonth,
                Status.LATE);
    }

    /**
     * Retrieves the count of on-time student attendance for the current week.
     *
     * @param studentLrn the student's LRN (Learner Reference Number)
     * @return the count of on-time student attendance for the current week
     */
    @GetMapping("/student/{studentLrn}/week/ontime")
    public long getStudentOnTimeThisWeek(@PathVariable Long studentLrn) {
        LocalDate firstDayOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate lastDayOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        return this.manipulateAttendance.getAllCountOfStudentAttendanceBetweenDate(
                studentLrn,
                firstDayOfWeek,
                lastDayOfWeek,
                Status.ONTIME);
    }

    /**
     * Retrieves the number of late attendances for a specific student in the current week.
     *
     * @param studentLrn the learning reference number of the student
     * @return the count of late attendances for the student in the current week
     */
    @GetMapping("/student/{studentLrn}/week/late")
    public long getStudentLateThisWeek(@PathVariable Long studentLrn) {
        LocalDate firstDayOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate lastDayOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        return this.manipulateAttendance.getAllCountOfStudentAttendanceBetweenDate(
                studentLrn,
                firstDayOfWeek,
                lastDayOfWeek,
                Status.LATE);
    }

    /**
     * Retrieves the attendance records for a student within a specific date range and attendance status.
     *
     * @param dates      object containing the start and end dates of the range
     * @param studentLrn the LRN (Learner Reference Number) of the student
     * @param status     the attendance status to filter the records by
     * @return an iterable collection of Attendance objects matching the specified criteria
     */
    @GetMapping("/student/{studentLrn}/{status}")
    public Iterable<Attendance> getStudentAttendanceBetweenDate(@RequestBody BetweenDate dates, @PathVariable Long studentLrn, @PathVariable Status status) {
        return this.manipulateAttendance.getStudentAttendanceBetweenDateWithAttendanceStatus(studentLrn, dates.getFirstDate(), dates.getSecondDate(), status);
    }

    /**
     * Retrieves the attendance of students in a specific section.
     *
     * @param  sectionId  the ID of the section
     * @return            a list of Student objects representing the attendance of students in the section
     */
    @GetMapping("/seection/{sectionId}/attendance")
    public List<Student> getStudentAttendanceInSectionId(@PathVariable String sectionId) {
        Iterable<Attendance> studentsAttendance = this.manipulateAttendance.getStudentAttendanceInSectionId(sectionId);
        List<Student> students = new ArrayList<>();

        studentsAttendance.forEach(student -> students.add(student.getStudent()));

        return students;
    }

    /**
     * Retrieves the attendance of students in a specific section by their attendance status within a given date range.
     *
     * @param  betweenDate  the date range within which the attendance is being retrieved
     * @param  status       the status of the attendance (e.g., present, absent)
     * @param  sectionId    the ID of the section for which the attendance is being retrieved
     * @return              a list of students who have attended the section within the specified date range and status
     */
    @GetMapping("/student/{sectionId}")
    public List<Student> getStudentsAttendanceInSectionIdByStatus(@RequestBody BetweenDate betweenDate, @RequestParam Status status, @RequestParam String sectionId) {
        Iterable<Attendance> studentsAttendance = manipulateAttendance.getStudentAttendanceInSectionIdByAttendanceStatusBetweenDate(sectionId, status, betweenDate.getFirstDate(), betweenDate.getSecondDate());
        List<Student> students = new ArrayList<>();

        studentsAttendance.forEach(student -> students.add(student.getStudent()));

        return students;
    }

    /**
     * Retrieves the count of student attendance in a specific section on a given date.
     *
     * @param  sectionId       the ID of the section
     * @param  attendanceStatus  the attendance status
     * @param  date            the date to filter the attendance count
     * @return                 the count of student attendance in the specified section on the given date
     */
    @GetMapping("/count/date/{sectionId}")
    public long getStudentAttendanceCountInSectionId(@PathVariable String sectionId, @RequestParam("status") Status attendanceStatus , @RequestParam LocalDate date) {
        return this.manipulateAttendance.countStudentAttendanceInSectionIdByAttendanceStatusAndDate(sectionId, attendanceStatus, date);
    }

    /**
     * Retrieves the number of student attendance records in a given section between two dates.
     *
     * @param  sectionId          the ID of the section
     * @param  attendanceStatus   the attendance status to filter by
     * @param  date               the date range to filter by
     * @return                    the count of student attendance records
     */
    @GetMapping("/count/between-date/{sectionId}")
    public long getStudentAttendanceCountInSectionIdBetweenDate(@PathVariable String sectionId, @RequestParam("status") Status attendanceStatus, @RequestBody BetweenDate date) {
        return this.manipulateAttendance.countStudentAttendanceInSectionIdByAttendanceStatusBetweenDate(sectionId, attendanceStatus, date.getFirstDate(), date.getSecondDate());
    }

    private static class BetweenDate {
        private LocalDate firstDate;
        private LocalDate secondDate;

        public LocalDate getFirstDate() {
            return firstDate;
        }

        public void setFirstDate(LocalDate firstDate) {
            this.firstDate = firstDate;
        }

        public LocalDate getSecondDate() {
            return secondDate;
        }

        public void setSecondDate(LocalDate secondDate) {
            this.secondDate = secondDate;
        }
    }
}

