package superstitio.cards.general.SkillCard

import basemod.AutoAdd
import com.evacipated.cardcrawl.mod.stslib.cards.targeting.SelfOrEnemyTargeting
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.monsters.AbstractMonster
import superstitio.DataManager
import superstitio.cards.CardOwnerPlayerManager.IsNotLupaCard
import superstitio.cards.CardOwnerPlayerManager.IsNotMasoCard
import superstitio.cards.general.GeneralCard
import superstitio.monster.ChibiKindMonster
import superstitioapi.pet.PetManager
import superstitioapi.renderManager.inBattleManager.InBattleDataManager
import superstitioapi.shader.heart.HeartMultiAtOneShader.HeartMultiAtOneEffect
import superstitioapi.utils.CardUtility

@AutoAdd.Ignore
class HaveBirthWith_Test : GeneralCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET), IsNotLupaCard, IsNotMasoCard
{
    override fun use(player: AbstractPlayer?, monster: AbstractMonster?)
    {
        val target = CardUtility.getSelfOrEnemyTarget(this, monster)
        if (target is AbstractMonster) InBattleDataManager.getPetManager()
            ?.let {
                PetManager.spawnMinion(target.javaClass)
                HeartMultiAtOneEffect(target.hb).addToEffectsQueue()
            }
        else InBattleDataManager.getPetManager()
            ?.let {
                PetManager.spawnMonster(ChibiKindMonster.MinionChibi(ChibiKindMonster()))
                HeartMultiAtOneEffect(target.hb).addToEffectsQueue()
            }
    }

    override fun upgradeAuto()
    {
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(HaveBirthWith_Test::class.java)

        val CARD_TYPE: CardType = CardType.SKILL

        val CARD_RARITY: CardRarity = CardRarity.COMMON

        val CARD_TARGET: CardTarget = SelfOrEnemyTargeting.SELF_OR_ENEMY

        private const val COST = 0
    }
}
