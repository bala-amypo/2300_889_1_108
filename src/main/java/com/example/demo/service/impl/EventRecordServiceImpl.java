package com.example.demo.service.impl;

import com.example.demo.model.EventRecord;
import com.example.demo.repository.EventRecordRepository;
import com.example.demo.service.EventRecordService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventRecordServiceImpl implements EventRecordService {

    private final EventRecordRepository eventRecordRepository;

    public EventRecordServiceImpl(EventRecordRepository eventRecordRepository) {
        this.eventRecordRepository = eventRecordRepository;
    }

    @Override
    public EventRecord createEvent(EventRecord event) {

        if (eventRecordRepository.existsByEventCode(event.getEventCode())) {
            throw new RuntimeException("Duplicate event code");
        }

        if (event.getBasePrice() <= 0) {
            throw new RuntimeException("Base price must be greater than zero");
        }

        return eventRecordRepository.save(event);
    }

    @Override
    public EventRecord getEventById(Long id) {
        return eventRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));
    }

    @Override
    public Optional<EventRecord> getEventByCode(String eventCode) {
        return eventRecordRepository.findByEventCode(eventCode);
    }

    @Override
    public List<EventRecord> getAllEvents() {
        return eventRecordRepository.findAll();
    }

    @Override
    public EventRecord updateEventStatus(Long id, boolean active) {
        EventRecord event = getEventById(id);
        event.setActive(active);
        return eventRecordRepository.save(event);
    }
}
