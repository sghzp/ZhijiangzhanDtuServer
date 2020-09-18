package com.seu.database;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class MixingPlantDao extends DAO {

    public void updateAll(String sql, Object... args) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = JDBCTools.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            System.out.println("object_num:" + args.length);
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);
            }

            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCTools.releaseDB(null, preparedStatement, connection);
        }
    }

    // 插入所有的数据进入其中，按照对象插入;
    public void InsertAll(String tablename, Object... obj) {
        boolean response = false;
        String sql = "insert  into " + tablename +"( " +
                "`STATION_NUM`, " +
                "`RECORD_ID`, " +
                "`EXP_WATER`, " +
                "`EXP_CEMENT`," +
                "`REAL_WATER`, " +
                "`REAL_CEMENTONE`, " +
                "`REAL_CEMENTTWO`, " +
                "`TOTAL_WEIGHT`, " +
                "`CLIENT_TIME`, " +
                "`STATION_ID`," +
                "`PROJECT_ID`, " +
                "`WATER_CEMENT_RATIO`, " +
                "`DENSITY`, " +
                "`CURRENT_TIME`) " +
                "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";


        updateAll(sql, obj);
        //      }
        response = true;
    }
}
