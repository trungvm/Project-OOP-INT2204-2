package oop.bufihofa.app;


import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import oop.bufihofa.classes.Item;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import oop.bufihofa.classes.*;

public class ui implements Initializable {
    @FXML
    private TableView<Item> table;

    @FXML
    private TableColumn<Item, Integer> idColumn;

    @FXML
    private TableColumn<Item, String> nameColumn;

    @FXML
    private TableColumn<Item, Double> quantityColumn;

    @FXML
    private TableColumn<Item, String> unitColumn;

    @FXML
    private TableColumn<Item, Integer> priceColumn;

    @FXML
    private TextField nameField;
    @FXML
    private TextField quantityField;
    @FXML
    private TextField unitField;
    @FXML
    private TextField priceField;

    @FXML
    private Label errorLabel;
    @FXML
    private Label idLabel;
    //@FXML
    //private TableColumn<Item, LocalDate> dateColumn;
    private int ID = 1000000005;
    private ObservableList<Item> storage;

    @Override
    public void initialize(URL location, ResourceBundle rcs){
        storage = FXCollections.observableArrayList(
                new Item("Rice 1", 1000000001, 500, 3, "kg"),
                new Item("Water 1", 1000000002, 700, 5, "m3"),
                new Item("Rice 2", 1000000003, 600, 2.5, "kg"),
                new Item("Water 2", 1000000004, 800, 1.5, "m3")
        );
        idColumn.setCellValueFactory(new PropertyValueFactory<Item, Integer>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("name"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<Item, Double>("quantity"));
        unitColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("unit"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<Item, Integer>("price"));
        table.setItems(storage);
        table.setEditable(true);
    }
    public void printError(String s){
        errorLabel.setText(s);
    }
    //them
    public void onAddButtonClicked(){
        printError("");
        String name = nameField.getText();
        String unit = unitField.getText();
        double price = 0;
        double quantity = 0;

        boolean kt = true;
        if(name.isEmpty()){
            printError("Tên không hợp lệ!");
            kt = false;
        }
        if(unit.isEmpty()){
            printError("Đơn vị không hợp lệ!");
            kt = false;
        }
        if(!priceField.getText().isEmpty()) {
            try {
                price = Double.parseDouble(priceField.getText());

            } catch (Exception e){
                printError("Giá không hợp lệ!");
                kt = false;
            }
        }
        if(!quantityField.getText().isEmpty()){
            try {
                quantity = Double.parseDouble(quantityField.getText());
            } catch (Exception e){
                printError("Số lượng không hợp lệ!");
                kt = false;
            }
        }
        if(kt){
            storage.add(new Item(name, ID++, price, quantity, unit));
        }
    }
    //xem
    public void onTableClicked(MouseEvent e){
        Item selected = table.getSelectionModel().getSelectedItem();
        idLabel.setText("ID:"+Integer.toString(selected.getId()));
        nameField.setText(selected.getName());
        unitField.setText(selected.getUnit());
        priceField.setText(Double.toString(selected.getPrice()));
        quantityField.setText(Double.toString(selected.getQuantity()));
    }
    //sua
    public void onEditButtonClicked(){
        printError("");
        String name = nameField.getText();
        String unit = unitField.getText();
        double price = 0;
        double quantity = 0;

        boolean kt = true;
        if(name.isEmpty()){
            printError("Tên không hợp lệ!");
            kt = false;
        }
        if(unit.isEmpty()){
            printError("Đơn vị không hợp lệ!");
            kt = false;
        }
        if(!priceField.getText().isEmpty()) {
            try {
                price = Double.parseDouble(priceField.getText());

            } catch (Exception e){
                printError("Giá không hợp lệ!");
                kt = false;
            }
        }
        if(!quantityField.getText().isEmpty()){
            try {
                quantity = Double.parseDouble(quantityField.getText());
            } catch (Exception e){
                printError("Số lượng không hợp lệ!");
                kt = false;
            }
        }
        if(kt){
            Item selected = table.getSelectionModel().getSelectedItem();
            try {
                selected.setPrice(price);
                selected.setName(name);
                selected.setQuantity(quantity);
                selected.setUnit(unit);
                table.refresh();
                printError("Sửa thành công ID:" + selected.getId());
            }
            catch(Exception e){
                printError(e.getMessage());
            }
        }
    }
    public void onRemoveButtonClicked(){
        Item selected = table.getSelectionModel().getSelectedItem();
        try {
            storage.remove(selected);
        }
        catch(Exception e){
            printError(e.getMessage());
        }
    }
}