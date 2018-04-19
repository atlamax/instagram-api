package com.mobcrush.instagram.request.payload;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class StartLivePayload {

    private String _uuid;
    private String _csrftoken;
    private String should_send_notifications;

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

    public String getShould_send_notifications() {
        return should_send_notifications;
    }

    public void setShould_send_notifications(String should_send_notifications) {
        this.should_send_notifications = should_send_notifications;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("_uuid", _uuid)
                .append("_csrftoken", _csrftoken)
                .append("should_send_notifications", should_send_notifications)
                .appendSuper(super.toString())
                .toString();
    }
}
