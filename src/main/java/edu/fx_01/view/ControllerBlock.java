package edu.fx_01.view;

import java.io.IOException;
import java.util.ArrayList;

import edu.fx_01.ObjectsManipulation.Anchor;
import javafx.beans.property.DoubleProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class ControllerBlock extends AnchorPane {
	private DoubleProperty x;
	private DoubleProperty y;
	private ArrayList<Anchor> groupList = null;
	private ControllerBlock self = this;

	public ControllerBlock(int x, int y) {

		groupList = new ArrayList<Anchor>();

		relocateBlock(x, y);
		

		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/" + getPackageNamePattern() + "/block.fxml"));

		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		getStyleClass().add("block-blue");

		try {
			fxmlLoader.load();

		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}

		enableDrag();
	}

	@FXML
	private void initialize() {
	}

	public void relocateBlock(int x, int y) {
		relocate((int) (x - (getBoundsInLocal().getWidth() / 2)), (int) (y - (getBoundsInLocal().getHeight() / 2)));
	}

	private void enableDrag() {

		setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				getScene().setCursor(Cursor.MOVE);
			}
		});
		setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (getScene() != null)
					getScene().setCursor(Cursor.HAND);
			}
		});
		setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {

				relocateBlock((int) mouseEvent.getSceneX(), (int) mouseEvent.getSceneY());
			}
		});
		setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (!mouseEvent.isPrimaryButtonDown()) {
					getScene().setCursor(Cursor.HAND);
				}
			}
		});
		setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (!mouseEvent.isPrimaryButtonDown()) {
					getScene().setCursor(Cursor.DEFAULT);
				}
			}
		});
	}

	public void setGroup(ArrayList<Anchor> groupList) {
		this.groupList = groupList;
	}

	private String getPackageNamePattern(){
		return getClass().getPackage().getName().replace(".", "/");
	}

}
