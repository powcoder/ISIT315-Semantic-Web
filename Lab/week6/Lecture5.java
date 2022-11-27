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

package week5;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

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
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;
import org.apache.jena.util.FileManager;

public class Lecture5 {

	public static void main(String[] args) {
		String PATH_DATA_LOCAL = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main"
				+ File.separator + "resources" + File.separator + "data" + File.separator + "temp.ttl";
		String PATH_DATA_REMOTE = "http://dig.csail.mit.edu/2008/webdav/timbl/foaf.rdf";
		String PATH_QUERY = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main"
				+ File.separator + "resources" + File.separator + "queries" + File.separator + "temp_Q.sparql";
		String ENDPOINT = "http://dbpedia.org/sparql";
		// select data source
		String data = PATH_DATA_REMOTE;
		// select query form file or directly enter
		String query = QueryFactory.read(PATH_QUERY).toString();

		try {
			Model model = ModelFactory.createDefaultModel();

			// read data
			model.read(data);
			//model.write(System.out, "Turtle");
			// read query
			executeQuery(query, false, model, "");
			executeQuery(query, true, null, ENDPOINT);
		} catch (Exception ex) {
			System.out.println("Faild to load Data/Query: " + ex.toString());
		}
	}

	public static void executeQuery(String query, boolean remote, Model model, String endPoint) {

		if (query.contains("SELECT")) {
			if (remote)
				select_remote(query, endPoint);
			else
				select_local(query, model);
		} else if (query.contains("CONSTRUCT")) {
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

	// --------------------------------------------------------------------------------------
	public static ResultSet select_remote(String query_string, String ENDPOINT) {
		ResultSet results = null;
		Query query = QueryFactory.create(query_string);
		QueryExecution qexec = QueryExecutionFactory.sparqlService(ENDPOINT, query);
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