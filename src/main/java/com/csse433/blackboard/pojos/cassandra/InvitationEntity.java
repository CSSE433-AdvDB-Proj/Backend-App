package com.csse433.blackboard.pojos.cassandra;


import lombok.Data;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * @author henryyang
 */
@Data
@Table("invitation")
public class InvitationEntity {

    @PrimaryKey("to_username")
    private String toUsername;

    @Column("group_id")
    private String groupId;

    @Column("from_username")
    private String fromUsername;

    @Column("gmt_create")
    private LocalDateTime gmtCreate;

    @Column("is_friend_request")
    private Boolean isFriendRequest;

}
