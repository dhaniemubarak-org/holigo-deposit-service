package id.holigo.services.holigodepositservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import id.holigo.services.common.model.*;
import id.holigo.services.holigodepositservice.domain.DepositTransaction;
import id.holigo.services.holigodepositservice.repositories.DepositTransactionRepository;
import id.holigo.services.holigodepositservice.services.transaction.TransactionService;
import id.holigo.services.holigodepositservice.services.user.UserService;
import id.holigo.services.holigodepositservice.web.exceptions.ForbiddenException;
import id.holigo.services.holigodepositservice.web.exceptions.NotAcceptableException;
import id.holigo.services.holigodepositservice.web.mappers.DepositTransactionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import java.math.BigDecimal;

@Service
public class DepositTransactionServiceImpl implements DepositTransactionService {


    private UserService userService;

    private DepositTransactionRepository depositTransactionRepository;

    private DepositTransactionMapper depositTransactionMapper;

    private TransactionService transactionService;

    @Autowired
    public void setTransactionService(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Autowired
    public void setDepositTransactionMapper(DepositTransactionMapper depositTransactionMapper) {
        this.depositTransactionMapper = depositTransactionMapper;
    }

    @Autowired
    public void setDepositTransactionRepository(DepositTransactionRepository depositTransactionRepository) {
        this.depositTransactionRepository = depositTransactionRepository;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public TransactionDto createTransaction(Long userId, BigDecimal nominal, DeviceTypeEnum deviceTypeEnum) {

        // Get user
        UserDtoForUser user = userService.getUserById(userId);

        if (!user.getEmailStatus().equals(EmailStatusEnum.CONFIRMED)) {
            throw new ForbiddenException("Verifikasi email dulu yuk.");
        }

        if (!userService.pinCheckAvailability(userId).equals(HttpStatus.OK)) {
            throw new NotAcceptableException("Buat pin dulu yuk.");
        }

        DepositTransaction depositTransaction = DepositTransaction.builder()
                .userId(userId)
                .device(deviceTypeEnum.toString())
                .fareAmount(nominal).nominal(nominal).ntaAmount(nominal).phoneNumber(user.getPhoneNumber())
                .name(user.getName())
                .paymentStatus(PaymentStatusEnum.SELECTING_PAYMENT).orderStatus(OrderStatusEnum.BOOKED).build();
        depositTransactionRepository.save(depositTransaction);

        TransactionDto transactionDto = depositTransactionMapper.depositTransactionToTransactionDto(depositTransaction, user);

        try {
            transactionDto = transactionService.createNewTransaction(transactionDto);
            depositTransaction.setTransactionId(transactionDto.getId());
            depositTransaction.setExpiredAt(transactionDto.getExpiredAt());
            depositTransaction.setInvoiceNumber(transactionDto.getInvoiceNumber());
            depositTransactionRepository.save(depositTransaction);
        } catch (JMSException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return transactionDto;
    }

}
