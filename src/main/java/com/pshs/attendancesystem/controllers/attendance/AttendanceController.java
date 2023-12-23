package com.pshs.attendancesystem.controllers.attendance;

import com.pshs.attendancesystem.entities.Attendance;
import com.pshs.attendancesystem.impl.AttendanceServiceImpl;
import com.pshs.attendancesystem.services.AttendanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@Tag(name = "Attendance", description = "The Attendance Endpoints")
@RequestMapping("${api.root}/attendance")
@SecurityRequirement(
	name = "JWT Authentication"
)
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
	public void createAttendance(@RequestParam("q") Long studentLrn) {
		attendanceService.createAttendance(studentLrn);
	}

	/**
	 * Deletes the attendance record with the specified ID.
	 *
	 */
	@Operation(
		summary = "Delete attendance",
		description = "Delete attendance of a student using attendance id"
	)
	@PostMapping(value = "/delete", produces = "text/plain")
	public void deleteAttendance(@RequestBody Attendance attendance) {
		attendanceService.deleteAttendance(attendance.getId());
	}

	@PostMapping(value = "/delete/id", produces = "text/plain")
	public void deleteAttendanceById(@RequestParam("q") Integer id) {
		attendanceService.deleteAttendance(id);
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
		summary = "Delete all attendance (Will always return TRUE)",
		description = "Use this endpoint to delete all attendance, please use with care as there is no confirmation. Principal only."
	)
	@PreAuthorize("hasRole('PRINCIPAL')")
	@PostMapping(value = "/delete/all", produces = "text/plain")
	public boolean deleteAllAttendance() {
		attendanceService.deleteAllAttendance();
		return true;
	}

	@Operation(
		summary = "Absent all students that has no attendance",
		description = "Use this endpoint to absent all students that has no attendance, please use with care as there is no confirmation. Principal only."
	)
	@PreAuthorize("hasRole('PRINCIPAL')")
	@PostMapping(value = "/absent/all", produces = "text/plain")
	public void absentAllStudents() {
		attendanceService.absentAllNoAttendanceToday();
	}
}
