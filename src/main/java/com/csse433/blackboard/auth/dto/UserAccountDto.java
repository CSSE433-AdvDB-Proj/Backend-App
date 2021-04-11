package com.csse433.blackboard.auth.dto;

import lombok.Data;

/**
 * User info dto
 * @author chetzhang
 */
@Data
public class UserAccountDto {

    private String username;

    private String password;

    private String email;

    private String firstName;

    private String lastName;

    private String nickname;
}
