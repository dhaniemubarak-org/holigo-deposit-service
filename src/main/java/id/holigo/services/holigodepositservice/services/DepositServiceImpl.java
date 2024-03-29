package id.holigo.services.holigodepositservice.services;

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
public class DepositServiceImpl implements DepositService {

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
    public DepositDto credit(DepositDto depositDto) throws JMSException, JsonProcessingException {

        Message received = jmsTemplate.sendAndReceive(JmsConfig.CREDIT_DEPOSIT, session -> {
            Message message;
            try {
                message = session.createTextMessage(objectMapper.writeValueAsString(depositDto));
                message.setStringProperty("_type", "id.holigo.services.common.model.DepositDto");
            } catch (JsonProcessingException e) {
                throw new JMSException(e.getMessage());
            }
            return message;
        });
        assert received != null;
        return objectMapper.readValue(received.getBody(String.class),
                DepositDto.class);
    }

    @Override
    public DepositDto debit(DepositDto depositDto) throws JMSException, JsonProcessingException {
        Message received = jmsTemplate.sendAndReceive(JmsConfig.DEBIT_DEPOSIT, session -> {
            Message message;
            try {
                message = session.createTextMessage(objectMapper.writeValueAsString(depositDto));
                message.setStringProperty("_type", "id.holigo.services.common.model.DepositDto");
            } catch (JsonProcessingException e) {
                throw new JMSException(e.getMessage());
            }
            return message;
        });
        assert received != null;
        return objectMapper.readValue(received.getBody(String.class),
                DepositDto.class);
    }
}
