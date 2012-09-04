package org.saiku.web.rest.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.saiku.web.rest.objects.repository.IRepositoryObject;
import org.saiku.web.rest.objects.repository.RepositoryFileObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import org.saiku.web.dao.*;

/*
 * Created with IntelliJ IDEA.
 * User: zhisheng.hzs
 * Date: 12-8-23
 * Time: 上午9:47
 * To change this template use File | Settings | File Templates.
 */


@Component
@Path("/saiku/{username}/repository2")
@XmlAccessorType(XmlAccessType.NONE)
public class BasicRepositoryResource3 {
    private static final Logger log = LoggerFactory.getLogger(BasicRepositoryResource3.class);


    //static boolean conn_bool=false;
    //static Connection conn=null;
    //static Statement stmt=null;
    static soasDao dao=null;//初始化关于连接oracle的变量



    public void setPath(String path) throws Exception {



        try{
            ApplicationContext ctx=new ClassPathXmlApplicationContext("soasManager.xml");
            //DataSource dataSource =(DataSource)ctx.getBean("dataSource");//连接池数据源
            dao=(soasDao)ctx.getBean("soasDao");
            //if(!conn_bool){
                //InitialContext initCtx=new InitialContext();
                //DataSource ds=(DataSource)initCtx.lookup("java:comp/env/jdbc/foodmart-ds");//获取数据源
                //Class.forName( "oracle.jdbc.driver.OracleDriver");
                //conn=DriverManager.getConnection("jdbc:oracle:thin:@freenas-1-1.alipay.net:1521:tool","adc","ADC789");

                //conn=dataSource.getConnection();//连接数据源
                //stmt=conn.createStatement();
                //stmt.executeUpdate("create table save_data (save_name varchar(4),content varchar(4));");//TODO 连接数据表,方便以后插入
                //conn_bool=true;//标志为已连接

        }
        catch (Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

    }

    /*
    public void set_path (String path)throws Exception{//TODO 建立与oracle的连接
        FileSystemManager fileSystemManager;
        try{
            if(!conn_bool){
                Class.forName( "oracle.jdbc.driver.OracleDriver");
                conn=DriverManager.getConnection("jdbc:oracle:thin:@freenas-1-1.alipay.net:1521:tool","adc","ADC789");
                stmt=conn.createStatement();
                stmt.executeUpdate("create table \"save_data\"(save_name varchar,content varchar)");//创建数据表
                conn_bool=true;//标志为已连接
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    */
    //TODO　返回数据表状态
    @GET
    @Produces({"application/json" })
    public List<IRepositoryObject> getSaveList() throws Exception{ //保存的
        List<IRepositoryObject> saveList = new ArrayList<IRepositoryObject>();//返回已存的情况
        //List<String> SaveName =new ArrayList<String>();//保存获取到的字符串
        try{
            Save save=null;
            // rs=stmt.executeQuery("select SAVENAME from SAVEDATA");
            //SaveName=dao.readState();
            List[] state=dao.readState();//获取存储的状态
            /*for(int i=0;i<map.size()/2;i++){
               SaveName.add(map.get("savename"+"i"));
               SaveDate.add(map.get("savedate"+"i"));
            }*/

       /* while (rs.next()){
            SaveName.add(rs.getString("SAVENAME"));
        }*/
                return getSaveListObject(state);//返回存储的文件名与ID

        }
        catch (Exception e){
            System.out.println(e.getMessage());
            //System.out.println(e.getErrorCode());
            e.printStackTrace();
        }

        return saveList;
    }

    private List<IRepositoryObject> getSaveListObject(List[] state){//要返回RepositoryFileObject\
        List<IRepositoryObject> saveListObject =new ArrayList<IRepositoryObject>();
        List<String> SaveName =new ArrayList<String>();//保存获取到的文件名
        List<String> SaveID =new ArrayList<String>();//保存获取到的存储时间
        SaveName=state[0];
        SaveID=state[1];
        for(int i=0;i<SaveName.size();i++){
            String nameTmp=SaveName.get(i);
            String IDTmp=SaveID.get(i);
            saveListObject.add(new RepositoryFileObject(nameTmp,IDTmp,"saiku",IDTmp));//filename id filetype Path
        }
        return saveListObject;
    }


    //TODO 读取数据
    @GET
    @Produces({"text/plain" })
    @Path("/resource")
    public Response load_data(@QueryParam("name") String filetmp,@QueryParam("file") String IDtmp) throws Exception{ //请求中有name与path两个东西，path为file
        try{
            Save save=new Save(filetmp);//初始化save类
            save.setID(IDtmp);
            //ResultSet rscontent=null;
            byte [] doc=null;
            //rscontent=stmt.executeQuery("select CONTENT from SAVEDATA where SAVENAME='"+filetmp+"'");

            /*while (rscontent.next()){
               doc =rscontent.getString(1).getBytes();
            } */
            doc=dao.readRecord(save).getContent().getBytes();
            return Response.ok(doc, MediaType.TEXT_PLAIN).header("content-length",doc.length).build();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            //System.out.println(e.getErrorCode());
            e.printStackTrace();
        }
        /*finally
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
        } */
        return Response.serverError().build();
    }

    //TODO save储存
    @POST
    @Path("/resource")
    public Status save_data(@FormParam("file") String filetmp,
                            @FormParam("content") String content) throws  Exception{
        try{
           /* ResultSet fileTest=stmt.executeQuery("select * from SAVEDATA where SAVENAME='"+filetmp+"'");//检查是否已经有要储存的文件
            while (fileTest.next())
            {
                stmt.executeUpdate("delete from savedata where SAVENAME='" + filetmp + "'");//若有就删除该列
            }

            stmt.executeUpdate("insert into savedata values ('"+filetmp+"','"+content+"')");//保存储存的内容
            */
            Save save=new Save(filetmp);
            String s_UUID=UUID.randomUUID().toString();
            save.setID(s_UUID);//保存UUID
            save.setContent(content);//设置好存储的内容
            dao.insertRecord(save);//存档
            return Status.OK;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        /*finally
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
        } */
        return Status.INTERNAL_SERVER_ERROR;
    }

    //TODO 删除
    @DELETE
    @Path("/resource")
    public Status delete_data(@QueryParam("name") String filetmp,@QueryParam("file") String IDtmp) throws Exception{
        try{
            //stmt.executeUpdate("delete from savedata where SAVENAME='" + filetmp + "'");//删除相应的文件
            Save save=new Save(filetmp);
            save.setID(IDtmp);//设置好ID
            dao.deleteRecord(save);//删除相应文件
            return Status.OK;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            //System.out.println(e.getErrorCode());
            e.printStackTrace();
        }
        /*finally
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
        }*/
        return Status.INTERNAL_SERVER_ERROR;
    }

}
