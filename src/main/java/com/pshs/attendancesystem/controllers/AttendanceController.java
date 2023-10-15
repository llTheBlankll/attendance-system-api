package com.pshs.attendancesystem.controllers;

import com.pshs.attendancesystem.Enums;
import com.pshs.attendancesystem.entities.Attendance;
import com.pshs.attendancesystem.entities.Student;
import com.pshs.attendancesystem.repositories.AttendanceRepository;
import com.pshs.attendancesystem.repositories.StudentRepository;
import org.springframework.web.bind.annotation.*;

import java.sql.Time;
import java.text.ParseException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/attendance")
public class AttendanceController {

    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;
    LocalDate today = LocalDate.now();
    LocalDate firstDayOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

    public AttendanceController(AttendanceRepository attendanceRepository, StudentRepository studentRepository) {
        this.attendanceRepository = attendanceRepository;
        this.studentRepository = studentRepository;
    }

    @GetMapping("/attendances")
    public Iterable<Attendance> getAllAttendance() {
        return this.attendanceRepository.findAll();
    }

    @PutMapping("/add/{studentLrn}")
    public String addAttendance(@PathVariable Long studentLrn) {
        // Check for the existence of Student LRN
        if (!studentRepository.existsById(studentLrn)) {
            return "Student LRN does not exist";
        }
        // Change flag ceremony time if today is monday.
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(calendar.getTime());
        boolean monday = calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY;
        LocalTime flagCeremonyTime;

        if (monday) {
            flagCeremonyTime = Time.valueOf("6:30:00").toLocalTime();
        } else {
            flagCeremonyTime = Time.valueOf("7:00:00").toLocalTime();
        }

        LocalTime earliestTimeToArrive = Time.valueOf("5:30:00").toLocalTime();
        Date date = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
        Time currentTime = new Time(System.currentTimeMillis());
        LocalTime currentLocalTime = currentTime.toLocalTime();

        Optional<Student> student = this.studentRepository.findById(studentLrn);
        if (student.isPresent()) {
            Attendance attendance = new Attendance();
            attendance.setStudent(student.get());

            if (currentLocalTime.isBefore(flagCeremonyTime) && currentLocalTime.isAfter(earliestTimeToArrive)) {
                attendance.setAttendanceStatus(Enums.status.ONTIME);
            } else if (currentLocalTime.isAfter(flagCeremonyTime)) {
                attendance.setAttendanceStatus(Enums.status.LATE);
            } else if (currentLocalTime.isBefore(earliestTimeToArrive)) {
                // EARLY
            }

            attendance.setDate(date);
            attendance.setTime(Time.valueOf(LocalTime.now()));
            attendance.setId(attendanceRepository.getNextSeriesId());
            this.attendanceRepository.save(attendance);
            return "The student is " + attendance.getAttendanceStatus();
        }

        return "Student does not exist";
    }

    @DeleteMapping("/delete/id/{id}")
    public String deleteAttendance(@PathVariable Integer id) {
        if (!this.attendanceRepository.existsById(id)) {
            return "Attendance does not exist";
        }

        this.attendanceRepository.deleteById(id);
        return "Attendance was deleted";
    }

    @PostMapping ("/update")
    public String updateAttendance(@RequestBody Attendance attendance) {
        if (!this.attendanceRepository.existsById(attendance.getId())) {
            return "Attendance does not exist";
        }

        this.attendanceRepository.save(attendance);
        return "Attendance was updated";
    }

    @GetMapping("/late/month")
    public Iterable<Attendance> getLateStudentsThisMonth() throws ParseException{
        LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate lastDayOfMonth = today.withDayOfMonth(today.lengthOfMonth());

        return this.attendanceRepository.findByDateAfterAndDateBeforeAndAttendanceStatus(
                Date.from(firstDayOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant()),
                Date.from(lastDayOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant()),
                Enums.status.LATE
        );
    }

    @GetMapping("/ontime/month")
    public Iterable<Attendance> getOnTimeStudentsThisMonth() throws ParseException {

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
    public Iterable<Attendance> getLateStudentsThisWeek() throws ParseException {
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
    public Iterable<Attendance> getOnTimeStudentsThisWeek() throws ParseException {
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
