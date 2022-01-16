package com.appsdeveloperblog.photoapp.api.users.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.appsdeveloperblog.photoapp.api.users.io.entity.UserEntity;
import com.appsdeveloperblog.photoapp.api.users.io.repository.UserRepository;
import com.appsdeveloperblog.photoapp.api.users.security.UserPrincipal;
import com.appsdeveloperblog.photoapp.api.users.service.UserService;
import com.appsdeveloperblog.photoapp.api.users.shared.dto.UserDto;
import com.appsdeveloperblog.photoapp.api.users.shared.utils.Utils;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private Utils utils;

    @Autowired
    BCryptPasswordEncoder bCryptpasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    private ModelMapper modelMapper = new ModelMapper();

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
	UserEntity userEntity = userRepository.findByEmail(email);

	if (userEntity == null) {
	    throw new UsernameNotFoundException(email);
	}

	return new UserPrincipal(userEntity);
    }

    @Override
    public UserDto createUser(UserDto userDto) throws Exception {

	if (userRepository.findByEmail(userDto.getEmail()) != null) {
	    throw new Exception("Record already exists");
	}

	UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);

	userEntity.setUserId(utils.generatePublicId(30));
	userEntity.setEncryptedPassword(bCryptpasswordEncoder.encode(userDto.getPassword()));

	UserEntity storedUserEntity = null;
	storedUserEntity = userRepository.save(userEntity);

	UserDto returnValue = modelMapper.map(storedUserEntity, UserDto.class);

	return returnValue;
    }

    @Override
    public UserDto getUser(String email) throws Exception {
	UserEntity userEntity = userRepository.findByEmail(email);
	if (userEntity == null) {
	    throw new Exception("Record not found");
	}

	return modelMapper.map(userEntity, UserDto.class);
    }

    @Override
    public UserDto getUserForLogin(String email) throws Exception {
	UserEntity userEntity = userRepository.findByEmail(email);
	if (userEntity == null) {
	    throw new Exception("Record not found");
	}

	UserDto userDto = new UserDto();
	// Model Mapper is not allowed here, but BeanUtils is enough
	BeanUtils.copyProperties(userEntity, userDto);
	return userDto;
    }

    @Override
    public UserDto getUserByUserId(String id) throws Exception {
	UserEntity userEntity = userRepository.findByUserId(id);
	if (userEntity == null) {
	    throw new Exception("Record not found");
	}

	return modelMapper.map(userEntity, UserDto.class);
    }

    @Override
    public UserDto updateUser(String id, UserDto user) throws Exception {
	UserEntity userEntity = userRepository.findByUserId(id);
	if (userEntity == null) {
	    throw new Exception("Record not found");
	}

	userEntity.setFirstName(user.getFirstName());
	userEntity.setLastName(user.getLastName());
	UserEntity updatedUserEntity = userRepository.save(userEntity);

	return modelMapper.map(updatedUserEntity, UserDto.class);
    }

    @Override
    public void deleteUser(String id) throws Exception {
	UserEntity userEntity = userRepository.findByUserId(id);
	if (userEntity == null) {
	    throw new Exception("Record not found");
	}

	userRepository.delete(userEntity);
    }

    @Override
    public List<UserDto> getUsers(int page, int limit) throws Exception {
	List<UserDto> returnValue = new ArrayList<>();
	Pageable pageableRequest = PageRequest.of(page, limit);
	Page<UserEntity> userPage = userRepository.findAll(pageableRequest);
	List<UserEntity> users = userPage.getContent();

	for (UserEntity userEntity : users) {
	    UserDto userDto = modelMapper.map(userEntity, UserDto.class);
	    returnValue.add(userDto);
	}

	return returnValue;
    }

    @Override
    public UserDto updateUserJpql(String userId, UserDto userDto) throws Exception {
	String firstName = userDto.getFirstName();
	String lastName = userDto.getLastName();
	int result = userRepository.updateUserJpql(userId, firstName, lastName);

	// result == 0 means no rows are updated, therefore there is some problem
	if (result == 0) {
	    throw new Exception("Could not update record");
	}

	return modelMapper.map(userRepository.findByUserId(userId), UserDto.class);
    }

    /*
     * @Override public UserDto updateUserNative(String userId, UserDto userDto)
     * throws RestApiException { String firstName = userDto.getFirstName(); String
     * lastName = userDto.getLastName(); userRepository.updateUserNative(userId,
     * firstName, lastName);
     * 
     * return modelMapper.map(userRepository.findByUserId(userId), UserDto.class); }
     */
}
