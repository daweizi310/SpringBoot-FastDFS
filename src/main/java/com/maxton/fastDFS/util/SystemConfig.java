//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.maxton.fastDFS.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(
    prefix = "system"
)
public class SystemConfig {
    private String path;
    private String domain;
    private String attachment_prefix_path;
    private String attachment_prefix_url;

    public SystemConfig() {
    }

    public String getAttachment_prefix_path() {
        return this.attachment_prefix_path;
    }

    public void setAttachment_prefix_path(String attachment_prefix_path) {
        this.attachment_prefix_path = attachment_prefix_path;
    }

    public String getAttachment_prefix_url() {
        return this.attachment_prefix_url;
    }

    public void setAttachment_prefix_url(String attachment_prefix_url) {
        this.attachment_prefix_url = attachment_prefix_url;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDomain() {
        return this.domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
}
