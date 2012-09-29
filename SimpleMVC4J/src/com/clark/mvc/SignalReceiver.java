package com.clark.mvc;

interface SignalReceiver<T> {

    void onReceive(Signal<T> notification);

}
