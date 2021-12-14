package com.nowcoder.community.util;

public interface CommunityConstant {

    int ACTIVATION_SUCCESS=0;

    /**
     *  Repeat activation
     */

    int ACTIVATION_REPEAT=01;

    /**
     * Activation fail
     */
    int ACTIVATION_FAILURE=02;

    /**
     *
     */

    int DEFAULT_EXPIRED_SECONDS = 3600 * 12;

    /**
     *
     */

    int REMEMBER_EXPIRED_SECONDS = 3600 * 24 * 100;

    int ENTITY_TYPE_POST = 1;

    int ENTITY_TYPE_COMMENT = 2;

}
