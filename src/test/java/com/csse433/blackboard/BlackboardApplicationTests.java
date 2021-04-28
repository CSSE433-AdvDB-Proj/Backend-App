package com.csse433.blackboard;

import com.csse433.blackboard.common.RelationTypeEnum;
import com.csse433.blackboard.friend.dao.FriendDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BlackboardApplicationTests {


    @Autowired
    private FriendDao friendDao;

    @Test
    void contextLoads() {
        RelationTypeEnum userRelation = friendDao.findUserRelation("zhangx8", "chaiq");
        System.out.println(userRelation.toString());
    }

}
