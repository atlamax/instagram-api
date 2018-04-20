package com.mobcrush.instagram.request.payload;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class EndLivePayload {

    private String _uid;
    private String _uuid;
    private String _csrftoken;

    public String get_uid() {
        return _uid;
    }

    public void set_uid(String _uid) {
        this._uid = _uid;
    }

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

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("_uid", _uid)
                .append("_uuid", _uuid)
                .append("_csrftoken", _csrftoken)
                .appendSuper(super.toString())
                .toString();
    }
}
