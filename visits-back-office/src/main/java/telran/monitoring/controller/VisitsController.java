package telran.monitoring.controller;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import telran.monitoring.model.*;
import telran.monitoring.service.VisitsService;


import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value="/visits")
@Validated
public class VisitsController {
	static final String ADD_PATIENT = "/patients";
	static final String ADD_DOCTOR = "/doctors";
	private static final String ISO_DATE = "\\d{4}-\\d{2}-\\d{2}";
	static Logger LOG = LoggerFactory.getLogger(VisitsController.class);
	@Autowired
	VisitsService visits;
	@PostMapping(value=ADD_PATIENT)
	PatientDto addPatient(@RequestBody @Valid PatientDto patient) {
		LOG.debug("received patient {} ", patient);
		visits.addPatient(patient);
		return patient;
	}
	@PostMapping(value=ADD_DOCTOR)
	DoctorDto addDoctor(@RequestBody @Valid DoctorDto doctor) {
		LOG.debug("received doctor {} ", doctor);
		visits.addDoctor(doctor);
		return doctor;
	}
	@PostMapping
	VisitDto addVisit(@RequestBody @Valid VisitDto visit) {
		LOG.debug("received visit {} ", visit);
		visits.addVisit(visit);
		return visit;
	}
	@GetMapping("/{patientId}")
	List<VisitDto> getVisits(@PathVariable long patientId,
			@RequestParam(required = false, name="from")
	@Pattern(regexp = ISO_DATE, message="wrong date format 'from'")String fromDate,
			@RequestParam(required = false, name="to")
	@Pattern(regexp = ISO_DATE, message="wrong date format 'to'")String toDate) {
		if (fromDate == null && toDate == null) {
			return visits.getAllVisits(patientId);
		}
		if (fromDate == null) {
			fromDate = "1111-01-01";
		}
		if (toDate == null) {
			toDate = "9999-11-11";
		}
		return visits.getVisitsDates(patientId, LocalDate.parse(fromDate), LocalDate.parse(toDate));
		
	}

}
