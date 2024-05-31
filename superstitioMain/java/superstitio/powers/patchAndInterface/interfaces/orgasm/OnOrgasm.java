package superstitio.powers.patchAndInterface.interfaces.orgasm;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import superstitio.powers.SexualHeat;
import superstitioapi.utils.CardUtility;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface OnOrgasm {

    static Stream<OnOrgasm> AllOnOrgasm(AbstractCreature owner) {
        ArrayList<OnOrgasm> onOrgasms =
                owner.powers.stream().filter(OnOrgasm.class::isInstance).map(power -> (OnOrgasm) power).collect(Collectors.toCollection(ArrayList::new));
        if (owner.isPlayer) {
            onOrgasms.addAll(CardUtility.AllCardInBattle().stream().filter(OnOrgasm.class::isInstance).map(card -> (OnOrgasm) card).collect(Collectors.toList()));
            onOrgasms.addAll(AbstractDungeon.player.relics.stream().filter(OnOrgasm.class::isInstance).map(relic -> (OnOrgasm) relic).collect(Collectors.toList()));
        }
        return onOrgasms.stream();
    }

    /**
     * 检测高潮时的钩子
     */
    default void onCheckOrgasm(SexualHeat SexualHeatPower) {
    }

    /**
     * 高潮时的处理
     */
    default void onOrgasm(SexualHeat SexualHeatPower) {
    }

    /**
     * 连续高潮时的额外处理
     */
    default void onContinuallyOrgasm(SexualHeat SexualHeatPower) {
    }

    /**
     * 高潮结束后的处理
     */
    default void onEndOrgasm(SexualHeat SexualHeatPower) {
    }


    /**
     * 调用时已经判断高潮成立，如果返回true则禁止本次高潮
     */
    default boolean preventOrgasm(SexualHeat SexualHeatPower) {
        return false;
    }

    /**
     * 潮吹之前
     */
    default void onSquirt(SexualHeat SexualHeatPower, AbstractCard card) {
    }

    default void onSuccessfullyPreventOrgasm(SexualHeat SexualHeatPower) {
    }
}
