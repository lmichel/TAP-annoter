package mapper;

import java.io.BufferedWriter;
import java.io.File;

import org.json.simple.JSONObject;
import org.w3c.dom.traversal.TreeWalker;

public class LonLatSkyPositionEllErrAppender {

	private JSONObject ourMeasure;
	private File mangoFile;
	
	public LonLatSkyPositionEllErrAppender(JSONObject json, File mango) {
		
		this.ourMeasure = json;
		this.mangoFile = mango;
	}
	
	public void AppendLonLatSkyPositionEllErr(BufferedWriter out, TreeWalker walker) {
		
	}

	public void AppendGlobalsLonLatSkyPositionEllErr(BufferedWriter out) {

	}
}
