package superstitio.cards.lupa.AttackCard

import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import superstitio.DataManager
import superstitio.cards.general.FuckJob_Card
import superstitio.cards.lupa.LupaCard
import superstitioapi.SuperstitioApiSetup

//腹股沟
class Job_Groin : LupaCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET), FuckJob_Card {
    init {
        FuckJob_Card.initFuckJobCard(this)
        this.setupDamage(DAMAGE, UPGRADE_DAMAGE)
        this.setupMagicNumber(MAGIC)
    }


    private val originDamage: Int
        get() = if (this.upgraded) DAMAGE + UPGRADE_DAMAGE
        else DAMAGE

    private fun updateDamage() {
        val totalCost = AbstractDungeon.player.hand.group.stream()
            .filter { card: AbstractCard -> card.costForTurn >= 1 }
            .mapToInt(AbstractCard::costForTurn).sum()
        var damageDown = magicNumber.toFloat()
        if (AbstractDungeon.player.hand.group.isNotEmpty()) damageDown =
            totalCost.toFloat() * damageDown / AbstractDungeon.player.hand.group.size
        if (damageDown >= 1) this.isDamageModified = true
        this.baseDamage = (this.originDamage - damageDown).toInt()
    }

    override fun upgradeAuto() {
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?) {
        updateDamage()
        addToBot_dealDamage(monster, SuperstitioApiSetup.DamageEffect.HeartMultiInOne)
    }

    override fun applyPowers() {
        super.applyPowers()
        updateDamage()
    }

    override fun onMoveToDiscard() {
        this.initializeDescription()
    }

    override fun calculateCardDamage(monster: AbstractMonster) {
        updateDamage()
        super.calculateCardDamage(monster)
        this.initializeDescription()
    }

    companion object {
        val ID: String = DataManager.MakeTextID(Job_Groin::class.java)

        val CARD_TYPE: CardType = CardType.ATTACK

        val CARD_RARITY: CardRarity = CardRarity.COMMON

        val CARD_TARGET: CardTarget = CardTarget.ENEMY

        private const val COST = 1
        private const val DAMAGE = 18
        private const val UPGRADE_DAMAGE = 6
        private const val MAGIC = 8
    }
}
