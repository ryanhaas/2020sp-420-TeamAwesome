// Package name
package views.components;

//System imports
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

// Local imports
import core.ErrorHandler;
import core.UMLFileIO;
import model.Method;
import model.UMLClass;
import model.UMLRelationship;
import observe.Observable;
import observe.Observer;
import views.GUIView;
import views.components.testable.TestableMenu;
import views.components.testable.TestableMenuBar;
import views.components.testable.TestableMenuItem;
import views.components.testable.TestablePanel;
import views.components.testable.TestablePopupMenu;

/**
 * A JPanel to display the UMLClasses and relationships
 * @author 98% Ryan and a tiny bit of Dylan
 *
 */
public class DiagramPanel extends TestablePanel implements Observer, MouseListener, MouseMotionListener {
	private static final long serialVersionUID = 1L;

	// Instance of the Controller
	private GUIView view;
	
	// Map of graphical representations of classes
	private HashMap<String, GUIClass> guiClasses;

	// Location of the last class selected/dragged
	private int lastX;
	private int lastY;
	
	// Menus
	private JPopupMenu mouseMenu;
	private JPopupMenu classMenu;
	
	//Menu titles bar for testing main menu interaction
	private JMenuBar mainMenuBar;
	private JMenu mainFile; //save loading and exporting
	private JMenu mainActions; //everything else
	private JMenuItem mainRemoveClass;
	private JMenuItem mainEditClass;
	private JMenuItem mainAddField;
	private JMenuItem mainRemoveField;
	private JMenuItem mainEditField;
	private JMenuItem mainAddMethod;
	private JMenuItem mainRemoveMethod;
	private JMenuItem mainEditMethod;
	private JMenuItem mainAddRelationship;
	private JMenuItem mainRemoveRelationship;
	private JMenuItem mainAddClass;
	private JMenuItem mainSaveFile;
	private JMenuItem mainLoadFile;
	private JMenuItem mainExportPNG;
	private JMenuItem mainResize;
	
	// MenuItems for generic mouse menu
	private JMenuItem mouseAddClass;
	private JMenuItem mouseSaveFile;
	private JMenuItem mouseLoadFile;
	private JMenuItem mouseExportPNG;
	
	// MenuItems for class mouse menu
	private JMenuItem classRemoveClass;
	private JMenuItem classEditClass;
	private JMenuItem classAddField;
	private JMenuItem classRemoveField;
	private JMenuItem classEditField;
	private JMenuItem classAddMethod;
	private JMenuItem classRemoveMethod;
	private JMenuItem classEditMethod;
	private JMenuItem classAddRelationship;
	private JMenuItem classRemoveRelationship;
	
	// Mouse coordinates of the last time a right click was detected
	// 		as well as the last GUIClass that was selected, if one exists
	private int mouseX;
	private int mouseY;
	private GUIClass prev;
	
	/**
	 * Create a panel that allows components to be drawn and dragged by the user.
	 * Also allows drawing of relationship types.
	 * @param view- the parent view
	 * @param isHuman - whether the view is for a human or testing
	 */
	public DiagramPanel(GUIView view, boolean isHuman) {
		super(isHuman);
		setupOps(view);
	}
	
	private void setupOps(GUIView view) {
		this.view = view;
		
		// To enable dragging and dynamic locations, remove the layout manager
		setLayout(null);
		
		// Setup map of class names to guiClasses (very similar to class manager)
		guiClasses = new HashMap<String, GUIClass>();
		
		// Set default size
		setPreferredSize(new Dimension(600, 600));
		
		// Add component listener to set prefferedSize
		addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				setPreferredSize(getSize());
			}
		});
		
		// Add mouse listener for clicks
		addMouseListener(this);
		
		setupMenus();
		setupActions();
	}
	
	/**
	 * Override paint component so we can draw in the relationships
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		// Enable better 2D graphics
		Graphics2D g2d = (Graphics2D)g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		// Get the relationships and draw the relationship between the two classes
		HashMap<String, UMLRelationship> relations = view.getController().getModel().getRelationships();
		
		// Loop through relationships and draw the lines
		for(Map.Entry<String, UMLRelationship> entry : relations.entrySet()) {
			// Get the relationship
			UMLRelationship relation = entry.getValue();
			
			// Get each GUIClass
			GUIClass c1 = guiClasses.get(relation.getClass1().getName());
			GUIClass c2 = guiClasses.get(relation.getClass2().getName());
			
			// Check if line should be dashed
			if(relation.getType().toLowerCase().equals("realization")) {
		        BasicStroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{4}, 0);
				g2d.setStroke(dashed);
			}
			else {
				g2d.setStroke(new BasicStroke(1));
			}
			
			// Check to see if the relationship is recursive or not
			//		Handle drawing differently
			
			// If not recursive then draw lines from midpoints of classes
			if(c1 != c2) {
				// Get center points of views
				int c1centerX = c1.getX() + c1.getWidth()/2;
				int c1centerY = c1.getY() + c1.getHeight()/2;
				int c2centerX = c2.getX() + c2.getWidth()/2;
				int c2centerY = c2.getY() + c2.getHeight()/2;
				
				// Draw horizontal line
				g2d.drawLine(c1centerX, c1centerY, c2centerX, c1centerY);
				
				// Draw vertical line
				g2d.drawLine(c2centerX, c1centerY, c2centerX, c2centerY);
				
				// Draw diamond/arrow
				int rectLen = 10;
				int rectX = c2centerX - rectLen/2;
				int rectY = 0;
				int angleRotate = 45;
				
				// Check how which way we need to rotate the rectangle and determine
				// 	where the location should really be based on the classes relative
				//	locations to each other
				if(Math.abs(c2centerY - c1centerY) < c2.getHeight()/2) {
					rectY = c2centerY - (c2centerY - c1centerY) - rectLen/2;
					if(c2centerX > c1centerX) {
						rectX = c2.getX() - rectLen;
						angleRotate += 90;
					}
					else if (c2centerX < c1centerX) {
						rectX = c2.getX() + c2.getWidth();
						angleRotate -= 90;
					}
				}
				else if(c2centerY < c1centerY)
					rectY = c2.getY() + c2.getHeight();
				else {
					rectY = c2.getY() - rectLen;
					angleRotate += 180;
				}
				
				// Rotate rectangle to create diamond shape
				AffineTransform old = g2d.getTransform();
				g2d.rotate(Math.toRadians(angleRotate), rectX + rectLen/2, rectY + rectLen/2);
				
				// Determine if we need to fill the rectangle or not
				if(relation.getType().toLowerCase().equals("composition"))
					g2d.fillRect(rectX, rectY, rectLen, rectLen);
				else if(relation.getType().equals("aggregation")) {
					g2d.setColor(Color.WHITE);
					g2d.fillRect(rectX, rectY, rectLen, rectLen);
					g2d.setColor(Color.BLACK);
					g2d.drawRect(rectX, rectY, rectLen, rectLen);
				}
				// Determine if we need dashed line
				else {
					g2d.setStroke(new BasicStroke(1));
					g2d.drawLine(rectX, rectY, rectX + rectLen, rectY);
					g2d.drawLine(rectX, rectY, rectX, rectY + rectLen);
				}
				
				// Reset the rotation
				g2d.setTransform(old);
			}
			// Otherwise do a loop in the shape of approximately:
			//     ------|
			//   __|__   |
			//   |    |  |
			//   |____|--|
			//   
			else {
				// How far loop is away from class border
				int offset = 20;
				
				int farX = c1.getX() + c1.getWidth() + offset;
				int farY = c1.getY() - offset;
				
				// Center points of view
				int centerX = c1.getX() + c1.getWidth()/2;
				int centerY = c1.getY() + c1.getHeight()/2;
				
				g2d.drawLine(centerX, centerY, centerX, farY);
				g2d.drawLine(centerX, farY, farX, farY);
				g2d.drawLine(farX, farY, farX, centerY);
				g2d.drawLine(farX, centerY, centerX, centerY);
				
				int rectLen = 10;
				int rectX = centerX - rectLen/2;
				int rectY = c1.getY() - rectLen;
				int angleRotate = 225;
				
				// Rotate rectangle
				AffineTransform old = g2d.getTransform();
				g2d.rotate(Math.toRadians(angleRotate), rectX + rectLen/2, rectY + rectLen/2);
				if(relation.getType().toLowerCase().equals("composition"))
					g2d.fillRect(rectX, rectY, rectLen, rectLen);
				else if(relation.getType().equals("aggregation")) {
					g2d.setColor(Color.WHITE);
					g2d.fillRect(rectX, rectY, rectLen, rectLen);
					g2d.setColor(Color.BLACK);
					g2d.drawRect(rectX, rectY, rectLen, rectLen);
				}
				else {
					g2d.setStroke(new BasicStroke(1));
					g2d.drawLine(rectX, rectY, rectX + rectLen, rectY);
					g2d.drawLine(rectX, rectY, rectX, rectY + rectLen);
				}
				g2d.setTransform(old);
			}
		}
	}
	
	/**
	 * Setup the mouse and window menus and their items.
	 * Set names for all components for tests
	 */
	private void setupMenus() {
		// Create the popup menu for when a user right clicks in the diagram
		mouseMenu = view.isHuman() ? new JPopupMenu() : new TestablePopupMenu();
		mouseMenu.setName("Mouse Menu");
		
		// Initialize the menu items
		mouseAddClass = createMenuItem("Add Class", "mouseAddClass");
		mouseSaveFile = createMenuItem("Save to File", "mouseSave");
		mouseLoadFile = createMenuItem("Load File", "mouseLoad");
		mouseExportPNG = createMenuItem("Export to PNG", "mouseExport");
		
		// Add menu items to mouse menu
		mouseMenu.add(mouseAddClass);
		mouseMenu.addSeparator();
		mouseMenu.add(mouseSaveFile);
		mouseMenu.add(mouseLoadFile);
		mouseMenu.add(mouseExportPNG);
		
		// Create the popup menu for when a user right clicks a class
		// 		NOTE: Displaying this is handled in the GUIView's mouse listeners
		classMenu = new JPopupMenu();
		classMenu.setName("Class Menu");
		
		// Initialize class menu options
		classRemoveClass = createMenuItem("Remove Class", "classRemoveClass");
		classEditClass = createMenuItem("Edit Class Name", "classEditClass");
		
		classAddField = createMenuItem("Add Field", "classAddField");
		classRemoveField = createMenuItem("Remove Field", "classRemoveField");
		classEditField = createMenuItem("Edit Field Name", "classEditField");

		classAddMethod = createMenuItem("Add Method", "classAddMethod");
		classRemoveMethod = createMenuItem("Remove Method", "classRemoveMethod");
		classEditMethod = createMenuItem("Edit Method Name", "classEditMethod");
		
		classAddRelationship = createMenuItem("Add Relationship", "classAddRelationship");
		classRemoveRelationship = createMenuItem("Remove Relationship", "classRemoveRelationship");
		
		// Add items to class menu
		classMenu.add(classRemoveClass);
		classMenu.add(classEditClass);
		classMenu.addSeparator();
		classMenu.add(classAddField);
		classMenu.add(classRemoveField);
		classMenu.add(classEditField);
		classMenu.addSeparator();
		classMenu.add(classAddMethod);
		classMenu.add(classRemoveMethod);
		classMenu.add(classEditMethod);
		classMenu.addSeparator();
		classMenu.add(classAddRelationship);
		classMenu.add(classRemoveRelationship);
		
		
		// Initialize the main menu items
		mainAddClass = createMenuItem("Add Class", "mainAddClass");
		mainSaveFile = createMenuItem("Save to File", "mainSave");
		mainLoadFile = createMenuItem("Load File", "mainLoad");
		mainExportPNG = createMenuItem("Export to PNG", "mainExport");
		mainResize = createMenuItem("Resize", "mainResize");
		
		//main Bar initialization
		mainMenuBar = view.isHuman() ? new JMenuBar() : new TestableMenuBar();
		mainMenuBar.setName("mainMenuBar");
		
		//main menu initialization
		if(view.isHuman()) {
			mainFile = new JMenu("File");
			mainActions = new JMenu("Actions");
		}
		else {
			mainFile = new TestableMenu();
			mainActions = new TestableMenu();
		}
		mainFile.setName("mainFile");
		mainActions.setName("mainActions");
		
		// Add menu items to mouse menu
		mainFile.add(mainAddClass);
		mainFile.addSeparator();
		mainFile.add(mainSaveFile);
		mainFile.add(mainLoadFile);
		mainFile.addSeparator();
		mainFile.add(mainExportPNG);
		mainFile.addSeparator();
		mainFile.add(mainResize);
		
		//main menu items initialization 
		mainRemoveClass = createMenuItem("Remove Class", "mainRemoveClass");
		mainEditClass = createMenuItem("Edit Class Name", "mainEditClass");
				
		mainAddField = createMenuItem("Add Field", "mainAddField");
		mainRemoveField = createMenuItem("Remove Field", "mainRemoveField");
		mainEditField = createMenuItem("Edit Field Name", "mainEditField");

		mainAddMethod = createMenuItem("Add Method", "mainAddMethod");
		mainRemoveMethod = createMenuItem("Remove Method", "mainRemoveMethod");
		mainEditMethod = createMenuItem("Edit Method Name", "mainEditMethod");
				
		mainAddRelationship = createMenuItem("Add Relationship", "mainAddRelationship");
		mainRemoveRelationship = createMenuItem("Remove Relationship", "mainRemoveRelationship");
		
		// Add items to mainActions
		mainActions.add(mainRemoveClass);
		mainActions.add(mainEditClass);
		mainActions.addSeparator();
		mainActions.add(mainAddField);
		mainActions.add(mainRemoveField);
		mainActions.add(mainEditField);
		mainActions.addSeparator();
		mainActions.add(mainAddMethod);
		mainActions.add(mainRemoveMethod);
		mainActions.add(mainEditMethod);
		mainActions.addSeparator();
		mainActions.add(mainAddRelationship);
		mainActions.add(mainRemoveRelationship);
		
		mainMenuBar.add(mainFile);
		mainMenuBar.add(mainActions);
		if(view.isHuman())
			view.getWindow().setJMenuBar(mainMenuBar);
	}
	
	/**
	 * Get a list of the valid relationship types
	 * @return String array of types
	 */
	private String[] validRelationships() {
		String[] relationships = {"aggregation", "composition", "inheritance", "realization"};
		return relationships;
	}
	
	
	/**
	 * Get a menu item with the specified label and set its name
	 * @param label - Text to display
	 * @param name - Name for testing purposes
	 */
	private JMenuItem createMenuItem(String label, String name) {
		JMenuItem temp;
		if(view.isHuman())
			temp = new JMenuItem(label);
		else
			temp = new TestableMenuItem();
		temp.setName(name);
		return temp;
	}
	
	/**
	 * Setup the actions for buttons
	 */
	private void setupActions() {
		// Setup actions for generic mouse menu items
		mouseAddClass.addActionListener(addClassAction());
		mouseSaveFile.addActionListener(saveFileAction());
		mouseLoadFile.addActionListener(loadFileAction());

		mainAddClass.addActionListener(addClassAction());
		mainSaveFile.addActionListener(saveFileAction());
		mainLoadFile.addActionListener(loadFileAction());
		mainExportPNG.addActionListener(exportPNGAction());
		mainResize.addActionListener(resizeAction());
		mouseExportPNG.addActionListener(exportPNGAction());
		
		// Setup actions for class menu items
		classRemoveClass.addActionListener(removeClassAction());
		classEditClass.addActionListener(editClassAction());
		mainRemoveClass.addActionListener(removeClassAction());
		mainEditClass.addActionListener(editClassAction());
		
		classAddField.addActionListener(addFieldAction());
		classRemoveField.addActionListener(removeFieldAction());
		classEditField.addActionListener(editFieldAction());
		mainAddField.addActionListener(addFieldAction());
		mainRemoveField.addActionListener(removeFieldAction());
		mainEditField.addActionListener(editFieldAction());
		
		
		classAddMethod.addActionListener(addMethodAction());
		classRemoveMethod.addActionListener(removeMethodAction());
		classEditMethod.addActionListener(editMethodAction());
		mainAddMethod.addActionListener(addMethodAction());
		mainRemoveMethod.addActionListener(removeMethodAction());
		mainEditMethod.addActionListener(editMethodAction());
		
		
		classAddRelationship.addActionListener(addRelationshipAction());
		classRemoveRelationship.addActionListener(removeRelationshipAction());
		mainAddRelationship.addActionListener(addRelationshipAction());
		mainRemoveRelationship.addActionListener(removeRelationshipAction());
		
	}
	
	/**
	 * Get an action listener that will add a class
	 * @return - ActionListener with definition for adding a class
	 */
	private ActionListener addClassAction() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Prompt user for class name
				Object className = view.promptInput("Enter class name:");
				
				// Make sure user did not cancel input
				if(className != null) {
					// Check if source is from the main menu, if so then offset the location
					if(e.getSource() == mainAddClass) {
						mouseX = 0;
						mouseY = 0;
						Component temp;
						while((temp = findComponentAt(mouseX, mouseY)) != null && temp != DiagramPanel.this) {
							mouseX += 10;
							mouseY += 10;
						}
					}
					int result = view.getController().addClass(className.toString(), mouseX, mouseY);
					if(result != 0)
						view.showError(DiagramPanel.this, result);
				}
				
				// Reset prev
				prev = null;
			}
		};
	}
	
	/**
	 * Get an action listener that will remove a class
	 * @return - ActionListener with definition for removing a class
	 */
	private ActionListener removeClassAction() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Make sure at least one class exists
				if(view.getModel().getClassNames().length == 0)
					return;
				
				// Make sure a selected class exists
				if(prev == null || e.getSource() == mainRemoveField)
				{
					Object[] classChoices = view.getController().getModel().getClassNames();
					Object originClass = view.promptSelection("Choose avaliable class:", classChoices);
					GUIClass temp = guiClasses.get(originClass);
					if (temp != null)
						prev = temp;
					else
					{
						ErrorHandler.setCode(109);
						return;	
					}
				}
				
				int result = view.getController().removeClass(prev.getName());
				if(result != 0)
					view.showError(DiagramPanel.this, result);
				
				// Reset prev
				prev = null;
			}
		};
	}
	
	/**
	 * Get an action listener that will edit a class's name
	 * @return - ActionListener with definition for editing a class name
	 */
	private ActionListener editClassAction() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {	
				// Make sure at least one class exists
				if(view.getModel().getClassNames().length == 0)
					return;
				
				//listener for main menu buttons
				if(prev == null || e.getSource() == mainEditClass)
				{
					Object[] classChoices = view.getController().getModel().getClassNames();
					Object originClass = view.promptSelection("Choose avaliable class containing class:", classChoices);
					GUIClass temp = guiClasses.get(originClass);
					if (temp != null)
						prev = temp;
					else
					{
						ErrorHandler.setCode(109);
						return;	
					}
				}
				
				// Prompt the user for a new class name
				Object className = view.promptInput("Enter new class name:");
				
				// Make sure user did not cancel input
				if(className != null) {
					int result = view.getController().editClass(prev.getName(), className.toString());
					if(result != 0)
						view.showError(DiagramPanel.this, result);
				}
					
				// Reset prev
				prev = null;
			}
		};
	}
	
	/**
	 * Get an action listener that will add a field to a given class
	 * @return - ActionListener with definition for adding a field
	 */
	private ActionListener addFieldAction() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Make sure at least one class exists
				if(view.getModel().getClassNames().length == 0)
					return;
				
				//listener for main menu buttons
				if(prev == null || e.getSource() == mainAddField)
				{
					Object[] classChoices = view.getController().getModel().getClassNames();
					Object originClass = view.promptSelection("Choose avaliable class to add field:", classChoices);
					GUIClass temp = guiClasses.get(originClass);
					if (temp != null)
						prev = temp;
					else
					{
						ErrorHandler.setCode(109);
						return;	
					}
				}
				
				// Prompt user for field type
				Object fieldType = view.promptInput("Enter field type (i.e. int, double, etc.): ");
				
				// Prompt user for field name
				Object fieldName = view.promptInput("Enter field name:");
				
				// Make sure user did not cancel input
				if(fieldName != null && fieldType != null) {
					int result = view.getController().addField(prev.getName(), fieldType.toString(), fieldName.toString());
					if(result != 0)
						view.showError(DiagramPanel.this, result);
				}
				
				prev = null;
			}
		};
	}
	
	/**
	 * Get an action listener that will remove a field from a given class
	 * @return - ActionListener with definition for removing a field
	 */
	private ActionListener removeFieldAction() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Make sure at least one class exists
				if(view.getModel().getClassNames().length == 0)
					return;
				
				//listener for main menu buttons
				if(prev == null || e.getSource() == mainRemoveField)
				{
					Object[] classChoices = view.getController().getModel().getClassNames();
					Object originClass = view.promptSelection("Choose class containing field:", classChoices);
					GUIClass temp = guiClasses.get(originClass);
					if (temp != null)
						prev = temp;
					else
					{
						ErrorHandler.setCode(109);
						return;	
					}
				}
				
				// Get a list of the available field names to remove
				Object[] availableOptions = view.getController().getModel().getClass(prev.getName()).getFields().keySet().toArray();
				
				// Make sure there is at least one field
				if(availableOptions.length > 0) {
					Object fieldName = view.promptSelection("Choose field name:", availableOptions);
					// Make sure user didn't cancel input
					if(fieldName != null) {
						int result = view.getController().removeField(prev.getName(), fieldName.toString());
						if(result != 0)
							view.showError(DiagramPanel.this, result);
					}
				}
				
				prev = null;
			}
		};
	}
	
	/**
	 * Get an action listener that will edit a field name from a given class
	 * @return - ActionListener with definition for editing the name of a field
	 */
	private ActionListener editFieldAction() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Make sure at least one class exists
				if(view.getModel().getClassNames().length == 0)
					return;
				
				//listener for main menu buttons
				if(prev == null || e.getSource() == mainEditField)
				{
					Object[] classChoices = view.getController().getModel().getClassNames();
					Object originClass = view.promptSelection("Choose class containing field:", classChoices);
					GUIClass temp = guiClasses.get(originClass);
					if (temp != null)
						prev = temp;
					else
					{
						ErrorHandler.setCode(109);
						return;	
					}
				}
				
				// Get a list of the available field names to edit
				Object[] availableOptions = view.getController().getModel().getClass(prev.getName()).getFields().keySet().toArray();
				
				// Make sure there is at least one field
				if(availableOptions.length > 0) {
					Object fieldName = view.promptSelection("Choose field name:", availableOptions);
					// Make sure user didn't cancel input
					if(fieldName != null) {
						// Prompt for the new name
						Object newFieldName = view.promptInput("Enter the new name of the field");
						
						// Make sure user didn't cancel
						if(newFieldName != null) {
							int result = view.getController().editField(prev.getName(), fieldName.toString(), newFieldName.toString());
							if(result != 0)
								view.showError(DiagramPanel.this, result);
						}
					}
				}
				
				prev = null;
			}
		};
	}
	
	/**
	 * Get an action listener that will add a method to a given class
	 * @return - ActionListener with definition for adding a method
	 */
	private ActionListener addMethodAction() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Make sure at least one class exists
				if(view.getModel().getClassNames().length == 0)
					return;
				
				//listener for main menu buttons
				if(prev == null || e.getSource() == mainAddMethod)
				{
					Object[] classChoices = view.getController().getModel().getClassNames();
					Object originClass = view.promptSelection("Choose avaliable class:", classChoices);
					GUIClass temp = guiClasses.get(originClass);
					if (temp != null)
						prev = temp;
					else
					{
						ErrorHandler.setCode(109);
						return;	
					}
				}
				
				// Prompt user for return type
				Object returnType = view.promptInput("Enter return type:");
				
				// Prompt user for field name
				Object methodName = view.promptInput("Enter method name:");
				
				// Prompt user for argument list
				Object argList = view.promptInput("Parameters (separated by commas):");
				
				// Make sure user did not cancel input
				if(methodName != null && returnType != null && argList != null) {
					int result = view.getController().addMethod(prev.getName(), returnType.toString(),
							methodName.toString(), argList.toString());
					if(result != 0)
						view.showError(DiagramPanel.this, result);
				}
				
				prev = null;
			}
		};
	}
	
	/**
	 * Get an action listener that will remove a method from a given class
	 * @return - ActionListener with definition for removing a method
	 */
	private ActionListener removeMethodAction() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Make sure at least one class exists
				if(view.getModel().getClassNames().length == 0)
					return;
				
				//listener for main menu buttons
				if(prev == null || e.getSource() == mainRemoveMethod)
				{
					Object[] classChoices = view.getController().getModel().getClassNames();
					Object originClass = view.promptSelection("Choose class containing method:", classChoices);
					GUIClass temp = guiClasses.get(originClass);
					if (temp != null)
						prev = temp;
					else
					{
						ErrorHandler.setCode(109);
						return;	
					}
				}
				
				// Get a list of the available field names to remove
				Object[] availableOptions = view.getController().getModel().getClass(prev.getName()).getMethods().keySet().toArray();
				
				// Make sure there is at least one field
				if(availableOptions.length > 0) {
					Object methodName = view.promptSelection("Choose method name:", availableOptions);
					// Make sure user didn't cancel input
					if(methodName != null) {
						Method method = view.getModel().getClass(prev.getName()).getMethods().get(methodName);
						// Make sure method exists
						if(method == null) {
							view.showError(DiagramPanel.this, ErrorHandler.setCode(406));
							return;
						}
						// Strip parameters
						String methodParams = method.getParams().substring(1, method.getParams().length()-1);
						int result = view.getController().removeMethod(prev.getName(), method.getName(), methodParams);
						if(result != 0)
							view.showError(DiagramPanel.this, result);
					}
				}
				
				prev = null;
			}
		};
	}
	
	
	/**
	 * Get an action listener that will edit a method name from a given class
	 * @return - ActionListener with definition for editing the name of a method
	 */
	private ActionListener editMethodAction() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Make sure at least one class exists
				if(view.getModel().getClassNames().length == 0)
					return;
				
				//listener for main menu buttons
				if(prev == null || e.getSource() == mainEditMethod)
				{
					Object[] classChoices = view.getController().getModel().getClassNames();
					Object originClass = view.promptSelection("Choose class containing method:", classChoices);
					GUIClass temp = guiClasses.get(originClass);
					if (temp != null)
						prev = temp;
					else
					{
						ErrorHandler.setCode(109);
						return;	
					}
				}

				// Get a list of the available method names to edit
				Object[] availableOptions = view.getController().getModel().getClass(prev.getName()).getMethods().keySet().toArray();
				
				// Make sure there is at least one method
				if(availableOptions.length > 0) {
					Object methodName = view.promptSelection("Choose method name:", availableOptions);
					// Make sure user didn't cancel input
					if(methodName != null) {
						// Prompt for the new name
						Object newMethodName = view.promptInput("Enter the new name of the method");
						
						// Make sure user didn't cancel
						if(newMethodName != null) {
							Method method = view.getModel().getClass(prev.getName()).getMethods().get(methodName);
							// Strip parameters
							String methodParams = method.getParams().substring(1, method.getParams().length() -1);
							int result = view.getController().editMethod(prev.getName(), method.getName(), newMethodName.toString(), methodParams);
							if(result != 0)
								view.showError(DiagramPanel.this, result);
						}
					}
				}
				
				prev = null;
			}
		};
	}
	
	/**
	 * Get an action listener that will add a relationship between two classes
	 * @return
	 */
	private ActionListener addRelationshipAction() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Make sure at least one class exists
				if(view.getModel().getClassNames().length == 0)
					return;
				
				//listener for main menu buttons
				if(prev == null || e.getSource() == mainAddRelationship)
				{
					Object[] classChoices = view.getController().getModel().getClassNames();
					Object originClass = view.promptSelection("Choose origin class: ", classChoices);
					GUIClass temp = guiClasses.get(originClass);
					if (temp != null)
						prev = temp;
					else
					{
						ErrorHandler.setCode(109);
						return;	
					}
				}

				// Get a list of available second classes
				Object[] availableClasses = view.getController().getModel().getClassNames();
				
				// Make sure there is at least one other class
				if(availableClasses.length > 0) {
					// Get the destination class
					Object destClass = view.promptSelection("Destination class: ", availableClasses);
					// Get the type
					Object type = view.promptSelection("Relationship Type:", validRelationships());
					// Make sure user didn't cancel input
					if(destClass != null && type != null) {
						int result = view.getController().addRelationship(prev.getName(), type.toString(), destClass.toString());
						if(result != 0)
							view.showError(DiagramPanel.this, result);
					}
				}
				
				prev = null;
			}
		};
	}
	
	/**
	 * Get an action listener that will remove a relationship between two classes
	 * @return
	 */
	private ActionListener removeRelationshipAction() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Make sure at least one class exists
				if(view.getModel().getClassNames().length == 0)
					return;
				
				//listener for main menu buttons
				if(prev == null || e.getSource() == mainRemoveRelationship)
				{
					Object[] classChoices = view.getController().getModel().getClassNames();
					Object originClass = view.promptSelection("Choose origin class: ", classChoices);
					GUIClass temp = guiClasses.get(originClass);
					if (temp != null)
						prev = temp;
					else
					{
						ErrorHandler.setCode(109);
						return;	
					}
				}
				
				// Get a list of available second classes
				Object[] availableClasses = view.getController().getModel().getClassNames();
				
				// Make sure there is at least one field
				if(availableClasses.length > 0) {
					// Get the destination class
					Object destClass = view.promptSelection("Destination class: ", availableClasses);
					// Get the type
					Object type = view.promptSelection("Relationship Type: ", validRelationships());
					// Make sure user didn't cancel input
					if(destClass != null && type != null) {
						int result = view.getController().removeRelationship(prev.getName(), type.toString(), destClass.toString());
						if(result != 0)
							view.showError(DiagramPanel.this, result);
					}
				}
				
				prev = null;
			}
		};
	}

	/**
	 * Get an action listener that will open a save dialog box to save the file too
	 * @return
	 */
	private ActionListener saveFileAction() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Prompt user for file
				File saveFile = view.getFile("", "", "Save UNL", JFileChooser.SAVE_DIALOG);
				
				// Make sure user didn't close the console
				if(saveFile != null) {
					
					// Check if file name ends with '.json' and if not add it manually
					if(!saveFile.getPath().endsWith(".json"))
						saveFile = new File(saveFile.getAbsolutePath() + ".json");
					
					// Save the file
					UMLFileIO fileIO = new UMLFileIO();
					
					// Set the path
					int result = fileIO.setFile(saveFile.getAbsolutePath());
					if(result != 0) {
						view.showError(DiagramPanel.this, result);
						return;
					}
					
					// Write to file
					String json = view.getController().getModel().convertToJSON();
					result = fileIO.writeToFile(json);
					if(result != 0) {
						view.showError(DiagramPanel.this, result);
						return;
					}
				}
			}
		};
	}
	
	/**
	 * Get an action listener that will open a load dialog box to load the file from
	 * @return
	 */
	private ActionListener loadFileAction() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				// Prompt for file
				File loadFile = view.getFile("JSON", ".json", "Load File", JFileChooser.OPEN_DIALOG);
				
				// Make sure user didn't close the console
				if(loadFile != null) {
					
					// Open the file
					UMLFileIO fileIO = new UMLFileIO();
					
					// Set the path
					int result = fileIO.setFile(loadFile.getAbsolutePath());
					if(result != 0) {
						view.showError(DiagramPanel.this, result);
						return;
					}
					
					// Read from file
					Object[] res = fileIO.readFile();
					if((int)res[1] != 0) {
						view.showError(DiagramPanel.this, result);
						return;
					}
					
					// Load file
					result = view.getController().getModel().parseJSON((String)res[0]);
					if(result != 0) {
						view.showError(DiagramPanel.this, result);
						return;
					}
				
					// Manually add all classes to the GUI
					for(Object classNameObj : view.getController().getModel().getClassNames()) {
						String className = (String)classNameObj;
						// Forcefully call updated
						updated(null, "addClass", (Object)view.getController().getModel().getClass(className));
					}
					
					validate();
				}
			}
		};
	}
	
	/**
	 * Get an action listener that will export to a PNG file
	 * @return
	 */
	private ActionListener exportPNGAction() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Prompt user for file
				File exportFile = view.getFile("PNG", ".png", "Export to PNG", JFileChooser.SAVE_DIALOG);
				
				// Make sure the user didn't cancel the operation
				if(exportFile != null) {
					// Check if file name ends with '.png' and if not manually add it
					if(!exportFile.getPath().endsWith(".png")) {
						exportFile = new File(exportFile.getAbsolutePath() + ".png");
					}
					
					// Write screen to image
					// Temp image to copy the drawn screen to
					BufferedImage image = new BufferedImage(Math.max(getWidth(), 100), Math.max(getHeight(), 100), BufferedImage.TYPE_INT_RGB);
					// Graphics for the image
					Graphics2D graphics = image.createGraphics();
					// Copy component graphics to image graphics
					paintAll(graphics);
					// Export the image
					try {
						ImageIO.write(image, "png", exportFile);
					} catch(IOException er) {
						view.showError(DiagramPanel.this, 111);
					}
				}
			}
		};
	}
	
	/**
	 * Action to set the size of the UML Diagram
	 * @return
	 */
	private ActionListener resizeAction() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int newWidth, newHeight;

				// Prompt user for dimensions
				Object widthInput = view.promptInput("New width: ");
				// Make sure user didn't cancel
				if(widthInput == null)
					return;
				Object heightInput = view.promptInput("New height: ");
				// Make sure user didn't cancel
				if(heightInput == null)
					return;
				
				// Cast input as int
				try {
					newWidth = Integer.parseInt(widthInput.toString());
					newHeight = Integer.parseInt(heightInput.toString());
				} catch(NumberFormatException nfe) {
					view.showError(DiagramPanel.this, 112);
					return;
				}
				
				// Resize panel
				setPreferredSize(new Dimension(newWidth, newHeight));
				view.updateFrame();
			}
		};
	}

	/**
	 * Listen for changes from the model
	 */
	@Override
	public void updated(Observable src, String tag, Object data) {
		// Check the action and react accordingly
		if(tag.equals("addClass")) {
			// Cast data as UMLClass
			UMLClass umlClass = (UMLClass)data;
			
			// Create GUIClass representation
			GUIClass temp = view.isHuman() ? new GUIClass(umlClass, true) : new GUIClass(umlClass, false);
			// Add a mouse listener to allow dragging and different options
			temp.addMouseListener(this);
			temp.addMouseMotionListener(this);
			guiClasses.put(umlClass.getName(), temp);
			
			// Add GUIClass to panel
			add(temp);
		}
		else if(tag.equals("removeClass")) {
			// Find the GUIClass that correlates to the removed object and delete it
			UMLClass removed = (UMLClass)data;
			remove(guiClasses.get(removed.getName()));
			guiClasses.remove(removed.getName());
		}
		else if(tag.equals("fieldChange")) {
			// Cast data as UMLClass
			UMLClass umlClass = (UMLClass)data;
			
			// Update associated GUIClass data
			guiClasses.get(umlClass.getName()).updateFields();
		}
		else if(tag.equals("methodChange")) {
			// Cast data as UMLClass
			UMLClass umlClass = (UMLClass)data;
			
			// Update associated GUIClass data
			guiClasses.get(umlClass.getName()).updateMethods();
		}
		else if(tag.equals("relationshipChange")) {
			// Nothing to do
		}
		else if(tag.equals("classChange")) {
			// Update names
			// Because you can't edit the name field you have to remove and readd the class
			// But you can't remove/add in the middle of a loop so you have to readd all classes
			// Remove all classes
			guiClasses.forEach((k, v) -> remove(v));
			guiClasses.clear();
			// Add them back
			for(Object className : view.getModel().getClassNames()) {
				updated(null, "addClass", view.getModel().getClass((String)className));
			}
		}
		
		// Update display
		validate();
		repaint();
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		// Destroy last saved guiClass;
		prev = null;
		
		// Get the source of the mouse event
		Object source = e.getSource();
		
		// Check if press is a left click
		if(SwingUtilities.isLeftMouseButton(e)) {
			// Loop through GUI classes and check if it triggered the event
			for(Map.Entry<String, GUIClass> entry : guiClasses.entrySet()) {
				GUIClass guiClass = entry.getValue();
				
				if(source == guiClass) {
					lastX = e.getLocationOnScreen().x - guiClass.getX();
					lastY = e.getLocationOnScreen().y - guiClass.getY();
				
					break;
				}
			}
		}
		// Check if press is a right click
		else if(SwingUtilities.isRightMouseButton(e)) {
			// Check if the source is the diagram, if so show the generic mouse menu
			if(source == this) {
				mouseX = e.getX();
				mouseY = e.getY();
				mouseMenu.show(this, mouseX, mouseY);
			}
			// Otherwise check if the source was a GUI Class
			else if(source instanceof GUIClass) {
				// Cast mouse source to GUIClass
				GUIClass guiClass = (GUIClass)e.getSource();
				mouseX = e.getX();
				mouseY = e.getY();
				prev = guiClass;
				classMenu.show(guiClass, mouseX, mouseY);
			}
		}
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		// Get the source of the mouse event
		Object source = e.getSource();
		
		// Check if press is a left click
		if(SwingUtilities.isLeftMouseButton(e)) {
			// Loop through GUI classes and check if it triggered the event
			for(Map.Entry<String, GUIClass> entry : guiClasses.entrySet()) {
				GUIClass guiClass = entry.getValue();
				
				if(source == guiClass) {
					// Set location of GUIClass
					int newX = e.getLocationOnScreen().x - lastX;
					int newY = e.getLocationOnScreen().y - lastY;
					guiClass.setLocation(newX, newY);
					
					lastX = e.getLocationOnScreen().x - guiClass.getX();
					lastY = e.getLocationOnScreen().y - guiClass.getY();
					
					// If the user is dragging a class then repaint in case the class has a relationship
					repaint();
				
					break;
				}
			}
		}
	}
	
	/**
	 * Find the component with the given name, or null if not found
	 * @param name
	 */
	public Component findComponent(String name) {
		// Check popup menus
		if(mouseMenu.getName().equals(name))
			return mouseMenu;
		if(classMenu.getName().equals(name))
			return classMenu;
		if(mainMenuBar.getName().equals(name))
			return mainMenuBar;
		
		// Check the main menu
		for(int i = 0; i < mainMenuBar.getMenuCount(); i++) {
			if(mainMenuBar.getMenu(i).getName().equals(name))
				return mainMenuBar.getMenu(i);
		}
		
		// Check main self children
		for(Component c : getComponents()) {
			if(c != null && c.getName().equals(name)) {
				return c;
			}
		}
		
		// Check menu children
		for(Component c : mouseMenu.getComponents()) {
			if(c != null && c.getName() != null && c.getName().equals(name)) {
				return c;
			}
		}
		for(Component c : classMenu.getComponents()) {
			if(c != null && c.getName() != null && c.getName().equals(name)) {
				return c;
			}
		}
		
		for(Component c : mainFile.getComponents()) {
			if(c != null && c.getName() != null && c.getName().equals(name)) {
				return c;
			}
		}
		for(Component c : mainActions.getComponents()) {
			if(c != null && c.getName() != null && c.getName().equals(name)) {
				return c;
			}
		}
		
		return null;
	}

	/**
	 * @return the guiClasses
	 */
	public HashMap<String, GUIClass> getGuiClasses() {
		return guiClasses;
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {}

	@Override
	public void mouseEntered(MouseEvent arg0) {}
	
	@Override
	public void mouseExited(MouseEvent arg0) {}

	@Override
	public void mouseReleased(MouseEvent arg0) {}

	@Override
	public void mouseMoved(MouseEvent arg0) {}
}
