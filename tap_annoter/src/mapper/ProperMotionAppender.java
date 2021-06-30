package mapper;

import java.io.BufferedWriter;
import java.io.File;

import org.json.simple.JSONObject;

public class ProperMotionAppender {

	private JSONObject ourMeasure;
	private File mangoFile;
	
	public ProperMotionAppender(JSONObject json, File mango) {
		this.ourMeasure = json;
		this.mangoFile = mango;
	}
	
	public void AppendProperMotion(BufferedWriter out) {
		
	}
}
