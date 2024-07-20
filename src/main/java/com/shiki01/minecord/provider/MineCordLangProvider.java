package com.shiki01.minecord.provider;

import com.shiki01.minecord.MineCord;
import com.shiki01.minecord.MineCordBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public abstract class MineCordLangProvider extends LanguageProvider {
    public MineCordLangProvider(DataGenerator gen, String locale) {
        super(gen, MineCord.MOD_ID, locale);
    }

    public static class MineCordLangEn extends MineCordLangProvider {
        public MineCordLangEn(DataGenerator gen) {
            super(gen, "en_us");
        }

        @Override
        protected void addTranslations() {
            this.add(MineCordBlocks.MINE_CORD.get(), "Mine Cord");
            this.add("itemGroup.minecord", "Mine Cord");
        }
    }

    public static class MineCordLangJa extends MineCordLangProvider {
        public MineCordLangJa(DataGenerator gen) {
            super(gen, "ja_jp");
        }

        @Override
        protected void addTranslations() {
            this.add(MineCordBlocks.MINE_CORD.get(), "マインコード");
            this.add("itemGroup.minecord", "マインコード");
        }
    }
}