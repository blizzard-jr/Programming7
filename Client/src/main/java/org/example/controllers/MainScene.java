package org.example.controllers;

import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn;
import javafx.scene.control.skin.TableColumnHeader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.UserInterface;
import org.example.island.commands.*;
import org.example.island.details.Serialization;
import org.example.island.object.TableGroup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.example.UserInterface.*;


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
    @FXML
    private Button refresh;


    private ObservableList<TableGroup> list;
    public void refresh(){
        process(new Show());

    }

    public void initialize() {
        table.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            Node node = event.getPickResult().getIntersectedNode();
            while (node != null && !(node instanceof TableColumnHeader) && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                node = node.getParent();
            }
            if (node instanceof TableColumnHeader) {
                List<Node> anchorPanesToRemove = new ArrayList<>();
                for (Node child : anchor.getChildren()) {
                    if (child instanceof AnchorPane) {
                        anchorPanesToRemove.add(child);
                    }
                }
                anchor.getChildren().removeAll(anchorPanesToRemove);
                TableColumnHeader header = (TableColumnHeader) node;
                String text = header.getTableColumn().getText();
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/sort.fxml"));
                try {
                    Parent p = loader.load();
                    SortAndFilter sortAndFilter = loader.getController();
                    sortAndFilter.init(this, anchor, text);
                    p.setLayoutX(event.getSceneX());
                    p.setLayoutY(event.getSceneY());
                    anchor.getChildren().add(p);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
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
        login.setText(UserInterface.getLogin());
    }
    public ObservableList<TableGroup> getList() {
        return list;
    }
    private void Sort(MouseEvent event) {
        if (event.getClickCount() > 0 && event.getTarget() instanceof TableColumnHeader) {
            TableColumnHeader header = (TableColumnHeader) event.getTarget();
            System.out.println("Заголовок столбца " + header.getTableColumn().getText() + " был кликнут.");
        }
    }

    public void process(Command cmd) {
        cmd.setArguments(UserInterface.getLog());
        outputData(Serialization.SerializeObject(cmd));
        Message msg = inputData();
        if (ChangingCollectionCommand.class.isAssignableFrom(cmd.getClass())){
            List<TableGroup> collection = (List<TableGroup>) msg.getArguments().get(msg.getArguments().size() - 1);
            animateCollection(collection);
            collectionInit(collection);

            if(msg.getArguments().get(0).equals("ок")){
                Image im = new Image("gal.png");
                galochka.setImage(im);
                PauseTransition delay = new PauseTransition(Duration.seconds(10));
                delay.setOnFinished(event -> galochka.setImage(null));
                delay.play();
            }else{
                Image im = new Image("crestic.png");
                galochka.setImage(im);
                PauseTransition delay = new PauseTransition(Duration.seconds(10));
                delay.setOnFinished(event -> galochka.setImage(null));
                delay.play();
            }
        }
        else {
            StringBuilder text = new StringBuilder();
            for (Object obj : msg.getArguments()) {
                text.append(obj).append("\n");
            }
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
    }

    public void collectionInit(List<TableGroup> data) {
        list = FXCollections.observableList(data);// определить логику для команд, нажимаешь на строку таблицы - окно с действиями над объектом, остальные команды можно в меню скинуть
        table.setItems(list);
    }
    public static void animateCollection(List<TableGroup> collection){
        collection.forEach(t -> System.out.println(t.toString()));
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
        System.exit(0);
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
