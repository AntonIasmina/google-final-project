package com.example.lab;


import com.example.lab.controller.UserController;
import com.example.lab.domain.Friendship;
import com.example.lab.domain.Tuple;
import com.example.lab.domain.User;
import com.example.lab.domain.validate.FriendshipValidator;
import com.example.lab.domain.validate.UserValidator;
import com.example.lab.repository.Repo;
import com.example.lab.repository.dbrepo.FriendDbRepo;
import com.example.lab.repository.dbrepo.UserDbRepo;
import com.example.lab.services.FriendshipService;
import com.example.lab.services.UtilizatorService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    Repo<Integer, User> utilizatorRepository;
    Repo<Tuple<Integer,Integer>, Friendship> friendsRepo;
    UtilizatorService service;
    FriendshipService fservice;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
//        String fileN = ApplicationContext.getPROPERTIES().getProperty("data.tasks.messageTask");
//        messageTaskRepository = new InFileMessageTaskRepository
//                (fileN, new MessageTaskValidator());
//        messageTaskService = new MessageTaskService(messageTaskRepository);
        //messageTaskService.getAll().forEach(System.out::println);

        System.out.println("Reading data from file");
        String username="postgres";
        String pasword="parola";
        String url="jdbc:postgresql://localhost:5432/Retea2";
        Repo<Integer, User> utilizatorRepository =
                new UserDbRepo(url,username, pasword,  new UserValidator());
        Repo<Tuple<Integer,Integer>, Friendship> friendRepository =
                new FriendDbRepo(url,username, pasword,  new FriendshipValidator());

        utilizatorRepository.findAll().forEach(x-> System.out.println(x));
        this.service =new UtilizatorService(utilizatorRepository, friendRepository);
        this.fservice = new FriendshipService(friendRepository, utilizatorRepository);
        initView(primaryStage);
        primaryStage.setWidth(800);
        primaryStage.show();

    }

    private void initView(Stage primaryStage) throws IOException {

        // FXMLLoader fxmlLoader = new FXMLLoader();
        //fxmlLoader.setLocation(getClass().getResource("com/example/guiex1/views/UtilizatorView.fxml"));
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/UtilizatorView.fxml"));

        AnchorPane userLayout = fxmlLoader.load();
        primaryStage.setScene(new Scene(userLayout));

        UserController userController = fxmlLoader.getController();
        userController.setUserService(service, fservice);

    }

}