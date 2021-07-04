package visitor;

import visitor.*;
import java.io.*;
import java.util.*;
import parser.*;
import parser.visitor.*;

public class SemanticScope {

	private TreeMap<String, tangible.Pair<parser.TYPE, Integer>> variable_symbol_table = new TreeMap<String, tangible.Pair<parser.TYPE, Integer>>();
	//private std::multimap<String, std::tuple<parser.TYPE, ArrayList<parser.TYPE>, Integer>> function_symbol_table = new std::multimap<String, std::tuple<parser.TYPE, ArrayList<parser.TYPE>, Integer>>();
	private HashMap<String, tangible.Tuple<parser.TYPE, ArrayList<parser.TYPE>, Integer>> function_symbol_table = new HashMap<String, tangible.Tuple<parser.TYPE, ArrayList<parser.TYPE>, Integer>>();

	public final boolean already_declared(String identifier) {
		return variable_symbol_table.containsKey(identifier);
	}

	public final boolean already_declared(String identifier, ArrayList<parser.TYPE> signature)
	{

		//var funcs = function_symbol_table.equal_range(identifier);
		/* 
		 * Hawnekk nahseb qed jistenna iterator fuq function_symbol_table, mentri kif qiehda bhalissa qed ittih il-value tal-key 'identifier'
		 */
		var funcs = function_symbol_table.get(identifier);

		// If key is not present in multimap
		//if (std::distance(funcs.first, funcs.second) == 0)
		if (function_symbol_table.containsKey(funcs.first))
		{
			return false;
		}

		// Check signature for each function in multimap
		for (var i = funcs.first; i != funcs.second; i++)
		{
			if (std::get<1>(i.second) == signature)
			{
				return true;
			}
		}

		// Function with matching signature not found
		return false;
	}

	public final void declare(String identifier, parser.TYPE type, int line_number) {
		variable_symbol_table.put(identifier, new tangible.Pair<parser.TYPE, Integer>(type, line_number));
	}

	public final void declare(String identifier, parser.TYPE type, ArrayList<parser.TYPE> signature, int line_number)
	{

		//function_symbol_table.insert(std::make_pair(identifier, std::make_tuple(type, signature, line_number)));
		function_symbol_table.put(identifier, new tangible.Tuple(type, signature, line_number));
	}

	public final parser.TYPE type(String identifier) {

		if (already_declared(identifier)) {
			return variable_symbol_table.get(identifier).first;
		}

		throw new RuntimeException("Something went wrong when determining the type of '" + identifier + "'.");
	}

	public final parser.TYPE type(String identifier, ArrayList<parser.TYPE> signature)
	{

		var funcs = function_symbol_table.equal_range(identifier);

		// If key is not present in multimap
		if (std::distance(funcs.first, funcs.second) == 0)
		{
			throw new RuntimeException("Something went wrong when determining the type of '" + identifier + "'.");
		}

		// Check signature for each
		for (var i = funcs.first; i != funcs.second; i++)
		{
			if (std::get<1>(i.second) == signature)
			{
				return std::get<0>(i.second);
			}
		}

		// Function with matching signature not found
		throw new RuntimeException("Something went wrong when determining the type of '" + identifier + "'.");
	}

	public final int declaration_line(String identifier)
	{

		if (already_declared(identifier))
		{
			return variable_symbol_table.get(identifier).second;
		}

		throw new RuntimeException("Something went wrong when determining the line number of '" + identifier + "'.");
	}

	public final int declaration_line(String identifier, ArrayList<parser.TYPE> signature)
	{

		var funcs = function_symbol_table.equal_range(identifier);

		// If key is not present in multimap
		if (std::distance(funcs.first, funcs.second) == 0)
		{
			throw new RuntimeException("Something went wrong when determining the line number of '" + identifier + "'.");
		}

		// Check signature for each
		for (var i = funcs.first; i != funcs.second; i++)
		{
			if (std::get<1>(i.second) == signature)
			{
				return std::get<2>(i.second);
			}
		}

		// Function with matching signature not found
		throw new RuntimeException("Something went wrong when determining the line number of '" + identifier + "'.");
	}

	public final ArrayList<tangible.Pair<String, String>> function_list()
	{

		ArrayList<tangible.Pair<String, String>> list = new ArrayList<tangible.Pair<String, String>>();

		for (var func = function_symbol_table.begin(), last = function_symbol_table.end(); func != last; func = function_symbol_table.upper_bound(func.first))
		{

			String func_name = func.first + "(";
			boolean has_params = false;
			for (var param : std::get<1>(func.second))
			{
				has_params = true;
				func_name += type_str(param) + ", ";
			}
			//func_name.pop_back(); // remove last whitespace
			func_name = pop_back(func_name);
			//func_name.pop_back(); // remove last comma
			func_name = pop_back(func_name);
			func_name += ")";

			list.emplace_back(new tangible.Pair<String, String>(func_name, type_str(std::get<0>(func.second))));
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
}