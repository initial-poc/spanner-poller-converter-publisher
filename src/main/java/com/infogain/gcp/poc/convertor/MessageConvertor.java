package com.infogain.gcp.poc.convertor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.infogain.gcp.poc.poller.entity.PNR;

@Component
public class MessageConvertor {

	private static final Logger logger = LoggerFactory.getLogger(MessageConvertor.class);

	public String doConvert(PNR pnr) {
		logger.info("going to convert object into json {}", pnr);
		String json = new Gson().toJson(pnr);
		logger.info("converted json {}", json);
		return json;
	}
}
