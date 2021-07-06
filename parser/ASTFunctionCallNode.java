package parser;

import parser.visitor.Visitor;
import java.util.*;

public class ASTFunctionCallNode implements ASTExprNode {

    public ASTFunctionCallNode(String identifier, ArrayList<ASTExprNode> statements, int line_number) {
        this.identifier = identifier;
        this.statements = statements;
        this.line_number = line_number;
    }

    public String identifier;
    public ArrayList<ASTExprNode> statements = new ArrayList<ASTExprNode>();
    public int line_number;

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
