package superstitio.cards.maso.SkillCard

import basemod.cardmods.ExhaustMod
import basemod.cardmods.RetainMod
import basemod.helpers.CardModifierManager
import com.badlogic.gdx.graphics.Color
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.powers.PoisonPower
import superstitio.DataManager
import superstitio.cards.maso.MasoCard
import superstitio.delayHpLose.DelayHpLosePower
import superstitio.powers.SexualHeat.Orgasm
import superstitioapi.utils.ActionUtility
import java.util.function.ToIntFunction

class DrinkPoison : MasoCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET) {
    init {
        this.setupMagicNumber(MAGIC)
        CardModifierManager.addModifier(this, ExhaustMod())
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?) {
        val totalDelayHpLose: Int =
            DelayHpLosePower.findAll(AbstractDungeon.player, DelayHpLosePower::class.java)
                .mapToInt(ToIntFunction(DelayHpLosePower::amount)).sum()
        DelayHpLosePower.addToBot_removePower(totalDelayHpLose / 2, AbstractDungeon.player, true)
        if (totalDelayHpLose / 4 <= 0) return
        addToBot_applyPower(
            PoisonPower(
                AbstractDungeon.player, AbstractDungeon.player,
                totalDelayHpLose / 4
            )
        )
    }

    override fun triggerOnGlowCheck() {
        this.glowColor = BLUE_BORDER_GLOW_COLOR.cpy()
        if (Orgasm.isPlayerInOrgasm()) {
            this.glowColor = Color.PINK.cpy()
        }
    }

    override fun initializeDescription() {
        if (!ActionUtility.isNotInBattle) {
            val totalDelayHpLose: Int = DelayHpLosePower.findAll(
                AbstractDungeon.player,
                DelayHpLosePower::class.java
            )
                .mapToInt(ToIntFunction(DelayHpLosePower::amount)).sum()
            this.magicNumber = totalDelayHpLose / 2
            this.baseMagicNumber = totalDelayHpLose / 2
        }
        super.initializeDescription()
    }

    override fun applyPowers() {
        super.applyPowers()
        initializeDescription()
    }

    override fun upgradeAuto() {
        CardModifierManager.addModifier(this, RetainMod())
    }

    companion object {
        val ID: String = DataManager.MakeTextID(DrinkPoison::class.java)

        val CARD_TYPE: CardType = CardType.SKILL

        val CARD_RARITY: CardRarity = CardRarity.UNCOMMON

        val CARD_TARGET: CardTarget = CardTarget.SELF

        private const val COST = 1
        private const val MAGIC = 0
    }
}

