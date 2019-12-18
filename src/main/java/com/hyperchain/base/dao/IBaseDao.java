package com.hyperchain.base.dao;


import com.hyperchain.base.Page;
import com.hyperchain.base.Ps;
import com.hyperchain.base.WMap;
import org.springframework.dao.DataAccessException;

import java.sql.SQLException;
import java.util.List;

public interface IBaseDao {
    Page getPageList(String var1, Ps var2, Page var3) throws SQLException;

    Page getPageList(String var1, Page var2) throws SQLException;

    List<WMap> getMapList(String var1, Ps var2) throws SQLException;

    List<WMap> getMapList(String var1) throws SQLException;

    WMap getMap(String var1) throws SQLException;

    WMap getMap(String var1, Ps var2) throws DataAccessException;

    int save(String var1) throws SQLException;

    int save(String var1, Ps var2) throws SQLException;

    int update(String var1, Ps var2) throws SQLException;

    int update(String var1) throws SQLException;

    int delete(String var1, Ps var2) throws SQLException;

    String getDriverType() throws SQLException;

    /**
     * 插入数据返回自增主键
     *
     * @param sql
     * @param ps
     * @return
     * @throws SQLException
     */
    long insertReturnId(String sql, Ps ps) throws SQLException;
}
