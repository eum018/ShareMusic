package com.sharemusic.sharemusicserver.models.entities;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Role_id;


    @ManyToOne
    @JoinColumn(name = "user_id")
    UserEntity user;

    @Enumerated(EnumType.STRING)
    Role role;

    @Builder
    RoleEntity(UserEntity user, Role role) {
        this.user = user;
        this.role = role;
    }


}

