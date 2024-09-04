package itmo_java_advanced.controllers;

import itmo_java_advanced.model.dto.request.UserRequest;
import itmo_java_advanced.model.dto.response.ApiResponse;
import itmo_java_advanced.model.dto.response.UserResponse;
import itmo_java_advanced.services.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService = new UserService();

    @PostMapping
    public ApiResponse createUser(UserRequest userRequest) {
        return this.userService.createUser(userRequest);
    }

    @GetMapping("/{id}")
    public UserResponse getUser(@PathVariable Long id) {
        return this.userService.getUser(id);
    }

    @PutMapping("/{id}")
    public ApiResponse updateUser(@PathVariable Long id, UserRequest userRequest) {
        return this.userService.updateUser(id, userRequest);
    }

    @DeleteMapping("/{id}")
    public ApiResponse deleteUser(@PathVariable Long id) {
        return this.userService.deleteUser(id);
    }
}
