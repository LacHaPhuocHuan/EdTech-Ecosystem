package com.thanhha.edtechcosystem.userservice.security;

import org.springframework.security.core.GrantedAuthority;

public enum Permission {
    student_read("student:read"),
    student_write("student:write"),
    student_change("student:change"),
    //
    teacher_read("teach:read"),
    teacher_change("teacher:change"),
    teacher_write("teacher:write"),
    teacher_create("teacher:create"),
    //
    admin_read("admin:read"),
    admin_change("admin:change"),
    admin_write("admin:write"),
    admin_create("admin:create");

    //

    private String permission;

    Permission(String permission) {
        this.permission = permission;
    }

}
