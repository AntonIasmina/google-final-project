package com.example.lab.controller;

import com.example.lab.HelloApplication;
import com.example.lab.domain.Friendship;
import com.example.lab.domain.Tuple;
import com.example.lab.domain.User;
import com.example.lab.services.FriendshipService;
import com.example.lab.utils.events.FriendshipEntityChangeEvent;
import com.example.lab.utils.observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class AddFriendController implements Observer<FriendshipEntityChangeEvent> {
    FriendshipService service;

    Integer currentUserId;
    @FXML
    public TableView<User> addTable;
    @FXML
    TableColumn<User, String> acolumn1;
    @FXML
    TableColumn<User, String> acolumn2;


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
        acolumn1.setCellValueFactory(new PropertyValueFactory<User, String>("FirstName"));
        acolumn2.setCellValueFactory(new PropertyValueFactory<User, String>("LastName"));
        addTable.setItems(model);
    }
    private void initModel() {
        Iterable<User> messages = service.getUserNonFriends(this.currentUserId);
        List<User> users = StreamSupport.stream(messages.spliterator(), false)
                .collect(Collectors.toList());
        model.setAll(users);
    }

    public void handleAdding(ActionEvent actionEvent)
    {
        User user = (User) addTable.getSelectionModel().getSelectedItem();
        if (user != null) {
            Friendship f = new Friendship(new Tuple<>(this.currentUserId,user.getId()));
            service.addCerere(f);
        }
        initModel();
    }

    @Override
    public void update(FriendshipEntityChangeEvent friendshipEntityChangeEvent) {
        initModel();
    }
//    public void handleAdauga(ActionEvent actionEvent) {
//
//    }
}