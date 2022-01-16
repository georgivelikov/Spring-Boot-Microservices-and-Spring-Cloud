package com.appsdeveloperblog.photoapp.api.users.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.appsdeveloperblog.photoapp.api.users.shared.dto.UserDto;

public interface UserService extends UserDetailsService {

    public UserDto createUser(UserDto user) throws Exception;

    public UserDto getUser(String email) throws Exception;

    public UserDto getUserForLogin(String email) throws Exception;

    public UserDto getUserByUserId(String id) throws Exception;

    public UserDto updateUser(String id, UserDto user) throws Exception;

    public void deleteUser(String id) throws Exception;

    public List<UserDto> getUsers(int page, int limit) throws Exception;

    public UserDto updateUserJpql(String id, UserDto user) throws Exception;
}
