package com.example.tgpsychobot.util;

import com.vdurmont.emoji.EmojiParser;

public enum AnswersForCommands {
    START("Привет!" + EmojiParser.parseToUnicode(":heart:")),
    HELP("This bot is for your mental health.\n" +
            "For help ask to @drestoonplaya.\n" +
            "Type /mydata to get your data.\n" +
            "Type /deletedata to delete your data.\n");

    private String text;

    AnswersForCommands(String s) {
        this.text = s;
    }

    @Override
    public String toString() {
        return text;
    }


}
