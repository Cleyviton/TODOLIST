package br.com.cleyvitonferreira.todolist.task;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/tasks")
public class TaskControler {
    
    @Autowired
    private ITaskRepository taskRepository;

    @PostMapping("/")
    public TaskModel create(@RequestBody TaskModel TaskModel, HttpServletRequest request){
        var newTask = this.taskRepository.save(TaskModel);
        var userId = request.getAttribute("userId");
        TaskModel.setUserId((UUID) userId);
        return newTask;
    }
}
