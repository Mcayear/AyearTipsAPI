package ayeartipsapi;

import ayeartipsapi.configLoad.PlayerConfig;
import cn.nukkit.Player;
import tip.utils.variables.BaseVariable;


/**
 * 默认变量
 * @author SmallasWater
 */
public class AyearVariables extends BaseVariable {

    public AyearVariables(Player player) {
        super(player);
    }

    @Override
    public void strReplace() {
        if(player != null) {
            ayearString();
        }
    }
    private void ayearString(){
        addStrReplaceString("{player_exp_level}", String.valueOf(player.getExperienceLevel()));
        int lv = (player.getExperienceLevel() + 1);
        addStrReplaceString("{player_exp_max}", String.valueOf(lv > 16 ? lv > 31 ? 9*lv-158 : 5*lv-38 : 2*lv+7));
        addStrReplaceString("{player_exp}", String.valueOf(player.getExperience()));
        String usingTitle = PlayerConfig.getUsing(player);
        if (usingTitle == null) {
            addStrReplaceString("{NTitle}", "");
        } else {
            addStrReplaceString("{NTitle}", usingTitle);
        }
    }

}