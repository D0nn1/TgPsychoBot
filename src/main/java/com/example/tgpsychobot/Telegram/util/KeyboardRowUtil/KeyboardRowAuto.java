package com.example.tgpsychobot.Telegram.util.KeyboardRowUtil;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

public class KeyboardRowAuto extends KeyboardRow {


    public KeyboardRowAuto addButton(String text) {
        super.add(text);
        return this;
    }

    public KeyboardRowAuto addButton(String... text) {
        KeyboardRowAuto row = new KeyboardRowAuto();
        for (String s : text) {
            super.add(s);
        }
        return this;
    }
}
