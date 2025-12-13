package com.wp.estore.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.wp.estore.dtos.PageableResponse;
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

	@Test
	public void testDeleteUser() {
		String userId = "sfdfsdfdfsdf";
		Mockito.when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.of(user));
		// stub delete to do nothing for any User instance, then verify it was called
		Mockito.doNothing().when(userRepository).delete(Mockito.any(User.class));

		userService.deleteUser(userId);
		Mockito.verify(userRepository, Mockito.times(1)).delete(user);

	}
	
	@Test
	public void testGetAllUsers() {
		
		User user1 = User.builder()
				.name("Shreyash")
				.email("Shreyash123@gmail.com")
				.about("This is testing create method")
				.gender("Male")
				.imageName("abc.png")
				.password("abc")
				.build();
		
		User user2 = User.builder()
				.name("Sanket")
				.email("Sanket123@gmail.com")
				.about("This is testing create method")
				.gender("Male")
				.imageName("abc.png")
				.password("abc")
				.build();
		
		List<User> userList = Arrays.asList(user, user1, user2);
		Page<User> page = new PageImpl<>(userList);
		Mockito.when(userRepository.findAll((Pageable) Mockito.any())).thenReturn(page);
		PageableResponse<UserDto> allUser = userService.getAllUser(1, 2, "name", "desc");
		Assertions.assertEquals(3, allUser.getContent().size());
		
	}
	
	@Test
	public void TestGetUserById() {
		String userId = "afddfsfsdf";
		user.setUserId(userId);
		Mockito.when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.of(user));
		UserDto userById = userService.getUserById(userId);
		Assertions.assertNotNull(userById);
		Assertions.assertEquals(user.getUserId(), userById.getUserId(),"Name not matched");
		System.out.println("Expected : " + user.getUserId());
		System.out.println("Actual : " + userById.getUserId());
	}
	
	@Test
	public void testGetUserByEmail() {
		String email = "piyush1@gmail.com";
		Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
		UserDto userByEmail = userService.getUserByEmail(email);
		Assertions.assertNotNull(userByEmail);
		Assertions.assertEquals(user.getEmail(),userByEmail.getEmail(),"Email not matched");	
		System.out.println("Expected : " + user.getEmail());
		System.out.println("Actual : " + userByEmail.getEmail());
	}
	
	@Test
	public void testSearchUser() {
		String keyword = "kumar";
		User user1 = User.builder()
				.name("Piyush kumar")
				.email("piyushkumar@gmail.com")
				.about("This is testing create")
				.gender("Male")
				.imageName("abc.png")
				.password("abc")
				.build();
		
		User user2 = User.builder()
				.name("Rohan kumar")
				.email("piyushkumar@gmail.com")
				.about("This is testing create")
				.gender("Male")
				.imageName("abc.png")
				.password("abc")
				.build();
		
		User user3 = User.builder()
				.name("Tushar kumar")
				.email("piyushkumar@gmail.com")
				.about("This is testing create")
				.gender("Male")
				.imageName("abc.png")
				.password("abc")
				.build();
		
		List<User> userList = Arrays.asList(user1, user2, user3);
		Mockito.when(userRepository.findByNameContaining(keyword)).thenReturn(userList);
		List<UserDto> userDtos = userService.searchUser(keyword);
		Assertions.assertEquals(3, userDtos.size());
	}

}
