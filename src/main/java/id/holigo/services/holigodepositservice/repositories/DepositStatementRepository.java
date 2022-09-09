package id.holigo.services.holigodepositservice.repositories;

import id.holigo.services.holigodepositservice.domain.DepositStatement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DepositStatementRepository extends JpaRepository<DepositStatement, UUID> {
    Page<DepositStatementRepository> findAllByUserId(Long userId, Pageable pageable);
}
