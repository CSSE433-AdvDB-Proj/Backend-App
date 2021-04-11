package com.csse433.blackboard.auth.service;

import com.csse433.blackboard.auth.dto.UserAccountDto;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author chetzhang
 */
public interface AuthService {

    UserAccountDto findUserByToken(String token);

    void extendExpireTime(String token);
}
