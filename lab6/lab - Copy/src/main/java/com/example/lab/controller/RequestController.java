package com.example.lab.controller;

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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class RequestController implements Observer<FriendshipEntityChangeEvent> {
    FriendshipService service;

    Integer currentUserId;
    @FXML
    public TableView<User> requestTable;
    @FXML
    TableColumn<User, String> rcolumn1;
    @FXML
    TableColumn<User, String> rcolumn2;
    @FXML
    TableColumn<User, String> rcolumn3;


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
        rcolumn1.setCellValueFactory(new PropertyValueFactory<User, String>("FirstName"));
        rcolumn2.setCellValueFactory(new PropertyValueFactory<User, String>("LastName"));
        //rcolumn3.setCellValueFactory(new PropertyValueFactory<User, String>("Date"));
        requestTable.setItems(model);
    }
    private void initModel() {
        Iterable<User> messages = service.getUserRequests(currentUserId);
        //Iterable<String> mess = service.getRequestsDate(currentUserId);
        List<User> request = StreamSupport.stream(messages.spliterator(), false).collect(Collectors.toList());
        model.setAll(request);
//        List<User> dates = StreamSupport.stream(messages.spliterator(), false).toList();
//        model.setAll(dates);
    }

    @Override
    public void update(FriendshipEntityChangeEvent friendshipEntityChangeEvent) {
        initModel();
    }
    public void handleAccept(ActionEvent actionEvent) {
        User user = (User) requestTable.getSelectionModel().getSelectedItem();

        Friendship f = service.getById(new Tuple<>(this.currentUserId, user.getId()));
        service.acceptCerere(f.getId());
        initModel();
    }
}