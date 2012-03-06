package com.clark.ifk.v2;

interface AsynchronousInvoker extends Invoker {
    Invoker ui();

    Invoker background();

    Invoker callback(String id, Object receiver);

    Invoker callbackUI();

    Invoker callbackBackground();
}
