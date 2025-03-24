package com.example.todoapi.controller;

import com.example.todoapi.model.Task;
import com.example.todoapi.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")

public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @GetMapping
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @GetMapping("/id")
    public ResponseEntity<Task> getTaskById(@RequestParam Long id) {
        Optional<Task> task = taskRepository.findById(id);

        return task.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    ResponseEntity<Task> createTask(@RequestBody Task task) {
        Task savedTask = taskRepository.save(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTask);
    }

    @PutMapping("/{id}")
    ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task TaskDetail) {
        Optional<Task> task = taskRepository.findById(id);

        if (task.isPresent()) {
            Task existingTask = task.get();
            existingTask.setTitle(TaskDetail.getTitle());
            existingTask.setDescription(TaskDetail.getDescription());
            existingTask.setCompleted(TaskDetail.isCompleted());

            Task updatedTask = taskRepository.save(existingTask);
            return ResponseEntity.ok(updatedTask);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        Optional<Task> task = taskRepository.findById(id);

        if (task.isPresent()) {
            taskRepository.delete(task.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
