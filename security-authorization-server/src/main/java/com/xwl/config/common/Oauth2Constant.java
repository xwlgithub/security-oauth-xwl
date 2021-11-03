package com.xwl.config.common;

/**
 * @author xueWenLiang
 * @date 2021/6/30 17:01
 * @Description 认证URL常量是
 */
public class Oauth2Constant {
    /**
     * 自定义client表名
     */
    public static final String CLIENT_TABLE = "auth_usr_client";
    /**
     * 基础查询语句
     */
    public static final String CLIENT_BASE = "select client_id, client_secret, " +
            "resource_ids, scope, " +
            "authorized_grant_types, web_server_redirect_uri, authorities," +
            "access_token_validity,refresh_token_validity,additional_information,autoapprove from " + CLIENT_TABLE;

    public static final String FIND_CLIENT_DETAIL_SQL = CLIENT_BASE + " order by client_id";

    public static final String SELECT_CLIENT_DETAIL_SQL = CLIENT_BASE + " where client_id = ?";
}
