package com.csse433.blackboard.pojos.cassandra;

import lombok.Data;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Table("group_by_group_id")
public class GroupEntity {

    @PrimaryKey("group_id")
    private String groupId;

    @Column("group_name")
    private String groupName;

    @Column("create_by")
    private String createBy;

    @Column("gmt_create")
    private LocalDateTime gmtCreate;

}
