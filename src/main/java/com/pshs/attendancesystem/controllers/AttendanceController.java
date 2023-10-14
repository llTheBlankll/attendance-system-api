package com.pshs.attendancesystem.controllers;

import com.pshs.attendancesystem.Enums;
import com.pshs.attendancesystem.entities.Attendance;
import com.pshs.attendancesystem.entities.Student;
import com.pshs.attendancesystem.repositories.AttendanceRepository;
import com.pshs.attendancesystem.repositories.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/attendance")
public class AttendanceController {

    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;

    public AttendanceController(AttendanceRepository attendanceRepository, StudentRepository studentRepository) {
        this.attendanceRepository = attendanceRepository;
        this.studentRepository = studentRepository;
    }

    @GetMapping("/attendances")
    public Iterable<Attendance> getAllAttendance() {
        return this.attendanceRepository.findAll();
    }

    @PutMapping("/add/{student_lrn}")
    public String addAttendance(@PathVariable Long student_lrn) {
        // Check for the existence of Student LRN
        if (!studentRepository.existsById(student_lrn)) {
            return "Student LRN does not exist";
        }

        LocalTime flagCeremonyTime = Time.valueOf("7:00:00").toLocalTime();
        LocalTime earliestTimeToArrive = Time.valueOf("4:00:00").toLocalTime();
        Date date = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());

        Optional<Student> student = this.studentRepository.findById(student_lrn);
        if (student.isPresent()) {
            Attendance attendance = new Attendance();
            attendance.setStudent(student.get());

            if (flagCeremonyTime.isBefore(earliestTimeToArrive)) {
                attendance.setAttendance_status(Enums.status.ONTIME);
            } else if (flagCeremonyTime.isAfter(earliestTimeToArrive)) {
                attendance.setAttendance_status(Enums.status.LATE);
            } else {
                // EARLY
            }

            attendance.setDate(date);
            attendance.setTime(Time.valueOf(LocalTime.now()));
            attendance.setId(attendanceRepository.getNextSeriesId());
            this.attendanceRepository.save(attendance);
            return "The student is " + attendance.getAttendance_status();
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
}
