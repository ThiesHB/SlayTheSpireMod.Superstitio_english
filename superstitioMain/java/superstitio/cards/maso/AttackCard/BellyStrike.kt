package superstitio.cards.maso.AttackCard

import com.evacipated.cardcrawl.mod.stslib.cards.targeting.SelfOrEnemyTargeting
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.powers.LoseStrengthPower
import com.megacrit.cardcrawl.powers.StrengthPower
import superstitio.DataManager
import superstitio.cards.IsMasoCard
import superstitio.cards.maso.MasoCard
import superstitioapi.actions.AutoDoneInstantAction
import superstitioapi.utils.CardUtility

@IsMasoCard
class BellyStrike : MasoCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET)
{
    init
    {
        this.setupDamage(DAMAGE, UPGRADE_DAMAGE)
        tags.add(CardTags.STRIKE)
        this.setupMagicNumber(MAGIC)
    }


    override fun use(player: AbstractPlayer?, monster: AbstractMonster?)
    {
        val target = CardUtility.getSelfOrEnemyTarget(this, monster)
        addToBot_applyPower(LoseStrengthPower(target, this.magicNumber))
        if (target is AbstractPlayer) addToBot_dealDamage(target, this.damage / 2, AttackEffect.BLUNT_HEAVY)
        else addToBot_dealDamage(target, this.damage, AttackEffect.BLUNT_HEAVY)
        addToBot_applyPower(StrengthPower(target, this.magicNumber))
        if (!target.isPlayer) return
        AutoDoneInstantAction.addToBotAbstract {
            if (AbstractDungeon.player.lastDamageTaken > 0)
            {
                val strength = AbstractDungeon.player.getPower(StrengthPower.POWER_ID)
                addToBot_applyPower(StrengthPower(AbstractDungeon.player, strength.amount))
            }
        }
    }


    override fun update()
    {
        super.update()
        if (AbstractDungeon.player != null && AbstractDungeon.player.hoveredCard === this)
        {
            calculateCardDamageForSelfOrEnemyTargeting()
            initializeDescription()
        }
    }

    //    @Override
    //    public void unhover() {
    //        super.unhover();
    //        if (ActionUtility.isNotInBattle) return;
    //        applyPowers();
    //        initializeDescription();
    //    }
    override fun calculateCardDamageForSelfOrEnemyTargeting(): AbstractCreature?
    {
        val target = super.calculateCardDamageForSelfOrEnemyTargeting()
        if (target is AbstractPlayer)
        {
            this.damage /= 2
            this.isDamageModified = true
        }
        return target
    }

    override fun upgradeAuto()
    {
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(BellyStrike::class.java)

        val CARD_TYPE: CardType = CardType.ATTACK

        val CARD_RARITY: CardRarity = CardRarity.UNCOMMON

        val CARD_TARGET: CardTarget = SelfOrEnemyTargeting.SELF_OR_ENEMY

        private const val COST = 2
        private const val DAMAGE = 22
        private const val UPGRADE_DAMAGE = 8

        private const val MAGIC = 4
    }
}
