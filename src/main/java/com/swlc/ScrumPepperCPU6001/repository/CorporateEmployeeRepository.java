package com.swlc.ScrumPepperCPU6001.repository;

import com.swlc.ScrumPepperCPU6001.entity.CorporateEmployeeEntity;
import com.swlc.ScrumPepperCPU6001.entity.CorporateEntity;
import com.swlc.ScrumPepperCPU6001.entity.UserEntity;
import com.swlc.ScrumPepperCPU6001.enums.CorporateAccessStatusType;
import com.swlc.ScrumPepperCPU6001.enums.CorporateAccessType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author hp
 */
public interface CorporateEmployeeRepository extends JpaRepository<CorporateEmployeeEntity, Long> {
    Optional<CorporateEmployeeEntity> findByUserEntityAndCorporateEntityAndCorporateAccessTypeAndStatusType(
            UserEntity userEntity, CorporateEntity corporateEntity, CorporateAccessType corporateAccessType,
            CorporateAccessStatusType statusType);
    Optional<CorporateEmployeeEntity> findByUserEntityAndCorporateEntityAndStatusType(
            UserEntity userEntity, CorporateEntity corporateEntity, CorporateAccessStatusType statusType);
    Optional<CorporateEmployeeEntity> findByUserEntityAndCorporateEntity(UserEntity userEntity, CorporateEntity corporateEntity);
}
