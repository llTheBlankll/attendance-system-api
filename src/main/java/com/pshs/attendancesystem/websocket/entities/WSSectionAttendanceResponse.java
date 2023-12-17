package com.pshs.attendancesystem.websocket.entities;

import com.pshs.attendancesystem.entities.Section;

public class WSSectionAttendanceResponse {

	private Section section;
	private Long absent;
	private Long present;
	private Long out;
	private Long late;
	private Long onTime;

	public Long getOnTime() {
		return onTime;
	}

	public void setOnTime(Long onTime) {
		this.onTime = onTime;
	}

	public Section getSection() {
		return section;
	}

	public Long getOut() {
		return out;
	}

	public void setOut(Long out) {
		this.out = out;
	}

	public Long getLate() {
		return late;
	}

	public void setLate(Long late) {
		this.late = late;
	}

	public void setSection(Section section) {
		this.section = section;
	}

	public Long getAbsent() {
		return absent;
	}

	public void setAbsent(Long absent) {
		this.absent = absent;
	}

	public Long getPresent() {
		return present;
	}

	public void setPresent(Long present) {
		this.present = present;
	}
}
