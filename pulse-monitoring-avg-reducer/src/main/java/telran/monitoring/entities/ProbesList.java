package telran.monitoring.entities;

import java.util.*;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash
public class ProbesList {
	@Id
	long patientId;
	List<Integer> pulseValues = new ArrayList<>();

	public ProbesList(long patientId) {
		this.patientId = patientId;
	}

	public ProbesList() {

	}

	public long getPatientId() {
		return patientId;
	}

	public List<Integer> getPulseValues() {
		return pulseValues;
	}

}
