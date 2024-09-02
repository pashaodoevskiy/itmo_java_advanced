package itmo_java_advanced.controllers;

import itmo_java_advanced.model.dto.request.CarRequest;
import itmo_java_advanced.model.dto.response.ApiResponse;
import itmo_java_advanced.model.dto.response.CarResponse;
import itmo_java_advanced.services.CarService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/car")
public class CarController {

    private final CarService carService = new CarService();

    @PostMapping
    public ApiResponse createCar(CarRequest carRequest) {
        return this.carService.createCar(carRequest);
    }

    @GetMapping("/{id}")
    public CarResponse getCar(@PathVariable Long id) {
        return this.carService.getCar(id);
    }

    @PutMapping("/{id}")
    public ApiResponse updateCar(@PathVariable Long id, CarRequest carRequest) {
        return this.carService.updateCar(id, carRequest);
    }

    @DeleteMapping("/{id}")
    public ApiResponse deleteCar(@PathVariable Long id) {
        return this.carService.deleteCar(id);
    }
}
