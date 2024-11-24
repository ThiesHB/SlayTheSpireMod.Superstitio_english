package superstitio.cards.lupa.SkillCard.getSemen;

import basemod.cardmods.ExhaustMod;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import superstitio.DataManager;
import superstitio.cards.lupa.LupaCard;
import superstitio.powers.SexualHeat;
import superstitio.powers.lupaOnly.InsideSemen;
import superstitio.powers.lupaOnly.SemenPower;

//强制塞入
public class PutEveryCumInBody extends LupaCard {
    public static final String ID = DataManager.MakeTextID(PutEveryCumInBody.class);

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.UNCOMMON;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 2;


    public PutEveryCumInBody() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        CardModifierManager.addModifier(this, new ExhaustMod());
    }

    @Override
    public void upgradeAuto() {
        CardModifierManager.removeModifiersById(this, ExhaustMod.ID, false);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        for (AbstractPower power : AbstractDungeon.player.powers) {
            if (power instanceof SemenPower && !(power instanceof InsideSemen)) {
//                addToBot_removeSpecificPower(power);
                SexualHeat.addToBot_addSexualHeat(AbstractDungeon.player, power.amount * ((SemenPower) power).getSemenValue());
//                addToBot_applyPower(new InsideSemen(AbstractDungeon.player, power.amount));
                ((SemenPower) power).transToOtherSemen((num) -> new InsideSemen(AbstractDungeon.player, num));
            }
        }
//        addToBotAbstract(() -> {
//            for (AbstractPower power : AbstractDungeon.player.powers) {
//                if (power instanceof InsideSemen) {
//                    ((InsideSemen) power).expand(power.amount);
//                }
//            }
//        });
    }
}
