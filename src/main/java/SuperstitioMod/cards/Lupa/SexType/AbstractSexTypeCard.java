package SuperstitioMod.cards.Lupa.SexType;

import SuperstitioMod.cards.Lupa.AbstractLupaCard;
import SuperstitioMod.cards.Lupa.CardStringsWithFlavor;
import SuperstitioMod.characters.Lupa;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.UIStrings;

public abstract class AbstractSexTypeCard extends AbstractLupaCard {


    public String betaArtPath;
    private static float functionPreviewCardScale;
    private static float functionPreviewCardY;
    private static float functionPreviewCardX;
    public int auto;
    public int baseAuto;
    public boolean upgradedAuto;
    public boolean isAutoModified;
    public boolean isHoveredInSequence;
    public int position;
    public boolean doSpecialCompileStuff;
    protected String DESCRIPTION;
    protected String UPGRADE_DESCRIPTION;
    protected String[] EXTENDED_DESCRIPTION;
    private AbstractCard functionPreviewCard;
    public static final UIStrings masterUI;
    private final static float DESC_LINE_WIDTH = 418.0f * Settings.scale;

    //调用父类的构造方法，传参为super(卡牌ID，卡牌名称，图片地址，能量花费，卡牌描述，卡牌类型，卡牌颜色，卡牌稀有度，卡牌目标)
    protected CardStringsWithFlavor cardStrings;

    /**
     * 普通的方法
     *
     * @param id         卡牌ID
     * @param cardType   卡牌类型
     * @param cost       卡牌消耗
     * @param cardRarity 卡牌稀有度
     * @param cardTarget 卡牌目标
     */
    public AbstractSexTypeCard(String id, CardType cardType, int cost, CardRarity cardRarity, CardTarget cardTarget) {
        this(id, cardType, cost, cardRarity, cardTarget, CardTypeToString(cardType));
    }

    /**
     * 当需要自定义卡牌存储的二级目录名时
     */
    public AbstractSexTypeCard(String id, CardType cardType, int cost, CardRarity cardRarity, CardTarget cardTarget, String customCardType) {
        this(id, cardType, cost, cardRarity, cardTarget, Lupa.Enums.LUPA_CARD, customCardType);
    }

    public AbstractSexTypeCard(String id, CardType cardType, int cost, CardRarity cardRarity, CardTarget cardTarget, CardColor cardColor) {
        this(id, cardType, cost, cardRarity, cardTarget, cardColor, CardTypeToString(cardType));
    }

    public AbstractSexTypeCard(String id, CardType cardType, int cost, CardRarity cardRarity, CardTarget cardTarget, CardColor cardColor,
                               String customCardType) {
        super(id, cardType, cost, cardRarity, cardTarget, cardColor, customCardType);
        this.isHoveredInSequence = false;
        this.position = -1;
        this.doSpecialCompileStuff = true;
        this.initializeTitle();
        this.initializeDescription();
    }


    public void onInput() {
    }

    public void onCompilePreCardEffectEmbed(final boolean forGameplay) {
    }

    public void onCompileFirst(final AbstractCard function, final boolean forGameplay) {
    }

    public void onCompile(final AbstractCard function, final boolean forGameplay) {
    }

    public void onCompileLast(final AbstractCard function, final boolean forGameplay) {
    }


    static {
        AbstractSexTypeCard.functionPreviewCardScale = 0.9f;
        AbstractSexTypeCard.functionPreviewCardY = Settings.HEIGHT * 0.45f;
        AbstractSexTypeCard.functionPreviewCardX = Settings.WIDTH * 0.1f;
        masterUI = CardCrawlGame.languagePack.getUIString("bronze:MiscStrings");
    }
}
