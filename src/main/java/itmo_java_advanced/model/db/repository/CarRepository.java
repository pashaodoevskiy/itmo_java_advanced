package itmo_java_advanced.model.db.repository;

import itmo_java_advanced.enums.Color;
import itmo_java_advanced.model.db.entity.Car;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    Optional<Car> findByModelAndColor(String brand, Color color);

    Car findByModel(String model);

    List<Car> findAllByModel(String model);

//    // sql Structured Query Language
//    @Query(nativeQuery = true, value = "select * from cars where id > 10 limit = :limit")
//    Car getCar(@Param("limit") Integer limit); // аннотация Param позволяет задать название переменной которая будет передаваться в sql запрос
//
//    //hql Hibernate Query Language
//    @Query("select c from Car c where c.color = :color and c.user.id = :userId")
//    List<Car> findCarsByColorAndUserId(@Param("color") Color color, @Param("userId") Integer id);

    @Query("select c from Car c where lower(c.model) like  %:filter%")
    Page<Car> findAllWithModelFilter(Pageable pageRequest, @Param("filter") String filter);
}
