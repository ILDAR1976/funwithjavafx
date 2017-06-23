package edu.fx_01;

import java.io.IOException;

import javafx.beans.property.DoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class Block extends AnchorPane {
	DoubleProperty x;
	DoubleProperty y;

	public Block(int x, int y) {
		relocate(x, y); 
		
		FXMLLoader fxmlLoader = new FXMLLoader(
				getClass().getResource("/block.fxml")
				);
		
		fxmlLoader.setRoot(this); 
		fxmlLoader.setController(this);
		
		getStyleClass().add("block-blue");
		
		try { 
			fxmlLoader.load();
        
		} catch (IOException exception) {
		    throw new RuntimeException(exception);
		}
	}
	
	@FXML
	private void initialize() {}

	public void relocateBlock(int x, int y) {
		relocate ( 
				(int) (x - (getBoundsInLocal().getWidth() / 2)),
				(int) (y - (getBoundsInLocal().getHeight() / 2))
			);
	}
}
