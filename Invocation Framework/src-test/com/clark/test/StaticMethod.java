package com.clark.test;

import com.clark.ifk.Message;
import com.clark.ifk.Messenger;

public class StaticMethod {

    @Messenger({ "1", "2" })
    static void test1(Message message) {
        System.out.println("StaticMethod.test1()");
        System.out.println(message);
    }
}
