package ayeartipsapi.command;

import ayeartipsapi.configLoad.PlayerConfig;
import ayeartipsapi.window.TitleWindow;
import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.command.defaults.VanillaCommand;
import cn.nukkit.command.tree.ParamList;
import cn.nukkit.command.tree.node.PlayersNode;
import cn.nukkit.command.utils.CommandLogger;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.ConfigSection;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NTitleCommand extends VanillaCommand {
    public NTitleCommand(String name) {
        super(name, "年系列称号插件");
        this.setPermission("plugin.mailsystem");
        this.getCommandParameters().clear();
        this.addCommandParameters("default", new CommandParameter[]{});
        this.addCommandParameters("shopPage", new CommandParameter[]{
                CommandParameter.newEnum("shopAction", false, new String[]{"shop"})
        });
        this.addCommandParameters("changeAction->add", new CommandParameter[]{
                CommandParameter.newEnum("changeAction", false, new String[]{"add"}),
                CommandParameter.newType("player", false, CommandParamType.TARGET, new PlayersNode()),
                CommandParameter.newType("titleName", false, CommandParamType.STRING),
                CommandParameter.newType("day", false, CommandParamType.INT)
        });
        this.addCommandParameters("changeAction->del", new CommandParameter[]{
                CommandParameter.newEnum("changeAction", false, new String[]{"del"}),
                CommandParameter.newType("player", false, CommandParamType.TARGET, new PlayersNode()),
                CommandParameter.newType("titleName", false, CommandParamType.STRING),
                CommandParameter.newType("info", false, CommandParamType.STRING)
        });
        this.enableParamTree();
    }

    @Override
    public int execute(CommandSender sender, String commandLabel, Map.Entry<String, ParamList> result, CommandLogger log) {
        var list = result.getValue();
        switch (result.getKey()) {
            case "default" -> {
                if (!sender.hasPermission("plugin.ntitle")) {
                    log.addError("nukkit.command.generic.permission").output();
                    return 0;
                }
                ((Player) sender).showFormWindow(TitleWindow.sendNTitleMyProfileWin((Player) sender));
                return 1;
            }
            case "shopPage" -> {
                if (!list.hasResult(0)) {
                    return 0;
                }
                if (!sender.hasPermission("plugin.ntitle")) {
                    log.addError("nukkit.command.generic.permission").output();
                    return 0;
                }
                ((Player) sender).showFormWindow(TitleWindow.sendNTitleShopWin((Player) sender));
                return 1;
            }
            case "changeAction->add" -> {
                if (!list.hasResult(0)) {
                    return 0;
                }
                if (!sender.hasPermission("plugin.ntitle.admin")) {
                    log.addError("nukkit.command.generic.permission").output();
                    return 0;
                }

                List<Player> players = list.getResult(1);
                Config data = PlayerConfig.getConfig(players.get(0));
                if (data == null) {
                    log.addError("给予失败，玩家数据不存在").output();
                    return 0;
                }
                String key = list.getResult(2);
                int day = list.getResult(3);

                ConfigSection titleList = data.getSection("list");
                long hasTime;
                if (titleList.exists(key)) {
                    hasTime = titleList.getLong(key);
                } else {
                    hasTime = 0;
                }
                long currentTime = Instant.now().getEpochSecond();
                if (day == -1) {
                    data.set(key, -1);
                } else if (hasTime > currentTime) {
                    data.set(key, hasTime + (day * 86400L));
                } else {
                    data.set(key, currentTime + (day * 86400L));
                }
                PlayerConfig.saveConfig(players.get(0), data);
                return 1;
            }
            case "changeAction->del" -> {
                if (!list.hasResult(0)) {
                    return 0;
                }
                if (!sender.hasPermission("plugin.ntitle.admin")) {
                    log.addError("nukkit.command.generic.permission").output();
                    return 0;
                }

                List<Player> players = list.getResult(1);
                Config data = PlayerConfig.getConfig(players.get(0));
                if (data == null) {
                    log.addError("移除失败，玩家数据不存在").output();
                    return 0;
                }
                String title = list.getResult(2);
                if (data.getString("using") == title) {
                    data.set("using", null);
                }
                data.set("using", title);

                ConfigSection titleList = data.getSection("list");

                if (titleList.exists(title)) {
                    titleList.remove(title);
                }
                PlayerConfig.saveConfig(players.get(0), data);
                return 1;
            }
            default -> {
                return 0;
            }
        }
    }
}
