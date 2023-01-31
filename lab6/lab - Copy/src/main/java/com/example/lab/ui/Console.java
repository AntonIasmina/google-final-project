package com.example.lab.ui;

import com.example.lab.service.Service;

import java.util.Scanner;

public class Console{
    private Service service;
    private Scanner in;

    public Console(Service service) {
        this.service = service;
        this.in = new Scanner(System.in);
    }

    public void showMenu(){
        System.out.println("1. Adauga un utilizator.");
        System.out.println("2. Sterge un utilizator.");
        System.out.println("3. Adauga un prieten.");
        System.out.println("4. Sterge un prieten.");
        System.out.println("5. Afisare prieteni ai unui utilizator.");
        System.out.println("6. Afisare utilizatori.");
        System.out.println("x. Iesire.");
        System.out.println("Optiune: ");
    }

    /*public void run(){
        String option;
        boolean running=true;

        while(running)
        {
            showMenu();
            option = in.nextLine();
            if(Objects.equals(option, "1"))
                uiAddUser();
            else if(Objects.equals(option, "2"))
                uiDeleteUser();
            else if(Objects.equals(option, "3"))
                uiAddFriend();
            else if(Objects.equals(option, "4"))
                uiRemoveFriend();
            else if(Objects.equals(option, "5"))
                uiShowFriends();
            else if(Objects.equals(option, "6"))
                uiShowAllUsers();
            else if(Objects.equals(option, "x"))
            {   System.out.println("Optiune gresita!");
                break;
            }
            else System.out.println("Optiune gresita!");

        }
        while(running){
            showMenu();
            option = in.nextLine();
            switch(option){
                case "1": uiAddUser(); break;
                case "2": uiDeleteUser(); break;
                case "3": uiAddFriend(); break;
                case "4": uiRemoveFriend(); break;
                case "5": uiShowFriends(); break;
                case "6": uiShowAllUsers(); break;
                case "x": running = false; break;
                default:
                    System.out.println("Optiune gresita!");
            }
        }}

    private void uiShowAllUsers() {
        Iterable<User> users = this.service.getAllUsers();
        for(User u: users)
            System.out.println(u);
    }

    private void uiAddUser() {
        try {
            String firstName, lastName, username;
            System.out.println("Nume: ");
            lastName = in.nextLine();
            System.out.println("Prenume: ");
            firstName = in.nextLine();
            System.out.println("Username: ");
            username = in.nextLine();
            this.service.addUser(firstName, lastName, username);
        }
        catch (ValidationException | IllegalArgumentException e){
            System.out.println(e);
        }
    }

    private void uiDeleteUser() {
        try {
            int id;
            System.out.println("ID: ");
            id = in.nextInt();
            this.service.deleteUser(id);
        }
        catch (IllegalArgumentException e){
            System.out.println(e);
        }
    }

        private void uiShowFriends() {
        try{
        int id;
        System.out.println("ID: ");
        id = in.nextInt();
        List<User> friends = this.service.getUserFriends(id);
            for(User u: friends)
                System.out.println(u);
        }
        catch (ValidationException | IllegalArgumentException e){
            System.out.println(e);
        }
    }

    private void uiAddFriend() {
        try {
            int firstId, secondId;
            System.out.println("Primul ID: ");
            firstId = in.nextInt();
            System.out.println("Al doilea ID: ");
            secondId = in.nextInt();
            this.service.addFriendship(firstId, secondId);
        }
        catch (IllegalArgumentException e){
            System.out.println(e);}
    }

    private void uiRemoveFriend() {
        try {
        int firstId, secondId;
        System.out.println("Primul ID: ");
        firstId = in.nextInt();
        System.out.println("Al doilea ID: ");
        secondId = in.nextInt();
        this.service.deleteFriendship(firstId,secondId);
        }
        catch (IllegalArgumentException e){
            System.out.println(e);}
    }*/

}
