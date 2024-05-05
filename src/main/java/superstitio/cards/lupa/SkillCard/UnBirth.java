package superstitio.cards.lupa.SkillCard;

import superstitio.DataManager;
import superstitio.actions.AutoDoneAction;
import superstitio.cards.modifiers.block.PregnantBlock;
import superstitio.cards.modifiers.block.PregnantBlock_sealPower;
import superstitio.cards.lupa.AbstractLupaCard;
import superstitio.cards.lupa.TempCard.GiveBirth;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;

import java.util.ArrayList;

import static superstitio.utils.ActionUtility.addToBot_makeTempCardInBattle;

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
        this.setupBlock(BLOCK, new PregnantBlock().removeAutoBind());
        this.cardsToPreview = new GiveBirth();
        //this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        ArrayList<AbstractPower> sealPower = new ArrayList<>();
        monster.powers.forEach(power -> {
            if (power.type == AbstractPower.PowerType.DEBUFF || power instanceof ArtifactPower) {
                power.owner = player;
                sealPower.add(power);
                AutoDoneAction.addToBotAbstract(() -> monster.powers.remove(power));
            }
        });
        addToBot_gainBlock(new PregnantBlock_sealPower(sealPower, monster));
        addToBot_makeTempCardInBattle(new GiveBirth(), BattleCardPlace.Discard, upgraded);
    }

    @Override
    public void upgradeAuto() {
        upgradeBlock(UPGRADE_BLOCK);
        cardsToPreview.upgrade();
    }
}
