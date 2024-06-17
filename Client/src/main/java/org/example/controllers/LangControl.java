package org.example.controllers;

import com.sun.tools.javac.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.util.Locale;
import java.util.ResourceBundle;

public class LangControl {
    @FXML
    private Button rus;
    @FXML
    private Button engl;
    @FXML
    private Button eest;
    @FXML
    private Button latw;
    private EnterScene enterScene;
    private MainScene mainScene;

    public void rus(){
        setLocale("resources_ru_RU", Locale.forLanguageTag("ru-RU"));
    }
    public void initEntr(EnterScene enterScene){
        this.enterScene = enterScene;
    }
    public void initMain(MainScene mainScene){
        this.mainScene = mainScene;
    }
    public void latw(){
    }
    public void eest(){

    }
    public void engl(){
        setLocale("resources_en_US", new Locale("en", "US"));
    }

    public void setLocale(String localeFile, Locale locale) {
        ResourceBundle bundle = ResourceBundle.getBundle(localeFile, locale);
        enterScene.setLocale(bundle);

    }
}
