package com.rb.customer.service.impl;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;
import com.rb.customer.component.CustomerReportProcess;
import com.rb.customer.service.CustomerService;
import com.rb.customer.util.CSVUtils;
import static com.rb.customer.constant.CustomerConstants.REPORT_REFERENCE;
import static com.rb.customer.constant.CustomerConstants.REPORT_DESCRIPTION;
import static com.rb.customer.constant.CustomerConstants.REPORT_ENDBALANCE;



/**
 * @author PaulNayagam
 *
 */
@Service
public class CustomerServiceImpl implements CustomerService {
	
	@Autowired
	CustomerReportProcess reportProcess;
	private static final Logger logger = LogManager.getLogger(CustomerServiceImpl.class);
	public void createReportForFailure() throws IOException, SAXException {
		logger.info("Called createReportForFailure method");
		String csvFile = "customerStatement.csv";
		FileWriter writer = new FileWriter(csvFile);
		List<LinkedHashMap<String, Object>> finallist = reportProcess.convertCSVRecordToList();
		logger.info("finallist.size():{} ",finallist.size());
		Set<String> s = new HashSet<String>();
		Set<String> s2 = new HashSet<String>();
		try {
			for (LinkedHashMap<String, Object> item : finallist) {
				String endBalance = null;
				Double eb = 0.0;
				if (!item.get(REPORT_REFERENCE).equals(REPORT_REFERENCE)) {
					endBalance = (String) item.get(REPORT_ENDBALANCE);
					logger.info("endBalance inside if condition :{} ",endBalance);
					eb = Double.parseDouble(endBalance);
				}
				logger.info("endBalance outside if condition :{} ",endBalance);
				if (s.add((String) item.get(REPORT_REFERENCE)) == false) {
					s2.add((String) item.get(REPORT_REFERENCE));
					logger.info("reference value :{} ",item.get(REPORT_REFERENCE));
					logger.info("description value :{} ",item.get(REPORT_DESCRIPTION));
				} else if (eb < 0) {
					s2.add((String) item.get(REPORT_ENDBALANCE));
					logger.info("s2 EndBalance:{} ",item.get(REPORT_ENDBALANCE));
				}

			}
			// for header
			CSVUtils.writeLine(writer, Arrays.asList(REPORT_REFERENCE, REPORT_DESCRIPTION, REPORT_ENDBALANCE));
			for (LinkedHashMap<String, Object> item : finallist) {
				for (String temp : s2) {
					if (item.get(REPORT_REFERENCE).equals(temp) || item.get(REPORT_ENDBALANCE).equals(temp)) {
						List<String> result = new ArrayList<>();
						result.add((String) item.get(REPORT_REFERENCE));
						result.add((String) item.get(REPORT_DESCRIPTION));
						result.add((String) item.get("EndBalance"));
						logger.info("Excel Data result value:{} ",result);
						CSVUtils.writeLine(writer, result);
					}
				}
			}
			writer.flush();
			writer.close();
			
		}catch(Exception e){
			 logger.error("Exception Occurred while processing the request in CustomerServiceImpl class  {}", e);
		}
	

	}
}
