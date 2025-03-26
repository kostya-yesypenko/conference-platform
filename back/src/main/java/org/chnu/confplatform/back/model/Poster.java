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
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "poster")
@Data
@NoArgsConstructor
public class Poster implements Identifiable, Archivable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String topic;

    @Column
    private String description;

    @OneToOne
    @JoinColumn(name = "image_id")
    private Image image;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean archived;
}
