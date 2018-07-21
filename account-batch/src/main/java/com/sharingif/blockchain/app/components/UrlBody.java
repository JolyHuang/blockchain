package com.sharingif.blockchain.app.components;

/**
 * UrlBody
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/21 下午1:20
 */
public class UrlBody<T> {

    private String url;

    private T body;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UrlBody{");
        sb.append("url='").append(url).append('\'');
        sb.append(", body=").append(body);
        sb.append('}');
        return sb.toString();
    }
}
