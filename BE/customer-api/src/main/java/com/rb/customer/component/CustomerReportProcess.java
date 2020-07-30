package com.rb.customer.component;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import static com.rb.customer.constant.CustomerConstants.REPORT_REFERENCE;
import static com.rb.customer.constant.CustomerConstants.REPORT_DESCRIPTION;
import static com.rb.customer.constant.CustomerConstants.REPORT_ENDBALANCE;
import static com.rb.customer.constant.CustomerConstants.REPORT_ACC_NUMBER;
import static com.rb.customer.constant.CustomerConstants.REPORT_STARTBALANCE;
import static com.rb.customer.constant.CustomerConstants.REPORT_MUTATION;

/**
 * @author PaulNayagam
 *
 */
@Component
public class CustomerReportProcess{
	private static final Logger logger = LogManager.getLogger(CustomerReportProcess.class);
	
	public List<LinkedHashMap<String, Object>> convertCSVRecordToList() throws SAXException, IOException {
		logger.info("Called convertCSVRecordToList()");
	    Resource csvResource = new ClassPathResource("records.csv");
	    Resource xmlResource = new ClassPathResource("records.xml");
		File csvFile = csvResource.getFile();
		File xmlFile = xmlResource.getFile();
	    BufferedReader br = null;
	    String line = "";
	    String cvsSplitBy = ",";
	    LinkedHashMap<String, Object> mapCSV = new LinkedHashMap<String, Object>();
	    LinkedHashMap<String, Object> mapXML = new LinkedHashMap<String, Object>();
	    List<LinkedHashMap<String, Object>> mapReportList = new ArrayList<LinkedHashMap<String, Object>>();	    
	    //Get Document Builder
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder builder = null;			    

	    try {
	        br = new BufferedReader(new FileReader(csvFile));
	        while ((line = br.readLine()) != null) {
	            System.out.println(line);
	            String[] data = line.split(cvsSplitBy);
	            mapCSV.put(REPORT_REFERENCE, data[0]);
	            mapCSV.put(REPORT_ACC_NUMBER, data[1]);
	            mapCSV.put(REPORT_DESCRIPTION, data[2]);
	            mapCSV.put(REPORT_STARTBALANCE, data[3]);
	            mapCSV.put(REPORT_MUTATION, data[4]);
	            mapCSV.put(REPORT_ENDBALANCE, data[5]);
	            mapReportList.add(mapCSV);
	            mapCSV = new LinkedHashMap<String, Object>();                
	        }
	        
	        try {
				builder = factory.newDocumentBuilder();
			} catch (ParserConfigurationException e1) {
				// TODO Auto-generated catch block
				logger.error("Exception occurred while parsing {}", e1);
			}		     
		    //Build Document
		    Document document = builder.parse(xmlFile);		     
		    //Normalize the XML Structure; It's just too important !!
		    document.getDocumentElement().normalize();		     
		    //Here comes the root node
		    Element root = document.getDocumentElement();
		    logger.info("Node name:{} ",root.getNodeName());
		    //Get all Records
		    NodeList nList = document.getElementsByTagName("record");
		    logger.info("====================================");
		     
		    for (int temp = 0; temp < nList.getLength(); temp++)
		    {
		     Node node = nList.item(temp);
		     if (node.getNodeType() == Node.ELEMENT_NODE)
		     {
		        //Get All each Record detail
		        Element eElement = (Element) node;		       
		        mapXML.put(REPORT_REFERENCE, eElement.getAttribute("reference"));
		        mapXML.put(REPORT_ACC_NUMBER, eElement.getElementsByTagName("accountNumber").item(0).getTextContent());
		        mapXML.put(REPORT_DESCRIPTION, eElement.getElementsByTagName("description").item(0).getTextContent());
		        mapXML.put(REPORT_STARTBALANCE, eElement.getElementsByTagName("startBalance").item(0).getTextContent());
		        mapXML.put(REPORT_MUTATION, eElement.getElementsByTagName("mutation").item(0).getTextContent());
		        mapXML.put(REPORT_ENDBALANCE, eElement.getElementsByTagName("endBalance").item(0).getTextContent());
		        mapReportList.add(mapXML);
	            mapXML = new LinkedHashMap<String, Object>(); 		        
		     }
		    }
	    } catch (FileNotFoundException e) {
	    	logger.error("FileNotFoundException {}", e);
	    }
	     catch (IOException e) {
	    	 logger.error("IOException {}", e);
	    }
	    logger.info("mapReportList:{} ",mapReportList);
	    return mapReportList;
	}
}
