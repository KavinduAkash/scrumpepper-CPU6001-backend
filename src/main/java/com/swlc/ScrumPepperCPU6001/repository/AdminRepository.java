package com.swlc.ScrumPepperCPU6001.repository;

import com.swlc.ScrumPepperCPU6001.entity.AdminEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * @author hp
 */
public interface AdminRepository extends JpaRepository<AdminEntity, Long> {
    Optional<AdminEntity> findByUsername(String username);
    Optional<AdminEntity> findByEmail(String email);
    Optional<AdminEntity> findByContactNumber(String contactNumber);
    @Query("SELECT a FROM AdminEntity a WHERE a.statusType<>\"DELETED\"")
    List<AdminEntity> getAllNotDeletedAdmins();
}
