package com.swlc.ScrumPepperCPU6001.repository;

import com.swlc.ScrumPepperCPU6001.entity.CorporateEmployeeInvitationEntity;
import com.swlc.ScrumPepperCPU6001.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * @author hp
 */
public interface CorporateEmployeeInvitationRepository extends JpaRepository<CorporateEmployeeInvitationEntity, Long> {
    @Query("SELECT i FROM CorporateEmployeeInvitationEntity i WHERE i.corporateEmployeeEntity.userEntity=:user")
    List<CorporateEmployeeInvitationEntity> getByUser(@Param("user") UserEntity userEntity);
}
