package com.pshs.attendancesystem.controllers;

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
@RequestMapping("/v1/attendance")
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
		return this.attendanceService.getAllAttendances();
	}

	@Operation(
		summary = "Create attendance",
		description = "Create attendance of a student using their LRN"
	)
	@PostMapping("/create")
	public Status addAttendance(@RequestParam("student_lrn") Long studentLrn) {
		return this.attendanceService.createAttendance(studentLrn);
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
	@PostMapping("/delete")
	public String deleteAttendance(@RequestParam Integer id) {
		return this.attendanceService.deleteAttendance(id);
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
	@PostMapping("/update")
	public String updateAttendance(@RequestBody Attendance attendance) {
		return this.attendanceService.updateAttendance(attendance);
	}

	@Operation(
		summary = "Delete all attendance",
		description = "Use this endpoint to delete all attendance, please use with care as there is no confirmation. Principal only."
	)
	@PreAuthorize("hasRole('PRINCIPAL')")
	@PostMapping("/delete/all")
	public String deleteAllAttendance() {
		return this.attendanceService.deleteAllAttendance();
	}
}
