package superstitio.cards.lupa.SkillCard.cardManipulation

import basemod.ReflectionHacks
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.MathUtils
import com.megacrit.cardcrawl.actions.common.UpgradeSpecificCardAction
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDrawPileEffect
import superstitio.DataManager
import superstitio.cards.lupa.LupaCard
import superstitioapi.actions.AutoDoneInstantAction
import superstitioapi.actions.ChoseCardFromGridSelectWindowAction

class NakedToSchool : LupaCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET) {
    init {
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC)
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?) {
        ChoseCardFromGridSelectWindowAction(AbstractDungeon.player.discardPile) { card: AbstractCard? ->
            val targetX = MathUtils.random(Settings.WIDTH.toFloat() * 0.1f, Settings.WIDTH.toFloat() * 0.9f)
            val targetY = MathUtils.random(Settings.HEIGHT.toFloat() * 0.8f, Settings.HEIGHT.toFloat() * 0.2f)
            addToBot(UpgradeSpecificCardAction(card))
            AutoDoneInstantAction.addToBotAbstract {
                AbstractDungeon.player.discardPile.removeCard(card)
                AbstractDungeon.effectList.add(UpgradeShineEffect(targetX, targetY))
                val toDrawPileEffect = ShowCardAndAddToDrawPileEffect(card, targetX, targetY, true)
                val fakeCard = ReflectionHacks.getPrivate<AbstractCard>(
                    toDrawPileEffect,
                    ShowCardAndAddToDrawPileEffect::class.java,
                    "card"
                )
                fakeCard.current_x = Gdx.graphics.width.toFloat()
                AbstractDungeon.effectList.add(toDrawPileEffect)
            }
        }
            .setWindowText(String.format(cardStrings.getEXTENDED_DESCRIPTION(0), this.magicNumber))
            .setAnyNumber(true)
            .setChoseAmount(this.magicNumber)
            .addToBot()
    }

    override fun upgradeAuto() {
    }

    companion object {
        val ID: String = DataManager.MakeTextID(NakedToSchool::class.java)

        val CARD_TYPE: CardType = CardType.SKILL

        val CARD_RARITY: CardRarity = CardRarity.UNCOMMON

        val CARD_TARGET: CardTarget = CardTarget.SELF

        private const val COST = 1
        private const val MAGIC = 3
        private const val UPGRADE_MAGIC = 1
    }
}
