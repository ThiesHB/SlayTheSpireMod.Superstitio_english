package superstitioapi.cardPool

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.megacrit.cardcrawl.screens.charSelect.CharacterOption
import superstitioapi.player.PlayerInitPostDungeonInitialize
import superstitioapi.renderManager.characterSelectScreenRender.RenderInCharacterSelect

/**
 * 绑定在角色身上
 */
interface CharacterWithCardPool : PlayerInitPostDungeonInitialize, RenderInCharacterSelect
{
//    val self: AbstractPlayer
//        get() = this as AbstractPlayer

    /**
     * 记得写满
     */
    val ableCardPools: List<BaseCardPool>

    val characterUUID: String
        get() = this.javaClass.simpleName

    override fun renderInCharacterSelectScreen(characterOption: CharacterOption, sb: SpriteBatch)
    {
        CardPoolManager.render(this, characterOption, sb)
    }

    override fun updateInCharacterSelectScreen(characterOption: CharacterOption)
    {
        CardPoolManager.update(this, characterOption)
    }
}
