package itmo_java_advanced.services;

import itmo_java_advanced.model.dto.request.CarRequest;
import itmo_java_advanced.model.dto.response.ApiResponse;

public class CarService {

    public ApiResponse createCar(CarRequest carRequest) {
        return new ApiResponse();
    }

    public ApiResponse getCar(Long id) {
        return new ApiResponse();
    }

    public ApiResponse updateCar(Long id, CarRequest carRequest) {
        return new ApiResponse();
    }

    public ApiResponse deleteCar(Long id) {
        return new ApiResponse("error", 401);
    }
}
