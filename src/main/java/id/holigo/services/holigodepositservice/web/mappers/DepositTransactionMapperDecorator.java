package id.holigo.services.holigodepositservice.web.mappers;

import id.holigo.services.common.model.TransactionDto;
import id.holigo.services.common.model.UserDtoForUser;
import id.holigo.services.holigodepositservice.domain.DepositTransaction;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public abstract class DepositTransactionMapperDecorator implements DepositTransactionMapper {

    private DepositTransactionMapper depositTransactionMapper;

    @Autowired
    public void setDepositTransactionMapper(DepositTransactionMapper depositTransactionMapper) {
        this.depositTransactionMapper = depositTransactionMapper;
    }

    @Override
    public TransactionDto depositTransactionToTransactionDto(DepositTransaction depositTransaction, UserDtoForUser user) {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance();
        TransactionDto transactionDto = this.depositTransactionMapper.depositTransactionToTransactionDto(depositTransaction, user);
        transactionDto.setAdminAmount(BigDecimal.ZERO);
        transactionDto.setTransactionType("HTD");
        transactionDto.setServiceId(1380);
        transactionDto.setProductId(1380);
        transactionDto.setPointAmount(BigDecimal.valueOf(0.0));
        transactionDto.setTransactionId(depositTransaction.getId().toString());
        transactionDto.setIndexProduct("Top Up|Holi Cash|" + formatter.format(depositTransaction.getNominal()) + "|" + user.getPhoneNumber());
        transactionDto.setIndexUser(user.getName() + "|" + user.getPhoneNumber() + "|" + user.getEmail());
        return transactionDto;
    }
}
