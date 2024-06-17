package org.example.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import org.example.UserInterface;
import org.island.commands.Message;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import static com.sun.javafx.scene.control.skin.Utils.getResource;

public class EnterScene {
    @FXML
    private TextField login;
    @FXML
    private PasswordField password;
    @FXML
    private TextField port;
    @FXML
    private CheckBox register;
    @FXML
    private Button enter;
    @FXML
    private Button Lang;
    @FXML
    private Label welcome;
    @FXML
    private Label logIn;
    @FXML
    private Label pass;
    @FXML
    private Label log;
    @FXML
    private Label ports;
    private MainScene mainScene;
    private FXMLLoader loader = new FXMLLoader();
    private ResourceBundle bundle = ResourceBundle.getBundle("resources_en_US", new Locale("en", "US"));


    private Alert alert = new Alert(Alert.AlertType.INFORMATION);

    public ResourceBundle getBundle() {
        return bundle;
    }

    public void enterClicked(){
        if(port.getText().isEmpty() || password.getText().isEmpty() || login.getText().isEmpty()){
            alert.setTitle("Пустой ввод");
            alert.setContentText("Вам необходимо ввести значения для каждого поля");
            alert.showAndWait();
            return;
        }
        int p = Integer.parseInt(port.getText());
        UserInterface.setPort(p);
        UserInterface.setPassword(password.getText());
        UserInterface.setLogin(login.getText());
        UserInterface.setRegister(register.isSelected());
        UserInterface.connect();
        UserInterface.authentication(new Message());
        loader.setLocation(getResource("/mainScene.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        mainScene = loader.getController();
        mainScene.init(bundle);
        mainScene.collectionInit(UserInterface.getData());
        UserInterface.getStage().setScene(new Scene(root));
        UserInterface.getStage().show();
    }
    public void choseLang(){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Language.fxml"));
        try {
            Parent parent = loader.load();
            LangControl langControl = loader.getController();
            langControl.initEntr(this);
            langControl.initMain(mainScene);
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public void setLocale(ResourceBundle bundle){
        ports.setText(bundle.getString("ports"));
        logIn.setText(bundle.getString("logIn"));
        pass.setText(bundle.getString("pass"));
        welcome.setText(bundle.getString("welcome"));
        log.setText(bundle.getString("log"));
        register.setText(bundle.getString("register"));
        enter.setText(bundle.getString("enter"));
        Lang.setText(bundle.getString("Lang"));
        this.bundle = bundle;
    }
    public void exceptionMessage(String title, String text){
        if(title != null){
            alert.setTitle(title);
        }
        alert.setContentText(text);
        alert.showAndWait();
    }

    public void exceptionMessage(Message msg){
        StringBuilder str = new StringBuilder();
        for(Object obj : msg.getArguments()){
            str.append(obj).append("\n");
        }
        alert.setTitle("Неудача");
        alert.setContentText("Попытка входа в систему завершилась неудачей");
        alert.showAndWait();
    }



}
