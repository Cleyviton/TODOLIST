package br.com.cleyvitonferreira.todolist.task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.cleyvitonferreira.todolist.utils.Utils;
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

    @GetMapping("/")
    public List<TaskModel> list(HttpServletRequest request){
        var userId = request.getAttribute("userId");
        var tasks = this.taskRepository.findByUserId((UUID) userId);

        return tasks;
    }

    @PutMapping("/{taskId}")
    public ResponseEntity update(@RequestBody TaskModel taskModel, @PathVariable UUID taskId, HttpServletRequest request){
        var userId = request.getAttribute("userId");
        var task = this.taskRepository.findById(taskId).orElse(null);

        if(task == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task não encontrada!");
        } else if(!task.getUserId().equals(userId)){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("O usuário não tem permissão para alterar essa task!");
        }

        Utils.copyNonNullProperties(taskModel, task);

        var newTask = this.taskRepository.save(task);

        return ResponseEntity.ok().body(newTask);
    }
}
