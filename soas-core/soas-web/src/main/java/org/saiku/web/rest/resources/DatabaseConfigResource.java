package org.saiku.web.rest.resources;

import org.saiku.service.ISessionService;
import org.saiku.web.bean.DatabaseInfoBean;
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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.saiku.web.bean.DatabaseInfoBean;
import org.saiku.web.dao.DatabaseInfoDao;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * User: zhisheng.hzs
 * Date: 12-9-6
 * Time: 上午10:32
 * 保存连接配置信息
 */
@Component
@Path("/saiku/database")
@XmlAccessorType(XmlAccessType.NONE)
public class DatabaseConfigResource {
    private static final Logger log = LoggerFactory.getLogger(DatabaseConfigResource.class);
    DatabaseInfoDao dao=null;

   /*public void  setConnection(ISessionService connection) throws Exception{
        try{

        }
        catch (Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

    }*/

    //TODO 保存
    @POST
    @Path("/save")
    public String saveConfig(@FormParam("con_name") String con_name,
                             @FormParam("con_server") String con_server,
                             @FormParam("con_port") String con_port,
                             @FormParam("con_type") String con_type,
                             @FormParam("user") String user,
                             @FormParam("password") String password){
        ApplicationContext ctx=new ClassPathXmlApplicationContext("soasManager.xml");
        dao=(DatabaseInfoDao)ctx.getBean("DatabaseInfoDao");
        String s_UUID=UUID.randomUUID().toString();
        DatabaseInfoBean config_info=new DatabaseInfoBean();
        config_info.setConnection_name(con_name);
        config_info.setConnection_type(con_type);
        config_info.setDatabase_server(con_server);
        config_info.setDatabase_port(con_port);
        config_info.setUserid(user);
        config_info.setPassword(password);//设置好保存的内容
        dao.insertConfigInfo(config_info);

        return "saved";
    }

    //TODO 返回已存信息
    @GET
    @Path("/load")
    @Produces("application/json")
    public List showConfigInfo() throws Exception{
        List<DatabaseInfoBean> config_information=new ArrayList<DatabaseInfoBean>();
        try{
            ApplicationContext ctx=new ClassPathXmlApplicationContext("soasManager.xml");
            dao=(DatabaseInfoDao)ctx.getBean("DatabaseInfoDao");
            List[] config_info_tmp=dao.showConfigInfo();//获取到数据库的配置信息。
            List<String> config_name= config_info_tmp[0];
            List<String> config_type= config_info_tmp[1];
            List<String> config_server= config_info_tmp[2];
            List<String> config_port=config_info_tmp[3];
            List<String> config_user= config_info_tmp[4];
            List<String> config_password= config_info_tmp[5];//依次为name,type,server.port,user,

            for(int i=0;i<config_name.size();i++){
                DatabaseInfoBean databaseInfoBean=new DatabaseInfoBean();
                databaseInfoBean.setConnection_name(config_name.get(i));
                databaseInfoBean.setConnection_type(config_type.get(i));
                databaseInfoBean.setDatabase_server(config_server.get(i));
                databaseInfoBean.setDatabase_port(config_port.get(i));
                databaseInfoBean.setUserid(config_user.get(i));
                databaseInfoBean.setPassword(config_password.get(i));
                config_information.add(databaseInfoBean);//依次添加
            }

        }
        catch (Exception e){
            System.out.println(e.getMessage());
            //System.out.println(e.getErrorCode());
            e.printStackTrace();
        }
       return config_information;
    }

    //TODO 删除信息
    @POST
    @Path("/delete")
    public String deleteConfigInfo(@FormParam("con_name") String con_name) throws Exception{
        try{
            ApplicationContext ctx=new ClassPathXmlApplicationContext("soasManager.xml");
            dao=(DatabaseInfoDao)ctx.getBean("DatabaseInfoDao");
            DatabaseInfoBean config_info=new DatabaseInfoBean();
            config_info.setConnection_name(con_name);//根据保存名称来删除
            dao.deleteConfigInfo(config_info);


        }
        catch (Exception e){
            System.out.println(e.getMessage());
            //System.out.println(e.getErrorCode());
            e.printStackTrace();
        }

     return "deleted!";
    }
}
