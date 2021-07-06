package parser;

import parser.visitor.Visitor;

// Abstract Nodes
public interface ASTNode {
	void accept(Visitor UnnamedParameter);
}