package com.sina.weibotv.controller;

import static com.clark.mvc.MultiCore.registerController;

public class CommandTable {

    public static void init() {
        registerController(AccountCommand.class);
        registerController(NetworkCommand.class);
        registerController(GlobalCommand.class);
    }

}
