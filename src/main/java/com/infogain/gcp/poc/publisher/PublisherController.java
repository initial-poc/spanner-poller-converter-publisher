package com.infogain.gcp.poc.publisher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PublisherController {

	@Autowired
	private PubSubTemplate pubSubTemplate;

	@PostMapping("/publishMessage")
	public String publishMessage(@RequestParam("message") String message) {
		pubSubTemplate.publish("testTopic", message);
		return "Ok";
	}

}
