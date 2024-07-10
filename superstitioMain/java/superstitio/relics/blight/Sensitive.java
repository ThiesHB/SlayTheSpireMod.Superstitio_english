package superstitio.relics.blight;

import basemod.AutoAdd;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.Logger;
import superstitio.powers.SexualHeat;
import superstitio.powers.patchAndInterface.interfaces.orgasm.OnOrgasm_onOrgasmFirst;
import superstitio.relics.SuperstitioRelic;
import superstitioapi.actions.AutoDoneInstantAction;
import superstitioapi.powers.AllCardCostModifier_PerEnergy;
import superstitioapi.powers.interfaces.HasAllCardCostModifyEffect;
import superstitioapi.relicToBlight.InfoBlight;

import java.lang.reflect.InvocationTargetException;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;
import static superstitio.powers.SexualHeat.getOrgasmTimesInTurn;
import static superstitioapi.actions.AutoDoneInstantAction.addToBotAbstract;
import static superstitioapi.powers.AllCardCostModifier.addTo_Bot_EditAmount_Top_FirstByHolder;

@AutoAdd.Seen
public class Sensitive extends SuperstitioRelic implements InfoBlight.BecomeInfoBlight, OnOrgasm_onOrgasmFirst {
    public static final String ID = DataManager.MakeTextID(Sensitive.class);
    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.SPECIAL;
    // 点击音效
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;
    private static final int SexualHeatRate = 2;
    private final int hpLose = 0;

    public Sensitive() {
        super(ID, RELIC_TIER, LANDING_SOUND);
    }

    @Override
    public void onOrgasmFirst(SexualHeat SexualHeatPower) {
        final int decreaseCost = Math.min(getOrgasmTimesInTurn(), AbstractDungeon.player.energy.energyMaster);
        addToBotAbstract(() -> {
            try {
                addTo_Bot_EditAmount_Top_FirstByHolder(SexualHeatPower, decreaseCost, power -> {
                    if (power.isPresent())
                        return 1;
                    else
                        return 1;
                }, AllCardCostModifier_PerEnergy.class.getConstructor(AbstractCreature.class, int.class, int.class,
                        HasAllCardCostModifyEffect.class));
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                     IllegalAccessException e) {
                Logger.error(e);
            }
        });
    }


    @Override
    public void updateDescriptionArgs() {
        setDescriptionArgs(SexualHeatRate);
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster monster) {
        if (card.isInAutoplay) return;
        int amount = 0;
        if (card.costForTurn >= 1)
            amount += card.costForTurn;
        if (card.costForTurn == -1)
            amount += card.energyOnUse;
        if (amount == 0) return;
        SexualHeat.useConsumer_addSexualHeat(player, amount * SexualHeatRate, AutoDoneInstantAction::addToTopAbstract);
    }

    @Override
    public void obtain() {
        AInfoBlightOnOrgasm.obtain(this);
    }

    @Override
    public void instantObtain(AbstractPlayer p, int slot, boolean callOnEquip) {
        AInfoBlightOnOrgasm.instanceObtain(this, callOnEquip);
    }

    @Override
    public void instantObtain() {
        AInfoBlightOnOrgasm.instanceObtain(this, true);
    }
}