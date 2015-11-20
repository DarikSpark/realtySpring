package com.realtycrmmysql.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Owner.
 */
@Entity
@Table(name = "owner")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Owner implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 1)
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "phone")
    private String phone;

    @Column(name = "bank_account")
    private Integer bank_account;

    @OneToMany(mappedBy = "owner")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Flat> flats = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getBank_account() {
        return bank_account;
    }

    public void setBank_account(Integer bank_account) {
        this.bank_account = bank_account;
    }

    public Set<Flat> getFlats() {
        return flats;
    }

    public void setFlats(Set<Flat> flats) {
        this.flats = flats;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Owner owner = (Owner) o;

        if ( ! Objects.equals(id, owner.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Owner{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", phone='" + phone + "'" +
            ", bank_account='" + bank_account + "'" +
            '}';
    }
}
