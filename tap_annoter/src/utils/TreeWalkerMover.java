package utils;

import org.apache.xerces.dom.TreeWalkerImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.TreeWalker;


/**
 * @author joann
 * This class is an implementation of TreeWalker in order to add several methods
 * that are usefull in our appenders
 */
public  class TreeWalkerMover extends TreeWalkerImpl {
	
	public TreeWalkerMover(Node arg0, int arg1, NodeFilter arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}
	
	
	/**
	 * This method is used to set the cursor of our Tree on the Node Child of TableMapping
	 */
	public void goToTableMapping() {
		
		this.goToRoot(); //getting on root to parse the tree from the begginning
		
		//putting mangoWalker to the right place
		while (((Element) (this.getCurrentNode())).getTagName()!="TABLE_MAPPING") {
			this.nextNode(); 
		}
		
		this.firstChild(); //we are at <INSTANCE dmrole="root"...>
		
	}
	
	
	/**
	 * This method is used to set the cursor of our template tree in the collection of parameters
	 * in order to add parameters later
	 */
	public void goToCollectionParameters() {
		
		while (this.getCurrentNode()!=null) {


			String currentdmrole = ((Element)(this.getCurrentNode())).getAttribute("dmrole");
				
			if (currentdmrole != null && currentdmrole.equals("mango:MangoObject.parameters")) {
				break; //we are in the right place
				}
			
			this.nextNode();
			
			}
		}

	
	/**
	 * @param mangoWalker
	 * @param templateDoc
	 * 
	 * This method merge mangoWalker to the templateTree
	 * We need the templateDoc because of importNode
	 */
	public void appendConfig(TreeWalkerMover mangoWalker,Document templateDoc) {
		
		Node newImportedChild = templateDoc.importNode(mangoWalker.getCurrentNode(),true); //necessary, otherwise java doesn't know which node to modify
		Node nodeToModify = this.getCurrentNode();
		nodeToModify.appendChild(newImportedChild);
		this.goToRoot();

	}
	
	/**
	 * This method is used to go to the root of a tree
	 */
	public void goToRoot() {
		
		while (this.getCurrentNode()!=this.getRoot()) {
			this.parentNode();
			System.out.println("Going back to root in generic measure");
		}
	}

}