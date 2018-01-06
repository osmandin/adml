package edu.mit.controller;


public class Search {

    private long id;

    private String content;

    private String any;

    private String all;

    private String entire;

    private String keywordOption;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAny() {
        return any;
    }

    public void setAny(String any) {
        this.any = any;
    }

    public String getAll() {
        return all;
    }

    public void setAll(String all) {
        this.all = all;
    }

    public String getEntire() {
        return entire;
    }

    public void setEntire(String entire) {
        this.entire = entire;
    }

    public String getKeywordOption() {
        return keywordOption;
    }

    public void setKeywordOption(String keywordOption) {
        this.keywordOption = keywordOption;
    }

    @Override
    public String toString() {
        return "Search{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", any='" + any + '\'' +
                ", all='" + all + '\'' +
                ", entire='" + entire + '\'' +
                ", keywordOption='" + keywordOption + '\'' +
                '}';
    }
}
