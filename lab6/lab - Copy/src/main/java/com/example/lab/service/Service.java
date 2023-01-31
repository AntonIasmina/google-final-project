package com.example.lab.service;

import com.example.lab.domain.User;
import com.example.lab.repository.Repo;
import com.example.lab.utils.events.ChangeEventType;
import com.example.lab.utils.events.UserEntityChangeEvent;
import com.example.lab.utils.observer.Observable;
import com.example.lab.utils.observer.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class  Service implements Observable<UserEntityChangeEvent> {
    private Repo<Integer, User> repo;
    private List<Observer<UserEntityChangeEvent>> observers=new ArrayList<>();

    public Service(Repo<Integer, User> repo) {
        this.repo = repo;
    }


    public User addUser(User user) {
        if(repo.save(user).isEmpty()){
            UserEntityChangeEvent event = new UserEntityChangeEvent(ChangeEventType.ADD, user);
            notifyObservers(event);
            return null;
        }
        return user;
    }

    public User deleteUser(Integer id){
        Optional<User> user=repo.delete(id);
        if (user.isPresent()) {
            notifyObservers(new UserEntityChangeEvent(ChangeEventType.DELETE, user.get()));
            return user.get();}
        return null;
    }

    public Iterable<User> getAll(){
        return repo.findAll();
    }



    @Override
    public void addObserver(Observer<UserEntityChangeEvent> e) {
        observers.add(e);

    }

    @Override
    public void removeObserver(Observer<UserEntityChangeEvent> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(UserEntityChangeEvent t) {

        observers.stream().forEach(x->x.update(t));
    }


}
