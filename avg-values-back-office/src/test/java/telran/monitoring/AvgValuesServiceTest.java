package telran.monitoring;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import telran.monitoring.entities.AvgPulseDoc;
import telran.monitoring.model.PulseProbe;
import telran.monitoring.repo.AvgPulseRepository;
import telran.monitoring.service.AvgPulseValuesService;

@SpringBootTest
public class AvgValuesServiceTest {
	private static final long PATIENT_ID = 123;
	private static final int VALUE1 = 60;
	private static final int VALUE2 = 80;
	private static final int VALUE3 = 100;
	private static final int AVG_PULSE_VALUE = 80;
	private static final long PATIENT_ID_1 = 124;
	private static final int VALUE4 = 150;
	@Autowired
AvgPulseRepository avgPulseRepo;
	@Autowired
	AvgPulseValuesService avgValuesService;
	PulseProbe probe1 = new PulseProbe(PATIENT_ID, System.currentTimeMillis(), 0, VALUE1);
	PulseProbe probe2 = new PulseProbe(PATIENT_ID, System.currentTimeMillis(), 0, VALUE2);
	PulseProbe probe3 = new PulseProbe(PATIENT_ID, System.currentTimeMillis(), 0, VALUE3);
	PulseProbe probe4 = new PulseProbe(PATIENT_ID_1, System.currentTimeMillis(), 0, VALUE4);
	@BeforeEach
	void addValues() {
		avgPulseRepo.save(AvgPulseDoc.of(probe1));
		avgPulseRepo.save(AvgPulseDoc.of(probe2));
		avgPulseRepo.save(AvgPulseDoc.of(probe3));
		avgPulseRepo.save(AvgPulseDoc.of(probe4));
	}
	@Test
	void test() {
		assertEquals(AVG_PULSE_VALUE, avgValuesService.getAvgPulseValue(PATIENT_ID));
		assertEquals(0, avgValuesService.getAvgPulseValueDates(PATIENT_ID,
				LocalDateTime.of(2020, 1, 1, 1, 1),
				LocalDateTime.of(2021, 1, 1, 1, 1)));
		assertEquals(VALUE4, avgValuesService.getAvgPulseValue(PATIENT_ID_1));
	}
}
