package com.example.lab.services;


import com.example.lab.domain.Friendship;
import com.example.lab.domain.Tuple;
import com.example.lab.domain.User;
import com.example.lab.repository.Repo;
import com.example.lab.utils.events.ChangeEventType;
import com.example.lab.utils.events.FriendshipEntityChangeEvent;
import com.example.lab.utils.events.UserEntityChangeEvent;
import com.example.lab.utils.observer.Observable;
import com.example.lab.utils.observer.Observer;

import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class UtilizatorService implements Observable<UserEntityChangeEvent> {
    private Repo<Integer, User> userRepo;
    private Repo<Tuple<Integer, Integer>, Friendship> friendsRepo;
    private List<Observer<UserEntityChangeEvent>> observers=new ArrayList<>();
    public UtilizatorService(Repo<Integer, User> userRepo, Repo<Tuple<Integer, Integer>, Friendship> friendsRepo) {
        this.userRepo = userRepo;
        this.friendsRepo = friendsRepo;
    }

    public User addUtilizator(User user) {
        if(userRepo.save(user).isEmpty()){
            UserEntityChangeEvent event = new UserEntityChangeEvent(ChangeEventType.ADD, user);
            notifyObservers(event);
            return null;
        }
        return user;
    }

    public User deleteUtilizator(Integer id){
        Optional<User> user=userRepo.delete(id);
        if (user.isPresent()) {
            notifyObservers(new UserEntityChangeEvent(ChangeEventType.DELETE, user.get()));
            return user.get();}
        return null;
    }

    public User updateUtilizator(User user){
        if(userRepo.findOne(user.getId()).isPresent()){
            userRepo.update(user);
            UserEntityChangeEvent event = new UserEntityChangeEvent(ChangeEventType.UPDATE, user);
            notifyObservers(event);
            return null;
        }
        return user;
    }

    public List<User> getUserFriends(Integer idUser)
    {
        userRepo.findOne(idUser);
        List<User> friends = new ArrayList<User>();;
        Iterable<Friendship> friendships = this.friendsRepo.findAll();
        for(Friendship f:friendships)
            if(f.getId().getRight()==idUser)
                friends.add(this.userRepo.findOne(f.getId().getLeft()).get());
            else if (f.getId().getLeft()==idUser)
                friends.add(this.userRepo.findOne(f.getId().getRight()).get());
        return friends;
    }

    public Iterable<User> getAllUsers(){
        return userRepo.findAll();
    }
    public  Iterable<Friendship> getAllFriendships(){return friendsRepo.findAll();}



    @Override
    public void addObserver(Observer<UserEntityChangeEvent> e) {
        observers.add(e);

    }

    @Override
    public void removeObserver(Observer<UserEntityChangeEvent> e) {
        //observers.remove(e);
    }

    @Override
    public void notifyObservers(UserEntityChangeEvent t) {

        observers.stream().forEach(x->x.update(t));
    }


}
