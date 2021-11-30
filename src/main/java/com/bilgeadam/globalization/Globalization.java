package com.bilgeadam.globalization;

import com.bilgeadam.bautils.BAUtils;
import com.bilgeadam.clientside.menuitems.MenuItems;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;

public class Globalization {
    private static ResourceBundle langBundle;
    private static Scanner scanner = new Scanner(System.in);
    private static Locale locale;

    private static ResourceBundle languageBundleTr(){
        locale = new Locale("tr");
        ResourceBundle languageBundleTr = ResourceBundle.getBundle("com.bilgeadam.globalization.texts",locale);
        return languageBundleTr;
    }

    private static ResourceBundle languageBundleEn(){
        locale = new Locale("en");
        ResourceBundle languageBundleEn = ResourceBundle.getBundle("com.bilgeadam.globalization.texts",locale);
        return languageBundleEn;
    }

    private static ResourceBundle languageBundleDe(){
        locale = new Locale("de");
        ResourceBundle languageBundleDe = ResourceBundle.getBundle("com.bilgeadam.globalization.texts",locale);
        return languageBundleDe;
    }

    public static ResourceBundle getLanguageBundle() {
        return langBundle;
    }

    public static void setLanguageBundle() {
        ResourceBundle bundle = Globalization.languageBundleEn();
        int choice = 0;

        BAUtils baUtils = BAUtils.getInstance();
        baUtils.header(bundle.getString("Globalization.WELCOME"));

        MenuItems menuItems = MenuItems.getInstance();

        do {
            baUtils.menuBodyCreator(menuItems.getLanQueryList(), "Please Make Your Language Selection");
            choice = scanner.nextInt();
            if (choice < 0 || choice > 3)
                System.out.println(bundle.getString("Globalization.INVALIDENTRY"));

        } while (choice < 0 || choice > 3);

        switch (choice) {
            case 1:
                bundle = Globalization.languageBundleTr();
                break;
            case 2:
                bundle = Globalization.languageBundleEn();
                break;
            case 3:
                bundle = Globalization.languageBundleDe();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + choice);
        }
        Globalization.langBundle = bundle;
    }



}
