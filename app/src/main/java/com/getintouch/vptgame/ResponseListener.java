package com.getintouch.vptgame;

/**
 * Created by Dell on 09-03-2018.
 */

public interface ResponseListener {
    void onErrorReceived();
    void onResponseReceived(String s);
}
