package mapper;

import java.io.BufferedWriter;
import java.io.File;

import org.json.simple.JSONObject;

public class LonLatSkyPositionAppender {
	
	private JSONObject ourMeasure;
	private File mangoFile;
	
	public LonLatSkyPositionAppender(JSONObject json,File mango) {
		this.ourMeasure = json;
		this.mangoFile = mango;
		
	}
	
	public void AppendLonLatSkyPosition(BufferedWriter out) {
		
	}

	public void AppendGlobalsLonLatSkyPosition(BufferedWriter out) {

	}
}
