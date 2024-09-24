package itmo_java_advanced.model.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import itmo_java_advanced.model.dto.request.CarRequest;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CarResponse extends CarRequest {
    Long id;
}
