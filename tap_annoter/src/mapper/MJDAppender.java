package mapper;

import java.io.BufferedWriter;
import java.io.File;

import org.json.simple.JSONObject;

public class MJDAppender {
	
	private JSONObject ourMeasure;
	private File mangoFile;

	public MJDAppender(JSONObject our_measure, File mango) {
		this.ourMeasure = our_measure;
		this.mangoFile = mango;
	}
	
	public void AppendMJD(BufferedWriter out) {
		
	}

}
