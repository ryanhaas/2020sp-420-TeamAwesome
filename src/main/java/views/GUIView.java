// Package name
package views;

// System imports
import java.awt.Dimension;
import java.io.File;
import java.util.HashMap;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

//Local imports
import controller.UMLController;
import core.ErrorHandler;
import model.UMLClassManager;
import observe.Observable;
import views.components.DiagramPanel;
import views.components.GUIClass;
import views.components.testable.TestableFileChooser;
import views.components.testable.TestableOptionPane;

/**
 * A graphical view of the UML editor
 * @author Ryan
 *
 */
public class GUIView extends View {
	private UMLClassManager model;
	private UMLController controller;
	
	// Window elements
	private JFrame window;
	private DiagramPanel umlDiagram;
	
	// Option pane
	private JOptionPane optionPane;
	
	// File chooser
	private JFileChooser fileChooser;
	
	/**
	 * Create a GUI view for a human
	 * @param controller
	 * @param model
	 */
	public GUIView(UMLController controller, UMLClassManager model) {
		this(controller, model, true);
	}
	
	/**
	 * Create a GUI view of the passed in model
	 * @param controller
	 * @param model
	 * @param isHuman
	 */
	public GUIView(UMLController controller, UMLClassManager model, boolean isHuman) {
		// Setup look and feel
		if(setLook() != 0);

		// Setup controller and model
		this.controller = controller;
		this.model = model;
		this.controller.addObserver(this);
		
		// Setup options
		if(isHuman) {
			// Set to a regular JOptionPane if human
			setOptionPane(new JOptionPane());
			setFileChooser(new JFileChooser());
		}
		// Set to a blank test pane otherwise (to avoid null pointers)
		else {
			setOptionPane(new TestableOptionPane(""));
			setFileChooser(new TestableFileChooser(new File("")));
		}
		
		setupWindow();
		setupDiagram();
		
		window.pack();
	}
	
	/**
	 * Set the view to be visible
	 */
	public void show() {
		window.setVisible(true);
	}
	
	/**
	 * Setup the GUI window with some default properties
	 */
	private void setupWindow() {
		// Initialize frame with title
		window = new JFrame("UML Editor");
		
		// Set the initial size of the window
		window.setPreferredSize(new Dimension(600, 600));
		
		// Set the close button behavior to exit the program
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	/**
	 * Initialize the diagram panel where the model will be represented
	 */
	private void setupDiagram() {
		// Setup a JPanel to display the classes and relationships
		umlDiagram = new DiagramPanel(this);
		
		// Add the umlDiagram to the list of listeners for model changes
		controller.addObserver(umlDiagram);
		
		// Add the diagram to the frame
		window.add(umlDiagram);
	}
	
	/**
	 * Prompt the user for String input
	 * @param message - The message directing the user what to enter
	 * @return - User entered String
	 */
	@SuppressWarnings("static-access")
	public Object promptInput(String message) {
		// Prompt the user for input and return the input
		return optionPane.showInputDialog(window, message);
	}
	
	/**
	 * Prompt the user for input given selection
	 * @param message - The message directing the user what to enter
	 * @return - User entered String
	 */
	@SuppressWarnings("static-access")
	public Object promptSelection(String message, Object[] options) {
		// Prompt the user for input with a list of selections and return the input
		return optionPane.showInputDialog(window, message, "Selection", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
	}
	
	/**
	 * Display an error message with the associated error
	 * @param parent - Parent component, can be null
	 * @param errorCode
	 */
	@SuppressWarnings("static-access")
	public void showError(JComponent parent, int errorCode) {
		// Create error message
		optionPane.showMessageDialog(parent, ErrorHandler.toString(errorCode), "Error", JOptionPane.ERROR_MESSAGE);
	}
	
	/**
	 * Open a JFileChooser and get a file from the user
	 * @param desc - description of the extension
	 * @param extension - the extension of the file, null if no filter
	 * @param title - the title of the windoer
	 * @param type - the type of chooser
	 * @return - The chosen file
	 */
	public File getFile(String desc, String extension, String title, int type) {
		// Set title of the chooser
		fileChooser.setDialogTitle(title);
		
		// Set extension filter to only allow extension
		fileChooser.setFileFilter(new FileNameExtensionFilter(desc, extension));
		
		// Choose a file
		int result;
		if(type == JFileChooser.SAVE_DIALOG) {
			result = fileChooser.showSaveDialog(window);
		}
		else {
			result = fileChooser.showOpenDialog(window);
		}
		
		// Make sure input wasn't cancel
		if(result == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			return file;
		}
		
		return null;
	}
	
	/**
	 * Get the instance of the controller
	 * @return
	 */
	public UMLController getController() {
		return controller;
	}
	
	/**
	 * Try to set the GUI look and feel to match the OS
	 * @return - return code, 0 if successful
	 */
	private int setLook() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch(Exception e) {
			return ErrorHandler.setCode(110);
		}
		return ErrorHandler.setCode(0);
	}
	
	/**
	 * Get the component that has the given name
	 * @param name - name of the component
	 * @return - associated JComponent
	 */
	public JComponent getComponent(String name) {
		return (JComponent)umlDiagram.findComponent(name);
	}
	
	/**
	 * Get the component at the specified location
	 * @param x - x coordinate contained in component
	 * @param y - y coordinate contained in component
	 * @return - associated JComponent
	 */
	public JComponent getComponent(int x, int y) {
		return (JComponent)umlDiagram.getComponentAt(x, y);
	}
	
	/**
	 * Get the entire window instance.
	 * @return - window JFrame
	 */
	public JFrame getWindow() {
		return window;
	}
	
	/**
	 * Get the instance of the diagram panel
	 * @return - diagramPanel
	 */
	public DiagramPanel getDiagram() {
		return umlDiagram;
	}
	
	/**
	 * Return the map of added GUIClasses
	 * @return
	 */
	public HashMap<String, GUIClass> getGUIClasses() {
		return umlDiagram.getGuiClasses();
	}
	
	/**
	 * Get the view's model
	 * @return - model
	 */
	public UMLClassManager getModel() {
		return model;
	}
	
	/**
	 * Set the option pane for prompts
	 * @param pane - option pane instance
	 */
	public void setOptionPane(JOptionPane pane) {
		this.optionPane = pane;
	}
	
	/**
	 * Set the file chooser for selecting files
	 * @param chooser - file chooser instance
	 */
	public void setFileChooser(JFileChooser chooser) {
		this.fileChooser = chooser;
	}

	@Override
	public void updated(Observable src, String tag, Object data) {
		
	}
}
