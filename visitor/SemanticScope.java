package visitor;

import visitor.*;
import java.io.*;
import java.util.*;
import parser.*;
import parser.visitor.*;

public class SemanticScope {

	private TreeMap<String, tangible.Pair<parser.TYPE, Integer>> variable_symbol_table = new TreeMap<String, tangible.Pair<parser.TYPE, Integer>>();
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

		// Function with matching signature not found
		return false;
	}

	public final void declare(String identifier, parser.TYPE type, int line_number) {
		variable_symbol_table.put(identifier, new tangible.Pair<parser.TYPE, Integer>(type, line_number));
	}

	public final void declare(String identifier, parser.TYPE type, ArrayList<parser.TYPE> signature, int line_number) {

		if (!function_symbol_table.containsKey(identifier)) {
			ArrayList<tangible.Function> lst = new ArrayList<>();
			lst.add(new tangible.Function(type, signature, line_number));
			function_symbol_table.put(identifier, lst);
		} else {
			function_symbol_table.get(identifier).add(new tangible.Function(type, signature, line_number));
		}

	}

	public final parser.TYPE type(String identifier) {

		if (already_declared(identifier)) {
			return variable_symbol_table.get(identifier).first;
		}

		throw new RuntimeException("Something went wrong when determining the type of '" + identifier + "'.");
	}

	public final parser.TYPE type(String identifier, ArrayList<parser.TYPE> signature) {

		var funcs = function_symbol_table.get(identifier);

		// If key is not present in hashmap
		if (!function_symbol_table.containsKey(identifier)) {
			throw new RuntimeException("Something went wrong when determining the type of '" + identifier + "'.");
		}

		// Check signature for each
		for (var func : funcs) {
			if (func.signature.equals(signature)) {
				return func.type;
			}
		}

		// Function with matching signature not found
		throw new RuntimeException("Something went wrong when determining the type of '" + identifier + "'.");
	}

	public final int declaration_line(String identifier) {

		if (already_declared(identifier)) {
			return variable_symbol_table.get(identifier).second;
		}

		throw new RuntimeException("Something went wrong when determining the line number of '" + identifier + "'.");
	}

	public final int declaration_line(String identifier, ArrayList<parser.TYPE> signature) {

		// If key is not present in hashmap
		if (!function_symbol_table.containsKey(identifier)) {
			throw new RuntimeException(
					"Something went wrong when determining the line number of '" + identifier + "'.");
		}

		// Check signature for each
		for (var key : function_symbol_table.keySet()) {
			for (var func : function_symbol_table.get(key)) {
				if (func.signature.equals(signature)) {
					return func.line_number;
				}
			}
		}

		// Function with matching signature not found
		throw new RuntimeException("Something went wrong when determining the line number of '" + identifier + "'.");
	}

	public final ArrayList<tangible.Pair<String, TYPE>> function_list() {

		ArrayList<tangible.Pair<String, TYPE>> list = new ArrayList<tangible.Pair<String, TYPE>>();

		for (String key : function_symbol_table.keySet()) {
			ArrayList<tangible.Function> value = function_symbol_table.get(key);
			for (var func : value) {
				String func_name = key + "(";

				for (int i = 0; i<func.signature.size(); i++) {
                    func_name += func.signature.get(i) +", ";

				}
				// remove last whitespace
				func_name = pop_back(func_name);
				// remove last comma
				func_name = pop_back(func_name);
				func_name += ")";

				list.add(new tangible.Pair<String, TYPE>(func_name, func.type));
			}
		}

		return list;
	}

	public static SemanticScope back(ArrayList<SemanticScope> scope) {
		int index = scope.size() - 1;
		// Access last element by passing index
		return scope.get(index);
	}

	public static String pop_back(String str) {
		return removeLastChars(str, 1);
	}

	public static String removeLastChars(String str, int chars) {
		if (str.length() > 0) {
			return str.substring(0, str.length() - chars);
		}
		return str;
	}

	public static SemanticScope equal_range(ArrayList<SemanticScope> scope) {
		int index = scope.size() - 1;
		// Access last element by passing index
		return scope.get(index);
	}

	public String type_str(parser.TYPE t) {

		switch (t) {
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
}