package mapper;

import java.io.*;

import org.json.simple.JSONObject;
import org.w3c.dom.traversal.TreeWalker;

public class HardnessRatioAppender {

	private JSONObject ourMeasure;
	private File mangoFile;
	
	public HardnessRatioAppender(JSONObject json,File mango) {
		
		this.ourMeasure = json;
		this.mangoFile = mango;
		
	}
	
	public void AppendHardnessRatio(BufferedWriter out, TreeWalker walker) {
		
	}

	public void AppendGlobalsHardnessRatio(BufferedWriter out) {

	}
}
