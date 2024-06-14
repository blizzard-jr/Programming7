package org.example.controllers;

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
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.UserInterface;
import org.example.island.commands.Command;
import org.example.island.commands.Exit;
import org.example.island.commands.InsertNull;
import org.example.island.commands.Message;
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
    private ObservableList<TableGroup> list;

    public void initialize() {
        table.setRowFactory(tv -> {
            TableRow<TableGroup> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
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
        collectionInit((List<TableGroup>) msg.getArguments().get(msg.getArguments().size() - 1));
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
}
