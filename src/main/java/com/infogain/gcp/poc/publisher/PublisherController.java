package com.infogain.gcp.poc.publisher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.infogain.gcp.poc.App.PubsubOutboundGateway;

@RestController
public class PublisherController {
	// tag::autowireGateway[]
	@Autowired
	private PubsubOutboundGateway messagingGateway;
	// end::autowireGateway[]

	@PostMapping("/publishMessage")
	public String publishMessage(@RequestParam("message") String message) {
		messagingGateway.sendToPubsub(message);
		return "Ok";
	}

}
