

package mapper;

import org.w3c.dom.Element;
import org.w3c.dom.traversal.TreeWalker;

public class IdentifierAppender {

	private String identifier;
	private TreeWalker walker;

	/**
	 * @param columnName the name of the identifier
	 * @param walker the walker we need to fill
	 */
	public IdentifierAppender(String columnName,TreeWalker walker) {
	
		this.identifier = columnName;
		this.walker = walker;
	}

	/**
	 * This methode is modifying the template mango with the right value in the identifier field
	 */
	public void appendIdentifier() {
		
		walker.setCurrentNode(walker.getRoot());
		
		Element currentElement = (Element) walker.getCurrentNode();
		String currentdmrole = (String) currentElement.getAttribute("dmrole");
		
		while (!currentdmrole.equals("mango:MangoObject.identifier")) {
			
			currentElement = (Element) walker.getCurrentNode();
			currentdmrole = (String) currentElement.getAttribute("dmrole");
			
			if (currentdmrole.equals("mango:MangoObject.identifier")) {
				currentElement.setAttribute("ref", this.identifier);
			}
			walker.nextNode();
		}
	}
}
