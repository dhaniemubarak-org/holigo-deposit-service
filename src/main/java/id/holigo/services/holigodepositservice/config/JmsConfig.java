package id.holigo.services.holigodepositservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

@Configuration
public class JmsConfig {

    public static final String DEBIT_DEPOSIT = "debit-deposit";
    public static final String CREDIT_DEPOSIT = "credit-deposit";
    public static final String CREATE_DEPOSIT_ACCOUNT_STATEMENT = "create-deposit-account-statement";
    public static final String CREATE_NEW_TRANSACTION = "create-new-transaction";

    public static final String GET_DETAIL_FARE_PRODUCT = "get-detail-fare-product";

    @Bean
    public MessageConverter jacksonJmsMessageConverter(ObjectMapper objectMapper) {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        converter.setObjectMapper(objectMapper);
        return converter;
    }
}
