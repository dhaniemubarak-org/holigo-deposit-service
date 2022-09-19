package id.holigo.services.common.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserParentDtoForUser implements Serializable {

    static final long serialVersionUID = -65181210L;
    private Long id;
    private Long officialId;
    private String name;
    private String referral;
}
