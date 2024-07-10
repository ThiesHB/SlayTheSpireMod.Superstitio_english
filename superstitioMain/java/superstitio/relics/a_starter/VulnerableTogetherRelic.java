package superstitio.relics.a_starter;

import basemod.AutoAdd;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import superstitio.DataManager;
import superstitio.cards.general.TempCard.VulnerableTogether;
import superstitio.relics.SuperstitioRelic;
import superstitioapi.utils.ActionUtility;

import static superstitioapi.utils.ActionUtility.addToBot_makeTempCardInBattle;

@AutoAdd.Seen
public class VulnerableTogetherRelic extends SuperstitioRelic {
    public static final String ID = DataManager.MakeTextID(VulnerableTogetherRelic.class);
    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.STARTER;
    // 点击音效
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;

    public VulnerableTogetherRelic() {
        super(ID, RELIC_TIER, LANDING_SOUND);
    }

    @Override
    public void atBattleStart() {
        this.flash();
        ActionUtility.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        addToBot_makeTempCardInBattle(new VulnerableTogether(), ActionUtility.BattleCardPlace.Hand);
    }

    @Override
    public void updateDescriptionArgs() {
    }
}