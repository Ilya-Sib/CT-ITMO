package ru.itmo.wp.model.service;

import ru.itmo.wp.model.domain.Event;
import ru.itmo.wp.model.domain.EventType;
import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.repository.EventRepository;
import ru.itmo.wp.model.repository.impl.EventRepositoryImpl;

public class EventService {
    private final EventRepository eventRepository = new EventRepositoryImpl();

    public void save(Event event) {
        eventRepository.save(event);
    }

    public void saveEvent(long id, EventType type) {
        Event event = new Event();
        event.setType(type);
        event.setUserId(id);
        save(event);
    }
}
