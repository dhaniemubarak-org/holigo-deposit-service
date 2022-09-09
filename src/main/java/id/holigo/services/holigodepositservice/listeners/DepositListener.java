package id.holigo.services.holigodepositservice.listeners;

import id.holigo.services.common.model.DepositDto;
import id.holigo.services.holigodepositservice.config.JmsConfig;
import id.holigo.services.holigodepositservice.services.UserDepositService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.JMSException;
import javax.jms.Message;

@Slf4j
@Component
public class DepositListener {

    private JmsTemplate jmsTemplate;

    private UserDepositService userDepositService;

    @Autowired
    public void setUserDepositService(UserDepositService userDepositService) {
        this.userDepositService = userDepositService;
    }

    @Autowired
    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @JmsListener(destination = JmsConfig.CREDIT_DEPOSIT)
    public void listenForCredit(@Payload DepositDto depositDto, @Headers MessageHeaders headers, Message message) throws JMSException {
        try {
            DepositDto credit = userDepositService.credit(depositDto);
            jmsTemplate.convertAndSend(message.getJMSReplyTo(), credit);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        jmsTemplate.convertAndSend(message.getJMSReplyTo(), depositDto);
    }

    @JmsListener(destination = JmsConfig.DEBIT_DEPOSIT)
    public void listenForDebit(@Payload DepositDto depositDto, @Headers MessageHeaders headers, Message message) throws JMSException {
        try {
            DepositDto debit = userDepositService.debit(depositDto);
            jmsTemplate.convertAndSend(message.getJMSReplyTo(), debit);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        jmsTemplate.convertAndSend(message.getJMSReplyTo(), depositDto);
    }
}
