package id.holigo.services.holigodepositservice.services.AccountBalance;

import com.fasterxml.jackson.core.JsonProcessingException;
import id.holigo.services.common.model.DepositDto;

import javax.jms.JMSException;

public interface AccountBalanceService {
    DepositDto createAccountStatement(DepositDto depositDto) throws JMSException, JsonProcessingException;
}
