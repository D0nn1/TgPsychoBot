package com.example.tgpsychobot.Telegram.util.KeyboardRowUtil;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class KeyboardRowsAuto extends ArrayList<KeyboardRowAuto> {



    public List<KeyboardRow> getList() {
        List<KeyboardRow> list = new ArrayList<>();
        for (KeyboardRowAuto row : this) {
            list.add(row);
        }
        return list;
    }

    public KeyboardRowsAuto addRow(KeyboardRowAuto rowAuto) {
        super.add(rowAuto);
        return this;
    }
}
