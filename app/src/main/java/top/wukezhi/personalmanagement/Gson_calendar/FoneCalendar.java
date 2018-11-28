package top.wukezhi.personalmanagement.Gson_calendar;

import com.google.gson.annotations.SerializedName;

public class FoneCalendar {

    /**
     * reason : Success
     * result : {"data":{"date":"2018-11-28","weekday":"星期三","animalsYear":"狗","suit":"祭祀.会亲友.嫁娶.沐浴.修造.动土.祈福.开光.塑绘.出行.订盟.纳采.裁衣.入殓.除服.成服.移柩.启攒.赴任.竖柱.上梁.纳财.扫舍.栽种.纳畜.伐木.","avoid":"入宅.作灶.安床.开仓.","year-month":"2018-11","lunar":"十月廿一","lunarYear":"戊戌年"}}
     * error_code : 0
     */

    private String reason;
    private ResultBean result;
    private int error_code;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public static class ResultBean {
        /**
         * data : {"date":"2018-11-28","weekday":"星期三","animalsYear":"狗","suit":"祭祀.会亲友.嫁娶.沐浴.修造.动土.祈福.开光.塑绘.出行.订盟.纳采.裁衣.入殓.除服.成服.移柩.启攒.赴任.竖柱.上梁.纳财.扫舍.栽种.纳畜.伐木.","avoid":"入宅.作灶.安床.开仓.","year-month":"2018-11","lunar":"十月廿一","lunarYear":"戊戌年"}
         */

        private DataBean data;

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * date : 2018-11-28
             * weekday : 星期三
             * animalsYear : 狗
             * suit : 祭祀.会亲友.嫁娶.沐浴.修造.动土.祈福.开光.塑绘.出行.订盟.纳采.裁衣.入殓.除服.成服.移柩.启攒.赴任.竖柱.上梁.纳财.扫舍.栽种.纳畜.伐木.
             * avoid : 入宅.作灶.安床.开仓.
             * year-month : 2018-11
             * lunar : 十月廿一
             * lunarYear : 戊戌年
             */

            private String date;
            private String weekday;
            private String animalsYear;
            private String suit;
            private String avoid;
            @SerializedName("year-month")
            private String yearmonth;
            private String lunar;
            private String lunarYear;

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getWeekday() {
                return weekday;
            }

            public void setWeekday(String weekday) {
                this.weekday = weekday;
            }

            public String getAnimalsYear() {
                return animalsYear;
            }

            public void setAnimalsYear(String animalsYear) {
                this.animalsYear = animalsYear;
            }

            public String getSuit() {
                return suit;
            }

            public void setSuit(String suit) {
                this.suit = suit;
            }

            public String getAvoid() {
                return avoid;
            }

            public void setAvoid(String avoid) {
                this.avoid = avoid;
            }

            public String getYearmonth() {
                return yearmonth;
            }

            public void setYearmonth(String yearmonth) {
                this.yearmonth = yearmonth;
            }

            public String getLunar() {
                return lunar;
            }

            public void setLunar(String lunar) {
                this.lunar = lunar;
            }

            public String getLunarYear() {
                return lunarYear;
            }

            public void setLunarYear(String lunarYear) {
                this.lunarYear = lunarYear;
            }
        }
    }
}
