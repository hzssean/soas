package org.saiku.web.rest.resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
/**
 * Created with IntelliJ IDEA.
 * User: zhisheng.hzs
 * Date: 12-9-11
 * Time: 下午3:52
 * 通过数据库配置信息连接不同的数据库.
 */
@Component
@Path("/saiku/database_connect")
@XmlAccessorType(XmlAccessType.NONE)
public class DatabaseConnectResource {
    private static final Logger log = LoggerFactory.getLogger(DatabaseConnectResource.class);
    static boolean conn_bool=false;
    static Connection conn=null;
    static Statement stmt=null;
    static ResultSet rs=null;//初始化关于连接数据库的变量

    @POST
    @Path("/connect")
    @Produces("application/json")
    public List connectDatabase(@FormParam("connection_name")String connection_name,
                                  @FormParam("connection_type")String connection_type,
                                  @FormParam("database_server")String database_server,
                                  @FormParam("database_port")String database_port,
                                  @FormParam("userid")String userid,
                                  @FormParam("password")String password) throws Exception{//连接用户选择的数据库并返回其中的表名
        List<String > tables=new ArrayList<String>();
        String database_driver =null;
        String pretext=null;
        String SID="";
        String database_type=connection_type;
        if(database_type.equals("Oracle") ){//Oracle类型数据库
            database_driver="oracle.jdbc.driver.OracleDriver";
            pretext="jdbc:oracle:thin:@";
            SID="tool";//TODO 暂时测试，要更改表的设计
        }
        else if(database_type.equals("MySQL") ){//MySQL类型数据库
            database_driver="org.gjt.mm.mysql.Driver";
            pretext="jdbc:mysql://";
        }
        String url=pretext+database_server+":"+database_port+":"+SID;

        //连接
        try{
            if(!conn_bool){
                Class.forName( database_driver);
                conn=DriverManager.getConnection(url,userid,password);
                stmt=conn.createStatement();
                //stmt.executeUpdate("create table \"save_data\"(save_name varchar,content varchar)");//创建数据表
                conn_bool=true;//标志为已连接
            }

        }
        catch (Exception e){
            e.printStackTrace();
            tables.add(e.toString());
        }
        try{
            if (database_type.equals("Oracle")){
                rs=stmt.executeQuery("select TABLE_NAME from tabs");
            }
            else if(database_type.equals("MySQL")){
                rs=stmt.executeQuery("select TABLE_NAME from tables");
            }
            while (rs.next()){
                tables.add(rs.getString("TABLE_NAME"));
            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
            System.out.println(e.getErrorCode());
            e.printStackTrace();
        }
        finally
        {
            // The finally clause is always executed - even in error
            // 关闭连接
            try
            {
                if (stmt != null)
                    stmt.close();
            }
            catch(Exception e) {}
            try
            {
                if (conn != null)
                    conn.close();
            }
            catch (Exception e){}
            try{
                conn_bool=false;//关闭之后重置为未连接状态
            }
            catch (Exception e){}
        }
        return tables;
    }
}
