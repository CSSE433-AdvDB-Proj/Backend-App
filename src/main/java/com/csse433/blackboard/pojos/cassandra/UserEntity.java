package com.csse433.blackboard.pojos.cassandra;

import lombok.Data;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

/**
 * @author chetzhang
 */
@Data
@Table("user")
public class UserEntity {

    @PrimaryKey
    private String username;

    @Column("email")
    private String email;

    @Column("first_name")
    private String firstName;

    @Column("last_name")
    private String lastName;

    @Column("nickname")
    private String nickname;

    @Column("password_hash")
    private String passwordHash;

    @Column("password_salt")
    private String passwordSalt;
}
