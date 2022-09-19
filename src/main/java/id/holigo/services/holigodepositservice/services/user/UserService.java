package id.holigo.services.holigodepositservice.services.user;

import id.holigo.services.common.model.UserDtoForUser;
import org.springframework.http.HttpStatus;

public interface UserService {

    UserDtoForUser getUserById(Long userId);

    HttpStatus pinCheckAvailability(Long userId);
}
