package superstitio.cards.general.SkillCard.energyAndDraw;

import basemod.cardmods.ExhaustMod;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import superstitio.DataManager;
import superstitio.cards.general.GeneralCard;
import superstitio.powers.SexualHeat;

public class EscapeConjuring extends GeneralCard {
    public static final String ID = DataManager.MakeTextID(EscapeConjuring.class);

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.UNCOMMON;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 0;
    private static final int MAGIC = 3;
    private static final int UPGRADE_MAGIC = 1;


    public EscapeConjuring() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        CardModifierManager.addModifier(this, new ExhaustMod());
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        int discardCard = (int) AbstractDungeon.player.hand.group.stream()
                .filter(card -> card.costForTurn != 0 && !card.freeToPlay()).count();
        int statusCardNum = (int) AbstractDungeon.player.hand.group.stream()
                .filter(card -> card.type == CardType.STATUS || card.type == CardType.CURSE).count();
        int deBuffNum = (int) AbstractDungeon.player.powers.stream()
                .filter(power -> power.type == AbstractPower.PowerType.DEBUFF).count();

        SexualHeat.addToBot_addSexualHeat(AbstractDungeon.player, deBuffNum * this.magicNumber);
        addToBot(new GainEnergyAction(statusCardNum));

        AbstractDungeon.player.hand.group.stream().filter(card -> card.costForTurn != 0 && !card.freeToPlay())
                .map(DiscardSpecificCardAction::new).forEach(this::addToBot);
        addToBot_drawCards(statusCardNum);
    }

    @Override
    public void upgradeAuto() {
        CardModifierManager.removeModifiersById(this, ExhaustMod.ID, false);
    }
}
