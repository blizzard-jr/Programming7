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


import javafx.scene.input.MouseEvent;
import org.island.object.TableGroup;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Animation {
    public static GraphicsContext gc;
    public static double canvasWidth;
    public static double canvasHeight;
    private static HashMap<Integer, MyRectangle> elements = new HashMap<Integer, MyRectangle>();
    public static ArrayList<Integer> toDelete;

//    public Animation(GraphicsContext gc, double canvasWidth, double canvasHeight) {
//        canvasWidth = canvasWidth;
//        this.canvasHeight = canvasHeight;
//        this.gc = gc;
//
//    }


    public static class MyRectangle {
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

    public static void drawShapes() {
        gc.clearRect(0, 0, canvasWidth, canvasHeight);

        for (MyRectangle rect : elements.values()) {

            gc.setFill(rect.color);
            gc.fillRect(rect.getX(),
                    rect.getY(),
                    rect.width,
                    rect.height);

        }
    }


    public static void initializeCollection(List<TableGroup> collection) {
        for (TableGroup elem : collection) {
            Color color = Color.color((double) Math.abs(elem.getOwner().hashCode() % 9) / 10,
                    (double) Math.abs(elem.getOwner().hashCode() % 8) / 10,
                    (double) Math.abs(elem.getOwner().hashCode() % 5) / 10);
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

    public static void startAnimation(List<TableGroup> collection) {
        initializeCollection(collection);
        System.out.println("START ANIMATION");
        Timeline timeline = new Timeline();

        for (MyRectangle rect : elements.values()) {
            double startX = (canvasWidth - rect.width) / 2;
            double startY = (canvasHeight - rect.height) / 2 + 100;

            KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.6),
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

    public static void boom() {
        gc.clearRect(0, 0, canvasWidth, canvasHeight);
        if (toDelete == null) {
            drawShapes();
            return;
        }

        for (MyRectangle rect : elements.values()) {
            if (toDelete.contains(rect.tableGroup.getId())){
                System.out.println("да надо удалять айди"+rect.tableGroup.getId());
                gc.drawImage(new Image("boom.png"), rect.targetX, rect.targetY, 100, 100);
            } else {
                System.out.println("нет не надо удалять рисую прямоуг"+rect.tableGroup.getId());
                gc.setFill(rect.color);
                gc.fillRect(rect.targetX, rect.targetY, rect.width, rect.height);
            }

        }
        Timeline hideImageTimeline = new Timeline(new KeyFrame(Duration.seconds(0.3), event -> drawOnFinishAfterBoom()));
        hideImageTimeline.setCycleCount(1);
        hideImageTimeline.play();
    }


    public static void drawOnFinishAfterBoom() {
        System.out.println("i am in draw on finish");
        elements.entrySet().removeIf(entry -> toDelete.contains(entry.getValue().tableGroup.getId()));

        toDelete.clear();
        drawShapes();
        System.out.println("FINISHED ANIMATION");
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

