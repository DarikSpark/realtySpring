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
 * A Flat.
 */
@Entity
@Table(name = "flat")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Flat implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 1)
    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "description")
    private String description;

    @Column(name = "day_cost")
    private Integer day_cost;

    @Min(value = 1)
    @Column(name = "people_count")
    private Integer people_count;

    @ManyToOne
    private Owner owner;

    @OneToMany(mappedBy = "flat")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Comment> comments = new HashSet<>();

    @OneToMany(mappedBy = "flat")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Bargain> bargains = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDay_cost() {
        return day_cost;
    }

    public void setDay_cost(Integer day_cost) {
        this.day_cost = day_cost;
    }

    public Integer getPeople_count() {
        return people_count;
    }

    public void setPeople_count(Integer people_count) {
        this.people_count = people_count;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public Set<Bargain> getBargains() {
        return bargains;
    }

    public void setBargains(Set<Bargain> bargains) {
        this.bargains = bargains;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Flat flat = (Flat) o;

        if ( ! Objects.equals(id, flat.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Flat{" +
            "id=" + id +
            ", address='" + address + "'" +
            ", description='" + description + "'" +
            ", day_cost='" + day_cost + "'" +
            ", people_count='" + people_count + "'" +
            '}';
    }
}
