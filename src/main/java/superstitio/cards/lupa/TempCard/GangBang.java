package superstitio.cards.lupa.TempCard;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cards.lupa.AbstractLupaCard_TempCard;
import superstitio.cards.modifiers.block.DrySemenBlock;
import superstitio.cards.modifiers.damage.SexDamage;

public class GangBang extends AbstractLupaCard_TempCard {
    public static final String ID = DataManager.MakeTextID(GangBang.class.getSimpleName());

    public static final CardType CARD_TYPE = CardType.STATUS;

    public static final CardRarity CARD_RARITY = CardRarity.RARE;

    public static final CardTarget CARD_TARGET = CardTarget.ALL_ENEMY;

    private static final int COST = -1;
    private static final int ATTACK_DMG = 3;
    private static final int BLOCK = 4;

    public GangBang() {
        this(ATTACK_DMG, BLOCK);
    }

    public GangBang(int attackAmount, int blockAmount) {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupDamage(attackAmount, new SexDamage());
        this.setupBlock(blockAmount, new DrySemenBlock());
        this.glowColor = Color.WHITE.cpy();
        this.beginGlowing();
        this.exhaust = true;
//        this.purgeOnUse = true;
        this.dontTriggerOnUseCard = true;

        this.setBackgroundTexture(
                DataManager.makeImgFilesPath_UI("bg_attack_512_semen"),
                DataManager.makeImgFilesPath_UI("bg_attack_semen"));
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
