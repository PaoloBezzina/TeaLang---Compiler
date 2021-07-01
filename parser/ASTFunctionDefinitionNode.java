package parser;

import parser.*;
import parser.visitor.Visitor;
import java.util.*;

public class ASTFunctionDefinitionNode implements ASTStatementNode {
    public ASTFunctionDefinitionNode(String identifier, ArrayList<tangible.Pair<String, TYPE>> parameters, TYPE type, ASTBlockNode block, int line_number) {
        this.identifier = identifier;
        this.parameters = parameters;
        this.type = type;
        this.block = block;
        this.line_number = line_number;

        //this.variable_names = variable_names;
        //this.signature = signature;

        for(tangible.Pair<String, TYPE> pair : parameters){
            this.variable_names.add(pair.first);
            this.signature.add(pair.second);
        }

    }

    public String identifier;
    public ArrayList<tangible.Pair<String, TYPE>> parameters = new ArrayList<tangible.Pair<String, TYPE>>();
    public ArrayList<String> variable_names = new ArrayList<String>();
    public ArrayList<TYPE> signature = new ArrayList<TYPE>();
    public TYPE type;
    public ASTBlockNode block;
    public int line_number;

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}