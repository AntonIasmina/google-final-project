package com.example.lab.controller;


import com.example.lab.HelloApplication;
import com.example.lab.domain.User;
import com.example.lab.domain.validate.ValidationException;
import com.example.lab.services.FriendshipService;
import com.example.lab.services.UtilizatorService;
import com.example.lab.utils.events.UserEntityChangeEvent;
import com.example.lab.utils.observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class UserController implements Observer<UserEntityChangeEvent> {

    public TableView tableView;
    UtilizatorService service;
    FriendshipService fservice;
    ObservableList<User> model = FXCollections.observableArrayList();

    @FXML
    TableColumn<User, String> tableColumnFirstName;
    @FXML
    TableColumn<User, String> tableColumnLastName;


    public void setUserService(UtilizatorService service, FriendshipService fservice) {
        this.service = service;
        this.fservice=fservice;
        service.addObserver(this);
        initModel();
    }

    @FXML
    public void initialize() {
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<User, String>("lastName"));
        tableView.setItems(model);
    }

    private void initModel() {
        Iterable<User> messages = service.getAllUsers();
        List<User> users = StreamSupport.stream(messages.spliterator(), false)
                .collect(Collectors.toList());
        model.setAll(users);
    }

    @Override
    public void update(UserEntityChangeEvent UserEntityChangeEvent) {

        initModel();
    }

    public void handleAddUser(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/addView.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("ADD PAGE");
        AddController adaugaController = fxmlLoader.getController();
        adaugaController.setService(service);
        stage.show();
    }

    public void handleDeleteUser(ActionEvent actionEvent) {
        User user = (User) tableView.getSelectionModel().getSelectedItem();
        if (user != null) {
            User deleted = service.deleteUtilizator(user.getId());
            fservice.deleteFriendsOfUser(user.getId());
        }
        initModel();
    }

    public void handleShowFriends(ActionEvent actionEvent) throws IOException {
        User user = (User) tableView.getSelectionModel().getSelectedItem();
        tableView.getSelectionModel().clearSelection();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/manageFriends.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("SHOW FRIENDS PAGE");
        FriendController fController = fxmlLoader.getController();
        fController.setService(fservice, user.getId());
        stage.show();
        tableView.getSelectionModel().clearSelection();
    }

    public void handleSeeRequests(ActionEvent actionEvent) throws IOException {
        User user = (User) tableView.getSelectionModel().getSelectedItem();
        tableView.getSelectionModel().clearSelection();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/manageRequests.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("SHOW REQUESTS PAGE");
        RequestController rController = fxmlLoader.getController();
        rController.setService(fservice, user.getId());
        stage.show();
    }

    public void handleUpdateUser(ActionEvent actionEvent) {
    }

    public void handleButton(ActionEvent actionEvent) {
        System.out.println("Hello!");
    }
}