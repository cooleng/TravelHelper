package com.xiaomo.travelhelper.model.found;

import java.util.List;

/**
 * 当前空气质量
 */

public class CurrentAir {


    private List<HeWeather6Entity> HeWeather6;

    public void setHeWeather6(List<HeWeather6Entity> HeWeather6) {
        this.HeWeather6 = HeWeather6;
    }

    public List<HeWeather6Entity> getHeWeather6() {
        return HeWeather6;
    }

    public class HeWeather6Entity {

        private UpdateEntity update;
        private Air_now_cityEntity air_now_city;
        private BasicEntity basic;
        private String status;

        public void setUpdate(UpdateEntity update) {
            this.update = update;
        }

        public void setAir_now_city(Air_now_cityEntity air_now_city) {
            this.air_now_city = air_now_city;
        }

        public void setBasic(BasicEntity basic) {
            this.basic = basic;
        }


        public void setStatus(String status) {
            this.status = status;
        }

        public UpdateEntity getUpdate() {
            return update;
        }

        public Air_now_cityEntity getAir_now_city() {
            return air_now_city;
        }

        public BasicEntity getBasic() {
            return basic;
        }


        public String getStatus() {
            return status;
        }

        public class UpdateEntity {

            private String loc;
            private String utc;

            public void setLoc(String loc) {
                this.loc = loc;
            }

            public void setUtc(String utc) {
                this.utc = utc;
            }

            public String getLoc() {
                return loc;
            }

            public String getUtc() {
                return utc;
            }

            @Override
            public String toString() {
                return "UpdateEntity{" +
                        "loc='" + loc + '\'' +
                        ", utc='" + utc + '\'' +
                        '}';
            }
        }

        public class Air_now_cityEntity {

            private String no2;
            private String o3;
            private String pm25;
            private String qlty;
            private String pub_time;
            private String so2;
            private String aqi;
            private String pm10;
            private String main;
            private String co;

            public void setNo2(String no2) {
                this.no2 = no2;
            }

            public void setO3(String o3) {
                this.o3 = o3;
            }

            public void setPm25(String pm25) {
                this.pm25 = pm25;
            }

            public void setQlty(String qlty) {
                this.qlty = qlty;
            }

            public void setPub_time(String pub_time) {
                this.pub_time = pub_time;
            }

            public void setSo2(String so2) {
                this.so2 = so2;
            }

            public void setAqi(String aqi) {
                this.aqi = aqi;
            }

            public void setPm10(String pm10) {
                this.pm10 = pm10;
            }

            public void setMain(String main) {
                this.main = main;
            }

            public void setCo(String co) {
                this.co = co;
            }

            public String getNo2() {
                return no2;
            }

            public String getO3() {
                return o3;
            }

            public String getPm25() {
                return pm25;
            }

            public String getQlty() {
                return qlty;
            }

            public String getPub_time() {
                return pub_time;
            }

            public String getSo2() {
                return so2;
            }

            public String getAqi() {
                return aqi;
            }

            public String getPm10() {
                return pm10;
            }

            public String getMain() {
                return main;
            }

            public String getCo() {
                return co;
            }

            @Override
            public String toString() {
                return "Air_now_cityEntity{" +
                        "no2='" + no2 + '\'' +
                        ", o3='" + o3 + '\'' +
                        ", pm25='" + pm25 + '\'' +
                        ", qlty='" + qlty + '\'' +
                        ", pub_time='" + pub_time + '\'' +
                        ", so2='" + so2 + '\'' +
                        ", aqi='" + aqi + '\'' +
                        ", pm10='" + pm10 + '\'' +
                        ", main='" + main + '\'' +
                        ", co='" + co + '\'' +
                        '}';
            }
        }

        public class BasicEntity {

            private String admin_area;
            private String tz;
            private String location;
            private String lon;
            private String parent_city;
            private String cnty;
            private String lat;
            private String cid;

            public void setAdmin_area(String admin_area) {
                this.admin_area = admin_area;
            }

            public void setTz(String tz) {
                this.tz = tz;
            }

            public void setLocation(String location) {
                this.location = location;
            }

            public void setLon(String lon) {
                this.lon = lon;
            }

            public void setParent_city(String parent_city) {
                this.parent_city = parent_city;
            }

            public void setCnty(String cnty) {
                this.cnty = cnty;
            }

            public void setLat(String lat) {
                this.lat = lat;
            }

            public void setCid(String cid) {
                this.cid = cid;
            }

            public String getAdmin_area() {
                return admin_area;
            }

            public String getTz() {
                return tz;
            }

            public String getLocation() {
                return location;
            }

            public String getLon() {
                return lon;
            }

            public String getParent_city() {
                return parent_city;
            }

            public String getCnty() {
                return cnty;
            }

            public String getLat() {
                return lat;
            }

            public String getCid() {
                return cid;
            }

            @Override
            public String toString() {
                return "BasicEntity{" +
                        "admin_area='" + admin_area + '\'' +
                        ", tz='" + tz + '\'' +
                        ", location='" + location + '\'' +
                        ", lon='" + lon + '\'' +
                        ", parent_city='" + parent_city + '\'' +
                        ", cnty='" + cnty + '\'' +
                        ", lat='" + lat + '\'' +
                        ", cid='" + cid + '\'' +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "HeWeather6Entity{" +
                    "update=" + update +
                    ", air_now_city=" + air_now_city +
                    ", basic=" + basic +
                    ", status='" + status + '\'' +
                    '}';
        }
    }
}
