package com.wp.estore.service;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.wp.estore.dtos.UserDto;
import com.wp.estore.entities.User;
import com.wp.estore.repositories.UserRepository;
import com.wp.estore.services.UserService;

@SpringBootTest
public class UserServiceTest {
	
	@MockitoBean
	private UserRepository userRepository;
	
	@Autowired
	private UserService userService;
	
	User user;
	
	@Autowired
	private ModelMapper mapper;
	
	@BeforeEach
	public void init() {
		user = User.builder()
		.name("Piyush")
		.email("piyush123@gmail.com")
		.about("This is testing create method")
		.gender("Male")
		.imageName("abc.png")
		.password("abc")
		.build();
	}
	
	@Test
	public void testCreateUser() {
		Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);
		UserDto user1 = userService.createUser(mapper.map(user,UserDto.class));
		//System.out.println(user1.getName());
		Assertions.assertNotNull(user1);
		Assertions.assertEquals("Piyush", user1.getName());
	}
	
	@Test
	public void testUpdateUser() {
		String userId = "sfsffdsfsdfdf";
		
		UserDto userDto = UserDto.builder()
		.name("Piyush Wankar")
		.about("This is updated testing create method")
		.gender("Malee")
		.imageName("abc.png")
		.build();
		
		//providing mock objects related to db operations
		Mockito.when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.of(user));
		Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);
		
		UserDto updateUser = userService.updateUser(userDto, userId);
		System.out.println(updateUser.getName());
		System.out.println(updateUser.getImageName());
		Assertions.assertNotNull(updateUser);
		Assertions.assertEquals(userDto.getName(), updateUser.getName(),"name is not valid");
		
	}
	
}
