package utils;

import org.w3c.dom.Document;
import org.w3c.dom.traversal.TreeWalker;

public interface NewTreeWalkerInterface extends TreeWalker {
	
	void goToTableMapping();
	
	void goToCollectionParameters();
	
	void appendConfig(TreeWalkerMover mangoWalker,Document templateDoc);
	
	void goToRoot();
}
