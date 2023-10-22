package com.pshs.attendancesystem.controllers.statistics;

import com.pshs.attendancesystem.Enums;
import com.pshs.attendancesystem.entities.Attendance;
import com.pshs.attendancesystem.impl.ManipulateAttendance;
import com.pshs.attendancesystem.repositories.AttendanceRepository;
import com.pshs.attendancesystem.repositories.StudentRepository;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

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
     * Retrieves the list of students who were marked as late this month.
     *
     * @return         	An iterable of Attendance objects representing the late students.
     */
    @GetMapping("/late/month")
    public Iterable<Attendance> getLateStudentsThisMonth() {
        LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate lastDayOfMonth = today.withDayOfMonth(today.lengthOfMonth());

        return this.manipulateAttendance.getAllAttendanceBetweenDate(firstDayOfMonth, lastDayOfMonth,
                Enums.status.LATE);
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
                Enums.status.ONTIME);
    }

    /**
     * Retrieves the list of late students for the current week.
     *
     * @return         	The list of late students for the current week.
     */
    @GetMapping("/late/week")
    public Iterable<Attendance> getLateStudentsThisWeek() {
        LocalDate firstDayOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate lastDayOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        return this.manipulateAttendance.getAllAttendanceBetweenDate(
                firstDayOfWeek,
                lastDayOfWeek,
                Enums.status.LATE);
    }

    /**
     * Retrieves the list of students who have attended classes on time this week.
     *
     * @return          An iterable collection of Attendance objects representing the students who attended on time.
     */
    @GetMapping("/ontime/week")
    public Iterable<Attendance> getOnTimeStudentsThisWeek() {
        LocalDate firstDayOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate lastDayOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        return this.manipulateAttendance.getAllAttendanceBetweenDate(
                firstDayOfWeek,
                lastDayOfWeek,
                Enums.status.ONTIME);
    }

    /**
     * Retrieves the list of late students for today.
     *
     * @return          An iterable of Attendance objects representing the late students for today.
     */
    @GetMapping("/late/today")
    public Iterable<Attendance> getLateStudentsToday() {
        return this.manipulateAttendance.getAllAttendanceBetweenDate(
                LocalDate.now(),
                LocalDate.now(),
                Enums.status.LATE);
    }

    /**
     * Retrieves a list of students who were on time today.
     *
     * @return  an iterable collection of Attendance objects representing the on time students today
     */
    @GetMapping("/ontime/today")
    public Iterable<Attendance> getOnTimeStudentsToday() {
        return this.manipulateAttendance.getAllAttendanceBetweenDate(
                LocalDate.now(),
                LocalDate.now(),
                Enums.status.ONTIME);
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
                Enums.status.LATE);
    }

    /**
     * Retrieves the count of on-time students for the current month.
     *
     * @return         	The count of on-time students for the current month.
     */
    @GetMapping("/ontime/month/count")
    public long getOnTimeStudentCountThisMonth() {
        LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate lastDayOfMonth = today.withDayOfMonth(today.lengthOfMonth());

        return this.manipulateAttendance.getAllCountOfAttendanceBetweenDate(
                firstDayOfMonth,
                lastDayOfMonth,
                Enums.status.ONTIME);
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
                Enums.status.LATE);
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
                Enums.status.ONTIME);
    }

    /**
     * Retrieves the number of on-time student attendances for the current month.
     *
     * @param  studentLrn  the learning reference number of the student
     * @return             the count of on-time student attendances for the current month
     */
    @GetMapping("/student/{studentLrn}/month/ontime")
    public long getStudentOnTimeThisMonth(@PathVariable Long studentLrn) {
        LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate lastDayOfMonth = today.withDayOfMonth(today.lengthOfMonth());

        return this.manipulateAttendance.getAllCountOfStudentAttendanceBetweenDate(
                studentLrn,
                firstDayOfMonth,
                lastDayOfMonth,
                Enums.status.ONTIME);
    }

    /**
     * Retrieves the total count of late attendances for a specific student in the current month.
     *
     * @param  studentLrn  the learning resource number (LRN) of the student
     * @return             the total count of late attendances for the student this month
     */
    @GetMapping("/student/{studentLrn}/month/late")
    public long getStudentLateThisMonth(@PathVariable Long studentLrn) {
        LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate lastDayOfMonth = today.withDayOfMonth(today.lengthOfMonth());

        return this.manipulateAttendance.getAllCountOfStudentAttendanceBetweenDate(
                studentLrn,
                firstDayOfMonth,
                lastDayOfMonth,
                Enums.status.LATE);
    }

    /**
     * Retrieves the count of on-time student attendance for the current week.
     *
     * @param  studentLrn  the student's LRN (Learner Reference Number)
     * @return             the count of on-time student attendance for the current week
     */
    @GetMapping("/student/{studentLrn}/week/ontime")
    public long getStudentOnTimeThisWeek(@PathVariable Long studentLrn) {
        LocalDate firstDayOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate lastDayOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        return this.manipulateAttendance.getAllCountOfStudentAttendanceBetweenDate(
                studentLrn,
                firstDayOfWeek,
                lastDayOfWeek,
                Enums.status.ONTIME);
    }

    /**
     * Retrieves the number of late attendances for a specific student in the current week.
     *
     * @param  studentLrn  the learning reference number of the student
     * @return             the count of late attendances for the student in the current week
     */
    @GetMapping("/student/{studentLrn}/week/late")
    public long getStudentLateThisWeek(@PathVariable Long studentLrn) {
        LocalDate firstDayOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate lastDayOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        return this.manipulateAttendance.getAllCountOfStudentAttendanceBetweenDate(
                studentLrn,
                firstDayOfWeek,
                lastDayOfWeek,
                Enums.status.LATE);
    }

    /**
     * Retrieves the attendance records for a specific student.
     *
     * @param  studentLrn  the learning reference number of the student
     * @return             an iterable collection of Attendance objects representing the student's attendance records
     */
    @GetMapping("/student/{studentLrn}")
    public Iterable<Attendance> getStudentAttendance(@PathVariable Long studentLrn) {
        return this.manipulateAttendance.getStudentAttendanceBetweenDate(
                studentLrn,
                LocalDate.now(),
                LocalDate.now());
    }
}
