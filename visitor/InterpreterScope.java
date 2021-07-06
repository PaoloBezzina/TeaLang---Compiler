package visitor;

import visitor.*;
import java.util.*;
import java.io.*;

public class InterpreterScope {

	private HashMap<String, value_t> variable_symbol_table = new HashMap<String, value_t>();
	private HashMap<String, ArrayList<tangible.Function>> function_symbol_table = new HashMap<String, ArrayList<tangible.Function>>();

	public final boolean already_declared(String identifier) {
		return variable_symbol_table.containsKey(identifier);
	}

	public final boolean already_declared(String identifier, ArrayList<parser.TYPE> signature) {

		// If key is not present in hashmap
		if (!function_symbol_table.containsKey(identifier)) {
			return false;
		}

		// Check signature for each function in hashmap
		for (String key : function_symbol_table.keySet()) {

			for (var funcs : function_symbol_table.get(key)) {
				if (funcs.signature.equals(signature)) {
					return true;
				}

			}
		}

		return false;
	}

	public final void declare(String identifier, int int_value) {
		value_t value = new value_t();
		value.i = int_value;
		value.type = parser.TYPE.INT;
		variable_symbol_table.put(identifier, value);
	}

	public final void declare(String identifier, float real_value) {
		value_t value = new value_t();
		value.f = real_value;
		value.type = parser.TYPE.FLOAT;
		variable_symbol_table.put(identifier, value);
	}

	public final void declare(String identifier, boolean bool_value) {
		value_t value = new value_t();
		value.b = bool_value;
		value.type = parser.TYPE.BOOLEAN;
		variable_symbol_table.put(identifier, value);
	}

	public final void declare(String identifier, String string_value) {
		value_t value = new value_t();
		value.s = string_value;
		value.type = parser.TYPE.STRING;
		variable_symbol_table.put(identifier, value);
	}

	public final void declare(String identifier, ArrayList<parser.TYPE> signature, ArrayList<String> variable_names,
			parser.ASTBlockNode block) {
		var function = new tangible.Function(variable_names, signature, block);
		ArrayList<tangible.Function> functions = new ArrayList<tangible.Function>();
		functions.add(function);
		function_symbol_table.put(identifier, functions);
	}

	public final parser.TYPE type_of(String identifier) {
		if (already_declared(identifier)) {
			return variable_symbol_table.get(identifier).type;
		}
		try {
			throw new Exception("Cannot determine type of " + identifier);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public final value_t value_of(String identifier) {
		if (already_declared(identifier)) {
			return variable_symbol_table.get(identifier);
		}
		try {
			throw new Exception("Cannot determine value of " + identifier);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public final ArrayList<String> variable_names_of(String identifier, ArrayList<parser.TYPE> signature) {

		// If key is not present in hashmap
		if (!function_symbol_table.containsKey(identifier)) {
			try {
				throw new Exception("Function " + identifier + " with type" + signature.toString() + " was not found");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// Match given signature to function in hashmap
		// checking each signature
		for (var key : function_symbol_table.keySet()) {
			for (var func : function_symbol_table.get(key)) {
				if (func.signature.equals(signature)) {
					return func.variables;
				}
			}
		}
		return null;

	}

	public final parser.ASTBlockNode block_of(String identifier, ArrayList<parser.TYPE> signature) {

		if (!function_symbol_table.containsKey(identifier)) {
			try {
				throw new Exception("Function " + identifier + " with type" + signature.toString() + " was not found");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// checking each signature
		for (var key : function_symbol_table.keySet()) {
			for (var func : function_symbol_table.get(key)) {
				if (func.signature.equals(signature)) {
					return func.block;
				}
			}
		}
		return null;
	}

	public final ArrayList<String[]> variable_list() {

		ArrayList<String[]> list = new ArrayList<String[]>();

		for (var key : variable_symbol_table.keySet()) {
			var var_Keyword = variable_symbol_table.get(key);

			switch (var_Keyword.type) {
				case INT:
					list.add(new String[] { key, "int", Integer.toString(var_Keyword.i) });
					break;
				case FLOAT:
					list.add(new String[] { key, "float", Integer.toString((int) (var_Keyword.f)) });
					break;
				case BOOLEAN:
					list.add(new String[] { key, "BOOLEAN", Boolean.toString(var_Keyword.b) });
					break;
				case STRING:
					list.add(new String[] { key, "string", (var_Keyword.s) });
					break;
			}
		}

		return list;
	}

}