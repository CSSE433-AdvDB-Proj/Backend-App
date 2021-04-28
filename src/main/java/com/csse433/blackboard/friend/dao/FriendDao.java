package com.csse433.blackboard.friend.dao;

import com.csse433.blackboard.common.RelationTypeEnum;
import com.csse433.blackboard.pojos.cassandra.FriendRelationEntity;
import com.csse433.blackboard.pojos.cassandra.UserEntity;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.core.query.*;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @author chetzhang
 */
@Component
public class FriendDao {

    @Autowired
    private CassandraTemplate cassandraTemplate;

    public RelationTypeEnum findUserRelation(String username1, String username2) {
        Query query = Query.empty();
        query = query
                .and(new CriteriaDefinition() {
                    @Override
                    public ColumnName getColumnName() {
                        return ColumnName.from("username1");
                    }

                    @Override
                    public Predicate getPredicate() {
                        return new Predicate(Operators.EQ, username1);
                    }
                })
                .and(new CriteriaDefinition() {
                    @Override
                    public ColumnName getColumnName() {
                        return ColumnName.from("username2");
                    }

                    @Override
                    public Predicate getPredicate() {
                        return new Predicate(Operators.EQ, username2);
                    }
                });
        FriendRelationEntity friendRelationEntity = cassandraTemplate.selectOne(query, FriendRelationEntity.class);
        return friendRelationEntity == null ? null : friendRelationEntity.getRelation();
    }

    public void createNewRelation(String fromUsername, String toUsername, RelationTypeEnum type, Date date) {
        if (findUserRelation(fromUsername, toUsername) != null) {
            return;
        }
        FriendRelationEntity friendRelationEntity = new FriendRelationEntity();
        friendRelationEntity.setUsername1(fromUsername);
        friendRelationEntity.setUsername2(toUsername);
        friendRelationEntity.setGmtCreate(LocalDateTime.from(date.toInstant()));
        friendRelationEntity.setRelation(type);
        cassandraTemplate.insert(friendRelationEntity);

        if (type == RelationTypeEnum.FRIEND) {
            FriendRelationEntity friendRelationEntityReversed = new FriendRelationEntity();
            friendRelationEntityReversed.setRelation(type);
            friendRelationEntityReversed.setUsername1(toUsername);
            friendRelationEntityReversed.setUsername2(fromUsername);
            friendRelationEntityReversed.setGmtCreate(LocalDateTime.from(date.toInstant()));
            cassandraTemplate.insert(friendRelationEntityReversed);
        }
    }

    public List<String> getFriendList(String username) {
        Query query = Query.empty();

        query = query
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
                .sort(Sort.by("username2").ascending());


        return cassandraTemplate.select(query, String.class);

    }

    public List<String> findFriendFuzzy(String currentUsername, String likeUsername) {
        Query query = Query.empty();

        query = query
                .columns(Columns.from("username2"))
                .and(new CriteriaDefinition() {
                    @Override
                    public ColumnName getColumnName() {
                        return ColumnName.from("username1");
                    }

                    @Override
                    public Predicate getPredicate() {
                        return new Predicate(Operators.EQ, currentUsername);
                    }
                })
                .and(new CriteriaDefinition() {
                    @Override
                    public ColumnName getColumnName() {
                        return ColumnName.from("username2");
                    }

                    @Override
                    public Predicate getPredicate() {
                        return new Predicate(Operators.LIKE, likeUsername);
                    }
                })
                .sort(Sort.by("username2").ascending());


        return cassandraTemplate.select(query, String.class);

    }


}
