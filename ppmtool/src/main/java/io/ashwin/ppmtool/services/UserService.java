package io.ashwin.ppmtool.services;

import io.ashwin.ppmtool.domain.User;
import io.ashwin.ppmtool.exceptions.UsernameAlreadyExistsException;
import io.ashwin.ppmtool.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User saveUser(User user)
    {
        try
        {
            user.setUsername(user.getUsername());
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            return userRepository.save(user);
        }catch (Exception e)
        {
            throw new UsernameAlreadyExistsException("Username already exists");
        }

    }
}
