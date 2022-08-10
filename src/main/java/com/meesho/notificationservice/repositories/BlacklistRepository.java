package com.meesho.notificationservice.repositories;

import com.meesho.notificationservice.entity.BlacklistNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface BlacklistRepository extends JpaRepository<BlacklistNumber, Integer> {
    @Modifying
    @Query(value = "DELETE FROM blacklist_numbers WHERE phone_number = :phone_number", nativeQuery = true)
    void whitelistNumber( @Param("phone_number") String number);
}
