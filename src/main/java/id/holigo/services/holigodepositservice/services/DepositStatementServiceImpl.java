package id.holigo.services.holigodepositservice.services;

import id.holigo.services.common.model.DepositDto;
import id.holigo.services.holigodepositservice.domain.DepositStatement;
import id.holigo.services.holigodepositservice.repositories.DepositStatementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class DepositStatementServiceImpl implements DepositStatementService {

    private DepositStatementRepository depositStatementRepository;

    @Autowired
    public void setDepositStatementRepository(DepositStatementRepository depositStatementRepository) {
        this.depositStatementRepository = depositStatementRepository;
    }

    @Override
    public DepositStatement createNewStatement(DepositDto depositDto, BigDecimal currentDeposit) {
        DepositStatement depositStatement = new DepositStatement();
        depositStatement.setDeposit(currentDeposit);
        depositStatement.setCredit(depositDto.getCreditAmount());
        depositStatement.setDebit(depositDto.getDebitAmount());
        depositStatement.setUserId(depositDto.getUserId());
        depositStatement.setTransactionId(depositDto.getTransactionId());
        depositStatement.setPaymentId(depositDto.getPaymentId());
        depositStatement.setInformationIndex(depositDto.getInformationIndex());
        depositStatement.setInformationValue(depositDto.getInformationValue());
        depositStatement.setTransactionType(depositDto.getTransactionType());
        depositStatement.setInvoiceNumber(depositDto.getInvoiceNumber());
        return depositStatementRepository.save(depositStatement);
    }

}
