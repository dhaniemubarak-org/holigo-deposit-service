package id.holigo.services.holigodepositservice.services;

import id.holigo.services.common.model.DepositDto;
import id.holigo.services.holigodepositservice.domain.DepositStatement;

import java.math.BigDecimal;

public interface DepositStatementService {
    DepositStatement createNewStatement(DepositDto depositDto, BigDecimal currentDeposit);
}
