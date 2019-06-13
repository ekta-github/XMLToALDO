package com.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ExcelReader {

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws FileNotFoundException, IOException, InvalidFormatException {
		Map<Double , String> xmlTagMap = new HashMap<Double , String>(); 
		try {

				File file = new File("/Users/ekta/Documents/projects/temporary/FIX.xml");
				
			

				DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance()
			                             .newDocumentBuilder();

				Document doc = dBuilder.parse(file);

				System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
				

				if (doc.hasChildNodes()) {

					printNote(doc.getChildNodes(),xmlTagMap);
					 System.out.println("PROGRAM COMPLETE");
					 
					 

				}

			    } catch (Exception e) {
				System.out.println(e.getMessage());
			    }
		 
		 
	    File file = new File("/Users/ekta/Documents/projects/temporary/Mapping_FIX_to_ALDO.xlsx");
	    Workbook workbook = WorkbookFactory.create(new FileInputStream(file));
	    Sheet sheet = workbook.getSheetAt(1);
	    int column_index_1 = 5;
	    int column_index_2 = 6;
	    int column_index_3 = 8;
	    Row row = sheet.getRow(0);
		/*
		 * for (Cell cell : row) { // Column header names. switch
		 * (cell.getStringCellValue()) { case "Précision": column_index_1 =
		 * cell.getColumnIndex(); break; case "Position": column_index_2 =
		 * cell.getColumnIndex(); break; case "FIX Tag": column_index_3 =
		 * cell.getColumnIndex(); break; } }
		 */
	    String aldoString = "";
	    for (Row r : sheet) {
	        if (r.getRowNum()==0) continue;//hearders
	        // Length
	        Cell c_1 = r.getCell(column_index_1);
	        // Position
	        Cell c_2 = r.getCell(column_index_2);
	        // FIX Tag
	        Cell c_3 = r.getCell(column_index_3);
	        
	        // If FIX tag value is numeric apply preceding zeros , if alphanumeric , apply training spaces
	        
	        if(c_3!=null && c_3.getCellTypeEnum() != CellType.BLANK && c_3.getCellTypeEnum() != CellType.STRING) {
	        	// what is the current size of number
        		
	        	//isAlphanumeric2
	        	if(StringUtils.isNumeric(xmlTagMap.get(c_3.getNumericCellValue()))) {
	        		int length = xmlTagMap.get(c_3.getNumericCellValue()).length();
	        		
	        		// length < c_1 then find c_1-length , place that many zeros
	        		
	        		if(c_1!=null && c_1.getCellTypeEnum() != CellType.BLANK && c_1.getCellTypeEnum() != CellType.STRING) {
		        		double data = c_1.getNumericCellValue();
			        	 int c_1_value = (int)Math.round(data);
			        	 
			        	 if(length<c_1_value) {
			        		 aldoString = aldoString + StringUtils.leftPad(xmlTagMap.get(c_3.getNumericCellValue()), c_1_value-length, '0');
			        	 }else {
			        		 aldoString = aldoString + xmlTagMap.get(c_3.getNumericCellValue());
			        	 }
	        		}
	        	
	        		
	        	}else {
	        		if(xmlTagMap.get(c_3.getNumericCellValue())!=null && !xmlTagMap.get(c_3.getNumericCellValue()).isBlank()) {
	        			int length = xmlTagMap.get(c_3.getNumericCellValue()).length();
	        			// place c_1 spaces
		        		if(c_1!=null && c_1.getCellTypeEnum() != CellType.BLANK && c_1.getCellTypeEnum() != CellType.STRING) {
			        		double data = c_1.getNumericCellValue();
				        	 int c_1_value = (int)Math.round(data);
				        	 
				        	 if(length<c_1_value) {
				        		 aldoString =aldoString + StringUtils.rightPad(xmlTagMap.get(c_3.getNumericCellValue()), c_1_value-length, ' ');
				        	 }else {
				        		 aldoString = aldoString + xmlTagMap.get(c_3.getNumericCellValue());
				        	 }
		        		}
	        		}
	        		
	        	}
	        	 
	        }else {
	        	//At c_2 position , place c_1 zeros
	        	if(c_1!=null && c_1.getCellTypeEnum() != CellType.BLANK && c_1.getCellTypeEnum() != CellType.STRING) {
	        		double data = c_1.getNumericCellValue();
		        	 int value = (int)Math.round(data); 
		        	 for (int i = 0; i < value; i++) {
		        		 aldoString =  aldoString + '0' ;
		        		}
	        	}
	        	
	        	
	        }
	       
	        
	        
	        
	       //ß System.out.print(" (((((((((((( "+String.format("%.0f", new DecimalFormat("#").format(c_3.getNumericCellValue())));
	           // System.out.print("  "+c_1 + "   " + c_2+"   "+c_3+"\n");
	            
	        
	    }
	    System.out.print(" aldoString "+aldoString);
	    
	    FileWriter csvWriter = new FileWriter("ALDO.csv");
	    csvWriter.append(aldoString);
		
		csvWriter.flush();  
		csvWriter.close();  

	}
	
	  private static void printNote(NodeList nodeList,Map<Double , String> xmlTagMap) {
			
		    for (int count = 0; count < nodeList.getLength(); count++) {

			Node tempNode = nodeList.item(count);
			

			// make sure it's element node.
			if (tempNode.getNodeType() == Node.ELEMENT_NODE) {

				// get node name and value
				//System.out.println("\nNode Name =" + tempNode.getNodeName() + " [OPEN]");
				//System.out.println("Node Value =" + tempNode.getTextContent());

				if (tempNode.hasAttributes()) {

					// get attributes names and values
					NamedNodeMap nodeMap = tempNode.getAttributes();

					for (int i = 0; i < nodeMap.getLength(); i++) {

						Node node = nodeMap.item(i);
						//System.out.println("attr name : " + node.getNodeName());
						//System.out.println("attr value : " + node.getNodeValue());
						
						xmlTagMap.put(Double.parseDouble(node.getNodeValue()),tempNode.getTextContent()!=null?tempNode.getTextContent().trim():"");

					}

				}

				if (tempNode.hasChildNodes()) {

					// loop again if has child nodes
					printNote(tempNode.getChildNodes(), xmlTagMap);

				}

				//System.out.println("Node Name =" + tempNode.getNodeName() + " [CLOSE]");

			}

		    }
		    
		   

		  }
}