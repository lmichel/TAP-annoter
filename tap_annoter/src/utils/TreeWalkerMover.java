package utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.TreeWalker;

public abstract class TreeWalkerMover implements TreeWalker {

	public TreeWalkerMover() {
		super();
	}

	public void goToTableMapping() {
		
		this.goToRoot();
		
		while (((Element) (this.getCurrentNode())).getTagName()!="TABLE_MAPPING") {
			this.nextNode(); //putting mangoWalker to the right place
		}
		
		this.firstChild(); //we are at <INSTANCE dmrole="root"...>
		
	}
	
	public void goToCollectionParameters() {
		
		while (this.getCurrentNode()!=null) {

			
			String currentdmrole = ((Element)(this.getCurrentNode())).getAttribute("dmrole");
				
			if (currentdmrole != null && currentdmrole.equals("mango:MangoObject.parameters")) {
				break;
				}
			
			this.nextNode();
			
			}
		}

	
	public void appendConfig(TreeWalkerMover mangoWalker,Document templateDoc) {
		
		Node newImportedChild = templateDoc.importNode(mangoWalker.getCurrentNode(),true);
		Node nodeToModify = this.getCurrentNode();
		nodeToModify.appendChild(newImportedChild);
		this.goToRoot();

	}
	
	public void goToRoot() {
		
		while (this.getCurrentNode()!=this.getRoot()) {
			this.parentNode();
			System.out.println("Going back to root in generic measure");
		}
	}
}