package com.pshs.attendancesystem.messages;

public class StrandMessages {

	public static final String STRAND_NOT_FOUND = "Strand does not exist.";
	public static final String STRAND_EXISTS = "Strand already exists.";
	public static final String STRAND_UPDATED = "Strand was updated.";
	public static final String STRAND_DELETED = "Strand was deleted.";
	public static final String STRAND_NULL = "Strand ID Cannot be null.";
	public static final String STRAND_NO_NAME = "Strand Name Cannot be empty.";

	private StrandMessages() {
	}

	public static String STRAND_CREATED(String strandName) {
		return "Strand " + strandName + " was created.";
	}
}
