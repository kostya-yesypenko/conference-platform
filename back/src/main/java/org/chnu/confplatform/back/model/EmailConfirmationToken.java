package org.chnu.confplatform.back.model;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.chnu.confplatform.back.model.base.Identifiable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@Table(name = "email_confirmation")
public class EmailConfirmationToken implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "token")
    private String token;

    @Column(name = "expired_at")
    private LocalDateTime expiredAt;

    @OneToOne
    private User user;
}
