package superstitio.cards.maso.SkillCard.cruelTorture;

import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.LoseDexterityPower;
import superstitio.DataManager;
import superstitio.cardModifier.modifiers.card.CruelTortureTag;
import superstitio.cards.maso.MasoCard;
import superstitioapi.cards.patch.GoSomewhereElseAfterUse;
import superstitioapi.hangUpCard.CardOrb_AtStartOfTurnEachTime;
import superstitioapi.utils.ActionUtility;

//炮烙
public class CruelTorture_HotPillar extends MasoCard implements GoSomewhereElseAfterUse {
    public static final String ID = DataManager.MakeTextID(CruelTorture_HotPillar.class);

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.UNCOMMON;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;
    public static final int BURN_CARD_NUM = 1;
    private static final int COST = 1;
    private static final int MAGIC = 3;
    private static final int UPGRADE_MAGIC = 1;

    public CruelTorture_HotPillar() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC);
        CardModifierManager.addModifier(this, new CruelTortureTag());
        this.cardsToPreview = new Burn();
    }

    private void addTempDexterity(CruelTorture_HotPillar self) {
        self.addToBot_applyPower(new DexterityPower(AbstractDungeon.player, this.magicNumber));
        self.addToBot_applyPower(new LoseDexterityPower(AbstractDungeon.player, this.magicNumber));
    }

    @Override
    public void updateDescriptionArgs() {
        setDescriptionArgs(BURN_CARD_NUM);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        ActionUtility.addToBot_makeTempCardInBattle(new Burn(), ActionUtility.BattleCardPlace.Discard, BURN_CARD_NUM);
        addTempDexterity(this);
    }

    @Override
    public void upgradeAuto() {
    }

    @Override
    public void afterInterruptMoveToCardGroup(CardGroup cardGroup) {
        CruelTorture_HotPillar self = this;
        new CardOrb_AtStartOfTurnEachTime(this, cardGroup, 99, cardOrbAtStartOfTurn -> {
            cardOrbAtStartOfTurn.StartHitCreature(AbstractDungeon.player);
            addTempDexterity(self);
        })
                .addToBot_HangCard();
    }
}
