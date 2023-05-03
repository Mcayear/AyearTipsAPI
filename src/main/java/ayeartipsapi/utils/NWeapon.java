package ayeartipsapi.utils;

import ayeartipsapi.Main;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class NWeapon {
    public static List<String> cfgData;
    public static boolean isEnable = false;
    public static boolean init(){
        if(new File("./plugins/NWeapon", "Config.yml").exists()) {
            Config cfg = new Config(new File("./plugins/NWeapon", "Config.yml"), Config.YAML);
            cfgData = cfg.getStringList("AttrDisplayPercent");
            Main.getPlugin().getLogger().info(TextFormat.YELLOW+"NWeapon 配置文件已找到!");
        }
        return true;
    }
    /**
     * 将数据可视化，输入data,属性输出min-max或x%
     *
     * @param data 类似 [1,2]
     * @param i    类似 攻击力
     * @return min-max 或 x%
     */
    public static String valueToString(Object data, String key) {
        String back = "";
        if (data instanceof Object[]) {
            Object[] dataArray = (Object[]) data;
            if (dataArray.length == 2 && dataArray[0].equals(dataArray[1])) {
                data = Double.parseDouble(String.format("%.2f", dataArray[0]));
            } else if (dataArray.length == 1) {
                data = dataArray[0];
            } else {
                return dataArray[0] + " - " + dataArray[1];
            }
        }
        if (cfgData.contains(key)) {
            double dataValue = (double) data;
            String dataString = String.format("%.2f", dataValue * 100) + "%%";
            if (dataString.endsWith(".00")) {
                dataString = String.valueOf((int) dataValue);
            }
            back = dataString;
        } else {
            back = String.valueOf(data);
        }
        if (data.equals(0)) {
            back = "0";
        }
        return back;
    }

    // 以下是用于模拟 JS 中的 contain 函数
    public static boolean contain(String str) {
        // 实现 contain 函数的逻辑
        return false;
    }

    // 以下是用于模拟 JS 中的 MainConfig 对象
    public static class MainConfig {
        public static String[] AttrDisplayPercent;
    }
}

