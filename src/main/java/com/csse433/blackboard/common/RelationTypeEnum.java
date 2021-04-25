package com.csse433.blackboard.common;

public enum RelationTypeEnum {
    /**
     * Is friend.
     */
    FRIEND("1"),

    /**
     * Is blacklisted.
     */
    BLACKLISTED("2");

    String data;


    RelationTypeEnum(String data) {
        this.data = data;
    }
}
