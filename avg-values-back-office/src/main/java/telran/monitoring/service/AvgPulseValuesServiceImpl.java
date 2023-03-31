package telran.monitoring.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import jakarta.annotation.PostConstruct;

import org.bson.Document;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import static telran.monitoring.entities.AvgPulseDoc.*;

import telran.monitoring.entities.AvgPulseDoc;
import telran.monitoring.repo.AvgPulseRepository;

@Service
public class AvgPulseValuesServiceImpl implements AvgPulseValuesService{
	static Logger LOG = LoggerFactory.getLogger(AvgPulseValuesService.class);
	private static final String AVG_VALUE = "avgValue";
	
	@Autowired
	MongoTemplate mongoTemplate;

	@Override
	public int getAvgPulseValue(long patientId) {
		MatchOperation matchOperation = match(Criteria.where(PATIENT_ID).is(patientId));
		
		return avgValueRequest(matchOperation);
	}

	private int avgValueRequest(MatchOperation matchOperation) {
		
		GroupOperation groupOperation = group().avg(PULSE_VALUE).as(AVG_VALUE);
		Aggregation pipeline = newAggregation(matchOperation, groupOperation);
		Document document = mongoTemplate.aggregate(pipeline, AvgPulseDoc.class, Document.class)
				.getUniqueMappedResult();
		return document == null ? 0 : document.getDouble(AVG_VALUE).intValue();
	}

	@Override
	public int getAvgPulseValueDates(long patientId, LocalDateTime from, LocalDateTime to) {
		LOG.trace("from time: {}", from);
		LOG.trace("to time: {}", to);
		Criteria criteria = Criteria.where(PATIENT_ID).is(patientId)
				.andOperator(Criteria.where(DATE_TIME).gte(from).lte(to));
		LOG.trace("all values of patient {} are {}", patientId,
				mongoTemplate.find(new Query(criteria), AvgPulseDoc.class));
		MatchOperation match = match(criteria);
		return avgValueRequest(match);
	}
	

}
