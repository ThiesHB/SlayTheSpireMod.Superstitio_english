package superstitio.cards.general.SkillCard;

import com.evacipated.cardcrawl.mod.stslib.cards.targeting.SelfOrEnemyTargeting;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import superstitio.DataManager;
import superstitio.cardModifier.modifiers.block.PregnantBlock_chooseEffect;
import superstitio.cardModifier.modifiers.block.PregnantBlock_newMonster;
import superstitio.cardModifier.modifiers.block.PregnantBlock_sealPower;
import superstitio.cards.general.GeneralCard;
import superstitio.cards.general.TempCard.GiveBirth;
import superstitio.cards.general.TempCard.SelfReference;
import superstitioapi.InBattleDataManager;
import superstitioapi.actions.AutoDoneInstantAction;
import superstitioapi.pet.Minion;
import superstitioapi.pet.PetManager;
import superstitioapi.utils.ActionUtility;

import java.util.ArrayList;

import static superstitioapi.pet.CopyAndSpawnMonsterUtility.motherFuckerWhyIShouldUseThisToCopyMonster;
import static superstitioapi.utils.ActionUtility.addToBot_makeTempCardInBattle;

//TODO 希望这个卡能制造一个跟班
//@AutoAdd.Ignore
public class HaveBirthWith extends GeneralCard {
    public static final String ID = DataManager.MakeTextID(HaveBirthWith.class);

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.COMMON;

    public static final CardTarget CARD_TARGET = SelfOrEnemyTargeting.SELF_OR_ENEMY;

    private static final int COST = 1;
    private static final int BLOCK = 10;
    private static final int UPGRADE_BLOCK = 3;

    public HaveBirthWith() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupBlock(BLOCK, UPGRADE_BLOCK, new PregnantBlock_newMonster().removeAutoBind());
        this.cardsToPreview = new GiveBirth();
        //this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        AbstractCreature target = SelfOrEnemyTargeting.getTarget(this);
        if (target instanceof AbstractMonster)
            InBattleDataManager.getPetManager()
                    .ifPresent(petManager -> PetManager.spawnMonster(new Minion(
                            motherFuckerWhyIShouldUseThisToCopyMonster((
                                    (AbstractMonster) target).getClass())
                    )));
//        ForMonster(monster);
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
        addToBot_gainCustomBlock(new PregnantBlock_sealPower(sealPower, player));
        addToBot_makeTempCardInBattle(new SelfReference(), ActionUtility.BattleCardPlace.Discard, upgraded);
    }

    private void ForMonster(AbstractMonster monster) {
        addToBot_gainCustomBlock(new PregnantBlock_chooseEffect(monster, () -> {
        }, () -> {

        }));
        addToBot_makeTempCardInBattle(new GiveBirth(), ActionUtility.BattleCardPlace.Discard, upgraded);
    }

    @Override
    public void upgradeAuto() {
        upgradeCardsToPreview();
    }
}
