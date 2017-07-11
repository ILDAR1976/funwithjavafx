package edu.fx_01;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

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
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeType;
import javafx.stage.Stage;

public class ObjectsManipulation extends Application {
  public final int WindowX = 600;
  public final int WindowY = 600;
  public Pixel[][] pool = null;
  private boolean controlDown = false;
  
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
     		new BlockIha(50,400, pool),
     		new BlockIha(450,100, pool),
       		new LineIha(10,10,590,10,Color.RED, pool),
    		new LineIha(10,35,590,35,Color.BLUE, pool),
    		new LineIha(10,60,590,60,Color.GREEN, pool),
    		new LineIha(10,10,590,310,Color.RED, pool)
      );
	*/
    
    root.getChildren().addAll(
    		new BlockIha(50,100, pool),
    		new LineIha(10,10,590,10,Color.RED, pool),
    		new LineIha(10,35,590,35,Color.BLUE, pool),
    		new LineIha(10,60,590,60,Color.GREEN, pool),
       		
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
	 Set<Object> link = new HashSet<Object>();
	 
	 public BlockIha(int startX, int startY, Pixel[][] pool){
		 this.pool = pool;
		 this.startX =  new SimpleDoubleProperty(startX);
		 this.startY =  new SimpleDoubleProperty(startY);
		 
		 this.main = new Block(startX, startY, pool);
		 this.main.toBack();
		 
		 Bind bind = new Bind();
		 
		 int count  = 22;
		 int Height = 150;
		 int Width  = 100;
		 
		 for (int i = 0; i < count; i++) {
			 
			 bind.anchor.add(new Anchor(Color.GRAY, 
					                    new SimpleDoubleProperty(startX - i - 1), 
					                    new SimpleDoubleProperty(startY + i + 1), 
					                    pool, 
					                    true,
					                    this));
			 
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
			 
			 bind.anchor.get(i).move(bind.anchor.get(i), pool);
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
			item.dispose(pool);
		}
	 }
	 
	 public Set<Object> getLink() {
		  return link;
     }
		
	 public void setLink(Object link) {
		  this.link.add(link);
	 }
	  
	 public void removeLink(Object o) {
		  this.link.remove(o);
	 }
  }
  
  class LineIha extends Group{
	  
	  private Line line;

	  private Anchor start;
	  private Anchor end;
	  Set<Object> link = new HashSet<Object>();
	  
	  DoubleProperty startX;
	  DoubleProperty startY;
	  DoubleProperty endX;
	  DoubleProperty endY;
	  
	  public LineIha(int startX, int startY, int endX, int endY, Color color, Pixel[][] pool){
			
		    this.startX =  new SimpleDoubleProperty(startX);
			this.startY =  new SimpleDoubleProperty(startY);
			this.endX   =  new SimpleDoubleProperty(endX);
			this.endY   =  new SimpleDoubleProperty(endY);
		  
			
			this.start  = new Anchor(color, this.startX, this.startY, pool, false, this);
			this.end    = new Anchor(color, this.endX, this.endY, pool, false, this);

			this.start.setParentAnchor(this);
			this.end.setParentAnchor(this);
			
			this.line   = new BoundLine(color, this.startX, this.startY, this.endX, this.endY);
	  
			getChildren().addAll(line, start, end);
	  }
	  
	  public LineIha(int startX, int startY, Color color, Pixel[][] pool){
		  this(startX, startY, startX, startY, color, pool);
		  this.end.notDraggeble = false;
		  this.end.setSelected(startX, startY);
		  this.start.toBack();
		  this.end.toFront();
		  this.end.centerXProperty().unbind();
		  this.end.centerYProperty().unbind();
	  }
	  
	  public void dispose(){
		 start.dispose(pool);
		 end.dispose(pool);
	  }
  
	  public Set<Object> getLink() {
		  return link;
      }
		
	  public void setLink(Object link) {
		  this.link.add(link);
	  }
	  
	  public void removeLink(Object o) {
		  this.link.remove(o);
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
	public DoubleProperty oldX = new SimpleDoubleProperty(0);
	public DoubleProperty oldY = new SimpleDoubleProperty(0);
    private Anchor self = this;
    private boolean notDraggeble = false;
    private boolean selected = false;
    private int selectX = 0;
    private int selectY = 0;
    private Object parent = null;
	private String id;
    private boolean controlDown = false;
    
	public Anchor(Color color, DoubleProperty x, DoubleProperty y, Pixel[][] pool, boolean notDraggeble, Node parent) {
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
	  
      //here
      int count = 0;
      
      if (bp.link.size() > 0 ) System.out.println("    L I N K S   C R E A T E   ");
      
      for (Anchor item : bp.link) {
  	        
    	  
    	    count++;

    	    System.out.println("OOPS!!! " + count);
    	    
    	    Node ip = item.getParent(); 
			
    	    if (ip instanceof LineIha) {
	   			
				if (parent instanceof LineIha) {
					((LineIha) ip).setLink(parent);
	  			    ((LineIha) parent).setLink(ip);
	  			    
	  			    System.out.println(" - LINK A");
	   			}
	   			
	   			if (parent instanceof BlockIha) {
					((LineIha) ip).setLink(parent);
	  			    ((BlockIha) parent).setLink(ip);
	  			    
	  			    System.out.println(" - LINK B");
	   			}
			}

			if (ip instanceof BlockIha) {
				if (parent instanceof LineIha) {
					((BlockIha) ip).setLink(parent);
	  			    ((LineIha) parent).setLink(ip);
	  			    
	  			    System.out.println(" - LINK C"); 
	   			}
				
				if (parent instanceof BlockIha) {
					((LineIha) ip).setLink(parent);
	  			    ((BlockIha) parent).setLink(ip);
	  			    
	  			    System.out.println(" - LINK D");
	   			}
			}
    	  
    	  
    	if (!notDraggeble) {
    		if (!item.notDraggeble) {
    			item.centerXProperty().unbind();
	        	item.centerYProperty().unbind();
	        	item.centerXProperty().bind(this.centerXProperty());
				item.centerYProperty().bind(this.centerYProperty());
				
				System.out.println("A");
    		} else {
    			if (count == 1) {
    			this.centerXProperty().bind(item.centerXProperty());
	        	this.centerYProperty().bind(item.centerYProperty());
				this.notDraggeble = true;
    			}
				//bp.link.add(this);
				//enableDrag();
				//return;
    			
    			System.out.println("B");
    		}
 		} else {
 			
			item.centerXProperty().bind(this.centerXProperty());
			item.centerYProperty().bind(this.centerYProperty());
			
			System.out.println("C");
 		}
	  
	  
	  }
      
      if (parent != null) {
    	  
    	  printLink(parent);
      }
      
      bp.link.add(this);
      
      enableDrag();
    }

	protected void dispose(Pixel[][] pool){
		Pixel bp = pool[(int) self.centerXProperty().get()][(int) self.centerYProperty().get()];
		System.out.println("-----------------------------------------------------------------------------------------");

		System.out.println("| dispose x= " + self.centerXProperty().get() + " y= " + self.centerYProperty().get());
		
		System.out.println("|  old bp.size= " + bp.link.size());
		
		bp.link.remove(self);

		System.out.println("|  new bp.size= " + bp.link.size());
		System.out.println("-----------------------------------------------------------------------------------------");
   }
	
	public boolean getDraggeble(){
		return notDraggeble;
	}
	
	private void setParentAnchor(Object parent){
		this.parent = parent;
	}
	
	private void setSelected(int x, int y){
		this.selected = true;
		this.selectX = x;
		this.selectY = y;
	}
	
	private int getSelectX() {
		return this.selectX;
	}

	private int getSelectY() {
		return this.selectY;
	}
	
	private void removeSelected(){
		this.selected = false;
		this.selectX = 0;
		this.selectY = 0;
	}
	
	private void enableDrag() {

      setOnMousePressed(new EventHandler<MouseEvent>() {
        @Override public void handle(MouseEvent mouseEvent) {
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
          double newX = mouseEvent.getSceneX();
          double newY = mouseEvent.getSceneY();

	      PickResult pr = mouseEvent.getPickResult();
	      Node rn = pr.getIntersectedNode();
		  
	      if (rn instanceof Circle) {
			  Circle circle = (Circle) rn;
		      newX = circle.getCenterX();
	          newY = circle.getCenterY();
	      }

	      int welldone = 0;
          
          if (!self.centerXProperty().isBound()) {
        	  self.getParent().toFront();
        	  self.setCenterX(newX);
        	  welldone++;
          } else {
        	  if (!self.selected) self.getParent().toBack();
          }
          
          if (!self.centerYProperty().isBound()) {
        	  self.getParent().toFront();
        	  self.setCenterY(newY);
        	  welldone++;
          } else {
        	  if (!self.selected) self.getParent().toBack();
          }
          
          if (welldone == 2) move( self, pool);
	      
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
 	
 	@SuppressWarnings("unchecked")
	public void move( Anchor self, Pixel[][] pool){
    	int newX = (int) self.centerXProperty().get();
    	int newY = (int) self.centerYProperty().get();
 		
 		
 		if ((newX < 0) || (newX > WindowX - 1)) return; 
        if ((newY < 0) || (newY > WindowY - 1)) return;
        
        ArrayList<Anchor> link = new ArrayList<Anchor>();
      	  
        { //Eraser block Start
      		  
        	  Pixel bps = pool[(int)self.oldX.get()][(int)self.oldY.get()];
        	  
        	  if (bps.link.size() != 0) {
        		  
        		  if (!self.selected) { 
        			link = (ArrayList<Anchor>) bps.link.clone();
        		  	link.remove(self);
        		  	bps.link.clear();
        		  } else {
        			link.clear();  
        			bps.link.remove(self);  
        		  }
        		  
        		 
        	  }
	        	  
        } //Eraser block Stop
          
          { //Move block Start
        	  
        	  Pixel bp = pool[newX][newY];
        	  self.oldX = new SimpleDoubleProperty(newX);
        	  self.oldY = new SimpleDoubleProperty(newY);
	          
	          //System.out.println("!" + self.getAnchorId().substring(34));
        	  
	          if (!notDraggeble) {
        		  int count = 0;
		    	  for ( Anchor item : bp.link) {
		    		  
		    		  count++;
		    		  
		    		  if  (!self.selected)	{
		    			  
			      	    Node ip = item.getParent(); 
						
			    	    if (ip instanceof LineIha) {
				   			
							if (self.getParent() instanceof LineIha) {
								((LineIha) ip).setLink(self.getParent());
				  			    ((LineIha) self.getParent()).setLink(ip);
				   			}
				   			
				   			if (self.getParent() instanceof BlockIha) {
								((LineIha) ip).setLink(self.getParent());
				  			    ((BlockIha) self.getParent()).setLink(ip);
				   			}
						}
	
						if (ip instanceof BlockIha) {
							
							if (self.getParent() instanceof LineIha) {
								((BlockIha) ip).setLink(self.getParent());
				  			    ((LineIha) parent).setLink(ip);
				   			}
							
							if (self.getParent() instanceof BlockIha) {
								((LineIha) ip).setLink(self.getParent());
				  			    ((BlockIha) self.getParent()).setLink(ip);
				   			}
							
						}
		    		  
		    		  
			    		if (!item.notDraggeble) {
			    			
							item.centerXProperty().bind(self.centerXProperty());
							item.centerYProperty().bind(self.centerYProperty());

			    	  	
			    		} else {
			    			
			    			System.out.println("    L I N K S   M O V E   ");
			    			item.printLink(item.getParent());
			    			if (count == 1) {

			    				self.notDraggeble = true;
					  	  	    self.centerXProperty().bind(item.centerXProperty());
					  	  	    self.centerYProperty().bind(item.centerYProperty());
					  	  	    
			    			}
			    			
				  	  	    item.getParent().toFront();
			    			
			    	  	}
		    	    }
		    	  }
        	  } 
        	  
	          if ((newX == getSelectX()) && (newY == getSelectY())) {
	        	  self.setSelected(newX, newY);
	          } else {
	        	  self.removeSelected(); 
	          }
	        	  
	          
 	  		  if (!link.isEmpty()) bp.link.addAll(link);
        	  bp.link.add(self);
   
          } //Move block Stop    	
    }
    
    public String getAnchorId(){
    	return id;
    }
  
    public void printLink(Object main){
		
		int count = 0;
    	
		Set<Object> item = null;
		
		if (main instanceof BlockIha) {
			item = ((BlockIha) main).getLink();
			if (item.size() == 0) return; 
		} if (main instanceof LineIha) {
			item = ((LineIha) main).getLink();
			if (item.size() == 0) return;
    	} else return;
		
		System.out.println("-LINKS-START-FOR---" +  main.toString().substring(20, main.toString().length()).split("@")[0] 
                + ":" + main.toString().substring(20, main.toString().length()).split("@")[1].substring(3) + "--------");
		
		
		for (Object obj : item) {
			count++;
    		System.out.println("| " + count + ") " + obj.toString().substring(20, obj.toString().length()).split("@")[0] 
    				                + ":" + obj.toString().substring(20, obj.toString().length()).split("@")[1].substring(3));
    	}
		
		System.out.println("-LINKS-END-------------------------------------------------------------------");
    }
  }  

  class Pixel {
	  public ArrayList<Anchor> link = new ArrayList<Anchor>();
	  public int staff;
  }

  
  private void enableDrag(Scene scene, Group root) {
	  scene.setOnMousePressed(new EventHandler<MouseEvent>() {
	    @Override public void handle(MouseEvent mouseEvent) {
	      
	      PickResult pr = mouseEvent.getPickResult();
	      Node rn = pr.getIntersectedNode();
	      
	      if (mouseEvent.isSecondaryButtonDown()){
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
	    			  
	    			  if ((parent instanceof LineIha)) {
	    				  ((LineIha) parent).dispose();
	    				  
	    				  for (Object item : ((LineIha) parent).link) {
	    					  if (item instanceof LineIha) {
	    						  ((LineIha) item).removeLink(parent);
	    						  
	    					  }
						  }
	    			  } 
	    			  
	    			
	    			  if ((parent instanceof BlockIha)) {
	    				  
	    				  BlockIha bi = ((BlockIha) parent);
	    				  
	    				  for (Object itemB : bi.link) {
	    					  if (itemB instanceof LineIha) {
	    						  
	    						  Set<Object> ln =  ((LineIha) itemB).link;
	    						  // unregister the line from others Start 
	    						  
	    						  for (Object item2 : ln) {
									if (item2 != parent) {
										if (item2 instanceof LineIha) {
										  ((LineIha) item2).removeLink(itemB);
									  	}
									  
									  	if (item2 instanceof BlockIha) {
										  ((BlockIha) item2).removeLink(itemB);
  										}
									}
	    						  }
	    						  
	    						  // unregister the line from others Stop
	    						  
	    						  ((LineIha) itemB).dispose();
	    						  root.getChildren().remove(itemB);
	    					  }
						  }
	    				  
	    				  bi.dispose();
	    			  }
	    			
	    			  
	    			  root.getChildren().remove(parent);
    			  }
	    	  }
	      }
	      
	      if (mouseEvent.isPrimaryButtonDown()){
	    	  if ( rn != null){
	    		  
	    		  if (rn instanceof Line){
	    			  LineIha o = (LineIha) rn.getParent();
	    			  
	    			  System.out.println("---------------------------------------------------------------------------------------");
	    			  System.out.println("| <-- Object: " + o + " -->");

	    			  System.out.println("|         --------------------------");
	    			  
	    			  for (Object item : o.link) {
	    				  System.out.println("|  " + item);
					  }
					  
	    			  
	    			  System.out.println("| (start: x=" + o.start.centerXProperty().get() + " y=" + o.start.centerYProperty().get() + 
	    					              ") (end: x=" + o.end.centerXProperty().get() + " y=" + o.end.centerYProperty().get() + ")" );
	    			  System.out.println("---------------------------------------------------------------------------------------");
	    		  }
	    		  
	    		  
	    		  if (rn instanceof Circle) {
	    			  
	    			  Anchor circle = (Anchor) rn;
	    			  
	    			  int x = (int) circle.centerXProperty().get();
	    			  int y = (int) circle.centerYProperty().get();
	    			  
	    			  Pixel bp = pool[x][y];
	    			  
	    			  if (!controlDown) { 

	    				  System.out.println("  <<<<<<<<<<<<<<<<<<< CONTAIN POINTS >>>>>>>>>>>>>>>>>>>>>");
		    			  for (Anchor item : bp.link) {
							System.out.println(item);
						  }
		    			  System.out.println("  <<<<<<<<<<<<<<<<<<<<<<< E N D >>>>>>>>>>>>>>>>>>>>>");
	    			  
	    			  } else {
		    			  
	    				  if (!bp.link.isEmpty() && circle.getDraggeble() && !circle.selected) {
		    				  
		    				  System.out.println("  <<<<<<<<<<<<<<<<<<< CREATE LINKER >>>>>>>>>>>>>>>>>>>>>");
		    				  
		    				  
		    				  Group gr = new LineIha(x, y, Color.RED, pool);
			    			  
			    			  root.getChildren().add(gr);
		    			  }
	    			  
	    			  }
	    		  }
	    		  
	    	  }
	       }
	      
	    }
	  });
      scene.setOnMouseExited(new EventHandler<MouseEvent>() {
          @Override public void handle(MouseEvent mouseEvent) {
            if (!mouseEvent.isPrimaryButtonDown()) {
              scene.setCursor(Cursor.DEFAULT);
            }
          }
        });
      scene.setOnKeyPressed(new EventHandler<KeyEvent>(){
		@Override public void handle(KeyEvent keyEvent) {
			controlDown = keyEvent.isControlDown(); 
		}
 	});
      
      scene.setOnKeyReleased(new EventHandler<KeyEvent>(){
		@Override public void handle(KeyEvent keyEvent) {
			controlDown = false; 
		}
 	});

   }

}