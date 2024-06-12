package org.example.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import org.example.UserInterface;
import org.example.island.commands.Message;

import java.io.IOException;

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
    private Alert alert = new Alert(Alert.AlertType.INFORMATION);
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
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getResource("/mainScene.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        UserInterface.getStage().setScene(new Scene(root));
        UserInterface.getStage().show();
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
