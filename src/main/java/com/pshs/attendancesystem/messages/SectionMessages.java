package com.pshs.attendancesystem.messages;

public class SectionMessages {

	public static final String SECTION_NOT_FOUND = "Section does not exists.";
	public static final String SECTION_EXISTS = "Section already exists.";
	public static final String SECTION_CREATED = "Section was created.";
	public static final String SECTION_UPDATED = "Section was updated.";
	public static final String SECTION_NULL = "Section ID Cannot be null";

	private SectionMessages() {

	}

	public static String SECTION_DELETED(Integer sectionId) {
		return "Section with ID " + sectionId + " was deleted.";
	}

}
