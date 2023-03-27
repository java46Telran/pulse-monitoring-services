package telran.monitoring.entities;

import java.time.*;
import java.util.Objects;

import org.springframework.data.mongodb.core.mapping.Document;

import telran.monitoring.model.PulseProbe;

@Document(collection="documents")
public class AvgPulseDoc {
long patientId;
LocalDateTime dateTime;
int value;
public static final String PATIENT_ID = "patientId";
public static final String DATE_TIME = "dateTime";
public static final String PULSE_VALUE = "value";
private AvgPulseDoc(long patientId, LocalDateTime dateTime, int value) {
	this.patientId = patientId;
	this.dateTime = dateTime;
	this.value = value;
}
public static AvgPulseDoc of(PulseProbe pulseProbe) {
	return new AvgPulseDoc(pulseProbe.patientId,
			LocalDateTime.ofInstant(Instant.ofEpochMilli(pulseProbe.timestamp),
					ZoneId.systemDefault()), pulseProbe.value);
}
@Override
public int hashCode() {
	return Objects.hash(patientId, value);
}
@Override
public boolean equals(Object obj) {
	if (this == obj)
		return true;
	if (obj == null)
		return false;
	if (getClass() != obj.getClass())
		return false;
	AvgPulseDoc other = (AvgPulseDoc) obj;
	return patientId == other.patientId && value == other.value;
}
public long getPatientId() {
	return patientId;
}
public LocalDateTime getDateTime() {
	return dateTime;
}
public int getValue() {
	return value;
}

}
