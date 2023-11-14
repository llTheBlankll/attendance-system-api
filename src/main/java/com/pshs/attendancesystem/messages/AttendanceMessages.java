package com.pshs.attendancesystem.messages;

public class AttendanceMessages {

    public static final String ATTENDANCE_LATE = "You are late.";

    public static final String ATTENDANCE_NOT_FOUND = "Attendance does not exist.";
    public static final String ATTENDANCE_UPDATED = "Attendance was updated.";
    public static final String ATTENDANCE_DELETED = "Attendance was deleted.";
    public static final String ATTENDANCE_ONTIME = "You are on time.";
    public static final String ATTENDANCE_EARLY = "You are early.";
    public static final String ATTENDANCE_NULL = "One of the field required is null or empty.";

    private AttendanceMessages() {
    }

    public static String onTimeAttendanceMessage(String studentName, String time) {
        // * This message is for the parents.
        return studentName + " is on time. Time: " + time;
    }

    public static String onLateAttendanceMessage(String studentName, String time) {
        // * This message is for the parents.
        return studentName + " is late today. Time: " + time;
    }

    public static String studentOutOfFacility(String studentName, String time) {
        return  studentName + " has been safely checked out of the facility. Time: " + time;
    }

    public static String StudentAttendedMessage(String studentName, String time) {
        // * This message is for the parents.
        return "Your child " + studentName + " has safely arrived at our facility. Time: " + time;
    }
}
