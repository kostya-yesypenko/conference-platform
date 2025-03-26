package org.chnu.confplatform.back.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.chnu.confplatform.back.model.base.Archivable;
import org.chnu.confplatform.back.model.base.Identifiable;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "conferention")
@Data
@NoArgsConstructor
public class Conferention implements Identifiable, Archivable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String topic;

    @Column
    private String description;

    @Column
    private Date date_from;

    @Column
    private Date date_to;

    @Column
    private String speakers;

    @Column
    private String countries;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean archived;

}
