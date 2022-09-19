package id.holigo.services.holigodepositservice.services;

import id.holigo.services.common.model.DeviceTypeEnum;
import id.holigo.services.common.model.TransactionDto;

import java.math.BigDecimal;

public interface DepositTransactionService {

    TransactionDto createTransaction(Long userId, BigDecimal nominal, DeviceTypeEnum deviceTypeEnum);
}
