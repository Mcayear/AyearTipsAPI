package ayeartipsapi.configLoad;

import ayeartipsapi.Main;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;
import java.io.File;

/**
 * @author Luckily_Baby
 * @date 2020/5/21 15:17
 */
public class LoadLanguage {
    public static Config lang;
    public static String titleShopMenu;
    public static String textShopMenu;
    public static String noSale;
    public static String titleBuyPage;
    public static String textBuyPage;
    public static String notStackable;
    public static String myProfileMenu;
    public static String myProfileCurrent;


    public static void loadCfgData(){
        if(!new File(Main.getPlugin().getDataFolder()+File.separator+"language", "zh_CN.yml").exists()) {
            Main.getPlugin().getLogger().info(TextFormat.BLUE+"未找到zh_CN.yml，正在创建...");
            lang = new Config(new File(Main.getPlugin().getDataFolder()+File.separator+"language", "zh_CN.yml"), Config.YAML);
            lang.set("ShopMenu.title", "称号商城");
            lang.set("ShopMenu.text", "");
            lang.set("ShopMenu.noSale", "非卖品");
            lang.set("BuyPage.title", "购买页面");
            lang.set("BuyPage.text", "");
            lang.set("BuyPage.notStackable", "不可堆叠");
            lang.set("myProfileMenu.title", "我的称号");
            lang.set("myProfileMenu.current", "当前佩戴：");
            lang.save();
            Main.getPlugin().getLogger().info(TextFormat.GREEN+"zh_CN.yml创建完成!");
        } else {
            lang = new Config(new File(Main.getPlugin().getDataFolder()+File.separator+"language", "zh_CN.yml"), Config.YAML);
            Main.getPlugin().getLogger().info(TextFormat.YELLOW+"zh_CN.yml已找到!");
        }

        titleShopMenu = lang.getString("ShopMenu.title", "称号商城");
        textShopMenu = lang.getString("ShopMenu.text", "");
        noSale = lang.getString("ShopMenu.noSale", "非卖品");
        titleBuyPage = lang.getString("BuyPage.title", "购买页面");
        textBuyPage = lang.getString("BuyPage.text", "");
        notStackable = lang.getString("BuyPage.notStackable", "不可堆叠");

        myProfileMenu = lang.getString("myProfileMenu.title", "我的称号");
        myProfileCurrent = lang.getString("myProfileMenu.current", "当前佩戴：");

        Main.getPlugin().getLogger().info(TextFormat.GREEN+"zh_CN.yml加载完成!");
    }
}
