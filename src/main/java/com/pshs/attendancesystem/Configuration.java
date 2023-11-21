package com.pshs.attendancesystem;

import java.sql.Time;
import java.time.LocalTime;

public class Configuration {

    private Configuration() {
    }

    public static class Attendance {
        public static final LocalTime flagCeremonyTime = Time.valueOf("06:30:00").toLocalTime();
        public static final LocalTime lateTimeArrival = Time.valueOf("07:00:00").toLocalTime();
        public static final LocalTime onTimeArrival = Time.valueOf("05:00:00").toLocalTime();
        private Attendance() {
        }
    }

    public static class Pagination {
        public static final int DEFAULT_PAGE_SIZE = 10;

        private Pagination() {
        }
    }

}
