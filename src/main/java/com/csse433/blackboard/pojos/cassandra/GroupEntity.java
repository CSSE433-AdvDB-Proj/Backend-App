package com.csse433.blackboard.pojos.cassandra;

import lombok.Data;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Table("groupByGroupId")
public class GroupEntity {

    @PrimaryKey
    private String groupId;

    @Column("groupName")
    private String groupName;

    @Column("createBy")
    private String createBy;

    @Column("gmt_create")
    private LocalDateTime gmtCreate;

}
