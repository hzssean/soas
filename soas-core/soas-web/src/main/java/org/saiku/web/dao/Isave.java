package org.saiku.web.dao;

/**
 * Created with IntelliJ IDEA.
 * User: zhisheng.hzs
 * Date: 12-9-3
 * Time: 下午4:15
 * To change this template use File | Settings | File Templates.
 */
public interface Isave {
    public String getFilename();
    public void setFilename(String filename);
    public String getContent();
    public void setContent(String content);
    public void setID(String ID);
    public String getID();

}
