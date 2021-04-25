package com.csse433.blackboard.auth.service;

import com.csse433.blackboard.auth.dto.UserAccountDto;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

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
    boolean updateUserInfo(String token, UserAccountDto userAccountDto);

    /**
     * update account password
     * @param username username
     * @param password password
     * @return
     */
    boolean updateUserPassword(String username, String password);

    /**
     * Login.
     * @param username
     * @param password
     * @param response
     * @return
     */
    UserAccountDto login(String username, String password, HttpServletResponse response);

    /**
     * Check if provided users exist.
     *
     * @param username @return
     * @return
     */
    String userExists(String... username);

    /**
     * Check if the password is correct
     *
     * @param username username
     * @param password password
     * @return whether the password is correct for this username
     */
    boolean verifyPassword(String username, String password);

    /**
     * Check if the password meets certain conditions
     *
     * @param password the new password
     * @return whether the password meets conditions
     */
    boolean checkPasswordConditions(String password);

    /**
     * Get user from username
     * @param username
     * @return
     */
    UserAccountDto getUserFromUsername(String username);
}
