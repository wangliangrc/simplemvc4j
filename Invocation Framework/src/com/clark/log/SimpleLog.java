package com.clark.log;

class SimpleLog extends Log {

    SimpleLog(String tag) {
        super(tag);
    }

    @Override
    public void v(boolean debug, CharSequence text) {
        outputText(debug, "[verbose]", text);
    }

    private void outputText(boolean debug, String type, CharSequence text) {
        if (!debug) {
            return;
        }
        text = text == null ? "" : text;
        StringBuilder builder = new StringBuilder();
        builder.append("[").append(type).append("]--").append(getTag())
                .append("-- ").append(text);
        System.err.println(builder);
    }

    @Override
    public void v(boolean debug, CharSequence text, Throwable throwable) {
        outputThrowable(debug, "[verbose]", text, throwable);
    }

    private void outputThrowable(boolean debug, String type, CharSequence text,
            Throwable throwable) {
        if (!debug) {
            return;
        }

        if (throwable == null) {
            outputText(debug, type, text);
            return;
        }

        synchronized (System.err) {
            text = text == null ? "" : text;
            outputText(debug, type, text);
            throwable.printStackTrace();
        }
    }

    @Override
    void d(boolean debug, CharSequence text) {
        outputText(debug, "[debug]", text);
    }

    @Override
    void d(boolean debug, CharSequence text, Throwable throwable) {
        outputThrowable(debug, "[debug]", text, throwable);
    }

    @Override
    void i(boolean debug, CharSequence text) {
        outputText(debug, "[info]", text);
    }

    @Override
    void i(boolean debug, CharSequence text, Throwable throwable) {
        outputThrowable(debug, "[info]", text, throwable);
    }

    @Override
    void w(boolean debug, CharSequence text) {
        outputText(debug, "[warn]", text);
    }

    @Override
    void w(boolean debug, CharSequence text, Throwable throwable) {
        outputThrowable(debug, "[warn]", text, throwable);
    }

    @Override
    void e(boolean debug, CharSequence text) {
        outputText(debug, "[error]", text);
    }

    @Override
    void e(boolean debug, CharSequence text, Throwable throwable) {
        outputThrowable(debug, "[error]", text, throwable);
    }

}
