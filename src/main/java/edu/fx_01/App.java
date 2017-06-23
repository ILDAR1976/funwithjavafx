package edu.fx_01;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


    public class App extends Application {

        @Override
        public void start(Stage primaryStage) {

            Pane pane = new Pane();
            pane.setMinSize(400, 400);
            pane.setPadding(new Insets(10));

            Button b = new Button("Drag Source (b)");
            Button c = new Button("Drag Target (c)");
            //here we load a custom image
            Image img = new Image("http://2.bp.blogspot.com/-ipjep9g59YY/VbqLU1vD9qI/AAAAAAAAAOc/CjB4YvRYz_M/s1600/skype-drunk.jpg");

            b.setOnDragOver(event->{
                    if (event.getGestureSource() != b && event.getDragboard().hasString()) {
                        event.acceptTransferModes(TransferMode.MOVE);
                        System.out.println("DragOver (b)");
                    }
                    event.consume();
            });

            b.setOnDragDropped(e -> {
                System.out.println("DragDropped (b)");
                //added following line as cursor wasn´t reseted to default
                b.setCursor(Cursor.DEFAULT);
                e.setDropCompleted(true);
                e.consume();
            });

            b.setOnDragExited(event->{
                event.consume();
                System.out.println("DragExited (b)");
                b.setCursor(Cursor.CROSSHAIR);
            });

            b.setOnDragDetected(e-> {
                Dragboard db = b.startDragAndDrop(TransferMode.MOVE);

                ClipboardContent content = new ClipboardContent();
                content.putString("Hey i´m b");
                db.setContent(content);
                b.setCursor(Cursor.OPEN_HAND);
                //b.setCursor(new ImageCursor(img));
                e.consume();
            });

            b.setOnDragDone(event->{
            /* the drag and drop gesture ended */
            /* if the data was successfully moved, do something and reset Cursor */
                    if (event.getTransferMode() == TransferMode.MOVE) {                        
                        //b.setCursor(Cursor.DEFAULT);
                    }
                    b.setCursor(Cursor.DEFAULT);
                    event.consume();
            });

            c.setOnDragOver(event->{
                if (event.getGestureSource() != c && event.getDragboard().hasString()) {
                    event.acceptTransferModes(TransferMode.MOVE);
                    System.out.println("DragOver (c)");
                }

                event.consume();

            });

            c.setOnDragDropped(e -> {
                System.out.println("DragDropped (c)");
                e.setDropCompleted(true);
                e.consume();
            });

            c.setOnDragExited(event->{
                event.consume();
                System.out.println("DragExited (c)");
            });


            c.setOnDragDetected(e-> {
                Dragboard db = c.startDragAndDrop(TransferMode.MOVE);

                ClipboardContent content = new ClipboardContent();
                content.putString("Hey i´m c");
                db.setContent(content);
                c.setCursor(Cursor.OPEN_HAND);
                e.consume();
            });
            c.setOnDragDone(event->{
            /* the drag and drop gesture ended */
            /* if the data was successfully moved, do something and reset Cursor */
                if (event.getTransferMode() == TransferMode.MOVE) {
                    c.setCursor(Cursor.DEFAULT);
                }
                event.consume();
            });

            VBox box = new VBox(b,c);
            box.setSpacing(20);

            BorderPane root = new BorderPane(pane, box, null, null, null);
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        }


        public static void main(String[] args) {
            launch(args);
        }
    }