package org.chnu.confplatform.back.model;


import org.apache.commons.lang3.builder.EqualsBuilder;
import org.chnu.confplatform.back.model.base.Identifiable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "auth_data")
public class AuthData implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = true, name = "client_secret_key")
    private String clientSecretKey;

    @Override
    public Integer getId() {
        return Math.toIntExact(this.id);
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }


    public String getClientSecretKey() {
        return this.clientSecretKey;
    }

    public void setClientSecretKey(String clientSecretKey) {
        this.clientSecretKey = clientSecretKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        AuthData authData = (AuthData) o;
        return new EqualsBuilder()
                .append(id, authData.id)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
