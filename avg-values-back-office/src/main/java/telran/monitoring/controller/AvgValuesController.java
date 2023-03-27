package telran.monitoring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import telran.monitoring.service.AvgPulseValuesService;


import java.time.LocalDateTime;

@RestController
@RequestMapping(value="/pulse/values")
public class AvgValuesController {
	@Autowired
	AvgPulseValuesService service;
	@GetMapping("{id}")
	Integer getAvgValue(@PathVariable ("id") long patientId,
			@RequestParam (name = "from", required = false) String fromDateTime,
			@RequestParam(name = "to", required=false) String toDateTime) {
		
		if (fromDateTime == null && toDateTime == null) {
			return service.getAvgPulseValue(patientId);
		}
		LocalDateTime from = fromDateTime == null ? LocalDateTime.of(1000, 1, 1, 0, 0) : LocalDateTime.parse(fromDateTime);
		LocalDateTime to = toDateTime == null ? LocalDateTime.of(10000, 1, 1, 0, 0) : LocalDateTime.parse(toDateTime);
		
		return service.getAvgPulseValueDates(patientId, from, to);
		
		
	}
}
