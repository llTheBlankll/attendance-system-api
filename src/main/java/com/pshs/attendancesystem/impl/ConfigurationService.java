package com.pshs.attendancesystem.impl;

import com.pshs.attendancesystem.config.APIConfig;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.LocalTime;

@Service
public class ConfigurationService {
	private final APIConfig apiConfig;
	public ConfigurationService(APIConfig apiConfig) {
		this.apiConfig = apiConfig;
	}

	public int getRedisPort() {
		return apiConfig.getRedisPort();
	}

	public String getSecurityRequirement() {
		return apiConfig.getSecurityRequirement();
	}

	public String getRedisUsername() {
		return apiConfig.getRedisUsername();
	}

	public String getRedisPassword() {
		return apiConfig.getRedisPassword();
	}

	public String getRedisHost() {
		return apiConfig.getRedisHost();
	}

	public String updateFlagCeremonyTime(LocalTime flagCeremonyTime) {
		LocalTime previousTime = apiConfig.getFlagCeremonyTime();
		apiConfig.setFlagCeremonyTime(Time.valueOf(flagCeremonyTime).toLocalTime());
		return String.format("From %s to %s", previousTime, apiConfig.getFlagCeremonyTime());
	}

	public String updateLateTimeArrival(LocalTime lateTimeArrival) {
		LocalTime previousTime = apiConfig.getLateTimeArrival();
		apiConfig.setLateTimeArrival(Time.valueOf(lateTimeArrival).toLocalTime());
		return String.format("From %s to %s", previousTime, apiConfig.getLateTimeArrival());
	}

	public String updateOnTimeArrival(LocalTime onTimeArrival) {
		LocalTime previousTime = apiConfig.getOnTimeArrival();
		apiConfig.setOnTimeArrival(Time.valueOf(onTimeArrival).toLocalTime());
		return String.format("From %s to %s", previousTime, apiConfig.getOnTimeArrival());
	}

	public String updateAbsentSchedule(String absentSchedule) {
		String previousSchedule = apiConfig.getAbsentSchedule();
		apiConfig.setAbsentSchedule(absentSchedule);
		return String.format("From %s to %s", previousSchedule, apiConfig.getAbsentSchedule());
	}

	public LocalTime getFlagCeremonyTime() {
		return apiConfig.getFlagCeremonyTime();
	}

	public String getAPIVersion() {
		return apiConfig.getAPI_VERSION();
	}

	public void setAPI_VERSION(String API_VERSION) {
		apiConfig.setAPI_VERSION(API_VERSION);
	}

	public LocalTime getLateTimeArrival() {
		return apiConfig.getLateTimeArrival();
	}

	public LocalTime getOnTimeArrival() {
		return apiConfig.getOnTimeArrival();
	}

	public String getAbsentSchedule() {
		return apiConfig.getAbsentSchedule();
	}

	public String getSPRING_DOCS_TITLE() {
		return apiConfig.getSPRING_DOCS_TITLE();
	}

	public String getSPRING_DOCS_DESCRIPTION() {
		return apiConfig.getSPRING_DOCS_DESCRIPTION();
	}
}
