package mapper;

import java.io.BufferedWriter;
import java.io.File;

import org.json.simple.JSONObject;
import org.w3c.dom.traversal.TreeWalker;

public class PhotometryAppender {

	private JSONObject ourMeasure;
	private File mangoFile;
	
	public PhotometryAppender(JSONObject json, File mango) {
		
		this.ourMeasure = json;
		this.mangoFile = mango;
		// TODO Auto-generated constructor stub
	}
	
	public void AppendPhotometry(BufferedWriter out, TreeWalker walker) {
		
	}

	public void AppendGlobalsPhotometry(BufferedWriter out) {
		
	}

}
