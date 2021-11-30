package com.bilgeadam.main;

import com.bilgeadam.bautils.BAUtils;
import com.bilgeadam.clientside.menuitems.MenuItems;
import com.bilgeadam.globalization.Globalization;

public class MainClass {
    private static MenuItems menuItems = MenuItems.getInstance();
    private static BAUtils baUtils = BAUtils.getInstance();

    public static void main(String[] args) {
        Globalization.setLanguageBundle();
        menuItems.showMainMenu(Globalization.getLanguageBundle());
    }
}
