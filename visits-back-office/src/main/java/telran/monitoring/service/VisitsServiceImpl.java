package telran.monitoring.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import telran.monitoring.model.*;
import telran.exceptions.NotFoundException;
import telran.monitoring.entities.*;
import telran.monitoring.repo.*;
@Service
public class VisitsServiceImpl implements VisitsService {
	DoctorRepository doctorRepository;
	PatientRepository patientRepository;
	VisitRepository visitRepository;
	

	public VisitsServiceImpl(DoctorRepository doctorRepository, PatientRepository patientRepository,
			VisitRepository visitRepository) {
		this.doctorRepository = doctorRepository;
		this.patientRepository = patientRepository;
		this.visitRepository = visitRepository;
	}

	@Override
	@Transactional
	public void addPatient(PatientDto patientDto) {
		if(patientRepository.existsById(patientDto.id)) {
			throw new IllegalStateException(String.format("patient with id %d already exists", patientDto.id));
		}
		Patient patient = new Patient(patientDto.id, patientDto.name);
		patientRepository.save(patient);
		

	}

	@Override
	@Transactional
	public void addDoctor(DoctorDto doctorDto) {
		if(doctorRepository.existsById(doctorDto.email)) {
			throw new IllegalStateException(String.format("doctor with email %s already exists", doctorDto.email));
		}
		Doctor doctor = new Doctor(doctorDto.email, doctorDto.name);
		doctorRepository.save(doctor);

	}

	@Override
	@Transactional
	public void addVisit(VisitDto visitDto) {
		LocalDate date = LocalDate.parse(visitDto.date);
		Doctor doctor = doctorRepository.findById(visitDto.doctorEmail).orElse(null);
		if (doctor == null) {
			throw new NotFoundException(String.format("doctor %s not found", visitDto.doctorEmail));
		}
		Patient patient = patientRepository.findById(visitDto.patientId).orElse(null);
		if (patient == null) {
			throw new NotFoundException(String.format("patient %d not found", visitDto.patientId));
		}
		Visit visit = new Visit(date , doctor , patient );
		visitRepository.save(visit );

	}

	@Override
	public List<VisitDto> getAllVisits(long patientId) {
		
		return toListVisitDto(visitRepository.findByPatientId(patientId));
	}
	private List<VisitDto> toListVisitDto(List<Visit> listVisits) {
		return listVisits.stream().map(this::toVisitDto).toList();
	}
	private VisitDto toVisitDto(Visit visit) {
		return new VisitDto(visit.getPatient().getId(), visit.getDoctor().getEmail(), visit.getDate().toString());
	}

	@Override
	public List<VisitDto> getVisitsDates(long patientId, LocalDate from, LocalDate to) {
		
		return toListVisitDto(visitRepository.findByDateBetweenAndPatientId( from, to, patientId));
	}

}
