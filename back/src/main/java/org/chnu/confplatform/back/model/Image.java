package org.chnu.confplatform.back.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.chnu.confplatform.back.model.base.Archivable;
import org.chnu.confplatform.back.model.base.Identifiable;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "image")
@Data
@NoArgsConstructor
public class Image implements Identifiable, Archivable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "image_name")
    private String imageName;

    @Column(name = "mime_type")
    private String mimeType;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean archived;

    @Transient
    private MultipartFile imageMultipartFile;

}
