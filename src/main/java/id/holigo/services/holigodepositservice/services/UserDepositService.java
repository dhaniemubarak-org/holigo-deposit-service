package id.holigo.services.holigodepositservice.services;

import id.holigo.services.common.model.DepositDto;

public interface UserDepositService {

    void createUserDeposit(Long userId);

    DepositDto credit(DepositDto depositDto) throws Exception;

    DepositDto debit(DepositDto depositDto) throws Exception;
}
