// System imports
import org.junit.Test;

import controller.GUIController;
import core.ErrorHandler;
import model.UMLClass;
import model.UMLClassManager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;

// Local imports
import views.GUIView;
import views.components.testable.TestableFileChooser;
import views.components.testable.TestableMenuItem;
import views.components.testable.TestableOptionPane;

/**
 * Class to run JUnit tests on the GUI
 * 
 * @author Ryan
 *
 */
public class GUITest {
	/**
	 * Create the GUI environment and the environment is initialized
	 */
	@Test
	public void baseInitialization() {
		UMLClassManager model = new UMLClassManager();
		GUIView gui = new GUIView(new GUIController(model), model, false);
		assertTrue("Controller not null", gui.getController() != null);
		assertTrue("Diagram exists", gui.getDiagram() != null);
	}
	
	/**
	 * Ensure the initialization of the mouse menu and its menu items
	 */
	@Test
	public void mouseMenuInitializations() {
		UMLClassManager model = new UMLClassManager();
		GUIView gui = new GUIView(new GUIController(model), model, false);
		JComponent mouseMenu = gui.getComponent("Mouse Menu");
		assertTrue("Main mouse menu exists", mouseMenu != null);
		assertTrue("Main mouse menu is popup menu", mouseMenu instanceof JPopupMenu);
		
		// Test MenuItems' initialization
		Component[] mouseMenuChildren = mouseMenu.getComponents();
		assertTrue("Main mouse menu not empty", mouseMenuChildren.length != 0);
		assertEquals("Main mouse menu number of items", 5, mouseMenuChildren.length);
		assertEquals("Main mouse menu first child class add", "mouseAddClass" , ((TestableMenuItem)mouseMenuChildren[0]).getName());
		assertTrue("Main mouse menu second child separator", mouseMenuChildren[1] == null);
		assertEquals("Main mouse menu third child save", "mouseSave" , ((TestableMenuItem)mouseMenuChildren[2]).getName());
		assertEquals("Main mouse menu fourth child load", "mouseLoad" , ((TestableMenuItem)mouseMenuChildren[3]).getName());
		assertEquals("Main mouse menu fifth child load", "mouseExport" , ((TestableMenuItem)mouseMenuChildren[4]).getName());
	}
	
	/**
	 * Ensure the initialization of the class mouse menu and its items
	 */
	@Test
	public void classMenuInitialization() {
		UMLClassManager model = new UMLClassManager();
		GUIView gui = new GUIView(new GUIController(model), model, false);
		JComponent classMenu = gui.getComponent("Class Menu");
		assertTrue("Class menu exists", classMenu != null);
		assertTrue("Class menu is popup menu", classMenu instanceof JPopupMenu);

		// Test MenuItems' initialization
		Component[] classMenuChildren = classMenu.getComponents();
		assertTrue("Class menu not empty", classMenuChildren.length != 0);
		assertEquals("Class menu number of items", 13, classMenuChildren.length);
		assertEquals("Class menu first child", "classRemoveClass" , ((TestableMenuItem)classMenuChildren[0]).getName());
		assertEquals("Class menu second child", "classEditClass", ((TestableMenuItem)classMenuChildren[1]).getName());
		assertTrue("Class menu third child separator", classMenuChildren[2] instanceof JSeparator);
		assertEquals("Class menu fourth child", "classAddField" , ((TestableMenuItem)classMenuChildren[3]).getName());
		assertEquals("Class menu child 5", "classRemoveField" , ((TestableMenuItem)classMenuChildren[4]).getName());
		assertEquals("Class menu child 6", "classEditField" , ((TestableMenuItem)classMenuChildren[5]).getName());
		assertTrue("Class menu child 7 separator", classMenuChildren[6] instanceof JSeparator);
		assertEquals("Class menu child 8", "classAddMethod" , ((TestableMenuItem)classMenuChildren[7]).getName());
		assertEquals("Class menu child 9", "classRemoveMethod" , ((TestableMenuItem)classMenuChildren[8]).getName());
		assertEquals("Class menu child 10", "classEditMethod" , ((TestableMenuItem)classMenuChildren[9]).getName());
		assertTrue("Class menu child 11 separator", classMenuChildren[10] instanceof JSeparator);
		assertEquals("Class menu child 12", "classAddRelationship" , ((TestableMenuItem)classMenuChildren[11]).getName());
		assertEquals("Class menu child 13", "classRemoveRelationship" , ((TestableMenuItem)classMenuChildren[12]).getName());
	}
	
	/**
	 * Test the MainMenu and it's items
	 */
	@Test
	public void mainMenuInitialization() {
		UMLClassManager model = new UMLClassManager();
		GUIView gui = new GUIView(new GUIController(model), model, false);
		JComponent MainMenu = gui.getComponent("mainMenuBar");
		assertTrue("Main menu bar exists", MainMenu != null);
		assertTrue("Main menu is menubar", MainMenu instanceof JMenuBar);
		
		//Test the MainMenu initialization of components
		assertTrue("Main File menu not empty", ((JMenu)gui.getComponent("mainFile")).getItemCount() != 0);
		assertEquals("Main File menu number of items", 8, ((JMenu)gui.getComponent("mainFile")).getItemCount());
		assertEquals("Main File menu first child", "mainAddClass" , ((JMenu)gui.getComponent("mainFile")).getItem(0).getName());
		// JSeparator's seem to be represented as null menuitems
		assertTrue("Main File menu second child separator", ((JMenu)gui.getComponent("mainFile")).getItem(1) == null);
		assertEquals("Main File menu third child", "mainSave", ((JMenu)gui.getComponent("mainFile")).getItem(2).getName());
		assertEquals("Main File menu fourth child", "mainLoad" , ((JMenu)gui.getComponent("mainFile")).getItem(3).getName());
		assertTrue("Main File menu fifth child separator", ((JMenu)gui.getComponent("mainFile")).getItem(4) == null);
		assertEquals("Main File menu sixth child", "mainExport" , ((JMenu)gui.getComponent("mainFile")).getItem(5).getName());
		assertTrue("Main File menu seventh child separator", ((JMenu)gui.getComponent("mainFile")).getItem(6) == null);
		assertEquals("Main File menu eigth child", "mainResize" , ((JMenu)gui.getComponent("mainFile")).getItem(7).getName());
		
		assertTrue("Main action menu not empty", ((JMenu)gui.getComponent("mainActions")).getItemCount() != 0);
		assertEquals("Main action menu number of items", 13, ((JMenu)gui.getComponent("mainActions")).getItemCount());
		assertEquals("Main menu actions child 1", "mainRemoveClass" , ((JMenu)gui.getComponent("mainActions")).getItem(0).getName());
		assertEquals("Main menu actions child 2", "mainEditClass" , ((JMenu)gui.getComponent("mainActions")).getItem(1).getName());
		// JSeparator's seem to be represented as null menuitems
		assertTrue("Main menu actions child 3 separator", ((JMenu)gui.getComponent("mainActions")).getItem(2) == null);
		assertEquals("Main menu actions child 4", "mainAddField" , ((JMenu)gui.getComponent("mainActions")).getItem(3).getName());
		assertEquals("Main menu actions child 5", "mainRemoveField" , ((JMenu)gui.getComponent("mainActions")).getItem(4).getName());
		assertEquals("Main menu actions child 6", "mainEditField" , ((JMenu)gui.getComponent("mainActions")).getItem(5).getName());
		// JSeparator's seem to be represented as null menuitems
		assertTrue("Main menu actions child 7 separator", ((JMenu)gui.getComponent("mainActions")).getItem(6) == null);
		assertEquals("Main menu actions child 4", "mainAddMethod" , ((JMenu)gui.getComponent("mainActions")).getItem(7).getName());
		assertEquals("Main menu actions child 5", "mainRemoveMethod" , ((JMenu)gui.getComponent("mainActions")).getItem(8).getName());
		assertEquals("Main menu actions child 6", "mainEditMethod" , ((JMenu)gui.getComponent("mainActions")).getItem(9).getName());
		// JSeparator's seem to be represented as null menuitems
		assertTrue("Main menu actions child 7 separator", ((JMenu)gui.getComponent("mainActions")).getItem(10) == null);
		assertEquals("Main menu actions child 8", "mainAddRelationship" , ((JMenu)gui.getComponent("mainActions")).getItem(11).getName());
		assertEquals("Main menu actions child 9", "mainRemoveRelationship" , ((JMenu)gui.getComponent("mainActions")).getItem(12).getName());
	}
	
	/**
	 * Test the addition of classes to the GUI
	 */
	@Test
	public void addClass() {
		UMLClassManager model = new UMLClassManager();
		GUIView gui = new GUIView(new GUIController(model), model, false);
		
		gui.setOptionPane(new TestableOptionPane("myclass"));
		((TestableMenuItem)gui.getComponent("mouseAddClass")).doClick();
		assertEquals("Add class normal exit code", 0, ErrorHandler.LAST_CODE);
		assertNotEquals("Add class normal exist check", null, model.getClass("myclass"));
		assertEquals("Number of classes after single add", 1, model.getClassNames().length);
		
		gui.setOptionPane(new TestableOptionPane("secondclass"));
		((TestableMenuItem)gui.getComponent("mouseAddClass")).doClick();
		assertEquals("Add class normal exit code 2", 0, ErrorHandler.LAST_CODE);
		assertNotEquals("Add class normal exist check 2", null, model.getClass("secondclass"));
		assertEquals("Number of classes after second add", 2, model.getClassNames().length);
		
		gui.setOptionPane(new TestableOptionPane("3*29"));
		((TestableMenuItem)gui.getComponent("mouseAddClass")).doClick();
		assertNotEquals("Add class invalid name error code", 0, ErrorHandler.LAST_CODE);
		assertEquals("Add class invalid not exists check", null, model.getClass("3*29"));
		assertEquals("Number of classes after invalid add", 2, model.getClassNames().length);
		
		gui.setOptionPane(new TestableOptionPane("myclass"));
		((TestableMenuItem)gui.getComponent("mouseAddClass")).doClick();
		assertNotEquals("Add class duplicate exit code", 0, ErrorHandler.LAST_CODE);
		assertNotEquals("Add class duplicate exist check", null, model.getClass("myclass"));
		assertEquals("Number of classes after single add", 2, model.getClassNames().length);
	}
	
	/**
	 * Test the addition of classes to the MainMenu GUI
	 */
	@Test
	public void mainMenuAddClass() {
		UMLClassManager model = new UMLClassManager();
		GUIView gui = new GUIView(new GUIController(model), model, false);
		
		gui.setOptionPane(new TestableOptionPane("myclass"));
		((TestableMenuItem)gui.getComponent("mainAddClass")).doClick();
		assertEquals("Add class normal exit code", 0, ErrorHandler.LAST_CODE);
		assertNotEquals("Add class normal exist check", null, model.getClass("myclass"));
		assertEquals("Number of classes after single add", 1, model.getClassNames().length);
		
		gui.setOptionPane(new TestableOptionPane("secondclass"));
		((TestableMenuItem)gui.getComponent("mainAddClass")).doClick();
		assertEquals("Add class normal exit code 2", 0, ErrorHandler.LAST_CODE);
		assertNotEquals("Add class normal exist check 2", null, model.getClass("secondclass"));
		assertEquals("Number of classes after second add", 2, model.getClassNames().length);
		
		gui.setOptionPane(new TestableOptionPane("3*29"));
		((TestableMenuItem)gui.getComponent("mainAddClass")).doClick();
		assertNotEquals("Add class invalid name error code", 0, ErrorHandler.LAST_CODE);
		assertEquals("Add class invalid not exists check", null, model.getClass("3*29"));
		assertEquals("Number of classes after invalid add", 2, model.getClassNames().length);
		
		gui.setOptionPane(new TestableOptionPane("myclass"));
		((TestableMenuItem)gui.getComponent("mainAddClass")).doClick();
		assertNotEquals("Add class duplicate exit code", 0, ErrorHandler.LAST_CODE);
		assertNotEquals("Add class duplicate exist check", null, model.getClass("myclass"));
		assertEquals("Number of classes after single add", 2, model.getClassNames().length);
	}
	
	/**
	 * Test the removal of classes from the GUI
	 */
	@Test
	public void removeClass() {
		UMLClassManager model = new UMLClassManager();
		GUIController controller = new GUIController(model);
		GUIView gui = new GUIView(controller, model, false);
		
		// Assuming add class works
		controller.addClass("myclass");
		controller.addClass("secondclass");
		controller.addClass("thirdclass");
		
		assertEquals("Number of classes prior to remove", 3, model.getClassNames().length);
		
		gui.setOptionPane(new TestableOptionPane("myclass"));
		((TestableMenuItem)gui.getComponent("classRemoveClass")).doClick();
		assertEquals("Remove class normal exit code", 0, ErrorHandler.LAST_CODE);
		assertEquals("Remove class normal exist check", null, model.getClass("myclass"));
		assertEquals("Number of classes after normal remove", 2, model.getClassNames().length);
		
		gui.setOptionPane(new TestableOptionPane("secondclass"));
		((TestableMenuItem)gui.getComponent("classRemoveClass")).doClick();
		assertEquals("Remove class normal exit code 2", 0, ErrorHandler.LAST_CODE);
		assertEquals("Remove class normal exist check 2", null, model.getClass("secondclass"));
		assertEquals("Number of classes after normal remove 2", 1, model.getClassNames().length);
		
		gui.setOptionPane(new TestableOptionPane("notreal")); // Like birds
		((TestableMenuItem)gui.getComponent("classRemoveClass")).doClick();
		assertNotEquals("Remove class not exist", 0, ErrorHandler.LAST_CODE);
		assertEquals("Number of classes after invalid remove", 1, model.getClassNames().length);
	}

	/**
	 * Test the removal of classes from the GUI main menu
	 */
	@Test
	public void mainMenuRemoveClass() {
		UMLClassManager model = new UMLClassManager();
		GUIController controller = new GUIController(model);
		GUIView gui = new GUIView(controller, model, false);
		
		// Assuming add class works
		controller.addClass("myclass");
		controller.addClass("secondclass");
		controller.addClass("thirdclass");
		
		assertEquals("Number of classes prior to remove", 3, model.getClassNames().length);
		
		gui.setOptionPane(new TestableOptionPane("myclass"));
		((TestableMenuItem)gui.getComponent("mainRemoveClass")).doClick();
		assertEquals("Remove class normal exit code", 0, ErrorHandler.LAST_CODE);
		assertEquals("Remove class normal exist check", null, model.getClass("myclass"));
		assertEquals("Number of classes after normal remove", 2, model.getClassNames().length);
		
		gui.setOptionPane(new TestableOptionPane("secondclass"));
		((TestableMenuItem)gui.getComponent("mainRemoveClass")).doClick();
		assertEquals("Remove class normal exit code 2", 0, ErrorHandler.LAST_CODE);
		assertEquals("Remove class normal exist check 2", null, model.getClass("secondclass"));
		assertEquals("Number of classes after normal remove 2", 1, model.getClassNames().length);
		
		gui.setOptionPane(new TestableOptionPane("notreal")); // Like birds
		((TestableMenuItem)gui.getComponent("mainRemoveClass")).doClick();
		assertEquals("Number of classes after invalid remove", 1, model.getClassNames().length);
	}
	
	/**
	 * Test the editing of classes
	 */
	@Test
	public void editClass() {
		UMLClassManager model = new UMLClassManager();
		GUIController controller = new GUIController(model);
		GUIView gui = new GUIView(controller, model, false);
		
		// Assuming add class works
		controller.addClass("myclass");
		controller.addClass("secondclass");
		
		assertEquals("Number of classes prior to edit", 2, model.getClassNames().length);
		
		gui.setOptionPane(new TestableOptionPane("myclass", "changedlol"));
		((TestableMenuItem)gui.getComponent("classEditClass")).doClick();
		assertEquals("Edit class normal exit code", 0, ErrorHandler.LAST_CODE);
		assertNotEquals("Edit class new name exist check", null, model.getClass("changedlol"));
		assertEquals("Number of class post name change", 2, model.getClassNames().length);
		
		gui.setOptionPane(new TestableOptionPane("changedlol", "myclass"));
		((TestableMenuItem)gui.getComponent("classEditClass")).doClick();
		assertEquals("Edit class normal exit code 2", 0, ErrorHandler.LAST_CODE);
		assertNotEquals("Edit class new name exist check 2", null, model.getClass("myclass"));
		assertEquals("Number of class post name change", 2, model.getClassNames().length);

		gui.setOptionPane(new TestableOptionPane("myclass", "n*tg*(dnamebird"));
		((TestableMenuItem)gui.getComponent("classEditClass")).doClick();
		assertNotEquals("Edit class invalid new name exit code", 0, ErrorHandler.LAST_CODE);
		assertNotEquals("Edit class invalid old name still exists", null, model.getClass("myclass"));
		assertEquals("Edit class invalid new name does not exist", null, model.getClass("n*tg*(dnamebird"));
		assertEquals("Number of class post invalid name change", 2, model.getClassNames().length);
		
		gui.setOptionPane(new TestableOptionPane("myclass", "secondclass"));
		((TestableMenuItem)gui.getComponent("classEditClass")).doClick();
		assertNotEquals("Edit class invalid new name exit code 2", 0, ErrorHandler.LAST_CODE);
		assertNotEquals("Edit class invalid old name still exists 2", null, model.getClass("myclass"));
		assertNotEquals("Edit class invalid new name still exists", null, model.getClass("secondclass"));
		assertEquals("Number of class post invalid name change", 2, model.getClassNames().length);
		
		gui.setOptionPane(new TestableOptionPane("notreal", "likebirds"));
		((TestableMenuItem)gui.getComponent("classEditClass")).doClick();
		assertNotEquals("Edit class invalid old name exit code", 0, ErrorHandler.LAST_CODE);
		assertEquals("Edit class invalid old name still not exist", null, model.getClass("notreal"));
		assertEquals("Edit class invalid new name not exists", null, model.getClass("likebirds"));
		assertEquals("Number of class post invalid name change", 2, model.getClassNames().length);
	}
	
	/**
	 * Test the editing of classes using main menu operations
	 */
	@Test
	public void mainMenuEditClass() {
		UMLClassManager model = new UMLClassManager();
		GUIController controller = new GUIController(model);
		GUIView gui = new GUIView(controller, model, false);
		
		// Assuming add class works
		controller.addClass("myclass");
		controller.addClass("secondclass");
		
		assertEquals("Number of classes prior to edit", 2, model.getClassNames().length);
		
		gui.setOptionPane(new TestableOptionPane("myclass", "changedlol"));
		((TestableMenuItem)gui.getComponent("mainEditClass")).doClick();
		assertEquals("Edit class normal exit code", 0, ErrorHandler.LAST_CODE);
		assertNotEquals("Edit class new name exist check", null, model.getClass("changedlol"));
		assertEquals("Number of class post name change", 2, model.getClassNames().length);
		
		gui.setOptionPane(new TestableOptionPane("changedlol", "myclass"));
		((TestableMenuItem)gui.getComponent("mainEditClass")).doClick();
		assertEquals("Edit class normal exit code 2", 0, ErrorHandler.LAST_CODE);
		assertNotEquals("Edit class new name exist check 2", null, model.getClass("myclass"));
		assertEquals("Number of class post name change", 2, model.getClassNames().length);

		gui.setOptionPane(new TestableOptionPane("myclass", "n*tg*(dnamebird"));
		((TestableMenuItem)gui.getComponent("mainEditClass")).doClick();
		assertNotEquals("Edit class invalid new name exit code", 0, ErrorHandler.LAST_CODE);
		assertNotEquals("Edit class invalid old name still exists", null, model.getClass("myclass"));
		assertEquals("Edit class invalid new name does not exist", null, model.getClass("n*tg*(dnamebird"));
		assertEquals("Number of class post invalid name change", 2, model.getClassNames().length);
		
		gui.setOptionPane(new TestableOptionPane("myclass", "secondclass"));
		((TestableMenuItem)gui.getComponent("mainEditClass")).doClick();
		assertNotEquals("Edit class invalid new name exit code 2", 0, ErrorHandler.LAST_CODE);
		assertNotEquals("Edit class invalid old name still exists 2", null, model.getClass("myclass"));
		assertNotEquals("Edit class invalid new name still exists", null, model.getClass("secondclass"));
		assertEquals("Number of class post invalid name change", 2, model.getClassNames().length);
		
		gui.setOptionPane(new TestableOptionPane("notreal", "likebirds"));
		((TestableMenuItem)gui.getComponent("mainEditClass")).doClick();
		assertNotEquals("Edit class invalid old name exit code", 0, ErrorHandler.LAST_CODE);
		assertEquals("Edit class invalid old name still not exist", null, model.getClass("notreal"));
		assertEquals("Edit class invalid new name not exists", null, model.getClass("likebirds"));
		assertEquals("Number of class post invalid name change", 2, model.getClassNames().length);
	}
	
	/**
	 * Test the addition of fields to classes
	 */
	@Test
	public void addField() {
		UMLClassManager model = new UMLClassManager();
		GUIController controller = new GUIController(model);
		GUIView gui = new GUIView(controller, model, false);
		
		// Assuming add class works
		controller.addClass("myclass");
		UMLClass myclass = model.getClass("myclass");
		
		assertEquals("Number of fields initial", 0, myclass.getFields().size());
		
		gui.setOptionPane(new TestableOptionPane("myclass", "int", "myfield"));
		((TestableMenuItem)gui.getComponent("classAddField")).doClick();
		assertEquals("Add field valid exit code", 0, ErrorHandler.LAST_CODE);
		assertTrue("Add field valid has field", myclass.hasField("myfield"));
		assertEquals("Add field valid num fields", 1, myclass.getFields().size());
		
		gui.setOptionPane(new TestableOptionPane("myclass", "UMLClass", "another"));
		((TestableMenuItem)gui.getComponent("classAddField")).doClick();
		assertEquals("Add field valid exit code 2", 0, ErrorHandler.LAST_CODE);
		assertTrue("Add field valid has field 2", myclass.hasField("another"));
		assertEquals("Add field valid num fields 2", 2, myclass.getFields().size());
		
		gui.setOptionPane(new TestableOptionPane("myclass", "double", "myfield"));
		((TestableMenuItem)gui.getComponent("classAddField")).doClick();
		assertNotEquals("Add field duplicate name diff type", 0, ErrorHandler.LAST_CODE);
		assertTrue("Add field original still exists", myclass.hasField("myfield"));
		assertEquals("Add field duplicate num fields", 2, myclass.getFields().size());
		
		gui.setOptionPane(new TestableOptionPane("myclass", "int", "m@lf0rm*dn@me"));
		((TestableMenuItem)gui.getComponent("classAddField")).doClick();
		assertNotEquals("Add field invalid exit code", 0, ErrorHandler.LAST_CODE);
		assertFalse("Add field invalid does not exist", myclass.hasField("m@lf0rm*dn@me"));
		assertEquals("Add filed invalid num fields", 2, myclass.getFields().size());
	}
	
	/**
	 * Test the addition of fields to classes using main menu interaction
	 */
	@Test
	public void mainMenuAddField() {
		UMLClassManager model = new UMLClassManager();
		GUIController controller = new GUIController(model);
		GUIView gui = new GUIView(controller, model, false);
		
		// Assuming add class works
		controller.addClass("myclass");
		UMLClass myclass = model.getClass("myclass");
		
		assertEquals("Number of fields initial", 0, myclass.getFields().size());
		
		gui.setOptionPane(new TestableOptionPane("myclass", "int", "myfield"));
		((TestableMenuItem)gui.getComponent("mainAddField")).doClick();
		assertEquals("Add field valid exit code", 0, ErrorHandler.LAST_CODE);
		assertTrue("Add field valid has field", myclass.hasField("myfield"));
		assertEquals("Add field valid num fields", 1, myclass.getFields().size());
		
		gui.setOptionPane(new TestableOptionPane("myclass", "UMLClass", "another"));
		((TestableMenuItem)gui.getComponent("mainAddField")).doClick();
		assertEquals("Add field valid exit code 2", 0, ErrorHandler.LAST_CODE);
		assertTrue("Add field valid has field 2", myclass.hasField("another"));
		assertEquals("Add field valid num fields 2", 2, myclass.getFields().size());
		
		gui.setOptionPane(new TestableOptionPane("myclass", "double", "myfield"));
		((TestableMenuItem)gui.getComponent("mainAddField")).doClick();
		assertNotEquals("Add field duplicate name diff type", 0, ErrorHandler.LAST_CODE);
		assertTrue("Add field original still exists", myclass.hasField("myfield"));
		assertEquals("Add field duplicate num fields", 2, myclass.getFields().size());
		
		gui.setOptionPane(new TestableOptionPane("myclass", "int", "m@lf0rm*dn@me"));
		((TestableMenuItem)gui.getComponent("mainAddField")).doClick();
		assertNotEquals("Add field invalid exit code", 0, ErrorHandler.LAST_CODE);
		assertFalse("Add field invalid does not exist", myclass.hasField("m@lf0rm*dn@me"));
		assertEquals("Add filed invalid num fields", 2, myclass.getFields().size());
	}
	
	/**
	 * Test the removal of fields from classes
	 */
	@Test
	public void removeField() {
		UMLClassManager model = new UMLClassManager();
		GUIController controller = new GUIController(model);
		GUIView gui = new GUIView(controller, model, false);
		
		// Assuming add class works
		controller.addClass("myclass");
		controller.addField("myclass", "int", "myfield");
		controller.addField("myclass", "String", "another");
		UMLClass myclass = model.getClass("myclass");
		
		assertEquals("Number of fields initial", 2, myclass.getFields().size());
		
		gui.setOptionPane(new TestableOptionPane("myclass", "myfield"));
		((TestableMenuItem)gui.getComponent("classRemoveField")).doClick();
		assertEquals("Remove field valid return code", 0, ErrorHandler.LAST_CODE);
		assertFalse("Remove field valid no longer exists", myclass.hasField("myfield"));
		assertEquals("Remove field valid num fields", 1, myclass.getFields().size());
		
		gui.setOptionPane(new TestableOptionPane("myclass", "myfield"));
		((TestableMenuItem)gui.getComponent("classRemoveField")).doClick();
		assertNotEquals("Remove field doesn't exist", 0, ErrorHandler.LAST_CODE);
		assertFalse("Remove filed not in list", myclass.hasField("myfield"));
		assertEquals("Remove field doesn't exist num fields", 1, myclass.getFields().size());
		
		gui.setOptionPane(new TestableOptionPane("myclass", "n*tr3@l")); // Like birds
		((TestableMenuItem)gui.getComponent("classRemoveField")).doClick();
		assertNotEquals("Remove field invalid", 0, ErrorHandler.LAST_CODE);
		assertEquals("Remove field invalid num fields", 1, myclass.getFields().size());
		
		gui.setOptionPane(new TestableOptionPane("myclass", "another"));
		((TestableMenuItem)gui.getComponent("classRemoveField")).doClick();
		assertEquals("Remove field valid return code 2", 0, ErrorHandler.LAST_CODE);
		assertFalse("Remove field valid no longer exists 2", myclass.hasField("another"));
		assertEquals("Remove field valid num fields 2", 0, myclass.getFields().size());
	}
	
	/**
	 * Test the removal of fields from classes
	 */
	@Test
	public void mainMenuRemoveField() {
		UMLClassManager model = new UMLClassManager();
		GUIController controller = new GUIController(model);
		GUIView gui = new GUIView(controller, model, false);
		
		// Assuming add class works
		controller.addClass("myclass");
		controller.addField("myclass", "int", "myfield");
		controller.addField("myclass", "String", "another");
		UMLClass myclass = model.getClass("myclass");
		
		assertEquals("Number of fields initial", 2, myclass.getFields().size());
		
		gui.setOptionPane(new TestableOptionPane("myclass", "myfield"));
		((TestableMenuItem)gui.getComponent("mainRemoveField")).doClick();
		assertEquals("Remove field valid return code", 0, ErrorHandler.LAST_CODE);
		assertFalse("Remove field valid no longer exists", myclass.hasField("myfield"));
		assertEquals("Remove field valid num fields", 1, myclass.getFields().size());
		
		gui.setOptionPane(new TestableOptionPane("myclass", "myfield"));
		((TestableMenuItem)gui.getComponent("mainRemoveField")).doClick();
		assertNotEquals("Remove field doesn't exist", 0, ErrorHandler.LAST_CODE);
		assertFalse("Remove filed not in list", myclass.hasField("myfield"));
		assertEquals("Remove field doesn't exist num fields", 1, myclass.getFields().size());
		
		gui.setOptionPane(new TestableOptionPane("myclass", "n*tr3@l")); // Like birds
		((TestableMenuItem)gui.getComponent("mainRemoveField")).doClick();
		assertNotEquals("Remove field invalid", 0, ErrorHandler.LAST_CODE);
		assertEquals("Remove field invalid num fields", 1, myclass.getFields().size());
		
		gui.setOptionPane(new TestableOptionPane("myclass", "another"));
		((TestableMenuItem)gui.getComponent("mainRemoveField")).doClick();
		assertEquals("Remove field valid return code 2", 0, ErrorHandler.LAST_CODE);
		assertFalse("Remove field valid no longer exists 2", myclass.hasField("another"));
		assertEquals("Remove field valid num fields 2", 0, myclass.getFields().size());
	}
	
	/**
	 * Test the editing of field names
	 */
	@Test
	public void editField() {
		UMLClassManager model = new UMLClassManager();
		GUIController controller = new GUIController(model);
		GUIView gui = new GUIView(controller, model, false);
		
		// Assuming add class works
		controller.addClass("myclass");
		controller.addField("myclass", "Object", "myfield");
		controller.addField("myclass", "double", "another");
		UMLClass myclass = model.getClass("myclass");
		
		assertEquals("Number of fields init", 2, myclass.getFields().size());
		
		gui.setOptionPane(new TestableOptionPane("myclass", "myfield", "other"));
		((TestableMenuItem)gui.getComponent("classEditField")).doClick();
		assertEquals("Edit field valid return code", 0, ErrorHandler.LAST_CODE);
		assertFalse("Edit field valid old name doesn't exist", myclass.hasField("myfield"));
		assertTrue("Edit field valid new name exists", myclass.hasField("other"));
		assertEquals("Edit field valid num fields", 2, myclass.getFields().size());
		
		gui.setOptionPane(new TestableOptionPane("myclass", "other", "myfield"));
		((TestableMenuItem)gui.getComponent("classEditField")).doClick();
		assertEquals("Edit field valid return code 2", 0, ErrorHandler.LAST_CODE);
		assertFalse("Edit field valid old name not exists 2", myclass.hasField("other"));
		assertTrue("Edit field valid new name exists 2", myclass.hasField("myfield"));
		assertEquals("Edit field valid num fields unchanged", 2, myclass.getFields().size());
		
		gui.setOptionPane(new TestableOptionPane("myclass", "myfield", "another"));
		((TestableMenuItem)gui.getComponent("classEditField")).doClick();
		assertNotEquals("Edit field duplicate return code", 0, ErrorHandler.LAST_CODE);
		assertTrue("Edit field duplicate original still exists", myclass.hasField("myfield"));
		assertEquals("Edit field duplicate num fields unchanged", 2, myclass.getFields().size());
		
		gui.setOptionPane(new TestableOptionPane("myclass", "another", "_n*t()real"));
		((TestableMenuItem)gui.getComponent("classEditField")).doClick();
		assertNotEquals("Edit field invalid return code", 0, ErrorHandler.LAST_CODE);
		assertTrue("Edit field invalid old name still exists", myclass.hasField("another"));
		assertFalse("Edit field invalid new name does not exist", myclass.hasField("_n*t()real"));
		assertEquals("Edit field invalid num fields unchanged", 2, myclass.getFields().size());
	}
	
	/**
	 * Test the editing of field names
	 */
	@Test
	public void mainMenuEditField() {
		UMLClassManager model = new UMLClassManager();
		GUIController controller = new GUIController(model);
		GUIView gui = new GUIView(controller, model, false);
		
		// Assuming add class works
		controller.addClass("myclass");
		controller.addField("myclass", "Object", "myfield");
		controller.addField("myclass", "double", "another");
		UMLClass myclass = model.getClass("myclass");
		
		assertEquals("Number of fields init", 2, myclass.getFields().size());
		
		gui.setOptionPane(new TestableOptionPane("myclass", "myfield", "other"));
		((TestableMenuItem)gui.getComponent("classEditField")).doClick();
		assertEquals("Edit field valid return code", 0, ErrorHandler.LAST_CODE);
		assertFalse("Edit field valid old name doesn't exist", myclass.hasField("myfield"));
		assertTrue("Edit field valid new name exists", myclass.hasField("other"));
		assertEquals("Edit field valid num fields", 2, myclass.getFields().size());
		
		gui.setOptionPane(new TestableOptionPane("myclass", "other", "myfield"));
		((TestableMenuItem)gui.getComponent("classEditField")).doClick();
		assertEquals("Edit field valid return code 2", 0, ErrorHandler.LAST_CODE);
		assertFalse("Edit field valid old name not exists 2", myclass.hasField("other"));
		assertTrue("Edit field valid new name exists 2", myclass.hasField("myfield"));
		assertEquals("Edit field valid num fields unchanged", 2, myclass.getFields().size());
		
		gui.setOptionPane(new TestableOptionPane("myclass", "myfield", "another"));
		((TestableMenuItem)gui.getComponent("classEditField")).doClick();
		assertNotEquals("Edit field duplicate return code", 0, ErrorHandler.LAST_CODE);
		assertTrue("Edit field duplicate original still exists", myclass.hasField("myfield"));
		assertEquals("Edit field duplicate num fields unchanged", 2, myclass.getFields().size());
		
		gui.setOptionPane(new TestableOptionPane("myclass", "another", "_n*t()real"));
		((TestableMenuItem)gui.getComponent("classEditField")).doClick();
		assertNotEquals("Edit field invalid return code", 0, ErrorHandler.LAST_CODE);
		assertTrue("Edit field invalid old name still exists", myclass.hasField("another"));
		assertFalse("Edit field invalid new name does not exist", myclass.hasField("_n*t()real"));
		assertEquals("Edit field invalid num fields unchanged", 2, myclass.getFields().size());
	}

	
	/**
	 * Test the addition of methods to class
	 */
	@Test
	public void addMethod() {
		UMLClassManager model = new UMLClassManager();
		GUIController controller = new GUIController(model);
		GUIView gui = new GUIView(controller, model, false);
		
		// Assuming add class works
		controller.addClass("myclass");
		UMLClass myclass = model.getClass("myclass");
		
		assertEquals("Number of methods initial", 0, myclass.getMethods().size());
		
		// Adding method 'int noparams() {}'
		gui.setOptionPane(new TestableOptionPane("myclass", "int", "noparams", ""));
		((TestableMenuItem)gui.getComponent("classAddMethod")).doClick();
		assertEquals("Add method valid return code", 0, ErrorHandler.LAST_CODE);
		assertEquals("Add method valid num methods", 1, myclass.getMethods().size());
		
		// Adding method 'void mymethod(String par1, int par2) {}'
		gui.setOptionPane(new TestableOptionPane("myclass", "void", "mymethod", "String par1, int par2"));
		((TestableMenuItem)gui.getComponent("classAddMethod")).doClick();
		assertEquals("Add method valid return code 2", 0, ErrorHandler.LAST_CODE);
		assertEquals("Add method valid num methods 2", 2, myclass.getMethods().size());
		
		gui.setOptionPane(new TestableOptionPane("myclass", "void", "mymethod", "String overloaded"));
		((TestableMenuItem)gui.getComponent("classAddMethod")).doClick();
		assertEquals("Add overload method return code", 0, ErrorHandler.LAST_CODE);
		assertEquals("Num methods", 3, myclass.getMethods().size());
		
		gui.setOptionPane(new TestableOptionPane("myclass", "void", "*not()real*", ""));
		((TestableMenuItem)gui.getComponent("classAddMethod")).doClick();
		assertNotEquals("Add method invalid return code", 0, ErrorHandler.LAST_CODE);
		assertEquals("Num methods", 3, myclass.getMethods().size());
	}
	
	/**
	 * Test the addition of methods to class using main menu interaction
	 */
	@Test
	public void mainMenuAddMethod() {
		UMLClassManager model = new UMLClassManager();
		GUIController controller = new GUIController(model);
		GUIView gui = new GUIView(controller, model, false);
		
		// Assuming add class works
		controller.addClass("myclass");
		UMLClass myclass = model.getClass("myclass");
		
		assertEquals("Number of methods initial", 0, myclass.getMethods().size());
		
		// Adding method 'int noparams() {}'
		gui.setOptionPane(new TestableOptionPane("myclass", "int", "noparams", ""));
		((TestableMenuItem)gui.getComponent("mainAddMethod")).doClick();
		assertEquals("Add method valid return code", 0, ErrorHandler.LAST_CODE);
		assertEquals("Add method valid num methods", 1, myclass.getMethods().size());
		
		// Adding method 'void mymethod(String par1, int par2) {}'
		gui.setOptionPane(new TestableOptionPane("myclass", "void", "mymethod", "String par1, int par2"));
		((TestableMenuItem)gui.getComponent("mainAddMethod")).doClick();
		assertEquals("Add method valid return code 2", 0, ErrorHandler.LAST_CODE);
		assertEquals("Add method valid num methods 2", 2, myclass.getMethods().size());
		
		gui.setOptionPane(new TestableOptionPane("myclass", "void", "mymethod", "String overloaded"));
		((TestableMenuItem)gui.getComponent("mainAddMethod")).doClick();
		assertEquals("Add overload method return code", 0, ErrorHandler.LAST_CODE);
		assertEquals("Num methods", 3, myclass.getMethods().size());
		
		gui.setOptionPane(new TestableOptionPane("myclass", "void", "*not()real*", ""));
		((TestableMenuItem)gui.getComponent("mainAddMethod")).doClick();
		assertNotEquals("Add method invalid return code", 0, ErrorHandler.LAST_CODE);
		assertEquals("Num methods", 3, myclass.getMethods().size());
	}
	
	/**
	 * Test the removal of methods to class
	 */
	@Test
	public void removeMethod() {
		UMLClassManager model = new UMLClassManager();
		GUIController controller = new GUIController(model);
		GUIView gui = new GUIView(controller, model, false);
		
		controller.addClass("myclass");
		controller.addMethod("myclass", "int", "mymethod", "");
		controller.addMethod("myclass", "String", "another", "String par1");
		controller.addMethod("myclass", "String", "another", "");
		UMLClass myclass = model.getClass("myclass");
		
		assertEquals("Init number of methods", 3, myclass.getMethods().size());
		
		// Remove mymethod with no params
		gui.setOptionPane(new TestableOptionPane("myclass", "mymethod", ""));
		((TestableMenuItem)gui.getComponent("classRemoveMethod")).doClick();
		assertEquals("Remove method valid return code", 0, ErrorHandler.LAST_CODE);
		assertEquals("Num methods post remove", 2, myclass.getMethods().size());
		
		// Remove another with args
		gui.setOptionPane(new TestableOptionPane("myclass", "another", "String par1"));
		((TestableMenuItem)gui.getComponent("classRemoveMethod")).doClick();
		assertEquals("Remove overload method valid return code", 0, ErrorHandler.LAST_CODE);
		assertEquals("Num methods post overload remove", 1, myclass.getMethods().size());
		
		gui.setOptionPane(new TestableOptionPane("myclass", "d*e$n0#", ""));
		((TestableMenuItem)gui.getComponent("classRemoveMethod")).doClick();
		assertNotEquals("Remove method invalid return code", 0, ErrorHandler.LAST_CODE);
		assertEquals("Remove invalid method nothing removed", 1, myclass.getMethods().size());
	}
	
	/**
	 * Test the removal of methods to class using main menu interaction
	 */
	@Test
	public void mainMenuRemoveMethod() {
		UMLClassManager model = new UMLClassManager();
		GUIController controller = new GUIController(model);
		GUIView gui = new GUIView(controller, model, false);
		
		controller.addClass("myclass");
		controller.addMethod("myclass", "int", "mymethod", "");
		controller.addMethod("myclass", "String", "another", "String par1");
		controller.addMethod("myclass", "String", "another", "");
		UMLClass myclass = model.getClass("myclass");
		
		assertEquals("Init number of methods", 3, myclass.getMethods().size());
		
		// Remove mymethod with no params
		gui.setOptionPane(new TestableOptionPane("myclass", "mymethod"));
		((TestableMenuItem)gui.getComponent("mainRemoveMethod")).doClick();
		assertEquals("Remove method valid return code", 0, ErrorHandler.LAST_CODE);
		assertEquals("Num methods post remove", 2, myclass.getMethods().size());
		
		// Remove another with args
		gui.setOptionPane(new TestableOptionPane("myclass", "another", "String par1"));
		((TestableMenuItem)gui.getComponent("mainRemoveMethod")).doClick();
		assertEquals("Remove overload method valid return code", 0, ErrorHandler.LAST_CODE);
		assertEquals("Num methods post overload remove", 1, myclass.getMethods().size());
		
		gui.setOptionPane(new TestableOptionPane("myclass", "d*e$n0#"));
		((TestableMenuItem)gui.getComponent("mainRemoveMethod")).doClick();
		assertNotEquals("Remove method invalid return code", 0, ErrorHandler.LAST_CODE);
		assertEquals("Remove invalid method nothing removed", 1, myclass.getMethods().size());
	}
	
	/**
	 * Test the editing of method names for a class
	 */
	@Test
	public void editMethod() {
		UMLClassManager model = new UMLClassManager();
		GUIController controller = new GUIController(model);
		GUIView gui = new GUIView(controller, model, false);
		
		controller.addClass("myclass");
		controller.addMethod("myclass", "int", "mymethod", "");
		controller.addMethod("myclass", "boolean", "another", "");
		controller.addMethod("myclass", "boolean", "another", "boolean istrue");
		UMLClass myclass = model.getClass("myclass");
		
		assertEquals("Init number of methods", 3, myclass.getMethods().size());
		
		gui.setOptionPane(new TestableOptionPane("myclass", "mymethod", "newmethod", ""));
		((TestableMenuItem)gui.getComponent("classEditMethod")).doClick();
		assertEquals("Edit method name valid return code", 0, ErrorHandler.LAST_CODE);
		assertEquals("Num methods the same", 3, myclass.getMethods().size());
		
		gui.setOptionPane(new TestableOptionPane("myclass", "newmethod", "mymethod", ""));
		((TestableMenuItem)gui.getComponent("classEditMethod")).doClick();
		assertEquals("Edit method name valid return code 2", 0, ErrorHandler.LAST_CODE);
		assertEquals("Num methods unchanged", 3, myclass.getMethods().size());
		
		gui.setOptionPane(new TestableOptionPane("myclass", "mymethod", "another", ""));
		((TestableMenuItem)gui.getComponent("classEditMethod")).doClick();
		assertNotEquals("Edit method to duplicate name return code", 0, ErrorHandler.LAST_CODE);
		assertEquals("Num methods unchanged", 3, myclass.getMethods().size());
	}
	
	/**
	 * Test the editing of method names for a class using main menu interaction
	 */
	@Test
	public void mainMenuEditMethod() {
		UMLClassManager model = new UMLClassManager();
		GUIController controller = new GUIController(model);
		GUIView gui = new GUIView(controller, model, false);
		
		controller.addClass("myclass");
		controller.addMethod("myclass", "int", "mymethod", "");
		controller.addMethod("myclass", "boolean", "another", "");
		controller.addMethod("myclass", "boolean", "another", "boolean istrue");
		UMLClass myclass = model.getClass("myclass");
		
		assertEquals("Init number of methods", 3, myclass.getMethods().size());
		
		gui.setOptionPane(new TestableOptionPane("myclass", "mymethod", "newmethod", ""));
		((TestableMenuItem)gui.getComponent("mainEditMethod")).doClick();
		assertEquals("Edit method name valid return code", 0, ErrorHandler.LAST_CODE);
		assertEquals("Num methods the same", 3, myclass.getMethods().size());
		
		gui.setOptionPane(new TestableOptionPane("myclass", "newmethod", "mymethod", ""));
		((TestableMenuItem)gui.getComponent("mainEditMethod")).doClick();
		assertEquals("Edit method name valid return code 2", 0, ErrorHandler.LAST_CODE);
		assertEquals("Num methods unchanged", 3, myclass.getMethods().size());
		
		gui.setOptionPane(new TestableOptionPane("myclass", "mymethod", "another", ""));
		((TestableMenuItem)gui.getComponent("mainEditMethod")).doClick();
		assertNotEquals("Edit method to duplicate name return code", 0, ErrorHandler.LAST_CODE);
		assertEquals("Num methods unchanged", 3, myclass.getMethods().size());
	}
	
	/**
	 * Test the adding of relationships
	 */
	@Test
	public void addRelationship() {
		UMLClassManager model = new UMLClassManager();
		GUIController controller = new GUIController(model);
		GUIView gui = new GUIView(controller, model, false);
		
		controller.addClass("class1");
		controller.addClass("class2");
		controller.addClass("class3");
		
		//assertEquals("Init number of relationships for class1", "[]", model.listRelationships("class1")[0]);
		//assertEquals("Init number of relationships for class2", "[]", model.listRelationships("class2")[0]);
		//assertEquals("Init number of relationships for class3", "[]", model.listRelationships("class3")[0]);
		assertEquals("Init number total relationships", 0, model.getRelationships().size());
		
		gui.setOptionPane(new TestableOptionPane("class1", "class2", "aggregation"));
		((TestableMenuItem)gui.getComponent("classAddRelationship")).doClick();
		assertEquals("Add relationship valid return code", 0, ErrorHandler.LAST_CODE);
		assertEquals("Num total relationships after valid add", 1, model.getRelationships().size());
		
		gui.setOptionPane(new TestableOptionPane("class2", "class3", "inheritance"));
		((TestableMenuItem)gui.getComponent("classAddRelationship")).doClick();
		assertEquals("Add relationship valid return code 2", 0, ErrorHandler.LAST_CODE);
		assertEquals("Num total relationships", 2, model.getRelationships().size());
		
		gui.setOptionPane(new TestableOptionPane("class1", "class3", "not real"));
		((TestableMenuItem)gui.getComponent("classAddRelationship")).doClick();
		assertNotEquals("Add relationship invalid return code", 0, ErrorHandler.LAST_CODE);
		assertEquals("Num total relationships", 2, model.getRelationships().size());
		
		gui.setOptionPane(new TestableOptionPane("class1", "class2", "aggregation"));
		((TestableMenuItem)gui.getComponent("classAddRelationship")).doClick();
		assertNotEquals("Add relationship duplicate return code", 0, ErrorHandler.LAST_CODE);
		assertEquals("Num total relationships", 2, model.getRelationships().size());
		
		gui.setOptionPane(new TestableOptionPane("class1", "notreal", "aggregation"));
		((TestableMenuItem)gui.getComponent("classAddRelationship")).doClick();
		assertNotEquals("Add relationship invalid return code", 0, ErrorHandler.LAST_CODE);
		assertEquals("Num total relationships", 2, model.getRelationships().size());
	}
	
	/**
	 * Test the adding of relationships using main menu interactions
	 */
	@Test
	public void mainMenuAddRelationship() {
		UMLClassManager model = new UMLClassManager();
		GUIController controller = new GUIController(model);
		GUIView gui = new GUIView(controller, model, false);
		
		controller.addClass("class1");
		controller.addClass("class2");
		controller.addClass("class3");
		
		assertEquals("Init number of relationships for class1", "[]", model.listRelationships("class1")[0]);
		assertEquals("Init number of relationships for class2", "[]", model.listRelationships("class2")[0]);
		assertEquals("Init number of relationships for class3", "[]", model.listRelationships("class3")[0]);
		assertEquals("Init number total relationships", 0, model.getRelationships().size());
		
		gui.setOptionPane(new TestableOptionPane("class1", "class2", "aggregation"));
		((TestableMenuItem)gui.getComponent("mainAddRelationship")).doClick();
		assertEquals("Add relationship valid return code", 0, ErrorHandler.LAST_CODE);
		assertEquals("Num total relationships after valid add", 1, model.getRelationships().size());
		
		gui.setOptionPane(new TestableOptionPane("class2", "class3", "inheritance"));
		((TestableMenuItem)gui.getComponent("mainAddRelationship")).doClick();
		assertEquals("Add relationship valid return code 2", 0, ErrorHandler.LAST_CODE);
		assertEquals("Num total relationships", 2, model.getRelationships().size());
		
		gui.setOptionPane(new TestableOptionPane("class1", "class3", "not real"));
		((TestableMenuItem)gui.getComponent("mainAddRelationship")).doClick();
		assertNotEquals("Add relationship invalid return code", 0, ErrorHandler.LAST_CODE);
		assertEquals("Num total relationships", 2, model.getRelationships().size());
		
		gui.setOptionPane(new TestableOptionPane("class1", "class2", "aggregation"));
		((TestableMenuItem)gui.getComponent("mainAddRelationship")).doClick();
		assertNotEquals("Add relationship duplicate return code", 0, ErrorHandler.LAST_CODE);
		assertEquals("Num total relationships", 2, model.getRelationships().size());
		
		gui.setOptionPane(new TestableOptionPane("class1", "notreal", "aggregation"));
		((TestableMenuItem)gui.getComponent("mainAddRelationship")).doClick();
		assertNotEquals("Add relationship invalid return code", 0, ErrorHandler.LAST_CODE);
		assertEquals("Num total relationships", 2, model.getRelationships().size());
	}
	
	/**
	 * Test the removal of relationships
	 */
	@Test
	public void removeRelationship() {
		UMLClassManager model = new UMLClassManager();
		GUIController controller = new GUIController(model);
		GUIView gui = new GUIView(controller, model, false);
		ErrorHandler.setCode(0);
		
		controller.addClass("class1");
		controller.addClass("class2");
		controller.addClass("class3");
		controller.addRelationship("class1", "aggregation", "class2");
		controller.addRelationship("class1", "inheritance", "class3");
		controller.addRelationship("class2", "composition", "class3");
		
		assertEquals("Init num relationships", 3, model.getRelationships().size());
		
		gui.setOptionPane(new TestableOptionPane("class1", "class2", "aggregation"));
		((TestableMenuItem)gui.getComponent("classRemoveRelationship")).doClick();
		assertEquals("Num relationships post remove", 2, model.getRelationships().size());
		
		gui.setOptionPane(new TestableOptionPane("class1", "class2", "aggregation"));
		((TestableMenuItem)gui.getComponent("classRemoveRelationship")).doClick();
		assertNotEquals("Remove invalid return code", 0, ErrorHandler.LAST_CODE);
		assertEquals("Num relationships post invalid remove", 2, model.getRelationships().size());
		
		gui.setOptionPane(new TestableOptionPane("class2", "class3", "notrealtype"));
		((TestableMenuItem)gui.getComponent("classRemoveRelationship")).doClick();
		assertNotEquals("Remove invalid return code 2", 0, ErrorHandler.LAST_CODE);
		assertEquals("Num relationships post invalid remove 2", 2, model.getRelationships().size());
		
		gui.setOptionPane(new TestableOptionPane("class1", "class3", "inheritance"));
		((TestableMenuItem)gui.getComponent("classRemoveRelationship")).doClick();
		assertEquals("Remove valid return code 2", 0, ErrorHandler.LAST_CODE);
		assertEquals("Num relationships post valid remove 2", 1, model.getRelationships().size());
	}
	
	/**
	 * Test the removal of relationships using main menu interaction
	 */
	@Test
	public void mainMenuRemoveRelationship() {
		UMLClassManager model = new UMLClassManager();
		GUIController controller = new GUIController(model);
		GUIView gui = new GUIView(controller, model, false);
		
		controller.addClass("class1");
		controller.addClass("class2");
		controller.addClass("class3");
		controller.addRelationship("class1", "aggregation", "class2");
		controller.addRelationship("class1", "inheritance", "class3");
		controller.addRelationship("class2", "composition", "class3");
		
		assertEquals("Init num relationships", 3, model.getRelationships().size());
		
		gui.setOptionPane(new TestableOptionPane("class1", "class2", "aggregation"));
		((TestableMenuItem)gui.getComponent("mainRemoveRelationship")).doClick();
		assertEquals("Remove valid return code", 0, ErrorHandler.LAST_CODE);
		assertEquals("Num relationships post remove", 2, model.getRelationships().size());
		
		gui.setOptionPane(new TestableOptionPane("class1", "class2", "aggregation"));
		((TestableMenuItem)gui.getComponent("mainRemoveRelationship")).doClick();
		assertNotEquals("Remove invalid return code", 0, ErrorHandler.LAST_CODE);
		assertEquals("Num relationships post invalid remove", 2, model.getRelationships().size());
		
		gui.setOptionPane(new TestableOptionPane("class2", "class3", "notrealtype"));
		((TestableMenuItem)gui.getComponent("mainRemoveRelationship")).doClick();
		assertNotEquals("Remove invalid return code 2", 0, ErrorHandler.LAST_CODE);
		assertEquals("Num relationships post invalid remove 2", 2, model.getRelationships().size());
		
		gui.setOptionPane(new TestableOptionPane("class1", "class3", "inheritance"));
		((TestableMenuItem)gui.getComponent("mainRemoveRelationship")).doClick();
		assertEquals("Remove valid return code 2", 0, ErrorHandler.LAST_CODE);
		assertEquals("Num relationships post valid remove 2", 1, model.getRelationships().size());
	}
	
	/**
	 * Test the saving of the model for both the main menu and mouse menu
	 */
	@Test
	public void save() {
		UMLClassManager model = new UMLClassManager();
		GUIController controller = new GUIController(model);
		GUIView gui = new GUIView(controller, model, false);
		
		// Add some stuff to the model just cause
		controller.addClass("myclass", 10, 10);
		controller.addField("myclass", "int", "myInt");
		
		// Test save for mouse menu
		File expectedFile = new File("uml-output.json");
		expectedFile.deleteOnExit();
		gui.setFileChooser(new TestableFileChooser(expectedFile));
		((TestableMenuItem)gui.getComponent("mouseSave")).doClick();
		// Make sure the file exists
		assertTrue("File created", expectedFile.exists());
		
		// Test save for the main menu
		File expected2electricboogaloo = new File("uml-output2.json");
		expected2electricboogaloo.deleteOnExit();
		gui.setFileChooser(new TestableFileChooser(expected2electricboogaloo));
		((TestableMenuItem)gui.getComponent("mainSave")).doClick();
		assertTrue("File created 2", expected2electricboogaloo.exists());
	}
	
	/**
	 * Test the loading of the model for both main menu and mouse menu
	 */
	@Test
	public void load() {
		UMLClassManager model = new UMLClassManager();
		GUIController controller = new GUIController(model);
		GUIView gui = new GUIView(controller, model, false);
		
		// Add some stuff to the model
		controller.addClass("myclass", 10, 10);
		controller.addField("myclass", "int", "myInt");
		
		// Save it so we have a file to load from
		File expectedFile = new File("uml-output.json");
		expectedFile.deleteOnExit();
		gui.setFileChooser(new TestableFileChooser(expectedFile));
		((TestableMenuItem)gui.getComponent("mouseSave")).doClick();
		// Make sure the file exists
		assertTrue("File created", expectedFile.exists());
		
		// Create a second instance
		UMLClassManager model2 = new UMLClassManager();
		GUIController controller2 = new GUIController(model);
		GUIView gui2 = new GUIView(controller2, model2, false);
		
		// Load from file
		File loadFile = new File("uml-output.json");
		gui.setFileChooser(new TestableFileChooser(loadFile));
		((TestableMenuItem)gui2.getComponent("mouseLoad")).doClick();
		// Check if it was applied
		assertTrue("Model has the loaded class", model.getClass("myclass") != null);
		assertTrue("Model has the loaded class's field", model.getClass("myclass").hasField("myInt"));
		
		// Do it for the main menu now
		UMLClassManager model3 = new UMLClassManager();
		GUIController controller3 = new GUIController(model);
		GUIView gui3 = new GUIView(controller3, model3, false);
		
		// Load from file
		gui.setFileChooser(new TestableFileChooser(loadFile));
		((TestableMenuItem)gui3.getComponent("mainLoad")).doClick();
		// Check if it was applied
		assertTrue("Model has the loaded class", model.getClass("myclass") != null);
		assertTrue("Model has the loaded class's field", model.getClass("myclass").hasField("myInt"));
	}
	
	/**
	 * Test the exporting of the model to a .png
	 */
	@Test
	public void export() {
		UMLClassManager model = new UMLClassManager();
		GUIController controller = new GUIController(model);
		GUIView gui = new GUIView(controller, model, false);
		
		// Add some stuff to the model just cause
		controller.addClass("myclass", 10, 10);
		controller.addField("myclass", "int", "myInt");
		controller.addClass("second");
		controller.addRelationship("myclass", "aggregation", "myclass");
		controller.addRelationship("myclass", "aggregation", "second");

		// Call paint to show that it doesn't error out, mostly for code coverage
		gui.getDiagram().paintComponent(new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB).getGraphics());
		
		// Test save for mouse menu
		File expectedFile = new File("uml-image.png");
		gui.setFileChooser(new TestableFileChooser(expectedFile));
		((TestableMenuItem)gui.getComponent("mouseExport")).doClick();
		// Make sure the file exists
		assertTrue("File created", expectedFile.exists());
		
		// Test save for the main menu
		File expected2electricboogaloo = new File("uml-image2.png");
		expected2electricboogaloo.deleteOnExit();
		gui.setFileChooser(new TestableFileChooser(expected2electricboogaloo));
		((TestableMenuItem)gui.getComponent("mainExport")).doClick();
		assertTrue("File created 2", expected2electricboogaloo.exists());
	}
	
	/**
	 * Test the resize ability
	 */
	@Test
	public void resize() {
		UMLClassManager model = new UMLClassManager();
		GUIController controller = new GUIController(model);
		GUIView gui = new GUIView(controller, model, false);
		
		// Ensure original size
		assertEquals("Original diagram width", 600, gui.getDiagram().getWidth());
		assertEquals("Original diagram height", 600, gui.getDiagram().getHeight());
		
		gui.setOptionPane(new TestableOptionPane("800", "800"));
		((TestableMenuItem)gui.getComponent("mainResize")).doClick();
		
		// Ensure new size
		assertEquals("New diagram width", 800, gui.getDiagram().getWidth());
		assertEquals("New diagram height", 800, gui.getDiagram().getHeight());
	}
}
