package mapper;

import java.io.BufferedWriter;
import java.io.File;

import org.json.simple.JSONObject;
import org.w3c.dom.traversal.TreeWalker;

public class PositionAppender {

	private JSONObject ourMeasure;
	private File mangoFile;
	
	public PositionAppender(JSONObject json,File mango) {
		
		this.ourMeasure = json;
		this.mangoFile = mango;
	}
	
	public void AppendPosition(BufferedWriter out, TreeWalker walker) {
		
	}

	public void AppendGlobalsPosition(BufferedWriter out) {

	}
}

