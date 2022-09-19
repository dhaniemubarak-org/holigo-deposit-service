package id.holigo.services.holigodepositservice.services.user;

import id.holigo.services.common.model.UserDtoForUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private UserServiceFeignClient userServiceFeignClient;

    @Autowired
    public void setUserServiceFeignClient(UserServiceFeignClient userServiceFeignClient) {
        this.userServiceFeignClient = userServiceFeignClient;
    }

    @Override
    public UserDtoForUser getUserById(Long userId) {
        log.info("getUserById is running..");
        return userServiceFeignClient.getUser(userId, userId).getBody();
    }

    @Override
    public HttpStatus pinCheckAvailability(Long userId) {
        log.info("pinCheckAvailability is running...");
        ResponseEntity<UserDtoForUser> user = userServiceFeignClient.pinCheckAvailability(userId, userId);
        return user.getStatusCode();
    }
}
