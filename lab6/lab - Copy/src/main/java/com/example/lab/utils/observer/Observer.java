package com.example.lab.utils.observer;


import com.example.lab.utils.events.Event;

public interface Observer<E extends Event> {
    void update(E e);
}