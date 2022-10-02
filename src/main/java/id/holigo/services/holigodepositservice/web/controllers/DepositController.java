package id.holigo.services.holigodepositservice.web.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import id.holigo.services.common.model.DepositDto;
import id.holigo.services.common.model.DeviceTypeEnum;
import id.holigo.services.common.model.TransactionDto;
import id.holigo.services.holigodepositservice.services.DepositService;
import id.holigo.services.holigodepositservice.services.DepositTransactionService;
import id.holigo.services.common.model.DepositTransactionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.jms.JMSException;

@RestController
public class DepositController {

    private DepositService depositService;
    private final static String TRANSACTION_PATH = "/api/v1/transactions/{id}";

    private DepositTransactionService depositTransactionService;

    @Autowired
    public void setDepositTransactionService(DepositTransactionService depositTransactionService) {
        this.depositTransactionService = depositTransactionService;
    }

    @Autowired
    public void setDepositService(DepositService depositService) {
        this.depositService = depositService;
    }

//    @PostMapping("/api/v1/deposit/debit")
//    public ResponseEntity<DepositDto> debit(@RequestBody DepositDto depositDto) throws JMSException, JsonProcessingException {
//        return new ResponseEntity<>(depositService.debit(depositDto), HttpStatus.CREATED);
//    }
//
//    @PostMapping("/api/v1/deposit/credit")
//    public ResponseEntity<DepositDto> credit(@RequestBody DepositDto depositDto) throws JMSException, JsonProcessingException {
//        return new ResponseEntity<>(depositService.credit(depositDto), HttpStatus.CREATED);
//    }

    @PostMapping("/api/v1/deposit/book")
    public ResponseEntity<HttpStatus> createBook(@RequestBody DepositTransactionDto depositTransactionDto,
                                                 @RequestHeader("user-id") Long userId,
                                                 @RequestHeader("device-type") DeviceTypeEnum deviceType) {
        TransactionDto transactionDto = depositTransactionService.createTransaction(userId, depositTransactionDto.getNominal(), deviceType);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(UriComponentsBuilder.fromPath(TRANSACTION_PATH)
                .buildAndExpand(transactionDto.getId().toString()).toUri());
        return new ResponseEntity<>(httpHeaders, HttpStatus.CREATED);
    }
}
