package com.clark.mvc;

@SuppressWarnings("rawtypes")
class SignalReceiverHolder {
    final String name;
    final SignalReceiver function;

    SignalReceiverHolder(String name, SignalReceiver function) {
        super();
        this.name = name;
        this.function = function;
    }
}
