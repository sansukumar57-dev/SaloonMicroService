package com.Salone.service.service;

import java.util.List;

import com.Salone.service.exception.UserException;
import com.Salone.service.model.User;
public interface UserService {
        User createUser(User user);
        User getUserById(Long id) throws UserException;
        List<User> getAllUsers();
        void deleteUser(Long id) throws UserException;
        User updateUser(Long id,User user) throws UserException;
        User getUserFromJwt(String jwt) throws Exception;
}
