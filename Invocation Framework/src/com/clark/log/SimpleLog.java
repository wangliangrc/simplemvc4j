package com.clark.log;

class SimpleLog extends Log {

    SimpleLog(String tag) {
        super(tag);
    }

    @Override
    public void v(CharSequence text) {
        outputText("[verbose]", text);
    }

    private void outputText(String type, CharSequence text) {
        text = text == null ? "" : text;
        StringBuilder builder = new StringBuilder();
        builder.append("[").append(type).append("]--").append(getTag())
                .append("-- ").append(text);
        System.err.println(builder);
    }

    @Override
    public void v(CharSequence text, Throwable throwable) {
        outputThrowable("[verbose]", text, throwable);
    }

    private void outputThrowable(String type, CharSequence text,
            Throwable throwable) {
        if (throwable == null) {
            outputText(type, text);
            return;
        }

        synchronized (System.err) {
            text = text == null ? "" : text;
            outputText(type, text);
            throwable.printStackTrace();
        }
    }

    @Override
    public void d(CharSequence text) {
        outputText("[debug]", text);
    }

    @Override
    public void d(CharSequence text, Throwable throwable) {
        outputThrowable("[debug]", text, throwable);
    }

    @Override
    public void i(CharSequence text) {
        outputText("[info]", text);
    }

    @Override
    public void i(CharSequence text, Throwable throwable) {
        outputThrowable("[info]", text, throwable);
    }

    @Override
    public void w(CharSequence text) {
        outputText("[warn]", text);
    }

    @Override
    public void w(CharSequence text, Throwable throwable) {
        outputThrowable("[warn]", text, throwable);
    }

    @Override
    public void e(CharSequence text) {
        outputText("[error]", text);
    }

    @Override
    public void e(CharSequence text, Throwable throwable) {
        outputThrowable("[error]", text, throwable);
    }

}
