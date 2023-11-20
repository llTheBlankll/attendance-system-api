package com.pshs.attendancesystem.controllers;

import com.pshs.attendancesystem.entities.Attendance;
import com.pshs.attendancesystem.enums.Status;
import com.pshs.attendancesystem.impl.AttendanceServiceImpl;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/attendance")
public class AttendanceController {

    private final AttendanceServiceImpl attendanceServiceImpl;

    public AttendanceController(AttendanceServiceImpl attendanceServiceImpl) {
        this.attendanceServiceImpl = attendanceServiceImpl;
    }

    /**
     * Retrieves all attendance records.
     *
     * @return an iterable collection of Attendance objects representing all attendance records
     */
    @GetMapping("/attendances")
    public Iterable<Attendance> getAllAttendance() {
        return this.attendanceServiceImpl.getAllAttendances();
    }

    @PostMapping("/create")
    public Status addAttendance(@RequestParam("student_lrn") Long studentLrn) {
        return this.attendanceServiceImpl.createAttendance(studentLrn);
    }

    /**
     * Deletes the attendance record with the specified ID.
     *
     * @param id the ID of the attendance record to be deleted
     * @return a message indicating whether the attendance record was successfully deleted or not
     */
    @PostMapping("/delete")
    public String deleteAttendance(@RequestParam Integer id) {
        return this.attendanceServiceImpl.deleteAttendance(id);
    }

    /**
     * Updates the attendance record.
     *
     * @param attendance the attendance object to be updated
     * @return a string indicating the result of the update
     */
    @PostMapping("/update")
    public String updateAttendance(@RequestBody Attendance attendance) {
        return this.attendanceServiceImpl.updateAttendance(attendance);
    }

    @PostMapping("/delete/all")
    public String deleteAllAttendance() {
        return this.attendanceServiceImpl.deleteAllAttendance();
    }
}
