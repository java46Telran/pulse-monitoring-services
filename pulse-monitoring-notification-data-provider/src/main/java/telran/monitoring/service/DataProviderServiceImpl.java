package telran.monitoring.service;

import org.slf4j.*;
import org.springframework.stereotype.Service;

import telran.exceptions.NotFoundException;
import telran.monitoring.entities.*;
import telran.monitoring.model.NotificationData;
import telran.monitoring.repo.*;
@Service
public class DataProviderServiceImpl implements DataProviderService {
	static Logger LOG = LoggerFactory.getLogger(DataProviderService.class);
	DoctorRepository doctorRepository;
	PatientRepository patientRepository;
	VisitRepository visitRepository;
	@Override
	public NotificationData getNotificationData(long patientId) {
		String doctorEmail = visitRepository.getDoctorEmail(patientId);
		LOG.debug("doctor email is {}", doctorEmail);
		if(doctorEmail == null || doctorEmail.isEmpty()) {
			throw new NotFoundException("no vists for patient " + patientId);
		}
		Doctor doctor = doctorRepository.findById(doctorEmail).orElse(null);
		if(doctor == null) {
			throw new NotFoundException("no doctor with email " + doctorEmail);
		}
		String doctorName = doctor.getName();
		Patient patient = patientRepository.findById(patientId).orElse(null);
		if(patient == null) {
			throw new NotFoundException("no  patient " + patientId);
		}
		String patientName = patient.getName();
		return new NotificationData(doctorEmail, doctorName, patientName);
	}
	public DataProviderServiceImpl(DoctorRepository doctorRepository, PatientRepository patientRepository,
			VisitRepository visitRepository) {
		this.doctorRepository = doctorRepository;
		this.patientRepository = patientRepository;
		this.visitRepository = visitRepository;
	}

}
