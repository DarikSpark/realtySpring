package com.realtycrmmysql.repository;

import com.realtycrmmysql.domain.Flat;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Flat entity.
 */
public interface FlatRepository extends JpaRepository<Flat,Long> {

}
