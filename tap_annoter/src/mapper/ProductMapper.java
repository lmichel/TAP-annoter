package mapper;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.parsers.*;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.*;
import org.w3c.dom.traversal.*;

import tap.TAPException;

import utils.FileGetter;
import utils.TreeWalkerMover;

import org.apache.xerces.parsers.*;

/**
 * @author joann
 *
 */
public class ProductMapper {

	private File jsonFile;
	
	public ProductMapper(File theFile) {
		this.jsonFile = theFile;
	}

	
	/**
	 * @param out
	 * @throws IOException
	 */
	public void BuildAnnotations(BufferedWriter out,TreeWalkerMover walker,Document templateDoc) throws IOException {
		
	    
		JSONParser jsonP = new JSONParser();
		
		try {
			 //On récupère le fichier json pour le parser
	         JSONObject jsonO = (JSONObject) jsonP.parse(new FileReader(jsonFile));
	     
	         JSONArray parametersList = (JSONArray) jsonO.get("parameters");
	         
	         Iterator<?> paramIter = parametersList.iterator();
	         
	         //On regarde tout les paramètres
	         while (paramIter.hasNext()) {

	        	 JSONObject our_measure = (JSONObject) paramIter.next();
	        	 
	        	 if (((our_measure.get("measure")).toString().equals("LonLatSkyPositionEllErr"))) {
	        		 
	        		 try {
	     				File mangoFile = new FileGetter("mango.LonLatSkyPosition_ellerr.mapping.xml").GetFile();
	     				LonLatSkyPositionEllErrAppender appender = new LonLatSkyPositionEllErrAppender(our_measure,mangoFile);
	     				appender.AppendLonLatSkyPositionEllErr(out,walker);
	     			} catch (URISyntaxException e1) {
	     				System.out.println("File doesn't exist");
	     				e1.printStackTrace();
	     			}
	        		 
	        		 System.out.println("LonLatSkyPosEllErr");
	        	 }
	        	 
	        	 
	        	 else if ((our_measure.get("measure").equals("LonLatSkyPosition"))) {
	        		 
	        		 try {
		     				File mangoFile = new FileGetter("mango.LonLatSkyPosition.mapping.xml").GetFile();
		     				LonLatSkyPositionAppender appender = new LonLatSkyPositionAppender(our_measure,mangoFile);
		     				appender.AppendLonLatSkyPosition(out,walker);
		     			} catch (URISyntaxException e1) {
		     				System.out.println("File doesn't exist");
		     				e1.printStackTrace();
		     			}
	        		 System.out.println("LonlatSkyPos found");
	        	 }
	        	 
	        	 else if ((our_measure.get("measure").equals("Position"))) {
	        		 
	        		 try {
		     				File mangoFile = new FileGetter("mango.Position.mapping.xml").GetFile();
		     				PositionAppender appender = new PositionAppender(our_measure,mangoFile);
		     				appender.AppendPosition(out,walker);
		     			} catch (URISyntaxException e1) {
		     				System.out.println("File doesn't exist");
		     				e1.printStackTrace();
		     			}
	        		 System.out.println("Position found");
	        	 }
	        	 
	        	 else if ((our_measure.get("measure").equals("ProperMotion"))) {
	        		 
	        		 try {
		     				File mangoFile = new FileGetter("mango.ProperMotion.mapping.xml").GetFile();
		     				ProperMotionAppender appender = new ProperMotionAppender(our_measure,mangoFile);
		     				appender.AppendProperMotion(out,walker);
		     			} catch (URISyntaxException e1) {
		     				System.out.println("File doesn't exist");
		     				e1.printStackTrace();
		     			}
	        		 System.out.println("ProperMotion found");
	        	 }
	        	 
	        	 else if ((our_measure.get("measure").equals("status"))) {
	        		 out.write("status found");
	        		 out.newLine();
	        		 System.out.println("status found");
	        	 }
	        	 
	        	 else if ((our_measure.get("measure").equals("Photometry"))) {
	        		 
	        		 try {
		     				File mangoFile = new FileGetter("mango.Photometry.mapping.xml").GetFile();
		     				PhotometryAppender appender = new PhotometryAppender(our_measure,mangoFile);
		     				appender.AppendPhotometry(out,walker);
		     			} catch (URISyntaxException e1) {
		     				System.out.println("File doesn't exist");
		     				e1.printStackTrace();
		     			}
	        		 System.out.println("Photometry found");
	        	 }
	        	 
	        	 else if ((our_measure.get("measure").equals("GenericMeasure"))) {
	        		 
	        		 try {
		     				File mangoFile = new FileGetter("mango.GenericMeasure.mapping.xml").GetFile();
		     				System.out.println("file done");
		     				GenericMeasureAppender appender = new GenericMeasureAppender(our_measure,mangoFile, walker);
		     				appender.AppendGenericMeasure(templateDoc);
		     			} catch (URISyntaxException e1) {
		     				System.out.println("File doesn't exist");
		     				e1.printStackTrace();
		     			}
	        		 System.out.println("GenericMeasure found");
	        	 }
	        	 
	        	 else if ((our_measure.get("measure").equals("HardnessRatio"))) {
	        		 
	        		 try {
		     				File mangoFile = new FileGetter("mango.HardnessRatio.mapping.xml").GetFile();
		     				HardnessRatioAppender appender = new HardnessRatioAppender(our_measure,mangoFile);
		     				appender.AppendHardnessRatio(out,walker);
		     				
		     			} catch (URISyntaxException e1) {
		     				System.out.println("File doesn't exist");
		     				e1.printStackTrace();
		     			}
	        		 System.out.println("HardnessRatio found");
	        	 }
	        	 
	        	 else if ((our_measure.get("measure").equals("DetectionFlag"))) {
	        		 
	        		 
	        		 try {
		     				File mangoFile = new FileGetter("mango.DetectionFlag.mapping.xml").GetFile();
		     				DetectionFlagAppender appender = new DetectionFlagAppender(our_measure,mangoFile, walker);
		     				appender.AppendDetectionFlag(templateDoc);
		     			} catch (URISyntaxException e1) {
		     				System.out.println("File doesn't exist");
		     				e1.printStackTrace();
		     			}
	        		  
	        	 }
	        	 
	        	 else if ((our_measure.get("measure").equals("MJD"))) {
	        		 
	        		 try {
		     				File mangoFile = new FileGetter("mango.MJD.mapping.xml").GetFile();
		     				MJDAppender appender = new MJDAppender(our_measure,mangoFile);
		     				appender.AppendMJD(out,walker);
		     			} catch (URISyntaxException e1) {
		     				System.out.println("File doesn't exist");
		     				e1.printStackTrace();
		     			}
	        		 System.out.println("MJD found");
	        	 }
	        	 
	         }
	         
	      } catch (FileNotFoundException e) {
	         e.printStackTrace();
	      } catch (IOException e) {
	         e.printStackTrace();
	      } catch (ParseException e) {
	         e.printStackTrace();
	      } finally {
	    	System.out.println("append done");
			}
		
	}

}