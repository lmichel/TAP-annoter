package mapper;

import java.io.BufferedWriter;
import java.io.File;

import org.json.simple.JSONObject;
import org.w3c.dom.traversal.TreeWalker;

public class LonLatSkyPositionAppender {
	
	private JSONObject ourMeasure;
	private File mangoFile;
	
	public LonLatSkyPositionAppender(JSONObject json,File mango) {
		this.ourMeasure = json;
		this.mangoFile = mango;
		
	}
	
	public void AppendLonLatSkyPosition(BufferedWriter out, TreeWalker walker) {
		
	}

	public void AppendGlobalsLonLatSkyPosition(BufferedWriter out) {

	}
}
