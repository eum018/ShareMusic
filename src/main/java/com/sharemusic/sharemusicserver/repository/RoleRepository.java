package com.sharemusic.sharemusicserver.repository;

import com.sharemusic.sharemusicserver.models.entities.RoleEntity;
import com.sharemusic.sharemusicserver.models.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository  extends JpaRepository<RoleEntity, Long> {

    RoleEntity findByUser(UserEntity user);

}

