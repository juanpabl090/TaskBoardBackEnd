package com.example.TaskBoardBackEnd.service;

import com.example.TaskBoardBackEnd.dto.AuthRequestDto;
import com.example.TaskBoardBackEnd.dto.AuthResponseDto;
import com.example.TaskBoardBackEnd.dto.UserRegisterDto;
import com.example.TaskBoardBackEnd.model.RoleEntity;
import com.example.TaskBoardBackEnd.model.User;
import com.example.TaskBoardBackEnd.repository.RoleRepository;
import com.example.TaskBoardBackEnd.repository.UserRepository;
import com.example.TaskBoardBackEnd.util.JwtUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final RoleRepository roleRepository;

    @Autowired
    public UserDetailServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            RoleRepository roleRepository,
            JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("user not found"));
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();

        user.getRoles()
                .forEach(role -> authorityList.add(new SimpleGrantedAuthority("ROLE_".concat(role.getRole().name()))));
        user.getRoles().stream()
                .flatMap(role -> role.getPermissionList().stream())
                .forEach(permissionEntity -> authorityList.add(new SimpleGrantedAuthority(permissionEntity.getName())));

        return new org.springframework.security.core.userdetails.User(
                user.getUserName(),
                user.getPassword(),
                authorityList
        );
    }

    public ResponseEntity<AuthResponseDto> registerUser(@Valid @RequestBody UserRegisterDto userRegisterDto) {
        if (userRepository.findUserByUserName(userRegisterDto.userName()).isPresent()
                || userRepository.findUserByEmail(userRegisterDto.email()).isPresent()) {
            throw new Error("User already exists");
        }
        User user = User.builder()
                .name(userRegisterDto.name())
                .userName(userRegisterDto.userName())
                .email(userRegisterDto.email())
                .password(passwordEncoder.encode(userRegisterDto.password()))
                .roles(Set.of(findRoleById()))
                .isEnabled(true)
                .accountNoExpired(true)
                .accountNoLocked(true)
                .credentialNoExpired(true)
                .build();

        User userSaved = userRepository.save(user);
        ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();

        userSaved.getRoles().stream().flatMap(role -> role.getPermissionList().stream())
                .forEach(permission -> authorities.add(new SimpleGrantedAuthority(permission.getName())));
        userSaved.getRoles().forEach(role -> authorities.add(
                new SimpleGrantedAuthority("ROLE_".concat(
                        role.getRole().name()))));

        Authentication authentication = new UsernamePasswordAuthenticationToken(userSaved, null, authorities);

        AuthResponseDto authResponseDto = new AuthResponseDto(authentication.getPrincipal().toString(), "User created", null, true);
        return ResponseEntity.status(HttpStatus.CREATED).body(authResponseDto);
    }

    public ResponseEntity<AuthResponseDto> loginUser(@Valid AuthRequestDto authRequest) {
        String username = authRequest.username();
        String password = authRequest.password();
        try {
            Authentication authentication = this.authenticate(username, password);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtUtils.createToken(authentication);

            return ResponseEntity.ok(new AuthResponseDto(
                    authentication.getPrincipal().toString(), "User logged", token, true));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    public Authentication authenticate(String username, String password) {
        UserDetails userDetails = this.loadUserByUsername(username);
        if (userDetails == null) {
            throw new BadCredentialsException("Invalid username or password");
        }
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Incorrect Password");
        }
        return new UsernamePasswordAuthenticationToken(username, password, userDetails.getAuthorities());
    }

    private RoleEntity findRoleById() {
        return roleRepository.findRoleNameById(10L);
    }
}
