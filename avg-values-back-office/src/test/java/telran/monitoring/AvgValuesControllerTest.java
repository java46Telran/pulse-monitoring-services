package telran.monitoring;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import telran.monitoring.service.AvgPulseValuesService;

@SpringBootTest
@AutoConfigureMockMvc
public class AvgValuesControllerTest {
private static final Integer AVG_VALUE_ONLY_PATIENT_ID = 80;
private static final Integer AVG_VALUE_DATES = 90;
@Autowired
MockMvc mockMvc;
@MockBean
AvgPulseValuesService pulseService;
@BeforeEach
void serviceMocking() {
	when(pulseService.getAvgPulseValue(ArgumentMatchers.anyLong()))
	.thenReturn(AVG_VALUE_ONLY_PATIENT_ID);
	when(pulseService.getAvgPulseValueDates(ArgumentMatchers.anyLong(),
			ArgumentMatchers.any(LocalDateTime.class),
			ArgumentMatchers.any(LocalDateTime.class))).thenReturn(AVG_VALUE_DATES);
}
@Test
void noDatesTest() throws UnsupportedEncodingException, Exception {
	String res = mockMvc.perform(get("/pulse/values/123")).andDo(print())
	.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
	assertEquals(AVG_VALUE_ONLY_PATIENT_ID +"", res);
}
@Test
void datesTest() throws UnsupportedEncodingException, Exception {
	String res = mockMvc.perform(get("/pulse/values/123")
			.param("from", "2023-01-01T12:00")
			.param("to", "2023-03-01T12:00")).andDo(print())
	.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
	assertEquals(AVG_VALUE_DATES +"", res);
}
@Test
void dateFromTest() throws UnsupportedEncodingException, Exception {
	String res = mockMvc.perform(get("/pulse/values/123")
			.param("from", "2023-01-01T12:00")).andDo(print())
	.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
	assertEquals(AVG_VALUE_DATES +"", res);
}
@Test
void dateToTest() throws UnsupportedEncodingException, Exception {
	String res = mockMvc.perform(get("/pulse/values/123")
			.param("to", "2023-01-01T12:00")).andDo(print())
	.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
	assertEquals(AVG_VALUE_DATES +"", res);
}
}
