package org.example.controllers;

import com.sun.tools.javac.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.island.commands.Remove_key;
import org.island.object.TableGroup;

import java.io.IOException;

public class ElSetting {
    @FXML
    private Button delete;
    @FXML
    private Button update;
    AnchorPane anchorPane;

    MainScene mainScene;


    public void init(MainScene scene, AnchorPane anchorPane) {
        mainScene = scene;
        this.anchorPane = anchorPane;
    }

    public void updateEl(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/GroupFields.fxml"));
        try {
            Parent p = loader.load();
            ChangeEl changeEl = loader.getController();
            changeEl.init(mainScene.getTable().getSelectionModel().getSelectedItem(), mainScene);
            Scene scene = new Scene(p);
            Stage st = new Stage();
            st.setScene(scene);
            st.showAndWait();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateEl(ActionEvent actionEvent, MainScene mainScene, TableGroup tg) { // переопределение метода для вызова его
        // из окна инфо (при нажатии на элемент в визуализации)
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/GroupFields.fxml"));
        try {
            Parent p = loader.load();
            ChangeEl changeEl = loader.getController();
            changeEl.init(tg, mainScene);
            Scene scene = new Scene(p);
            Stage st = new Stage();
            st.setScene(scene);
            st.showAndWait();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteEl() {
        int key = mainScene.getTable().getSelectionModel().getSelectedItem().getKey();
        Remove_key removeKey = new Remove_key();
        removeKey.setArguments(key);
        mainScene.process(removeKey);
    }

    public void deleteEl(ActionEvent actionEvent, MainScene mainScene, TableGroup tg) {
        int key = tg.getKey();
        Remove_key removeKey = new Remove_key();
        removeKey.setArguments(key);
        mainScene.process(removeKey);
    }
}
