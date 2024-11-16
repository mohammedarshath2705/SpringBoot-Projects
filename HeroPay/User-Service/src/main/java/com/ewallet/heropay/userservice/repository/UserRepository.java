package com.ewallet.heropay.userservice.repository;

import com.ewallet.heropay.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    User findByPhone(String phone);
}
