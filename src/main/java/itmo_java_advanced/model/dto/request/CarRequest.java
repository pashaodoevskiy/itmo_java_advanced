package itmo_java_advanced.model.dto.request;

import itmo_java_advanced.enums.Color;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CarRequest {
    String model;
    Color color;
    Integer year;
}
