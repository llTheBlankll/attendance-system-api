package com.pshs.attendancesystem.controllers;

import com.pshs.attendancesystem.entities.Attendance;
import com.pshs.attendancesystem.repositories.AttendanceRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/attendance")
public class AttendanceController {

    private final AttendanceRepository attendanceRepository;

    public AttendanceController(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    @GetMapping("/attendances")
    public Iterable<Attendance> getAllAttendance() {
        return this.attendanceRepository.findAll();
    }

    @PutMapping("/add")
    public void addAttendance(@RequestBody Attendance attendance) {
        this.attendanceRepository.save(attendance);
    }

    @DeleteMapping("/delete")
    public void deleteAttendance(@RequestBody Attendance attendance) {
        this.attendanceRepository.delete(attendance);
    }
}
