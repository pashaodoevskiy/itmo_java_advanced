package itmo_java_advanced.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import itmo_java_advanced.model.dto.request.UserRequest;
import itmo_java_advanced.model.dto.response.ApiResponse;
import itmo_java_advanced.model.dto.response.CarResponse;
import itmo_java_advanced.model.dto.response.UserResponse;
import itmo_java_advanced.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Пользователи")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Создать пользователя")
    @PostMapping
    public UserResponse createUser(@RequestBody UserRequest userRequest) {
        return userService.createUser(userRequest);
    }

    @Operation(summary = "Получить пользователя по id")
    @GetMapping("/{id}")
    public UserResponse getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @Operation(summary = "Обновить пользователя по id")
    @PutMapping("/{id}")
    public UserResponse updateUser(@PathVariable Long id, @RequestBody UserRequest userRequest) {
        return userService.updateUser(id, userRequest);
    }

    @Operation(summary = "Удалить пользователя по id")
    @DeleteMapping("/{id}")
    public ApiResponse deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }

    @Operation(summary = "Получить список пользователей")
    @GetMapping
    public Page<UserResponse> getAllUsers(@RequestParam(defaultValue = "1") Integer page,
                                          @RequestParam(defaultValue = "10") Integer perPage,
                                          @RequestParam(defaultValue = "id") String sort,
                                          @RequestParam(defaultValue = "ASC") Sort.Direction order,
                                          @RequestParam(required = false) String filter

    ) {
        return userService.getAllUsers(page, perPage, sort, order, filter);
    }

    @Operation(summary = "Получить список автомобилей по пользователю")
    @GetMapping("/{id}/cars")
    public List<CarResponse> getAllCarsByUserId(@PathVariable Long id) {
        return userService.getAllCarsByUserId(id);
    }
}
