package org.saiku.web.bean;

/**
 * Created with IntelliJ IDEA.
 * User: zhisheng.hzs
 * Date: 12-8-30
 * Time: 下午8:25
 * To change this template use File | Settings | File Templates.
 */

/*数据类*/
public class ResourceBean implements IResourceBean {
    private String content;//储存内容
    private String filename;//文件名
    private String ID;//存储ID
   // private ArrayList<String> saveState;//存储状况
   public ResourceBean(){}
   public ResourceBean(String filename){
       this.filename=filename;

   }

   public String getFilename(){//读取文件名
       return filename;
   }
   public void setFilename(String filename){
       this.filename=filename;
   }

    public String getContent(){//读取存储内容
        return content;
    }
    public void setContent(String content){
        this.content=content;
    }
    public void setID(String ID){
        this.ID=ID;
    }
    public String getID(){
        return ID;
    }

   /* public List<String> getSaveState(){//读取存储状态
        return saveState;
    }
    public void setSaveState(ArrayList<String> saveState){
       this.saveState=saveState;
    } */


}
