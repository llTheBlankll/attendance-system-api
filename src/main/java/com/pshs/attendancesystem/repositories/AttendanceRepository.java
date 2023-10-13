package com.pshs.attendancesystem.repositories;

import com.pshs.attendancesystem.entities.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {
}