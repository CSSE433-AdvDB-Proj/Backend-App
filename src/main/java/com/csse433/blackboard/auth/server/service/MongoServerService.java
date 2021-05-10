package com.csse433.blackboard.auth.server.service;

import com.mongodb.connection.ServerDescription;

public interface MongoServerService {

    ServerDescription getFirstServerDescription();

    boolean isFirstServerConnected();
}
