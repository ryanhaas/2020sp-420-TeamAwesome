// Package name
package tests;

import static org.junit.Assert.assertEquals;
// System imports
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import org.junit.Test;

// Local imports
import model.UMLClassManager;
import model.UMLRelationship;

/**
 * Class for running JUnit tests on ClassManager
 * 
 * @author Anthony
 */
public class ClassManagerTests {
	/*
	 * Create a new instance of ClassManager with an empty list 
	 */
	@Test
	public void baseInstance() {
	UMLClassManager manager = new UMLClassManager();
	assertTrue("List is empty", manager.empty());
	}
	
	/*
	 * Creates a new manager and adds classes
	 */
	@Test
	public void addTest() {
		UMLClassManager manager = new UMLClassManager();
		manager.addClass("a");
		manager.addClass("n");
		manager.addClass("t");
		assertEquals("List is populated", manager.listClasses(), "[a, n, t]");
		int result = manager.addClass("t");
		assertEquals("Class already exists", result, 200);
	}
	
	/*
	 * Creates a new manager, adds classes, and removes them
	 */
	@Test
	public void removeTest() {
		UMLClassManager manager = new UMLClassManager();
		int result = manager.removeClass("a");
		assertEquals("There is no size", result, 201);
		manager.addClass("a");
		manager.addClass("n");
		manager.addClass("t");
		manager.removeClass("a");
		manager.removeClass("n");
		manager.removeClass("t");
		assertEquals("List is empty", manager.listClasses(), "[]");
		result = manager.removeClass("c");
		assertEquals("Class does not exist", result, 201);
		result = manager.addClass("c");
		assertEquals("class removed", result, 0);
	}
	
	/*
	 * Creates manager then adds and removes methods from classes in list.
	 */
	@Test
	public void methodTests() {
		UMLClassManager manager = new UMLClassManager();
		int result = manager.addMethods("class1", "method");
		assertEquals("Class does not exist", result, 403);
		manager.addClass("c");
		result = manager.addMethods("c", "method");
		assertEquals("Success", result, 0);
		result = manager.addMethods("c", "method");
		assertEquals("Method exists already", result, 402);
		manager.removeClass("c");
		result = manager.removeMethods("c", "method");
		assertEquals("Class does not exist", result, 403);
		manager.addClass("d");
		result = manager.removeMethods("d", "method");
		assertEquals("Method does not exist", result, 406);
		manager.addMethods("d", "method");
		String r = manager.listMethods("d")[0].toString();
		assertEquals("method is the only method", "[method]", r);
		result = manager.removeMethods("d", "method");
		assertEquals("Success", result, 0);
	}
	
	/*
	 * Creates manager then adds and removes fields from classes in list.
	 */
	@Test
	public void fieldTests() {
		UMLClassManager manager = new UMLClassManager();
		int result = manager.addFields("class1", "field");
		assertEquals("Class does not exist", result, 403);
		manager.addClass("c");
		result = manager.addFields("c", "field");
		assertEquals("Success", result, 0);
		result = manager.addFields("c", "field");
		assertEquals("Field exists already", result, 404);
		manager.removeClass("c");
		result = manager.removeFields("c", "field");
		assertEquals("Class does not exist", result, 403);
		manager.addClass("d");
		result = manager.removeFields("d", "field");
		assertEquals("Field does not exist", result, 405);
		manager.addFields("d", "field");
		String r = manager.listFields("d")[0].toString();
		assertEquals("field is the only field", "[field]", r);
		result = manager.removeFields("d", "field");
		assertEquals("Success", result, 0);
	}
	
	/*
	 * Creates manager then adds classes, fields, and methods to be changed.
	 */
	@Test
	public void EditTests() {
		UMLClassManager manager = new UMLClassManager();
		int result = manager.editClass("e", "f");
		assertEquals("Selected class does not exist", result, 401);
		result = manager.editFields("e", "f", "g");
		assertEquals("Selected class does not exist", result, 403);
		result = manager.editMethods("e", "f", "g");
		assertEquals("Selected class does not exist", result, 403);
		manager.addClass("e");
		result = manager.editClass("e", "f");
		assertEquals("Success", result, 0);
		manager.addClass("h");
		result = manager.editClass("f", "h");
		assertEquals("New class name already exists", result, 400);
		result = manager.editFields("h", "f", "g");
		assertEquals("Selected field does not exist", result, 405);
		result = manager.editMethods("h", "f", "g");
		assertEquals("Selected method does not exist", result, 406);
		manager.addFields("h", "f");
		manager.addMethods("h", "m");
		result = manager.editFields("h", "f", "g");
		assertEquals("Success", result, 0);
		result = manager.editMethods("h", "m", "b");
		assertEquals("Succcess", result, 0);
		manager.addFields("h", "z");
		manager.addMethods("h", "a");
		result = manager.editFields("h", "g", "z");
		assertEquals("Selected new name already exists", result, 404);
		result = manager.editMethods("h", "b", "a");
		assertEquals("Selected new name already exists", result, 402);
	}
	
	/*
	 * Creates manager to test relationships between classes.
	 */
	@Test
	public void relationshipTests() {
		UMLClassManager manager = new UMLClassManager();
		int result = manager.addRelationship("class1", "method");
		assertEquals("One or more of the classes do not exist", result, 107);
		manager.addClass("c");
		manager.addClass("d");
		result = manager.addRelationship("c", "d");
		assertEquals("Success", result, 0);
		result = manager.addRelationship("c", "d");
		assertEquals("Relationship exists already", result, 106);
		String r = manager.listRelationships("c")[0].toString();
		assertEquals("C is related to D", "[c" + UMLRelationship.DELIMITER + "d]", r);
		manager.removeClass("c");
		result = manager.removeRelationship("c", "d");
		assertEquals("One or more classes do not exist", result, 107);
		manager.addClass("c");
		result = manager.removeRelationship("c", "d");
		assertEquals("Relationship does not exist", result, 108);
		manager.addMethods("d", "method");
		manager.addRelationship("c", "d");
		result = manager.removeRelationship("c", "d");
		assertEquals("Success", result, 0);
	}
	
	/*
	 * Creates list of classes then converts to json then parses back to String
	 */
	@Test
	public void convertParseTest() {
		UMLClassManager manager = new UMLClassManager();
		manager.addClass("b");
		manager.addClass("i");
		manager.addClass("g");
		String j = manager.convertToJSON();
		
		UMLClassManager parse = new UMLClassManager();
		int result = parse.parseJSON(j);
		assertEquals("List is the same as start", result, 0);
	}
}
