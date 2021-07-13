/*Not done, need to be implemented*/

package mapper;

import java.io.BufferedWriter;
import java.io.File;

import org.json.simple.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.traversal.TreeWalker;

public class ProperMotionAppender {

	private JSONObject ourMeasure;
	private File mangoFile;
	private TreeWalker walker;
	
	public ProperMotionAppender(JSONObject json, File mango, TreeWalker walker) {
		this.ourMeasure = json;
		this.mangoFile = mango;
		this.walker = walker;
		
	}
	
	public void AppendProperMotion(Document templateDoc) {
		
	}

	public void AppendGlobalsProperMotion(BufferedWriter out) {

	}
}
