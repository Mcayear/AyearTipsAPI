package ayeartipsapi;

import ayeartipsapi.command.NTitleCommand;
import ayeartipsapi.configLoad.LoadLanguage;
import ayeartipsapi.configLoad.LoadShop;
import ayeartipsapi.listen.PlayerListener;
import cn.nukkit.Server;
import cn.nukkit.event.Listener;
import cn.nukkit.permission.Permission;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.plugin.PluginLogger;
import tip.utils.Api;
import ayeartipsapi.configLoad.LoadCfg;

public class Main extends PluginBase implements Listener {
    PluginLogger log;

    private static Main plugin;
    public static Main getPlugin(){return plugin;}
    @Override
    public void onLoad() {
        plugin = this;
        log = new PluginLogger(this);
    }

    @Override
    public void onEnable() {
        LoadCfg.loadCfgData();
        LoadShop.loadCfgData();
        LoadLanguage.loadCfgData();
        Api.registerVariables("AyearTipsApi", AyearVariables.class);
        log.info("AyearTips 变量启动成功!");

        // 加载监听器
        this.getServer().getPluginManager().registerEvents(new PlayerListener(),this);
        // 注册命令
        Server.getInstance().getPluginManager().addPermission(new Permission("plugin.ntitle", "mailsystem 命令权限", "true"));
        Server.getInstance().getPluginManager().addPermission(new Permission("plugin.ntitle.admin", "mailsystem 管理员命令权限", "op"));
        Server.getInstance().getCommandMap().register("NTitleSystem", new NTitleCommand("ntitle"));
    }

    @Override
    public void onDisable() {

    }
}
