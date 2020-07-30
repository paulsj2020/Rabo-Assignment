/**
 * 
 */
package com.rb.customer.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.SAXException;

import com.rb.customer.service.CustomerService;

/**
 * @author PaulNayagam
 *
 */
@RestController
public class CustomerController {
	
	@Autowired
	CustomerService cutomerService;
	
	@GetMapping("/test")
	public String getsample() {
		return "Customer Microservice API is working fine now";		
	}
	
	@GetMapping("/customer-Failure-report")
	public String cutomerReport() {
		try {
			cutomerService.createReportForFailure();
		} catch (IOException | SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "success";		
	}

}
