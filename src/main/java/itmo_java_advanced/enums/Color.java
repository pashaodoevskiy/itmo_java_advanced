package itmo_java_advanced.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Color {
    BLACK("Черный"),
    WHITE("Белый"),
    RED("Красный");

    private final String description;
}
