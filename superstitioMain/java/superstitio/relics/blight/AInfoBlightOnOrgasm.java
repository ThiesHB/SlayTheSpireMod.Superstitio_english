package superstitio.relics.blight;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import superstitio.powers.SexualHeat;
import superstitio.powers.patchAndInterface.interfaces.orgasm.OnOrgasm;
import superstitioapi.relicToBlight.InfoBlight;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;

public class AInfoBlightOnOrgasm extends InfoBlight implements OnOrgasm {
    public AInfoBlightOnOrgasm(AbstractRelic relic) {
        super(relic);
    }

    /**
     * 需要在想要转化为荒疫的遗物中，修改如下的一个方法，使用这个函数，并且去掉super部分：
     * obtain
     */
    public static void obtain(AbstractRelic relic) {
//        new InfoBlight<>(relic).obtain();
        instanceObtain(relic, true);
        //obtain有一点bug，所以就先用着这个吧
    }

    /**
     * 需要在想要转化为荒疫的遗物中，修改如下的两个方法，使用这个函数，并且去掉super部分：
     * 无参数的instantObtain
     * 有参数的instantObtain
     */
    public static void instanceObtain(AbstractRelic relic, boolean callOnEquip) {
        new AInfoBlightOnOrgasm(relic).instantObtain(player, player.blights.size(), callOnEquip);
    }

    @Override
    public void onCheckOrgasm(SexualHeat SexualHeatPower) {
        if (this.relic instanceof OnOrgasm) {
            ((OnOrgasm) this.relic).onCheckOrgasm(SexualHeatPower);
        }
    }

    @Override
    public void onContinuallyOrgasm(SexualHeat SexualHeatPower) {
        if (this.relic instanceof OnOrgasm) {
            ((OnOrgasm) this.relic).onContinuallyOrgasm(SexualHeatPower);
        }
    }

    @Override
    public void onEndOrgasm(SexualHeat SexualHeatPower) {
        if (this.relic instanceof OnOrgasm) {
            ((OnOrgasm) this.relic).onEndOrgasm(SexualHeatPower);
        }
    }

    @Override
    public void onOrgasm(SexualHeat SexualHeatPower) {
        if (this.relic instanceof OnOrgasm) {
            ((OnOrgasm) this.relic).onOrgasm(SexualHeatPower);
        }
    }

    @Override
    public void onSquirt(SexualHeat SexualHeatPower, AbstractCard card) {
        if (this.relic instanceof OnOrgasm) {
            ((OnOrgasm) this.relic).onSquirt(SexualHeatPower, card);
        }
    }

    @Override
    public void onSuccessfullyPreventOrgasm(SexualHeat SexualHeatPower) {
        if (this.relic instanceof OnOrgasm) {
            ((OnOrgasm) this.relic).onSuccessfullyPreventOrgasm(SexualHeatPower);
        }
    }

    @Override
    public boolean preventOrgasm(SexualHeat SexualHeatPower) {
        if (this.relic instanceof OnOrgasm) {
            return ((OnOrgasm) this.relic).preventOrgasm(SexualHeatPower);
        }
        return OnOrgasm.super.preventOrgasm(SexualHeatPower);
    }

}