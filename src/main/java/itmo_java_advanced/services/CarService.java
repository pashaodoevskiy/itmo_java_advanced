package itmo_java_advanced.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import itmo_java_advanced.model.db.entity.Car;
import itmo_java_advanced.model.db.repository.CarRepository;
import itmo_java_advanced.model.dto.request.CarRequest;
import itmo_java_advanced.model.dto.response.ApiResponse;
import itmo_java_advanced.model.dto.response.CarResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarService {
    private final CarRepository carRepository;
    private final ObjectMapper mapper;

    public CarResponse createCar(CarRequest carRequest) {
        Car car = mapper.convertValue(carRequest, Car.class);

        return mapper.convertValue(carRepository.save(car), CarResponse.class);
    }

    public CarResponse getCar(Long id) {
        Car car = getCarFromDB(id);

        return mapper.convertValue(car, CarResponse.class);
    }

    public CarResponse updateCar(Long id, CarRequest carRequest) {
        Car car = getCarFromDB(id);
        car.setModel(carRequest.getModel() == null ? car.getModel() : carRequest.getModel());
        car.setColor(carRequest.getColor() == null ? car.getColor() : carRequest.getColor());
        car.setYear(carRequest.getYear() == null ? car.getYear() : carRequest.getYear());

        return mapper.convertValue(carRepository.save(car), CarResponse.class);
    }

    public ApiResponse deleteCar(Long id) {
        if (!carRepository.existsById(id)) {
            throw new EntityNotFoundException("Автомобиль не найден");
        }

        carRepository.deleteById(id);

        return new ApiResponse();
    }

    public List<CarResponse> getAllCars() {
        return carRepository.findAll().stream()
                .map(car -> mapper.convertValue(car, CarResponse.class))
                .collect(Collectors.toList());
    }

    private Car getCarFromDB(Long id) {
        return carRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Автомобиль не найден"));
    }

}
