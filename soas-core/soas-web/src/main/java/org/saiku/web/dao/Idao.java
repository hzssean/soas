package org.saiku.web.dao;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: zhisheng.hzs
 * Date: 12-8-31
 * Time: 上午9:49
 * To change this template use File | Settings | File Templates.
 */


public interface Idao {
    public void deleteRecord(Save save);
    public void insertRecord(Save save);
    public Save  readRecord(Save save);
    //public List readState();
    public List[] readState();

}
