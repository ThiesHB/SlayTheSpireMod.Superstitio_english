package superstitio.cards.general.AttackCard

import basemod.helpers.TooltipInfo
import com.badlogic.gdx.graphics.Color
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.monsters.AbstractMonster
import superstitio.DataManager
import superstitio.InBattleDataManager
import superstitio.cards.NormalCard
import superstitio.powers.SexualHeat.Orgasm
import superstitioapi.SuperstitioApiSetup
import superstitioapi.utils.ActionUtility

class CountSign : NormalCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET)
{
    init
    {
        this.setupDamage(DAMAGE, UPGRADE_DAMAGE)
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC)
    }

    private val originDamage: Int
        get() = if (this.upgraded) DAMAGE + UPGRADE_DAMAGE
        else DAMAGE

    private fun updateDamage()
    {
        this.baseDamage = this.originDamage
        if (ActionUtility.isNotInBattle) return
        if (!Orgasm.isPlayerInOrgasm()) return
        val damageUp = magicNumber * InBattleDataManager.OrgasmTimesTotal
        //        if (damageUp >= 1)
//            this.isDamageModified = true;
        this.baseDamage = this.originDamage + damageUp
    }

    override fun upgradeAuto()
    {
    }

    override fun getCustomTooltipsTop(): ArrayList<TooltipInfo>
    {
        val customTooltipsTop = super.getCustomTooltipsTop()
        customTooltipsTop.add(
            TooltipInfo(
                this.name, String.format(
                    cardStrings.getEXTENDED_DESCRIPTION(0),
                    InBattleDataManager.OrgasmTimesTotal
                )
            )
        )
        return customTooltipsTop
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?)
    {
        updateDamage()
        addToBot_dealDamage(monster, SuperstitioApiSetup.DamageEffect.HeartMultiInOne)
    }

    override fun applyPowers()
    {
        super.applyPowers()
        updateDamage()
    }

    override fun onMoveToDiscard()
    {
        this.initializeDescription()
    }

    override fun calculateCardDamage(monster: AbstractMonster?)
    {
        updateDamage()
        super.calculateCardDamage(monster)
        this.initializeDescription()
    }

    override fun triggerOnGlowCheck()
    {
        this.glowColor = BLUE_BORDER_GLOW_COLOR.cpy()
        if (Orgasm.isPlayerInOrgasm())
        {
            this.glowColor = Color.PINK.cpy()
        }
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(CountSign::class.java)

        val CARD_TYPE: CardType = CardType.ATTACK

        val CARD_RARITY: CardRarity = CardRarity.RARE

        val CARD_TARGET: CardTarget = CardTarget.ENEMY

        private const val COST = 2
        private const val DAMAGE = 16
        private const val UPGRADE_DAMAGE = 5

        private const val MAGIC = 3

        private const val UPGRADE_MAGIC = 1
    }
}
