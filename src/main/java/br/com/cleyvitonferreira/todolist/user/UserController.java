package br.com.cleyvitonferreira.todolist.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private IUserRepository userRepository;
    
    @PostMapping("/")
    public ResponseEntity create(@RequestBody UserModel UserModel) {
        var findUserExists = this.userRepository.findByUsername(UserModel.getUsername());

        if(findUserExists != null){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists!");
        }

        var passHashed = BCrypt.withDefaults().hashToString(10, UserModel.getPassword().toCharArray());

        UserModel.setPassword(passHashed);

        var newUser = this.userRepository.save(UserModel);

        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }
}