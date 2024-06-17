package org.example.controllers;


import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn;
import javafx.scene.control.skin.TableColumnHeader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.UserInterface;
import org.island.commands.*;
import org.island.details.Serialization;
import org.island.object.TableGroup;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import static org.example.UserInterface.*;


public class MainScene {
    @FXML
    public Canvas canvas;
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
    @FXML
    private MenuItem script;
    @FXML
    private MenuItem lower;
    @FXML
    private MenuItem greater;
    @FXML
    private TextField Rkey;
    private Animation animation;
    private List<TableGroup> collection;
    private ObservableList<TableGroup> list;
    private ResourceBundle bundle;

    public void refresh() {
        process(new Show());
    }

    public void init(ResourceBundle bundle){
        this.bundle = bundle;
    }
    public void executeLang(){

    }
    public void executeScript(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File selectedFile = fileChooser.showOpenDialog(null);
        if(selectedFile != null){
            Execute_script scripts = new Execute_script();
            scripts.setArguments(selectedFile.getName());
            process(scripts);
        }
    }
    public void executeRemoveGreater(){
        InsertControl.setExInsert(false);
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
    public void executeRemoveLower(){
        int key = Integer.parseInt(Rkey.getText());
        Remove_lower_key removeLowerKey = new Remove_lower_key();
        removeLowerKey.setArguments(key);
        process(removeLowerKey);
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
                } else {
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
        canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, this::handleMouseClick);
    }

    public void handleMouseClick(MouseEvent event) {
        org.example.controllers.Animation.handleMouseClick(event);
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
        if (ChangingCollectionCommand.class.isAssignableFrom(cmd.getClass())) {
            if (UserInterface.getData() != null) {
                ArrayList<TableGroup> deletedElems = getDeletedElems(UserInterface.getData(),
                        (List<TableGroup>) msg.getArguments().get(msg.getArguments().size() - 1));
                if (deletedElems != null ) animation.setElemsToDelete(deletedElems);
                animateCollection(UserInterface.getData());
            }
            UserInterface.setData((List<TableGroup>) msg.getArguments().get(msg.getArguments().size() - 1));
            animateCollection(UserInterface.getData());
            collectionInit(UserInterface.getData());

            if (msg.getArguments().get(0).equals("ок")) {
                Image im = new Image("gal.png");
                galochka.setImage(im);
                PauseTransition delay = new PauseTransition(Duration.seconds(10));
                delay.setOnFinished(event -> galochka.setImage(null));
                delay.play();
            } else {
                Image im = new Image("crestic.png");
                galochka.setImage(im);
                PauseTransition delay = new PauseTransition(Duration.seconds(10));
                delay.setOnFinished(event -> galochka.setImage(null));
                delay.play();
            }
        } else {
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

    private ArrayList<TableGroup> getDeletedElems(List<TableGroup> collection, List<TableGroup> tableGroups) {
        if (collection.size() > tableGroups.size()) {
            collection.removeAll(tableGroups);
            return (ArrayList<TableGroup>) collection;
        }
        return null;
    }

    public void collectionInit(List<TableGroup> data) {
        list = FXCollections.observableList(data);// определить логику для команд, нажимаешь на строку таблицы - окно с действиями над объектом, остальные команды можно в меню скинуть
        table.setItems(list);
        animateCollection(data);
    }

    public void animateCollection(List<TableGroup> collection) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Animation animation = new org.example.controllers.Animation(gc, collection, canvas.getWidth(), canvas.getHeight());
        animation.startAnimation(collection);
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

    public void executeExit() {
        Exit exit = new Exit();
        process(exit);
        System.exit(0);
    }

    public void executeClear() {
        Clear clear = new Clear();
        process(clear);
    }

    public void executeInfo() {
        Info info = new Info();
        process(info);
    }

    public void executeHistory() {
        History history = new History();
        process(history);
    }
    public void setLocale(ResourceBundle bundle){
        id.setText(bundle.getString("id"));
        key.setText(bundle.getString("key"));
        gr_name.setText(bundle.getString("gr_name"));
        st_count.setText(bundle.getString("st_count"));
        shouldExp.setText(bundle.getString("shouldExp"));
        form.setText(bundle.getString("form"));
        sem.setText(bundle.getString("sem"));
        c_x.setText(bundle.getString("c_x"));
        c_y.setText(bundle.getString("c_y"));
        p_name.setText(bundle.getString("p_name"));
        p_height.setText(bundle.getString("p_height"));
        p_weight.setText(bundle.getString("p_weight"));
        p_color.setText(bundle.getString("p_color"));
        l_x.setText(bundle.getString("l_x"));
        l_y.setText(bundle.getString("l_y"));
        l_z.setText(bundle.getString("l_z"));
        l_name.setText(bundle.getString("l_name"));
        creationDate.setText(bundle.getString("creationDate"));
        owner.setText(bundle.getString("owner"));
        insert.setText(bundle.getString("insert"));
        login.setText(bundle.getString("login"));
        exit.setText(bundle.getString("exit"));
        language.setText(bundle.getString("language"));
        clear.setText(bundle.getString("clear"));
        info.setText(bundle.getString("info"));
        history.setText(bundle.getString("history"));
        removeLower.setText(bundle.getString("removeLower"));
        refresh.setText(bundle.getString("refresh"));
        script.setText(bundle.getString("script"));
        lower.setText(bundle.getString("lower"));
        greater.setText(bundle.getString("greater"));
        this.bundle = bundle;
    }
}
