package com.metasoft.flying.service;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.metasoft.flying.model.Flight;
import com.metasoft.flying.model.FlightDequeTimer;
import com.metasoft.flying.service.common.ScheduleService;

@Service
public class FlightService {

	@Autowired
	private ScheduleService scheduleService;

	@Value("${room.check.period}")
	private int roomCheckPeriod;
	
	private FlightDequeTimer flightDeque;
	
	//@PostConstruct
	public void init() {
		flightDeque = new FlightDequeTimer(){
			@Override
			public void checkDeque() {}
		};
		scheduleService.scheduleAtFixedRate(flightDeque, DateUtils.MILLIS_PER_SECOND, DateUtils.MILLIS_PER_SECOND
				* roomCheckPeriod);
	}

	public void addFlightDeq(Flight flight) {
		flightDeque.addFlightDeq(flight);
	}
	public int getDequeSize() {
		return flightDeque.getDequeSize();
	}

}