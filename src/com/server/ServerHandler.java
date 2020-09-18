package com.server;
import com.seu.database.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
public class ServerHandler extends SimpleChannelInboundHandler<String> {
    public static final String[] messageName = new String[]{"当前设备时间", "设备id", "数据类型（即表名）", "数据个数n", "数据1名称", "数据1数值", "数据n名称", "数据n数值", "校验值"};

    /**
     * socket连接成功之后接受数据
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void messageReceived(ChannelHandlerContext ctx, String msg) throws Exception {
        //解析数据之后返回的javaBean
        saveData_DB(ctx, msg);
    }

    /**
     * 新客户端接入
     * <p>
     * channelActive()方法将会在连接被建立并且准备进行通信时被调用
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelActive");
        String fa = "$time," + this.getStringDate() + "\r\n";
        String fa1 = new String(fa.getBytes(), "utf-8");
        System.out.println(fa1);
        ctx.channel().writeAndFlush(fa1);
        //信息交互
    }

    /**
     * 客户端断开
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelInactive");
    }

    /**
     * 异常
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public String getStringDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * 异或运算，用于异或校验
     */
    public static String getXor(byte[] datas) {
//        System.out.println(datas.length);
        byte temp = datas[0];
        for (int i = 1; i < datas.length; i++) {
            temp ^= datas[i];
//            System.out.println(datas[i]);
        }
        String check = Integer.toHexString(temp);
        return check;
    }

//#realtimedata，当前设备时间，设备id，数据类型（即表名），数据个数n，数据1名称，数据1数值，……,数据n名称，数据n数值，校验值*

    /**
     * 数据解析函数
     *
     * @param ctx
     * @param msg
     * @return
     * @throws Exception
     */
    public  void saveData_DB(ChannelHandlerContext ctx, String msg) throws Exception {


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String nowDataTime = simpleDateFormat.format(new Date());

        //msg=msg.trim();
        /**************************制浆站数据接受解析**************************/
        if ((msg.startsWith("#")) || (msg.startsWith("!"))) {
            System.out.println(msg);
            String divide_str1;

            if(msg.indexOf("*") == -1){
                divide_str1 = msg.substring(1);
            }else {
                divide_str1 = msg.substring(1,msg.indexOf("*"));
            }

            /*//去掉#和校验位
            String divide_str = msg.substring(1, msg.length() - 3);
            byte[] divide_byte = divide_str.toString().getBytes();
            //计算crc校验和
            int sum_check = 0;
            for (int k = 0; k < divide_byte.length; k++) {
                sum_check += divide_byte[k];
            }
            //保留个位和十位
            int check_data = sum_check & 0x00000ff;*/
            //把数据分割成字符串数组
            String[] divide_str_block = divide_str1.split("_");
            //判断校验位是否正确
            /*boolean tag = Integer.parseInt(divide_str_block[divide_str_block.length - 1]) == check_data;*/
            boolean tag = divide_str_block.length==11;
            if (tag) {
                String clientTime = divide_str_block[8] + " " + divide_str_block[9];
                int realWater = Integer.parseInt( divide_str_block[4]);
                int realCement = Integer.parseInt( divide_str_block[5]);
                /*Date ClientTime = simpleDateFormat.parse(divide_str_block[8] + " " + divide_str_block[9]);*/
                float WATER_CEMENT_RATIO = (float)realWater/realCement;
                float DENSITY = (float)realCement/realWater;
                MixingPlantDao mixingPlantDao = new MixingPlantDao();
                Object object[] = new Object[14];
                for (int i = 0; i < 14; i++){
                    if(i==8){
                        object[8] = clientTime;
                    }
                    else if(i==9){
                        object[9] = divide_str_block[10];
                    }
                    else if(i == 10){
                        java.sql.Connection conn = null;
                        PreparedStatement ps = null;
                        ResultSet rs = null;
                        String sql = "SELECT CurProject FROM tb_deviceinfo WHERE Num = '" + divide_str_block[0] + "';";//查询 通知sql语句
                        try {
                            conn = JDBCTools.getConnection();
                            ps = conn.prepareStatement(sql);
                            rs = ps.executeQuery();
                            if (rs.next()) {
                                System.out.println("inin");
                                object[10] = rs.getString("CurProject");
                            } else {
                                System.out.println("inin2");
                            }
                        } catch (SQLException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } finally {
                            try {
                                rs.close();
                                ps.close();
                                conn.close();
                            } catch (SQLException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                        }
                    }
                    else if(i == 11){
                        if(Float.isNaN(WATER_CEMENT_RATIO)){
                            object[11] = 0;
                        }else {
                        object[11] = WATER_CEMENT_RATIO;
                        }
                    }
                    else if(i == 12){
                        if(Float.isNaN(DENSITY)){
                        object[12] = 0;
                    }else {
                        object[12] = DENSITY;
                    }
                    }
                    else if(i == 13){
                        object[13] = nowDataTime;
                    }
                    else {
                        object[i] = divide_str_block[i];
                    }
                }
                mixingPlantDao.InsertAll("tb_mixing_plant",object);
                System.out.println(divide_str1);


                ctx.channel().writeAndFlush("#realtimedataok,"+object[8].toString()+",99*\r\n");
                ctx.channel().writeAndFlush(object[0].toString()+"_"+object[8].toString()+"\r\n");
            }
            //更新上次在线时间
            java.sql.Connection conn = null;
            PreparedStatement ps = null;
            String sql = "UPDATE tb_deviceinfo SET LastOnlineTime='"+nowDataTime+"' WHERE Num ='"+divide_str_block[0]+"';";//查询 通知sql语句
            try {
                conn = JDBCTools.getConnection();
                ps = conn.prepareStatement(sql);
                ps.executeUpdate();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                try {
                    ps.close();
                    conn.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
            ctx.channel().writeAndFlush("CON_01*\r\n");
        }

    }
}




