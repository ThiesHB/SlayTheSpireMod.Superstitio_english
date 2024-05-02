package SuperstitioMod.cards.Lupa.SkillCard;

import SuperstitioMod.DataManager;
import SuperstitioMod.actions.AutoDoneAction;
import SuperstitioMod.cards.Lupa.AbstractLupaCard;
import SuperstitioMod.cards.Lupa.TempCard.ReBirth;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;

import java.util.ArrayList;

public class UnBirth extends AbstractLupaCard {
    public static final String ID = DataManager.MakeTextID(UnBirth.class.getSimpleName());

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.UNCOMMON;

    public static final CardTarget CARD_TARGET = CardTarget.ENEMY;

    private static final int COST = 1;
    private static final int BLOCK = 8;
    private static final int UPGRADE_BLOCK = 3;

    public UnBirth() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupBlock(BLOCK);
        this.cardsToPreview = new ReBirth();
        //this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        this.addToBot_gainBlock();
        ArrayList<AbstractPower> sealPower = new ArrayList<>();
        monster.powers.forEach(power -> {
            if (power.type != AbstractPower.PowerType.DEBUFF && !(power instanceof ArtifactPower)) return;
            power.owner = player;
            sealPower.add(power);
            AutoDoneAction.addToBotAbstract(() ->
                    monster.powers.remove(power));
        });
        AbstractCard card = new ReBirth(sealPower, monster);
        if (this.upgraded)
            card.upgrade();
        addToBot_makeTempCardInBattle(card, BattleCardPlace.Hand);
    }

    @Override
    public void upgradeAuto() {
        upgradeBlock(UPGRADE_BLOCK);
    }
}
