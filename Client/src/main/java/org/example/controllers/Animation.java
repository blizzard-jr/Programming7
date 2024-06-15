package org.example.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.island.object.TableGroup;

import javafx.scene.input.MouseEvent;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Animation {
    private List<TableGroup> collection;
    private GraphicsContext gc;
    private double canvasWidth;
    private double canvasHeight;
    private static HashMap<Integer, MyRectangle> elements = new HashMap<Integer, MyRectangle>();
    private static ArrayList<Integer> toDelete= new ArrayList<>();
    public Animation(GraphicsContext gc, List<TableGroup> collection, double canvasWidth, double canvasHeight) {
        this.collection = collection;
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
        this.gc = gc;

    }

    public static void setElemsToDelete(ArrayList<TableGroup> deletedElems) {
        if (!deletedElems.isEmpty()) deletedElems.forEach(v -> toDelete.add(v.getId()));
    }


    private class MyRectangle {
        private javafx.beans.property.DoubleProperty x = new javafx.beans.property.SimpleDoubleProperty();
        private javafx.beans.property.DoubleProperty y = new javafx.beans.property.SimpleDoubleProperty();
        double width, height;
        double targetX, targetY;
        TableGroup tableGroup;
        Color color;

        MyRectangle(double targetX, double targetY, double width, double height, Color color, TableGroup tableGroup) {
            this.targetX = targetX;
            this.targetY = targetY;
            this.width = width;
            this.height = height;
            this.color = color;
            this.tableGroup = tableGroup;
        }

        double getX() {
            return x.get();
        }

        double getY() {
            return y.get();
        }

        void setPosition(double x, double y) {
            this.x.set(x);
            this.y.set(y);
        }

        boolean contains(double px, double py) {
            return px >= x.get() && px <= x.get() + width && py >= y.get() && py <= y.get() + height;
        }

        javafx.beans.property.DoubleProperty xProperty() {
            return x;
        }

        javafx.beans.property.DoubleProperty yProperty() {
            return y;
        }
    }

    public void drawShapes() {
        gc.clearRect(0, 0, canvasWidth, canvasHeight);

        for (MyRectangle rect : elements.values()) {

            gc.setFill(rect.color);
            gc.fillRect(rect.getX(),
                    rect.getY(),
                    rect.width,
                    rect.height);

        }
    }


    public void initializeCollection(List<TableGroup> collection) {

        for (TableGroup elem : collection) {

            Color color = Color.color((double) (elem.getOwner().hashCode() % 9) / 10,
                    (double) (elem.getOwner().hashCode() % 8) / 10,
                    (double) (elem.getOwner().hashCode() % 5) / 10);
            MyRectangle rect = new MyRectangle(
                    Math.round(canvasWidth / 2 + (elem.getC_x() % 500)),
                    (int) Math.round(canvasHeight / 2 + (elem.getC_y() % 500)),
                    Math.round(10 + elem.getP_height() % 50),
                    (int) Math.round(10 + elem.getP_weight() % 50),
                    color,
                    elem);

            elements.put(elem.getId(), rect);
        }

    }

    public void startAnimation(List<TableGroup> collection) {
        initializeCollection(collection);
        Timeline timeline = new Timeline();

        for (MyRectangle rect : elements.values()) {
            double startX = (canvasWidth - rect.width) / 2;
            double startY = (canvasHeight - rect.height) / 2 + 100;

            KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.8),
                    new javafx.animation.KeyValue(rect.xProperty(), rect.targetX),
                    new javafx.animation.KeyValue(rect.yProperty(), rect.targetY)
            );
            rect.setPosition(startX, startY);
            rect.xProperty().addListener((obs, oldVal, newVal) -> drawShapes());
            rect.yProperty().addListener((obs, oldVal, newVal) -> drawShapes());
            timeline.getKeyFrames().add(keyFrame);
        }
        timeline.setCycleCount(1);
        timeline.setOnFinished(event -> boom());
        timeline.play();

    }
    public void boom(){
        gc.clearRect(0, 0, canvasWidth, canvasHeight);

        for (MyRectangle rect : elements.values()) {
            double canvasWidth = gc.getCanvas().getWidth();
            double canvasHeight = gc.getCanvas().getHeight();
            if (toDelete != null && toDelete.contains(rect.tableGroup.getId())){
                try{
                    Image image = new Image("boom.png");
                    double bgX = 20;
                    double bgY = 20;
                    gc.drawImage(image, rect.getX(), rect.getY(),50,50);
                } catch (NullPointerException e){
                    gc.setFill(Color.ORANGE);
                    gc.fillOval(rect.getX(), rect.getY(),30,30);
                }

                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), event -> {
                    drawShapes();
                }));
                timeline.setCycleCount(1);
                timeline.play();

            }
            else drawShapes();

            if (toDelete != null && ! toDelete.isEmpty())toDelete.clear();
        }
    }


    public static void handleMouseClick(MouseEvent event) {
        double x = event.getX();
        double y = event.getY();
        for (Integer key : elements.keySet()) {
            if (elements.get(key).contains(x, y)) {
                MyRectangle rect = elements.get(key);

                String data = "Information about " + rect.tableGroup.getId() + "\n"
                        + "ID: " + rect.tableGroup.getId() + "\n" +
                        "coords:(" + rect.getX() + ", " + rect.getY() + ")\n"
                        + "owner: " + rect.tableGroup.getOwner();


                Label label = new Label(data);


                StackPane root = new StackPane();
                root.getChildren().add(label);


                Scene scene = new Scene(root, 300, 200);

                // Настраиваем и показываем основное окно
                Stage primaryStage = new Stage();
                primaryStage.setTitle("info");
                primaryStage.setScene(scene);
                primaryStage.show();


            }

        }
    }


}

