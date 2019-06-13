package com.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ReadXMLFile {

	  public static void main(String[] args) {

	    try {

		File file = new File("/Users/ekta/Documents/projects/temporary/FIX.xml");
		
	

		DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance()
	                             .newDocumentBuilder();

		Document doc = dBuilder.parse(file);

		System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
		Map<String , String> xmlTagMap = new HashMap<String , String>();

		if (doc.hasChildNodes()) {

			printNote(doc.getChildNodes(),xmlTagMap);
			 System.out.println("PROGRAM COMPLETE");
			 
			 writeToCSV(xmlTagMap);

		}

	    } catch (Exception e) {
		System.out.println(e.getMessage());
	    }

	  }
	  
	  private static String writeToCSV(Map<String , String> xmlTagMap) {
		  
		  String eol = "";

		  try (Writer writer = new FileWriter("somefile.txt")) {
			  
			  try (InputStream input = new FileInputStream("config.properties")) {

		            Properties props = new Properties();

		            // load a properties file
		            props.load(input);
		            
		            @SuppressWarnings("unchecked")
		            Enumeration<String> enums = (Enumeration<String>) props.propertyNames();
		            while (enums.hasMoreElements()) {
		              String key = enums.nextElement();
		              
		              // Write at position as key
		              String value = props.getProperty(key);
		              System.out.println(key + " : " + value);
		            }

		            

		        } catch (IOException ex) {
		            ex.printStackTrace();
		        }
			  
			  
			  
			  
			  
		    for (Map.Entry<String, String> entry : xmlTagMap.entrySet()) {
		      writer.append(entry.getKey())
		            .append(',')
		            .append(entry.getValue())
		            .append(eol);
		    }
		  } catch (IOException ex) {
		    ex.printStackTrace(System.err);
		  }

		  return "WOW";
	  }

	  private static void printNote(NodeList nodeList,Map<String , String> xmlTagMap) {
		
	    for (int count = 0; count < nodeList.getLength(); count++) {

		Node tempNode = nodeList.item(count);
		

		// make sure it's element node.
		if (tempNode.getNodeType() == Node.ELEMENT_NODE) {

			// get node name and value
			System.out.println("\nNode Name =" + tempNode.getNodeName() + " [OPEN]");
			System.out.println("Node Value =" + tempNode.getTextContent());

			if (tempNode.hasAttributes()) {

				// get attributes names and values
				NamedNodeMap nodeMap = tempNode.getAttributes();

				for (int i = 0; i < nodeMap.getLength(); i++) {

					Node node = nodeMap.item(i);
					System.out.println("attr name : " + node.getNodeName());
					System.out.println("attr value : " + node.getNodeValue());
					
					xmlTagMap.put(node.getNodeValue(),tempNode.getTextContent());

				}

			}

			if (tempNode.hasChildNodes()) {

				// loop again if has child nodes
				printNote(tempNode.getChildNodes(), xmlTagMap);

			}

			System.out.println("Node Name =" + tempNode.getNodeName() + " [CLOSE]");

		}

	    }
	    
	   

	  }

	}