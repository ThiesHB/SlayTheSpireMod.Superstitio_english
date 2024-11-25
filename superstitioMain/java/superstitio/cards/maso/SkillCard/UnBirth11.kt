package superstitio.cards.maso.SkillCard

import com.evacipated.cardcrawl.mod.stslib.blockmods.AbstractBlockModifier
import com.evacipated.cardcrawl.mod.stslib.blockmods.BlockInstance
import com.evacipated.cardcrawl.mod.stslib.blockmods.BlockModifierManager
import com.evacipated.cardcrawl.mod.stslib.cards.targeting.SelfOrEnemyTargeting
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.powers.*
import superstitio.DataManager
import superstitio.cardModifier.modifiers.block.PregnantBlock_sealPower
import superstitio.cards.general.TempCard.GiveBirth
import superstitio.cards.general.TempCard.SelfReference
import superstitio.cards.maso.MasoCard
import superstitioapi.actions.AutoDoneInstantAction
import superstitioapi.utils.ActionUtility
import superstitioapi.utils.CardUtility

//TODO 增加一个按照怪物体型获得格挡的效果
class UnBirth : MasoCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET) {
    init {
        this.setupBlock(BLOCK, UPGRADE_BLOCK, PregnantBlock_sealPower(ArrayList(), null).removeAutoBind())
        this.cardsToPreview = GiveBirth()
        //this.exhaust = true;
    }

    private fun ForPlayer(player: AbstractPlayer) {
        val sealPower = doSealPowers(player.powers)
        addToBot_gainCustomBlock(PregnantBlock_sealPower(sealPower, player))
        this.exhaust = true
        ActionUtility.addToBot_makeTempCardInBattle(SelfReference(), ActionUtility.BattleCardPlace.Hand, upgraded)
    }

    private fun ForMonsterBrokenSpaceStructure(monster: AbstractMonster) {
        val sealPower = doSealPowers(monster.powers)
        addToBot_gainCustomBlock(PregnantBlock_sealPower(sealPower, monster))
        this.exhaust = true
        ActionUtility.addToBot_makeTempCardInBattle(SelfReference(), ActionUtility.BattleCardPlace.Hand, upgraded)
    }

    private fun ForMonster(monster: AbstractMonster) {
        val sealPower = doSealPowers(monster.powers)
        addToBot_gainCustomBlock(PregnantBlock_sealPower(sealPower, monster))
        ActionUtility.addToBot_makeTempCardInBattle(GiveBirth(), ActionUtility.BattleCardPlace.Discard, upgraded)
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?) {
        val target = CardUtility.getSelfOrEnemyTarget(this, monster)
        if (target is AbstractPlayer) ForPlayer(AbstractDungeon.player)
        else if (BlockModifierManager.blockInstances(target).stream()
                .anyMatch { blockInstance: BlockInstance ->
                    blockInstance.blockTypes.stream()
                        .filter { blockModifier: AbstractBlockModifier? -> blockModifier is PregnantBlock_sealPower }
                        .anyMatch { blockModifier: AbstractBlockModifier -> (blockModifier as PregnantBlock_sealPower).sealCreature === monster }
                }
        ) ForMonsterBrokenSpaceStructure(target as AbstractMonster)
        else ForMonster(target as AbstractMonster)
    }

    override fun upgradeAuto() {
        upgradeCardsToPreview()
    }

    internal sealed class MonsterBodyType {
        data object Tiny : MonsterBodyType()
        data object Small : MonsterBodyType()
        data object Middle : MonsterBodyType()
        data object Big : MonsterBodyType()
        data object VeryBig : MonsterBodyType()
        companion object {
            fun values(): Array<MonsterBodyType> {
                return arrayOf(Tiny, Small, Middle, Big, VeryBig)
            }

            fun valueOf(value: String): MonsterBodyType {
                return when (value) {
                    "Tiny" -> Tiny
                    "Small" -> Small
                    "Middle" -> Middle
                    "Big" -> Big
                    "VeryBig" -> VeryBig
                    else -> throw IllegalArgumentException("No object superstitio.cards.maso.SkillCard.UnBirth.MonsterBodyType.$value")
                }
            }
        }
    } //    MonsterBodyType getMonsterBodyType(AbstractCreature creature){
    //        if (creature.maxHealth < 10)
    //            return MonsterBodyType.Tiny;
    //        if (creature.maxHealth < 20)
    //            return MonsterBodyType.Middle
    //
    //    }

    companion object {
        val ID: String = DataManager.MakeTextID(UnBirth::class.java)

        val CARD_TYPE: CardType = CardType.SKILL

        val CARD_RARITY: CardRarity = CardRarity.UNCOMMON

        val CARD_TARGET: CardTarget = SelfOrEnemyTargeting.SELF_OR_ENEMY

        private const val COST = 1
        private const val BLOCK = 8
        private const val UPGRADE_BLOCK = 3

        private fun doSealPowers(monster: MutableList<AbstractPower>): MutableList<AbstractPower> {
            val sealPower = ArrayList<AbstractPower>()
            monster.forEach { power: AbstractPower ->
                if (power is WeakPower || power is VulnerablePower || power is FrailPower || power is ArtifactPower) {
                    if (power is InvisiblePower) return@forEach
                    power.owner = AbstractDungeon.player
                    power.amount *= 2
                    sealPower.add(power)
                    AutoDoneInstantAction.addToBotAbstract { monster.remove(power) }
                }
            }
            return sealPower
        }
    }
}
