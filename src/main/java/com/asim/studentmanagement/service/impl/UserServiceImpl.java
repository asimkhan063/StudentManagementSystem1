package com.asim.studentmanagement.service.impl;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.asim.studentmanagement.model.Users;
import com.asim.studentmanagement.repository.UsersRepository;

@Service
public class UserServiceImpl implements UserDetailsService {

    private final UsersRepository usersRepository;

    public UserServiceImpl(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException 
    {

        Users user = usersRepository.findByUsername(username) 
        .orElseThrow(() -> new UsernameNotFoundException("Invalid username:"));
        

        return User.withUsername(username)
                .password(user.getPassword())
                .disabled(!user.isActive())
                .build();
    }
}