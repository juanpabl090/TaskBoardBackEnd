package com.example.TaskBoardBackEnd.service;

import com.example.TaskBoardBackEnd.dto.UserRegisterDto;
import com.example.TaskBoardBackEnd.model.RoleEntity;
import com.example.TaskBoardBackEnd.model.User;
import com.example.TaskBoardBackEnd.repository.RoleRepository;
import com.example.TaskBoardBackEnd.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Set;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       RoleRepository roleRepository,
                       AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
    }

    public ResponseEntity<UserRegisterDto> registerUser(@Valid UserRegisterDto userRegisterDto) {
        if (userRepository.findUserByUserName(userRegisterDto.getName()).isPresent()
                && userRepository.findUserByEmail(userRegisterDto.getEmail()).isPresent()) {
            throw new Error("User already exists");
        }
        User user = User.builder()
                .name(userRegisterDto.getName())
                .userName(userRegisterDto.getUserName())
                .email(userRegisterDto.getEmail())
                .password(passwordEncoder.encode(userRegisterDto.getPassword()))
                .roles(Set.of(findRoleById()))
                .build();
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    public ResponseEntity<Authentication> loginUser(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Basic")) {
            String base64Credentials = authHeader.substring("Basic".length()).trim();
            String credentials = new String(Base64.getDecoder().decode(base64Credentials), StandardCharsets.UTF_8);
            final String[] values = credentials.split(":", 2);
            String username = values[0];
            String password = values[1];

            try {
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(username, password);
                Authentication authentication = authenticationManager.authenticate(authenticationToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                return ResponseEntity.ok(authentication);
            } catch (BadCredentialsException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    private RoleEntity findRoleById() {
        return roleRepository.findRoleNameById(10L);
    }

    public ResponseEntity<List<User>> getAllUsers() throws HttpClientErrorException.NotFound {
        List<User> userList = userRepository.findAll();
        return ResponseEntity.ok(userList);
    }
}