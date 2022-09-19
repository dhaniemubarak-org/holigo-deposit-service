package id.holigo.services.holigodepositservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {


    public static final String UPDATE_DEPOSIT_TRANSACTION = "update-deposit-transaction";

    @Bean
    public NewTopic userUpdateEmailStatus() {
        return TopicBuilder.name(UPDATE_DEPOSIT_TRANSACTION).build();
    }
}
