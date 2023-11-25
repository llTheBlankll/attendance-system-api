package com.pshs.attendancesystem.documentation;

import com.pshs.attendancesystem.messages.TeacherMessages;

public class TeacherDocumentation {
	private TeacherDocumentation() {
	}
	private static final String LINEBREAK = "<br>";

	public static final String GET_ALL_TEACHERS = "Get all teachers in the database";
	public static final String GET_TEACHERS_BY_LAST_NAME = "Get Teacher by Last Name in the database, provide the Last Name";
	public static final String CREATE_TEACHER = "Create Teacher in the database, provide the Teacher object" +
		"Returns string <b>true</b> if Teacher is created successfully";
	public static final String UPDATE_TEACHER = "Update Teacher in the database, provide the Teacher object" +
		LINEBREAK + "Returns string <b>" + TeacherMessages.TEACHER_UPDATED + "</b> if Teacher is updated successfully" +
		LINEBREAK + "Returns string <b>" + TeacherMessages.TEACHER_NOT_FOUND + "</b> if Teacher is not updated successfully";
	public static final String DELETE_TEACHER = "Delete Teacher in the database, provide the Teacher object" +
		LINEBREAK + "Returns string <b>" + TeacherMessages.TEACHER_DELETED + "</b> if Teacher is deleted successfully" +
		LINEBREAK + "Returns string <b>" + TeacherMessages.TEACHER_NOT_FOUND + "</b> if Teacher is not deleted successfully";
	public static final String GET_TEACHER_BY_ID = "Get Teacher by ID in the database, provide the ID";
}
