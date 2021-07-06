package parser;

import parser.visitor.Visitor;

public class ASTIdentifierNode implements ASTExprNode{

    public ASTIdentifierNode(String identifier, int line_number) {
        this.identifier = identifier;
		this.line_number = line_number;
	}

    public String identifier;
	public int line_number;

    @Override
	public void accept(Visitor v) {
		v.visit(this);
	}
    
}
