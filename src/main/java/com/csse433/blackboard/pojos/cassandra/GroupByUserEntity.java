package com.csse433.blackboard.pojos.cassandra;

import lombok.Data;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("group_by_username")
@Data
public class GroupByUserEntity {

    @PrimaryKey
    private String username;

    @Column("group_id")
    private String groupId;

}
