package itmo_java_advanced.model.db.entity;

import itmo_java_advanced.enums.Color;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "cars")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Car extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "model")
    String model;

    @Column(name = "color")
    @Enumerated(EnumType.STRING)
    Color color;

    @Column(name = "year")
    Integer year;

    @ManyToOne
    User user;
}
