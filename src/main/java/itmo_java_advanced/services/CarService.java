package itmo_java_advanced.services;

import itmo_java_advanced.model.dto.request.CarRequest;
import itmo_java_advanced.model.dto.response.ApiResponse;
import itmo_java_advanced.model.dto.response.CarResponse;

public class CarService {

    public ApiResponse createCar(CarRequest carRequest) {
        return new ApiResponse();
    }

    public CarResponse getCar(Long id) {
        return new CarResponse();
    }

    public ApiResponse updateCar(Long id, CarRequest carRequest) {
        return new ApiResponse();
    }

    public ApiResponse deleteCar(Long id) {
        return new ApiResponse("error", 401);
    }
}
