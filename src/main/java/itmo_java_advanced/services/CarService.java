package itmo_java_advanced.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import itmo_java_advanced.exceptions.CustomException;
import itmo_java_advanced.model.db.entity.Car;
import itmo_java_advanced.model.db.entity.User;
import itmo_java_advanced.model.db.repository.CarRepository;
import itmo_java_advanced.model.dto.request.CarRequest;
import itmo_java_advanced.model.dto.request.CarToUserRequest;
import itmo_java_advanced.model.dto.response.ApiResponse;
import itmo_java_advanced.model.dto.response.CarResponse;
import itmo_java_advanced.utils.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarService {
    private final CarRepository carRepository;
    private final ObjectMapper mapper;
    private final UserService userService;

    private void validateCreateRequest(CarRequest carRequest) {
        if (carRequest.getModel() == null) {
            throw new CustomException("Поле model обязательно для заполнения", HttpStatus.BAD_REQUEST);
        }

        if (carRequest.getColor() == null) {
            throw new CustomException("Поле color обязательно для заполнения", HttpStatus.BAD_REQUEST);
        }

        if (carRequest.getYear() == null) {
            throw new CustomException("Поле year обязательно для заполнения", HttpStatus.BAD_REQUEST);
        }
    }

    public CarResponse createCar(CarRequest carRequest) {
        validateCreateRequest(carRequest);

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
            throw new CustomException("Автомобиль не найден", HttpStatus.NOT_FOUND);
        }

        carRepository.deleteById(id);

        return new ApiResponse();
    }

    public Page<CarResponse> getAllCars(Integer page, Integer perPage, String sort, Sort.Direction order, String filter) {
        Pageable pageRequest = PaginationUtil.getPageRequest(page, perPage, sort, order);

        Page<Car> cars;
        if (filter == null) {
            cars = carRepository.findAll(pageRequest);
        } else {
            cars = carRepository.findAllWithModelFilter(pageRequest, filter);
        }

        List<CarResponse> content = cars.getContent().stream()
                .map(car -> mapper.convertValue(car, CarResponse.class))
                .collect(Collectors.toList());

        return new PageImpl<>(content, pageRequest, cars.getTotalElements());
    }

    public ApiResponse addCarToUser(CarToUserRequest carToUserRequest) {
        Car car = getCarFromDB(carToUserRequest.getCarId());
        User user = userService.getUserFromDB(carToUserRequest.getUserId());

        try {
            user.getCars().add(car);
            userService.updateUserData(user);

            car.setUser(user);
            carRepository.save(car);
        } catch (DataIntegrityViolationException exception) {
            throw new CustomException("Не удалось добавить автомобиль пользователю", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ApiResponse();
    }

    public Car getCarFromDB(Long id) {
        return carRepository
                .findById(id)
                .orElseThrow(() -> new CustomException("Автомобиль не найден", HttpStatus.NOT_FOUND));
    }
}
