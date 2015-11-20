package com.realtycrmmysql.repository;

import com.realtycrmmysql.domain.Owner;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Owner entity.
 */
public interface OwnerRepository extends JpaRepository<Owner,Long> {

}
