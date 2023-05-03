package ayeartipsapi.configLoad;

import ayeartipsapi.Main;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;

import java.io.File;

/**
 * @author Luckily_Baby
 * @date 2020/5/21 15:17
 */
public class LoadShop {
    public static Config ShopData;
    public static void loadCfgData(){
        if(!new File(Main.getPlugin().getDataFolder(), "shop.yml").exists()) {
            Main.getPlugin().saveResource("shop.yml");
            ShopData = new Config(new File(Main.getPlugin().getDataFolder(), "shop.yml"), Config.YAML);
        } else {
            ShopData = new Config(new File(Main.getPlugin().getDataFolder(), "shop.yml"), Config.YAML);
        }
        Main.getPlugin().getLogger().info(TextFormat.GREEN+"shop.yml加载完成!");
    }
}
