package com.realtycrmmysql.repository;

import com.realtycrmmysql.domain.Bargain;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Bargain entity.
 */
public interface BargainRepository extends JpaRepository<Bargain,Long> {

}
