package org.example.demo.Controllers;

import org.example.demo.Models.Task;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api/tasks")

public class TaskController {
    private List<Task> tasks = new ArrayList<>();
    private int idCounter = 1;


    //Skapa konstruktor som lägger till uppgifter
    public TaskController() {
        tasks.add(new Task(idCounter++, "Complete Java assignment"));
        tasks.add(new Task(idCounter++, "Read Spring documentation"));
    }

    //Hämta alla uppgifter med GET
    @GetMapping
    public List<Task> getTasks() {
        return tasks;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable int id) {
        return tasks.stream()
                .filter(task -> task.getId() == id)//filtrerar böckerna från Arraylist
                .findFirst()//retunera första boken
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    //Skapa ny uppgift med POST
    @PostMapping
    public ResponseEntity<Object> addTask(@RequestBody Task task) {
        if (task != null && task.getDescription() != null && !task.getDescription().isEmpty()){
            task.setId(idCounter++);
            tasks.add(task);
            return ResponseEntity.status(HttpStatus.CREATED).body(task);
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    //Uppdatera en befintlig uppgift med PUT
    @PutMapping("/{id}")
    public ResponseEntity<Task>updateTask(@PathVariable int id, @RequestBody Task updateTask){
        for (Task task : tasks){
            if (task.getId() == id){
                task.setDescription(updateTask.getDescription());
                return ResponseEntity.ok(task);
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    //Ta bort en uppgift med DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity <String> deleteTask(@PathVariable int id){
        boolean removed = tasks.removeIf(task -> task.getId() == id);
        if (removed){
            return ResponseEntity.ok("Task with ID " + id + " has been deleted");
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task with ID" + id + " not found");
        }

    }
}



