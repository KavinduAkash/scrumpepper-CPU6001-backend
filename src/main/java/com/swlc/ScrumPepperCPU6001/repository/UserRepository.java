package com.swlc.ScrumPepperCPU6001.repository;

import com.swlc.ScrumPepperCPU6001.entity.UserEntity;
import com.swlc.ScrumPepperCPU6001.enums.StatusType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * @author hp
 */
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByRefNo(String refNo);
    Optional<UserEntity> findByEmailAndStatusType(String email, StatusType statusType);
    @Query(value = "SELECT * FROM user u WHERE u.ref_no LIKE %?1% OR u.email LIKE %?1%", nativeQuery = true)
    List<UserEntity> searchUser(String keyword);
    @Query(value = "SELECT * FROM user u WHERE u.id IN (SELECT c.user_id FROM corporate_employee c WHERE c.corporate_id=?1 AND c.status_type=\"ACTIVE\") AND (u.ref_no LIKE %?2% OR u.email LIKE %?2%)", nativeQuery = true)
    List<UserEntity> searchUserForProject(long corporateId, String keyword);
}
