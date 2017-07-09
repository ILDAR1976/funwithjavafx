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
import javafx.scene.input.PickResult;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;

public class ObjectsManipulation extends Application {
  public final int WindowX = 600;
  public final int WindowY = 600;
  public Pixel[][] pool = null;
  
  public static void main(String[] args) throws Exception { launch(args); }
  @Override public void start(final Stage stage) throws Exception {
	pool = new Pixel[WindowX + 300][WindowY + 300];
	
	for (int i = 0; i < WindowX + 300; i++) {
		for (int j = 0; j < WindowY + 300; j++) {
			pool[i][j] = new Pixel();
		}
	}  
	
	stage.setTitle("Line Manipulation Sample");
    
    Group root = new Group();

    /*
    root.getChildren().addAll(
    		new LineIha(10,35,300,335,Color.BLUE, pool),
    		new LineIha(10,10,590,310,Color.RED, pool)
      );
	*/
    
    root.getChildren().addAll(
    		new LineIha(10,10,590,10,Color.RED, pool),
    		new LineIha(10,35,590,35,Color.BLUE, pool),
    		new LineIha(10,60,590,60,Color.GREEN, pool),
       		new BlockIha(50,100, pool),
     		new BlockIha(50,400, pool),
     		new BlockIha(450,100, pool),
     		new BlockIha(450,400, pool),
     		new LineIha(70,250,300,400,Color.RED, pool),
     		new LineIha(300,400,530,250,Color.CHOCOLATE, pool),
     		new LineIha(150,420,450,120,Color.BROWN, pool),
     		new LineIha(150,120,450,420,Color.BLACK, pool)
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
	
	enableDrag(scene, root);
    
	stage.setScene(scene);
    
    stage.show();
  }
  
  class BlockIha extends Group{
	 Pixel[][] pool = null;
	 DoubleProperty startX;
	 DoubleProperty startY;
	 Block main;
	 ArrayList<Bind> binder = new ArrayList<Bind>();
	 ArrayList<Anchor> groupList = new ArrayList<Anchor>();
	 
	 public BlockIha(int startX, int startY, Pixel[][] pool){
		 this.pool = pool;
		 this.startX =  new SimpleDoubleProperty(startX);
		 this.startY =  new SimpleDoubleProperty(startY);
		 
		 this.main = new Block(startX, startY, pool);
		 this.main.toBack();
		 
		 Bind bind = new Bind();
		 
		 int count = 22;
		 int Height = 150;
		 int Width = 100;
		 
		 for (int i = 0; i < count; i++) {
			 
			 bind.anchor.add(new Anchor(Color.GRAY, new SimpleDoubleProperty(startX - i - 1), new SimpleDoubleProperty(startY + i + 1), pool, true));
			 
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
  
	 public void dispose(){
		 for (Anchor item : groupList) {
			item.dispose();
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
		  
			
			this.start  = new Anchor(color, this.startX, this.startY, pool, false);
			this.start.setParentAnchor(this);
			this.end    = new Anchor(color, this.endX, this.endY, pool, false);
			this.end.setParentAnchor(this);
			
			this.line   = new BoundLine(color, this.startX, this.startY, this.endX, this.endY);
	  
			getChildren().addAll(line, start, end);
	  }
	  
	  public void dispose(){
		 start.dispose();
		 end.dispose();
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
	private DoubleProperty oldX = new SimpleDoubleProperty(0);
    private DoubleProperty oldY = new SimpleDoubleProperty(0);
    private Anchor self = this;
    private boolean notDraggeble = false;
    private Object parent = null;
    private String id;
    
	Anchor(Color color, DoubleProperty x, DoubleProperty y, Pixel[][] pool, boolean notDraggeble) {
      super(x.get(), y.get(), 1);
      id = UUID.randomUUID().toString();
      setFill(color.deriveColor(1, 1, 1, 0.5));
      setStroke(color);
      setStrokeWidth(2);
      setStrokeType(StrokeType.OUTSIDE);
      
      this.notDraggeble = notDraggeble;
      
      x.bind(centerXProperty());
      y.bind(centerYProperty());
      
      oldX = new SimpleDoubleProperty(x.get());
      oldY = new SimpleDoubleProperty(y.get());
       
      Pixel bp = pool[(int)x.get()][(int)y.get()];
   	  
      if (!notDraggeble) {
    	  this.centerXProperty().unbind();
    	  this.centerYProperty().unbind();
      }
      
	  for (Anchor item : bp.link) {
 		if (!notDraggeble) {
    		if (!item.notDraggeble) {
     			item.centerXProperty().unbind();
	        	item.centerYProperty().unbind();
	        	item.centerXProperty().bind(this.centerXProperty());
				item.centerYProperty().bind(this.centerYProperty());
				item.toBack();
    		} else {
	        	this.centerXProperty().bind(item.centerXProperty());
	        	this.centerYProperty().bind(item.centerYProperty());
				this.notDraggeble = true;
				this.toBack();
				item.toFront();
				bp.link.add(this);
				enableDrag();
				return;
    		}
 		} else {
        	item.centerXProperty().bind(this.centerXProperty());
			item.centerYProperty().bind(this.centerYProperty());
 		}
	  }
	  
	  this.toFront();
      bp.link.add(this);
      enableDrag();
    }


	protected void dispose(){
		Pixel bp = pool[(int)self.centerXProperty().get()][(int)self.centerXProperty().get()];
		bp.link.remove(self);
   }
	
	private void setParentAnchor(Object parent){
		this.parent = parent;
	}
	
	private Object getParentAnchor(){
		return this.parent;
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
          if (getScene() != null) getScene().setCursor(Cursor.HAND);
        }
      });
      setOnMouseDragged(new EventHandler<MouseEvent>() {
		@Override public void handle(MouseEvent mouseEvent) {
          double newX = mouseEvent.getX() + dragDelta.x;
          double newY = mouseEvent.getY() + dragDelta.y;
          
          move(newX, newY, self);
          
          if (!self.centerXProperty().isBound()) {
        	  self.getParent().toFront();
        	  self.setCenterX(newX);
          } else {
        	  self.getParent().toBack();
          }
          
          if (!self.centerYProperty().isBound()) {
        	  self.getParent().toFront();
        	  self.setCenterY(newY);
          } else {
        	  self.getParent().toBack();
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
		    	  for ( Anchor item : bp.link) {
		    		 if (!item.notDraggeble) {
		    			item.centerXProperty().bind(self.centerXProperty());
						item.centerYProperty().bind(self.centerYProperty());
		    	  	} else {
			  	  		self.notDraggeble = true;
			  	  	    self.centerXProperty().bind(item.centerXProperty());
			  	  	    self.centerYProperty().bind(item.centerYProperty());
			  	  	    bp.link.add(self);
		    	  		return;
		    	  	}
		    	  }
        	  } 
 	  		  bp.link.add(self);
          } //Move block Stop    	
    }
    
    private class Delta { double x, y; }
  }  

  class Pixel {
	  public ArrayList<Anchor> link = new ArrayList<Anchor>();
  }

  private class Delta { double x, y; }
  
  private void enableDrag(Scene scene, Group root) {
	  final Delta dragDelta = new Delta();
	  scene.setOnMousePressed(new EventHandler<MouseEvent>() {
	    @Override public void handle(MouseEvent mouseEvent) {
	      if (mouseEvent.isSecondaryButtonDown()){
	    	  PickResult pr = mouseEvent.getPickResult();

	    	  Node rn = pr.getIntersectedNode();
	    	  if ( rn != null){
	    		  Node parent3 = rn.getParent().getParent().getParent();
	    		  Node parent1 = rn.getParent();
	    		  Node parent = null;
	    		  
	    		  if (parent3 != null) {
	    			  parent = parent3;
	    		  } else {
	    			  parent = parent1;
	    		  }

	    		  if (parent != null) {
	    			  if ((parent instanceof LineIha)) ((LineIha) parent).dispose(); 
	    			  if ((parent instanceof BlockIha)) ((BlockIha) parent).dispose();
	    			  root.getChildren().remove(parent);
    			  }
	    	  }
	      }
	    }
	  });
  }

}