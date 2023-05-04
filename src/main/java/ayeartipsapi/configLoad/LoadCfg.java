package ayeartipsapi.configLoad;

import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;
import ayeartipsapi.Main;
import java.io.File;

/**
 * @author Luckily_Baby
 * @date 2020/5/21 15:17
 */
public class LoadCfg {
    public static Config cfgData;
    public static String defaultTitle;
    public static void loadCfgData(){
        if(!new File(Main.getPlugin().getDataFolder(), "config.yml").exists()) {
            Main.getPlugin().saveResource("config.yml");
            cfgData = new Config(new File(Main.getPlugin().getDataFolder(), "config.yml"), Config.YAML);
        } else {
            cfgData = new Config(new File(Main.getPlugin().getDataFolder(), "config.yml"), Config.YAML);
        }
        defaultTitle = cfgData.getSection("title").getString("default");
        Main.getPlugin().getLogger().info(TextFormat.GREEN+"config.yml加载完成!");
    }
}
