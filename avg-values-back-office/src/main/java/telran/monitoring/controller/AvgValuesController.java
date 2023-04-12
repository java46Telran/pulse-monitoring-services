package telran.monitoring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.Pattern;
import telran.monitoring.service.AvgPulseValuesService;


import java.time.LocalDateTime;

@RestController
@RequestMapping(value="/pulse/values")
@Validated
public class AvgValuesController {
	private static final String ISO_DATE_TIME = "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}";
	@Autowired
	AvgPulseValuesService service;
	@GetMapping("{id}")
	Integer getAvgValue(@PathVariable ("id") long patientId,
			@RequestParam (name = "from", required = false)
	@Pattern(regexp = ISO_DATE_TIME, message="wrong date/time format 'from'")String fromDateTime,
			@RequestParam(name = "to", required=false)
	@Pattern(regexp = ISO_DATE_TIME, message="wrong date/time format 'to'")String toDateTime) {
		
		if (fromDateTime == null && toDateTime == null) {
			return service.getAvgPulseValue(patientId);
		}
		LocalDateTime from = fromDateTime == null ? LocalDateTime.of(1000, 1, 1, 0, 0) : LocalDateTime.parse(fromDateTime);
		LocalDateTime to = toDateTime == null ? LocalDateTime.of(10000, 1, 1, 0, 0) : LocalDateTime.parse(toDateTime);
		
		return service.getAvgPulseValueDates(patientId, from, to);
		
		
	}
}
