package itmo_java_advanced.model.dto.response;

import itmo_java_advanced.model.dto.request.UserRequest;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse extends UserRequest {
    Long id;
}
