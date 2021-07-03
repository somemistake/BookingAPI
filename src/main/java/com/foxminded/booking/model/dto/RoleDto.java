package com.foxminded.booking.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Objects;

@Schema(description = "Role Entity")
public class RoleDto {
    @Schema(description = "Identifier")
    private Long id;
    @Schema(description = "Role Name")
    private String name;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoleDto dto = (RoleDto) o;
        return name.equals(dto.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
