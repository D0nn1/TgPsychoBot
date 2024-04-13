package com.example.tgpsychobot.util;

import com.example.tgpsychobot.util.KeyboardRowUtil.KeyboardRowAuto;
import com.example.tgpsychobot.util.KeyboardRowUtil.KeyboardRowsAuto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

public class KeyboardRowsForPsychoBot {


    public static List<KeyboardRow> start () {
        return new KeyboardRowsAuto()
                .addRow(new KeyboardRowAuto().addButton("music", "weather"))
                .addRow(new KeyboardRowAuto().addButton("windows", "help"))
                .getList();
    }
}
