package tangible;

import java.util.*;
import parser.*;

public class Function {
    public Function(ArrayList<String> variables, ArrayList<TYPE> signature, ASTBlockNode block) {
        this.variables = variables;
        this.signature = signature;
        this.block = block;
    }

    public Function(TYPE type, ArrayList<TYPE> signature, int line_number) {
        this.type = type;
        this.signature = signature;
        this.line_number = line_number;
    }

    public TYPE type;
    public ArrayList<TYPE> signature;
    public ArrayList<String> variables;
    public ASTBlockNode block;
    public int line_number;
}
