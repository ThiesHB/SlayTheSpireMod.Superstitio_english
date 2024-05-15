package superstitio.cards.maso.SkillCard;

import basemod.AutoAdd;
import com.evacipated.cardcrawl.mod.stslib.cards.targeting.SelfOrEnemyTargeting;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import superstitio.DataManager;
import superstitio.actions.AutoDoneInstantAction;
import superstitio.cardModifier.modifiers.block.PregnantBlock_chooseEffect;
import superstitio.cardModifier.modifiers.block.PregnantBlock_onlyForTag;
import superstitio.cardModifier.modifiers.block.PregnantBlock_sealPower;
import superstitio.cards.general.TempCard.GiveBirth;
import superstitio.cards.general.TempCard.SelfReference;
import superstitio.cards.maso.MasoCard;

import java.util.ArrayList;

import static superstitio.utils.ActionUtility.addToBot_makeTempCardInBattle;


//TODO 增加一个按照怪物体型获得格挡的效果
@AutoAdd.Ignore
public class HaveBirthWith extends MasoCard {
    public static final String ID = DataManager.MakeTextID(HaveBirthWith.class);

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.COMMON;

    public static final CardTarget CARD_TARGET = SelfOrEnemyTargeting.SELF_OR_ENEMY;

    private static final int COST = 1;
    private static final int BLOCK = 10;
    private static final int UPGRADE_BLOCK = 3;

    public HaveBirthWith() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupBlock(BLOCK, UPGRADE_BLOCK, new PregnantBlock_onlyForTag().removeAutoBind());
        this.cardsToPreview = new GiveBirth();
        //this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        ForMonster(monster);
    }

    private void ForPlayer(AbstractPlayer player) {
        ArrayList<AbstractPower> sealPower = new ArrayList<>();
        player.powers.forEach(power -> {
            if (power.type == AbstractPower.PowerType.DEBUFF || power instanceof ArtifactPower) {
                power.owner = AbstractDungeon.player;
                power.amount *= 2;
                sealPower.add(power);
                AutoDoneInstantAction.addToBotAbstract(() -> player.powers.remove(power));
            }
        });
        addToBot_gainBlock(new PregnantBlock_sealPower(sealPower, player));
        addToBot_makeTempCardInBattle(new SelfReference(), BattleCardPlace.Discard, upgraded);
    }

    private void ForMonster(AbstractMonster monster) {
        addToBot_gainBlock(new PregnantBlock_chooseEffect(monster, () -> {
        }, () -> {

        }));
        addToBot_makeTempCardInBattle(new GiveBirth(), BattleCardPlace.Discard, upgraded);
    }

    @Override
    public void upgradeAuto() {
        upgradeCardsToPreview();
    }
}
