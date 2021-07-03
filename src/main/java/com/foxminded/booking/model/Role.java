package com.foxminded.booking.model;

import com.foxminded.booking.model.dto.RoleDto;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @NotEmpty
    @Column(name = "name")
    private String name;
    @OneToMany(mappedBy = "role")
    private List<User> users = new ArrayList<>();

    public Role() {
    }

    public Role(@NotEmpty String name) {
        this.name = name;
    }

    public Role(Long id, @NotEmpty String name) {
        this.id = id;
        this.name = name;
    }

    public RoleDto toRoleDto() {
        RoleDto dto = new RoleDto();
        dto.setId(id);
        dto.setName(name);
        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return name.equals(role.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
