package mapper;

import java.io.BufferedWriter;
import java.io.File;

import org.json.simple.JSONObject;

public class LonLatSkyPositionEllErrAppender {

	private JSONObject ourMeasure;
	private File mangoFile;
	
	public LonLatSkyPositionEllErrAppender(JSONObject json, File mango) {
		
		this.ourMeasure = json;
		this.mangoFile = mango;
	}
	
	public void AppendLonLatSkyPositionEllErr(BufferedWriter out) {
		
	}

	public void AppendGlobalsLonLatSkyPositionEllErr(BufferedWriter out) {

	}
}
