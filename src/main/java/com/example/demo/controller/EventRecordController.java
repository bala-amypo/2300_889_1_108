package com.example.demo.controller;

import com.example.demo.model.EventRecord;
import com.example.demo.service.EventRecordService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventRecordController {

 private final EventRecordService eventService;

 public EventRecordController(EventRecordService eventService) {
  this.eventService = eventService;
 }

 @PostMapping
 public EventRecord create(@RequestBody EventRecord event) {
  return eventService.createEvent(event);
 }

 @GetMapping("/{id}")
 public EventRecord getById(@PathVariable Long id) {
  return eventService.getEventById(id);
 }

 @GetMapping
 public List<EventRecord> getAll() {
  return eventService.getAllEvents();
 }

 @PutMapping("/{id}/status")
 public EventRecord updateStatus(@PathVariable Long id, @RequestParam boolean active) {
  return eventService.updateEventStatus(id, active);
 }

 @GetMapping("/lookup/{eventCode}")
 public EventRecord getByCode(@PathVariable String eventCode) {
  return eventService.getEventByCode(eventCode).orElseThrow();
 }
}
