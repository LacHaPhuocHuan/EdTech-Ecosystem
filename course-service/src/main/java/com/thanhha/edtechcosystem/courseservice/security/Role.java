package com.thanhha.edtechcosystem.courseservice.security;

import com.google.common.collect.Sets;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;


public enum Role {
    STUDENT(Sets.newHashSet(
            Permission.student_change,
            Permission.student_read,
            Permission.student_write)),
    TEACHER(Sets.newHashSet(
            Permission.teacher_change,
            Permission.teacher_create,
            Permission.teacher_write,
            Permission.teacher_change
    )),
    ADMIN(Sets.newHashSet(
            Permission.student_change,
            Permission.student_read,
            Permission.student_write,
            Permission.teacher_change,
            Permission.teacher_create,
            Permission.teacher_write,
            Permission.teacher_read,
            Permission.admin_change,
            Permission.admin_create,
            Permission.admin_read,
            Permission.admin_write
    ));

    private Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions=permissions;
    }

    public Set<? extends GrantedAuthority> getAuthority(){
        Set<SimpleGrantedAuthority> grantedAuthorities=
                this.permissions.stream()
                        .map(permission -> new SimpleGrantedAuthority(permission.name()))
                        .collect(Collectors.toSet());
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_"+this.name()));
        return grantedAuthorities;
    }


}
