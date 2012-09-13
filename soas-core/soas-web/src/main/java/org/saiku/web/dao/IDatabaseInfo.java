package org.saiku.web.dao;

import org.saiku.web.bean.DatabaseInfoBean;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhisheng.hzs
 * Date: 12-9-6
 * Time: 下午1:31
 * To change this template use File | Settings | File Templates.
 */
public interface IDatabaseInfo {
    public void insertConfigInfo (DatabaseInfoBean base_info);
    public List[] showConfigInfo();
    public void deleteConfigInfo(DatabaseInfoBean base_info);

}
