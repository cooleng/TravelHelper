package com.xiaomo.travelhelper.model.found;

import java.util.List;

/**
 * 生活指数
 */

public class LifeStyle {

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
        private List<LifestyleEntity> lifestyle;
        private String status;

        public void setUpdate(UpdateEntity update) {
            this.update = update;
        }

        public void setBasic(BasicEntity basic) {
            this.basic = basic;
        }

        public void setLifestyle(List<LifestyleEntity> lifestyle) {
            this.lifestyle = lifestyle;
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

        public List<LifestyleEntity> getLifestyle() {
            return lifestyle;
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

        public class LifestyleEntity {

            private String txt;
            private String brf;
            private String type;

            public void setTxt(String txt) {
                this.txt = txt;
            }

            public void setBrf(String brf) {
                this.brf = brf;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getTxt() {
                return txt;
            }

            public String getBrf() {
                return brf;
            }

            public String getType() {
                return type;
            }
        }
    }
}
