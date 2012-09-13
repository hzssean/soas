package org.saiku.web.dao;

/**
 * Created with IntelliJ IDEA.
 * User: zhisheng.hzs
 * Date: 12-8-30
 * Time: 下午3:26
 * dao层，操作数据库。.
 */
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.saiku.web.bean.ResourceBean;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
/*dao*/

public class SaveLoadDao extends SqlMapClientDaoSupport implements ISaveLoad {

    public void deleteRecord(ResourceBean save){//通过id删除存档
        getSqlMapClientTemplate().delete("deleteRecord",save.getID());
    };

    public void insertRecord(ResourceBean save){//存档
        Map<String,String>map=new HashMap<String, String>();
        map.put("filetmp",save.getFilename());
        map.put("content",save.getContent());
        map.put("ID",save.getID());//用map来传递
       getSqlMapClientTemplate().insert("insertRecord",map);
    }

    public ResourceBean readRecord(ResourceBean save){//通过id来读档
       return (ResourceBean)getSqlMapClientTemplate().queryForObject("selectRecord",save.getID());

    }

    public List[] readState(){//读取储存状态
       List saveStateList=getSqlMapClientTemplate().queryForList("selectState");//
       List[] stateList=new List[2];
       List<String>nameList= new ArrayList<String>();
       List<String>IDList= new ArrayList<String>();
      // Map<String,String>stateMap=new HashMap<String, String>();
       /*nameList= saveStateList;*/
      // Iterator iterator=saveStateList.iterator();
        for(int i=0;i<saveStateList.size();i++){
            Map ListTmp=(Map)saveStateList.get(i);
            nameList.add(ListTmp.get("SAVENAME").toString());
            IDList.add(ListTmp.get("UUID").toString());//id

        }
        stateList[0]=nameList;
        stateList[1]=IDList;

        return stateList;//返回状态
       //return stateMap;

    }


}
