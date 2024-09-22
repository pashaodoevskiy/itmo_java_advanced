package itmo_java_advanced.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import itmo_java_advanced.model.dto.request.CarRequest;
import itmo_java_advanced.model.dto.request.CarToUserRequest;
import itmo_java_advanced.model.dto.response.ApiResponse;
import itmo_java_advanced.model.dto.response.CarResponse;
import itmo_java_advanced.services.CarService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Автомобили")
@RestController
@AllArgsConstructor
@RequestMapping("/api/cars")
public class CarController {
    private final CarService carService;

    @Operation(summary = "Создать автомобиль")
    @PostMapping
    public CarResponse createCar(@RequestBody CarRequest carRequest) {
        return carService.createCar(carRequest);
    }

    @Operation(summary = "Получить автомобиль по id")
    @GetMapping("/{id}")
    public CarResponse getCar(@PathVariable Long id) {
        return carService.getCar(id);
    }

    @Operation(summary = "Обновить автомобиль по id")
    @PutMapping("/{id}")
    public CarResponse updateCar(@PathVariable Long id, @RequestBody CarRequest carRequest) {
        return carService.updateCar(id, carRequest);
    }

    @Operation(summary = "Удалить автомобиль по id")
    @DeleteMapping("/{id}")
    public ApiResponse deleteCar(@PathVariable Long id) {
        return carService.deleteCar(id);
    }

    @Operation(summary = "Получить список автомобилей")
    @GetMapping
    public Page<CarResponse> getAllCars(@RequestParam(defaultValue = "1") Integer page,
                                        @RequestParam(defaultValue = "10") Integer perPage,
                                        @RequestParam(defaultValue = "id") String sort,
                                        @RequestParam(defaultValue = "ASC") Sort.Direction order,
                                        @RequestParam(required = false) String filter

    ) {
        return carService.getAllCars(page, perPage, sort, order, filter);
    }

    @Operation(summary = "Добавить автомобиль пользователю")
    @PostMapping("/carToUser")
    public ApiResponse addCarToUser(@RequestBody CarToUserRequest carToUserRequest) {
        return carService.addCarToUser(carToUserRequest);
    }
}
