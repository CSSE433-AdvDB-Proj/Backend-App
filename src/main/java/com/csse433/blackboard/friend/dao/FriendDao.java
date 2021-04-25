package com.csse433.blackboard.friend.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.core.query.ColumnName;
import org.springframework.data.cassandra.core.query.Columns;
import org.springframework.data.cassandra.core.query.CriteriaDefinition;
import org.springframework.data.cassandra.core.query.Query;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

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
                .sort(Sort.by("username2").ascending());


        return cassandraTemplate.select(query, String.class);

    }

    public List<String> findFriendFuzzy(String currentUsername, String likeUsername) {
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
