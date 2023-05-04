package ayeartipsapi.configLoad;

import ayeartipsapi.Main;
import cn.nukkit.Player;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.ConfigSection;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Objects;

public class PlayerConfig {
    public static Config getConfig(Player p) {
        File pfi=new File(Main.getPlugin().getDataFolder()+File.separator+"players",p.getName()+".yml");
        if(!pfi.exists()) {
            p.sendMessage("§4不存在玩家 "+p.getName()+" 的数据。");
            return null;
        }
        return new Config(pfi, Config.YAML);
    }
    public static void saveConfig(Player p, Config c) {
        if (p == null || c == null) {
            return;
        }
        File pfi = new File(Main.getPlugin().getDataFolder() + File.separator + "players", p.getName() + ".yml");
        c.save(pfi);
    }
    public static boolean isUsableTitle(Player p, String title) {
        Config data = getConfig(p);
        if (data == null) return false;
        ConfigSection titleList = data.getSection("list");
        //if (!titleList.exists(title)) return false;
        if (titleList.getLong(title) < 0) return true;// 永久称号返回true
        if (LocalDateTime.now().getSecond() > titleList.getLong(title)) {
            p.sendMessage("[NTitle] 您的 "+title+" 称号已过期");
            titleList.remove(title);
            if(Objects.equals(data.getString("using"), title)) {
                p.sendMessage("[NTitle] 您的 "+title+" 已取消佩戴");
                data.set("using", null);
            }
            saveConfig(p, data);
            return false;
        }
        return true;
    }

    /**
     * 获取正在使用的称号
     * @param p
     * @return
     */
    public static String getUsing(Player p) {
        Config data = getConfig(p);
        String title = null;
        if (data != null) {
            title = data.getString("using", null);
        }
        if (title == null || title == "null") {
            title = LoadCfg.defaultTitle;
        }
        return title;
    }

}
