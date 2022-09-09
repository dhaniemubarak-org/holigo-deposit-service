package id.holigo.services.holigodepositservice.web.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import id.holigo.services.common.model.DepositDto;
import id.holigo.services.holigodepositservice.services.DepositService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.JMSException;

@Slf4j
@RestController
public class DepositController {

    private DepositService depositService;

    @Autowired
    public void setDepositService(DepositService depositService) {
        this.depositService = depositService;
    }

    @PostMapping("/api/v1/deposit/debit")
    public ResponseEntity<DepositDto> debit(@RequestBody DepositDto depositDto) throws JMSException, JsonProcessingException {
        return new ResponseEntity<>(depositService.debit(depositDto), HttpStatus.CREATED);
    }

    @PostMapping("/api/v1/deposit/credit")
    public ResponseEntity<DepositDto> credit(@RequestBody DepositDto depositDto) throws JMSException, JsonProcessingException {
        return new ResponseEntity<>(depositService.credit(depositDto), HttpStatus.CREATED);
    }
}
