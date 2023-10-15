package com.pshs.attendancesystem.controllers;

import com.pshs.attendancesystem.entities.Attendance;
import com.pshs.attendancesystem.impl.ManipulateAttendance;
import com.pshs.attendancesystem.repositories.AttendanceRepository;
import com.pshs.attendancesystem.repositories.StudentRepository;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin
@RequestMapping("/api/v1/attendance")
public class AttendanceController {

    private final AttendanceRepository attendanceRepository;
    private final ManipulateAttendance manipulateAttendance;

    public AttendanceController(AttendanceRepository attendanceRepository, StudentRepository studentRepository) {
        this.attendanceRepository = attendanceRepository;
        this.manipulateAttendance = new ManipulateAttendance(this.attendanceRepository, studentRepository);
    }

    @GetMapping("/attendances")
    public Iterable<Attendance> getAllAttendance() {
        return this.attendanceRepository.findAll();
    }

    @PutMapping("/add/{studentLrn}")
    public boolean addAttendance(@PathVariable Long studentLrn) {
        return manipulateAttendance.addAttendance(studentLrn);
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
