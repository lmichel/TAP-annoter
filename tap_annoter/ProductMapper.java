package tap_annoter;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import tap.TAPException;

public class ProductMapper {

	private String jsonPath;
	
	public ProductMapper(String nom_du_fichier) {
		this.jsonPath = nom_du_fichier;
	}

	
	public void BuildAnnotations(BufferedWriter out) throws IOException {
		
		JSONParser jsonP = new JSONParser();
		System.out.println("Salut");
		
		try {
			 //On récupère le fichier json pour le parser
	         JSONObject jsonO = (JSONObject) jsonP.parse(new FileReader(jsonPath));
	     
	         JSONArray parametersList = (JSONArray) jsonO.get("parameters");
	         
	         Iterator<?> paramIter = parametersList.iterator();
	         
	         //On regarde tout les paramètres
	         while (paramIter.hasNext()) {
	        	 
	        	 System.out.println("On rentre dans le while");
	        	 
	        	 JSONObject our_measure = (JSONObject) paramIter.next();
	        	 
	        	 if (((our_measure.get("measure")).toString().equals("LonLatSkyPositionEllErr"))) {
	        		 //out.write("LonLatSkyPositionEllErr found");
	        		 //out.newLine();
	        		 System.out.println("LonLatSkyPosEllErr");
	        	 }
	        	 
	        	 
	        	 else if ((our_measure.get("measure").equals("LonLatSkyPosition"))) {
	        		 //out.write("LonLatSkyPosition found");
	        		 //out.newLine();
	        		 System.out.println("LonlatSkyPos");
	        	 }
	        	 
	        	 else if ((our_measure.get("measure").equals("Position"))) {
	        		 //out.write("Position found");
	        		 //out.newLine();
	        		 System.out.println("Position");
	        	 }
	        	 
	        	 else if ((our_measure.get("measure").equals("ProperMotion"))) {
	        		 //out.write("ProperMotion found");
	        		 //out.newLine();
	        		 //paramIter.next();
	        		 System.out.println("ProperMotion");
	        	 }
	        	 
	        	 else if ((our_measure.get("measure").equals("status"))) {
	        		 //out.write("status found");
	        		 //out.newLine();
	        		 //paramIter.next();
	        		 System.out.println("status");
	        	 }
	        	 
	        	 else if ((our_measure.get("measure").equals("Photometry"))) {
	        		 //out.write("Photometry found");
	        		 //out.newLine();
	        		 //paramIter.next();
	        		 System.out.println("Photometry");
	        	 }
	        	 
	        	 else if ((our_measure.get("measure").equals("GenericMeasure"))) {
	        		 //out.write("GenericMeasure found");
	        		 //out.newLine();
	        		 System.out.println("On est la ou on devrait être");
	        		 //paramIter.next();
	        	 }
	        	 
	        	 else if ((our_measure.get("measure").equals("HardnessRatio"))) {
	        		 //out.write("HardnessRation found");
	        		 //out.newLine();
	        		 //paramIter.next();
	        		 System.out.println("Hardness");
	        	 }
	        	 
	        	 else if ((our_measure.get("measure").equals("DetectionFlag"))) {
	        		 //out.write("DetectionFlag found");
	        		 //out.newLine();
	        		 //paramIter.next();
	        		 System.out.println("Detection Flag");
	        	 }
	        	 
	        	 else if ((our_measure.get("measure").equals("MJD"))) {
	        		 //out.write("MJD found");
	        		 //out.newLine();
	        		 //paramIter.next();
	        		 System.out.println("MJD");
	        	 }
	        	 
	         }
	         
	      } catch (FileNotFoundException e) {
	         e.printStackTrace();
	      } catch (IOException e) {
	         e.printStackTrace();
	      } catch (ParseException e) {
	         e.printStackTrace();
	      } finally { 
			//out.write("</VODML>");
			//out.newLine();
	    	System.out.println("Finnaly");
			}
		
	}
}