package itmo_java_advanced.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Gender {
    MALE("М"),
    FEMALE("Ж");

    private final String description;
}
