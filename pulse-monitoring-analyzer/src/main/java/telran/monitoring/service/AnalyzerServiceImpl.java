package telran.monitoring.service;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import telran.monitoring.entities.LastProbe;
import telran.monitoring.model.PulseJump;
import telran.monitoring.model.PulseProbe;
import telran.monitoring.repo.LastProbeRepository;
@Service
public class AnalyzerServiceImpl implements AnalyzerService {
	static Logger LOG = LoggerFactory.getLogger(AnalyzerService.class);
	@Autowired
LastProbeRepository lastProbeRepository;
	@Value("${app.jump.threshold:0.3}")
	double jumpThreshold;
	@Override
	@Transactional
	public PulseJump processPulseProbe(PulseProbe probe) {
		PulseJump res = null;
		
		LastProbe lastProbe =
				lastProbeRepository.findById(probe.patientId).orElse(null);
		if (lastProbe == null) {
			LOG.debug("for patient {} no saved values", probe.patientId);
			lastProbe = new LastProbe(probe.patientId, probe.value);
			
		} else if (isJump(lastProbe.getValue(), probe.value)){
			
			res = new PulseJump(probe.patientId, lastProbe.getValue(), probe.value);
			LOG.debug("jump: patientId is {} previous value is {}, current value is {}",probe.patientId,
					lastProbe.getValue(), probe.value);
		}
		LOG.trace("lastProbe value for patient {} last value {}", lastProbe.getPatientId(), lastProbe.getValue());
		lastProbe.setValue(probe.value);
		lastProbeRepository.save(lastProbe);
		return res;
		
		
	}
	private boolean isJump(int lastValue, int currentValue) {
		int delta = Math.abs(currentValue - lastValue);
		return delta >= lastValue * jumpThreshold;
	}

}
