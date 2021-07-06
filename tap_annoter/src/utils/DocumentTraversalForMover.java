package utils;

import org.w3c.dom.traversal.DocumentTraversal;

public interface DocumentTraversalForMover extends DocumentTraversal {

	TreeWalkerMover createTreeWalker();
}
