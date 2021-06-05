//package com.yalta.telegram.service;
//
//import com.yalta.telegram.command.CallbackCommand;
//import com.yalta.telegram.command.TextCommand;
//import org.springframework.stereotype.Service;
//
//@Service
//public class MessageParser {
//
//    public TextCommand textParse(String cmd) {
//        return TextCommand.findOnDesc(cmd);
//    }
//
//    public CallbackCommand callbackParse(String data) {
//        return CallbackCommand.findByData(data);
//    }
//
//}