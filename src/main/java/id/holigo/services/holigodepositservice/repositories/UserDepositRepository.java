package id.holigo.services.holigodepositservice.repositories;

import id.holigo.services.holigodepositservice.domain.UserDeposit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface UserDepositRepository extends JpaRepository<UserDeposit, Long> {

    @Modifying(flushAutomatically = true)
    @Query("UPDATE UserDeposit u SET u.deposit=:newDeposit WHERE u.userId=:userId AND u.deposit=:currentDeposit")
    int updateDeposit(@Param("userId") Long userId,
                      @Param("currentDeposit") BigDecimal currentDeposit,
                      @Param("newDeposit") BigDecimal newDeposit);
}
