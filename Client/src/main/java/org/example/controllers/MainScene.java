package org.example.controllers;

import javafx.animation.PauseTransition;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.UserInterface;
import org.example.island.commands.*;
import org.example.island.details.Serialization;
import org.example.island.object.TableGroup;
import org.example.island.object.TableGroup;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static org.example.UserInterface.inputData;
import static org.example.UserInterface.outputData;


public class MainScene {
    @FXML
    private AnchorPane anchor;
    @FXML
    private TableView<TableGroup> table;
    @FXML
    private TableColumn<TableGroup, Integer> id;
    @FXML
    private TableColumn<TableGroup, Integer> key;
    @FXML
    private TableColumn<TableGroup, String> gr_name;
    @FXML
    private TableColumn<TableGroup, Integer> st_count;
    @FXML
    private TableColumn<TableGroup, Integer> shouldExp;
    @FXML
    private TableColumn<TableGroup, String> form;
    @FXML
    private TableColumn<TableGroup, String> sem;
    @FXML
    private TableColumn<TableGroup, Integer> c_x;
    @FXML
    private TableColumn<TableGroup, Integer> c_y;
    @FXML
    private TableColumn<TableGroup, String> p_name;
    @FXML
    private TableColumn<TableGroup, Integer> p_height;
    @FXML
    private TableColumn<TableGroup, Integer> p_weight;
    @FXML
    private TableColumn<TableGroup, String> p_color;
    @FXML
    private TableColumn<TableGroup, Integer> l_x;
    @FXML
    private TableColumn<TableGroup, Integer> l_y;
    @FXML
    private TableColumn<TableGroup, Integer> l_z;
    @FXML
    private TableColumn<TableGroup, String> l_name;
    @FXML
    private TableColumn<TableGroup, String> creationDate;
    @FXML
    private TableColumn<TableGroup, String> owner;
    @FXML
    private Button insert;
    @FXML
    private Label login;
    @FXML
    private MenuItem exit;
    @FXML
    private MenuItem language;
    @FXML
    private MenuItem clear;
    @FXML
    private MenuItem info;
    @FXML
    private MenuItem history;
    @FXML
    private MenuItem removeLower;
    @FXML
    private ImageView galochka;

    private ObservableList<TableGroup> list;

    public void initialize() {
        login.setText(UserInterface.getLogin());
        table.setRowFactory(tv -> {
            TableRow<TableGroup> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.SECONDARY) {
                    try {
                        List<Node> anchorPanesToRemove = new ArrayList<>();
                        for (Node child : anchor.getChildren()) {
                            if (child instanceof AnchorPane) {
                                anchorPanesToRemove.add(child);
                            }
                        }
                        anchor.getChildren().removeAll(anchorPanesToRemove);
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(getClass().getResource("/elSetting.fxml"));
                        Parent vboxMenu = loader.load();
                        ElSetting setting = loader.getController();
                        setting.init(this, anchor);
                        vboxMenu.setLayoutX(event.getSceneX());
                        vboxMenu.setLayoutY(event.getSceneY());
                        anchor.getChildren().add(vboxMenu);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }else{
                    List<Node> anchorPanesToRemove = new ArrayList<>();
                    for (Node child : anchor.getChildren()) {
                        if (child instanceof AnchorPane) {
                            anchorPanesToRemove.add(child);
                        }
                    }
                    anchor.getChildren().removeAll(anchorPanesToRemove);
                }
            });
            return row;
        });
    }
    public ObservableList<TableGroup> getList() {
        return list;
    }

    public void process(Command cmd) {
        cmd.setArguments(UserInterface.getLog());
        outputData(Serialization.SerializeObject(cmd));
        Message msg = inputData();
        StringBuilder text = new StringBuilder();
        if(msg.getArguments().get(0).equals("ок")){
            Image im = new Image("gal.png");
            galochka.setImage(im);
            PauseTransition delay = new PauseTransition(Duration.seconds(10));
            delay.setOnFinished(event -> galochka.setImage(null));
            delay.play();
        }
        else {
            for (Object obj : msg.getArguments()) {
                text.append(obj).append("\n");
            }
            Image im = new Image("crestic.png");
            galochka.setImage(im);
            PauseTransition delay = new PauseTransition(Duration.seconds(10));
            delay.setOnFinished(event -> galochka.setImage(null));
            delay.play();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/GeneralOutput.fxml"));
            try {
                TextFlow parent = loader.load();
                TextArea textArea = (TextArea) parent.getChildren().get(0);
                textArea.setText(text.toString());
                Scene scene = new Scene(parent);
                Stage st = new Stage();
                st.setScene(scene);
                st.showAndWait();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        List<TableGroup> newList = (List<TableGroup>) msg.getArguments().get(msg.getArguments().size() - 1);
        collectionInit(newList);
        UserInterface.setData(newList);
    }

    public void collectionInit(List<TableGroup> data) {
        list = FXCollections.observableList(data);// определить логику для команд, нажимаешь на строку таблицы - окно с действиями над объектом, остальные команды можно в меню скинуть
        table.setItems(list);
    }

    public void rowSelected() {
        System.out.println("ehhhff");
    }

    public TableView<TableGroup> getTable() {
        return table;
    }

    public void executeInsert() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Insert.fxml"));
        try {
            Parent p = loader.load();
            InsertControl insertControl = loader.getController();
            insertControl.init(this);
            Scene scene = new Scene(p);
            Stage st = new Stage();
            st.setScene(scene);
            st.showAndWait();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void executeExit(){
        Exit exit = new Exit();
        process(exit);
    }
    public void executeClear(){
        Clear clear = new Clear();
        process(clear);
    }
    public void executeInfo(){
        Info info = new Info();
        process(info);
    }
    public void executeHistory(){
        History history = new History();
        process(history);
    }

}
