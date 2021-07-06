package parser;

import parser.visitor.Visitor;

public interface ASTExprNode extends ASTNode {
	@Override
	void accept(Visitor UnnamedParameter);
}