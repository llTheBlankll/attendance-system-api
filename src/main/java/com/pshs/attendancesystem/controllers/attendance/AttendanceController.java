package com.pshs.attendancesystem.controllers.attendance;

import com.pshs.attendancesystem.entities.Attendance;
import com.pshs.attendancesystem.enums.Status;
import com.pshs.attendancesystem.impl.AttendanceServiceImpl;
import com.pshs.attendancesystem.services.AttendanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@Tag(name = "Attendance", description = "The Attendance Endpoints")
@RequestMapping("${api.root}/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceServiceImpl attendanceService) {
        this.attendanceService = attendanceService;
    }

    /**
     * Retrieves all attendance records.
     *
     * @return an iterable collection of Attendance objects representing all attendance records
     */
    @Operation(
        summary = "Retrieves all attendance records",
        description = "Retrieves all attendance records"
    )
    @GetMapping("/all")
    public Iterable<Attendance> getAllAttendance() {
        return attendanceService.getAllAttendances();
    }

    @Operation(
        summary = "Create attendance",
        description = "Create attendance of a student using their LRN"
    )
    @PostMapping("/create")
    public Status addAttendance(@RequestParam("student_lrn") Long studentLrn) {
        return attendanceService.createAttendance(studentLrn);
    }

    /**
     * Deletes the attendance record with the specified ID.
     *
     * @param id the ID of the attendance record to be deleted
     * @return a message indicating whether the attendance record was successfully deleted or not
     */
    @Operation(
        summary = "Delete attendance",
        description = "Delete attendance of a student using attendance id"
    )
    @PostMapping(value = "/delete", produces = "text/plain")
    public String deleteAttendance(@RequestParam Integer id) {
        return attendanceService.deleteAttendance(id);
    }

    /**
     * Updates the attendance record.
     *
     * @param attendance the attendance object to be updated
     * @return a string indicating the result of the update
     */
    @Operation(
        summary = "Update attendance",
        description = "Update attendance of a student using attendance id"
    )
    @PostMapping(value = "/update", produces = "text/plain")
    public String updateAttendance(@RequestBody Attendance attendance) {
        return attendanceService.updateAttendance(attendance);
    }

    @Operation(
        summary = "Delete all attendance",
        description = "Use this endpoint to delete all attendance, please use with care as there is no confirmation. Principal only."
    )
    @PreAuthorize("hasRole('PRINCIPAL')")
    @PostMapping(value = "/delete/all", produces = "text/plain")
    public String deleteAllAttendance() {
        return attendanceService.deleteAllAttendance();
    }
}
