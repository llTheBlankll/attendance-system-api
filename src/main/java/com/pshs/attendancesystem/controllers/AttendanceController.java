package com.pshs.attendancesystem.controllers;

import com.pshs.attendancesystem.entities.Attendance;
import com.pshs.attendancesystem.messages.AttendanceMessages;
import com.pshs.attendancesystem.services.AttendanceService;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin
@RequestMapping("/api/v1/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    /**
     * Retrieves all attendance records.
     *
     * @return an iterable collection of Attendance objects representing all attendance records
     */
    @GetMapping("/attendances")
    public Iterable<Attendance> getAllAttendance() {
        return this.attendanceService.getAllAttendances();
    }

    @PostMapping("/create")
    public String addAttendance(@RequestParam("student_lrn") Long studentLrn) {
        return this.attendanceService.createAttendance(studentLrn);
    }

    /**
     * Deletes the attendance record with the specified ID.
     *
     * @param id the ID of the attendance record to be deleted
     * @return a message indicating whether the attendance record was successfully deleted or not
     */
    @PostMapping("/delete")
    public String deleteAttendance(@RequestParam Integer id) {
        if (!this.attendanceService.existsByAttendanceId(id)) {
            return AttendanceMessages.ATTENDANCE_NOT_FOUND;
        }

        return this.attendanceService.deleteAttendance(id);
    }

    /**
     * Updates the attendance record.
     *
     * @param attendance the attendance object to be updated
     * @return a string indicating the result of the update
     */
    @PostMapping("/update")
    public String updateAttendance(@RequestBody Attendance attendance) {
        if (!this.attendanceService.existsByAttendanceId(attendance.getId())) {
            return AttendanceMessages.ATTENDANCE_NOT_FOUND;
        }

        return this.attendanceService.updateAttendance(attendance);
    }
}
