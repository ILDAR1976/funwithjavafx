package edu.fx_01;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.junit.Test;

import edu.fx_01.ObjectsManipulation.BlockIha;
import edu.fx_01.ObjectsManipulation.LineIha;
import edu.fx_01.model.Utils;
import edu.fx_01.view.ControllerBlock;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeType;
import javafx.stage.Stage;

public class ObjectsManipulation extends Application {

	private boolean controlDown = false;
    private final int TYPE_PANE = 1;
    private final int TYPE_CONTROLLER = 2;
    private final int TYPE_BLOCK = 3;
    private final int TYPE_LINE = 4;
	
	public static void main(String[] args) throws Exception {
		launch(args);
	}

	@Override
	public void start(final Stage stage) throws Exception {

		stage.setTitle("Line Manipulation Sample");

		Group root = new Group();

		root.getChildren().addAll(new BlockIha(50, 100), new LineIha(10, 10, 590, 10, Color.RED),
				new LineIha(10, 35, 590, 35, Color.BLUE), new LineIha(10, 60, 590, 60, Color.GREEN),
				new BlockIha(50, 400), new BlockIha(450, 100), new BlockIha(450, 400),
				new LineIha(70, 250, 300, 400, Color.RED), new LineIha(300, 400, 530, 250, Color.CHOCOLATE),
				new LineIha(150, 420, 450, 120, Color.BROWN), new LineIha(150, 120, 450, 420, Color.BLACK));
		/*
		 * root.getChildren().addAll( new LineIha(70,250,300,400,Color.RED), new
		 * LineIha(300,400,530,250,Color.CHOCOLATE), new
		 * LineIha(300,400,330,250,Color.GREEN) );
		 */

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

	private void makeLinks(Group root) {
		ArrayList<Anchor> anchorList = new ArrayList<Anchor>();

		for (Node item : root.getChildren()) {
			if (item instanceof LineIha) {
				anchorList.add(((LineIha) item).getStart());
				anchorList.add(((LineIha) item).getEnd());
			}

			if (item instanceof BlockIha) {
				for (Anchor anchor : ((BlockIha) item).getGroupList()) {
					anchorList.add(anchor);
				}
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
			}
		}

	}

	private void enableDrag(Scene scene, Group root) {
		scene.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				scene.setCursor(Cursor.DEFAULT);

				if (mouseEvent.isPrimaryButtonDown()) {

					PickResult pickResult = mouseEvent.getPickResult();
					Node point = pickResult.getIntersectedNode();
					
					if (point != null) {
						if (point.getClass().getSuperclass().getName() == "javafx.scene.shape.Circle"
								|| point.getClass().getSuperclass().getName() == "javafx.scene.shape.Line") {
							if (controlDown) {
								if (point instanceof Circle) {
									if (!((Anchor) point).selected) {
										Anchor anchor = (Anchor) point;

										Utils.s();
										Utils.p("<<<< Create the new line >>>>");
										Utils.s();

										LineIha line = new LineIha((int) anchor.centerXProperty().get(),
												(int) anchor.centerYProperty().get(), Color.GREEN);

										Utils.linkAnchor(anchor, line.start);
										

										root.getChildren().add(line);
									}
								}
							} else {
								if (point instanceof Circle) {
									System.out.println("--------------Lines list this point---------");

									int count = 0;

									for (Anchor item : ((Anchor) point).linkAnchor) {
										count++;
										System.out.println("| " + count 
																+ ") "
																+ item.getID().substring(33) 
												                + " - " 
												                + item.getParent().toString().substring(20) 
								                                + " NotMoved: " 
												                + item.isNotMoved());
									}

									System.out.println("------------------------------------------");
								}

								if (point instanceof BoundLine) {
									System.out.println(
											"---------------Linked list this line-----------------------------------");

									System.out.println("|   <This line:" + ((LineIha) point.getParent()) + ">");
									System.out.println(
											"|--------------Linked list this line-----------------------------------");

									int count = 0;

									for (Anchor item : ((LineIha) point.getParent()).start.linkAnchor) {
										if (item != ((LineIha) point.getParent()).start) {
											count++;
											System.out.println("| " + count + ") " + item.getParent());
										}
									}

									count = (count == 0) ? 0 : count--;

									for (Anchor item : ((LineIha) point.getParent()).end.linkAnchor) {
										if (item != ((LineIha) point.getParent()).end) {
											count++;
											System.out.println("| " + count + ") " + item.getParent());
										}
									}

									System.out.println(
											"-------------------------------------------------------------------");

								}
							}
						}
					}
				}

				if (mouseEvent.isSecondaryButtonDown()) {

					PickResult pickResult = mouseEvent.getPickResult();
					Node point = pickResult.getIntersectedNode();
					
					int type = 0;
					
					if (point != null) {
						if (point.getParent() instanceof Pane) type = TYPE_PANE;
						if (point.getParent() instanceof ControllerBlock) type = TYPE_CONTROLLER;
						if (point.getParent() instanceof BlockIha) type = TYPE_BLOCK;
						if (point.getParent() instanceof LineIha) type = TYPE_LINE;
					}
					
					if ((type >= TYPE_PANE) && (type <= TYPE_BLOCK)){
						
						BlockIha block = null;
						
						switch (type){
							case TYPE_PANE:
								block = (BlockIha) point.getParent().getParent().getParent();
								break;
							case TYPE_CONTROLLER:
								block = (BlockIha) point.getParent().getParent();
								break;
							case TYPE_BLOCK:
								block = (BlockIha) point.getParent();
								break;	
						}
						
						deleteBlock(block, root);
					}
					
					
					if (point instanceof Circle) {
						if (point.getParent() instanceof BlockIha) {
							deleteBlock(((BlockIha) point.getParent()), root);
						}

						if (point.getParent() instanceof LineIha) {
							((LineIha) point.getParent()).start.dispose();
							((LineIha) point.getParent()).end.dispose();
							root.getChildren().remove(point.getParent());
						}
					}

					if (point instanceof BoundLine) {
						((LineIha) point.getParent()).start.dispose();
						((LineIha) point.getParent()).end.dispose();
						root.getChildren().remove(point.getParent());
					}
				}

			}
		});
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent keyEvent) {
				scene.setCursor(Cursor.CROSSHAIR);
				controlDown = keyEvent.isControlDown();
			}
		});
		scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent keyEvent) {
				scene.setCursor(Cursor.DEFAULT);
				controlDown = false;
			}
		});

	}

	private void deleteBlock(BlockIha block, Group root){
		for (Anchor item : block.groupList) {
			item.getLinkAnchor().remove(item);
			for (Anchor item2 : item.getLinkAnchor()) {
				if (item2.getParent() instanceof LineIha) {
					((LineIha) item2.getParent()).getStart().getLinkAnchor().remove(item2);
					((LineIha) item2.getParent()).getStart().getLinkAnchor().remove(item);
					((LineIha) item2.getParent()).getStart().dispose();
					((LineIha) item2.getParent()).getEnd().getLinkAnchor().remove(item2);
					((LineIha) item2.getParent()).getEnd().getLinkAnchor().remove(item);
					((LineIha) item2.getParent()).getEnd().dispose();
					root.getChildren().remove((LineIha)item2.getParent());
				}
			}

			item.dispose();
			root.getChildren().remove(item.getParent());
		}		
	}
	
	public class AnchorList {
		public ArrayList<Anchor> list = new ArrayList<Anchor>();
	}

	public class BlockIha extends Group {
		DoubleProperty startX;
		DoubleProperty startY;
		ControllerBlock main;
		ArrayList<Bind> binder = new ArrayList<Bind>();
		ArrayList<Anchor> groupList = new ArrayList<Anchor>();
		Set<Object> link = new HashSet<Object>();

		public BlockIha(int startX, int startY) {

			this.startX = new SimpleDoubleProperty(startX);
			this.startY = new SimpleDoubleProperty(startY);

			this.main = new ControllerBlock(startX, startY);
			this.main.toBack();

			Bind bind = new Bind();

			int count = 22;
			int Height = 150;
			int Width = 100;

			for (int i = 0; i < count; i++) {

				bind.anchor.add(new Anchor(Color.GRAY, new SimpleDoubleProperty(startX - i - 1),
						new SimpleDoubleProperty(startY + i + 1), false));

				if (i < 7) {
					bind.property.add(new SimpleDoubleProperty(0));
					bind.property.add(new SimpleDoubleProperty((i + 1) * 20));
				} else if ((i > 6) && (i < 11)) {
					bind.property.add(new SimpleDoubleProperty((i - 6) * 20));
					bind.property.add(new SimpleDoubleProperty(Height));
				} else if ((i > 10) && (i < 18)) {
					bind.property.add(new SimpleDoubleProperty(Width));
					bind.property.add(new SimpleDoubleProperty((i - 10) * 20));
				} else if ((i > 17) && (i < 23)) {
					bind.property.add(new SimpleDoubleProperty((i - 17) * 20));
					bind.property.add(new SimpleDoubleProperty(0));
				}

				bind.summa.add(Bindings.add(bind.property.get(i * 2), this.main.layoutXProperty()));
				bind.summa.add(Bindings.add(bind.property.get((i * 2) + 1), this.main.layoutYProperty()));

				bind.anchor.get(i).centerXProperty().bind(bind.summa.get((i * 2)));
				bind.anchor.get(i).centerYProperty().bind(bind.summa.get((i * 2) + 1));

				bind.anchor.get(i).setNotMoved(true);
				
				groupList.add(bind.anchor.get(i));

			}

			this.main.setGroup(groupList);

			getChildren().add(main);

			for (int i = 0; i < count; i++) {
				getChildren().add(bind.anchor.get(i));
			}

		}

		public ArrayList<Anchor> getGroupList() {
			return groupList;
		}

		class Bind {
			public ArrayList<Anchor> anchor = null;
			public ArrayList<SimpleDoubleProperty> property = null;
			public ArrayList<NumberBinding> summa = null;

			public Bind() {
				this.anchor = new ArrayList<Anchor>();
				this.property = new ArrayList<SimpleDoubleProperty>();
				this.summa = new ArrayList<NumberBinding>();
			}
		}
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

			this.start = new Anchor(color, this.startX, this.startY, false);
			this.end = new Anchor(color, this.endX, this.endY, false);
			this.line = new BoundLine(color, this.startX, this.startY, this.endX, this.endY);

			getChildren().addAll(line, start, end);
		}

		public LineIha(int startX, int startY, Color color) {

			this.startX = new SimpleDoubleProperty(startX);
			this.startY = new SimpleDoubleProperty(startY);
			this.endX = new SimpleDoubleProperty(startX);
			this.endY = new SimpleDoubleProperty(startY);

			this.start = new Anchor(color, this.startX, this.startY, false);
			this.end = new Anchor(color, this.endX, this.endY, true);
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

	public class Anchor extends Circle {
		private Anchor self = this;
		private Set<Anchor> linkAnchor = new HashSet<Anchor>();
		private boolean selected = false;
		private String id;
		private boolean notMoved = false;
		

		public Anchor(Color color, DoubleProperty x, DoubleProperty y, boolean selected) {
			super(x.get(), y.get(), 1);
			id = UUID.randomUUID().toString();
			setFill(color.deriveColor(1, 1, 1, 0.5));
			setStroke(color);
			setStrokeWidth(2);
			setStrokeType(StrokeType.OUTSIDE);

			x.bind(centerXProperty());
			y.bind(centerYProperty());

			this.selected = selected;

			enableDrag();
			
			linkAnchor.add(self);
		}

		public Set<Anchor> getLinkAnchor() {
			return linkAnchor;
		}

		public void dispose() {
			this.centerXProperty().unbind();
			this.centerYProperty().unbind();
			this.getLinkAnchor().remove(this);

			Anchor main = null;

			for (Anchor item : this.linkAnchor) {
				if (item.getParent() instanceof BlockIha) {
					main = item;
					break;
				} else {
					main = item;
					main.centerXProperty().unbind();
					main.centerYProperty().unbind();
				}
			}

			if (main == null)
				return;

			main.getLinkAnchor().remove(this);
			main.getParent().toFront();
			this.getLinkAnchor().remove(main);

			for (Anchor item : this.getLinkAnchor()) {

				item.getLinkAnchor().remove(this);
				item.centerXProperty().unbind();
				item.centerYProperty().unbind();
				item.centerXProperty().bind(main.centerXProperty());
				item.centerYProperty().bind(main.centerYProperty());
				item.getParent().toBack();

			}

		}
		
		public boolean isNotMoved() {
			return notMoved;
		}

		public void setNotMoved(boolean notMoved) {
			this.notMoved = notMoved;
		}

		public boolean isSelected() {
			return selected;
		}

		public void setSelected(boolean selected) {
			this.selected = selected;
		}

		public String getID() {
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

					if (isSelected()) {
						if (point instanceof Circle) {
							if (((Anchor) point) != self) {
								setSelected(false);
							}
						}
					}

					if (point instanceof Circle && !selected)
						if (point.getClass().getSuperclass().getName() == "javafx.scene.shape.Circle") {
							
							if (point.getParent() instanceof BlockIha)
								if (self.getParent() instanceof BlockIha) 
									if (point != self) setNotMoved(true);
							
							if (self.getParent() instanceof BlockIha)
								if (point.getParent() instanceof BlockIha) 
									if (point != self) setNotMoved(true);
							
							if (!isNotMoved() && ((Anchor)point) != self  ) {
								Utils.linkAnchor(self, (Anchor) point);
							}
						}

					if (!self.centerXProperty().isBound())
						self.setCenterX(newX);
					if (!self.centerYProperty().isBound())
						self.setCenterY(newY);
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

	//For tests
	
	public LineIha testLineIha(int startX, int startY, int endX, int endY, Color color){
		return new LineIha(startX, startY, endX, endY, color);
	}
	
	public BlockIha testBlockIha(int startX, int startY){
		return new BlockIha(startX, startY);
	}
	
	public void testEnableDrag(Scene scene, Group root){
		enableDrag(scene, root);
	}

	public void testMakeLinks(Group root){
		makeLinks(root);
	}
}