package org.chnu.confplatform.back.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.chnu.confplatform.back.model.base.Archivable;
import org.chnu.confplatform.back.model.base.Identifiable;
import org.chnu.confplatform.back.model.base.Role;

import javax.persistence.*;

@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
public class User implements Identifiable, Archivable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(unique = true)
    private String password;

    @Column
    private String email;

    @Column
    private String scienceRank;

    @Column
    private String country;

    @Column
    private String city;

    @Column
    private String work;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean archived;
}
