package itmo_java_advanced.services;

import itmo_java_advanced.model.dto.request.UserRequest;
import itmo_java_advanced.model.dto.response.ApiResponse;
import itmo_java_advanced.model.dto.response.UserResponse;

public class UserService {

    public ApiResponse createUser(UserRequest userRequest) {
        return new ApiResponse();
    }

    public UserResponse getUser(Long id) {
        return new UserResponse();
    }

    public ApiResponse updateUser(Long id, UserRequest userRequest) {
        return new ApiResponse();
    }

    public ApiResponse deleteUser(Long id) {
        return new ApiResponse("error", 401);
    }
}
