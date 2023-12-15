package com.pshs.attendancesystem.dto;

import com.pshs.attendancesystem.entities.Section;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

/**
 * DTO for {@link com.pshs.attendancesystem.entities.Strand}
 */
public class StrandDto implements Serializable {
	private final Integer id;
	private final String strandName;
	private final Set<Section> sections;

	public StrandDto(Integer id, String strandName, Set<Section> sections) {
		this.id = id;
		this.strandName = strandName;
		this.sections = sections;
	}

	public Integer getId() {
		return id;
	}

	public String getStrandName() {
		return strandName;
	}

	public Set<Section> getSections() {
		return sections;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		StrandDto entity = (StrandDto) o;
		return Objects.equals(this.id, entity.id) &&
			Objects.equals(this.strandName, entity.strandName) &&
			Objects.equals(this.sections, entity.sections);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, strandName, sections);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "(" +
			"id = " + id + ", " +
			"strandName = " + strandName + ", " +
			"sections = " + sections + ")";
	}
}