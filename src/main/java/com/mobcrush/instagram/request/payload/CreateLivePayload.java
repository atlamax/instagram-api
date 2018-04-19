package com.mobcrush.instagram.request.payload;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class CreateLivePayload {

    private String _uuid;
    private String _csrftoken;
    private String preview_height;
    private String preview_width;
    private String broadcast_message;
    private String broadcast_type;
    private String internal_only;

    public String get_uuid() {
        return _uuid;
    }

    public void set_uuid(String _uuid) {
        this._uuid = _uuid;
    }

    public String get_csrftoken() {
        return _csrftoken;
    }

    public void set_csrftoken(String _csrftoken) {
        this._csrftoken = _csrftoken;
    }

    public String getPreview_height() {
        return preview_height;
    }

    public void setPreview_height(String preview_height) {
        this.preview_height = preview_height;
    }

    public String getPreview_width() {
        return preview_width;
    }

    public void setPreview_width(String preview_width) {
        this.preview_width = preview_width;
    }

    public String getBroadcast_message() {
        return broadcast_message;
    }

    public void setBroadcast_message(String broadcast_message) {
        this.broadcast_message = broadcast_message;
    }

    public String getBroadcast_type() {
        return broadcast_type;
    }

    public void setBroadcast_type(String broadcast_type) {
        this.broadcast_type = broadcast_type;
    }

    public String getInternal_only() {
        return internal_only;
    }

    public void setInternal_only(String internal_only) {
        this.internal_only = internal_only;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("_uuid", _uuid)
                .append("_csrftoken", _csrftoken)
                .append("preview_height", preview_height)
                .append("preview_width", preview_width)
                .append("broadcast_message", broadcast_message)
                .append("broadcast_type", broadcast_type)
                .append("internal_only", internal_only)
                .appendSuper(super.toString())
                .toString();
    }
}
