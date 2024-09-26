package itmo_java_advanced.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import itmo_java_advanced.enums.Color;
import itmo_java_advanced.enums.Gender;
import itmo_java_advanced.model.db.entity.Car;
import itmo_java_advanced.model.db.entity.User;
import itmo_java_advanced.model.db.repository.CarRepository;
import itmo_java_advanced.model.db.repository.UserRepository;
import itmo_java_advanced.model.dto.request.UserRequest;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.ConfigurableMockMvcBuilder;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerTest {

    UserRequest userRequest = new UserRequest();

    {
        userRequest.setName("TEST");
        userRequest.setSurname("TEST");
        userRequest.setGender(Gender.MALE);
        userRequest.setAge(20);
    }

    MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    ObjectMapper objectMapper;

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

    @Before
    public void setup() {
        ConfigurableMockMvcBuilder<DefaultMockMvcBuilder> builder = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(this.restDocumentation));

        this.mockMvc = builder.build();
    }

    private User setUpUser() {
        Car car = new Car();
        car.setModel("TEST");
        car.setYear(2020);
        car.setColor(Color.BLACK);
        carRepository.save(car);

        User user = new User();
        user.setName("TEST_1");
        user.setSurname("TEST_1");
        user.setGender(Gender.MALE);
        user.setAge(20);
        user.setCars(Collections.singletonList(car));

        return userRepository.save(user);
    }

    private List<User> setUpUsers(int usersCount) {
        List<User> users = new ArrayList<>();

        for (int i = 0; i < usersCount; i++) {
            User user = new User();
            user.setName("TEST_" + i);
            user.setSurname("TEST_" + i);
            user.setGender(Gender.MALE);
            user.setAge(20);

            users.add(userRepository.save(user));
        }

        return users;
    }

    @Test
    @SneakyThrows
    public void createUser() {
        String content = objectMapper.writeValueAsString(userRequest);

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(userRequest.getName()))
                .andExpect(jsonPath("$.surname").value(userRequest.getSurname()))
                .andDo(document("users/create"));
    }

    @Test
    @SneakyThrows
    public void getUser() {
        User user = setUpUser();

        mockMvc.perform(get("/api/users/" + user.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(user.getName()))
                .andExpect(jsonPath("$.surname").value(user.getSurname()))
                .andDo(document("users/get"));
    }

    @Test
    @SneakyThrows
    public void updateUser() {
        User user = setUpUser();

        String content = objectMapper.writeValueAsString(userRequest);

        mockMvc.perform(put("/api/users/" + user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(userRequest.getName()))
                .andExpect(jsonPath("$.surname").value(userRequest.getSurname()))
                .andDo(document("users/update"));
    }

    @Test
    @SneakyThrows
    public void deleteUser() {
        User user = setUpUser();

        mockMvc.perform(delete("/api/users/" + user.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("users/delete"));
    }

    @Test
    @SneakyThrows
    public void getAllUsers() {
        List<User> users = setUpUsers(10);

        mockMvc.perform(get("/api/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(users.size())))
                .andExpect(jsonPath("$.content.[0].name").value(users.get(0).getName()))
                .andExpect(jsonPath("$.content.[0].surname").value(users.get(0).getSurname()))
                .andExpect(jsonPath("$.content.[1].name").value(users.get(1).getName()))
                .andExpect(jsonPath("$.content.[1].surname").value(users.get(1).getSurname()))
                .andDo(document("users/all"));
    }

    @Test
    @SneakyThrows
    public void getAllCarsByUserId() {
        User user = setUpUser();

        mockMvc.perform(get("/api/users/1/cars"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].model").value(user.getCars().get(0).getModel()))
                .andExpect(jsonPath("$.[0].color").value(user.getCars().get(0).getColor().toString()))
                .andExpect(jsonPath("$.[0].year").value(user.getCars().get(0).getYear()))
                .andDo(document("users/cars"));
    }
}