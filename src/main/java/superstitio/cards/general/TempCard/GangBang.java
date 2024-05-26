package superstitio.cards.general.TempCard;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.AutoplayField;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cardModifier.modifiers.block.DrySemenBlock;
import superstitio.cardModifier.modifiers.damage.SexDamage;
import superstitio.cards.general.AbstractTempCard;
import superstitio.utils.CardUtility;

public class GangBang extends AbstractTempCard {
    public static final String ID = DataManager.MakeTextID(GangBang.class);

    public static final CardType CARD_TYPE = CardType.STATUS;

    public static final CardRarity CARD_RARITY = CardRarity.RARE;

    public static final CardTarget CARD_TARGET = CardTarget.ALL_ENEMY;

    private static final int COST = -2;
    private static final int DAMAGE = 3;
    private static final int BLOCK = 4;

    public GangBang() {
        this(DAMAGE, BLOCK, 1);
    }

    /**
     * @param score 1-5
     */
    public GangBang(int attackAmount, int blockAmount, int score) {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.originalName = cardStrings.getEXTENDED_DESCRIPTION()[score - 1] + cardStrings.getNAME();
        this.name = this.originalName;
        this.setupDamage((int) (attackAmount * (1 + (score - 1) * 0.15)), new SexDamage());
        this.setupBlock((int) (blockAmount * (1 + (score - 1) * 0.15)), new DrySemenBlock());
        this.glowColor = Color.WHITE.cpy();
        if (!CardUtility.isNotInBattle())
            this.beginGlowing();
        this.exhaust = true;
        this.isMultiDamage = true;
//        this.purgeOnUse = true;
        this.dontTriggerOnUseCard = true;
        AutoplayField.autoplay.set(this, true);
        this.setBackgroundTexture(
                DataManager.SPTT_DATA.BG_ATTACK_512_SEMEN,
                DataManager.SPTT_DATA.BG_ATTACK_SEMEN);
        this.initializeDescription();
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot_gainBlock();
        addToBot_dealDamageToAllEnemies(AbstractGameAction.AttackEffect.POISON);
    }

    @Override
    public boolean canUpgrade() {
        return false;
    }

    @Override
    public void upgradeAuto() {
    }

}
