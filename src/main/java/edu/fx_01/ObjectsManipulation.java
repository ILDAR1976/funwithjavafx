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
    		new LineIha(10,10,200,10,Color.RED, pool),
    		new LineIha(40,100,200,10,Color.RED, pool),
    		new LineIha(12,110,200,23,Color.RED, pool),
    		new LineIha(45,300,345,76,Color.RED, pool),
    		new LineIha(98,400,230,23,Color.RED, pool),
    		new LineIha(12,200,220,56,Color.RED, pool),
    		new LineIha(14,120,100,20,Color.RED, pool),
    		new LineIha(50,135,233,30,Color.RED, pool),
    		new LineIha(99,112,555,40,Color.RED, pool),
    		new LineIha(36,150,320,50,Color.RED, pool),
     		new BlockIha(240,440, pool),
     		new BlockIha(40,140, pool),
     		new BlockIha(340,140, pool),
     		new BlockIha(10,340, pool)
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
	 Pixel[][] pool = null;
	 DoubleProperty startX;
	 DoubleProperty startY;
	 Block main;
	 ArrayList<Bind> binder = new ArrayList<Bind>();
	 
	 
	 public BlockIha(int startX, int startY, Pixel[][] pool){
		 this.pool = pool;
		 this.startX =  new SimpleDoubleProperty(startX);
		 this.startY =  new SimpleDoubleProperty(startY);
		 
		 
		 this.main = new Block(startX, startY, pool);
		 this.main.toBack();
		
		 ArrayList<Anchor> groupList = new ArrayList<Anchor>();
		 
		 Bind bind = new Bind();
		 
		 int count = 5;
		 
		 for (int i = 0; i < count; i++) {
			 
			 bind.anchor.add(new Anchor(Color.GRAY, new SimpleDoubleProperty(startX),new SimpleDoubleProperty(startY + i), true));
			 
			 bind.property.add(new SimpleDoubleProperty(0));
			 bind.property.add(new SimpleDoubleProperty((i + 1) * 20));
			 
			 bind.summa.add(Bindings.add(bind.property.get(i * 2), this.main.layoutXProperty()));
			 bind.summa.add(Bindings.add(bind.property.get((i * 2) + 1), this.main.layoutYProperty()));
			 
			 bind.anchor.get(i).centerXProperty().bind(bind.summa.get((i * 2)));
			 bind.anchor.get(i).centerYProperty().bind(bind.summa.get((i * 2) + 1));
		 
			 groupList.add(bind.anchor.get(i)); 
		 
			 bind.anchor.get(i).move((int) bind.anchor.get(i).centerXProperty().get(), 
					                 (int) bind.anchor.get(i).centerYProperty().get(), 
					                 bind.anchor.get(i));
		 }
		 
		 this.main.setGroup(groupList);
		 
		 getChildren().add(main);
				 
		 for (int i = 0; i < count; i++) {
			 getChildren().add(bind.anchor.get(i));
		 }
		 	 
	 }
	
	 class Bind {
		 public ArrayList<Anchor> anchor = null;
		 public ArrayList<SimpleDoubleProperty> property = null;
		 public ArrayList<NumberBinding> summa = null;
		 public Bind(){
			 this.anchor = new ArrayList<Anchor>();
			 this.property = new ArrayList<SimpleDoubleProperty>();
			 this.summa = new ArrayList<NumberBinding>();
		 }
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
	
	  
	  public LineIha(int startX, int startY, int endX, int endY, Color color, Pixel[][] pool){
			this.startX =  new SimpleDoubleProperty(startX);
			this.startY =  new SimpleDoubleProperty(startY);
			this.endX   =  new SimpleDoubleProperty(endX);
			this.endY   =  new SimpleDoubleProperty(endY);
		  
			this.start  = new Anchor(color, this.startX, this.startY, pool);
			this.end    = new Anchor(color, this.endX, this.endY, pool);
			
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
    private Anchor self = this;
    private boolean notDraggeble = false;
    private String id;
    
	Anchor(Color color, DoubleProperty x, DoubleProperty y,Pixel[][] pool) {
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
     
      enableDrag();
    }

	Anchor(Color color, DoubleProperty x, DoubleProperty y, boolean notDraggeble) {
		
		this(color, x, y, pool);
		
		this.notDraggeble = notDraggeble;
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
          
          if (!self.centerXProperty().isBound()) 
	  	    setCenterX(newX);
          if (!self.centerYProperty().isBound())
        	setCenterY(newY);
	  	  
	  	  move(newX, newY, self);
          
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
 
    public void move(double newX, double newY, Anchor self){
    	if ((newX < 0) || (newX > WindowX - 1)) return; 
        if ((newY < 0) || (newY > WindowY - 1)) return;
        
      	  { //Eraser block Start
      		  
        	  Pixel bp = pool[(int)self.oldX.get()][(int)self.oldY.get()];
 
        	  if (bp.link.size() != 0) {
        		  bp.link.clear();
        	  }
	        	  
          } //Eraser block Stop
          
          { //Move block Start
        	  Pixel bp = pool[(int)newX][(int)newY];
        	  self.oldX = new SimpleDoubleProperty(newX);
        	  self.oldY = new SimpleDoubleProperty(newY);
	        
        	  if (!notDraggeble) {
		    	  self.centerXProperty().unbind();
		    	  self.centerYProperty().unbind();
		    	  
		    	  for ( Anchor item : bp.link) {
		    		if (!item.notDraggeble) {
			    		item.centerXProperty().unbind();
			    		item.centerYProperty().unbind();
						item.centerXProperty().bind(self.centerXProperty());
						item.centerYProperty().bind(self.centerYProperty());
						item.toBack();
		    	  	} else {
			  	  		self.notDraggeble = true;
			  	  		
			  	  		self.centerXProperty().bind(item.centerXProperty());
			  	  	    self.centerYProperty().bind(item.centerYProperty());
		    	  		
			  	  	    bp.link.add(self);
						
						self.toBack();  
						item.toFront();
		    	  		return;
		    	  	}
		    	  }
        	  } else {
		    	  for ( Anchor item : bp.link) {
		    		item.centerXProperty().unbind();
		    		item.centerYProperty().unbind();
		    		item.centerXProperty().bind(self.centerXProperty());
					item.centerYProperty().bind(self.centerYProperty());
					item.toBack();
		    	  }
        	  }
        	  
	  		  bp.link.add(self);
			  
			  self.toFront();
		  
	  
          } //Move block Stop    	
    }
    
    private class Delta { double x, y; }
  }  

  class Pixel {
	  public ArrayList<Anchor> link = new ArrayList<Anchor>();
  }

 
}
