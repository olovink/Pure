package cn.nukkit.block;

import cn.nukkit.item.Item;
import cn.nukkit.item.ItemEmerald;
import cn.nukkit.item.ItemTool;
import cn.nukkit.item.enchantment.Enchantment;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created on 2015/12/1 by xtypr.
 * Package cn.nukkit.block in project Nukkit .
 */
public class BlockOreEmerald extends BlockSolid {

    public BlockOreEmerald() {
        this(0);
    }

    public BlockOreEmerald(int meta) {
        super(meta);
    }

    @Override
    public String getName() {
        return "Emerald Ore";
    }

    @Override
    public int getId() {
        return EMERALD_ORE;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }

    @Override
    public double getHardness() {
        return 3;
    }

    @Override
    public double getResistance() {
        return 15;
    }

    @Override
    public Item[] getDrops(Item item) {
        if (item.isPickaxe() && item.getTier() >= ItemTool.TIER_IRON) {
            int count = 1;
            Enchantment fortune = item.getEnchantment(Enchantment.ID_FORTUNE_DIGGING);
            if (fortune != null && fortune.getLevel() >= 1) {
                int i = ThreadLocalRandom.current().nextInt(fortune.getLevel() + 2) - 1;

                if (i < 0) {
                    i = 0;
                }

                count = i + 1;
            }

            return new Item[]{
                    new ItemEmerald(0, count)
            };
        } else {
            return new Item[0];
        }
}

    @Override
    public int getDropExp() {
        return ThreadLocalRandom.current().nextInt(7);
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }
}