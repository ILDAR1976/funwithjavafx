package edu.fx_01;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Locale;
import java.util.ResourceBundle;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit.ApplicationTest;

import edu.fx_01.ObjectsManipulation.BlockIha;
import edu.fx_01.ObjectsManipulation.LineIha;
import edu.fx_01.model.Utils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import edu.fx_01.ObjectsManipulation.*;

/**
 * Object manipulation test
 *
 */

public class AppTest extends ApplicationTest {
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

	@Before
	public void setUpStreams() {
	    System.setOut(new PrintStream(outContent));
	    System.setErr(new PrintStream(errContent));
	}

	Group root = new Group();
	ObjectsManipulation o = new ObjectsManipulation();
	
	public void assertContains(String str){
		if (outContent.toString().contains(str)) 
			assertTrue(true);
		else
			assertTrue(false);
	}
	
	
	@Override
	public void start(Stage stage) {
		stage.setTitle("Line Manipulation Sample");
		
		root.getChildren().addAll( 
			o.testBlockIha(50, 100), 
			o.testLineIha(10, 10, 590, 10, Color.RED),
		    o.testLineIha(10, 35, 590, 35, Color.BLUE),
		    o.testLineIha(10, 60, 590, 60, Color.GREEN),
			o.testBlockIha(50, 400), 
			o.testBlockIha(450, 100), 
			o.testBlockIha(450, 400),
			o.testLineIha(70, 250, 300, 400, Color.RED), 
			o.testLineIha(300, 400, 530, 250, Color.CHOCOLATE),
			o.testLineIha(150, 420, 450, 120, Color.BROWN), 
			o.testLineIha(150, 120, 450, 420, Color.BLACK));
		
		Scene scene = new Scene(root, 600, 600, Color.ALICEBLUE);
	
		o.testEnableDrag(scene, root);

		stage.setScene(scene);

		stage.show();

		o.testMakeLinks(root);

	}
	
	@Test
	public void infoByPoint() {
		Anchor testAnchor = ((LineIha)root.getChildren().get(2)).getEnd();
		
		String str = testAnchor.getID().substring(33) + " - " + testAnchor.getParent().toString().substring(20);
		
		rightClickOn(((BlockIha)root.getChildren().get(7)).getGroupList().get(0));
		clickOn(((LineIha)root.getChildren().get(0)).getStart());
		clickOn(((LineIha)root.getChildren().get(1)).getEnd());
		clickOn(((LineIha)root.getChildren().get(2)).getEnd());
		rightClickOn(((LineIha)root.getChildren().get(0)));
		
		assertContains(str);
	}
	
	@After
	public void cleanUpStreams() {
	    System.setOut(null);
	    System.setErr(null);
	}
}