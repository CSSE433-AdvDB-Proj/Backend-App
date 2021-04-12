package com.csse433.blackboard.auth.service;

import com.csse433.blackboard.auth.dto.UserAccountDto;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

/**
 * @author chetzhang
 */
@Service
public interface AuthService {

    /**
     * Find the corresponding user for a given token. Returns null if the token is not found.
     * @param token Token in the request header.
     * @return UserAccountDto
     */
    UserAccountDto findUserByToken(String token);

    /**
     * Extends the token time on user's activity.
     *
     * @param token Token in the request header.
     */
    void extendExpireTime(String token);

    /**
     * Registers a user and returns true. Returns false if the username is already registered.
     * @param userAccountDto
     * @return
     */
    boolean registerUser(UserAccountDto userAccountDto);


    /**
     * Deletes the token when a user tries to log out.
     *
     * @param token Token in the request header.
     */
    void deleteToken(String token);


    /**
     * Update account information
     * @param userAccountDto
     * @return
     */
    boolean updateUser(String token, UserAccountDto userAccountDto);

    /**
     * Login.
     * @param username
     * @param password
     * @param response
     * @return
     */
    boolean login(String username, String password, HttpServletResponse response);
}
