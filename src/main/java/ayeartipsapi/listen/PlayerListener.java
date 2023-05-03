package ayeartipsapi.listen;

import ayeartipsapi.configLoad.LoadCfg;
import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.ConfigSection;
import cn.nukkit.utils.TextFormat;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import ayeartipsapi.Main;

public class PlayerListener implements Listener {
    /**
     * 创建用户配置文件
     *
     * @param e 玩家加入事件
     */
    @EventHandler
    public void onPlayerJoins(PlayerJoinEvent e){
        Player p = e.getPlayer();
        File pf = new File(Main.getPlugin().getDataFolder() + File.separator + "players", p.getName() + ".yml");
        if (!pf.exists()) {
            File fileParents = pf.getParentFile();
            //新建文件夹
            if (!fileParents.exists()) {
                fileParents.mkdirs();
            }
            ConfigSection config = LoadCfg.cfgData.getSection("title").getSection("initial");
            try {
                pf.createNewFile();
                Config pfs = new Config(pf, Config.YAML);
                ConfigSection titleList = new ConfigSection();
                if (config != null) {
                    pfs.set("using", null);
                    pfs.set("list", titleList);
                } else if (config.exists("name") && config.exists("day")) {
                    pfs.set("using", config.getString("name"));
                    titleList.set(config.getString("name"), config.getLong("day"));
                    pfs.set("list", titleList);
                } else if (config.exists("name")) {
                    pfs.set("using", config.getString("name"));
                    titleList.set(config.getString("name"), (long) -1);
                }
                pfs.save();
            } catch (IOException es) {
                es.printStackTrace();
                Main.getPlugin().getLogger().info(TextFormat.RED + "[NTitle] 插件内部错误: ERROR-301");
            }
        }
    }
}