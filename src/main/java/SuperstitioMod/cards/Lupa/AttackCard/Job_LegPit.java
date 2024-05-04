package SuperstitioMod.cards.Lupa.AttackCard;

import SuperstitioMod.DataManager;
import SuperstitioMod.InBattleDataManager;
import SuperstitioMod.actions.AutoDoneAction;
import SuperstitioMod.cards.Lupa.AbstractLupaCard_FuckJob;
import SuperstitioMod.powers.SexualHeat;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Job_LegPit extends AbstractLupaCard_FuckJob {
    public static final String ID = DataManager.MakeTextID(Job_LegPit.class.getSimpleName());

    public static final CardType CARD_TYPE = CardType.ATTACK;

    public static final CardRarity CARD_RARITY = CardRarity.UNCOMMON;

    public static final CardTarget CARD_TARGET = CardTarget.ENEMY;

    private static final int COST = 1;
    private static final int ATTACK_DMG = 4;
    private static final int UPGRADE_PLUS_DMG = 2;

    private static final int Attack_Num = 2;

    public Job_LegPit() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupDamage(ATTACK_DMG);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        for (int i = 0; i < Attack_Num; i++) {
            addToBot_dealDamage(monster, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
            AutoDoneAction.addToBotAbstract(() -> {
                if (monster.lastDamageTaken > 0 && SexualHeat.isInOrgasm(AbstractDungeon.player))
                    addToBot_drawCards();
            });
        }
        AbstractLupaCard_FuckJob.addToTop_gainSexMark_Outside(this.getEXTENDED_DESCRIPTION()[0]);
    }

    @Override
    public void triggerOnGlowCheck() {
        this.glowColor = BLUE_BORDER_GLOW_COLOR.cpy();
        if (InBattleDataManager.InOrgasm) {
            this.glowColor = Color.PINK.cpy();
        }
    }


    @Override
    public void upgradeAuto() {
        upgradeDamage(UPGRADE_PLUS_DMG);
    }

}
