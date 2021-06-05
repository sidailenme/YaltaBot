package com.yalta.telegram.keyboard;

import org.springframework.data.domain.Page;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class InlineKeyboardUtils {

    public static <T> InlineKeyboardMarkup numericPageKeyboard(Page<T> page) {
        int pageNumber = page.getNumber();
        int lastPageNumber = page.getTotalPages();

        ArrayList<List<InlineKeyboardButton>> keyboardTable = new ArrayList<>();
        ArrayList<InlineKeyboardButton> keyRow1 = new ArrayList<>();

        if (pageNumber > 0) {
            InlineKeyboardButton buttonFirstPage = new InlineKeyboardButton("<<< (1)");
            buttonFirstPage.setCallbackData("buttonPage_0");
            keyRow1.add(buttonFirstPage);
        }

        if (pageNumber > 1) {
            InlineKeyboardButton buttonPreviousPage = new InlineKeyboardButton("<< (" + pageNumber + ")");
            buttonPreviousPage.setCallbackData("buttonPage_" + (pageNumber - 1));
            keyRow1.add(buttonPreviousPage);
        }

        if (pageNumber + 1 < lastPageNumber) {
            InlineKeyboardButton buttonNextPage = new InlineKeyboardButton(">> (" + (pageNumber + 2) + ")");
            buttonNextPage.setCallbackData("buttonPage_" + (pageNumber + 1));
            keyRow1.add(buttonNextPage);
        }

        if (pageNumber + 2 < lastPageNumber) {
            InlineKeyboardButton buttonLastPage = new InlineKeyboardButton(">>>(" + (lastPageNumber - 1) + ")");
            buttonLastPage.setCallbackData("buttonPage_" + (lastPageNumber - 1));
            keyRow1.add(buttonLastPage);
        }

        keyboardTable.add(keyRow1);

        return new InlineKeyboardMarkup(keyboardTable);
    }
}
