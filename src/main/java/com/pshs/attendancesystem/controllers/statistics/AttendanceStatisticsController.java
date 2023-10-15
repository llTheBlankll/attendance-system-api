package com.pshs.attendancesystem.controllers.statistics;

import com.pshs.attendancesystem.Enums;
import com.pshs.attendancesystem.entities.Attendance;
import com.pshs.attendancesystem.repositories.AttendanceRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/attendance/statistics")
public class AttendanceStatisticsController {

    private final AttendanceRepository attendanceRepository;
    LocalDate today = LocalDate.now();
    LocalDate firstDayOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

    public AttendanceStatisticsController(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    @GetMapping("/late/month")
    public Iterable<Attendance> getLateStudentsThisMonth() {
        LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate lastDayOfMonth = today.withDayOfMonth(today.lengthOfMonth());

        return this.attendanceRepository.findByDateAfterAndDateBeforeAndAttendanceStatus(
                Date.from(firstDayOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant()),
                Date.from(lastDayOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant()),
                Enums.status.LATE
        );
    }

    @GetMapping("/ontime/month")
    public Iterable<Attendance> getOnTimeStudentsThisMonth() {

        LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate lastDayOfMonth = today.withDayOfMonth(today.lengthOfMonth());
        Date fromFirstMonth = Date.from(firstDayOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date toLastMonth = Date.from(lastDayOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant());

        return this.attendanceRepository.findByDateAfterAndDateBeforeAndAttendanceStatus(
                fromFirstMonth,
                toLastMonth,
                Enums.status.ONTIME
        );
    }

    @GetMapping("/late/week")
    public Iterable<Attendance> getLateStudentsThisWeek() {
        LocalDate lastDayOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        Date fromFirstWeek = Date.from(firstDayOfWeek.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date toLastWeek = Date.from(lastDayOfWeek.atStartOfDay(ZoneId.systemDefault()).toInstant());

        return this.attendanceRepository.findByDateAfterAndDateBeforeAndAttendanceStatus(
                fromFirstWeek,
                toLastWeek,
                Enums.status.LATE
        );
    }

    @GetMapping("/ontime/week")
    public Iterable<Attendance> getOnTimeStudentsThisWeek() {
        LocalDate lastDayOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        Date fromFirstWeek = Date.from(firstDayOfWeek.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date toLastWeek = Date.from(lastDayOfWeek.atStartOfDay(ZoneId.systemDefault()).toInstant());

        return this.attendanceRepository.findByDateAfterAndDateBeforeAndAttendanceStatus(
                fromFirstWeek,
                toLastWeek,
                Enums.status.ONTIME
        );
    }
}
