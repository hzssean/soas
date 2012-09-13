package org.saiku.web.bean;

/**
 * Created with IntelliJ IDEA.
 * User: zhisheng.hzs
 * Date: 12-9-6
 * Time: 下午1:32
 * To change this template use File | Settings | File Templates.
 */
public class DatabaseInfoBean {
    private String connection_name;
    private String connection_type;
    private String database_server;
    private String database_port;
    private String userid;
    private String password;

    public DatabaseInfoBean(){}
    public void setConnection_name(String con_name){
        this.connection_name=con_name;
    }
    public String getConnection_name(){
        return connection_name;
    }
    public void setConnection_type(String con_type){
        this.connection_type=con_type;
    }
    public String getConnection_type(){
        return connection_type;
    }
    public void setDatabase_server(String database_server){
        this.database_server=database_server;
    }
    public String getDatabase_server(){
        return database_server;
    }
    public void setDatabase_port(String database_port){
        this.database_port=database_port;
    }
    public String getDatabase_port(){
        return database_port;
    }
    public void setUserid(String userid){
        this.userid=userid;
    }
    public String getUserid(){
        return userid;
    }
    public void setPassword(String password){
        this.password=password;
    }
    public String getPassword(){
        return password;
    }
}
