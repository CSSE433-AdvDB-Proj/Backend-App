package com.csse433.blackboard.pojos.cassandra;

import com.csse433.blackboard.common.RelationTypeEnum;
import lombok.Data;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author chetzhang
 */
@Table("friend")
@Data
public class FriendRelationEntity {

    @PrimaryKey("username1")
    private String username1;

    @Column("username2")
    private String username2;

    @Column("gmt_create")
    private LocalDateTime gmtCreate;

    @Column("relation")
    private RelationTypeEnum relation;
}
