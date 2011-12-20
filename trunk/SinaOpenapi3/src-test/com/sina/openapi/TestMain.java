package com.sina.openapi;

import static com.clark.func.Functions.isBlank;
import static com.clark.func.Functions.readFileToString;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestMain {

    public static void main(String[] args) throws IOException {
        String content = readFileToString(new File("text.txt"), "UTF-8");
        String boundary = findBoundary(content);
        String[] parts = content.split(boundary);
        String name = null;
        String value = null;
        for (String part : parts) {
            if (part.contains("Content-Transfer-Encoding: binary")) {
                continue;
            }

            if (part.endsWith("\r\n")) {
                Matcher matcher = PATTERN_NAME.matcher(part);
                if (matcher.find()) {
                    name = matcher.group(1);
                    value = part.substring(0, part.length() - 2);
                    value = value.substring(value.lastIndexOf("\r\n") + 2);
                }

                System.out.println("name=" + name + "\tvalue:" + value);
            }
        }
    }

    private static final Pattern PATTERN_NAME = Pattern
            .compile("Content\\-Disposition: form\\-data; name=\"(.+)\"");

    private static String findBoundary(String content) {
        if (isBlank(content) || !content.endsWith("--\r\n")) {
            return null;
        }

        int index = content.indexOf("\r\n");
        return content.substring(0, index);
    }
}
