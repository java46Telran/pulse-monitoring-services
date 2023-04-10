package telran.monitoring.controller;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import telran.monitoring.model.*;
import telran.monitoring.service.VisitsService;


import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value="/visits")
public class VisitsController {
	static final String ADD_PATIENT = "/patients";
	static final String ADD_DOCTOR = "/doctors";
	static Logger LOG = LoggerFactory.getLogger(VisitsController.class);
	@Autowired
	VisitsService visits;
	@PostMapping(value=ADD_PATIENT)
	PatientDto addPatient(@RequestBody PatientDto patient) {
		LOG.debug("received patient {} ", patient);
		visits.addPatient(patient);
		return patient;
	}
	@PostMapping(value=ADD_DOCTOR)
	DoctorDto addDoctor(@RequestBody DoctorDto doctor) {
		LOG.debug("received doctor {} ", doctor);
		visits.addDoctor(doctor);
		return doctor;
	}
	@PostMapping
	VisitDto addVisit(@RequestBody VisitDto visit) {
		LOG.debug("received visit {} ", visit);
		visits.addVisit(visit);
		return visit;
	}
	@GetMapping("/{patientId}")
	List<VisitDto> getVisits(@PathVariable long patientId, @RequestParam(required = false, name="from") String fromDate,
			@RequestParam(required = false, name="to") String toDate) {
		if (fromDate == null && toDate == null) {
			return visits.getAllVisits(patientId);
		}
		if (fromDate == null) {
			fromDate = "1000-01-01";
		}
		if (toDate == null) {
			toDate = "10000-11-11";
		}
		return visits.getVisitsDates(patientId, LocalDate.parse(fromDate), LocalDate.parse(toDate));
		
	}

}
