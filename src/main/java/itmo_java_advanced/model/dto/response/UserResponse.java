package itmo_java_advanced.model.dto.response;

import itmo_java_advanced.model.db.entity.Car;
import itmo_java_advanced.model.dto.request.UserRequest;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse extends UserRequest {
    Long id;
    List<Car> cars;
}
