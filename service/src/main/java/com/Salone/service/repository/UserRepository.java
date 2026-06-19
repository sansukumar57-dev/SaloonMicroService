package com.Salone.service.repository;

import com.Salone.service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository  extends JpaRepository<User, Long>{


}
