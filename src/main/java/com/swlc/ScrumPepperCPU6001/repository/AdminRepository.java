package com.swlc.ScrumPepperCPU6001.repository;

import com.swlc.ScrumPepperCPU6001.constant.NativeQueryConstant;
import com.swlc.ScrumPepperCPU6001.entity.AdminEntity;
import com.swlc.ScrumPepperCPU6001.enums.StatusType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author hp
 */

@Repository
public interface AdminRepository extends JpaRepository<AdminEntity, Long> {
    Optional<AdminEntity> findByUsername(String username);
    Optional<AdminEntity> findByEmail(String email);
    Optional<AdminEntity> findByContactNumber(String contactNumber);
    @Query(value = NativeQueryConstant.FILTER_ADMIN_BY_NOT_EQUAL_STATUS, nativeQuery = true)
    List<AdminEntity> filerAdminByNotEqualStatusType(StatusType statusType);
}
