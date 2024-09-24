package itmo_java_advanced.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import itmo_java_advanced.enums.Color;
import itmo_java_advanced.exceptions.CustomException;
import itmo_java_advanced.model.db.entity.Car;
import itmo_java_advanced.model.db.entity.User;
import itmo_java_advanced.model.db.repository.CarRepository;
import itmo_java_advanced.model.dto.request.CarRequest;
import itmo_java_advanced.model.dto.request.CarToUserRequest;
import itmo_java_advanced.model.dto.response.CarResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CarServiceTest {

    @InjectMocks
    private CarService carService;

    @Mock
    private CarRepository carRepository;

    @Spy
    private ObjectMapper mapper;

    @Mock
    private UserService userService;

    @Test
    public void createCar() {
        CarRequest request = new CarRequest();
        request.setModel("ВАЗ");
        request.setColor(Color.BLACK);
        request.setYear(2020);

        Car car = new Car();
        car.setModel("ВАЗ");
        car.setColor(Color.BLACK);
        car.setYear(2020);

        when(carRepository.save(any(Car.class))).thenReturn(car);

        CarResponse result = carService.createCar(request);

        assertEquals(car.getId(), result.getId());
        assertEquals(car.getModel(), result.getModel());
        assertEquals(car.getColor(), result.getColor());
        assertEquals(car.getYear(), result.getYear());
    }

    @Test(expected = CustomException.class)
    public void createCar_validationFailed() {
        carService.createCar(new CarRequest());
    }

    @Test
    public void getCar() {
        Car car = new Car();
        car.setId(1L);

        when(carRepository.findById(anyLong())).thenReturn(Optional.of(car));

        CarResponse result = carService.getCar(car.getId());

        assertEquals(car.getId(), result.getId());
    }

    @Test(expected = CustomException.class)
    public void getCar_notFound() {
        when(carRepository.findById(anyLong())).thenReturn(Optional.empty());

        carService.getCar(anyLong());
    }

    @Test
    public void updateCar() {
        CarRequest request = new CarRequest();
        request.setModel("ВАЗ");
        request.setColor(Color.BLACK);
        request.setYear(2020);

        Car car = new Car();
        car.setId(1L);

        when(carRepository.findById(anyLong())).thenReturn(Optional.of(car));
        when(carRepository.save(any(Car.class))).thenReturn(car);

        CarResponse result = carService.updateCar(car.getId(), request);

        assertEquals(car.getId(), result.getId());
        assertEquals(car.getModel(), result.getModel());
        assertEquals(car.getColor(), result.getColor());
        assertEquals(car.getYear(), result.getYear());
    }

    @Test
    public void deleteCar() {
        when(carRepository.existsById(anyLong())).thenReturn(true);
        carService.deleteCar(anyLong());

        verify(carRepository, times(1)).deleteById(anyLong());
    }

    @Test(expected = CustomException.class)
    public void deleteCar_notFound() {
        carService.deleteCar(1L);
    }

    @Test
    public void getAllCars() {
        Car car = new Car();
        car.setId(1L);

        ArrayList<Car> cars = new ArrayList<>();
        cars.add(car);
        cars.add(car);
        cars.add(car);

        when(carRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(cars));

        Page<CarResponse> result = carService.getAllCars(1, 2, "id", Sort.Direction.ASC, null);

        assertEquals(cars.size(), result.getTotalElements());
        assertEquals(2, result.getTotalPages());
        assertEquals(0, result.getPageable().getPageNumber());
    }

    @Test
    public void addCarToUser() {
        Car car = new Car();
        car.setId(1L);

        when(carRepository.findById(anyLong())).thenReturn(Optional.of(car));

        User user = new User();
        user.setId(1L);
        user.setCars(new ArrayList<>());

        when(userService.getUserFromDB(user.getId())).thenReturn(user);
        when(userService.updateUserData(any(User.class))).thenReturn(user);

        CarToUserRequest request = CarToUserRequest.builder()
                .carId(car.getId())
                .userId(user.getId())
                .build();

        carService.addCarToUser(request);

        verify(carRepository, times(1)).save(any(Car.class));
        assertEquals(user.getId(), car.getUser().getId());
    }
}