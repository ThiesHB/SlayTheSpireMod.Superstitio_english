package superstitio.cards.lupa.SkillCard;

import basemod.cardmods.ExhaustMod;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cards.lupa.AbstractLupaCard;
import superstitio.cards.lupa.AbstractLupaCard_FuckJob;
import superstitio.utils.ActionUtility;
import superstitio.utils.CardUtility;

import java.util.List;
import java.util.stream.Collectors;

//随机生成一张Fuck/Job卡
public class ChooseCoitalPosture extends AbstractLupaCard {
    public static final String ID = DataManager.MakeTextID(ChooseCoitalPosture.class.getSimpleName());

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.UNCOMMON;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 1;
    private static final int COST_UPDATE = 0;


    public ChooseCoitalPosture() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
    }

    public static AbstractLupaCard_FuckJob getRandomFuckJobCard() {
        return CardUtility.getRandomFromList(getAllFuckJobCard(), AbstractDungeon.cardRandomRng);
    }

    public static List<AbstractLupaCard_FuckJob> getAllFuckJobCard() {
        return CardLibrary.cards.values().stream()
                .filter(card -> card instanceof AbstractLupaCard_FuckJob)
                .map(card -> (AbstractLupaCard_FuckJob) card).collect(Collectors.toList());
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        AbstractCard card = getRandomFuckJobCard().makeCopy();
//        if (!CardModifierManager.hasModifier(card, ExhaustMod.ID))
        CardModifierManager.addModifier(card, new ExhaustMod());
        ActionUtility.addToBot_makeTempCardInBattle(card, BattleCardPlace.Hand, upgraded);
    }

    @Override
    public void upgradeAuto() {
        upgradeBaseCost(COST_UPDATE);
    }
}

