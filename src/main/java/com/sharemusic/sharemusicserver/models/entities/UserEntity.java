package com.sharemusic.sharemusicserver.models.entities;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;

@Entity
@Getter
@NoArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;

    @Column(unique = true,  nullable = false)
    private String email;
    private String name;
    private String password;
    private String refreshToken;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    Collection<RoleEntity> user_roles = new ArrayList<>();

    @Builder
    UserEntity(String name, String password, String email) {
        this.name = name;
        this.password = password;
        this.email = email;
    }

    public void addRole(RoleEntity role) {
        user_roles.add(role);
        role.user = this;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }



}