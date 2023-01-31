package com.example.lab.controller;

import com.example.lab.domain.User;
import com.example.lab.domain.validate.ValidationException;
import com.example.lab.services.UtilizatorService;
import com.example.lab.utils.events.FriendshipEntityChangeEvent;
import com.example.lab.utils.events.UserEntityChangeEvent;
import com.example.lab.utils.observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class AddController implements Observer<UserEntityChangeEvent> {
    UtilizatorService service;
    @FXML
    private TextField firstname;
    @FXML
    private TextField lastname;
    @FXML
    private TextField username;
    @FXML
    public TableView<User> tableView;
    @FXML

    ObservableList<User> model= FXCollections.observableArrayList();

    public void setService(UtilizatorService service) {
        this.service = service;
        initModel();
        service.addObserver(this);
    }

    @FXML
    public void initialize() {

    }
    private void initModel() {
        Iterable<User> messages = service.getAllUsers();
        List<User> users = StreamSupport.stream(messages.spliterator(), false)
                .collect(Collectors.toList());
        model.setAll(users);
    }

    @Override
    public void update(UserEntityChangeEvent userEntityChangeEvent) {
        initModel();
    }
    public void handleAdauga(ActionEvent actionEvent) {

        try {
            String firstname1 = firstname.getText();
            String lastname1 = lastname.getText();
            String username1 = username.getText();
            User newUser = new User(firstname1,lastname1,username1);
            service.addUtilizator(newUser);
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "INFO", "Adaugare cu succes!");
        }catch(ValidationException e){
            MessageAlert.showErrorMessage(null, e.getMessage());
        }

    }
}
