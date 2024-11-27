package superstitio.cards.general.PowerCard

import basemod.cardmods.EtherealMod
import basemod.helpers.CardModifierManager
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.monsters.AbstractMonster
import superstitio.DataManager
import superstitio.cards.general.GeneralCard
import superstitio.cards.general.PowerCard.monsterGirl.FishGirlMode
import superstitio.cards.general.PowerCard.monsterGirl.KakaaGirlMode
import superstitio.cards.general.PowerCard.monsterGirl.SlimeGirlMode

class MonsterGirlMode : GeneralCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET)
{
    init
    {
        CardModifierManager.addModifier(this, EtherealMod())
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?)
    {
        val stanceChoices = ArrayList<AbstractCard>()
        stanceChoices.add(FishGirlMode())
        stanceChoices.add(KakaaGirlMode())
        stanceChoices.add(SlimeGirlMode())
        //        if (this.upgraded) {
//            for (final AbstractCard c : stanceChoices) {
//                c.upgrade();
//            }
//        }
        this.addToBot(ChooseOneAction(stanceChoices))
    }

    override fun upgradeAuto()
    {
        CardModifierManager.removeModifiersById(this, EtherealMod.ID, false)
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(MonsterGirlMode::class.java)

        val CARD_TYPE: CardType = CardType.POWER

        val CARD_RARITY: CardRarity = CardRarity.RARE

        val CARD_TARGET: CardTarget = CardTarget.SELF

        private const val COST = 2
    }
}

