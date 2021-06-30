package parser;

import parser.*;
import java.util.*;

// Abstract Nodes
public interface ASTNode {
	void accept(visitor.Visitor UnnamedParameter);
}