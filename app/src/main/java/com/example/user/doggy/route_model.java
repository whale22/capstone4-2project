package com.example.user.doggy;

import java.io.Serializable;
import java.lang.String;

public class route_model implements Serializable {
    private  String name;
    private  String time;
    private  String memo;
    private  String datetime;
    private int random;
    private  float latitude=1.0f;
    private  float longitude=1.0f;

    public int getRandom(){return random;}
    /*
    public void setRandom(String random){
        int random1 = Integer.parseInt(random);
        this.random = random1;
    }
*/
    public void setRandom(int random){
        this.random = random;
    }

    public  float getLatitude() { return latitude;}
    public  void setLatitude(String latitude1){

        try{
            float l_latitude = Float.parseFloat(latitude1);
            this.latitude = l_latitude;
        }
        catch(NumberFormatException NFE){

        }

    }

    public  float getLongitude() { return longitude;}
    public  void setLongitude(String longitude1){
        try{

            float l_longitude = Float.parseFloat(longitude1);
            this.longitude = l_longitude;

        }
        catch(NumberFormatException NFE){
            NFE.printStackTrace();
            NFE.getMessage();
        }


    }


    public  String getName() {
        return name;
    }

    public  void setName(String name) {
        this.name = name;
    }

    public  String getTime() {
        return time;
    }

    public  void setTime(String time) {
        this.time = time;
    }

    public  String getMemo() {
        return memo;
    }

    public  void setMemo(String memo) {
        this.memo = memo;
    }

    public  String getDatetime() {
        return datetime;
    }

    public  void setDatetime(String datetime) {
        this.datetime = datetime;
    }


}

