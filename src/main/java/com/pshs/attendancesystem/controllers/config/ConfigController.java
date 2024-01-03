package com.pshs.attendancesystem.controllers.config;

import com.pshs.attendancesystem.impl.ConfigurationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;

@RestController
@RequestMapping("${api.root}/config")
@Tag(name = "Config", description = "Config API")
@SecurityRequirement(
	name = "JWT Authentication"
)
public class ConfigController {

	private final ConfigurationService configurationService;

	public ConfigController(ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}

	@GetMapping(value = "/set/flag-ceremony", produces = "text/plain")
	@Operation(
		summary = "Set Flag Ceremony Time",
		description = "Set Flag Ceremony Time",
		parameters = {
			@Parameter(name = "time", description = "The time of the flag ceremony", schema = @Schema(name = "time", type = "string", example = "06:30:00"))
		}
	)
	public String setFlagCeremonyTime(@RequestParam LocalTime time) {
		return configurationService.updateFlagCeremonyTime(time);
	}

	@Operation(
		summary = "Set Late Arrival Time",
		description = "Set Late Arrival Time",
		parameters = {
			@Parameter(name = "time", description = "The time of the late arrival", schema = @Schema(name = "time", type = "string", example = "07:00:00"))
		}
	)
	@GetMapping(value = "/set/late-time", produces = "text/plain")
	public String setLateTimeArrival(@RequestParam LocalTime time) {
		return configurationService.updateLateTimeArrival(time);
	}

	@Operation(
		summary = "Set On-Time Arrival Time",
		description = "Set On-Time Arrival Time",
		parameters = {
			@Parameter(name = "time", description = "The time of the on-time arrival", schema = @Schema(name = "time", type = "string", example = "05:00:00"))
		}
	)
	@GetMapping(value = "/set/on-time-arrival", produces = "text/plain")
	public String setOnTimeArrival(@RequestParam LocalTime time) {
		return configurationService.updateOnTimeArrival(time);
	}

	@GetMapping(value = "/set/absent-schedule", produces = "text/plain")
	@Operation(
		summary = "Set Absent Schedule",
		description = "Set Absent Schedule. Uses cron format. E.g: '0 18 * * * *'; Runs every 6 PM." +
			"<br>Note: It uses six asterisk (*), the last one being the year.",
		parameters = {
			@Parameter(name = "time", description = "The time of the absent schedule", schema = @Schema(name = "time", type = "string", example = "0 18 * * * *"))
		}
	)
	public String setAbsentSchedule(@RequestParam String time) {
		return configurationService.updateAbsentSchedule(time);
	}

	@GetMapping(value = "/get/late-time")
	public LocalTime getLateTimeArrival() {
		return configurationService.getLateTimeArrival();
	}

	@GetMapping(value = "/get/flag-ceremony-time")
	public LocalTime getFlagCeremonyTime() {
		return configurationService.getFlagCeremonyTime();
	}

	@GetMapping(value = "/get/on-time-arrival")
	public LocalTime getOnTimeArrival() {
		return configurationService.getOnTimeArrival();
	}

	@GetMapping(value = "/get/absent-schedule", produces = "text/plain")
	public String getAbsentSchedule() {
		return configurationService.getAbsentSchedule();
	}
}
