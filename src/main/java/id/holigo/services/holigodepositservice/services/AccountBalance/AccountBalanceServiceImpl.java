package id.holigo.services.holigodepositservice.services.AccountBalance;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import id.holigo.services.common.model.DepositDto;
import id.holigo.services.holigodepositservice.config.JmsConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;

@Service
public class AccountBalanceServiceImpl implements AccountBalanceService {
    private JmsTemplate jmsTemplate;

    private ObjectMapper objectMapper;

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Autowired
    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @Override
    public DepositDto createAccountStatement(DepositDto depositDto) throws JMSException, JsonProcessingException {
        Message received = jmsTemplate.sendAndReceive(JmsConfig.CREATE_ACCOUNT_STATEMENT, session -> {
            Message message;
            try {
                message = session.createTextMessage(objectMapper.writeValueAsString(depositDto));
                message.setStringProperty("_type", "id.holigo.services.common.model.DepositDto");
            } catch (JsonProcessingException e) {
                throw new JMSException(e.getMessage());
            }
            return message;
        });
        if (received == null) {
            return depositDto;
        }
        return objectMapper.readValue(received.getBody(String.class),
                DepositDto.class);
    }
}
