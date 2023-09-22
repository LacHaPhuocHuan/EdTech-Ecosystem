package com.thanhha.edtechcosystem.userservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.thanhha.edtechcosystem.userservice.dto.AdditionalUserRequest;
import com.thanhha.edtechcosystem.userservice.dto.UpdateUserRequest;
import com.thanhha.edtechcosystem.userservice.dto.UserDto;
import com.thanhha.edtechcosystem.userservice.dto.UserPage;

import java.util.List;

public interface UserService {



    UserPage getAllUser(int page, int size) ;

    UserDto getUserById(String id);

    UserDto addUser(AdditionalUserRequest additionalUser) ;

    UserDto updateUser(UpdateUserRequest updateUser, String id);

    UserDto deactivateAccount(String idUser);

    UserDto deactivateMyAccount(String id, String password);

    void resetPassword(String oldPassword, String newPassword, String confirmPassword, String id);
}
