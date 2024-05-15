package superstitio.relics.a_starter;

import basemod.AutoAdd;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import superstitio.DataManager;
import superstitio.cards.general.TempCard.SexToy;
import superstitio.relics.AbstractLupaRelic;
import superstitio.utils.ActionUtility;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;

@AutoAdd.Seen
public class StartWithSexToy extends AbstractLupaRelic {
    public static final String ID = DataManager.MakeTextID(StartWithSexToy.class);
    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.STARTER;
    // 点击音效
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;
    private static final int SexToyNum = 2;

    public StartWithSexToy() {
        super(ID, RELIC_TIER, LANDING_SOUND);
    }

    @Override
    public void atBattleStart() {
        this.flash();
        this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        for (int i = 0; i < SexToyNum; i++) {
            ActionUtility.addToBot_applyPower(new superstitio.powers.SexToy(player, 1, SexToy.getRandomSexToyNameWithoutRng()));
        }
    }

    @Override
    public void updateDescriptionArgs() {
        setDescriptionArgs(SexToyNum);
    }
}