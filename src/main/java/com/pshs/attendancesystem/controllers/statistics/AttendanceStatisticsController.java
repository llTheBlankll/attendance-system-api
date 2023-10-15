package com.pshs.attendancesystem.controllers.statistics;

import com.pshs.attendancesystem.Enums;
import com.pshs.attendancesystem.entities.Attendance;
import com.pshs.attendancesystem.impl.ManipulateAttendance;
import com.pshs.attendancesystem.repositories.AttendanceRepository;
import com.pshs.attendancesystem.repositories.StudentRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/attendance/statistics")
public class AttendanceStatisticsController {

    private final AttendanceRepository attendanceRepository;
    private final ManipulateAttendance manipulateAttendance;

    LocalDate today = LocalDate.now();

    public AttendanceStatisticsController(AttendanceRepository attendanceRepository, StudentRepository studentRepository) {
        this.attendanceRepository = attendanceRepository;
        manipulateAttendance = new ManipulateAttendance(attendanceRepository, studentRepository);
    }

    @GetMapping("/late/month")
    public Iterable<Attendance> getLateStudentsThisMonth() {
        LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate lastDayOfMonth = today.withDayOfMonth(today.lengthOfMonth());

        return this.manipulateAttendance.getAllAttendanceBetweenDate(firstDayOfMonth, lastDayOfMonth, Enums.status.LATE);
    }

    @GetMapping("/ontime/month")
    public Iterable<Attendance> getOnTimeStudentsThisMonth() {

        LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate lastDayOfMonth = today.withDayOfMonth(today.lengthOfMonth());

        return this.manipulateAttendance.getAllAttendanceBetweenDate(
                firstDayOfMonth,
                lastDayOfMonth,
                Enums.status.ONTIME
        );
    }

    @GetMapping("/late/week")
    public Iterable<Attendance> getLateStudentsThisWeek() {
        LocalDate firstDayOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate lastDayOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        return this.manipulateAttendance.getAllAttendanceBetweenDate(
                firstDayOfWeek,
                lastDayOfWeek,
                Enums.status.LATE
        );
    }

    @GetMapping("/ontime/week")
    public Iterable<Attendance> getOnTimeStudentsThisWeek() {
        LocalDate firstDayOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate lastDayOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        return this.manipulateAttendance.getAllAttendanceBetweenDate(
                firstDayOfWeek,
                lastDayOfWeek,
                Enums.status.ONTIME
        );
    }

    @GetMapping("/late/today")
    public Iterable<Attendance> getLateStudentsToday() {
        return this.manipulateAttendance.getAllAttendanceBetweenDate(
                LocalDate.now(),
                LocalDate.now(),
                Enums.status.LATE
        );
    }

    @GetMapping("/ontime/today")
    public Iterable<Attendance> getOnTimeStudentsToday() {
        return this.manipulateAttendance.getAllAttendanceBetweenDate(
                LocalDate.now(),
                LocalDate.now(),
                Enums.status.ONTIME
        );
    }
}
