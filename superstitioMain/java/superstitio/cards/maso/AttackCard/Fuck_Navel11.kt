package superstitio.cards.maso.AttackCard

import basemod.cardmods.RetainMod
import basemod.helpers.CardModifierManager
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.powers.VulnerablePower
import superstitio.DataManager
import superstitio.cards.CardOwnerPlayerManager.IsNotLupaCard
import superstitio.cards.general.FuckJob_Card
import superstitio.cards.maso.MasoCard
import superstitio.powers.SexualHeat
import superstitio.powers.patchAndInterface.interfaces.orgasm.OnOrgasm_onOrgasm
import superstitioapi.SuperstitioApiSetup
import superstitioapi.actions.*
import superstitioapi.cards.DamageActionMaker

class Fuck_Navel : MasoCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET), FuckJob_Card, OnOrgasm_onOrgasm,
    IsNotLupaCard {
    //    private int orgasmTimes;
    init {
        FuckJob_Card.initFuckJobCard(this)
        this.setupDamage(DAMAGE, UPGRADE_DAMAGE)
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC)
        this.isMultiDamage = true
        CardModifierManager.addModifier(this, RetainMod())
        //        this.orgasmTimes = 0;
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?) {
//        addToBot_dealDamageToAllEnemies(SuperstitioApiSetup.DamageEffect.HeartMultiInOne);
//        addToBot_dealDamage(AbstractDungeon.player, SuperstitioApiSetup.DamageEffect.HeartMultiInOne);
        val damageMap = DamageAllEnemiesAction.Builder.GetDamageMapFromMultiDamagesOfCard(this.multiDamage)
        this.calculateSingleDamage(AbstractDungeon.player, AbstractDungeon.player)
        damageMap[AbstractDungeon.player] = damage
        DamageActionMaker.makeDamages(damageMap)
            .setSource(AbstractDungeon.player)
            .setDamageType(DamageType.NORMAL)
            .setEffect(SuperstitioApiSetup.DamageEffect.HeartMultiInOne)
            .setExampleCard(this)
            .addToBot()

        addToBot_applyPower(VulnerablePower(AbstractDungeon.player, 1, false))
    }

    override fun upgradeAuto() {
    }

    override fun onOrgasm(SexualHeatPower: SexualHeat) {
        if (AbstractDungeon.player.hand.contains(this)) {
            this.flash()
            this.setupDamage(this.baseDamage + this.magicNumber)
        }
    }

    companion object {
        val ID: String = DataManager.MakeTextID(Fuck_Navel::class.java)

        val CARD_TYPE: CardType = CardType.ATTACK

        val CARD_RARITY: CardRarity = CardRarity.UNCOMMON

        val CARD_TARGET: CardTarget = CardTarget.ALL

        private const val COST = 2
        private const val DAMAGE = 15
        private const val UPGRADE_DAMAGE = 5

        private const val MAGIC = 3
        private const val UPGRADE_MAGIC = 1
    }
}
