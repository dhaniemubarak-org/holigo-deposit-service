package id.holigo.services.holigodepositservice.services.user;

import id.holigo.services.common.model.UserDtoForUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@FeignClient(value = "holigo-user-service")
public interface UserServiceFeignClient {

    String GET_USER_BY_ID = "/api/v1/users/{id}";

    String CHECK_PIN_HAS_SET = "/api/v1/users/{id}/pin";

    @RequestMapping(method = RequestMethod.GET, value = GET_USER_BY_ID)
    ResponseEntity<UserDtoForUser> getUser(@PathVariable Long id, @RequestHeader("user-id") Long userId);

    @RequestMapping(method = RequestMethod.GET, value = CHECK_PIN_HAS_SET)
    ResponseEntity<UserDtoForUser> pinCheckAvailability(@PathVariable("id") Long id, @RequestHeader("user-id") Long userId);
}
