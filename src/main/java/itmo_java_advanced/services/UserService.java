package itmo_java_advanced.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import itmo_java_advanced.model.db.entity.Car;
import itmo_java_advanced.model.db.entity.User;
import itmo_java_advanced.model.db.repository.UserRepository;
import itmo_java_advanced.model.dto.request.UserRequest;
import itmo_java_advanced.model.dto.response.ApiResponse;
import itmo_java_advanced.model.dto.response.CarResponse;
import itmo_java_advanced.model.dto.response.UserResponse;
import itmo_java_advanced.utils.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ObjectMapper mapper;

    public UserResponse createUser(UserRequest userRequest) {
        User user = mapper.convertValue(userRequest, User.class);
        userRepository.save(user);

        return mapper.convertValue(user, UserResponse.class);
    }

    public UserResponse getUser(Long id) {
        User user = getUserFromDB(id);

        return mapper.convertValue(user, UserResponse.class);
    }

    public UserResponse updateUser(Long id, UserRequest userRequest) {
        User user = getUserFromDB(id);
        user.setName(userRequest.getName() == null ? user.getName() : userRequest.getName());
        user.setSurname(userRequest.getSurname() == null ? user.getSurname() : userRequest.getSurname());
        user.setAge(userRequest.getAge() == null ? user.getAge() : userRequest.getAge());
        user.setGender(userRequest.getGender() == null ? user.getGender() : userRequest.getGender());

        return mapper.convertValue(userRepository.save(user), UserResponse.class);
    }

    public ApiResponse deleteUser(Long id) {
        if(!userRepository.existsById(id)) {
            throw new EntityNotFoundException("Пользователь не найден");
        }

        userRepository.deleteById(id);

        return new ApiResponse();
    }

    public Page<UserResponse> getAllUsers(Integer page, Integer perPage, String sort, Sort.Direction order, String filter) {
        Pageable pageRequest = PaginationUtil.getPageRequest(page, perPage, sort, order);

        Page<User> users;
        if (filter == null) {
            users = userRepository.findAll(pageRequest);
        } else {
            users = userRepository.findAllWithNameAndSurnameFilter(pageRequest, filter);
        }

        List<UserResponse> content = users.getContent().stream()
                .map(user -> mapper.convertValue(user, UserResponse.class))
                .collect(Collectors.toList());

        return new PageImpl<>(content, pageRequest, users.getTotalElements());
    }

    public User getUserFromDB(Long id) {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));
    }

    public void updateUserData(User user) {
        userRepository.save(user);
    }

    public List<CarResponse> getAllCarsByUserId(Long id) {
        List<Car> cars = getUserFromDB(id).getCars();

        return cars
                .stream()
                .map(car -> mapper.convertValue(car, CarResponse.class))
                .collect(Collectors.toList());
    }
}
