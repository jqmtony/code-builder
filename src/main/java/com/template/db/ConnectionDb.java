package com.template.db;

import com.template.constant.CommonConstant;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Package com.template.db
 * @Description:
 * @Author zhouxi
 * @Date 2017/9/29 22:24
 */
public class ConnectionDb {

    /**
     * 单例对象
     */
    private static ConnectionDb connectionDb = null;

    /**
     * 数据库连接对象
     */
    private Connection connection = null;

    /**
     * PreparedStatement对象
     */
    private PreparedStatement preparedStatement = null;

    /**
     * 结果集对象
     */
    private ResultSet resultSet = null;

    private ConnectionDb() {}

    public static ConnectionDb getInstance() {
        if(connectionDb == null) {
            connectionDb = new ConnectionDb();
        }
        return connectionDb;
    }

    static {
        try {
            Class.forName(CommonConstant.DRIVER);  // 加载数据库驱动程序
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * @Description: 建立数据库连接
     * @return Connection
     */
    public Connection getConnection() {
        try{
            connection = DriverManager.getConnection(CommonConstant.URLDB, CommonConstant.USER_NAME,
                    CommonConstant.USER_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * @Description: insert update delete
     * @param sql
     * @param params
     * @return affectedLine
     */
    public int executeUpdate(String sql, Object... params) {
        int affectedLine = 0;  // 受影响的行数

        try{
            connection = getConnection();  // 获取到连接
            preparedStatement = connection.prepareStatement(sql);
            if(params != null) {
                for (int i = 0; i < params.length; i++) {
                    preparedStatement.setObject(i + 1, params[i]);
                }
            }
            affectedLine = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResource();   // 关闭资源
        }
        return affectedLine;
    }

    /**
     * @Description: 根据条件查询出一条数据
     * @param sql
     * @param params
     * @return
     */
    public Object executeQuerySingleOne(String sql, Object... params) {
        Object object = null;
        try{
            resultSet = executeQueryResult(sql, params);  // 查询出结果集
            if(resultSet.next()) {
                object = resultSet.getObject(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResource();
        }
        return object;
    }

    /**
     * @Description: 根据条件查询出一串数据
     * @param sql
     * @param params
     * @return
     */
    public List<Object> excuteQueryList(String sql, Object... params) {
        List<Object> list = new ArrayList<Object>();
        resultSet = executeQueryResult(sql, params);
        ResultSetMetaData rsmd = null;   // 创建ResultSetMetaData对象
        int columnCount = 0;    // 结果集列数
        try{
            rsmd = resultSet.getMetaData();
            columnCount = rsmd.getColumnCount();  // 获得结果集列数
            while (resultSet.next()) {
                Map<String, Object> map = new HashMap<String, Object>();
                for (int i = 1; i <= columnCount; i++) {
                    map.put(rsmd.getColumnLabel(i), resultSet.getObject(i));
                }
                list.add(map);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @Description: 查询出结果集
     * @param sql
     * @param params
     * @return
     */
    private ResultSet executeQueryResult(String sql, Object[] params) {
        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);
            // 参数赋值
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    preparedStatement.setObject(i + 1, params[i]);
                }
            }
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    /**
     * @Description: 关闭所有资源
     */
    private void closeResource() {
        // 关闭结果集对象
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        // 关闭PreparedStatement对象
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        // 关闭Connection对象
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
