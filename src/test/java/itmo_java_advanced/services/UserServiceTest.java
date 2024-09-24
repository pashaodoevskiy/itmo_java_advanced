package itmo_java_advanced.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import itmo_java_advanced.enums.Gender;
import itmo_java_advanced.exceptions.CustomException;
import itmo_java_advanced.model.db.entity.Car;
import itmo_java_advanced.model.db.entity.User;
import itmo_java_advanced.model.db.repository.UserRepository;
import itmo_java_advanced.model.dto.request.UserRequest;
import itmo_java_advanced.model.dto.response.CarResponse;
import itmo_java_advanced.model.dto.response.UserResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Spy
    private ObjectMapper mapper;

    @Test
    public void createUser() {
        UserRequest request = new UserRequest();
        request.setName("Иван");
        request.setSurname("Иванов");
        request.setAge(20);
        request.setGender(Gender.MALE);

        User user = new User();
        user.setId(1L);
        user.setName("Иван");
        user.setSurname("Иванов");
        user.setAge(20);
        user.setGender(Gender.MALE);

        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponse result = userService.createUser(request);

        assertEquals(user.getId(), result.getId());
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getSurname(), result.getSurname());
        assertEquals(user.getAge(), result.getAge());
    }

    @Test(expected = CustomException.class)
    public void createUser_validationFailed() {
        userService.createUser(new UserRequest());
    }

    @Test(expected = CustomException.class)
    public void createUser_userExists() {
        UserRequest request = new UserRequest();
        request.setName("Иван");
        request.setSurname("Иванов");

        User user = new User();
        user.setId(1L);
        user.setName("Иван");
        user.setSurname("Иванов");

        when(userRepository.findByNameAndSurname(anyString(), anyString())).thenReturn(Optional.of(user));

        userService.createUser(request);
    }

    @Test
    public void getUser() {
        User user = new User();
        user.setId(1L);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        UserResponse result = userService.getUser(user.getId());

        assertEquals(user.getId(), result.getId());
    }

    @Test(expected = CustomException.class)
    public void getUser_notFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        userService.getUser(1L);
    }


    @Test
    public void updateUser() {
        UserRequest request = new UserRequest();
        request.setName("Иван");
        request.setSurname("Иванов");

        User user = new User();
        user.setId(1L);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponse result = userService.updateUser(user.getId(), request);

        assertEquals(user.getId(), result.getId());
        assertEquals(user.getName(), result.getName());
    }

    @Test
    public void deleteUser() {
        when(userRepository.existsById(anyLong())).thenReturn(true);
        userService.deleteUser(anyLong());

        verify(userRepository, times(1)).deleteById(anyLong());
    }

    @Test(expected = CustomException.class)
    public void deleteUser_notFound() {
        userService.deleteUser(1L);
    }

    @Test
    public void getAllUsers() {
        User user = new User();
        user.setId(1L);

        List<User> users = List.of(user, user, user);

        when(userRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(users));

        Page<UserResponse> result = userService.getAllUsers(1, 2, "id", Sort.Direction.ASC, null);

        assertEquals(users.size(), result.getTotalElements());
        assertEquals(2, result.getTotalPages());
        assertEquals(0, result.getPageable().getPageNumber());
    }

    @Test
    public void updateUserData() {
        User user = new User();
        user.setId(1L);

        userService.updateUserData(user);

        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void getAllCarsByUserId() {
        Car car = new Car();
        car.setId(1L);

        User user = new User();
        user.setId(1L);
        user.setCars(Collections.singletonList(car));

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        List<CarResponse> cars = userService.getAllCarsByUserId(user.getId());
        assertEquals(user.getCars().size(), cars.size());
    }
}