package itmo_java_advanced.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import itmo_java_advanced.model.db.entity.User;
import itmo_java_advanced.model.db.repository.UserRepository;
import itmo_java_advanced.model.dto.request.UserRequest;
import itmo_java_advanced.model.dto.response.ApiResponse;
import itmo_java_advanced.model.dto.response.UserResponse;
import lombok.RequiredArgsConstructor;
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

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> mapper.convertValue(user, UserResponse.class))
                .collect(Collectors.toList());
    }

    private User getUserFromDB(Long id) {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));
    }
}
