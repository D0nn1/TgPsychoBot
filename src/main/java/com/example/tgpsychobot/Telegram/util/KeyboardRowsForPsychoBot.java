package com.example.tgpsychobot.Telegram.util;

import com.example.tgpsychobot.Telegram.util.KeyboardRowUtil.KeyboardRowAuto;
import com.example.tgpsychobot.Telegram.util.KeyboardRowUtil.KeyboardRowsAuto;
import com.vdurmont.emoji.EmojiParser;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

public class KeyboardRowsForPsychoBot {

    public static List<KeyboardRow> start() {
        return new KeyboardRowsAuto()
                .addRow(new KeyboardRowAuto()
                        .addButton("получить трек" + EmojiParser.parseToUnicode(":notes:"),
                                "загрузить трек" + EmojiParser.parseToUnicode(":studio_microphone:")))
                .addRow(new KeyboardRowAuto()
                        .addButton("помощь" + EmojiParser.parseToUnicode(":question:")))
                .getList();
    }
}
