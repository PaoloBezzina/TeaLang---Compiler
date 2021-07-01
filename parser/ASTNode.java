package parser;

import parser.*;
import parser.visitor.Visitor;
import java.util.*;

// Abstract Nodes
public interface ASTNode {
	void accept(Visitor UnnamedParameter);
}