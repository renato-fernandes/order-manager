package org.aubay.challenge.repository;

import org.aubay.challenge.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsersRepository extends JpaRepository<Users, Long> {

    List<Users> findByEmail(String email);
}
