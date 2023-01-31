package com.example.lab;
import com.example.lab.domain.Friendship;
import com.example.lab.domain.Tuple;
import com.example.lab.domain.User;
import com.example.lab.domain.validate.FriendshipValidator;
import com.example.lab.domain.validate.UserValidator;
import com.example.lab.repository.Repo;
import com.example.lab.repository.dbrepo.FriendDbRepo;
import com.example.lab.repository.dbrepo.UserDbRepo;
import com.example.lab.services.FriendshipService;

public class Main {
    public static void main(String[] args) {

//        DatabaseRepo<Integer, User> userRepository = new DatabaseRepo(userValidator);
//
//        FriendshipValidator friendshipValidator = new FriendshipValidator();
//        DatabaseRepo<Tuple<Integer,Integer>, Friendship> friendshipRepository = new DatabaseRepo(friendshipValidator);
//
//        Service service = new Service(userRepository, friendshipRepository);
//
//        Console ui = new Console(service);
//        ui.run();

        String username="postgres";
        String pasword="parola";
        String url="jdbc:postgresql://localhost:5432/Retea2";
        //String url="jdbc:postgresql://localhost:5434/dbex1";
        Repo<Integer, User> fFileRepository =
                new UserDbRepo(url,username, pasword,  new UserValidator());
        Repo<Tuple<Integer,Integer>, Friendship> fFileRepository3 =
                new FriendDbRepo(url,username, pasword,  new FriendshipValidator());
        FriendshipService fs = new FriendshipService(fFileRepository3,fFileRepository);

        //Friendship f = new Friendship(new Tuple<>(2,9));
        //fs.addCerere(f);

        //userFileRepository3.findAll().forEach(System.out::println);
        //Service service = new Service(userFileRepository3);
        //Console ui = new Console(service);
        //ui.run();

    }
}