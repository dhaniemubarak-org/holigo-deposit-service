package id.holigo.services.holigodepositservice.repositories;

import id.holigo.services.holigodepositservice.domain.DepositTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepositTransactionRepository extends JpaRepository<DepositTransaction, Long> {

}
