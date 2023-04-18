package com.purityvanilla.purityarenas.util;

import net.kyori.adventure.text.Component;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

public class BannerHelper {

    /*
    Pattern orders for each letter: https://www.gamergeeks.net/apps/minecraft/banners/letters#letter-f
    Pattern codes to names: https://minecraft.fandom.com/wiki/Banner/Patterns
    Pattern types: https://jd.papermc.io/paper/1.18/org/bukkit/block/banner/PatternType.html
     */
    public static ItemStack LetterBanner(char letter, Material bannerMaterial, DyeColor baseColour, DyeColor letterColour) {
        ItemStack banner = new ItemStack(bannerMaterial);
        BannerMeta bannerMeta = (BannerMeta) banner.getItemMeta();
        bannerMeta.displayName(Component.text(""));

        switch (letter) {
            case 'A':
                bannerMeta.addPattern(new Pattern(letterColour, PatternType.STRIPE_RIGHT));
                bannerMeta.addPattern(new Pattern(letterColour, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(letterColour, PatternType.STRIPE_MIDDLE));
                bannerMeta.addPattern(new Pattern(letterColour, PatternType.STRIPE_TOP));
                bannerMeta.addPattern(new Pattern(baseColour, PatternType.BORDER));
                break;

            case 'E':
                bannerMeta.addPattern(new Pattern(letterColour, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(letterColour, PatternType.STRIPE_TOP));
                bannerMeta.addPattern(new Pattern(letterColour, PatternType.STRIPE_MIDDLE));
                bannerMeta.addPattern(new Pattern(letterColour, PatternType.STRIPE_BOTTOM));
                bannerMeta.addPattern(new Pattern(baseColour, PatternType.BORDER));
                break;

            case 'F':
                bannerMeta.addPattern(new Pattern(letterColour, PatternType.STRIPE_MIDDLE));
                bannerMeta.addPattern(new Pattern(baseColour, PatternType.STRIPE_RIGHT));
                bannerMeta.addPattern(new Pattern(letterColour, PatternType.STRIPE_TOP));
                bannerMeta.addPattern(new Pattern(letterColour, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(baseColour, PatternType.BORDER));
                break;

            case 'H':
                bannerMeta.addPattern(new Pattern(letterColour, PatternType.BASE));
                bannerMeta.addPattern(new Pattern(baseColour, PatternType.STRIPE_TOP));
                bannerMeta.addPattern(new Pattern(baseColour, PatternType.STRIPE_BOTTOM));
                bannerMeta.addPattern(new Pattern(letterColour, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(letterColour, PatternType.STRIPE_RIGHT));
                bannerMeta.addPattern(new Pattern(baseColour, PatternType.BORDER));
                break;

            case 'O':
                bannerMeta.addPattern(new Pattern(letterColour, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(letterColour, PatternType.STRIPE_RIGHT));
                bannerMeta.addPattern(new Pattern(letterColour, PatternType.STRIPE_BOTTOM));
                bannerMeta.addPattern(new Pattern(letterColour, PatternType.STRIPE_TOP));
                bannerMeta.addPattern(new Pattern(baseColour, PatternType.BORDER));
                break;

            case 'R':
                bannerMeta.addPattern(new Pattern(letterColour, PatternType.HALF_HORIZONTAL));
                bannerMeta.addPattern(new Pattern(baseColour, PatternType.STRIPE_CENTER));
                bannerMeta.addPattern(new Pattern(letterColour, PatternType.STRIPE_TOP));
                bannerMeta.addPattern(new Pattern(letterColour, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(letterColour, PatternType.STRIPE_DOWNRIGHT));
                bannerMeta.addPattern(new Pattern(baseColour, PatternType.BORDER));
                break;

            case 'T':
                bannerMeta.addPattern(new Pattern(letterColour, PatternType.STRIPE_TOP));
                bannerMeta.addPattern(new Pattern(letterColour, PatternType.STRIPE_CENTER));
                bannerMeta.addPattern(new Pattern(baseColour, PatternType.BORDER));
                break;

        }

        banner.setItemMeta(bannerMeta);
        banner.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ITEM_SPECIFICS});

        return banner;
    }
}
