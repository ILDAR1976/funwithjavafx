package edu.fx_01;

import java.util.ArrayList;
import java.util.UUID;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.DoubleStringConverter;

public class ObjectsManipulation extends Application {
  public final int WindowX = 600;
  public final int WindowY = 600;
  public Pixel[][] pool = null;
  
  public static void main(String[] args) throws Exception { launch(args); }
  @Override public void start(final Stage stage) throws Exception {
	pool = new Pixel[WindowX][WindowY];
	
	for (int i = 0; i < WindowX; i++) {
		for (int j = 0; j < WindowY; j++) {
			pool[i][j] = new Pixel();
		}
	}  
	
	stage.setTitle("Line Manipulation Sample");
    
    Group root = new Group();
    
    root.getChildren().addAll(
    		new LineIha(10,10,200,10,Color.RED),
    		new LineIha(30,230,200,10,Color.GREEN),
    		new LineIha(330,230,400,210,Color.RED),
    		new LineIha(10,230,100,210,Color.RED),
    		new LineIha(15,530,300,210,Color.RED),
    		new LineIha(10,230,136,410,Color.RED),
    		new LineIha(60,30,400,410,Color.RED),
    		new BlockIha(140,40),
    		new BlockIha(340,140),
    		new BlockIha(240,440)
      );
    
    Scene scene = new Scene(root, WindowX, WindowY, Color.ALICEBLUE);
    
    scene.widthProperty().addListener(new ChangeListener<Number>() {
        @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
            System.out.println("Width: " + newSceneWidth);
        }

	   });
    scene.heightProperty().addListener(new ChangeListener<Number>() {
        @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
            System.out.println("Height: " + newSceneHeight);
        }
    });
	scene.getStylesheets().add(getClass().getResource("/application.css").toExternalForm());

    stage.setScene(scene);
    
    stage.show();
  }

  class BlockIha extends Group{
	 Block main;
	 Anchor anc_01;
	 Anchor anc_02;
	 Anchor anc_03;
	 
	 SimpleDoubleProperty delta0;
	 SimpleDoubleProperty delta1;
	 SimpleDoubleProperty delta2;
	 
	 SimpleDoubleProperty delta1A;
	 SimpleDoubleProperty delta1B;
	 SimpleDoubleProperty delta1C;
	 
	 DoubleProperty startX;
	 DoubleProperty startY;
	 
	 NumberBinding sum1X;
	 NumberBinding sum1Y;
	 
	 NumberBinding sum2X;
	 NumberBinding sum2Y;

	 NumberBinding sum3X;
	 NumberBinding sum3Y;
	 
	 public BlockIha(int startX, int startY){
		 this.startX =  new SimpleDoubleProperty(startX);
		 this.startY =  new SimpleDoubleProperty(startY);
		 
		 delta0 = new SimpleDoubleProperty(0);
		 delta1 = new SimpleDoubleProperty(0);
		 delta2 = new SimpleDoubleProperty(0);
		 
		 delta1A = new SimpleDoubleProperty(-20);
		 delta1B = new SimpleDoubleProperty(20);		 
		 delta1C = new SimpleDoubleProperty(40);
		 
		 ArrayList<Anchor> groupList = new ArrayList<Anchor>();
		 
		 this.main = new Block(startX, startY);
		 this.anc_01 = new Anchor(Color.GRAY, new SimpleDoubleProperty(startX),new SimpleDoubleProperty(startY + 1), false, true);
		 this.anc_02 = new Anchor(Color.GRAY, new SimpleDoubleProperty(startX),new SimpleDoubleProperty(startY + 2), true);
		 this.anc_03 = new Anchor(Color.GRAY, new SimpleDoubleProperty(startX),new SimpleDoubleProperty(startY + 3), true);
		 
		 this.main.toBack();
		 
		 sum1X = Bindings.add(this.delta0, this.anc_01.centerXProperty());
		 sum1Y = Bindings.add(this.delta1A, this.anc_01.centerYProperty());
		 
		 this.main.layoutXProperty().bind(sum1X);
		 this.main.layoutYProperty().bind(sum1Y);
		 
		 sum2X = Bindings.add(this.delta1, this.anc_01.centerXProperty());
		 sum2Y = Bindings.add(this.delta1B, this.anc_01.centerYProperty());

		 this.anc_02.centerXProperty().bind(sum2X);
		 this.anc_02.centerYProperty().bind(sum2Y);

		 sum3X = Bindings.add(this.delta2, this.anc_01.centerXProperty());
		 sum3Y = Bindings.add(this.delta1C, this.anc_01.centerYProperty());

		 this.anc_03.centerXProperty().bind(sum3X);
		 this.anc_03.centerYProperty().bind(sum3Y);

		 groupList.add(this.anc_02);
		 groupList.add(this.anc_03);
		 
		 this.anc_01.setGroup(groupList);
		 
		 getChildren().addAll(main, anc_01, anc_02, anc_03);	 
	 }
	 
  }
  
  class LineIha extends Group{
	  
	  private Line line;
	  private Anchor start;
	  private Anchor end;
	  
	  DoubleProperty startX;
	  DoubleProperty startY;
	  DoubleProperty endX;
	  DoubleProperty endY;
	
	  
	  public LineIha(int startX, int startY, int endX, int endY, Color color){
			this.startX =  new SimpleDoubleProperty(startX);
			this.startY =  new SimpleDoubleProperty(startY);
			this.endX   =  new SimpleDoubleProperty(endX);
			this.endY   =  new SimpleDoubleProperty(endY);
		  
			this.start  = new Anchor(color, this.startX, this.startY);
			this.end    = new Anchor(color, this.endX, this.endY);
			
			this.line   = new BoundLine(color, this.startX, this.startY, this.endX, this.endY);
	  
			getChildren().addAll(line, start, end);
	  }
	  
	  public int setStartX() {
		  return (int) startX.get();
	  }

	  public void setStartX(int startX) {
		  this.startX = new SimpleDoubleProperty(startX);
	  }
	  
	  public int setStartY() {
		  return (int) startY.get();
	  }

	  public void setStartY(int startY) {
		  this.startY = new SimpleDoubleProperty(startY);
	  }
	  
	  public int setEndX() {
		  return (int) endX.get();
	  }

	  public void setEndX(int endX) {
		  this.startX = new SimpleDoubleProperty(endX);
	  }
	  
	  public int setEndY() {
		  return (int) endY.get();
	  }

	  public void setEndY(int EndY) {
		  this.endY = new SimpleDoubleProperty(EndY);
	  }
  }
  
  class BoundLine extends Line {
    BoundLine(Color color, DoubleProperty startX, DoubleProperty startY, DoubleProperty endX, DoubleProperty endY) {
      startXProperty().bind(startX);
      startYProperty().bind(startY);
      endXProperty().bind(endX);
      endYProperty().bind(endY);
      
      setStroke(color.deriveColor(0, 1, 1, 0.5));
      setMouseTransparent(true);
    }
  }

  class Anchor extends Circle { 
	private DoubleProperty oldX = new SimpleDoubleProperty(0);
    private DoubleProperty oldY = new SimpleDoubleProperty(0);
    private int oldIndex = 0;
    private ArrayList<Anchor> oldLink = null;
    private Anchor self = this;
    private boolean notDraggeble = false;
    private ArrayList<Anchor> groupList = null;
    private boolean group = false; 
    
    private String id;
    
	Anchor(Color color, DoubleProperty x, DoubleProperty y) {
      super(x.get(), y.get(), 1);
      id = UUID.randomUUID().toString();
      setFill(color.deriveColor(1, 1, 1, 0.5));
      setStroke(color);
      setStrokeWidth(2);
      setStrokeType(StrokeType.OUTSIDE);

      x.bind(centerXProperty());
      y.bind(centerYProperty());
      
      oldX = new SimpleDoubleProperty(x.get());
      oldY = new SimpleDoubleProperty(y.get());
       
      Pixel bp = pool[(int)x.get()][(int)y.get()];
   	  oldLink = new ArrayList<Anchor>();
   	  
	  this.centerXProperty().unbind();
	  this.centerYProperty().unbind();

      if (bp.link.size() != 0) {
    	  
    	  for (Anchor item : bp.link) {
     		item.centerXProperty().unbind();
        	item.centerYProperty().unbind();
      		item.centerXProperty().bind(self.centerXProperty());
			item.centerYProperty().bind(self.centerYProperty());
			item.toBack();
     	  }
    	  
    	  this.toFront();
      }
      
      bp.link.add(this);
      oldIndex = bp.link.size() - 1;
     
      enableDrag();
    }

	Anchor(Color color, DoubleProperty x, DoubleProperty y, boolean notDraggeble) {
		
		this(color, x, y);
		
		this.notDraggeble = notDraggeble;
	}

	Anchor(Color color, DoubleProperty x, DoubleProperty y, boolean notDraggeble, boolean group) {
		
		this(color, x, y);
		
		this.notDraggeble = notDraggeble;
		this.group = group;
		
	}	
    
	private void enableDrag() {
      final Delta dragDelta = new Delta();
      setOnMousePressed(new EventHandler<MouseEvent>() {
        @Override public void handle(MouseEvent mouseEvent) {
          // record a delta distance for the drag and drop operation.
          dragDelta.x = getCenterX() - mouseEvent.getX();
          dragDelta.y = getCenterY() - mouseEvent.getY();
          getScene().setCursor(Cursor.MOVE);
        }
      });
      setOnMouseReleased(new EventHandler<MouseEvent>() {
        @Override public void handle(MouseEvent mouseEvent) {
          getScene().setCursor(Cursor.HAND);
        }
      });
      setOnMouseDragged(new EventHandler<MouseEvent>() {
		@Override public void handle(MouseEvent mouseEvent) {
          double newX = mouseEvent.getX() + dragDelta.x;
          double newY = mouseEvent.getY() + dragDelta.y;
          
          if (!notDraggeble) {
        	  
        	  move(newX, newY, self);
		
		  	  setCenterX(newX);
		  	  setCenterY(newY);
          }
          
          if (group) {
        	  moveGroup(groupList);
          }
		}
      });
      setOnMouseEntered(new EventHandler<MouseEvent>() {
        @Override public void handle(MouseEvent mouseEvent) {
          if (!mouseEvent.isPrimaryButtonDown()) {
            getScene().setCursor(Cursor.HAND);
          }
        }
      });
      setOnMouseExited(new EventHandler<MouseEvent>() {
        @Override public void handle(MouseEvent mouseEvent) {
          if (!mouseEvent.isPrimaryButtonDown()) {
            getScene().setCursor(Cursor.DEFAULT);
          }
        }
      });
    }
    
	private void moveGroup(ArrayList<Anchor> group) {
		for (Anchor item : group) {
			move(item.getCenterX(),item.getCenterY(), item);
		}
	}
	
	public void setGroup(ArrayList<Anchor> groupList){
		this.groupList = groupList;
	}
	
    private void move(double newX, double newY, Anchor self){
    	if ((newX < 0) || (newX > WindowX - 1)) return; 
        if ((newY < 0) || (newY > WindowY - 1)) return;
        
      	  { //Eraser block Start
      		  
	        	  Pixel bp = pool[(int)self.oldX.get()][(int)self.oldY.get()];
	 
	        	  if (bp.link.size() != 0) {
	        		  bp.link.remove(self.oldIndex);
	        	  }
	        	  
	          } //Eraser block Stop
	          
	          { //Move block Start
	        	  Pixel bp = pool[(int)newX][(int)newY];
	        	  self.oldX = new SimpleDoubleProperty(newX);
	        	  self.oldY = new SimpleDoubleProperty(newY);
		        
	        	  if (!group) {
		        	  self.centerXProperty().unbind();
		        	  self.centerYProperty().unbind();
	        	  }
	        	  
	        	  for ( Anchor item : bp.link) {
	        		
	        		if (!item.notDraggeble) {  
		        		item.centerXProperty().unbind();
		        		item.centerYProperty().unbind();
	        		}
	        		
					item.centerXProperty().bind(self.centerXProperty());
					item.centerYProperty().bind(self.centerYProperty());
					item.toBack();
	        	  }
	        	  
      		  bp.link.add(self);
  			  self.oldIndex = bp.link.size() - 1;
  			  
  			  self.toFront();
  			  
  		  
	          } //Move block Stop    	
    }
    
    private class Delta { double x, y; }
  }  

  class Pixel {
	  public ArrayList<Anchor> link = new ArrayList<Anchor>();
  }
}
