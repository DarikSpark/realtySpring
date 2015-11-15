package com.realtycrmmysql.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import java.time.LocalDate;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Bargain.
 */
@Entity
@Table(name = "bargain")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Bargain implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Min(value = 0)
    @Column(name = "day_count")
    private Integer day_count;

    @NotNull
    @Column(name = "coming_date", nullable = false)
    private LocalDate coming_date;

    @Column(name = "note")
    private String note;

    @ManyToOne
    private Client client;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDay_count() {
        return day_count;
    }

    public void setDay_count(Integer day_count) {
        this.day_count = day_count;
    }

    public LocalDate getComing_date() {
        return coming_date;
    }

    public void setComing_date(LocalDate coming_date) {
        this.coming_date = coming_date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Bargain bargain = (Bargain) o;

        if ( ! Objects.equals(id, bargain.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Bargain{" +
            "id=" + id +
            ", day_count='" + day_count + "'" +
            ", coming_date='" + coming_date + "'" +
            ", note='" + note + "'" +
            '}';
    }
}
