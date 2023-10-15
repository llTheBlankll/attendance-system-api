package com.pshs.attendancesystem.repositories;

import com.pshs.attendancesystem.entities.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.pshs.attendancesystem.Enums;
import java.util.Date;

public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {
    @Query(value = "SELECT nextval('attendance_id_seq') AS next_series_id;", nativeQuery =
            true)
    Integer getNextSeriesId();

    Iterable<Attendance> findByDateAfterAndDateBeforeAndAttendanceStatus(Date startDate, Date endDate, Enums.status status);
}