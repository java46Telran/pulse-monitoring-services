package telran.monitoring.service;

import java.time.LocalDateTime;

public interface AvgPulseValuesService {
int getAvgPulseValue(long patientId);
int getAvgPulseValueDates(long patientId, LocalDateTime from, LocalDateTime to);
}
