package com.appsdeveloperblog.photoapp.api.users.ui.controller;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.appsdeveloperblog.photoapp.api.users.service.UserService;
import com.appsdeveloperblog.photoapp.api.users.shared.dto.UserDto;
import com.appsdeveloperblog.photoapp.api.users.shared.utils.Utils;
import com.appsdeveloperblog.photoapp.api.users.ui.model.request.UserDetailsRequestModel;
import com.appsdeveloperblog.photoapp.api.users.ui.model.response.UserRest;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    // using ModelMapper from www.modelmapper.org
    private ModelMapper modelMapper = new ModelMapper();

    // 'consumes' and 'produces' may be needed for good practice
    @GetMapping(path = "/{userId}")
    public UserRest getUser(@PathVariable String userId) throws Exception {
	UserDto userDto = userService.getUserByUserId(userId);
	return modelMapper.map(userDto, UserRest.class);
    }

    @GetMapping(path = "/welcome")
    public String welcome() throws Exception {
	return "Welcome!";
    }

    @PreAuthorize("hasAuthority('DELETE_AUTHORITY')")
    @GetMapping(path = "/protected")
    public String protectedResource() throws Exception {
	return "Welcome to protected resourse!";
    }

    @PostMapping
    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {
	UserRest returnValue = new UserRest();

	// Added to test Exception handling
	if (Utils.isNullOrBlank(userDetails.getFirstName())) {
	    throw new Exception("Missing required field");
	}

	UserDto userDto = modelMapper.map(userDetails, UserDto.class);

	UserDto createdUser = userService.createUser(userDto);
	returnValue = modelMapper.map(createdUser, UserRest.class);

	return returnValue;
    }

    @PutMapping(path = "/{id}")
    public UserRest updateUser(@PathVariable String id, @RequestBody UserDetailsRequestModel userDetails)
	    throws Exception {
	UserDto userDto = modelMapper.map(userDetails, UserDto.class);

	UserDto updatedUser = userService.updateUser(id, userDto);

	return modelMapper.map(updatedUser, UserRest.class);
    }

    @PutMapping(path = "/{id}/jpql")
    public UserRest updateUserJpql(@PathVariable String id, @RequestBody UserDetailsRequestModel userDetails)
	    throws Exception {
	UserDto userDto = modelMapper.map(userDetails, UserDto.class);

	UserDto updatedUser = userService.updateUserJpql(id, userDto);

	return modelMapper.map(updatedUser, UserRest.class);
    }

    /*
     * @PutMapping(path = "/{id}/native") public UserRest
     * updateUserNative(@PathVariable String id, @RequestBody
     * UserDetailsRequestModel userDetails) throws RestApiException { UserDto
     * userDto = modelMapper.map(userDetails, UserDto.class);
     * 
     * UserDto updatedUser = userService.updateUserNative(id, userDto);
     * 
     * return modelMapper.map(updatedUser, UserRest.class); }
     */

    @DeleteMapping(path = "/{id}")
    public String deleteUser(@PathVariable String id) throws Exception {
	userService.deleteUser(id);

	return "DELETE USER SUCCESS";
    }

    @GetMapping()
    public List<UserRest> getUsers(@RequestParam(value = "page", defaultValue = "1") int page,
	    @RequestParam(value = "limit", defaultValue = "20") int limit) throws Exception {
	List<UserRest> returnValue = new ArrayList<>();

	// In the Repository implementation pagination starts with '0', but in UI
	// usually pages start from 1, 2, 3 etc. So UI will send the number of the page,
	// which should be reduced by 1
	if (page > 0) {
	    page -= 1;
	}

	List<UserDto> users = userService.getUsers(page, limit);

	for (UserDto userDto : users) {
	    UserRest userRest = modelMapper.map(userDto, UserRest.class);
	    returnValue.add(userRest);
	}

	return returnValue;
    }

    @GetMapping("/status/check")
    public String status() {
	return "USERS SERVICE WORKING!";
    }

}
