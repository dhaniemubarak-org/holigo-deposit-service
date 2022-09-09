package id.holigo.services.holigodepositservice.services;

import id.holigo.services.common.model.DepositDto;
import id.holigo.services.holigodepositservice.domain.DepositStatement;
import id.holigo.services.holigodepositservice.domain.UserDeposit;
import id.holigo.services.holigodepositservice.repositories.UserDepositRepository;
import id.holigo.services.holigodepositservice.services.AccountBalance.AccountBalanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Slf4j
@Service
public class UserDepositServiceImpl implements UserDepositService {

    private UserDepositRepository userDepositRepository;

    private DepositStatementService depositStatementService;

    private AccountBalanceService accountBalanceService;

    @Autowired
    public void setAccountBalanceService(AccountBalanceService accountBalanceService) {
        this.accountBalanceService = accountBalanceService;
    }

    @Autowired
    public void setUserPointRepository(UserDepositRepository userDepositRepository) {
        this.userDepositRepository = userDepositRepository;
    }

    @Autowired
    public void setPointHistoryService(DepositStatementService depositStatementService) {
        this.depositStatementService = depositStatementService;
    }

    @Override
    public void createUserDeposit(Long userId) {
        boolean isExists = userDepositRepository.existsById(userId);
        if (!isExists) {
            UserDeposit userDeposit = new UserDeposit();
            userDeposit.setUserId(userId);
            userDeposit.setDeposit(BigDecimal.ZERO);
            userDepositRepository.save(userDeposit);
        }
        userDepositRepository.getById(userId);
    }

    @Transactional
    @Override
    public DepositDto credit(DepositDto depositDto) throws Exception {
        depositDto.setIsValid(false);
        if (depositDto.getCreditAmount().compareTo(BigDecimal.ZERO) == 0) {
            return depositDto;
        }
        if (depositDto.getDebitAmount().compareTo(BigDecimal.ZERO) > 0) {
            return depositDto;
        }
        boolean isExists = userDepositRepository.existsById(depositDto.getUserId());
        if (!isExists) {
            createUserDeposit(depositDto.getUserId());
        }
        UserDeposit userDeposit = userDepositRepository.getById(depositDto.getUserId());
        BigDecimal currentDeposit = userDeposit.getDeposit();
        BigDecimal newDeposit = currentDeposit.add(depositDto.getCreditAmount());
        updateDeposit(depositDto, userDeposit, currentDeposit, newDeposit);
        return depositDto;
    }

    private void updateDeposit(DepositDto depositDto, UserDeposit userDeposit, BigDecimal currentDeposit, BigDecimal newDeposit) throws Exception {
        int isUpdate = userDepositRepository.updateDeposit(userDeposit.getUserId(), currentDeposit, newDeposit);
        if (isUpdate == 0) {
            throw new RuntimeException();
        }
        DepositStatement depositStatement = depositStatementService.createNewStatement(depositDto, currentDeposit);
        if (depositStatement.getId() != null) {
            depositDto.setDeposit(currentDeposit);
            DepositDto resultAccountStatement = accountBalanceService.createAccountStatement(depositDto);
            if (!resultAccountStatement.getIsValid()) {
                throw new RuntimeException("Invalid createAccountStatement");
            }
            depositDto.setIsValid(resultAccountStatement.getIsValid());
            depositDto.setDeposit(resultAccountStatement.getDeposit());
        }
    }

    @Transactional
    @Override
    public DepositDto debit(DepositDto depositDto) throws Exception {
        depositDto.setIsValid(false);
        if (depositDto.getDebitAmount().compareTo(BigDecimal.ZERO) == 0) {
            return depositDto;
        }
        if (depositDto.getCreditAmount().compareTo(BigDecimal.ZERO) > 0) {
            return depositDto;
        }
        boolean isExists = userDepositRepository.existsById(depositDto.getUserId());
        if (!isExists) {
            createUserDeposit(depositDto.getUserId());
        }
        UserDeposit userDeposit = userDepositRepository.getById(depositDto.getUserId());
        if (userDeposit.getDeposit().compareTo(depositDto.getDebitAmount()) < 0) {
            depositDto.setMessage("Saldo tidak cukup");
            return depositDto;
        }
        BigDecimal currentDeposit = userDeposit.getDeposit();
        BigDecimal newDeposit = currentDeposit.subtract(depositDto.getDebitAmount());
        updateDeposit(depositDto, userDeposit, currentDeposit, newDeposit);
        return depositDto;
    }
}
