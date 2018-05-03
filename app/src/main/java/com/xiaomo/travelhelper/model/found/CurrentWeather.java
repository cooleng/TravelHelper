package com.xiaomo.travelhelper.model.found;

import java.util.List;

/**
 * 当前实况天气
 */

public class CurrentWeather {

    private List<HeWeather6Entity> HeWeather6;

    public void setHeWeather6(List<HeWeather6Entity> HeWeather6) {
        this.HeWeather6 = HeWeather6;
    }

    public List<HeWeather6Entity> getHeWeather6() {
        return HeWeather6;
    }

    public class HeWeather6Entity {

        private NowEntity now;
        private UpdateEntity update;
        private BasicEntity basic;
        private String status;

        public void setNow(NowEntity now) {
            this.now = now;
        }

        public void setUpdate(UpdateEntity update) {
            this.update = update;
        }

        public void setBasic(BasicEntity basic) {
            this.basic = basic;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public NowEntity getNow() {
            return now;
        }

        public UpdateEntity getUpdate() {
            return update;
        }

        public BasicEntity getBasic() {
            return basic;
        }

        public String getStatus() {
            return status;
        }

        @Override
        public String toString() {
            return "HeWeather6Entity{" +
                    "now=" + now +
                    ", update=" + update +
                    ", basic=" + basic +
                    ", status='" + status + '\'' +
                    '}';
        }

        public class NowEntity {

            private String hum;
            private String vis;
            private String wind_deg;
            private String pres;
            private String pcpn;
            private String fl;
            private String tmp;
            private String cond_txt;
            private String wind_sc;
            private String wind_dir;
            private String wind_spd;
            private String cond_code;

            public void setHum(String hum) {
                this.hum = hum;
            }

            public void setVis(String vis) {
                this.vis = vis;
            }

            public void setWind_deg(String wind_deg) {
                this.wind_deg = wind_deg;
            }

            public void setPres(String pres) {
                this.pres = pres;
            }

            public void setPcpn(String pcpn) {
                this.pcpn = pcpn;
            }

            public void setFl(String fl) {
                this.fl = fl;
            }

            public void setTmp(String tmp) {
                this.tmp = tmp;
            }

            public void setCond_txt(String cond_txt) {
                this.cond_txt = cond_txt;
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

            public void setCond_code(String cond_code) {
                this.cond_code = cond_code;
            }

            public String getHum() {
                return hum;
            }

            public String getVis() {
                return vis;
            }

            public String getWind_deg() {
                return wind_deg;
            }

            public String getPres() {
                return pres;
            }

            public String getPcpn() {
                return pcpn;
            }

            public String getFl() {
                return fl;
            }

            public String getTmp() {
                return tmp;
            }

            public String getCond_txt() {
                return cond_txt;
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

            public String getCond_code() {
                return cond_code;
            }

            @Override
            public String toString() {
                return "NowEntity{" +
                        "hum='" + hum + '\'' +
                        ", vis='" + vis + '\'' +
                        ", wind_deg='" + wind_deg + '\'' +
                        ", pres='" + pres + '\'' +
                        ", pcpn='" + pcpn + '\'' +
                        ", fl='" + fl + '\'' +
                        ", tmp='" + tmp + '\'' +
                        ", cond_txt='" + cond_txt + '\'' +
                        ", wind_sc='" + wind_sc + '\'' +
                        ", wind_dir='" + wind_dir + '\'' +
                        ", wind_spd='" + wind_spd + '\'' +
                        ", cond_code='" + cond_code + '\'' +
                        '}';
            }
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
    }

    @Override
    public String toString() {
        return "CurrentWeather{" +
                "HeWeather6=" + HeWeather6 +
                '}';
    }
}
