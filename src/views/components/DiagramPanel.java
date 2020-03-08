// Package name
package views.components;

//System imports
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import main.UMLFileIO;
// Local imports
import model.UMLClass;
import model.UMLRelationship;
import observe.Observable;
import observe.Observer;
import views.GUIView;

/**
 * A JPanel to display the UMLClasses and relationships
 * @author Ryan
 *
 */
public class DiagramPanel extends JPanel implements Observer, MouseListener, MouseMotionListener {
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
	
	// MenuItems for generic mouse menu
	private JMenuItem mouseAddClass;
	private JMenuItem mouseSaveFile;
	private JMenuItem mouseLoadFile;
	
	// MenuItems for class mouse menu
	private JMenuItem classRemoveClass;
	private JMenuItem classAddField;
	private JMenuItem classRemoveField;
	private JMenuItem classAddMethod;
	private JMenuItem classRemoveMethod;
	private JMenuItem classAddRelationship;
	private JMenuItem classRemoveRelationship;
	
	// Mouse coordinates of the last time a right click was detected
	// 		as well as the last GUIClass that was selected, if one exists
	private int mouseX;
	private int mouseY;
	private GUIClass prev;
	
	public DiagramPanel(GUIView view) {
		this.view = view;
		
		// To enable dragging and dynamic locations, remove the layout manager
		setLayout(null);
		
		// Setup map of class names to guiClasses (very similar to class manager)
		guiClasses = new HashMap<String, GUIClass>();
		
		// Add mouse listener for clicks
		addMouseListener(this);
		
		setupMenus();
		setupActions();
	}
	
	/**
	 * Override paint component so we can draw in the relationships
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		// Get the relationships and draw the relationship between the two classes
		HashMap<String, UMLRelationship> relations = view.getController().getModel().getRelationships();
		
		// Loop through relationships and draw the lines
		for(Map.Entry<String, UMLRelationship> entry : relations.entrySet()) {
			// Get the relationship
			UMLRelationship relation = entry.getValue();
			
			// Get each GUIClass
			GUIClass c1 = guiClasses.get(relation.getClass1().getName());
			GUIClass c2 = guiClasses.get(relation.getClass2().getName());
			
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
				g.drawLine(c1centerX, c1centerY, c2centerX, c1centerY);
				
				// Draw vertical line
				g.drawLine(c2centerX, c1centerY, c2centerX, c2centerY);
			}
			// Otherwise do a loop in the shape of approximately:
			//     ------|
			//   __|__   |
			//   |    |  |
			//   |____|--|
			//   
			else {
				// How far loop is away from class border
				int offset = 10;
				
				int farX = c1.getX() + c1.getWidth() + offset;
				int farY = c1.getY() - offset;
				
				// Center points of view
				int centerX = c1.getX() + c1.getWidth()/2;
				int centerY = c1.getY() + c1.getHeight()/2;
				
				g.drawLine(centerX, centerY, centerX, farY);
				g.drawLine(centerX, farY, farX, farY);
				g.drawLine(farX, farY, farX, centerY);
				g.drawLine(farX, centerY, centerX, centerY);
			}
		}
	}
	
	/**
	 * Setup the mouse and window menus and their items
	 */
	private void setupMenus() {
		// Create the popup menu for when a user right clicks in the diagram
		mouseMenu = new JPopupMenu();
		
		// Initialize the menu items
		mouseAddClass = new JMenuItem("Add Class");
		mouseSaveFile = new JMenuItem("Save to File");
		mouseLoadFile = new JMenuItem("Load File");
		
		// Add menu items to mouse menu
		mouseMenu.add(mouseAddClass);
		mouseMenu.addSeparator();
		mouseMenu.add(mouseSaveFile);
		mouseMenu.add(mouseLoadFile);
		
		// Create the popup menu for when a user right clicks a class
		// 		NOTE: Displaying this is handled in the GUIView's mouse listeners
		classMenu = new JPopupMenu();
		
		// Initialize class menu options
		classRemoveClass = new JMenuItem("Remove Class");
		
		classAddField = new JMenuItem("Add Field");
		classRemoveField = new JMenuItem("Remove Field");

		classAddMethod = new JMenuItem("Add Method");
		classRemoveMethod = new JMenuItem("Remove Method");
		
		classAddRelationship = new JMenuItem("Add Relationship");
		classRemoveRelationship = new JMenuItem("Remove Relationship");
		
		// Add items to class menu
		classMenu.add(classRemoveClass);
		classMenu.addSeparator();
		classMenu.add(classAddField);
		classMenu.add(classRemoveField);
		classMenu.addSeparator();
		classMenu.add(classAddMethod);
		classMenu.add(classRemoveMethod);
		classMenu.addSeparator();
		classMenu.add(classAddRelationship);
		classMenu.add(classRemoveRelationship);
	}
	
	/**
	 * Setup the actions for buttons
	 */
	private void setupActions() {
		// Setup actions for generic mouse menu items
		mouseAddClass.addActionListener(addClassAction());
		mouseSaveFile.addActionListener(saveFileAction());
		mouseLoadFile.addActionListener(loadFileAction());
		
		// Setup actions for class menu items
		classRemoveClass.addActionListener(removeClassAction());
		
		classAddField.addActionListener(addFieldAction());
		classRemoveField.addActionListener(removeFieldAction());
		
		classAddMethod.addActionListener(addMethodAction());
		classRemoveMethod.addActionListener(removeMethodAction());
		
		classAddRelationship.addActionListener(addRelationshipAction());
		classRemoveRelationship.addActionListener(removeRelationshipAction());
	}
	
	/**
	 * Get an action listener that will add a class
	 * @return - ActionListener with definition for adding a class
	 */
	private ActionListener addClassAction() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Prompt the user for a class name
				Object className = view.promptInput("Enter class name:");
				
				// Make sure user did not cancel input
				if(className != null) {
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
				// Make sure a selected class exists
				if(prev != null) {
					int result = view.getController().removeClass(prev.getName());
					if(result != 0)
						view.showError(DiagramPanel.this, result);
					
					// Reset prev
					prev = null;
				}
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
				// Make sure a selected class exists
				if(prev != null) {
					// Prompt user for field name
					Object fieldName = view.promptInput("Enter field name:");
					
					// Make sure user did not cancel input
					if(fieldName != null) {
						int result = view.getController().addField(prev.getName(), fieldName.toString());
						if(result != 0)
							view.showError(DiagramPanel.this, result);
					}
					
					prev = null;
				}
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
				// Make sure a selected class exists
				if(prev != null) {
					// Get a list of the available field names to remove
					Object[] availableOptions = view.getController().getModel().getClass(prev.getName()).getFields().toArray();
					
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
				// Make sure a selected class exists
				if(prev != null) {
					// Prompt user for field name
					Object methodName = view.promptInput("Enter field name:");
					
					// Make sure user did not cancel input
					if(methodName != null) {
						int result = view.getController().addMethod(prev.getName(), methodName.toString());
						if(result != 0)
							view.showError(DiagramPanel.this, result);
					}
					
					prev = null;
				}
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
				// Make sure a selected class exists
				if(prev != null) {
					// Get a list of the available field names to remove
					Object[] availableOptions = view.getController().getModel().getClass(prev.getName()).getMethods().toArray();
					
					// Make sure there is at least one field
					if(availableOptions.length > 0) {
						Object methodName = view.promptSelection("Choose method name:", availableOptions);
						// Make sure user didn't cancel input
						if(methodName != null) {
							int result = view.getController().removeMethod(prev.getName(), methodName.toString());
							if(result != 0)
								view.showError(DiagramPanel.this, result);
						}
					}
					
					prev = null;
				}
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
				// Make sure a selected class exists
				if(prev != null) {
					// Get a list of available second classes
					Object[] availableClasses = view.getController().getModel().getClassNames();
					
					// Make sure there is at least one field
					if(availableClasses.length > 0) {
						// Get the destination class
						Object destClass = view.promptSelection("Destination class: ", availableClasses);
						// Make sure user didn't cancel input
						if(destClass != null) {
							int result = view.getController().addRelationship(prev.getName(), destClass.toString());
							if(result != 0)
								view.showError(DiagramPanel.this, result);
						}
					}
					
					prev = null;
				}
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
				// Make sure a selected class exists
				if(prev != null) {
					// Get a list of available second classes
					Object[] availableClasses = view.getController().getModel().getClassNames();
					
					// Make sure there is at least one field
					if(availableClasses.length > 0) {
						// Get the destination class
						Object destClass = view.promptSelection("Destination class: ", availableClasses);
						// Make sure user didn't cancel input
						if(destClass != null) {
							int result = view.getController().removeRelationship(prev.getName(), destClass.toString());
							if(result != 0)
								view.showError(DiagramPanel.this, result);
						}
					}
					
					prev = null;
				}
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
				// Create save dialog instance
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("Save UML");
				
				// Choose file
				int result = fileChooser.showSaveDialog(DiagramPanel.this);
				
				// Make sure user didn't close the console
				if(result == JFileChooser.APPROVE_OPTION) {
					File saveFile = fileChooser.getSelectedFile();
					
					// Check if file name ends with '.json' and if not add it manually
					if(!saveFile.getPath().endsWith(".json"))
						saveFile = new File(saveFile.getAbsolutePath() + ".json");
					
					// Save the file
					UMLFileIO fileIO = new UMLFileIO();
					
					// Set the path
					result = fileIO.setFile(saveFile.getAbsolutePath());
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
				// Create load dialog instance
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("Save UML");
				
				// Set extension filter to only allow JSON files
				FileNameExtensionFilter filter = new FileNameExtensionFilter("JSON Files", "json");
				fileChooser.setFileFilter(filter);
				
				// Choose file
				int result = fileChooser.showOpenDialog(DiagramPanel.this);
				
				// Make sure user didn't close the console
				if(result == JFileChooser.APPROVE_OPTION) {
					File loadFile = fileChooser.getSelectedFile();
					
					// Open the file
					UMLFileIO fileIO = new UMLFileIO();
					
					// Set the path
					result = fileIO.setFile(loadFile.getAbsolutePath());
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
	 * Listen for changes from the model
	 */
	@Override
	public void updated(Observable src, String tag, Object data) {
		// Check the action and react accordingly
		if(tag.equals("addClass")) {
			// Cast data as UMLClass
			UMLClass umlClass = (UMLClass)data;
			
			// Create GUIClass representation
			GUIClass temp = new GUIClass(umlClass);
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
