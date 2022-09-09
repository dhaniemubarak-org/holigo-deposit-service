package id.holigo.services.holigodepositservice.repositories;

import id.holigo.services.holigodepositservice.domain.Language;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LanguageRepository extends JpaRepository<Language, Integer> {
    Language findByMessageKeyAndLocale(String messageKey, String locale);
}
