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

    /**
     * Retrieves all attendance records.
     *
     * @return an iterable collection of Attendance objects representing all attendance records
     */
    @GetMapping("/attendances")
    public Iterable<Attendance> getAllAttendance() {
        return this.attendanceRepository.findAll();
    }

    /**
     * Adds attendance for a student.
     *
     * @param  studentLrn  the Long studentLrn to add attendance for
     * @return             true if the attendance was successfully added, false otherwise
     */
    @PutMapping("/add/{studentLrn}")
    public boolean addAttendance(@PathVariable Long studentLrn) {
        return manipulateAttendance.addAttendance(studentLrn);
    }

    /**
     * Deletes the attendance record with the specified ID.
     *
     * @param  id  the ID of the attendance record to be deleted
     * @return     a message indicating whether the attendance record was successfully deleted or not
     */
    @DeleteMapping("/delete/id/{id}")
    public String deleteAttendance(@PathVariable Integer id) {
        if (!this.attendanceRepository.existsById(id)) {
            return "Attendance does not exist";
        }

        this.attendanceRepository.deleteById(id);
        return "Attendance was deleted";
    }

    /**
     * Updates the attendance record.
     *
     * @param  attendance  the attendance object to be updated
     * @return             a string indicating the result of the update
     */
    @PostMapping ("/update")
    public String updateAttendance(@RequestBody Attendance attendance) {
        if (!this.attendanceRepository.existsById(attendance.getId())) {
            return "Attendance does not exist";
        }

        this.attendanceRepository.save(attendance);
        return "Attendance was updated";
    }
}
