package bsu.by.studently.model;

import jakarta.persistence.*;

@Entity(name = "roles")
@Table(
        name = "roles",
        uniqueConstraints = {
                @UniqueConstraint(name = "role_name_unique", columnNames = "role_name")
        }
)
public class Role{
    @Id
    @SequenceGenerator(
            name = "role_sequence",
            sequenceName = "role_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "role_sequence"
    )
    @Column(
            name = "role_id",
            updatable = false
    )
    private Long id;

    @Column(
            name = "role_name",
            nullable = false
    )
    private String name;

    public Role(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Role(String name) {
        this.name = name;
    }

    public Role() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
