package com.appsdeveloperblog.photoapp.api.users.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.appsdeveloperblog.photoapp.api.users.io.entity.UserEntity;

public class UserPrincipal implements UserDetails {

    private static final long serialVersionUID = 3819574297358073680L;

    private UserEntity userEntity;

    public UserPrincipal(UserEntity userEntity) {
	this.userEntity = userEntity;
    }

    @Override
    public String getPassword() {
	return userEntity.getEncryptedPassword();
    }

    @Override
    public String getUsername() {
	return userEntity.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
	return true;
    }

    @Override
    public boolean isAccountNonLocked() {
	return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
	return true;
    }

    @Override
    public boolean isEnabled() {
	return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
	// TODO Auto-generated method stub
	return null;
    }

}
