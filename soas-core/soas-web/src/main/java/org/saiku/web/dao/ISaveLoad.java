package org.saiku.web.dao;

import org.saiku.web.bean.ResourceBean;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhisheng.hzs
 * Date: 12-8-31
 * Time: 上午9:49
 * To change this template use File | Settings | File Templates.
 */


public interface ISaveLoad {
    public void deleteRecord(ResourceBean save);
    public void insertRecord(ResourceBean save);
    public ResourceBean readRecord(ResourceBean save);
    //public List readState();
    public List[] readState();

}
