package com.example.wordhelper.bean;

import java.util.List;

public class EnglishSearchResultBean {

    /**
     * word_name : fuck
     * is_CRI : 1
     * exchange : {"word_pl":["fucks"],"word_third":["fucks"],"word_past":["fucked"],"word_done":["fucked"],"word_ing":["fucking"],"word_er":"","word_est":""}
     * symbols : [{"ph_en":"fʌk","ph_am":"fʌk","ph_other":"","ph_en_mp3":"http://res.iciba.com/resource/amp3/oxford/0/73/39/7339b287d5b107d13619496623a5ef7c.mp3","ph_am_mp3":"http://res.iciba.com/resource/amp3/1/0/99/75/99754106633f94d350db34d548d6091a.mp3","ph_tts_mp3":"http://res-tts.iciba.com/9/9/7/99754106633f94d350db34d548d6091a.mp3","parts":[{"part":"vt.& vi.","means":["与（某人）性交","（表示气愤、厌恶、惊奇的粗话）他妈的"]},{"part":"n.","means":["性交","杂种，讨厌透顶的人","一丁点儿"]},{"part":"vi.","means":["鬼混","乱弄（常与with连用）"]},{"part":"vt.","means":["不公道地（或粗暴地、生硬地）对待","欺骗","（笨手笨脚地）搞坏","粗制滥造（常与up连用）"]},{"part":"int.","means":["他妈的，滚开，混账（常后接you）"]}]}]
     */

    private String word_name;
    private String is_CRI;
    private ExchangeBean exchange;
    private List<SymbolsBean> symbols;

    public String getWord_name() {
        return word_name;
    }

    public void setWord_name(String word_name) {
        this.word_name = word_name;
    }

    public String getIs_CRI() {
        return is_CRI;
    }

    public void setIs_CRI(String is_CRI) {
        this.is_CRI = is_CRI;
    }

    public ExchangeBean getExchange() {
        return exchange;
    }

    public void setExchange(ExchangeBean exchange) {
        this.exchange = exchange;
    }

    public List<SymbolsBean> getSymbols() {
        return symbols;
    }

    public void setSymbols(List<SymbolsBean> symbols) {
        this.symbols = symbols;
    }

    public static class ExchangeBean {
        /**
         * word_pl : ["fucks"]
         * word_third : ["fucks"]
         * word_past : ["fucked"]
         * word_done : ["fucked"]
         * word_ing : ["fucking"]
         * word_er :
         * word_est :
         */

        private String word_er;
        private String word_est;
        private List<String> word_pl;
        private List<String> word_third;
        private List<String> word_past;
        private List<String> word_done;
        private List<String> word_ing;

        public String getWord_er() {
            return word_er;
        }

        public void setWord_er(String word_er) {
            this.word_er = word_er;
        }

        public String getWord_est() {
            return word_est;
        }

        public void setWord_est(String word_est) {
            this.word_est = word_est;
        }

        public List<String> getWord_pl() {
            return word_pl;
        }

        public void setWord_pl(List<String> word_pl) {
            this.word_pl = word_pl;
        }

        public List<String> getWord_third() {
            return word_third;
        }

        public void setWord_third(List<String> word_third) {
            this.word_third = word_third;
        }

        public List<String> getWord_past() {
            return word_past;
        }

        public void setWord_past(List<String> word_past) {
            this.word_past = word_past;
        }

        public List<String> getWord_done() {
            return word_done;
        }

        public void setWord_done(List<String> word_done) {
            this.word_done = word_done;
        }

        public List<String> getWord_ing() {
            return word_ing;
        }

        public void setWord_ing(List<String> word_ing) {
            this.word_ing = word_ing;
        }
    }

    public static class SymbolsBean {
        /**
         * ph_en : fʌk
         * ph_am : fʌk
         * ph_other :
         * ph_en_mp3 : http://res.iciba.com/resource/amp3/oxford/0/73/39/7339b287d5b107d13619496623a5ef7c.mp3
         * ph_am_mp3 : http://res.iciba.com/resource/amp3/1/0/99/75/99754106633f94d350db34d548d6091a.mp3
         * ph_tts_mp3 : http://res-tts.iciba.com/9/9/7/99754106633f94d350db34d548d6091a.mp3
         * parts : [{"part":"vt.& vi.","means":["与（某人）性交","（表示气愤、厌恶、惊奇的粗话）他妈的"]},{"part":"n.","means":["性交","杂种，讨厌透顶的人","一丁点儿"]},{"part":"vi.","means":["鬼混","乱弄（常与with连用）"]},{"part":"vt.","means":["不公道地（或粗暴地、生硬地）对待","欺骗","（笨手笨脚地）搞坏","粗制滥造（常与up连用）"]},{"part":"int.","means":["他妈的，滚开，混账（常后接you）"]}]
         */

        private String ph_en;
        private String ph_am;
        private String ph_other;
        private String ph_en_mp3;
        private String ph_am_mp3;
        private String ph_tts_mp3;
        private List<PartsBean> parts;

        public String getPh_en() {
            return ph_en;
        }

        public void setPh_en(String ph_en) {
            this.ph_en = ph_en;
        }

        public String getPh_am() {
            return ph_am;
        }

        public void setPh_am(String ph_am) {
            this.ph_am = ph_am;
        }

        public String getPh_other() {
            return ph_other;
        }

        public void setPh_other(String ph_other) {
            this.ph_other = ph_other;
        }

        public String getPh_en_mp3() {
            return ph_en_mp3;
        }

        public void setPh_en_mp3(String ph_en_mp3) {
            this.ph_en_mp3 = ph_en_mp3;
        }

        public String getPh_am_mp3() {
            return ph_am_mp3;
        }

        public void setPh_am_mp3(String ph_am_mp3) {
            this.ph_am_mp3 = ph_am_mp3;
        }

        public String getPh_tts_mp3() {
            return ph_tts_mp3;
        }

        public void setPh_tts_mp3(String ph_tts_mp3) {
            this.ph_tts_mp3 = ph_tts_mp3;
        }

        public List<PartsBean> getParts() {
            return parts;
        }

        public void setParts(List<PartsBean> parts) {
            this.parts = parts;
        }

        public static class PartsBean {
            /**
             * part : vt.& vi.
             * means : ["与（某人）性交","（表示气愤、厌恶、惊奇的粗话）他妈的"]
             */

            private String part;
            private List<String> means;

            public String getPart() {
                return part;
            }

            public void setPart(String part) {
                this.part = part;
            }

            public List<String> getMeans() {
                return means;
            }

            public void setMeans(List<String> means) {
                this.means = means;
            }
        }
    }
}
