package ayeartipsapi.window;

import ayeartipsapi.Main;
import ayeartipsapi.configLoad.LoadLanguage;
import ayeartipsapi.configLoad.LoadShop;
import ayeartipsapi.configLoad.PlayerConfig;
import ayeartipsapi.utils.NWeapon;
import cn.nukkit.IPlayer;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.form.element.*;
import cn.nukkit.form.handler.FormResponseHandler;
import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.form.window.FormWindowCustom;
import cn.nukkit.form.window.FormWindowSimple;
import cn.nukkit.item.Item;
import cn.nukkit.utils.Config;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ayeartipsapi.configLoad.LoadCfg;
import cn.nukkit.utils.ConfigSection;

import me.onebone.economyapi.EconomyAPI;

import static ayeartipsapi.configLoad.PlayerConfig.isUsableTitle;
import static ayeartipsapi.utils.Util.Date2YMD;

public class TitleWindow {

    public static FormWindowSimple sendNTitleShopWin(Player p) {
        return sendNTitleShopWin(p, false);
    }
    /**
     * 发送称号商城的主菜单
     * @param p 发送者
     */
    public static FormWindowSimple sendNTitleShopWin(Player p, boolean needBack){
        FormWindowSimple wd = new FormWindowSimple(LoadLanguage.titleShopMenu, LoadLanguage.textShopMenu);

        Config shopData = LoadShop.ShopData;
        List<String> keys = shopData.getKeys(false).stream().toList();
        for (String key : keys) {
            ConfigSection shop = shopData.getSection(key);
            int sell = shop.getInt("sell", -1);
            String info = shop.getString("info", "");

            String buttonDescription = key + " " + (sell < 0 ? "非卖品" : "售价: " + sell) + "§r\n" + info;
            if (shop.exists("imgType")) {
                wd.addButton(new ElementButton(buttonDescription, new ElementButtonImageData(shop.getString("imgType"), shop.getString("imgPath"))));
            } else {
                wd.addButton(new ElementButton(buttonDescription));
            }
        }

        wd.addHandler(FormResponseHandler.withoutPlayer(ignored -> {
            if (wd.wasClosed()) {
                if (needBack) {
                    p.showFormWindow(sendNTitleMyProfileWin(p));
                }
                return;
            }

            p.showFormWindow(sendNTitleBuyWin(p, shopData.getSection(keys.get(wd.getResponse().getClickedButtonId())), keys.get(wd.getResponse().getClickedButtonId())));
        }));
        return wd;
    }
    /**
     * 称号购买页面
     * @param player 发送者
     * @param titleConfig 称号配置
     * @return 界面
     */
    public static FormWindowCustom sendNTitleBuyWin(Player player, ConfigSection titleConfig, String title) {
        FormWindowCustom win = new FormWindowCustom(titleConfig.getString("title") + "§r - "+LoadLanguage.titleBuyPage);

        Map<String, Object> attr = titleConfig.getSection("attr").getAll();
        StringBuilder str = new StringBuilder();
        for (Map.Entry<String, Object> entry : attr.entrySet()) {
            if (NWeapon.isEnable) {
                str.append("\n  ").append(entry.getKey()).append(": ").append(NWeapon.valueToString(entry.getValue(), entry.getKey()));
            } else {
                str.append("\n  ").append(entry.getKey()).append(": ").append(entry.getValue().toString());
            }
        }

        win.addElement(new ElementLabel(
                "称号: " + title +
                        "\n§r售价: " + (titleConfig.getDouble("sell") < 0 ? "§7"+LoadLanguage.noSale : titleConfig.getDouble("sell") + " /" + titleConfig.getInt("day") + "天") +
                        "\n§r属性: " + str
        ));

        int maxday = titleConfig.getInt("maxDay", 30);
        List<String> reason = new ArrayList<>();
        boolean isNotSell = false;
        boolean isOverlap = false;

        if (titleConfig.getBoolean("noOverlap") && isUsableTitle(player, title)) {
            isOverlap = true;
            reason.add(LoadLanguage.notStackable);
        }

        if (titleConfig.getDouble("sell") < 0) {
            isNotSell = true;
            reason.add(LoadLanguage.noSale);
        }

        if (isNotSell || isOverlap) {
            win.addElement(new ElementSlider("不可购买 (" + String.join("、", reason) + ")", 0, 0, 0, 0));
        } else if (titleConfig.getInt("day") == -1) {
            win.addElement(new ElementSlider("购买永久", 0, 1, 1, 0));
        } else {
            win.addElement(new ElementSlider("购买天数", 0, maxday, titleConfig.getInt("day"), 0));
        }

        win.addElement(new ElementToggle("立即佩戴", true));

        win.addHandler(FormResponseHandler.withoutPlayer(ignored -> {// 用户发送邮件
            FormResponseCustom resp = win.getResponse();
            if (win.wasClosed()) {
                return;
            }
            int day = (int) resp.getSliderResponse(1);
            double needMoney = day * titleConfig.getDouble("sell");

            double haveMoney = EconomyAPI.getInstance().myMoney(player);

            if(day == 0) {
                player.showFormWindow(sendNTitleShopWin(player));
                return;
            }
            if(haveMoney < needMoney) {
                player.sendMessage("[NTitle] 余额不足: §c"+haveMoney+"§r/"+needMoney);
                player.showFormWindow(sendNTitleShopWin(player));
                return;
            }
            //Start - 处理玩家数据
            EconomyAPI.getInstance().setMoney(player, haveMoney - needMoney);


            Config data = PlayerConfig.getConfig(player);
            ConfigSection playerTitleList = null;
            if (data != null) {
                playerTitleList = data.getSection("list");
            }
            if(titleConfig.getInt("day") == -1) {
                // 购买永久的处理
                playerTitleList.set(title, -1);
                player.sendMessage("[NTitle] 购买成功，称号 "+title+" 有效期至 永久");
            } else if(playerTitleList.exists(title) && isUsableTitle(player, title)) {
                playerTitleList.set(title, day * 86400L + playerTitleList.getLong(title));
                player.sendMessage("[NTitle] 购买成功，称号 "+title+" 已续费至 "+Date2YMD(playerTitleList.getLong(title)));
            } else {
                long second = Instant.now().getEpochSecond();
                playerTitleList.set(title, day * 86400L + second);
                player.sendMessage("[NTitle] 购买成功，称号 "+title+" 有效期至 "+Date2YMD(playerTitleList.getLong(title)));
            }
            if (resp.getToggleResponse(2)) {
                data.set("using", title);
            }
            data.save();
            player.showFormWindow(sendNTitleShopWin(player));

        }));
        return win;
    }

    /**
     * 发送我的称号 页面
     * @param p 发送者
     */
    public static FormWindowSimple sendNTitleMyProfileWin(Player p){
        FormWindowSimple wd = new FormWindowSimple(LoadLanguage.myProfileMenu, LoadLanguage.myProfileCurrent+PlayerConfig.getUsing(p));
        Config cfg = PlayerConfig.getConfig(p);
        ConfigSection playerConfig = cfg.getSection("list");

        List<String> keys = playerConfig.getKeys(false).stream().toList();
        for (String key : keys) {
            if (!PlayerConfig.isUsableTitle(p, key)) continue;
            long time = playerConfig.getLong(key);
            wd.addButton(new ElementButton(key+"\n§7有效期: "+(time > -1 ? Date2YMD(time) : "永久")));
        }
        wd.addButton(new ElementButton(LoadCfg.defaultTitle+"\n§7有效期: 永久(默认称号)"));
        wd.addButton(new ElementButton("§l获取更多称号.."));


        wd.addHandler(FormResponseHandler.withoutPlayer(ignored -> {
            if (wd.wasClosed()) {
                return;
            }

            int butId = wd.getResponse().getClickedButtonId();// 0-2, 3, 4    3 2 1, 0, -1
            int index = keys.size()- butId;
            if (index > 0) {
                if (!PlayerConfig.isUsableTitle(p, keys.get(butId))) {
                    p.sendMessage("[NTitle] 你的 "+keys.get(butId)+" 称号已过期");
                    return;
                }
                cfg.set("using", keys.get(butId));
                p.sendMessage("[NTitle] 成功佩戴称号 "+keys.get(butId));
                cfg.save();
            } else if (index < 0) {
                p.showFormWindow(sendNTitleShopWin(p, true));
            } else {
                cfg.set("using", null);
                p.sendMessage("[NTitle] 成功佩戴称号 "+LoadCfg.defaultTitle);
                cfg.save();
            }
        }));
        return wd;
    }
}
