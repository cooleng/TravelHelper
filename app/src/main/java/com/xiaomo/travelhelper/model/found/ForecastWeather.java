package com.xiaomo.travelhelper.model.found;

import java.util.List;

/**
 * 3 天预报
 */

public class ForecastWeather {

    private List<HeWeather6Entity> HeWeather6;

    public void setHeWeather6(List<HeWeather6Entity> HeWeather6) {
        this.HeWeather6 = HeWeather6;
    }

    public List<HeWeather6Entity> getHeWeather6() {
        return HeWeather6;
    }

    public class HeWeather6Entity {

        private UpdateEntity update;
        private BasicEntity basic;
        private List<Daily_forecastEntity> daily_forecast;
        private String status;

        public void setUpdate(UpdateEntity update) {
            this.update = update;
        }

        public void setBasic(BasicEntity basic) {
            this.basic = basic;
        }

        public void setDaily_forecast(List<Daily_forecastEntity> daily_forecast) {
            this.daily_forecast = daily_forecast;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public UpdateEntity getUpdate() {
            return update;
        }

        public BasicEntity getBasic() {
            return basic;
        }

        public List<Daily_forecastEntity> getDaily_forecast() {
            return daily_forecast;
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
        }

        public class Daily_forecastEntity {

            private String date;
            private String tmp_min;
            private String hum;
            private String vis;
            private String cond_txt_d;
            private String pres;
            private String pcpn;
            private String cond_code_n;
            private String wind_sc;
            private String wind_dir;
            private String wind_spd;
            private String pop;
            private String wind_deg;
            private String uv_index;
            private String tmp_max;
            private String cond_txt_n;
            private String cond_code_d;

            public void setDate(String date) {
                this.date = date;
            }

            public void setTmp_min(String tmp_min) {
                this.tmp_min = tmp_min;
            }

            public void setHum(String hum) {
                this.hum = hum;
            }

            public void setVis(String vis) {
                this.vis = vis;
            }

            public void setCond_txt_d(String cond_txt_d) {
                this.cond_txt_d = cond_txt_d;
            }

            public void setPres(String pres) {
                this.pres = pres;
            }

            public void setPcpn(String pcpn) {
                this.pcpn = pcpn;
            }

            public void setCond_code_n(String cond_code_n) {
                this.cond_code_n = cond_code_n;
            }

            public void setWind_sc(String wind_sc) {
                this.wind_sc = wind_sc;
            }

            public void setWind_dir(String wind_dir) {
                this.wind_dir = wind_dir;
            }

            public void setWind_spd(String wind_spd) {
                this.wind_spd = wind_spd;
            }

            public void setPop(String pop) {
                this.pop = pop;
            }

            public void setWind_deg(String wind_deg) {
                this.wind_deg = wind_deg;
            }

            public void setUv_index(String uv_index) {
                this.uv_index = uv_index;
            }

            public void setTmp_max(String tmp_max) {
                this.tmp_max = tmp_max;
            }

            public void setCond_txt_n(String cond_txt_n) {
                this.cond_txt_n = cond_txt_n;
            }

            public void setCond_code_d(String cond_code_d) {
                this.cond_code_d = cond_code_d;
            }

            public String getDate() {
                return date;
            }

            public String getTmp_min() {
                return tmp_min;
            }

            public String getHum() {
                return hum;
            }

            public String getVis() {
                return vis;
            }

            public String getCond_txt_d() {
                return cond_txt_d;
            }

            public String getPres() {
                return pres;
            }

            public String getPcpn() {
                return pcpn;
            }

            public String getCond_code_n() {
                return cond_code_n;
            }

            public String getWind_sc() {
                return wind_sc;
            }

            public String getWind_dir() {
                return wind_dir;
            }

            public String getWind_spd() {
                return wind_spd;
            }

            public String getPop() {
                return pop;
            }

            public String getWind_deg() {
                return wind_deg;
            }

            public String getUv_index() {
                return uv_index;
            }

            public String getTmp_max() {
                return tmp_max;
            }

            public String getCond_txt_n() {
                return cond_txt_n;
            }

            public String getCond_code_d() {
                return cond_code_d;
            }
        }
    }
}
