package edu.fx_01;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Graph extends Application {
    private double startX;
    private double startY;

    private ObjectBinding<Bounds> bounds;
    private DoubleBinding tx;
    private DoubleBinding ty;

    public static void main(String[] args) throws Exception { launch(args); }
    @Override
    public void start(Stage primaryStage) throws Exception {
        Pane pane = new Pane();
        Circle target = new Circle(25, Color.RED);
        VBox node = wrap(target);
        Line connector = new Line();
       
        bounds = Bindings.createObjectBinding(() -> {
                    Bounds nodeLocal = target.getBoundsInLocal();
                    Bounds nodeScene = target.localToScene(nodeLocal);
                    Bounds nodePane = pane.sceneToLocal(nodeScene);
                    return nodePane;
                },
                target.boundsInLocalProperty(),
                target.localToSceneTransformProperty(),
                pane.localToSceneTransformProperty()
        );
        
       
        connector.setStartX(0);
        connector.setStartY(0);
        tx = Bindings.createDoubleBinding(() -> bounds.get().getMinX(), bounds);
        ty = Bindings.createDoubleBinding(() -> bounds.get().getMinY(), bounds);
        //connector.endXProperty().bind(tx);
        //connector.endYProperty().bind(ty);
        connector.setStroke(Color.BLACK);
        pane.getChildren().add(node);
        pane.getChildren().add(connector);
        node.relocate(240, 100);
        
        primaryStage.setScene(new Scene(pane, 300, 300));
        primaryStage.show();
    }

    private VBox wrap(Circle target) {
        VBox node = new VBox(new Label("Node"), new StackPane(new Rectangle(50, 50, Color.GRAY), target));
        node.setOnMousePressed(event -> {
            Node source = (Node) event.getSource();
            startX = source.getBoundsInParent().getMinX() - event.getScreenX();
            startY = source.getBoundsInParent().getMinY() - event.getScreenY();
        });
        node.setOnMouseDragged(event -> {
            Node source = (Node) event.getSource();
            double offsetX = event.getScreenX() + startX;
            double offsetY = event.getScreenY() + startY;
            source.relocate(offsetX, offsetY);
        });
        return node;
    }


}