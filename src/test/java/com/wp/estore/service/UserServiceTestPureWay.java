package com.wp.estore.service;

import com.wp.estore.dtos.PageableResponse;
import com.wp.estore.dtos.UserDto;
import com.wp.estore.entities.User;
import com.wp.estore.exceptions.ResourceNotFoundException;
import com.wp.estore.repositories.UserRepository;
import com.wp.estore.services.Impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTestPureWay {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .userId("123")
                .name("Piyush")
                .email("piyush@gmail.com")
                .about("Test user")
                .gender("Male")
                .imageName("abc.png")
                .password("pass")
                .build();

        userDto = UserDto.builder()
                .userId("123")
                .name("Piyush")
                .email("piyush@gmail.com")
                .about("Test user")
                .gender("Male")
                .imageName("abc.png")
                .password("pass")
                .build();
    }

    // ---------------- CREATE USER ----------------

    @Test
    void testCreateUser() {
        Mockito.when(modelMapper.map(Mockito.any(UserDto.class), Mockito.eq(User.class)))
                .thenReturn(user);

        Mockito.when(userRepository.save(Mockito.any(User.class)))
                .thenReturn(user);

        Mockito.when(modelMapper.map(Mockito.any(User.class), Mockito.eq(UserDto.class)))
                .thenReturn(userDto);

        UserDto result = userService.createUser(userDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Piyush", result.getName());
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any(User.class));
    }

    // ---------------- UPDATE USER ----------------

    @Test
    void testUpdateUser() {
        Mockito.when(userRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.of(user));

        Mockito.when(userRepository.save(Mockito.any(User.class)))
                .thenReturn(user);

        Mockito.when(modelMapper.map(Mockito.any(User.class), Mockito.eq(UserDto.class)))
                .thenReturn(userDto);

        UserDto updated = userService.updateUser(userDto, "123");

        Assertions.assertNotNull(updated);
        Assertions.assertEquals(userDto.getName(), updated.getName());
    }

    @Test
    void testUpdateUser_NotFound() {
        Mockito.when(userRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () ->
                userService.updateUser(userDto, "invalid-id"));
    }

    // ---------------- DELETE USER ----------------

    @Test
    void testDeleteUser() {
        Mockito.when(userRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.of(user));

        Mockito.doNothing().when(userRepository).delete(Mockito.any(User.class));

        userService.deleteUser("123");

        Mockito.verify(userRepository, Mockito.times(1)).delete(user);
    }

    @Test
    void testDeleteUser_NotFound() {
        Mockito.when(userRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () ->
                userService.deleteUser("invalid-id"));
    }

    // ---------------- GET ALL USERS ----------------

    @Test
    void testGetAllUsers() {
        List<User> users = Arrays.asList(user, user, user);
        Page<User> page = new PageImpl<>(users);

        Mockito.when(userRepository.findAll(Mockito.any(Pageable.class)))
                .thenReturn(page);

        PageableResponse<UserDto> response =
                userService.getAllUser(0, 10, "name", "asc");

        Assertions.assertEquals(3, response.getContent().size());
    }

    // ---------------- GET USER BY ID ----------------

    @Test
    void testGetUserById() {
        Mockito.when(userRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.of(user));

        Mockito.when(modelMapper.map(Mockito.any(User.class), Mockito.eq(UserDto.class)))
                .thenReturn(userDto);

        UserDto result = userService.getUserById("123");

        Assertions.assertNotNull(result);
        Assertions.assertEquals("123", result.getUserId());
    }

    @Test
    void testGetUserById_NotFound() {
        Mockito.when(userRepository.findById(Mockito.anyString()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () ->
                userService.getUserById("invalid-id"));
    }

    // ---------------- GET USER BY EMAIL ----------------

    @Test
    void testGetUserByEmail() {
        Mockito.when(userRepository.findByEmail(Mockito.anyString()))
                .thenReturn(Optional.of(user));

        Mockito.when(modelMapper.map(Mockito.any(User.class), Mockito.eq(UserDto.class)))
                .thenReturn(userDto);

        UserDto result = userService.getUserByEmail("piyush@gmail.com");

        Assertions.assertEquals(user.getEmail(), result.getEmail());
    }

    // ---------------- SEARCH USER ----------------

    @Test
    void testSearchUser() {
        List<User> users = Arrays.asList(user, user, user);

        Mockito.when(userRepository.findByNameContaining(Mockito.anyString()))
                .thenReturn(users);

        Mockito.when(modelMapper.map(Mockito.any(User.class), Mockito.eq(UserDto.class)))
                .thenReturn(userDto);

        List<UserDto> result = userService.searchUser("Piyush");

        Assertions.assertEquals(3, result.size());
    }
}