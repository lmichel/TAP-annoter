/* Currently useless, may be useful at some point in order to instantiate WalkerMover*/

package utils;

import org.w3c.dom.traversal.DocumentTraversal;

public interface DocumentTraversalForMover extends DocumentTraversal {

	TreeWalkerMover createTreeWalker();
}
