package com.InfinityRaider.AgriCraft.farming;

import com.InfinityRaider.AgriCraft.reference.Constants;
import com.InfinityRaider.AgriCraft.utility.SeedHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Random;

/** main class used by TileEntityCrop to perform its functionality, only make one object of this per seed object and register it using CropPlantHandler.registerPlant(CropPlant plant) */
public abstract class CropPlant {
    public final int getGrowthRate() {
        switch(getTier()) {
            case 1: return Constants.growthTier1;
            case 2: return Constants.growthTier2;
            case 3: return Constants.growthTier3;
            case 4: return Constants.growthTier4;
            case 5: return Constants.growthTier5;
            default: return 0;
        }
    }

    /** This is called to get the actual tier of a seed */
    public final int getTier() {
        int seedTierOverride = SeedHelper.getSeedTierOverride(getSeed());
        if(seedTierOverride>0) {
            return seedTierOverride;
        }
        return tier();
    }

    /** Gets the tier of this plant, can be overridden trough the configs */
    public abstract int tier();

    /** Gets a stack of the seed for this plant */
    public abstract ItemStack getSeed();

    /** Gets an arraylist of all possible fruit drops from this plant */
    public abstract ArrayList<ItemStack> getAllFruits();

    /** Returns a random fruit for this plant */
    public abstract ItemStack getRandomFruit(Random rand);

    /** Returns an ArrayList with amount of random fruit stacks for this plant */
    public abstract ArrayList<ItemStack> getFruitsOnHarvest(int gain, Random rand);

    /** Gets right before a harvest attempt, return false to prevent further processing */
    public boolean onHarvest(World world, int x, int y, int z) {
        return true;
    }

    /** Allow this plant to be bonemealed or not */
    public abstract boolean canBonemeal();

    /** When a growth thick is allowed for this plant, return true to re-render the crop clientside */
    public abstract boolean onAllowedGrowthTick(World world, int x, int y, int z, int oldGrowthStage);

    /** Checks if the plant can grow on this position */
    public abstract boolean isFertile(World world, int x, int y, int z);

    /** Gets the icon for the plant, growthstage goes from 0 to 7 (both inclusive, 0 is sprout and 7 is mature) */
    @SideOnly(Side.CLIENT)
    public abstract IIcon getPlantIcon(int growthStage);

    /** Determines how the plant is rendered, return false to render as wheat (#), true to render as a flower (X) */
    @SideOnly(Side.CLIENT)
    public abstract boolean renderAsFlower();

    /** Gets some information about the plant for the journal */
    @SideOnly(Side.CLIENT)
    public abstract String getInformation();
}
