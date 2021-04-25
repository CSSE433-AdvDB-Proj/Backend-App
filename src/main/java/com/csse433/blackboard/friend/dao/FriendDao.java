package com.csse433.blackboard.friend.dao;

import com.csse433.blackboard.common.RelationTypeEnum;
import com.csse433.blackboard.pojos.cassandra.FriendRelationEntity;
import com.csse433.blackboard.pojos.cassandra.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.core.query.ColumnName;
import org.springframework.data.cassandra.core.query.Columns;
import org.springframework.data.cassandra.core.query.CriteriaDefinition;
import org.springframework.data.cassandra.core.query.Query;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @author chetzhang
 */
@Component
public class FriendDao {

    @Autowired
    private CassandraTemplate cassandraTemplate;


    public List<String> getFriendList(String username) {
        Query query = Query.empty();

        query
                .columns(Columns.from("username2"))
                .and(new CriteriaDefinition() {
                    @Override
                    public ColumnName getColumnName() {
                        return ColumnName.from("username1");
                    }

                    @Override
                    public Predicate getPredicate() {
                        return new Predicate(Operators.EQ, username);
                    }
                })
                .sort(Sort.by("username1").ascending());


        return cassandraTemplate.select(query, String.class);

    }

    public RelationTypeEnum findUserRelation(String fromUsername, String toUsername) {
        String cql = String.format("SELECT * FROM blackboard.friend WHERE username1 = '%s' and username2 = '%s';", fromUsername, toUsername);
        FriendRelationEntity friendRelationEntity = cassandraTemplate.selectOne(cql, FriendRelationEntity.class);
        return friendRelationEntity == null ? null : friendRelationEntity.getRelation();
    }

    public void createNewRelation(String fromUsername, String toUsername, RelationTypeEnum type, Date date) {
        if (findUserRelation(fromUsername, toUsername) != null) {
            return;
        }
        FriendRelationEntity friendRelationEntity = new FriendRelationEntity();
        friendRelationEntity.setUsername1(fromUsername);
        friendRelationEntity.setUsername2(toUsername);
        friendRelationEntity.setGmtCreate(date);
        friendRelationEntity.setRelation(type);
        cassandraTemplate.insert(friendRelationEntity);

        if (type == RelationTypeEnum.FRIEND) {
            FriendRelationEntity friendRelationEntityReversed = new FriendRelationEntity();
            friendRelationEntityReversed.setRelation(type);
            friendRelationEntityReversed.setUsername1(toUsername);
            friendRelationEntityReversed.setUsername2(fromUsername);
            friendRelationEntityReversed.setGmtCreate(date);
            cassandraTemplate.insert(friendRelationEntityReversed);
        }
    }
}
