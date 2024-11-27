package superstitioapi.relicToBlight

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn
import com.megacrit.cardcrawl.blights.AbstractBlight
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.relics.AbstractRelic
import com.megacrit.cardcrawl.rooms.AbstractRoom
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption
import superstitioapi.relicToBlight.blightHook.BlightCampfire
import superstitioapi.relicToBlight.blightHook.BlightForCard
import superstitioapi.relicToBlight.blightHook.BlightOnEnterRoom

/**
 * 比起父类会更加傻瓜式，并且可以适配多个遗物重复的情况
 * 注意它的id就是遗物的id
 */
class InfoBlight(relic: AbstractRelic) : BlightWithRelic(relic), BlightOnEnterRoom, BlightForCard, BlightCampfire {
    /**
     * 单纯的复制一下，主要是在CustomBlightPatch中使用，可以不用管
     */
    fun makeCopy(): InfoBlight {
        return InfoBlight(relic.makeCopy())
    }

    /*
    下面是所有该荒疫包含的遗物可使用的Hook，注意，如果需要其他Hook的话，为了简洁明了，请写一个Patch写一个接口，然后在这里注册
     */
    override fun onEquip() {
        super.onEquip()
        relic.onEquip()
    }

    override fun onEnterRoom(room: AbstractRoom?) {
        relic.onEnterRoom(room)
    }

    override fun justEnteredRoom(room: AbstractRoom?) {
        relic.justEnteredRoom(room)
    }

    override fun onExhaust(card: AbstractCard?) {
        relic.onExhaust(card)
    }

    override fun onCardDraw(card: AbstractCard?) {
        relic.onCardDraw(card)
    }

    override fun onVictory() {
        super.onVictory()
        relic.onVictory()
    }

    override fun onPlayCard(card: AbstractCard, m: AbstractMonster?) {
        super.onPlayCard(card, m)
        relic.onPlayCard(card, m)
    }

    override fun onPlayerEndTurn() {
        super.onPlayerEndTurn()
        relic.onPlayerEndTurn()
    }

    override fun atBattleStart() {
        super.atBattleStart()
        relic.atBattleStart()
    }

    override fun atTurnStart() {
        super.atTurnStart()
        relic.atTurnStart()
    }

    override fun canUseCampfireOption(option: AbstractCampfireOption): Boolean {
        return relic.canUseCampfireOption(option)
    }

    override fun addCampfireOption(options: ArrayList<AbstractCampfireOption>) {
        relic.addCampfireOption(options)
    }

    override fun canPlay(card: AbstractCard?): Boolean {
        return relic.canPlay(card)
    }

    interface BecomeInfoBlight

    companion object {

        @SpirePatch2(clz = AbstractPlayer::class, method = "getRelic", paramtypez = [String::class])
        object GetRelicPatch {
            @SpirePrefixPatch
            @JvmStatic
            fun Prefix(__instance: AbstractPlayer, targetID: String): SpireReturn<AbstractRelic> {
                val anyRelic = __instance.blights
                    .filterIsInstance<BlightWithRelic>()
                    .filter { it.relic.relicId == targetID }
                    .map(BlightWithRelic::relic).firstOrNull()
                return if (anyRelic != null) SpireReturn.Return(anyRelic)
                else SpireReturn.Continue()
            }
        }

        @SpirePatch2(clz = AbstractPlayer::class, method = "hasRelic", paramtypez = [String::class])
        object HasRelicPatch {
            @SpirePostfixPatch
            @JvmStatic
            fun Prefix(__instance: AbstractPlayer, targetID: String): SpireReturn<Boolean> {
                val anyRelic = __instance.blights.stream()
                    .filter { blight: AbstractBlight? -> blight is BlightWithRelic }
                    .map { blight: AbstractBlight -> blight as BlightWithRelic }
                    .filter { blight: BlightWithRelic -> blight.relic.relicId == targetID }
                    .map(BlightWithRelic::relic).findAny()
                return if (anyRelic.isPresent) SpireReturn.Return(true)
                else SpireReturn.Continue()
            }
        }


        /**
         * 需要在初始化时，使用这个注册
         * 每添加一个转换为荒疫的遗物，就需要注册一下
         *
         * @param relic 遗物
         */
        @JvmStatic
        fun initInfoBlight(relic: AbstractRelic) {
            CustomBlightPatch.Assign(InfoBlight(relic))
        }

        /**
         * 需要在想要转化为荒疫的遗物中，修改如下的一个方法，使用这个函数，并且去掉super部分：
         * obtain
         */
        fun obtain(relic: AbstractRelic) {
//        new InfoBlight<>(relic).obtain();
            instanceObtain(relic, true)
            //obtain有一点bug，所以就先用着这个吧
        }

        /**
         * 需要在想要转化为荒疫的遗物中，修改如下的两个方法，使用这个函数，并且去掉super部分：
         * 无参数的instantObtain
         * 有参数的instantObtain
         */
        fun instanceObtain(relic: AbstractRelic, callOnEquip: Boolean) {
            InfoBlight(relic).instantObtain(AbstractDungeon.player, AbstractDungeon.player.blights.size, callOnEquip)
        }

        /**
         * 获取所有荒疫包含遗物中，指定类型的遗物，只获取第一个匹配的
         *
         * @param relicClass 遗物类型
         * @param <T>        遗物类型
         * @return 第一个匹配项
        </T> */
        fun <T : AbstractRelic> getOneRelic(relicClass: Class<T>): T? {
            for (blight in AbstractDungeon.player.blights) {
                if (blight is BlightWithRelic) {
                    if (relicClass.isInstance(blight.relic)) {
                        return relicClass.cast(blight.relic)
                    }
                }
            }
            return null
        }

        /**
         * 获取所有荒疫包含遗物中，指定类型的遗物，获取所有的匹配项
         *
         * @param relicClass 遗物类型
         * @param <T>        遗物类型
         * @return 列表
        </T> */
        fun <T : AbstractRelic> getAllRelics(relicClass: Class<T>): List<T> {
            return AbstractDungeon.player.blights
                .filterIsInstance<BlightWithRelic>()
                .filter { relicClass.isInstance(it.relic) }
                .map { relicClass.cast(it.relic) }
        }

        /**
         * 获取所有荒疫中，类型为指定类型的荒疫，感觉没啥用，不如直接一个for循环
         *
         * @param blightClass 荒疫类型
         * @param <T>         荒疫类型
         * @return 列表
        </T> */
        fun <T : AbstractBlight> getAllInfoBlights(blightClass: Class<T>): List<T> {
            return AbstractDungeon.player.blights
                .filter(blightClass::isInstance)
                .map(blightClass::cast)
        }

        /**
         * 获取所有荒疫包含遗物中，指定类型的遗物，获取所有的匹配项，并且还将获取这个荒疫位于荒疫列表的位置
         * （注意，不是这个荒疫是第几个匹配项，而是在总的荒疫中的位置）
         *
         * @param relicClass 遗物类型
         * @param <T>        遗物类型
         * @return 列表和索引
        </T> */
        fun <T : AbstractRelic> getAllRelicsWithBlightIndex(relicClass: Class<T>): Map<Int, T> {
            val list: MutableMap<Int, T> = HashMap()
            for ((s, blight) in AbstractDungeon.player.blights.withIndex()) {
                if (blight is BlightWithRelic) {
                    if (relicClass.isInstance(blight.relic)) {
                        list[s] = relicClass.cast(blight.relic)
                    }
                }
            }
            return list
        }

        fun addAsInfoBlight(relic: AbstractRelic) {
            if (AbstractDungeon.player == null) return
            if (AbstractDungeon.player.hasRelic(relic.relicId)) return
            if (relic is BecomeInfoBlight)
                relic.instantObtain(
                AbstractDungeon.player,
                AbstractDungeon.player.blights.size,
                false
            )
        }
    }
}
