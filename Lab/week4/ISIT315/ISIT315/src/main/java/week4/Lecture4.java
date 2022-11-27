https://powcoder.com
代写代考加微信 powcoder
Assignment Project Exam Help
Add WeChat powcoder
https://powcoder.com
代写代考加微信 powcoder
Assignment Project Exam Help
Add WeChat powcoder
//-----------------------------------------------------------------------
// This is written by a cool lecturer ;)
// SCIT, UOW
// ISIT315, Spring 2020
//-----------------------------------------------------------------------

package week4;

import java.io.File;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFactory;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.query.ResultSetRewindable;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;
import org.apache.jena.util.FileManager;

public class Lecture4 {

	public static void main(String[] args) {
		// Define RDF files
		final String TEMP_DATA = "temp.ttl";

		// Define Query files
		final String TEMP_Q = "temp_Q.sparql";

		// Select RDF and Query
		String Data = TEMP_DATA;
		String Query = TEMP_Q;

		String Q1 = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator
				+ "resources" + File.separator + "queries" + File.separator + Query;

		// local execution
		Model model = RDFDataMgr.loadModel(System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator
				+ "resources" + File.separator + "data" + File.separator + Data);
		executeQuery_Local(Q1, model, true);
	}

	public static void executeQuery_Local(String query_string_or_file, Model model, boolean fromFile) {
		String query = query_string_or_file;
		if (fromFile)
			query = QueryFactory.read(query_string_or_file).toString();
		if (query.contains("SELECT"))
			select_local(query, model);
		else if (query.contains("CONSTRUCT")) {
			Model graph = construct_local(query, model);
			select_local("SELECT * {?s ?p ?o .}", graph);
			// Write a model in Turtle syntax, default style (pretty printed)
			RDFDataMgr.write(System.out, graph, Lang.TURTLE);
		} else if (query.contains("ASK")) {
			System.out.println(ask_local(query, model));
		} else if (query.contains("DESCRIBE")) {
			Model graph = describe_local(query, model);
			select_local("SELECT * {?s ?p ?o .}", graph);
			// Write a model in Turtle syntax, default style (pretty printed)
			RDFDataMgr.write(System.out, graph, Lang.TURTLE);
		}
	}

	// ---------------------------------------------------------------------------------------
	public static ResultSet select_local(String query_string, Model model) {
		ResultSet results = null;
		Query query = QueryFactory.create(query_string);
		QueryExecution qexec = QueryExecutionFactory.create(query, model);
		try {
			results = qexec.execSelect();
		} finally {
			if (results != null)
				ResultSetFormatter.out(System.out, results, query);
			else
				System.out.println("no results were found!");
			qexec.close();
		}
		return results;
	}

	// -----------------------------------------------------------------------------------------
	public static Model construct_local(String query_string, Model model) {
		Model graph = null;
		Query query = QueryFactory.create(query_string);
		QueryExecution qexec = QueryExecutionFactory.create(query, model);
		try {
			graph = qexec.execConstruct();
		} finally {
			qexec.close();
		}
		return graph;
	}

	// ------------------------------------------------------------------------------------------
	public static Boolean ask_local(String query_string, Model model) {
		Boolean results = null;
		Query query = QueryFactory.create(query_string);
		QueryExecution qexec = QueryExecutionFactory.create(query, model);
		try {
			results = qexec.execAsk();
		} finally {
			qexec.close();
		}
		return results;
	}

	// ------------------------------------------------------------------------------------------
	public static Model describe_local(String query_string, Model model) {
		Model results = null;
		Query query = QueryFactory.create(query_string);
		QueryExecution qexec = QueryExecutionFactory.create(query, model);
		try {
			results = qexec.execDescribe();
		} finally {
			qexec.close();
		}
		return results;
	}
}