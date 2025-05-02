package services;

import Repositories.EventRepository;
import entities.Event;

import java.net.http.HttpClient;
import java.util.List;

public class ServiceEvent implements Ievent{

    private final EventRepository eventRepository;

    public ServiceEvent() {
        this.eventRepository = new EventRepository();
    }

    @Override
    public void create(Event e) {
        eventRepository.createEvent(e);
    }

    @Override
    public void delete(Event e) {
      eventRepository.deleteEvent(e.getIdEvent());

    }

    @Override
    public void update(Event e, int id) {
        eventRepository.updateEvent(e ,id);
    }

    @Override
    public List<Event> list_Event() {
        List<Event> events = eventRepository.getAllEvents();
    return events;
    }

    @Override
    public List<Event> listDeletedEvents() {
        return eventRepository.getDeletedEvents();
    }

    @Override
    public Event get_Event(int id) {
        return eventRepository.getEventById(id);
    }

}


