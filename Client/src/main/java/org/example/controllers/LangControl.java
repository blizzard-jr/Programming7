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
    private boolean mainOrEntr = false;

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
        setLocale("resources_lv_LV", new Locale("lv", "LV"));
    }
    public void eest(){
        setLocale("resources_et_EE", new Locale("et", "EE"));
    }
    public void engl(){
        setLocale("resources_en_NZ", new Locale("en", "NZ"));
    }

    public void setLocale(String localeFile, Locale locale) {
        ResourceBundle bundle = ResourceBundle.getBundle(localeFile, locale);
        if(mainOrEntr){
            mainScene.setLocale(bundle);
        }else {
            enterScene.setLocale(bundle);
        }
    }
    public void setMainOrEntr(boolean mainOrEntr) {
        this.mainOrEntr = mainOrEntr;
    }
}
