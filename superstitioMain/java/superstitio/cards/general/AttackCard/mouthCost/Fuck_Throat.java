package superstitio.cards.general.AttackCard.mouthCost;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cards.general.FuckJob_Card;
import superstitio.cards.general.GeneralCard;
import superstitioapi.SuperstitioApiSetup;
import superstitioapi.hangUpCard.HangUpCardGroup;

public class Fuck_Throat extends GeneralCard implements FuckJob_Card {
    public static final String ID = DataManager.MakeTextID(Fuck_Throat.class);

    public static final CardType CARD_TYPE = CardType.ATTACK;

    public static final CardRarity CARD_RARITY = CardRarity.COMMON;

    public static final CardTarget CARD_TARGET = CardTarget.ENEMY;

    private static final int COST = 2;
    private static final int DAMAGE = 10;
    private static final int UPGRADE_DAMAGE = 3;
    private static final int MagicNum = 2;

    public Fuck_Throat() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        FuckJob_Card.initFuckJobCard(this);
        this.setupDamage(DAMAGE, UPGRADE_DAMAGE);
        this.setupMagicNumber(MagicNum);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot_dealDamage(monster, SuperstitioApiSetup.DamageEffect.HeartMultiInOne);
        HangUpCardGroup.forHangUpCardGroup(group -> {
            if (group.hasOrb())
                addToBot(new GainEnergyAction(this.magicNumber));
        }).addToBotAsAbstractAction();

    }

    @Override
    public void triggerOnGlowCheck() {
        this.glowColor = BLUE_BORDER_GLOW_COLOR.cpy();
        HangUpCardGroup.forHangUpCardGroup(group -> {
            if (group.hasOrb())
                this.glowColor = Color.PINK.cpy();
        }).get();
    }

    @Override
    public void upgradeAuto() {
    }
}
