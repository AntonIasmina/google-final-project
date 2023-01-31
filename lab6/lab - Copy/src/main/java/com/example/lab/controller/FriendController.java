package com.example.lab.controller;

import com.example.lab.HelloApplication;
import com.example.lab.domain.Friendship;
import com.example.lab.domain.Tuple;
import com.example.lab.domain.User;
import com.example.lab.domain.validate.ValidationException;
import com.example.lab.services.FriendshipService;
import com.example.lab.services.UtilizatorService;
import com.example.lab.utils.events.FriendshipEntityChangeEvent;
import com.example.lab.utils.events.UserEntityChangeEvent;
import com.example.lab.utils.observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class FriendController implements Observer<FriendshipEntityChangeEvent> {
    FriendshipService service;

    Integer currentUserId;
    @FXML
    public TableView<User> friendTable;
    @FXML
    TableColumn<User, String> fcolumn1;
    @FXML
    TableColumn<User, String> fcolumn2;


    ObservableList<User> model= FXCollections.observableArrayList();

    public void setId(Integer id){this.currentUserId=id;}

    public void setService(FriendshipService service, Integer id) {
        this.service = service;
        this.setId(id);
        initModel();
        service.addObserver(this);
    }
    @FXML
    public void initialize() {
        fcolumn1.setCellValueFactory(new PropertyValueFactory<User, String>("FirstName"));
        fcolumn2.setCellValueFactory(new PropertyValueFactory<User, String>("LastName"));
        friendTable.setItems(model);
    }
    private void initModel() {
        Iterable<User> messages = service.getUserFriends(this.currentUserId);
        List<User> users = StreamSupport.stream(messages.spliterator(), false)
                .collect(Collectors.toList());
        model.setAll(users);
    }

    public void handleDeleteFriend(ActionEvent actionEvent) {
        User user = (User) friendTable.getSelectionModel().getSelectedItem();
        if (user != null) {
            Friendship f = service.getById(new Tuple<>(user.getId(),this.currentUserId));
            Friendship deleted = service.deleteFriendship(f);
        }
        initModel();
    }
    public void handleAddFriend(ActionEvent actionEvent) throws IOException {
        friendTable.getSelectionModel().clearSelection();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/manageAddFriend.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("SHOW NONFRIENDS PAGE");
        AddFriendController fController = fxmlLoader.getController();
        fController.setService(service, this.currentUserId);
        stage.show();
        friendTable.getSelectionModel().clearSelection();
    }

    @Override
    public void update(FriendshipEntityChangeEvent friendshipEntityChangeEvent) {
        initModel();
    }
//    public void handleAdauga(ActionEvent actionEvent) {
//
//    }
}