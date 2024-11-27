package superstitio.cards.lupa.SkillCard.getSemen

import basemod.cardmods.ExhaustMod
import basemod.helpers.CardModifierManager
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import superstitio.DataManager
import superstitio.cards.lupa.LupaCard
import superstitio.powers.SexualHeat
import superstitio.powers.lupaOnly.InsideSemen
import superstitio.powers.lupaOnly.SemenPower

//强制塞入
class PutEveryCumInBody : LupaCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET)
{
    init
    {
        CardModifierManager.addModifier(this, ExhaustMod())
    }

    override fun upgradeAuto()
    {
        CardModifierManager.removeModifiersById(this, ExhaustMod.ID, false)
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?)
    {
        for (power in AbstractDungeon.player.powers)
        {
            if (power is SemenPower && power !is InsideSemen)
            {
//                addToBot_removeSpecificPower(power);
                SexualHeat.addToBot_addSexualHeat(
                    AbstractDungeon.player,
                    power.amount * (power as SemenPower).getSemenValue()
                )
                //                addToBot_applyPower(new InsideSemen(AbstractDungeon.player, power.amount));
                (power as SemenPower).transToOtherSemen { num: Int -> InsideSemen(AbstractDungeon.player, num) }
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

    companion object
    {
        val ID: String = DataManager.MakeTextID(PutEveryCumInBody::class.java)

        val CARD_TYPE: CardType = CardType.SKILL

        val CARD_RARITY: CardRarity = CardRarity.UNCOMMON

        val CARD_TARGET: CardTarget = CardTarget.SELF

        private const val COST = 2
    }
}
