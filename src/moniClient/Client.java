package moniClient;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by admin on 2017/8/17.
 */
public class Client {

    static double count = 1458985738762.0;


    public static void main(String[] args) {
        new Thread() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                  Socket socket = new Socket("10.193.7.9", 50001);
                  PrintStream ps = new PrintStream(socket.getOutputStream());
                  InputStream is=socket.getInputStream();
                  BufferedReader br=new BufferedReader(new InputStreamReader(is));
                  double lat =32.05877181930;
                    while (true) {
                        if(lat >=32.05877181930 && lat<35.05877181930){
                            lat=lat + 0.5;
                        }else{
                            lat =32.05877181930;
                        }

                        //#realtimedata，当前设备时间，设备id，数据类型（即表名），数据个数n，数据1名称，数据1数值，……,数据n名称，数据n数值，校验值*
                      String s = "realtimedata,201803161212113,jly000002,tb_pilerealtime,16,ZhuangNum,0001,Lon,118.4,Lat,38.9,Curtime,20180316121213," +
                                "CurLength,12,flow,0.5,slurry,5,concrete,6,Incurrent,2.0,OutCurrent,1.2,FBAngularity,9,LRAngularity,2,Verticality,98,Speed,10," +
                                "DeviceNum,YX-1,1*" ;
                        s = "#" + s;
                        count++;
                        ps.print(s);
//                        System.out.println(br.readLine());
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {

                            e.printStackTrace();
                        }
                    }

                } catch (UnknownHostException e) {

                    e.printStackTrace();
                } catch (IOException e) {

                    e.printStackTrace();
                }
            }
        }.start();
    }

}

