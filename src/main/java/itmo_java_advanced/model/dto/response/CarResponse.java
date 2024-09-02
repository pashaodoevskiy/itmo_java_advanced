package itmo_java_advanced.model.dto.response;

import itmo_java_advanced.model.dto.request.CarRequest;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CarResponse extends CarRequest {
    Long id;
}
