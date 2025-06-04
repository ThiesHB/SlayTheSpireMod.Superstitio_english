package superstitio.monster

import basemod.ReflectionHacks
import basemod.abstracts.CustomMonster
import com.evacipated.cardcrawl.modthespire.lib.SpireOverride
import com.megacrit.cardcrawl.actions.common.RollMoveAction
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.CardQueueItem
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.helpers.Hitbox
import com.megacrit.cardcrawl.helpers.ImageMaster
import com.megacrit.cardcrawl.helpers.PowerTip
import com.megacrit.cardcrawl.localization.MonsterStrings
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.monsters.AbstractMonster.Intent.ATTACK
import com.megacrit.cardcrawl.random.Random
import superstitio.DataManager
import superstitio.DataManager.Companion.ImgPath
import superstitioapi.actions.AutoDoneInstantAction
import superstitioapi.pet.MinionMonster
import superstitioapi.utils.CardUtility
import superstitioapi.utils.CreatureUtility
import superstitioapi.utils.ListUtility

class ChibiKindMonster : CustomMonster(
    NAME, ID, AbstractDungeon.player.maxHealth, 0f, 0f, 250.0f, 250.0f, ListUtility.getRandomFromList(
        CHIBI_CHARACTER, Random()
    )
)
{
    init
    {
        this.setHp(AbstractDungeon.player.maxHealth)
        this.animX = 0.0f
        this.animY = 0.0f
        this.type = EnemyType.ELITE
        this.dialogX = -70.0f * Settings.scale
        this.dialogY = 50.0f * Settings.scale
        damage.add(DamageInfo(this, 6))

        if (img != null)
        {
            this.hb_w = img.width * Settings.scale
            this.hb_h = img.height * Settings.xScale
            this.hb = Hitbox(this.hb_w, this.hb_h)
            this.healthHb = Hitbox(this.hb_w, 72.0f * Settings.scale)
            this.refreshHitboxLocation()
            this.refreshIntentHbLocation()
        }
    }

    @SpireOverride
    protected fun updateIntentTip()
    {
        val intentTip = ReflectionHacks.getPrivate<PowerTip>(this, AbstractMonster::class.java, "intentTip")
        when (this.nextMove)
        {
            1.toByte() ->
            {
                intentTip.header = DIALOG[0]
                intentTip.body = DIALOG[1]
                intentTip.img = ImageMaster.INTENT_ATK_TIP_1
            }

            2.toByte() ->
            {
                intentTip.header = DIALOG[2]
                intentTip.body = DIALOG[3]
                intentTip.img = ImageMaster.INTENT_DEFEND
            }

            else       ->
            {
                intentTip.header = "NOT SET"
                intentTip.body = "NOT SET"
                intentTip.img = ImageMaster.INTENT_UNKNOWN
            }
        }
    }

    private fun playStrike(): Boolean
    {
        val strike = CardUtility.AllCardInBattle_ButWithoutCardInUse()
            .filter { card: AbstractCard -> !willPlayCards.contains(card) }
            .firstOrNull { card: AbstractCard -> card.hasTag(AbstractCard.CardTags.STARTER_STRIKE) }
        strike?.let { card: AbstractCard ->
            willPlayCards.add(card)
            //            addToBot(new UseCardAction(card));
            AbstractDungeon.actionManager.cardQueue.add(
                CardQueueItem(
                    card,
                    CreatureUtility.getRandomMonsterSafe(),
                    0,
                    true,
                    true
                )
            )
        }
        return strike != null
    }

    private fun playDefend(): Boolean
    {
        val defend = CardUtility.AllCardInBattle_ButWithoutCardInUse()
            .filter { card: AbstractCard -> !willPlayCards.contains(card) }
            .firstOrNull { card: AbstractCard -> card.hasTag(AbstractCard.CardTags.STARTER_DEFEND) }
        defend?.let { card: AbstractCard ->
            willPlayCards.add(card)
            card.dontTriggerOnUseCard = true
            //            card.dontTriggerOnUseCard
//            addToBot(new UseCardAction(card));
            AbstractDungeon.actionManager.cardQueue.add(
                CardQueueItem(
                    card,
                    CreatureUtility.getRandomMonsterSafe(),
                    0,
                    true,
                    true
                )
            )
        }
        return defend != null
    }

    override fun takeTurn()
    {
        this.flashIntent()
        when (this.intent)
        {
            ATTACK        -> playStrike()
            Intent.DEFEND -> playDefend()
            else          ->
            {
            }
        }
        addToBot(RollMoveAction(this))
        //        addToBotAbstract(() ->
        AutoDoneInstantAction.addToBotAbstract(willPlayCards::clear, 2)
        //        );
    }

    override fun getMove(num: Int)
    {
        if (num < 50)
        {
            this.setMove(STRIKE, 1.toByte(), ATTACK, 6)
        }
        else
        {
            this.setMove(DEFEND, 2.toByte(), Intent.DEFEND)
        }
    }

    class MinionChibi(private val petCoreChibi: ChibiKindMonster) : MinionMonster(petCoreChibi, CHIBI_DRAW_SCALE)
    {
        init
        {
            this.flipHorizontal = false
        }

        override fun takeTurn()
        {
            petCoreChibi.takeTurn()
        }

        companion object
        {
            private const val CHIBI_DRAW_SCALE = 2.0f
        }
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(ChibiKindMonster::class.java)
        val CHIBI_CHARACTER: Array<String> = arrayOf(
            ImgPath.characterPath.resolveFile("chibiCharacter0"),
            ImgPath.characterPath.resolveFile("chibiCharacter1"),
            ImgPath.characterPath.resolveFile("chibiCharacter2")
        )
        private val willPlayCards: MutableList<AbstractCard> = ArrayList()
        private val monsterStrings: MonsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID)
        val NAME: String = monsterStrings.NAME
        val MOVES: Array<String> = monsterStrings.MOVES
        val STRIKE: String = MOVES[0]
        val DEFEND: String = MOVES[1]
        val DIALOG: Array<String> = monsterStrings.DIALOG
    }
}
