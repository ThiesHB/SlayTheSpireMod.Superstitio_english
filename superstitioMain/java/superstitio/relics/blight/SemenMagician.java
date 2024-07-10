package superstitio.relics.blight;

import basemod.AutoAdd;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import superstitio.DataManager;
import superstitio.cards.SuperstitioCard;
import superstitio.cards.general.FuckJob_Card;
import superstitio.cards.lupa.OnAddSemenPower;
import superstitio.powers.lupaOnly.FloorSemen;
import superstitio.powers.lupaOnly.InsideSemen;
import superstitio.powers.lupaOnly.OutsideSemen;
import superstitio.relics.SuperstitioRelic;
import superstitioapi.relicToBlight.InfoBlight;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;
import static superstitioapi.utils.ActionUtility.addToTop_applyPower;

@AutoAdd.Seen
public class SemenMagician extends SuperstitioRelic implements InfoBlight.BecomeInfoBlight {
    public static final String ID = DataManager.MakeTextID(SemenMagician.class);
    // 遗物类型
    private static final RelicTier RELIC_TIER = RelicTier.SPECIAL;
    // 点击音效
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT;

    public SemenMagician() {
        super(ID, RELIC_TIER, LANDING_SOUND);
    }

    @Override
    public void updateDescriptionArgs() {
    }

    private static void addToBot_AddSemen(AbstractCard card) {
        boolean shouldApply = true;
        for (AbstractPower power : player.powers) {
            if (power instanceof OnAddSemenPower && !((OnAddSemenPower) power).onAddSemen_shouldApply(getSemenType(card)))
                shouldApply = false;
        }
        if (shouldApply) {
            addToTop_applyPower(getSemenType(card));
        }
    }

    private static AbstractPower getSemenType(AbstractCard card) {
        if (card.type != AbstractCard.CardType.ATTACK) return null;
        if (!(card instanceof FuckJob_Card && card instanceof SuperstitioCard))
            return new FloorSemen(player, 1);
        else if (card.hasTag(DataManager.CardTagsType.InsideEjaculation))
            return new InsideSemen(player, 1);
        else if (card.hasTag(DataManager.CardTagsType.OutsideEjaculation))
            return new OutsideSemen(player, 1);
        return new FloorSemen(player, 1);
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        if (card.type == AbstractCard.CardType.ATTACK)
            addToBot_AddSemen(card);
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