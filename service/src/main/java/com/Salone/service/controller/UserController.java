package com.Salone.service.controller;

import com.Salone.service.exception.UserException;
import com.Salone.service.model.User;
import com.Salone.service.repository.UserRepository;
import com.Salone.service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController// it handles the web request// s
@RequiredArgsConstructor
public class UserController
{
   // @Autowired// it will help to connect with database
   // private UserRepository userRepository;
   private final UserService userService;

    @PostMapping("/users")// it will handle the post request
    public ResponseEntity<User> createUser(@RequestBody @Valid User user){
        User createdUser = userService.createUser(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
       //  return userRepository.save(user);// it will return created user
    }
    @GetMapping("/users/profile")// it will handle the post request
    public ResponseEntity<User> getUserProfile(@RequestHeader("Authorization") String jwt) throws Exception {
        User createdUser = userService.getUserFromJwt(jwt);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        //  return userRepository.save(user);// it will return created user
    }



    @GetMapping("/users")// it will handle the get request
    public ResponseEntity<List<User>> getUser() {
        List<User>users=userService.getAllUsers();
        return new ResponseEntity<>(users,HttpStatus.OK);

    }
    @GetMapping("/users/{userId}")
   public  ResponseEntity<User>getUserById(@PathVariable("userId") Long id)throws Exception{
        User user=userService.getUserById(id);
        return new ResponseEntity<>(user,HttpStatus.OK);

   }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(
            @RequestBody User user,
            @PathVariable Long id) throws Exception{
        User updatedUser=userService.updateUser(id,user);
        return new ResponseEntity<>(updatedUser,HttpStatus.OK);

    }
    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable Long id) throws Exception{
    userService.deleteUser(id);
    return new ResponseEntity<>("User deleted ",HttpStatus.ACCEPTED);
    }





}
