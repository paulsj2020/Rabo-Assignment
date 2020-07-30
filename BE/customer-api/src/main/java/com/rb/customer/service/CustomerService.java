package com.rb.customer.service;
import java.io.IOException;

import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.SAXException;
/**
 * @author PaulNayagam
 *
 */
public interface CustomerService {
	public void createReportForFailure() throws IOException, SAXException;	
}


