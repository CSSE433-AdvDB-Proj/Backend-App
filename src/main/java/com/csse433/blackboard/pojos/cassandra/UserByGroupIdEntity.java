package com.csse433.blackboard.pojos.cassandra;

import lombok.Data;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * @author chetzhang
 */
@Data
@Table("user_by_group_id")
public class UserByGroupIdEntity {

    @PrimaryKey("group_id")
    private String groupId;

    @Column("username")
    private String username;

    @Column("gmt_join")
    private LocalDateTime gmtJoin;

}