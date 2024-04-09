package com.qxy.notice.domain;


import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class EnterpriseRobotMessage implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    public final static String MSG_TYPE_TEXT = "text";
    public final static String MSG_TYPE_MARKDOWN = "markdown";
    public final static String MSG_TYPE_IMAGE = "image";
    public final static String MSG_TYPE_NEWS = "news";

    /**
     * 推送robot key（不填则由实现决定如何处理）
     * 优先使用pushKey
     */
    private String robotKey;
    /**
     * 消息类型枚举
     */
    private final String msgtype;

    /**
     * type=text时需要去构建的实体
     */
    private TextType text;
    /**
     * type=markdown时需要去构建的实体
     */
    private MarkdownType markdown;
    /**
     * type=image时需要去构建的实体
     */
    private ImageType image;
    /**
     * type=news时需要去构建的实体
     */
    private NewsType news;
    //endregion


    public TextType getText() {
        return text;
    }

    public MarkdownType getMarkdown() {
        return markdown;
    }

    public ImageType getImage() {
        return image;
    }

    public NewsType getNews() {
        return news;
    }

    /**
     * 构建一个Text类型消息实体Builder
     *
     * @param content 消息内容
     * @return 返回builder
     */
    public static TextBuilder textBuilder(String content) {
        return new TextBuilder(content);
    }

    /**
     * 构建一个Markdown类型消息实体
     *
     * @param content 消息内容（Markdown格式）
     * @return 返回builder
     */
    public static MarkdownBuilder markdownBuilder(String content) {
        return new MarkdownBuilder(content);
    }

    /**
     * 构建一个Image类型消息实体
     *
     * @param base64 图片内容的base64编码；无需增加类似data:image/png;base64,的头。这一点要注意，因为在线转换工具大多会带上这个前缀
     * @param md5    图片内容（base64编码前）的md5值;
     * @return 返回builder
     */
    public static ImageBuilder imageBuilder(String base64, String md5) {
        return new ImageBuilder(base64, md5);
    }

    /**
     * 构建一个news类型消息实体
     *
     * @param title       标题，不超过128个字节，超过会自动截断
     * @param url         点击后跳转的链接。
     * @param description 描述，不超过512个字节，超过会自动截断 非必填
     * @param picUrl      图文消息的图片链接，支持JPG、PNG格式，较好的效果为大图 1068*455，小图150*150。 非必填
     * @return 返回builder
     */
    public static NewsBuilder newsBuilder(String title, String url, String description, String picUrl) {
        return new NewsBuilder(title, url, description, picUrl);
    }

    public static NewsBuilder newsBuilder(String title, String url, String description) {
        return new NewsBuilder(title, url, description, null);
    }

    public static NewsBuilder newsBuilder(String title, String url) {
        return new NewsBuilder(title, url, null, null);
    }

    public String getRobotKey() {
        return robotKey;
    }

    public void setRobotKey(String robotKey) {
        this.robotKey = robotKey;
    }

    public String getMsgtype() {
        return msgtype;
    }


    //region 消息实体类
    public static class TextType {
        private final String content;
        /**
         * userid的列表，提醒群中的指定成员(@某个成员)，@all表示提醒所有人，如果开发者获取不到userid，可以使用mentioned_mobile_list
         */
        private final List<String> mentioned_list;
        /**
         * 手机号列表，提醒手机号对应的群成员(@某个成员)，@all表示提醒所有人
         */
        private final List<String> mentioned_mobile_list;
        public TextType(String content, List<String> mentionedList, List<String> mentionedMobileList) {
            this.content = content;
            this.mentioned_list = mentionedList;
            this.mentioned_mobile_list = mentionedMobileList;
        }

        public String getContent() {
            return content;
        }

        public List<String> getMentioned_list() {
            return mentioned_list;
        }

        public List<String> getMentioned_mobile_list() {
            return mentioned_mobile_list;
        }
    }

    public static class MarkdownType {

        /**
         * markdown内容，最长不超过4096个字节，必须是utf8编码
         */

        private final String content;
        MarkdownType(String content) {
            this.content = content;
        }

        public String getContent() {
            return content;
        }
    }

    public static class ImageType {
        /**
         * 图片内容的base64编码
         */
        private final String base64;
        /**
         * 图片内容（base64编码前）的md5值
         */
        private final String md5;

        public ImageType(String base64, String md5) {
            this.base64 = base64;
            this.md5 = md5;
        }

        public String getBase64() {
            return base64;
        }

        public String getMd5() {
            return md5;
        }
    }

    public static class NewsType {
        /**
         * 图文消息，一个图文消息支持1到8条图文
         */
        private final List<Article> articles;

        public NewsType(List<Article> articles) {
            this.articles = articles;
        }

        /**
         * 图文消息实体
         */
        public static class Article {
            /**
             * 点击后跳转的链接。
             */
            private String url;

            public Article(String title, String description, String url, String picUrl) {
            }
        }

        public List<Article> getArticles() {
            return articles;
        }
    }
    //endregion


    //region 各类构造方法，用于构建不同的消息类型实体

    private EnterpriseRobotMessage(NewsType news) {
        this.msgtype = MSG_TYPE_NEWS;
        this.news = news;
    }

    private EnterpriseRobotMessage(NewsType news, String robotKey) {
        this.msgtype = MSG_TYPE_NEWS;
        this.news = news;
        this.robotKey = robotKey;
    }

    private EnterpriseRobotMessage(ImageType image) {
        this.msgtype = MSG_TYPE_IMAGE;
        this.image = image;
    }

    private EnterpriseRobotMessage(ImageType image, String robotKey) {
        this.msgtype = MSG_TYPE_IMAGE;
        this.image = image;
        this.robotKey = robotKey;
    }

    private EnterpriseRobotMessage(MarkdownType markdown) {
        this.msgtype = MSG_TYPE_MARKDOWN;
        this.markdown = markdown;
    }

    private EnterpriseRobotMessage(MarkdownType markdown, String robotKey) {
        this.msgtype = MSG_TYPE_MARKDOWN;
        this.markdown = markdown;
        this.robotKey = robotKey;
    }

    private EnterpriseRobotMessage(TextType text) {
        this.msgtype = MSG_TYPE_TEXT;
        this.text = text;
    }

    private EnterpriseRobotMessage(TextType text, String robotKey) {
        this.msgtype = MSG_TYPE_TEXT;
        this.text = text;
        this.robotKey = robotKey;
    }
    //endregion

    //region 不同消息类型的Builder

    /**
     * Text类型消息Builder
     */
    public static class TextBuilder implements Serializable {

        @Serial
        private static final long serialVersionUID = 1L;

        /**
         * 当需要@all时候需要填入mentioned_list或mentioned_mobile_list中的
         */
        private static final String AT_ALL = "@all";
        private final String content;
        private List<String> mentionedList;
        private List<String> mentionedMobileList;

        public String getContent() {
            return content;
        }

        public List<String> getMentionedList() {
            return mentionedList;
        }

        public void setMentionedList(List<String> mentionedList) {
            this.mentionedList = mentionedList;
        }

        public List<String> getMentionedMobileList() {
            return mentionedMobileList;
        }

        public void setMentionedMobileList(List<String> mentionedMobileList) {
            this.mentionedMobileList = mentionedMobileList;
        }

        /**
         * 构造方法，消息体必填
         *
         * @param content 消息体
         */
        private TextBuilder(String content) {
            this.content = content;
        }

        /**
         * 添加userId，用于在消息中@某人
         *
         * @param mentioned 企业微信userId
         * @return 返回建造者本身
         */
        public TextBuilder addUserIdForAt(String... mentioned) {
            if (mentioned != null && mentioned.length > 0) {
                if (mentionedList == null) {
                    mentionedList = new ArrayList<>();
                }
                mentionedList.addAll(Arrays.asList(mentioned));
            }
            return this;
        }

        /**
         * 添加手机号，用于添加某人
         * 当无法获取到userId的时候，则可以添加手机号（需要是企业微信绑定的）
         *
         * @param mobiles 企业微信userId
         */
        public void addMobileForAt(String... mobiles) {
            if (mobiles != null && mobiles.length > 0) {
                if (mentionedMobileList == null) {
                    mentionedMobileList = new ArrayList<>();
                }
                mentionedMobileList.addAll(Arrays.asList(mobiles));
            }
        }

        public TextBuilder atAll() {
            addMobileForAt(AT_ALL);
            return this;
        }

        public EnterpriseRobotMessage build() {
            return new EnterpriseRobotMessage(new TextType(content, mentionedList, mentionedMobileList));
        }

        public EnterpriseRobotMessage build(String robotKey) {
            return new EnterpriseRobotMessage(new TextType(content, mentionedList, mentionedMobileList), robotKey);
        }
    }

    /**
     * Markdown类型消息Builder
     */
    public static class MarkdownBuilder {
        /**
         * markdown内容，最长不超过4096个字节，必须是utf8编码
         */
        private final String content;

        public MarkdownBuilder(String content) {
            this.content = content;
        }

        public EnterpriseRobotMessage build() {
            return new EnterpriseRobotMessage(new MarkdownType(content));
        }

        public EnterpriseRobotMessage build(String robotKey) {
            return new EnterpriseRobotMessage(new MarkdownType(content), robotKey);
        }

    }

    /**
     * Image类型消息Builder
     */
    public static class ImageBuilder {
        /**
         * 图片内容的base64编码
         */
        private final String base64;
        /**
         * 图片内容（base64编码前）的md5值
         */
        private final String md5;

        public ImageBuilder(String base64, String md5) {
            this.base64 = base64;
            this.md5 = md5;
        }

        public EnterpriseRobotMessage build() {
            return new EnterpriseRobotMessage(new ImageType(base64, md5));
        }

        public EnterpriseRobotMessage build(String robotKey) {
            return new EnterpriseRobotMessage(new ImageType(base64, md5), robotKey);
        }
    }

    /**
     * News类型消息Builder
     */
    public static class NewsBuilder {
        /**
         * 图文消息，一个图文消息支持1到8条图文
         */
        private final List<NewsType.Article> articles;

        /**
         * 构造方法
         *
         * @param title       标题
         * @param url         跳转地址
         * @param description 描述（非必填）
         * @param picUrl      图片地址（非必填）
         */
        private NewsBuilder(String title, String url, String description, String picUrl) {
            this.articles = new ArrayList<>(Collections.singletonList(new NewsType.Article(title, description, url, picUrl)));
        }

        /**
         * 新增一个图文
         *
         * @param title       标题
         * @param url         跳转地址
         * @param description 描述（可为空）
         * @param picUrl      图片地址
         * @return 返回builder
         */
        public NewsBuilder addArticles(String title, String url, String description, String picUrl) {
            articles.add(new NewsType.Article(title, description, url, picUrl));
            return this;
        }

        /**
         * 新增一个图文
         *
         * @param title       标题
         * @param url         跳转地址
         * @param description 描述（可为空）
         * @return 返回builder
         */
        public NewsBuilder addArticles(String title, String url, String description) {
            return addArticles(title, url, description, null);
        }

        /**
         * 新增一个图文
         *
         * @param title 标题
         * @param url   跳转地址
         * @return 返回builder
         */
        public NewsBuilder addArticles(String title, String url) {
            return addArticles(title, url, null, null);
        }

        public EnterpriseRobotMessage build() {
            return new EnterpriseRobotMessage(new NewsType(articles));
        }

        public EnterpriseRobotMessage build(String robotKey) {
            return new EnterpriseRobotMessage(new NewsType(articles), robotKey);
        }
    }
}