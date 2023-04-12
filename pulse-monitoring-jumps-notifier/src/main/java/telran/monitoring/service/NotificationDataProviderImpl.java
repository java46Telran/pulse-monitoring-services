package telran.monitoring.service;

import java.net.URI;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import telran.monitoring.model.NotificationData;
@Service
public class NotificationDataProviderImpl implements NotificationDataProvider {
	static private Logger LOG = LoggerFactory.getLogger(NotificationDataProvider.class);
 @Autowired
	private RestTemplate restTemplate;
 @Value("${app.data.provider.mapping.url:data}")
 String mappingUrl;
 
 @Value("${app.data.provider.host:localhost}")
 String host;
 @Value("${app.data.provider.port:8080}")
 int port;
	@Override
	public NotificationData getData(long patientId) {
		NotificationData notificationData = null;
		try {
			ResponseEntity<NotificationData> response =
					restTemplate.exchange(getFullUrl(patientId),
							HttpMethod.GET, null, NotificationData.class);
			notificationData =  response.getBody();
			LOG.debug("doctor's email received from data provider: {}", notificationData.doctorEmail);
		} catch (RestClientException e) {
			LOG.error("Notification Data Provider has not sent the data; reason: {}",e.getMessage());
		}
		return notificationData;
	}
	private String getFullUrl(long patientId) {
		String url = String.format("http://%s:%d/%s/%d", host,port,mappingUrl,patientId);
		LOG.debug("URL for communicating with data provider is {}", url);
		return url;
	}
	

}
