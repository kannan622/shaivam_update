package org.shaivam.interfaces;

public interface VolleyCallback {
    public void Success(int statuscode, String response);
    public void Failure(int statuscode, String errorResponse);
}
