package telran.monitoring;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;

import telran.monitoring.model.*;
import telran.monitoring.entities.LastProbe;
import telran.monitoring.repo.LastProbeRepository;
import telran.monitoring.service.AnalyzerService;
@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
class AnalyzerServiceTest {
	 static final Long PATIENT_NO_REDIS_DATA = 123L;
	private static final Long PATIENT_NO_JUMP = 125L;
	private static final int VALUE = 100;
	private static final Long PATIENT_JUMP = 127L;
	private static final int JUMP_VALUE = 300;
	@Autowired
	AnalyzerService service;
	@MockBean
	LastProbeRepository probesRepository;
	LastProbe probeNoJump = new LastProbe(PATIENT_NO_JUMP, VALUE);
	LastProbe probeJump = new LastProbe(PATIENT_JUMP, VALUE);
	PulseJump jump = new PulseJump(PATIENT_JUMP, VALUE, JUMP_VALUE);
	@BeforeEach
	void redisMocking() {
		when(probesRepository.findById(PATIENT_NO_REDIS_DATA)).thenReturn(Optional.ofNullable(null));
		when(probesRepository.findById(PATIENT_NO_JUMP)).thenReturn(Optional.of(probeNoJump));
		when(probesRepository.findById(PATIENT_JUMP)).thenReturn(Optional.of(probeJump));
	}

	@Test
	void noRedisDataTest() {
		assertNull(service.processPulseProbe(new PulseProbe(PATIENT_NO_REDIS_DATA,0,0, VALUE)));
	}
	@Test
	void  noJumpTest() {
		assertNull(service.processPulseProbe(new PulseProbe(PATIENT_NO_JUMP,0,0, VALUE)));
	}
	@Test
	void  jumpTest() {
		assertEquals(this.jump, service.processPulseProbe(new PulseProbe(PATIENT_JUMP,0,0, JUMP_VALUE)));
	}

}
