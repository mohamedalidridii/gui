package services;

import entities.Event;

import java.util.List;

public interface Ievent {
    void create(Event e);
    void delete(Event e);
    void update(Event e,int id);
    List<Event> list_Event();
    List<Event> listDeletedEvents();
    Event get_Event(int id);


}
