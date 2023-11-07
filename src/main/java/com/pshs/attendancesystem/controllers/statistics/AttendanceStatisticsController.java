package com.pshs.attendancesystem.controllers.statistics;

import com.pshs.attendancesystem.entities.Attendance;
import com.pshs.attendancesystem.entities.statistics.*;
import com.pshs.attendancesystem.enums.Status;
import com.pshs.attendancesystem.impl.ManipulateAttendance;
import com.pshs.attendancesystem.messages.AttendanceMessages;
import com.pshs.attendancesystem.repositories.AttendanceRepository;
import com.pshs.attendancesystem.repositories.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/attendance/statistics")
public class AttendanceStatisticsController {

    private final ManipulateAttendance manipulateAttendance;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    LocalDate today = LocalDate.now();

    public AttendanceStatisticsController(AttendanceRepository attendanceRepository,
                                          StudentRepository studentRepository) {
        manipulateAttendance = new ManipulateAttendance(attendanceRepository, studentRepository);
    }

    /**
     * Retrieves the attendance records for a specific student.
     *
     * @param studentLrn the learning reference number of the student
     * @return an iterable collection of Attendance objects representing the student's attendance records
     */
    @GetMapping("/student")
    public Iterable<Attendance> getStudentAttendanceBetweenDate(@RequestParam("lrn") Long studentLrn) {
        if (studentLrn == null) {
            logger.info(AttendanceMessages.ATTENDANCE_NULL);
            return null;
        }

        LocalDate firstDayOfYear = LocalDate.now().withDayOfYear(1);
        LocalDate lastDayOfYear = today.withDayOfYear(today.lengthOfYear());


        return this.manipulateAttendance.getStudentAttendanceBetweenDate(
            studentLrn,
            firstDayOfYear,
            lastDayOfYear);
    }

    /**
     * Retrieves the attendance records for students based on a given date and status.
     *
     * @param dateWithStatus object containing the date and status to filter the attendance records
     * @return an iterable collection of Attendance objects representing the attendance records
     */
    @GetMapping("/get/date")
    public Iterable<Attendance> getStudentsAttendanceByDate(@RequestBody DateWithStatus dateWithStatus) {
        LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate lastDayOfMonth = today.withDayOfMonth(today.lengthOfMonth());

        if (dateWithStatus.getDate() == null || dateWithStatus.getStatus() == null) {
            logger.info(AttendanceMessages.ATTENDANCE_NULL);
            return null;
        }

        Status status = dateWithStatus.getStatus();
        BetweenDate betweenDate = dateWithStatus.getDate();

        if (status == Status.LATE) {
            return this.manipulateAttendance.getAllAttendanceBetweenDate(
                betweenDate.getStartDate(),
                betweenDate.getEndDate(),
                Status.LATE
            );
        } else if (status == Status.ONTIME) {
            return this.manipulateAttendance.getAllAttendanceBetweenDate(
                betweenDate.getStartDate(),
                betweenDate.getEndDate(),
                Status.ONTIME
            );
        }

        return this.manipulateAttendance.getAllAttendanceBetweenDate(firstDayOfMonth, lastDayOfMonth,
            Status.LATE);
    }

    /**
     * Retrieves the attendance of students based on a specified time period and status.
     *
     * @param timeWithStatus an object that contains the time period and status to filter the attendance
     * @return the list of attendance records that match the specified time period and status
     */
    @GetMapping("/get/time")
    public Iterable<Attendance> getStudentsAttendanceByTime(@RequestBody TimeWithStatus timeWithStatus) {
        Status status = timeWithStatus.getStatus();
        String time = timeWithStatus.getTime();

        LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate lastDayOfMonth = today.withDayOfMonth(today.lengthOfMonth());

        LocalDate firstDayOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate lastDayOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        switch (time) {
            case "month":
                if (status == Status.LATE) {
                    return this.manipulateAttendance.getAllAttendanceBetweenDate(
                        firstDayOfMonth,
                        lastDayOfMonth,
                        Status.LATE
                    );
                } else if (status == Status.ONTIME) {
                    return this.manipulateAttendance.getAllAttendanceBetweenDate(
                        firstDayOfMonth,
                        lastDayOfMonth,
                        Status.ONTIME
                    );
                }
                break;

            case "week":
                if (status == Status.LATE) {
                    return this.manipulateAttendance.getAllAttendanceBetweenDate(
                        firstDayOfWeek,
                        lastDayOfWeek,
                        Status.LATE
                    );
                } else if (status == Status.ONTIME) {
                    return this.manipulateAttendance.getAllAttendanceBetweenDate(
                        firstDayOfWeek,
                        lastDayOfWeek,
                        Status.ONTIME
                    );
                }
                break;

            case "today":
                if (status == Status.LATE) {
                    return this.manipulateAttendance.getAllAttendanceBetweenDate(
                        LocalDate.now(),
                        LocalDate.now(),
                        Status.LATE
                    );
                } else if (status == Status.ONTIME) {
                    return this.manipulateAttendance.getAllAttendanceBetweenDate(
                        LocalDate.now(),
                        LocalDate.now(),
                        Status.ONTIME
                    );
                }
                break;

            default:
                return null;
        }

        return null;
    }

    /**
     * Retrieves the attendance count by date.
     *
     * @param dateWithStatus the DateWithStatus object containing the date and status
     * @return the count of attendance for the given date and status
     */
    @GetMapping("/count/date")
    public long getAttendanceCountByDate(@RequestBody DateWithStatus dateWithStatus) {
        if (dateWithStatus.getDate() == null || dateWithStatus.getStatus() == null) {
            logger.info(AttendanceMessages.ATTENDANCE_NULL);
            return -3;
        }

        Status status = dateWithStatus.getStatus();
        BetweenDate betweenDate = dateWithStatus.getDate();

        if (status == Status.LATE) {
            return this.manipulateAttendance.getAllCountOfAttendanceBetweenDate(
                betweenDate.getStartDate(),
                betweenDate.getEndDate(),
                Status.LATE
            );
        } else if (status == Status.ONTIME) {
            return this.manipulateAttendance.getAllCountOfAttendanceBetweenDate(
                betweenDate.getStartDate(),
                betweenDate.getEndDate(),
                Status.ONTIME
            );
        }

        return -1;
    }

    /**
     * Retrieves the attendance count based on the given time and status.
     *
     * @param timeWithStatus The time and status to filter the attendance count.
     * @return The count of attendance based on the given time and status.
     */
    @GetMapping("/count/time")
    public long getAttendanceCountByTime(@RequestBody TimeWithStatus timeWithStatus) {
        LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate lastDayOfMonth = today.withDayOfMonth(today.lengthOfMonth());

        LocalDate firstDayOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate lastDayOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        Status status = timeWithStatus.getStatus();
        String time = timeWithStatus.getTime();
        switch (time) {
            case "month" -> {
                if (status == Status.LATE) {
                    return this.manipulateAttendance.getAllCountOfAttendanceBetweenDate(
                        firstDayOfMonth,
                        lastDayOfMonth,
                        Status.LATE
                    );
                } else if (status == Status.ONTIME) {
                    return this.manipulateAttendance.getAllCountOfAttendanceBetweenDate(
                        firstDayOfMonth,
                        lastDayOfMonth,
                        Status.ONTIME
                    );
                }
            }

            case "week" -> {
                if (status == Status.LATE) {
                    return this.manipulateAttendance.getAllCountOfAttendanceBetweenDate(
                        firstDayOfWeek,
                        lastDayOfWeek,
                        Status.LATE
                    );
                } else if (status == Status.ONTIME) {
                    return this.manipulateAttendance.getAllCountOfAttendanceBetweenDate(
                        firstDayOfWeek,
                        lastDayOfWeek,
                        Status.ONTIME
                    );
                }
            }

            case "today" -> {
                if (status == Status.LATE) {
                    return this.manipulateAttendance.getAllCountOfAttendanceBetweenDate(
                        LocalDate.now(),
                        LocalDate.now(),
                        Status.LATE
                    );
                } else if (status == Status.ONTIME) {
                    return this.manipulateAttendance.getAllCountOfAttendanceBetweenDate(
                        LocalDate.now(),
                        LocalDate.now(),
                        Status.ONTIME
                    );
                }
            }

            default -> {
                return -1;
            }
        }

        return -2;
    }

    /**
     * Retrieves the count of students based on the given date and status.
     *
     * @param dateWithStatus the date and status for filtering the student count
     * @return the count of students based on the given date and status
     */
    @GetMapping("/student/date")
    public long getStudentCountByDate(@RequestBody DateWithStatusLrn dateWithStatus) {
        if (dateWithStatus.getDate() == null || dateWithStatus.getStatus() == null) {
            logger.info(AttendanceMessages.ATTENDANCE_NULL);
            return -2;
        }

        Status status = dateWithStatus.getStatus();
        BetweenDate betweenDate = dateWithStatus.getDate();
        Long studentLrn = dateWithStatus.getStudentLrn();

        if (status == Status.LATE) {
            return this.manipulateAttendance.getAllCountOfStudentAttendanceBetweenDate(
                studentLrn,
                betweenDate.getStartDate(),
                betweenDate.getEndDate(),
                Status.LATE
            );
        } else if (status == Status.ONTIME) {
            return this.manipulateAttendance.getAllCountOfStudentAttendanceBetweenDate(
                studentLrn,
                betweenDate.getStartDate(),
                betweenDate.getEndDate(),
                Status.ONTIME
            );
        }

        return -1;
    }

    /**
     * Retrieves the count of student attendance based on the specified time and status.
     *
     * @param timeWithStatus the time and status information for the attendance count
     * @return the count of student attendance based on the specified time and status,
     * or -1 if an error occurs or the specified time is invalid
     */
    @GetMapping("/student/time")
    public long getStudentCountByTime(@RequestBody TimeWithStatusLrn timeWithStatus) {
        Status status = timeWithStatus.getStatus();
        String time = timeWithStatus.getTime();
        Long studentLrn = timeWithStatus.getStudentLrn();

        LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate lastDayOfMonth = today.withDayOfMonth(today.lengthOfMonth());

        LocalDate firstDayOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate lastDayOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        switch (time) {
            case "month" -> {
                if (status == Status.LATE) {
                    return this.manipulateAttendance.getAllCountOfStudentAttendanceBetweenDate(
                        studentLrn,
                        firstDayOfMonth,
                        lastDayOfMonth,
                        Status.LATE
                    );
                } else if (status == Status.ONTIME) {
                    return this.manipulateAttendance.getAllCountOfStudentAttendanceBetweenDate(
                        studentLrn,
                        firstDayOfMonth,
                        lastDayOfMonth,
                        Status.ONTIME
                    );
                }
            }

            case "week" -> {
                if (status == Status.LATE) {
                    return this.manipulateAttendance.getAllCountOfStudentAttendanceBetweenDate(
                        studentLrn,
                        firstDayOfWeek,
                        lastDayOfWeek,
                        Status.LATE
                    );
                } else if (status == Status.ONTIME) {
                    return this.manipulateAttendance.getAllCountOfStudentAttendanceBetweenDate(
                        studentLrn,
                        firstDayOfWeek,
                        lastDayOfWeek,
                        Status.ONTIME
                    );
                }
            }

            case "today" -> {
                if (status == Status.LATE) {
                    return this.manipulateAttendance.getAllCountOfStudentAttendanceBetweenDate(
                        studentLrn,
                        LocalDate.now(),
                        LocalDate.now(),
                        Status.LATE
                    );
                } else if (status == Status.ONTIME) {
                    return this.manipulateAttendance.getAllCountOfStudentAttendanceBetweenDate(
                        studentLrn,
                        LocalDate.now(),
                        LocalDate.now(),
                        Status.ONTIME
                    );
                }
            }

            default -> {
                return -1;
            }
        }

        return -1;
    }

    /**
     * Retrieves the student attendance between two dates with a specific attendance status.
     *
     * @param dateWithStatusLrn the object containing the start and end dates, attendance status, and student LRN
     * @return an iterable collection of Attendance objects representing the student attendance
     */
    @GetMapping("/student/get/date")
    public Iterable<Attendance> getStudentAttendanceBetweenDate(@RequestBody DateWithStatusLrn dateWithStatusLrn) {
        if (dateWithStatusLrn.getDate() == null || dateWithStatusLrn.getStatus() == null) {
            logger.info(AttendanceMessages.ATTENDANCE_NULL);
            return null;
        }

        Status status = dateWithStatusLrn.getStatus();
        BetweenDate betweenDate = dateWithStatusLrn.getDate();
        Long studentLrn = dateWithStatusLrn.getStudentLrn();

        if (status == Status.LATE) {
            return this.manipulateAttendance.getStudentAttendanceBetweenDateWithAttendanceStatus(
                studentLrn,
                betweenDate.getStartDate(),
                betweenDate.getEndDate(),
                Status.LATE
            );
        } else if (status == Status.ONTIME) {
            return this.manipulateAttendance.getAllAttendanceBetweenDate(
                betweenDate.getStartDate(),
                betweenDate.getEndDate(),
                Status.ONTIME
            );
        }

        return null;
    }

    @GetMapping("/student/get/time")
    public Iterable<Attendance> getStudentAttendanceByTime(@RequestBody TimeWithStatusLrn timeWithStatus) {
        Status status = timeWithStatus.getStatus();
        String time = timeWithStatus.getTime();
        Long studentLrn = timeWithStatus.getStudentLrn();

        LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate lastDayOfMonth = today.withDayOfMonth(today.lengthOfMonth());

        LocalDate firstDayOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate lastDayOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        switch (time) {
            case "month" -> {
                if (status == Status.LATE) {
                    return this.manipulateAttendance.getStudentAttendanceBetweenDateWithAttendanceStatus(
                        studentLrn,
                        firstDayOfMonth,
                        lastDayOfMonth,
                        Status.LATE
                    );
                } else if (status == Status.ONTIME) {
                    return this.manipulateAttendance.getStudentAttendanceBetweenDateWithAttendanceStatus(
                        studentLrn,
                        firstDayOfMonth,
                        lastDayOfMonth,
                        Status.ONTIME
                    );
                }
            }

            case "week" -> {
                if (status == Status.LATE) {
                    return this.manipulateAttendance.getStudentAttendanceBetweenDateWithAttendanceStatus(
                        studentLrn,
                        firstDayOfWeek,
                        lastDayOfWeek,
                        Status.LATE
                    );
                } else if (status == Status.ONTIME) {
                    return this.manipulateAttendance.getStudentAttendanceBetweenDateWithAttendanceStatus(
                        studentLrn,
                        firstDayOfWeek,
                        lastDayOfWeek,
                        Status.ONTIME
                    );
                }
            }

            case "today" -> {
                if (status == Status.LATE) {
                    return this.manipulateAttendance.getStudentAttendanceBetweenDateWithAttendanceStatus(
                        studentLrn,
                        LocalDate.now(),
                        LocalDate.now(),
                        Status.LATE
                    );
                } else if (status == Status.ONTIME) {
                    return this.manipulateAttendance.getStudentAttendanceBetweenDateWithAttendanceStatus(
                        studentLrn,
                        LocalDate.now(),
                        LocalDate.now(),
                        Status.ONTIME
                    );
                }
            }

            default -> {
                return null;
            }
        }

        return null;
    }
}

