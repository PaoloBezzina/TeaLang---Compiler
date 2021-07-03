package visitor;

import visitor.*;
import java.io.*;
import parser.*;
import parser.visitor.*;

/* 
std::ofstream xmlfile;
unsigned int indentation_level;
const std::string TAB = "    ";
std::string indentation();
std::string type_str(parser::TYPE);
std::string xml_safe_op(std::string);
 */

public class XMLVisitor implements Visitor{

	String filePath = "program.xml";
	FileWriter xmlfile;
	private int indentation_level;
	private String TAB = String.valueOf('\t');


	public XMLVisitor() {
		this.indentation_level = 0;
		try {
			xmlfile = new FileWriter(filePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public final void close() {
		try {
			xmlfile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public final String indentation() {

		String tabs = "";
	
		// Tab character for each indentation level
		for(int i = 0; i < indentation_level; i++)
			tabs += TAB;
	
		return tabs;
	}

	@Override
	public void visit(parser.ASTProgramNode program)
	{
		// Add initial <program> tag
		//xmlfile << indentation() << "<program>" << std::endl;
		try {
			xmlfile.write(indentation() + "<program>" + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Indent
		indentation_level++;

		// For each statement, accept
		for (var statement : program.statements)
		{
			statement.accept(this);
		}

		// Unindent
		indentation_level--;

		// Add closing tag
		//xmlfile << indentation() << "</program>" << std::endl;
		try {
			xmlfile.write(indentation() + "</program>" + "\n");
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}

	@Override
	public void visit(parser.ASTDeclarationNode decl)
	{

		// Add initial <decl> tag
		//xmlfile << indentation() << "<decl>" << std::endl;
		try {
			xmlfile.write(indentation() + "<decl>" + "\n");
		} catch (IOException e) {
			
			e.printStackTrace();
		}

		// Indent
		indentation_level++;

		// Add identifier
		//xmlfile << indentation() << "<id type = \"" + type_str(decl.type) + "\">" << decl.identifier << "</id>" << std::endl;
		try {
			xmlfile.write(indentation() + "<id type = \"" + type_str(decl.type) + "\">" + decl.identifier + "</id>" + "\n");
		} catch (IOException e) {
			
			e.printStackTrace();
		}

		// Expression tags
		decl.expr.accept(this);

		// Unindent
		indentation_level--;

		// Add closing tag
		//xmlfile << indentation() << "</decl>" << std::endl;
		try {
			xmlfile.write(indentation() + "</decl>" + "\n");
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}

	@Override
	public void visit(parser.ASTAssignmentNode assign)
	{

		// Add initial <assign> tag
		//xmlfile << indentation() << "<assign>" << std::endl;
		try {
			xmlfile.write(indentation() + "<assign>" + "\n");
		} catch (IOException e) {
			
			e.printStackTrace();
		}

		// Indent
		indentation_level++;

		// Add identifier
		//xmlfile << indentation() << "<id>" << assign.identifier << "</id>" << std::endl;
		try {
			xmlfile.write(indentation() + "<id>" + assign.identifier + "</id>"  + "\n");
		} catch (IOException e) {
			
			e.printStackTrace();
		}

		// Expression tags
		assign.expr.accept(this);

		// Unindent
		indentation_level--;

		// Add closing tag
		//xmlfile << indentation() << "</assign>" << std::endl;
		try {
			xmlfile.write(indentation() + "</assign>" + "\n");
		} catch (IOException e) {
			
			e.printStackTrace();
		}

	}

	@Override
	public void visit(parser.ASTPrintNode print)
	{

		// Add initial <print> tag
		//xmlfile << indentation() << "<print>" << std::endl;
		try {
			xmlfile.write(indentation() + "<print>" + "\n");
		} catch (IOException e) {
			
			e.printStackTrace();
		}

		// Indent
		indentation_level++;

		// Expression tags
		print.expr.accept(this);

		// Unindent
		indentation_level--;

		// Add closing tag
		try {
			xmlfile.write(indentation() + "</print>" + "\n");
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}

	@Override
	public void visit(parser.ASTReturnNode ret)
	{

		// Add initial <return> tag
		//xmlfile << indentation() << "<return>" << std::endl;
		try {
			xmlfile.write(indentation() + "<return>" + "\n");
		} catch (IOException e) {
			
			e.printStackTrace();
		}

		// Indent
		indentation_level++;

		// Expression tags
		ret.expr.accept(this);

		// Unindent
		indentation_level--;

		// Add closing tag
		//xmlfile << indentation() << "</return>" << std::endl;
		try {
			xmlfile.write(indentation() + "</return>" + "\n");
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}

	@Override
	public void visit(parser.ASTBlockNode block)
	{

		// Add initial <block> tag
		//xmlfile << indentation() << "<block>" << std::endl;
		try {
			xmlfile.write(indentation() + "<block>" + "\n");
		} catch (IOException e) {
			
			e.printStackTrace();
		}

		// Indent
		indentation_level++;

		// For each statement, accept
		for (var statement : block.statements)
		{
			statement.accept(this);
		}

		// Unindent
		indentation_level--;

		// Add closing tag
		//xmlfile << indentation() << "</block>" << std::endl;
		try {
			xmlfile.write(indentation() + "</block>" + "\n");
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}

	@Override
	public void visit(parser.ASTIfNode ifnode)
	{

		// Add initial <if> tag
		//xmlfile << indentation() << "<if>" << std::endl;
		try {
			xmlfile.write(indentation() + "<if>" + "\n");
		} catch (IOException e) {
			
			e.printStackTrace();
		}

		// Indent
		indentation_level++;

		// Add <condition> tag
		//xmlfile << indentation() << "<condition>" << std::endl;
		try {
			xmlfile.write(indentation() + "<condition>" + "\n");
		} catch (IOException e) {
			
			e.printStackTrace();
		}

		// Indent
		indentation_level++;

		// Expression
		ifnode.condition.accept(this);

		// Unindent
		indentation_level--;

		// Add closing tag
		//xmlfile << indentation() << "</condition>" << std::endl;
		try {
			xmlfile.write(indentation() + "</condition>" + "\n");
		} catch (IOException e) {
			
			e.printStackTrace();
		}

		// Add <if-block> tag
		//xmlfile << indentation() << "<if-block>" << std::endl;
		try {
			xmlfile.write(indentation() + "<if-block>" + "\n");
		} catch (IOException e) {
			
			e.printStackTrace();
		}

		// Indent
		indentation_level++;

		// If-block
		ifnode.if_block.accept(this);

		// Unindent
		indentation_level--;

		// Add closing tag
		//xmlfile << indentation() << "</if-block>" << std::endl;
		try {
			xmlfile.write(indentation() + "</if-block>" + "\n");
		} catch (IOException e) {
			
			e.printStackTrace();
		}

		// Unindent
		indentation_level--;

		// If there is an else-block
		if (ifnode.else_block != null)
		{

			// Add <else-block> tag
			//xmlfile << indentation() << "<else-block>" << std::endl;
			try {
				xmlfile.write(indentation() + "<else-block>" + "\n");
			} catch (IOException e) {
				
				e.printStackTrace();
			}

			// Indent
			indentation_level++;

			// Else-block
			ifnode.else_block.accept(this);

			// Unindent
			indentation_level--;

			// Add closing tag
			//xmlfile << indentation() << "</else-block>" << std::endl;
			try {
				xmlfile.write(indentation() + "</else-block>" + "\n");
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}

		// Add closing tag
		//xmlfile << indentation() << "</if>" << std::endl;
		try {
			xmlfile.write(indentation() + "</if>" + "\n");
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}

	@Override
	public void visit(parser.ASTWhileNode whilenode)
	{

		// Add initial <while> tag
		//xmlfile << indentation() << "<while>" << std::endl;
		try {
			xmlfile.write(indentation() + "<while>" + "\n");
		} catch (IOException e) {
			
			e.printStackTrace();
		}

		// Indent
		indentation_level++;

		// Add <condition> tag
		//xmlfile << indentation() << "<condition>" << std::endl;
		try {
			xmlfile.write(indentation() + "<condition>" + "\n");
		} catch (IOException e) {
			
			e.printStackTrace();
		}

		// Indent
		indentation_level++;

		// Expression
		whilenode.condition.accept(this);

		// Unindent
		indentation_level--;

		// Add closing tag
		//xmlfile << indentation() << "</condition>" << std::endl;
		try {
			xmlfile.write(indentation() + "</condition>" + "\n");
		} catch (IOException e) {
			
			e.printStackTrace();
		}

		// while-block
		whilenode.block.accept(this);

		// Unindent
		indentation_level--;

		// Add closing tag
		//xmlfile << indentation() << "</while>" << std::endl;
		try {
			xmlfile.write(indentation() + "</while>" + "\n");
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}

	@Override
	public void visit(parser.ASTFunctionDefinitionNode func)
	{

		// Add initial <func-def> tag
		//xmlfile << indentation() << "<func-def type = \"" + type_str(func.type) + "\">" << std::endl;
		try {
			xmlfile.write(indentation() + "<func-def type = \"" + type_str(func.type) + "\">" + "\n");
		} catch (IOException e) {
			
			e.printStackTrace();
		}

		// Indent
		indentation_level++;

		// Function identifier
		//xmlfile << indentation() << "<id>" + func.identifier + "</id>" << std::endl;
		try {
			xmlfile.write(indentation() + "<id>" + func.identifier + "</id>" + "\n");
		} catch (IOException e) {
			
			e.printStackTrace();
		}

		// For each parameter
		for (var param : func.parameters)
		{
			//xmlfile << indentation() << "<param type = \"" + type_str(param.second) + "\">" + param.first + "</param>" << std::endl;
			try {
				xmlfile.write(indentation() + "<param type = \"" + type_str(param.second) + "\">" + param.first + "</param>" + "\n");
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}

		// Function body
		func.block.accept(this);

		// Unindent
		indentation_level--;

		// Add closing tag
		//xmlfile << indentation() << "</func-def>" << std::endl;
		try {
			xmlfile.write(indentation() + "</func-def>" + "\n");
		} catch (IOException e) {
			
			e.printStackTrace();
		}

	}

	@Override
	public void visit(parser.ASTLiteralNode lit)
	{
		//String type = lit.getType();

		switch (lit.getType()) {

			case "Integer":
				// Add initial <int> tag
				//xmlfile << indentation() << "<int>";
				try {
					xmlfile.write(indentation() + "<int>");
				} catch (IOException e) {
					
					e.printStackTrace();
				}

				// Add value
				//xmlfile << String.valueOf(lit.val);
				try {
					xmlfile.write(indentation() + String.valueOf(lit.value));
				} catch (IOException e) {
					
					e.printStackTrace();
				}

				// Add closing tag
				//xmlfile << "</int>" << std::endl;
				try {
					xmlfile.write(indentation() + "</int>" + "\n");
				} catch (IOException e) {
					
					e.printStackTrace();
				}

			case "Float":
				// Add initial <float> tag
				//xmlfile << indentation() << "<float>";
				try {
					xmlfile.write(indentation() + "<float>");
				} catch (IOException e) {
					
					e.printStackTrace();
				}

				// Add value
				//xmlfile << String.valueOf(lit.value);
				try {
					xmlfile.write(indentation() + String.valueOf(lit.value));
				} catch (IOException e) {
					
					e.printStackTrace();
				}

				// Add closing tag
				//xmlfile << "</float>" << std::endl;
				try {
					xmlfile.write(indentation() + "</float>" + "\n");
				} catch (IOException e) {
					
					e.printStackTrace();
				}

			case "Boolean":
				// Add initial <bool> tag
				//xmlfile << indentation() << "<bool>";
				try {
					xmlfile.write(indentation() + "<bool>");
				} catch (IOException e) {
					
					e.printStackTrace();
				}

				// Add value
				//xmlfile << ((lit->val) ? "true" : "false");
				if (String.valueOf(lit.value)=="true"){
					try {
						xmlfile.write(indentation() + "true");
					} catch (IOException e) {
						
						e.printStackTrace();
					}
				}else{
					try {
						xmlfile.write(indentation() + "false");
					} catch (IOException e) {
						
						e.printStackTrace();
					}
				}
	
				// Add closing tag
				//xmlfile << "</bool>" << std::endl;
				try {
					xmlfile.write(indentation() + "</bool>" + "\n");
				} catch (IOException e) {
					
					e.printStackTrace();
				}

			case "String":
				// Add initial <string> tag
				//xmlfile << indentation() << "<string>";
				try {
					xmlfile.write(indentation() + "<string>");
				} catch (IOException e) {
					
					e.printStackTrace();
				}
			
				// Add value
				//xmlfile << lit.val;
				try {
					xmlfile.write(indentation() + lit.value);
				} catch (IOException e) {
					
					e.printStackTrace();
				}
			
				// Add closing tag
				//xmlfile << "</string>" << std::endl;
				try {
					xmlfile.write(indentation() + "</string>" + "\n");
				} catch (IOException e) {
					
					e.printStackTrace();
				}

			default:
				try {
					xmlfile.write(indentation() + "invalid Literal Node tyoe");
				} catch (IOException e) {
					
					e.printStackTrace();
				}
		}

	}
/* 
	@Override
	public void visit(parser.ASTLiteralNode<Integer> lit)
	{

		// Add initial <int> tag
		//xmlfile << indentation() << "<int>";
		xmlfile.write(indentation() + "<int>");

		// Add value
		//xmlfile << String.valueOf(lit.val);
		xmlfile.write(indentation() + String.valueOf(lit.val));

		// Add closing tag
		//xmlfile << "</int>" << std::endl;
		xmlfile.write(indentation() + "</int>" + "\n");
	}

	@Override
	public void visit(parser.ASTLiteralNode<Float> lit)
	{

		// Add initial <float> tag
		//xmlfile << indentation() << "<float>";
		xmlfile.write(indentation() + "<float>");

		// Add value
		//xmlfile << String.valueOf(lit.value);
		xmlfile.write(indentation() + String.valueOf(lit.value));

		// Add closing tag
		//xmlfile << "</float>" << std::endl;
		xmlfile.write(indentation() + "</float>" + "\n");
	}

	@Override
	public void visit(parser.ASTLiteralNode<Boolean> lit)
	{

		// Add initial <bool> tag
		//xmlfile << indentation() << "<bool>";
		xmlfile.write(indentation() + "<bool>");

		// Add value
		//xmlfile << ((lit->val) ? "true" : "false");
		if (lit.value){
			xmlfile.write(indentation() + "true");
		}else{
			xmlfile.write(indentation() + "false");
		}
	
		// Add closing tag
		//xmlfile << "</bool>" << std::endl;
		xmlfile.write(indentation() + "</bool>" + "\n");
	}


//C++ TO JAVA CONVERTER WARNING: The original C++ declaration of the following method implementation was not found:
	public void visit(parser.ASTLiteralNode<String> lit)
	{
    
		// Add initial <string> tag
		//xmlfile << indentation() << "<string>";
		xmlfile.write(indentation() + "<string>");
    
		// Add value
		//xmlfile << lit.val;
		xmlfile.write(indentation() + lit.value);
    
		// Add closing tag
		//xmlfile << "</string>" << std::endl;
		xmlfile.write(indentation() + "</string>" + "\n");
	}
 */
//C++ TO JAVA CONVERTER WARNING: The original C++ declaration of the following method implementation was not found:
	public void visit(parser.ASTBinaryExprNode bin)
	{
    
		// Add initial <bin> tag
		//mlfile << indentation() << "<bin op = \"" + xml_safe_op(bin.op) + "\">" << std::endl;
		try {
			xmlfile.write(indentation() + "<bin op = \"" + xml_safe_op(bin.identifier) + "\">" + "\n");
		} catch (IOException e) {
			
			e.printStackTrace();
		}
    
		// Indent
		indentation_level++;
    
		// Left node
		bin.left.accept(this);
    
		// Right node
		bin.right.accept(this);
    
		// Unindent
		indentation_level--;
    
		// Add closing tag
		//xmlfile << indentation() << "</bin>" << std::endl;
		try {
			xmlfile.write(indentation() + "</bin>" + "\n");
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}

//C++ TO JAVA CONVERTER WARNING: The original C++ declaration of the following method implementation was not found:
	public void visit(parser.ASTIdentifierNode id)
	{
    
		// Add initial <id> tag
		//xmlfile << indentation() << "<id>";
		try {
			xmlfile.write(indentation() + "<id>");
		} catch (IOException e) {
			
			e.printStackTrace();
		}
    
		// Add value
		//xmlfile << id.identifier;
		try {
			xmlfile.write(indentation() + id.identifier);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
    
		// Add closing tag
		//xmlfile << "</id>" << std::endl;
		try {
			xmlfile.write(indentation() + "</id>" + "\n");
		} catch (IOException e) {
			
			e.printStackTrace();
		}
    
	}

//C++ TO JAVA CONVERTER WARNING: The original C++ declaration of the following method implementation was not found:
	public void visit(parser.ASTUnaryExprNode un)
	{
    
		// Add initial <unary> tag
		//xmlfile << indentation() << "<unary op = \"" + un.unary_op + "\">" << std::endl;
		try {
			xmlfile.write(indentation() + "<unary op = \"" + un.unary_op + "\">" + "\n");
		} catch (IOException e) {
			
			e.printStackTrace();
		}
    
		// Indent
		indentation_level++;
    
		// Value
		un.expr.accept(this);
    
		// Unindent
		indentation_level--;
    
		// Add closing tag
		//xmlfile << indentation() << "</unary>" << std::endl;
		try {
			xmlfile.write(indentation() +  "</unary>" + "\n");
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}

//C++ TO JAVA CONVERTER WARNING: The original C++ declaration of the following method implementation was not found:
	public void visit(parser.ASTFunctionCallNode func)
	{
    
		// Add initial <func-call> tag
		//xmlfile << indentation() << "<func-call>" << std::endl;
		try {
			xmlfile.write(indentation() +  "<func-call>" + "\n");
		} catch (IOException e) {
			
			e.printStackTrace();
		}
    
		// Indent
		indentation_level++;
    
		// Function identifier
		//xmlfile << indentation() << "<id>" + func.identifier + "</id>" << std::endl;
		try {
			xmlfile.write(indentation() + "<id>" + func.identifier + "</id>" + "\n");
		} catch (IOException e) {
			
			e.printStackTrace();
		}
    
		// For each parameter
		for (var param : func.statements)
		{
			//xmlfile << indentation() << "<arg>" << std::endl;
			try {
				xmlfile.write(indentation() +  "<arg>" + "\n");
			} catch (IOException e) {
				
				e.printStackTrace();
			}
    
			// Indent
			indentation_level++;
    
			// Parameter
			param.accept(this);
    
			// Unindent
			indentation_level++;
    
			//xmlfile << indentation() << "</arg>" << std::endl;
			try {
				xmlfile.write(indentation() +  "</arg>" + "\n");
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
    
		// Unindent
		indentation_level--;
    
		// Add closing tag
		//xmlfile << indentation() << "</func-call>" << std::endl;
		try {
			xmlfile.write(indentation() +  "</func-call>" + "\n");
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}

//C++ TO JAVA CONVERTER WARNING: The original C++ declaration of the following method implementation was not found:
	public String type_str(parser.TYPE t)
	{
    
		switch (t)
		{
			case INT:
				return "int";
			case FLOAT:
				return "float";
			case BOOLEAN:
				return "bool";
			case STRING:
				return "string";
			default:
				throw new RuntimeException("Invalid type encountered in syntax tree when generating XML.");
		}
	}

//C++ TO JAVA CONVERTER WARNING: The original C++ declaration of the following method implementation was not found:
	public String xml_safe_op(String op)
	{
    
		if (op.equals("<"))
		{
			return "&lt;";
		}
		if (op.equals(">"))
		{
			return "&gt;";
		}
		if (op.equals("<="))
		{
			return "&lt;=";
		}
		if (op.equals(">="))
		{
			return "&gt;=";
		}
		return op;
	}

}