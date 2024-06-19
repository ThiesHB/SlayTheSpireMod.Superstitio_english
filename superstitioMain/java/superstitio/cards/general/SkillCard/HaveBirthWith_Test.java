package superstitio.cards.general.SkillCard;

import com.evacipated.cardcrawl.mod.stslib.cards.targeting.SelfOrEnemyTargeting;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cards.general.GeneralCard;
import superstitio.monster.ChibiKindMonster;
import superstitioapi.InBattleDataManager;
import superstitioapi.pet.PetManager;
import superstitioapi.shader.HeartShader;

import static superstitio.cards.CardOwnerPlayerManager.IsNotLupaCard;
import static superstitio.cards.CardOwnerPlayerManager.IsNotMasoCard;
import static superstitioapi.utils.CardUtility.getSelfOrEnemyTarget;

//@AutoAdd.Ignore
public class HaveBirthWith_Test extends GeneralCard implements IsNotLupaCard, IsNotMasoCard {
    public static final String ID = DataManager.MakeTextID(HaveBirthWith_Test.class);

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.COMMON;

    public static final CardTarget CARD_TARGET = SelfOrEnemyTargeting.SELF_OR_ENEMY;

    private static final int COST = 0;

    public HaveBirthWith_Test() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        AbstractCreature target = getSelfOrEnemyTarget(this, monster);
        if (target instanceof AbstractMonster)
            InBattleDataManager.getPetManager()
                    .ifPresent(petManager -> {
                        PetManager.spawnMinion((
                                (AbstractMonster) target).getClass(
                        ));
                        new HeartShader.HeartMultiAtOneEffect(target.hb).addToEffectsQueue();
                    });

        else
            InBattleDataManager.getPetManager()
                    .ifPresent(petManager -> {
                        PetManager.spawnMonster(new ChibiKindMonster.MinionChibi(new ChibiKindMonster()));
                        new HeartShader.HeartMultiAtOneEffect(target.hb).addToEffectsQueue();
                    });
    }

    @Override
    public void upgradeAuto() {
    }
}
