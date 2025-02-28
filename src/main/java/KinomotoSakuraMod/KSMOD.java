package KinomotoSakuraMod;

import KinomotoSakuraMod.Cards.ClowCard.*;
import KinomotoSakuraMod.Cards.SakuraCard.*;
import KinomotoSakuraMod.Cards.SpellCard.*;
import KinomotoSakuraMod.Characters.KinomotoSakura;
import KinomotoSakuraMod.Patches.KSMOD_CustomCardColor;
import KinomotoSakuraMod.Patches.KSMOD_CustomCharacter;
import KinomotoSakuraMod.Patches.KSMOD_CustomKeywords;
import KinomotoSakuraMod.Potions.KSMOD_MagickBottle;
import KinomotoSakuraMod.Relics.*;
import KinomotoSakuraMod.Utility.KSMOD_ImageConst;
import KinomotoSakuraMod.Utility.KSMOD_Utility;
import basemod.BaseMod;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

@SpireInitializer
public class KSMOD implements ISubscriber, PostInitializeSubscriber, EditCharactersSubscriber, EditRelicsSubscriber, EditCardsSubscriber, EditStringsSubscriber, EditKeywordsSubscriber, OnStartBattleSubscriber
{
    // CardColor卡片颜色，卡片总览中的tab按钮颜色
    public static final Color colorClowCard = CardHelper.getColor(255f, 152f, 74f);
    public static final Color colorSakuraCard = CardHelper.getColor(255f, 192f, 203f);
    public static final Color colorSpellCard = CardHelper.getColor(253f, 220f, 106f);
    private static String localizationPath = null;

    public KSMOD()
    {
        BaseMod.subscribe(this);
        BaseMod.addColor(KSMOD_CustomCardColor.CLOWCARD_COLOR, colorClowCard, colorClowCard, colorClowCard, colorClowCard, colorClowCard, colorClowCard, colorClowCard, KSMOD_ImageConst.CLOWCARD_BG_PATH, KSMOD_ImageConst.CLOWCARD_BG_PATH, KSMOD_ImageConst.CLOWCARD_BG_PATH, KSMOD_ImageConst.ORB_CLOWCARD_PATH, KSMOD_ImageConst.CLOWCARD_BG_LARGE_PATH, KSMOD_ImageConst.CLOWCARD_BG_LARGE_PATH, KSMOD_ImageConst.CLOWCARD_BG_LARGE_PATH, KSMOD_ImageConst.ORB_CLOWCARD_LARGE_PATH);
        BaseMod.addColor(KSMOD_CustomCardColor.SAKURACARD_COLOR, colorSakuraCard, colorSakuraCard, colorSakuraCard, colorSakuraCard, colorSakuraCard, colorSakuraCard, colorSakuraCard, KSMOD_ImageConst.SAKURACARD_BG_PATH, KSMOD_ImageConst.SAKURACARD_BG_PATH, KSMOD_ImageConst.SAKURACARD_BG_PATH, KSMOD_ImageConst.ORB_SAKURACARD_PATH, KSMOD_ImageConst.SAKURACARD_BG_LARGE_PATH, KSMOD_ImageConst.SAKURACARD_BG_LARGE_PATH, KSMOD_ImageConst.SAKURACARD_BG_LARGE_PATH, KSMOD_ImageConst.ORB_SAKURACARD_LARGE_PATH);
        BaseMod.addColor(KSMOD_CustomCardColor.SPELL_COLOR, colorSpellCard, colorSpellCard, colorSpellCard, colorSpellCard, colorSpellCard, colorSpellCard, colorSpellCard, KSMOD_ImageConst.SPELLCARD_BG_PATH, KSMOD_ImageConst.SPELLCARD_BG_PATH, KSMOD_ImageConst.SPELLCARD_BG_PATH, KSMOD_ImageConst.ORB_SPELLCARD_PATH, KSMOD_ImageConst.SPELLCARD_BG_LARGE_PATH, KSMOD_ImageConst.SPELLCARD_BG_LARGE_PATH, KSMOD_ImageConst.SPELLCARD_BG_LARGE_PATH, KSMOD_ImageConst.ORB_SPELLCARD_LARGE_PATH);
    }

    public static void initialize()
    {
        KSMOD_Utility.Logger.info("开始初始化 KSMOD");

        new KSMOD();

        KSMOD_Utility.Logger.info("完成初始化 KSMOD");
    }

    @SuppressWarnings("deprecation")
    @Override
    public void receivePostInitialize()
    {
        receiveEditPotions();
        receiveEditEvents();
        receiveEditMonsters();
        // Texture badgeTexture = new Texture("mod图标路径");
        // ModPanel settingsPanel = new ModPanel();
        // settingsPanel.addLabel("config里的mod描叙", 400.0f, 700.0f, (me) -> {});
        // BaseMod.registerModBadge(badgeTexture, MODNAME, AUTHOR, DESCRIPTION, settingsPanel);
        //
        // Settings.isDailyRun = false;
        // Settings.isTrial = false;
        // Settings.isDemo = false;
    }

    @Override
    public void receiveEditCharacters()
    {
        KSMOD_Utility.Logger.info("开始编辑角色");

        BaseMod.addCharacter(new KinomotoSakura(), KSMOD_ImageConst.SELECT_BUTTON_PATH, KSMOD_ImageConst.PORTRAIT_PATH, KSMOD_CustomCharacter.KINOMOTOSAKURA);

        KSMOD_Utility.Logger.info("结束编辑角色");
    }

    @Override
    public void receiveEditRelics()
    {
        KSMOD_Utility.Logger.info("开始编辑遗物");

        for (AbstractRelic relic : GetRelicList())
        {
            BaseMod.addRelicToCustomPool(relic, KSMOD_CustomCardColor.CLOWCARD_COLOR);
            KSMOD_Utility.Logger.info("Loading relic : " + relic.name);
        }

        KSMOD_Utility.Logger.info("结束编辑遗物");
    }

    public static ArrayList<AbstractRelic> GetRelicList()
    {
        ArrayList<AbstractRelic> relicList = new ArrayList<AbstractRelic>();

        relicList.add(new KSMOD_SealedWand());
        relicList.add(new KSMOD_SealedBook());
        relicList.add(new KSMOD_StarWand());
        relicList.add(new KSMOD_UltimateWand());
        relicList.add(new KSMOD_DarknessWand());
        relicList.add(new KSMOD_Cerberus());
        relicList.add(new KSMOD_Yue());
        relicList.add(new KSMOD_TaoistSuit());
        relicList.add(new KSMOD_SwordJade());
        relicList.add(new KSMOD_TeddyBear());
        relicList.add(new KSMOD_MoonBell());
        relicList.add(new KSMOD_YukitosBentoBox());
        relicList.add(new KSMOD_RollerSkates());
        relicList.add(new KSMOD_Compass());
        relicList.add(new KSMOD_GemBrooch());
        relicList.add(new KSMOD_TouyasBicycle());
        relicList.add(new KSMOD_TomoyosHeart());

        return relicList;
    }

    @Override
    public void receiveEditCards()
    {
        KSMOD_Utility.Logger.info("开始编辑卡牌");

        ArrayList<AbstractCard> unlockedCardList = new ArrayList<>();
        unlockedCardList.addAll(GetClowCardList());
        unlockedCardList.addAll(GetSpellCardList());
        unlockedCardList.addAll(GetSakuraCardList());
        for (AbstractCard card : unlockedCardList)
        {
            BaseMod.addCard(card);
            UnlockTracker.unlockCard(card.cardID);
            KSMOD_Utility.Logger.info("Loading Unlocked Card : " + card.name);
        }

        ArrayList<AbstractCard> lockedCardList = new ArrayList<>();
        // lockedCardList.addAll(GetSakuraCardList());
        for (AbstractCard card : lockedCardList)
        {
            BaseMod.addCard(card);
            KSMOD_Utility.Logger.info("Loading Locked Card : " + card.name);
        }

        KSMOD_Utility.Logger.info("结束编辑卡牌");
    }

    public static ArrayList<AbstractCard> GetClowCardList()
    {
        ArrayList<AbstractCard> cardList = new ArrayList<AbstractCard>();

        cardList.add(new ClowCardTheArrow());
        cardList.add(new ClowCardTheBig());
        cardList.add(new ClowCardTheBubbles());
        cardList.add(new ClowCardTheChange());
        cardList.add(new ClowCardTheCloud());
        cardList.add(new ClowCardTheCreate());
        cardList.add(new ClowCardTheDark());
        cardList.add(new ClowCardTheDash());
        cardList.add(new ClowCardTheDream());
        cardList.add(new ClowCardTheEarthy());
        cardList.add(new ClowCardTheErase());
        cardList.add(new ClowCardTheFight());
        cardList.add(new ClowCardTheFirey());
        cardList.add(new ClowCardTheFloat());
        cardList.add(new ClowCardTheFlower());
        cardList.add(new ClowCardTheFly());
        cardList.add(new ClowCardTheFreeze());
        cardList.add(new ClowCardTheGlow());
        cardList.add(new ClowCardTheIllusion());
        cardList.add(new ClowCardTheJump());
        cardList.add(new ClowCardTheLibra());
        cardList.add(new ClowCardTheLight());
        cardList.add(new ClowCardTheLittle());
        cardList.add(new ClowCardTheLock());
        cardList.add(new ClowCardTheLoop());
        cardList.add(new ClowCardTheMaze());
        cardList.add(new ClowCardTheMirror());
        cardList.add(new ClowCardTheMist());
        cardList.add(new ClowCardTheMove());
        cardList.add(new ClowCardThePower());
        cardList.add(new ClowCardTheRain());
        cardList.add(new ClowCardTheReturn());
        cardList.add(new ClowCardTheSand());
        cardList.add(new ClowCardTheShadow());
        cardList.add(new ClowCardTheShield());
        cardList.add(new ClowCardTheShot());
        cardList.add(new ClowCardTheSilent());
        cardList.add(new ClowCardTheSleep());
        cardList.add(new ClowCardTheSnow());
        cardList.add(new ClowCardTheSong());
        cardList.add(new ClowCardTheStorm());
        cardList.add(new ClowCardTheSweet());
        cardList.add(new ClowCardTheSword());
        cardList.add(new ClowCardTheThrough());
        cardList.add(new ClowCardTheThunder());
        cardList.add(new ClowCardTheTime());
        cardList.add(new ClowCardTheTwin());
        cardList.add(new ClowCardTheVoice());
        cardList.add(new ClowCardTheWatery());
        cardList.add(new ClowCardTheWave());
        cardList.add(new ClowCardTheWindy());
        cardList.add(new ClowCardTheWood());

        return cardList;
    }

    public static ArrayList<AbstractCard> GetSpellCardList()
    {
        ArrayList<AbstractCard> cardList = new ArrayList<AbstractCard>();

        cardList.add(new SpellCardRelease());
        cardList.add(new SpellCardSeal());
        cardList.add(new SpellCardTurn());
        cardList.add(new SpellCardEmptySpell());
        cardList.add(new SpellCardHuoShen());
        cardList.add(new SpellCardLeiDi());
        cardList.add(new SpellCardFengHua());
        cardList.add(new SpellCardShuiLong());

        // cardList.add(new TestCard());

        return cardList;
    }

    public static ArrayList<AbstractCard> GetSakuraCardList()
    {
        ArrayList<AbstractCard> cardList = new ArrayList<AbstractCard>();

        cardList.add(new SakuraCardTheArrow());
        cardList.add(new SakuraCardTheBig());
        cardList.add(new SakuraCardTheBubbles());
        cardList.add(new SakuraCardTheChange());
        cardList.add(new SakuraCardTheCloud());
        cardList.add(new SakuraCardTheCreate());
        cardList.add(new SakuraCardTheDark());
        cardList.add(new SakuraCardTheDash());
        cardList.add(new SakuraCardTheDream());
        cardList.add(new SakuraCardTheEarthy());
        cardList.add(new SakuraCardTheErase());
        cardList.add(new SakuraCardTheFight());
        cardList.add(new SakuraCardTheFirey());
        cardList.add(new SakuraCardTheFloat());
        cardList.add(new SakuraCardTheFlower());
        cardList.add(new SakuraCardTheFly());
        cardList.add(new SakuraCardTheFreeze());
        cardList.add(new SakuraCardTheGlow());
        cardList.add(new SakuraCardTheIllusion());
        cardList.add(new SakuraCardTheJump());
        cardList.add(new SakuraCardTheLibra());
        cardList.add(new SakuraCardTheLight());
        cardList.add(new SakuraCardTheLittle());
        cardList.add(new SakuraCardTheLock());
        cardList.add(new SakuraCardTheLoop());
        cardList.add(new SakuraCardTheMaze());
        cardList.add(new SakuraCardTheMirror());
        cardList.add(new SakuraCardTheMist());
        cardList.add(new SakuraCardTheMove());
        cardList.add(new SakuraCardThePower());
        cardList.add(new SakuraCardTheRain());
        cardList.add(new SakuraCardTheReturn());
        cardList.add(new SakuraCardTheSand());
        cardList.add(new SakuraCardTheShadow());
        cardList.add(new SakuraCardTheShield());
        cardList.add(new SakuraCardTheShot());
        cardList.add(new SakuraCardTheSilent());
        cardList.add(new SakuraCardTheSleep());
        cardList.add(new SakuraCardTheSnow());
        cardList.add(new SakuraCardTheSong());
        cardList.add(new SakuraCardTheStorm());
        cardList.add(new SakuraCardTheSweet());
        cardList.add(new SakuraCardTheSword());
        cardList.add(new SakuraCardTheThrough());
        cardList.add(new SakuraCardTheThunder());
        cardList.add(new SakuraCardTheTime());
        cardList.add(new SakuraCardTheTwin());
        cardList.add(new SakuraCardTheVoice());
        cardList.add(new SakuraCardTheWatery());
        cardList.add(new SakuraCardTheWave());
        cardList.add(new SakuraCardTheWindy());
        cardList.add(new SakuraCardTheWood());

        return cardList;
    }

    public void receiveEditPotions()
    {
        KSMOD_Utility.Logger.info("开始编辑药水");

        BaseMod.addPotion(KSMOD_MagickBottle.class, Color.NAVY.cpy(), Color.BLUE.cpy(), Color.SKY.cpy(), KSMOD_MagickBottle.POTION_ID, KSMOD_CustomCharacter.KINOMOTOSAKURA);

        KSMOD_Utility.Logger.info("结束编辑药水");
    }

    public void receiveEditEvents()
    {
        KSMOD_Utility.Logger.info("开始编辑事件");

        KSMOD_Utility.Logger.info("结束编辑事件");
    }

    public void receiveEditMonsters()
    {
        KSMOD_Utility.Logger.info("开始编辑怪物");

        KSMOD_Utility.Logger.info("结束编辑怪物");
    }

    public static String GetLocalizationPath()
    {
        if (localizationPath == null)
        {
            localizationPath = "localization/";
            switch (Settings.language)
            {
                case ZHS:
                    KSMOD_Utility.Logger.info("language == zhs");
                    localizationPath = localizationPath + "zhs/";
                    break;
                default:
                    KSMOD_Utility.Logger.info("language == eng");
                    localizationPath = localizationPath + "eng/";
                    break;
            }
        }
        return localizationPath;
    }

    @Override
    public void receiveEditStrings()
    {
        KSMOD_Utility.Logger.info("开始编辑本地化文本");

        String card = GetLocalizationPath() + "sakura_card.json";
        String cardStrings = Gdx.files.internal(card).readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(CardStrings.class, cardStrings);

        String character = GetLocalizationPath() + "sakura_character.json";
        String charStrings = Gdx.files.internal(character).readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(CharacterStrings.class, charStrings);

        String power = GetLocalizationPath() + "sakura_power.json";
        String powerStrings = Gdx.files.internal(power).readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(PowerStrings.class, powerStrings);

        String relic = GetLocalizationPath() + "sakura_relic.json";
        String relicStrings = Gdx.files.internal(relic).readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(RelicStrings.class, relicStrings);

        String ui = GetLocalizationPath() + "sakura_ui.json";
        String uiStrings = Gdx.files.internal(ui).readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(UIStrings.class, uiStrings);

        String potion = GetLocalizationPath() + "sakura_potion.json";
        String potionStrings = Gdx.files.internal(potion).readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(PotionStrings.class, potionStrings);

        KSMOD_Utility.Logger.info("结束编辑本地化文本");
    }

    @Override
    public void receiveEditKeywords()
    {
        KSMOD_Utility.Logger.info("开始编辑关键字");

        String path = "localization/";
        switch (Settings.language)
        {
            case ZHS:
                KSMOD_Utility.Logger.info("language == zhs");
                path += "zhs/";
                break;
            default:
                KSMOD_Utility.Logger.info("language == eng");
                path += "eng/";
                break;
        }

        path += "sakura_keyword.json";
        Gson gson = new Gson();
        String json = Gdx.files.internal(path).readString(String.valueOf(StandardCharsets.UTF_8));
        KSMOD_CustomKeywords keywords = gson.fromJson(json, KSMOD_CustomKeywords.class);
        Keyword[] keywordList = keywords.keywords;

        for (int i = 0; i < keywordList.length; ++i)
        {
            Keyword key = keywordList[i];
            KSMOD_Utility.Logger.info("加载关键字：" + key.NAMES[0]);
            BaseMod.addKeyword(key.NAMES, key.DESCRIPTION);
        }

        KSMOD_Utility.Logger.info("结束编辑关键字");
    }

    @Override
    public void receiveOnBattleStart(AbstractRoom abstractRoom)
    {
        KSMOD_Utility.Logger.info("common relic pool");
        ShowRelicList(AbstractDungeon.commonRelicPool);
        KSMOD_Utility.Logger.info("uncommon relic pool");
        ShowRelicList(AbstractDungeon.uncommonRelicPool);
        KSMOD_Utility.Logger.info("rare relic pool");
        ShowRelicList(AbstractDungeon.rareRelicPool);
        KSMOD_Utility.Logger.info("boss relic pool");
        ShowRelicList(AbstractDungeon.bossRelicPool);
        KSMOD_Utility.Logger.info("shop relic pool");
        ShowRelicList(AbstractDungeon.shopRelicPool);
    }

    public void ShowRelicList(ArrayList<String> relics)
    {
        for (int i = 0; i < relics.size(); i++)
        {
            KSMOD_Utility.Logger.info(i + ": " + relics.get(i) + (istargetrelic(relics.get(i)) ? "<=========" : ""));
        }
    }

    public boolean istargetrelic(String relic)
    {
        if (relic.contains(KSMOD_StarWand.RELIC_ID))
        {
            return true;
        }
        else if (relic.contains(KSMOD_Cerberus.RELIC_ID))
        {
            return true;
        }
        else if (relic.contains(KSMOD_Yue.RELIC_ID))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
