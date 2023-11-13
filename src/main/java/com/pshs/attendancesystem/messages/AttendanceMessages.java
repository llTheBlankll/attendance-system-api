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
        return "Dear Parent, your child has safely arrived at our facility and is not late. Time: " + time;
    }

    public static String onLateAttendanceMessage(String studentName, String time) {
        // * This message is for the parents.
        return "Dear Parent,\n" +
            "\n" +
            "Regrettably, your child is currently running behind schedule." +
            "\nTime: " + time + "\nStudent Name: " + studentName;
    }

    public static String StudentAttendedMessage(String studentName, String time) {
        // * This message is for the parents.
        return "Dear Parent, your child has safely arrived at our facility. Time: " + time;
    }
}
