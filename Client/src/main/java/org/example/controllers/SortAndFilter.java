package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import org.example.UserInterface;
import org.example.island.object.TableGroup;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SortAndFilter {
    @FXML
    private Button sort;
    @FXML
    private Button filter;
    @FXML
    private ToggleButton upOrDown;
    @FXML
    private TextField filterValue;
    private String columnName;
    private MainScene mainScene;
    AnchorPane anchorPane;
    public void init(MainScene scene, AnchorPane anchorPane, String columnName){
        mainScene = scene;
        this.anchorPane = anchorPane;
        this.columnName = columnName;
    }
    public void upOrDown(){
        if(upOrDown.isSelected()){
            upOrDown.setText("Down");
        }else{
            upOrDown.setText("Up");
        }
    }

    public void sort(){
        if(!upOrDown.isSelected()){
            List<TableGroup> list = UserInterface.getData();
            list = (List<TableGroup>) list.stream()
                    .sorted(Comparator.comparing((TableGroup obj) -> (Comparable) getFieldValueByName(obj, columnName)))
                    .collect(Collectors.toList());
            mainScene.collectionInit(list);
        }
        else{
            List<TableGroup> list = UserInterface.getData();
            list = (List<TableGroup>) list.stream()
                    .sorted(Comparator.comparing((TableGroup obj) -> (Comparable) getFieldValueByName(obj, columnName)).reversed())
                    .collect(Collectors.toList());
            mainScene.collectionInit(list);
        }
    }
    public static Object getFieldValueByName(Object obj, String fieldName) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Ошибка при получении значения поля", e);
        }
    }
    public void filter(){
        List<TableGroup> list = UserInterface.getData();
        list = list.stream()
                .filter(obj -> {
                    Object fieldValue = getFieldValueByName(obj, columnName);
                    if(fieldValue instanceof Number){
                        if(new BigDecimal(fieldValue.toString()).compareTo(new BigDecimal(filterValue.getText())) == 0){
                            return true;
                        }
                        return false;
                    }else{
                        return Objects.equals(fieldValue, filterValue.getText());
                    }
                })
                .collect(Collectors.toList());
        mainScene.collectionInit(list);
    }
}
