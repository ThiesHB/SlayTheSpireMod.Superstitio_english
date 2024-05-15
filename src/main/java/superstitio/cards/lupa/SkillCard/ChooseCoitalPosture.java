package superstitio.cards.lupa.SkillCard;

import basemod.cardmods.ExhaustMod;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cards.lupa.LupaCard;
import superstitio.cards.general.AbstractCard_FuckJob;
import superstitio.utils.ActionUtility;
import superstitio.utils.CardUtility;

import java.util.List;
import java.util.stream.Collectors;

//随机生成一张Fuck/Job卡
public class ChooseCoitalPosture extends LupaCard {
    public static final String ID = DataManager.MakeTextID(ChooseCoitalPosture.class);

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.UNCOMMON;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 1;
    private static final int COST_UPGRADED_NEW = 0;


    public ChooseCoitalPosture() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
    }

    public static AbstractCard_FuckJob getRandomFuckJobCard() {
        return CardUtility.getRandomFromList(getAllFuckJobCard(), AbstractDungeon.cardRandomRng);
    }

    public static List<AbstractCard_FuckJob> getAllFuckJobCard() {
        return CardLibrary.cards.values().stream()
                .filter(card -> card instanceof AbstractCard_FuckJob)
                .map(card -> (AbstractCard_FuckJob) card).collect(Collectors.toList());
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        AbstractCard card = getRandomFuckJobCard().makeCopy();
//        if (!CardModifierManager.hasModifier(card, ExhaustMod.ID))
        card.setCostForTurn(0);
        CardModifierManager.addModifier(card, new ExhaustMod());
        ActionUtility.addToBot_makeTempCardInBattle(card, BattleCardPlace.Hand, upgraded);
    }

    @Override
    public void upgradeAuto() {
        upgradeBaseCost(COST_UPGRADED_NEW);
    }
}

