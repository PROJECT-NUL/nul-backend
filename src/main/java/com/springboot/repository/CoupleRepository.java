package com.springboot.repository;

import com.springboot.model.Couple;
import com.springboot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CoupleRepository extends JpaRepository<Couple, Long> {
    Optional<Couple> findByPartner1OrPartner2(User partner1, User partner2);
}
