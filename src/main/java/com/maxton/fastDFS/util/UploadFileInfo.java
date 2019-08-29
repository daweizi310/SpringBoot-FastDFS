package com.maxton.fastDFS.util;

/**
 * @Description
 * @Author maxton.zhang
 * @Date 2019/8/1 16:00
 * @Version 1.0
 */
public class UploadFileInfo {

    private String name;
    private String old_name;
    private String file_type;
    private String suffix;
    private String prefix_url;
    private String context_path;
    private String url;
    private long size;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOld_name() {
        return old_name;
    }

    public void setOld_name(String old_name) {
        this.old_name = old_name;
    }

    public String getFile_type() {
        return file_type;
    }

    public void setFile_type(String file_type) {
        this.file_type = file_type;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getPrefix_url() {
        return prefix_url;
    }

    public void setPrefix_url(String prefix_url) {
        this.prefix_url = prefix_url;
    }

    public String getContext_path() {
        return context_path;
    }

    public void setContext_path(String context_path) {
        this.context_path = context_path;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

}
