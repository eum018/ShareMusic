package com.sharemusic.sharemusicserver.repository;


import com.sharemusic.sharemusicserver.models.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByEmail(String email);
    UserEntity findByRefreshToken(String refreshToken);
    boolean existsByEmail(String email);
}