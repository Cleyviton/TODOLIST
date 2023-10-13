package br.com.cleyvitonferreira.todolist.task;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity create(@RequestBody TaskModel TaskModel, HttpServletRequest request){
        var userId = request.getAttribute("userId");
        TaskModel.setUserId((UUID) userId);
        
        var currentDate = LocalDateTime.now();
        if(currentDate.isAfter(TaskModel.getStartAt())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data inicial deve ser maior ou igual a data atual.");
        }
        
        if(TaskModel.getEndAt().isBefore(TaskModel.getStartAt())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data final deve ser maior do que a data inicial.");
        }
        
        var newTask = this.taskRepository.save(TaskModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(newTask);
    }
}
