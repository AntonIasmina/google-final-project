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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class FriendshipService implements Observable<FriendshipEntityChangeEvent> {
    private Repo<Tuple<Integer, Integer>, Friendship> friendsRepo;
    private Repo<Integer, User> userRepo;
    private List<Observer<FriendshipEntityChangeEvent>> observers=new ArrayList<>();
    public FriendshipService(Repo<Tuple<Integer, Integer>, Friendship> friendsRepo, Repo<Integer, User> userRepo) {
        this.friendsRepo = friendsRepo;
        this.userRepo = userRepo;
    }

    public Friendship addCerere(Friendship friendship)
    {
        if(friendsRepo.save(friendship).isEmpty()){
            FriendshipEntityChangeEvent event = new FriendshipEntityChangeEvent(ChangeEventType.ADD, friendship);
            notifyObservers(event);
            return null;
        }
        return friendship;
    }

    public void acceptCerere(Tuple<Integer,Integer> id)
    {
        Friendship f = friendsRepo.findOne(id).get();
        friendsRepo.update(new Friendship(f.getId(),f.getDate(),false));
        notifyObservers(new FriendshipEntityChangeEvent(ChangeEventType.UPDATE, friendsRepo.findOne(id).get()));
    }


    public Friendship deleteFriendship(Friendship friendship)
    {
        Optional<Friendship> friendship1=friendsRepo.delete(friendship.getId());
        if (friendship1.isPresent() && !friendship1.get().getPending()) {
            notifyObservers(new FriendshipEntityChangeEvent(ChangeEventType.DELETE, friendship1.get()));
            return friendship1.get();}
        return null;
    }

    public Friendship getById(Tuple<Integer,Integer> id)
    {
        return friendsRepo.findOne(id).get();
    }

    public void deleteFriendsOfUser(Integer idUser)
    {
        Iterable<Friendship> friendships = this.friendsRepo.findAll();
        for(Friendship f:friendships)
            if((f.getId().getRight()==idUser)|| (f.getId().getLeft()==idUser))
            {
                this.friendsRepo.delete(f.getId());
            }
    }

    public List<User> getUserFriends(Integer idUser)
    {
        //userRepo.findOne(idUser);
        List<User> friends = new ArrayList<User>();;
        Iterable<Friendship> friendships = this.friendsRepo.findAll();
        for(Friendship f:friendships)
            if(f.getId().getRight()==idUser&& !f.getPending())
                friends.add(this.userRepo.findOne(f.getId().getLeft()).get());
            else if (f.getId().getLeft()==idUser&&!f.getPending())
                friends.add(this.userRepo.findOne(f.getId().getRight()).get());
        return friends;
    }

    public List<User> getUserNonFriends(Integer idUser)
    {
        userRepo.findOne(idUser);
        List<User> nonfriends = new ArrayList<>();
        List<User> friends = getUserFriends(idUser);
        List<User> requests = getUserRequests(idUser);
        Iterable<Friendship> friendships = this.friendsRepo.findAll();
        for(User u:userRepo.findAll())
            if(!friends.contains(u)&& !Objects.equals(u.getId(), idUser)&&!requests.contains(u))
                nonfriends.add(u);
        return nonfriends;
    }

    public List<User> getUserRequests(Integer idUser)
    {
        //userRepo.findOne(idUser);
        List<User> requests = new ArrayList<User>();
        Iterable<Friendship> friendships = this.friendsRepo.findAll();
        for(Friendship f:friendships)
            if(f.getId().getRight()==idUser&& f.getPending())
                requests.add(this.userRepo.findOne(f.getId().getLeft()).get());
            else if (f.getId().getLeft()==idUser&&f.getPending())
                requests.add(this.userRepo.findOne(f.getId().getRight()).get());
        return requests;
    }

    public List<String> getRequestsDate(Integer idUser)
    {
        //userRepo.findOne(idUser);
        List<String> dates = new ArrayList<String>();;
        Iterable<Friendship> friendships = this.friendsRepo.findAll();
        for(Friendship f:friendships)
            if(f.getId().getRight()==idUser&& f.getPending())
                dates.add(f.getDate().toString());
            else if (f.getId().getLeft()==idUser&&f.getPending())
                dates.add(f.getDate().toString());
        return dates;
    }



    public  Iterable<Friendship> getAllFriendships(){return friendsRepo.findAll();}



    @Override
    public void addObserver(Observer<FriendshipEntityChangeEvent> e) {
        observers.add(e);

    }

    @Override
    public void removeObserver(Observer<FriendshipEntityChangeEvent> e) {
        //observers.remove(e);
    }

    @Override
    public void notifyObservers(FriendshipEntityChangeEvent t) {

        observers.stream().forEach(x->x.update(t));
    }

}