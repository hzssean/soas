package org.saiku.web.dao;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.saiku.web.bean.DatabaseInfoBean;
/**
 * Created with IntelliJ IDEA.
 * User: zhisheng.hzs
 * Date: 12-9-6
 * Time: 下午1:30
 * dao层，操作数据库。.
 */
public class DatabaseInfoDao extends SqlMapClientDaoSupport implements IDatabaseInfo{
   //新增数据库配置信息
    public void insertConfigInfo(DatabaseInfoBean base_info){
        Map<String,String>map=new HashMap<String, String>();
        map.put("source_name",base_info.getConnection_name());
        map.put("source_type",base_info.getConnection_type());
        map.put("server",base_info.getDatabase_server());
        map.put("port",base_info.getDatabase_port());
        map.put("userid",base_info.getUserid());
        map.put("password",base_info.getPassword());

        getSqlMapClientTemplate().insert("insertInfo",map);
    }

    //读取现有配置信息
    public List[] showConfigInfo(){
        List config_info_list=getSqlMapClientTemplate().queryForList("showInfo");
        List<String> config_name= new ArrayList<String>();
        List<String> config_type= new ArrayList<String>();
        List<String> config_server= new ArrayList<String>();
        List<String> config_port= new ArrayList<String>();
        List<String> config_user= new ArrayList<String>();
        List<String> config_password= new ArrayList<String>();
        for(int i=0;i<config_info_list.size();i++){
            Map config_tmp=(Map) config_info_list.get(i);//获取每一条
            config_name.add(config_tmp.get("NAME").toString());
            config_type.add(config_tmp.get("DATA_BASE_TYPE").toString());
            config_server.add(config_tmp.get("DATA_BASE_SERVER").toString());
            config_port.add(config_tmp.get("DATA_BASE_PORT").toString());
            config_user.add(config_tmp.get("USER_ID").toString());
            config_password.add(config_tmp.get("PASSWORD").toString());
        }
        List[] get_config_info={config_name,config_type,config_server,config_port,config_user,config_password};//6个信息：顺序依次为name，server，type...
        return get_config_info;
    }

    //删除
    public void deleteConfigInfo(DatabaseInfoBean base_info){
        getSqlMapClientTemplate().delete("deleteInfo",base_info.getConnection_name());

    }
}
