package parser;

import parser.visitor.Visitor;

public interface ASTStatementNode extends ASTNode{
	@Override
	void accept(Visitor UnnamedParameter);
}