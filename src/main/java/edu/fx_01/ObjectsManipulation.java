package edu.fx_01;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.sun.javafx.geom.PickRay;
import com.sun.javafx.scene.input.PickResultChooser;

import javafx.application.Application;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeType;
import javafx.stage.Stage;

public class ObjectsManipulation extends Application {

	public static void main(String[] args) throws Exception {
		launch(args);
	}

	@Override
	public void start(final Stage stage) throws Exception {

		stage.setTitle("Line Manipulation Sample");

		Group root = new Group();

		root.getChildren().addAll(
				new LineIha(10, 10, 590, 10, Color.RED), 
				new LineIha(10, 35, 590, 35, Color.BLUE),
				new LineIha(10, 60, 590, 60, Color.GREEN), 
				new LineIha(70, 250, 300, 400, Color.RED),
				new LineIha(300, 400, 530, 250, Color.CHOCOLATE), 
				new LineIha(150, 420, 450, 120, Color.BROWN),
				new LineIha(150, 120, 450, 420, Color.BLACK));

		Scene scene = new Scene(root, 600, 600, Color.ALICEBLUE);

		scene.widthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth,
					Number newSceneWidth) {
				System.out.println("Width: " + newSceneWidth);
			}

		});
		scene.heightProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight,
					Number newSceneHeight) {
				System.out.println("Height: " + newSceneHeight);
			}
		});
		scene.getStylesheets().add(getClass().getResource("/application.css").toExternalForm());

		enableDrag(scene, root);
		
		stage.setScene(scene);

		stage.show();

		makeLinks(root);
	}

	private void makeLinks(Group root){
		ArrayList<Anchor> anchorList = new ArrayList<Anchor>();

		for (Node item : root.getChildren()) {
			if (item instanceof LineIha) {
				anchorList.add(((LineIha) item).getStart());
				anchorList.add(((LineIha) item).getEnd());
			}
		}

		Map<String, AnchorList> points = new HashMap<String, AnchorList>();

		for (Anchor item : anchorList) {
			String key = (int) item.centerXProperty().get() + "*" + (int) item.centerYProperty().get();
			AnchorList value = new AnchorList();
			value.list.add(item);

			if (points.get(key) != null) {
				value = points.get(key);
				value.list.add(item);
				points.put(key, value);
			} else {
				points.put(key, value);
			}
		}

		for (Map.Entry<String, AnchorList> item : points.entrySet()) {
			if (item.getValue().list.size() > 1) {
				ArrayList<Anchor> jobAnchor = item.getValue().list;
				Anchor main = jobAnchor.get(0);
				jobAnchor.remove(0);

				for (Anchor anchor : jobAnchor) {
					Utils.linkAnchor(main, anchor);
				}
			} else {
				ArrayList<Anchor> jobAnchor = item.getValue().list;
				jobAnchor.get(0).linkAnchor.add(jobAnchor.get(0));
			}
		}
		
	}
	
	private void enableDrag(Scene scene, Group root){
		scene.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				scene.setCursor(Cursor.HAND);
				
				if (mouseEvent.isPrimaryButtonDown()) {
					
					PickResult pickResult = mouseEvent.getPickResult();
					Node point = pickResult.getIntersectedNode();

					System.out.println("--------------Lines list this point---------");

					if (point instanceof Circle) {
						for (Anchor item : ((Anchor) point).linkAnchor) {
							System.out.println(item.getParent());
						}
					}

					System.out.println("-------------------------------");
				}
				
				if (mouseEvent.isSecondaryButtonDown()) {
					
					PickResult pickResult = mouseEvent.getPickResult();
					Node point = pickResult.getIntersectedNode();
					
					if (point instanceof Circle) {
						((Anchor) point).dispose();
						root.getChildren().remove(point.getParent());
					}

					if (point instanceof LineIha) {
						
					}
				}
				
			}
		});
		
	}
	
	class AnchorList {
		public ArrayList<Anchor> list = new ArrayList<Anchor>();
	}

	class LineIha extends Group {

		private Line line;
		private Anchor start;
		private Anchor end;

		DoubleProperty startX;
		DoubleProperty startY;
		DoubleProperty endX;
		DoubleProperty endY;

		public LineIha(int startX, int startY, int endX, int endY, Color color) {

			this.startX = new SimpleDoubleProperty(startX);
			this.startY = new SimpleDoubleProperty(startY);
			this.endX = new SimpleDoubleProperty(endX);
			this.endY = new SimpleDoubleProperty(endY);

			this.start = new Anchor(color, this.startX, this.startY);
			this.end = new Anchor(color, this.endX, this.endY);
			this.line = new BoundLine(color, this.startX, this.startY, this.endX, this.endY);

			getChildren().addAll(line, start, end);
		}

		
		public Anchor getStart() {
			return start;
		}

		
		public Anchor getEnd() {
			return end;
		}

	}

	class BoundLine extends Line {
		BoundLine(Color color, DoubleProperty startX, DoubleProperty startY, DoubleProperty endX, DoubleProperty endY) {
			startXProperty().bind(startX);
			startYProperty().bind(startY);
			endXProperty().bind(endX);
			endYProperty().bind(endY);

			setStroke(color.deriveColor(0, 1, 1, 0.5));

		}
	}

	class Anchor extends Circle {
		private Anchor self = this;
		Set<Anchor> linkAnchor = new HashSet<Anchor>();
		private String id;

		public Anchor(Color color, DoubleProperty x, DoubleProperty y) {
			super(x.get(), y.get(), 1);
			id = UUID.randomUUID().toString();
			setFill(color.deriveColor(1, 1, 1, 0.5));
			setStroke(color);
			setStrokeWidth(2);
			setStrokeType(StrokeType.OUTSIDE);

			x.bind(centerXProperty());
			y.bind(centerYProperty());

			enableDrag();
		}

		public void dispose(){
			this.centerXProperty().unbind();
			this.centerYProperty().unbind();
			this.linkAnchor.remove(this);
			
			Anchor main = null;
				
			int count = 0;
			
			for (Anchor item: this.linkAnchor) {
				
				if ( count == 0 ) {
					main = item;
					main.linkAnchor.remove(this);
					main.centerXProperty().unbind();
					main.centerYProperty().unbind();
					main.getParent().toFront();
				} else {
					item.linkAnchor.remove(this);
					item.centerXProperty().unbind();
					item.centerYProperty().unbind();
					item.centerXProperty().bind(main.centerXProperty());
					item.centerYProperty().bind(main.centerYProperty());
					item.getParent().toBack();
				}
				
				count++;
				
			}

		}

		public String getID(){
			return id;
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
					double newX = mouseEvent.getSceneX();
					double newY = mouseEvent.getSceneY();

					PickResult pickResult = mouseEvent.getPickResult();
					Node point = pickResult.getIntersectedNode();

					if (point instanceof Circle) 
						Utils.linkAnchor(self, point);

					if (!self.centerXProperty().isBound()) self.setCenterX(newX);
					if (!self.centerYProperty().isBound()) self.setCenterY(newY);
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
	}
}