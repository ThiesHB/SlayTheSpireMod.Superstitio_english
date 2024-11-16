package superstitio.cards.general.TempCard;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.AutoplayField;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cardModifier.modifiers.block.DrySemenBlock;
import superstitio.cardModifier.modifiers.damage.SexDamage;
import superstitio.cards.general.AbstractTempCard;
import superstitioapi.SuperstitioApiSetup;
import superstitioapi.cards.patch.GoSomewhereElseAfterUse;
import superstitioapi.hangUpCard.CardOrb_AtEndOfTurnEachTime;
import superstitioapi.utils.ActionUtility;
import superstitioapi.utils.CreatureUtility;

public class GangBang extends AbstractTempCard implements GoSomewhereElseAfterUse {
    public static final String ID = DataManager.MakeTextID(GangBang.class);

    public static final CardType CARD_TYPE = CardType.STATUS;

    public static final CardRarity CARD_RARITY = CardRarity.RARE;

    public static final CardTarget CARD_TARGET = CardTarget.ALL_ENEMY;

    private static final int COST = -2;
    private static final int DAMAGE = 3;
    private static final int BLOCK = 4;
    private static final int TURN_TAKE =1;

    public GangBang() {
        this(DAMAGE, BLOCK, 1, 0.15);
    }

    /**
     * @param score 1-5
     */
    public GangBang(int attackAmount, int blockAmount, int score, double scoreRate) {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.originalName = cardStrings.getEXTENDED_DESCRIPTION(score) + cardStrings.getNAME();
        this.name = this.originalName;
        this.setupDamage((int) (attackAmount * (1 + (score - 1) * scoreRate)), new SexDamage());
        this.setupBlock((int) (blockAmount * (1 + (score - 1) * scoreRate)), new DrySemenBlock());
        this.glowColor = Color.WHITE.cpy();
        if (!ActionUtility.isNotInBattle())
            this.beginGlowing();
        this.exhaust = true;
        this.isMultiDamage = true;
//        this.purgeOnUse = true;
        AutoplayField.autoplay.set(this, true);
        this.dontTriggerOnUseCard = true;
        this.setBackgroundTexture(
                DataManager.SPTT_DATA.BG_ATTACK_512_SEMEN,
                DataManager.SPTT_DATA.BG_ATTACK_SEMEN);
        this.initializeDescription();
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        this.dontTriggerOnUseCard = true;
    }

    @Override
    public boolean canUpgrade() {
        return false;
    }

    @Override
    public void upgradeAuto() {
    }

    @Override
    public void updateDescriptionArgs() {
        setDescriptionArgs(TURN_TAKE);
    }

    @Override
    public void afterInterruptMoveToCardGroup(CardGroup cardGroup) {
        GangBang self = this;
        new CardOrb_AtEndOfTurnEachTime(this, cardGroup, TURN_TAKE, cardOrbAtEndOfTurn -> {
            cardOrbAtEndOfTurn.StartHitCreature(CreatureUtility.getRandomMonsterWithoutRngSafe());
            self.addToBot_gainCustomBlock(self.block, new DrySemenBlock());
            self.addToBot_dealDamageToAllEnemies(SuperstitioApiSetup.DamageEffect.HeartMultiInOne);
            addToBot(new WaitAction(1.0f));
        })
                .setCardRawDescriptionWillShow(cardStrings.getEXTENDED_DESCRIPTION(0))
                .addToBot_HangCard();
    }
}
