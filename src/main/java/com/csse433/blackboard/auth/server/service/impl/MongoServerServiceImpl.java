package com.csse433.blackboard.auth.server.service.impl;

import com.csse433.blackboard.auth.server.service.MongoServerService;
import com.mongodb.client.MongoClient;
import com.mongodb.connection.ClusterDescription;
import com.mongodb.connection.ServerConnectionState;
import com.mongodb.connection.ServerDescription;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MongoServerServiceImpl implements MongoServerService {


    @Autowired
    private MongoClient mongo;

    @Override
    public ServerDescription getFirstServerDescription() {
        ClusterDescription clusterDescription = mongo.getClusterDescription();
        return clusterDescription.getServerDescriptions().get(0);
    }

    @Override
    public boolean isFirstServerConnected() {
        ServerDescription firstServerDescription = getFirstServerDescription();
        boolean connected = ServerConnectionState.CONNECTED.equals(firstServerDescription.getState());
        if(!connected) {
            log.error(String.format("MongoDB server running on %s is disconnected. Description: %s", firstServerDescription.getAddress(), firstServerDescription.getShortDescription()));
        }
        return connected;
    }
}
