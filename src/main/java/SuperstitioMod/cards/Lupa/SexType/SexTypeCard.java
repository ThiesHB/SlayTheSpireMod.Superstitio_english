package SuperstitioMod.cards.Lupa.SexType;

import SuperstitioMod.SuperstitioModSetup;
import SuperstitioMod.cards.Lupa.PowerCard.ChokeChoker;
import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.evacipated.cardcrawl.modthespire.lib.SpireSuper;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

import java.util.ArrayList;

//@CardIgnore
public class SexTypeCard extends AbstractSexTypeCard
{
    public static final String ID = SuperstitioModSetup.MakeTextID(SexTypeCard.class.getSimpleName());
    public static final String RIBBON_COLOR = "#198a2a";
    public String textPrefix;

    public SexTypeCard() {
        super(ID, AbstractCard.CardType.SKILL, 1, AbstractCard.CardRarity.SPECIAL, AbstractCard.CardTarget.ALL,"default");
        this.textPrefix = "";
        //this.tags.add(SneckoMod.BANNEDFORSNECKO);
        //this.setPortraitTextures("bronzeResources/images/512/frame_function.png", "bronzeResources/images/1024/frame_function.png");
        //this.setBackgroundTexture("bronzeResources/images/512/bg_skill_function.png", "bronzeResources/images/1024/bg_skill_function.png");
        //this.doNothingSpecificInParticular();
    }

    @Override
    protected Texture getPortraitImage() {
        return null;
    }

    public void use(final AbstractPlayer abstractPlayer, final AbstractMonster abstractMonster) {
    }

    public void upp() {
    }

//    public boolean isPerfect() {
//        int x = 0;
//        for (final AbstractCard q : this.cards()) {
//            if (x == 0 && q instanceof Constructor) {
//                ++x;
//            }
//            else if (x == 1 && q instanceof Separator) {
//                ++x;
//            }
//            else {
//                if (x == 2 && q instanceof Terminator) {
//                    return true;
//                }
//                if (x == 2 && q instanceof Separator && AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(ElectromagneticCoil.ID)) {
//                    ++x;
//                }
//                else {
//                    if (x == 3 && q instanceof Terminator) {
//                        return true;
//                    }
//                    continue;
//                }
//            }
//        }
//        return false;
//    }

//    public boolean triplicateCheck() {
//        final String cardIDBase = this.cards().get(0).cardID;
//        return this.cards().stream().allMatch(c -> c.cardID.equals(cardIDBase)) && this.cards().get(0).hasTriplicate();
//    }

    public void doNothingSpecificInParticular() {
        if (this.textPrefix.isEmpty()) {
            this.name = this.EXTENDED_DESCRIPTION[0];
        }
//        else if (this.isPerfect()) {
//            this.name = this.EXTENDED_DESCRIPTION[1];
//        }
//        else if (this.triplicateCheck()) {
//            this.name = this.cards().get(0).getTriplicate();
//        }
        else {
            this.name = this.textPrefix + this.EXTENDED_DESCRIPTION[2];
        }
        this.initializeTitle();
    }

    @SpireOverride
    protected void renderBannerImage(final SpriteBatch sb, final float x, final float y) {
        final Color blah = ReflectionHacks.getPrivate(this, AbstractCard.class, "renderColor");
        ReflectionHacks.setPrivate(this, AbstractCard.class, "renderColor", Color.valueOf("#198a2a"));
        SpireSuper.call(new Object[] { sb, x, y });
        ReflectionHacks.setPrivate(this, AbstractCard.class, "renderColor", blah);
    }

    public ArrayList<AbstractSexTypeCard> cards() {
        final ArrayList<AbstractSexTypeCard> mCardList = new ArrayList<>();
//        for (final AbstractCardModifier m : CardModifierManager.getModifiers((AbstractCard)this, CardEffectsCardMod.ID)) {
//            if (m instanceof CardEffectsCardMod) {
//                mCardList.add(((CardEffectsCardMod)m).stored());
//            }
//        }
        return mCardList;
    }

    public void onMoveToDiscard() {
//        if (CardModifierManager.hasModifier((AbstractCard)this, CardEffectsCardMod.ID)) {
//            for (final AbstractCardModifier m : CardModifierManager.getModifiers((AbstractCard)this, CardEffectsCardMod.ID)) {
//                if (m instanceof CardEffectsCardMod) {
//                    ((CardEffectsCardMod)m).stored().resetAttributes();
//                }
//            }
//            this.initializeDescription();
//        }
    }

    public boolean canUpgrade() {
        return false;
    }

    @Override
    public void upgrade() {
    }

    private TextureAtlas.AtlasRegion getEncodablePortrait(final AbstractCard c) {
        if (UnlockTracker.betaCardPref.getBoolean(c.cardID, false) || Settings.PLAYTESTER_ART_MODE) {
            return c.jokePortrait;
        }
        return c.portrait;
    }

    @SpireOverride
    protected void renderPortrait(final SpriteBatch sb) {
        final int x = this.cards().size();
        switch (x) {
            case 1: {
                TextureAtlas.AtlasRegion portrait0 = this.getEncodablePortrait(this.cards().get(0));
                if (portrait0 != null) {
                    portrait0 = new TextureAtlas.AtlasRegion(portrait0);
                    portrait0.setRegion(portrait0.getRegionX(), portrait0.getRegionY(), portrait0.getRegionWidth(), portrait0.getRegionHeight());
                    final float drawX = this.current_x - portrait0.packedWidth / 2.0f;
                    final float drawY = this.current_y - portrait0.packedHeight / 2.0f;
                    sb.draw(portrait0, drawX, drawY + 72.0f, portrait0.packedWidth / 2.0f, portrait0.packedHeight / 2.0f - 72.0f, (float)portrait0.packedWidth, (float)portrait0.packedHeight, this.drawScale * Settings.scale, this.drawScale * Settings.scale, this.angle);
                    break;
                }
                break;
            }
            case 2: {
                TextureAtlas.AtlasRegion portrait0 = this.getEncodablePortrait(this.cards().get(0));
                TextureAtlas.AtlasRegion portrait2 = this.getEncodablePortrait(this.cards().get(1));
                if (portrait0 != null && portrait2 != null) {
                    portrait0 = new TextureAtlas.AtlasRegion(portrait0);
                    portrait0.setRegion(portrait0.getRegionX(), portrait0.getRegionY(), portrait0.getRegionWidth() / 2, portrait0.getRegionHeight());
                    portrait2 = new TextureAtlas.AtlasRegion(portrait2);
                    portrait2.setRegion(portrait2.getRegionX() + portrait2.getRegionWidth() / 2, portrait2.getRegionY(), portrait2.getRegionWidth() / 2, portrait2.getRegionHeight());
                    float drawX = this.current_x - portrait0.packedWidth / 2.0f;
                    float drawY = this.current_y - portrait0.packedHeight / 2.0f;
                    sb.draw(portrait0, drawX, drawY + 72.0f, portrait0.packedWidth / 2.0f, portrait0.packedHeight / 2.0f - 72.0f, portrait0.packedWidth / 2.0f, (float)portrait0.packedHeight, this.drawScale * Settings.scale, this.drawScale * Settings.scale, this.angle);
                    drawX = this.current_x - portrait2.packedWidth / 2.0f;
                    drawY = this.current_y - portrait2.packedHeight / 2.0f;
                    sb.draw(portrait2, drawX + portrait2.packedWidth / 2.0f, drawY + 72.0f, 0.0f, portrait2.packedHeight / 2.0f - 72.0f, portrait2.packedWidth / 2.0f, (float)portrait2.packedHeight, this.drawScale * Settings.scale, this.drawScale * Settings.scale, this.angle);
                    break;
                }
                break;
            }
            case 3: {
                TextureAtlas.AtlasRegion portrait0 = this.getEncodablePortrait(this.cards().get(0));
                TextureAtlas.AtlasRegion portrait2 = this.getEncodablePortrait(this.cards().get(1));
                TextureAtlas.AtlasRegion portrait3 = this.getEncodablePortrait(this.cards().get(2));
                if (portrait0 != null && portrait2 != null) {
                    portrait0 = new TextureAtlas.AtlasRegion(portrait0);
                    portrait0.setRegion(portrait0.getRegionX(), portrait0.getRegionY(), portrait0.getRegionWidth() / 3, portrait0.getRegionHeight());
                    portrait2 = new TextureAtlas.AtlasRegion(portrait2);
                    portrait2.setRegion(portrait2.getRegionX() + portrait2.getRegionWidth() / 3, portrait2.getRegionY(), portrait2.getRegionWidth() / 3, portrait2.getRegionHeight());
                    portrait3 = new TextureAtlas.AtlasRegion(portrait3);
                    portrait3.setRegion(portrait3.getRegionX() + portrait3.getRegionWidth() / 3 * 2, portrait3.getRegionY(), portrait3.getRegionWidth() / 3, portrait3.getRegionHeight());
                    float drawX = this.current_x - portrait0.packedWidth / 2.0f;
                    float drawY = this.current_y - portrait0.packedHeight / 2.0f;
                    sb.draw(portrait0, drawX, drawY + 72.0f, portrait0.packedWidth / 2.0f, portrait0.packedHeight / 2.0f - 72.0f, portrait0.packedWidth / 3.0f, (float)portrait0.packedHeight, this.drawScale * Settings.scale, this.drawScale * Settings.scale, this.angle);
                    drawX = this.current_x - portrait2.packedWidth / 2.0f;
                    drawY = this.current_y - portrait2.packedHeight / 2.0f;
                    sb.draw(portrait2, drawX + portrait2.packedWidth / 3.0f, drawY + 72.0f, portrait2.packedWidth / 6.0f, portrait2.packedHeight / 2.0f - 72.0f, portrait2.packedWidth / 3.0f, (float)portrait2.packedHeight, this.drawScale * Settings.scale, this.drawScale * Settings.scale, this.angle);
                    drawX = this.current_x - portrait3.packedWidth / 2.0f;
                    drawY = this.current_y - portrait3.packedHeight / 2.0f;
                    sb.draw(portrait3, drawX + portrait3.packedWidth / 3.0f * 2.0f, drawY + 72.0f, -(portrait3.packedWidth / 6.0f), portrait3.packedHeight / 2.0f - 72.0f, portrait3.packedWidth / 3.0f, (float)portrait3.packedHeight, this.drawScale * Settings.scale, this.drawScale * Settings.scale, this.angle);
                    break;
                }
                break;
            }
            case 4: {
                TextureAtlas.AtlasRegion portrait0 = this.getEncodablePortrait(this.cards().get(0));
                TextureAtlas.AtlasRegion portrait2 = this.getEncodablePortrait(this.cards().get(1));
                TextureAtlas.AtlasRegion portrait3 = this.getEncodablePortrait(this.cards().get(2));
                TextureAtlas.AtlasRegion portrait4 = this.getEncodablePortrait(this.cards().get(3));
                if (portrait0 != null && portrait2 != null) {
                    portrait0 = new TextureAtlas.AtlasRegion(portrait0);
                    portrait0.setRegion(portrait0.getRegionX(), portrait0.getRegionY(), portrait0.getRegionWidth() / 2, portrait0.getRegionHeight() / 2);
                    portrait2 = new TextureAtlas.AtlasRegion(portrait2);
                    portrait2.setRegion(portrait2.getRegionX() + portrait2.getRegionWidth() / 2, portrait2.getRegionY(), portrait2.getRegionWidth() / 2, portrait2.getRegionHeight() / 2);
                    portrait3 = new TextureAtlas.AtlasRegion(portrait3);
                    portrait3.setRegion(portrait3.getRegionX(), portrait3.getRegionY() + portrait3.getRegionHeight() / 2, portrait3.getRegionWidth() / 2, portrait3.getRegionHeight() / 2);
                    portrait4 = new TextureAtlas.AtlasRegion(portrait4);
                    portrait4.setRegion(portrait4.getRegionX() + portrait4.getRegionWidth() / 2, portrait4.getRegionY() + portrait4.getRegionHeight() / 2, portrait4.getRegionWidth() / 2, portrait4.getRegionHeight() / 2);
                    float drawX = this.current_x - portrait0.packedWidth / 2.0f;
                    float drawY = this.current_y + 72.0f;
                    sb.draw(portrait0, drawX, drawY, portrait0.packedWidth / 2.0f, -72.0f, portrait0.packedWidth / 2.0f, portrait0.packedHeight / 2.0f, this.drawScale * Settings.scale, this.drawScale * Settings.scale, this.angle);
                    drawX = this.current_x - portrait2.packedWidth / 2.0f;
                    drawY = this.current_y + 72.0f;
                    sb.draw(portrait2, drawX + portrait2.packedWidth / 2.0f, drawY, 0.0f, -72.0f, portrait2.packedWidth / 2.0f, portrait2.packedHeight / 2.0f, this.drawScale * Settings.scale, this.drawScale * Settings.scale, this.angle);
                    drawX = this.current_x - portrait3.packedWidth / 2.0f;
                    drawY = this.current_y - portrait3.packedHeight / 2.0f + 72.0f;
                    sb.draw(portrait3, drawX, drawY, portrait3.packedWidth / 2.0f, portrait3.packedHeight / 2.0f - 72.0f, portrait3.packedWidth / 2.0f, portrait3.packedHeight / 2.0f, this.drawScale * Settings.scale, this.drawScale * Settings.scale, this.angle);
                    drawX = this.current_x - portrait4.packedWidth / 2.0f;
                    drawY = this.current_y - portrait4.packedHeight / 2.0f + 72.0f;
                    sb.draw(portrait4, drawX + portrait4.packedWidth / 2.0f, drawY, 0.0f, portrait4.packedHeight / 2.0f - 72.0f, portrait4.packedWidth / 2.0f, portrait4.packedHeight / 2.0f, this.drawScale * Settings.scale, this.drawScale * Settings.scale, this.angle);
                    break;
                }
                break;
            }
        }
    }
}
