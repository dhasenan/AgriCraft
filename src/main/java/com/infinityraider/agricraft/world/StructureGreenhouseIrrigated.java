package com.infinityraider.agricraft.world;

import com.infinityraider.agricraft.config.AgriCraftConfig;
import com.infinityraider.agricraft.tiles.irrigation.TileEntityChannel;
import com.infinityraider.agricraft.tiles.irrigation.TileEntityTank;
import com.infinityraider.agricraft.utility.AgriForgeDirection;
import com.agricraft.agricore.core.AgriCore;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;

import java.util.List;
import java.util.Random;
import com.infinityraider.agricraft.api.plant.IAgriPlant;
import com.infinityraider.agricraft.apiimpl.PlantRegistry;
public class StructureGreenhouseIrrigated extends StructureGreenhouse {
    //structure dimensions
    private static final int xSize = 17;
    private static final int ySize = 10;
    private static final int zSize = 16;
    //helper fields
    private int averageGroundLevel = -1;

    @SuppressWarnings("unused")
    public StructureGreenhouseIrrigated() {}

    public StructureGreenhouseIrrigated(StructureVillagePieces.Start villagePiece, int nr, Random rand, StructureBoundingBox structureBoundingBox, EnumFacing coordBaseMode) {
        super(villagePiece, nr, rand, structureBoundingBox, coordBaseMode);
    }

    //public method to build the component
    public static StructureGreenhouseIrrigated buildComponent(StructureVillagePieces.Start villagePiece, List<StructureComponent> pieces, Random random, int p1, int p2, int p3, EnumFacing side, int p5) {
        StructureBoundingBox boundingBox = StructureBoundingBox.getComponentToAddBoundingBox(p1, p2, p3, 0, 0, 0, xSize, ySize, zSize, side);
        return (canVillageGoDeeper(boundingBox)) && (StructureComponent.findIntersecting(pieces, boundingBox) == null)?new StructureGreenhouseIrrigated(villagePiece, p5, random, boundingBox,side) : null;
    }

    //structure generation code
    @Override
    public boolean addComponentParts(World world, Random rand, StructureBoundingBox boundingBox) {
        //level off ground
        if (this.averageGroundLevel < 0) {
            this.averageGroundLevel = getAverageGroundLevel(world, boundingBox);
            if (this.averageGroundLevel < 0) {
                return true;
            }
            this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.maxY + 7, 0);
        }
        //cobblestone.getDefaultState() base
        this.fillWithBlocks(world, boundingBox, 0, 0, 0, xSize-1, 0, zSize-1, Blocks.COBBLESTONE.getDefaultState(), Blocks.COBBLESTONE.getDefaultState(), false);   //args: (world, boundingBox, minX, minY, MinZ, maxX, maxY, maxZ, placeBlock, replaceBlock, doReplace)
        //ring of gravel.getDefaultState()
        this.fillWithBlocks(world, boundingBox, 0, 1, 0, xSize-1, 1, 0, Blocks.GRAVEL.getDefaultState(), Blocks.GRAVEL.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 0, 1, 0, 0, 1, zSize-1, Blocks.GRAVEL.getDefaultState(), Blocks.GRAVEL.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 0, 1, zSize-1, xSize-1, 1, zSize-1, Blocks.GRAVEL.getDefaultState(), Blocks.GRAVEL.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, xSize-1, 1, 0, xSize-1, 1, zSize-1, Blocks.GRAVEL.getDefaultState(), Blocks.GRAVEL.getDefaultState(), false);
        //grass.getDefaultState() patch
        this.fillWithBlocks(world, boundingBox, 1, 1, 1, 9, 1, 5, Blocks.GRASS.getDefaultState(), Blocks.GRASS.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 10, 1, 1, 13, 1, 2, Blocks.GRASS.getDefaultState(), Blocks.GRASS.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 14, 1, 1, 15, 1, 5, Blocks.GRASS.getDefaultState(), Blocks.GRASS.getDefaultState(), false);
        //cobble foundations
        this.fillWithBlocks(world, boundingBox, 1, 1, 6, 1, 1, 9, Blocks.COBBLESTONE.getDefaultState(), Blocks.COBBLESTONE.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 1, 1, 11, 1, 1, 14, Blocks.COBBLESTONE.getDefaultState(), Blocks.COBBLESTONE.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 15, 1, 6, 15, 1, 9, Blocks.COBBLESTONE.getDefaultState(), Blocks.COBBLESTONE.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 15, 1, 11, 15, 1, 14, Blocks.COBBLESTONE.getDefaultState(), Blocks.COBBLESTONE.getDefaultState(), false);
        this.setBlockState(world, Blocks.COBBLESTONE.getDefaultState(), 2, 1, 6, boundingBox);
        this.setBlockState(world, Blocks.COBBLESTONE.getDefaultState(), 8, 1, 6, boundingBox);
        this.setBlockState(world, Blocks.COBBLESTONE.getDefaultState(), 14, 1, 6, boundingBox);
        this.setBlockState(world, Blocks.COBBLESTONE.getDefaultState(), 2, 1, 14, boundingBox);
        this.setBlockState(world, Blocks.COBBLESTONE.getDefaultState(), 8, 1, 14, boundingBox);
        this.setBlockState(world, Blocks.COBBLESTONE.getDefaultState(), 14, 1, 14, boundingBox);
        this.fillWithBlocks(world, boundingBox, 10, 1, 3, 10, 1, 5, Blocks.COBBLESTONE.getDefaultState(), Blocks.COBBLESTONE.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 13, 1, 3, 13, 1, 5, Blocks.COBBLESTONE.getDefaultState(), Blocks.COBBLESTONE.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 11, 1, 3, 12, 1, 3, Blocks.COBBLESTONE.getDefaultState(), Blocks.COBBLESTONE.getDefaultState(), false);
        //place slabs
        this.setBlockState(world, Blocks.DOUBLE_STONE_SLAB.getDefaultState(), 1, 1, 10, boundingBox);
        this.setBlockState(world, Blocks.DOUBLE_STONE_SLAB.getDefaultState(), 15, 1, 10, boundingBox);
        this.fillWithBlocks(world, boundingBox, 2, 1, 7, 2, 1, 13, Blocks.DOUBLE_STONE_SLAB.getDefaultState(), Blocks.DOUBLE_STONE_SLAB.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 8, 1, 7, 8, 1, 13, Blocks.DOUBLE_STONE_SLAB.getDefaultState(), Blocks.DOUBLE_STONE_SLAB.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 14, 1, 7, 14, 1, 13, Blocks.DOUBLE_STONE_SLAB.getDefaultState(), Blocks.DOUBLE_STONE_SLAB.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 2, 1, 7, 14, 1, 7, Blocks.DOUBLE_STONE_SLAB.getDefaultState(), Blocks.DOUBLE_STONE_SLAB.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 2, 1, 13, 14, 1, 13, Blocks.DOUBLE_STONE_SLAB.getDefaultState(), Blocks.DOUBLE_STONE_SLAB.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 11, 1, 4, 12, 1, 6, Blocks.DOUBLE_STONE_SLAB.getDefaultState(), Blocks.DOUBLE_STONE_SLAB.getDefaultState(), false);
        //place water.getDefaultState()
        this.fillWithBlocks(world, boundingBox, 3, 1, 6, 7, 1, 6, Blocks.WATER.getDefaultState(), Blocks.WATER.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 9, 1, 6, 10, 1, 6, Blocks.WATER.getDefaultState(), Blocks.WATER.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 3, 1, 14, 7, 1, 14, Blocks.WATER.getDefaultState(), Blocks.WATER.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 9, 1, 14, 13, 1, 14, Blocks.WATER.getDefaultState(), Blocks.WATER.getDefaultState(), false);
        this.setBlockState(world, Blocks.WATER.getDefaultState(), 13, 1, 6, boundingBox);
        //place farmland
        this.fillWithBlocks(world, boundingBox, 3, 1, 8, 7, 1, 12, Blocks.FARMLAND.getStateFromMeta(7), Blocks.FARMLAND.getStateFromMeta(7), false);
        this.fillWithBlocks(world, boundingBox, 9, 1, 8, 13, 1, 12, Blocks.FARMLAND.getStateFromMeta(7), Blocks.FARMLAND.getStateFromMeta(7), false);
        //place standing logs
        this.fillWithBlocks(world, boundingBox, 10, 2, 3, 10, 5, 3, Blocks.LOG.getStateFromMeta(0), Blocks.LOG.getStateFromMeta(0), false);
        this.fillWithBlocks(world, boundingBox, 13, 2, 3, 13, 5, 3, Blocks.LOG.getStateFromMeta(0), Blocks.LOG.getStateFromMeta(0), false);
        this.fillWithBlocks(world, boundingBox, 1, 2, 6, 1, 6, 6, Blocks.LOG.getStateFromMeta(0), Blocks.LOG.getStateFromMeta(0), false);
        this.fillWithBlocks(world, boundingBox, 3, 2, 6, 3, 5, 6, Blocks.LOG.getStateFromMeta(0), Blocks.LOG.getStateFromMeta(0), false);
        this.fillWithBlocks(world, boundingBox, 6, 2, 6, 6, 5, 6, Blocks.LOG.getStateFromMeta(0), Blocks.LOG.getStateFromMeta(0), false);
        this.fillWithBlocks(world, boundingBox, 8, 2, 6, 8, 6, 6, Blocks.LOG.getStateFromMeta(0), Blocks.LOG.getStateFromMeta(0), false);
        this.fillWithBlocks(world, boundingBox, 10, 2, 6, 10, 5, 6, Blocks.LOG.getStateFromMeta(0), Blocks.LOG.getStateFromMeta(0), false);
        this.fillWithBlocks(world, boundingBox, 13, 2, 6, 13, 5, 6, Blocks.LOG.getStateFromMeta(0), Blocks.LOG.getStateFromMeta(0), false);
        this.fillWithBlocks(world, boundingBox, 15, 2, 6, 15, 6, 6, Blocks.LOG.getStateFromMeta(0), Blocks.LOG.getStateFromMeta(0), false);
        this.fillWithBlocks(world, boundingBox, 1, 2, 9, 1, 5, 9, Blocks.LOG.getStateFromMeta(0), Blocks.LOG.getStateFromMeta(0), false);
        this.fillWithBlocks(world, boundingBox, 15, 2, 9, 15, 5, 9, Blocks.LOG.getStateFromMeta(0), Blocks.LOG.getStateFromMeta(0), false);
        this.fillWithBlocks(world, boundingBox, 1, 2, 11, 1, 5, 11, Blocks.LOG.getStateFromMeta(0), Blocks.LOG.getStateFromMeta(0), false);
        this.fillWithBlocks(world, boundingBox, 15, 2, 11, 15, 5, 11, Blocks.LOG.getStateFromMeta(0), Blocks.LOG.getStateFromMeta(0), false);
        this.fillWithBlocks(world, boundingBox, 1, 2, 14, 1, 6, 14, Blocks.LOG.getStateFromMeta(0), Blocks.LOG.getStateFromMeta(0), false);
        this.fillWithBlocks(world, boundingBox, 8, 2, 14, 8, 6, 14, Blocks.LOG.getStateFromMeta(0), Blocks.LOG.getStateFromMeta(0), false);
        this.fillWithBlocks(world, boundingBox, 15, 2, 14, 15, 6, 14, Blocks.LOG.getStateFromMeta(0), Blocks.LOG.getStateFromMeta(0), false);
        //logs along x-axis
        this.fillWithBlocks(world, boundingBox, 11, 5, 3, 12, 5, 3, Blocks.LOG.getStateFromMeta(4), Blocks.LOG.getStateFromMeta(4), false);
        this.fillWithBlocks(world, boundingBox, 2, 6, 6, 7, 6, 6, Blocks.LOG.getStateFromMeta(4), Blocks.LOG.getStateFromMeta(4), false);
        this.fillWithBlocks(world, boundingBox, 9, 6, 6, 14, 6, 6, Blocks.LOG.getStateFromMeta(4), Blocks.LOG.getStateFromMeta(4), false);
        this.fillWithBlocks(world, boundingBox, 4, 4, 6, 5, 4, 6, Blocks.LOG.getStateFromMeta(4), Blocks.LOG.getStateFromMeta(4), false);
        this.fillWithBlocks(world, boundingBox, 11, 4, 6, 12, 4, 6, Blocks.LOG.getStateFromMeta(4), Blocks.LOG.getStateFromMeta(4), false);
        this.fillWithBlocks(world, boundingBox, 2, 6, 14, 7, 6, 14, Blocks.LOG.getStateFromMeta(4), Blocks.LOG.getStateFromMeta(4), false);
        this.fillWithBlocks(world, boundingBox, 9, 6, 14, 14, 6, 14, Blocks.LOG.getStateFromMeta(4), Blocks.LOG.getStateFromMeta(4), false);
        //logs along z-axis
        this.fillWithBlocks(world, boundingBox, 1, 6, 7, 1, 6, 13, Blocks.LOG.getStateFromMeta(8), Blocks.LOG.getStateFromMeta(8), false);
        this.fillWithBlocks(world, boundingBox, 8, 6, 7, 8, 6, 13, Blocks.LOG.getStateFromMeta(8), Blocks.LOG.getStateFromMeta(8), false);
        this.fillWithBlocks(world, boundingBox, 15, 6, 7, 15, 6, 13, Blocks.LOG.getStateFromMeta(8), Blocks.LOG.getStateFromMeta(8), false);
        this.fillWithBlocks(world, boundingBox, 10, 5, 4, 10, 5, 5, Blocks.LOG.getStateFromMeta(8), Blocks.LOG.getStateFromMeta(8), false);
        this.fillWithBlocks(world, boundingBox, 13, 5, 4, 13, 5, 5, Blocks.LOG.getStateFromMeta(8), Blocks.LOG.getStateFromMeta(8), false);
        this.setBlockState(world, Blocks.LOG.getStateFromMeta(8), 1, 4, 10, boundingBox);
        this.setBlockState(world, Blocks.LOG.getStateFromMeta(8), 15, 4, 10, boundingBox);
        //cobble walls
        this.fillWithBlocks(world, boundingBox, 4, 2, 6, 5, 2, 6, Blocks.COBBLESTONE.getDefaultState(), Blocks.COBBLESTONE.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 10, 2, 4, 10, 2, 5, Blocks.COBBLESTONE.getDefaultState(), Blocks.COBBLESTONE.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 11, 2, 3, 12, 2, 3, Blocks.COBBLESTONE.getDefaultState(), Blocks.COBBLESTONE.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 13, 2, 4, 13, 2, 5, Blocks.COBBLESTONE.getDefaultState(), Blocks.COBBLESTONE.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 1, 2, 7, 1, 2, 8, Blocks.COBBLESTONE.getDefaultState(), Blocks.COBBLESTONE.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 1, 2, 12, 1, 2, 13, Blocks.COBBLESTONE.getDefaultState(), Blocks.COBBLESTONE.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 15, 2, 7, 15, 2, 8, Blocks.COBBLESTONE.getDefaultState(), Blocks.COBBLESTONE.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 15, 2, 12, 15, 2, 13, Blocks.COBBLESTONE.getDefaultState(), Blocks.COBBLESTONE.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 2, 2, 14, 7, 2, 14, Blocks.COBBLESTONE.getDefaultState(), Blocks.COBBLESTONE.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 9, 2, 14, 14, 2, 14, Blocks.COBBLESTONE.getDefaultState(), Blocks.COBBLESTONE.getDefaultState(), false);
        this.setBlockState(world, Blocks.COBBLESTONE.getDefaultState(), 2, 2, 6, boundingBox);
        this.setBlockState(world, Blocks.COBBLESTONE.getDefaultState(), 7, 2, 6, boundingBox);
        this.setBlockState(world, Blocks.COBBLESTONE.getDefaultState(), 9, 2, 6, boundingBox);
        this.setBlockState(world, Blocks.COBBLESTONE.getDefaultState(), 14, 2, 6, boundingBox);
        //place glass
        this.fillWithBlocks(world, boundingBox, 1, 3, 7, 1, 5, 8, Blocks.GLASS.getDefaultState(), Blocks.GLASS.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 1, 3, 12, 1, 5, 13, Blocks.GLASS.getDefaultState(), Blocks.GLASS.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 2, 3, 6, 2, 5, 6, Blocks.GLASS.getDefaultState(), Blocks.GLASS.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 4, 3, 6, 5, 3, 6, Blocks.GLASS.getDefaultState(), Blocks.GLASS.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 7, 3, 6, 7, 5, 6, Blocks.GLASS.getDefaultState(), Blocks.GLASS.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 9, 3, 6, 9, 5, 6, Blocks.GLASS.getDefaultState(), Blocks.GLASS.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 14, 3, 6, 14, 5, 6, Blocks.GLASS.getDefaultState(), Blocks.GLASS.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 11, 5, 4, 12, 5, 6, Blocks.GLASS.getDefaultState(), Blocks.GLASS.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 15, 3, 7, 15, 5, 8, Blocks.GLASS.getDefaultState(), Blocks.GLASS.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 15, 3, 12, 15, 5, 13, Blocks.GLASS.getDefaultState(), Blocks.GLASS.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 2, 3, 14, 7, 5, 14, Blocks.GLASS.getDefaultState(), Blocks.GLASS.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 9, 3, 14, 14, 5, 14, Blocks.GLASS.getDefaultState(), Blocks.GLASS.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 2, 6, 7, 7, 6, 13, Blocks.GLASS.getDefaultState(), Blocks.GLASS.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 9, 6, 7, 14, 6, 13, Blocks.GLASS.getDefaultState(), Blocks.GLASS.getDefaultState(), false);
        this.setBlockState(world, Blocks.GLASS.getDefaultState(), 4, 5, 6, boundingBox);
        this.setBlockState(world, Blocks.GLASS.getDefaultState(), 1, 5, 10, boundingBox);
        this.setBlockState(world, Blocks.GLASS.getDefaultState(), 15, 5, 10, boundingBox);
        //wooden pillars
        this.fillWithBlocks(world, boundingBox, 3, 2, 1, 3, 4, 1, Blocks.PLANKS.getDefaultState(), Blocks.PLANKS.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 3, 2, 4, 3, 4, 4, Blocks.PLANKS.getDefaultState(), Blocks.PLANKS.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 6, 2, 1, 6, 4, 1, Blocks.PLANKS.getDefaultState(), Blocks.PLANKS.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 6, 2, 4, 6, 4, 4, Blocks.PLANKS.getDefaultState(), Blocks.PLANKS.getDefaultState(), false);
        //oak stairs
        this.setBlockState(world, Blocks.OAK_STAIRS.getStateFromMeta(6), 3, 4, 3, boundingBox);
        this.setBlockState(world, Blocks.OAK_STAIRS.getStateFromMeta(6), 6, 4, 3, boundingBox);
        this.setBlockState(world, Blocks.OAK_STAIRS.getStateFromMeta(6), 11, 4, 3, boundingBox);
        this.setBlockState(world, Blocks.OAK_STAIRS.getStateFromMeta(6), 12, 4, 3, boundingBox);
        this.setBlockState(world, Blocks.OAK_STAIRS.getStateFromMeta(4), 5, 4, 1, boundingBox);
        this.setBlockState(world, Blocks.OAK_STAIRS.getStateFromMeta(4), 5, 4, 4, boundingBox);
        this.setBlockState(world, Blocks.OAK_STAIRS.getStateFromMeta(4), 10, 4, 4, boundingBox);
        this.setBlockState(world, Blocks.OAK_STAIRS.getStateFromMeta(4), 10, 4, 5, boundingBox);
        this.setBlockState(world, Blocks.OAK_STAIRS.getStateFromMeta(5), 4, 4, 1, boundingBox);
        this.setBlockState(world, Blocks.OAK_STAIRS.getStateFromMeta(5), 4, 4, 4, boundingBox);
        this.setBlockState(world, Blocks.OAK_STAIRS.getStateFromMeta(5), 13, 4, 4, boundingBox);
        this.setBlockState(world, Blocks.OAK_STAIRS.getStateFromMeta(5), 13, 4, 5, boundingBox);
        this.setBlockState(world, Blocks.OAK_STAIRS.getStateFromMeta(7), 3, 4, 2, boundingBox);
        this.setBlockState(world, Blocks.OAK_STAIRS.getStateFromMeta(7), 6, 4, 2, boundingBox);
        //place doors
        this.setBlockState(world, Blocks.OAK_DOOR.getStateFromMeta(0), 1, 2, 10, boundingBox);
        this.setBlockState(world, Blocks.OAK_DOOR.getStateFromMeta(8), 1, 3, 10, boundingBox);
        this.setBlockState(world, Blocks.OAK_DOOR.getStateFromMeta(2), 15, 2, 10, boundingBox);
        this.setBlockState(world, Blocks.OAK_DOOR.getStateFromMeta(8), 15, 3, 10, boundingBox);
        //place air blocks
        this.fillWithBlocks(world, boundingBox, 0, 2, 0, 0, 9, 15, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 16, 2, 0, 16, 9, 15, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 0, 2, 0, 16, 9, 0, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 0, 2, 15, 16, 9, 15, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 2, 2, 7, 14, 5, 13, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 1, 7, 6, 14, 9, 14, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 1, 3, 7, 2, 9, 5, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 2, 2, 7, 2, 2, 5, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 3, 2, 2, 3, 3, 3, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 2, 2, 5, 9, 4, 5, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 2, 5, 5, 4, 5, 5, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 6, 5, 5, 9, 5, 5, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 2, 6, 5, 9, 9, 5, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 4, 3, 1, 5, 3, 1, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 4, 2, 4, 5, 3, 4, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 4, 2, 2, 5, 4, 3, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 6, 2, 2, 6, 3, 3, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 7, 2, 2, 9, 9, 4, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 10, 2, 2, 13, 9, 2, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 10, 6, 3, 13, 9, 5, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 14, 2, 2, 14, 9, 5, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 7, 3, 1, 15, 9, 1, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 15, 3, 2, 15, 9, 5, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 11, 3, 4, 12, 4, 5, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 11, 2, 6, 12, 4, 6, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 11, 2, 5, 12, 4, 5, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 12, 2, 4, 12, 4, 4, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
        //place fence.getDefaultState()s
        this.fillWithBlocks(world, boundingBox, 1, 2, 1, 2, 2, 1, Blocks.OAK_FENCE.getDefaultState(), Blocks.OAK_FENCE.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 4, 2, 1, 5, 2, 1, Blocks.OAK_FENCE.getDefaultState(), Blocks.OAK_FENCE.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 7, 2, 1, 15, 2, 1, Blocks.OAK_FENCE.getDefaultState(), Blocks.OAK_FENCE.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 1, 2, 2, 1, 2, 5, Blocks.OAK_FENCE.getDefaultState(), Blocks.OAK_FENCE.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 15, 2, 2, 15, 2, 5, Blocks.OAK_FENCE.getDefaultState(), Blocks.OAK_FENCE.getDefaultState(), false);
        this.fillWithBlocks(world, boundingBox, 8, 4, 9, 8, 4, 11, Blocks.OAK_FENCE.getDefaultState(), Blocks.OAK_FENCE.getDefaultState(), false);
        this.setBlockState(world, Blocks.OAK_FENCE.getDefaultState(), 8, 5, 9, boundingBox);
        this.setBlockState(world, Blocks.OAK_FENCE.getDefaultState(), 8, 5, 11, boundingBox);
        //place torches
        this.setBlockState(world, Blocks.TORCH.getDefaultState(), 0, 4, 6, boundingBox);
        this.setBlockState(world, Blocks.TORCH.getDefaultState(), 0, 4, 9, boundingBox);
        this.setBlockState(world, Blocks.TORCH.getDefaultState(), 0, 4, 11, boundingBox);
        this.setBlockState(world, Blocks.TORCH.getDefaultState(), 0, 4, 14, boundingBox);
        this.setBlockState(world, Blocks.TORCH.getDefaultState(), 9, 4, 3, boundingBox);
        this.setBlockState(world, Blocks.TORCH.getDefaultState(), 14, 4, 9, boundingBox);
        this.setBlockState(world, Blocks.TORCH.getDefaultState(), 14, 4, 11, boundingBox);
        this.setBlockState(world, Blocks.TORCH.getDefaultState(), 1, 3, 1, boundingBox);
        this.setBlockState(world, Blocks.TORCH.getDefaultState(), 15, 3, 1, boundingBox);
        this.setBlockState(world, Blocks.TORCH.getDefaultState(), 1, 4, 5, boundingBox);
        this.setBlockState(world, Blocks.TORCH.getDefaultState(), 8, 4, 5, boundingBox);
        this.setBlockState(world, Blocks.TORCH.getDefaultState(), 10, 4, 2, boundingBox);
        this.setBlockState(world, Blocks.TORCH.getDefaultState(), 13, 4, 2, boundingBox);
        this.setBlockState(world, Blocks.TORCH.getDefaultState(), 15, 4, 5, boundingBox);
        this.setBlockState(world, Blocks.TORCH.getDefaultState(), 8, 4, 13, boundingBox);
        this.setBlockState(world, Blocks.TORCH.getDefaultState(), 14, 4, 3, boundingBox);
        this.setBlockState(world, Blocks.TORCH.getDefaultState(), 16, 4, 6, boundingBox);
        this.setBlockState(world, Blocks.TORCH.getDefaultState(), 16, 4, 9, boundingBox);
        this.setBlockState(world, Blocks.TORCH.getDefaultState(), 16, 4, 14, boundingBox);
        this.setBlockState(world, Blocks.TORCH.getDefaultState(), 16, 4, 14, boundingBox);
        this.setBlockState(world, Blocks.TORCH.getDefaultState(), 2, 4, 9, boundingBox);
        this.setBlockState(world, Blocks.TORCH.getDefaultState(), 2, 4, 11, boundingBox);
        this.setBlockState(world, Blocks.TORCH.getDefaultState(), 1, 4, 15, boundingBox);
        this.setBlockState(world, Blocks.TORCH.getDefaultState(), 8, 4, 15, boundingBox);
        this.setBlockState(world, Blocks.TORCH.getDefaultState(), 15, 4, 15, boundingBox);
        this.setBlockState(world, Blocks.TORCH.getDefaultState(), 3, 4, 7, boundingBox);
        this.setBlockState(world, Blocks.TORCH.getDefaultState(), 6, 4, 7, boundingBox);
        this.setBlockState(world, Blocks.TORCH.getDefaultState(), 8, 4, 7, boundingBox);
        this.setBlockState(world, Blocks.TORCH.getDefaultState(), 10, 4, 7, boundingBox);
        this.setBlockState(world, Blocks.TORCH.getDefaultState(), 13, 4, 7, boundingBox);
        //place crops
        List<IAgriPlant> plants = PlantRegistry.getInstance().getPlants();
		plants.removeIf((p) -> { return p.getTier() > AgriCraftConfig.greenHouseMaxTier; });
        for(int x=3;x<=7;x++) {
            for(int z=8;z<=12;z++) {
                this.generateStructureCrop(world, boundingBox, x, 2, z, (z%2==1 && x%2==0) || (x==5 &&z==10), plants);
            }
        }
        for(int x=9;x<=13;x++) {
            for(int z=8;z<=12;z++) {
                this.generateStructureCrop(world, boundingBox, x, 2, z, (z%2==1 && x%2==0) || (x==11 &&z==10), plants);
            }
        }
        //place water.getDefaultState() tank
        for(int x=3;x<=6;x++) {
            for(int y=5;y<=8;y++) {
                for(int z=1;z<=4;z++) {
                    this.generateStructureWoodenTank(world, boundingBox, x, y, z, (x==6 && y==8 && z==4));
                }
            }
        }
        //place irrigation channels
        this.fillWithBlocks(world, boundingBox, 5, 5, 5, 5, 5, 10, com.infinityraider.agricraft.init.AgriBlocks.CHANNEL.getDefaultState(), com.infinityraider.agricraft.init.AgriBlocks.CHANNEL.getDefaultState(), false);
        for(int z=5;z<=10;z++) {
            this.generateStructureIrrigationChannel(world, boundingBox, 5, 5, z);
        }
        this.fillWithBlocks(world, boundingBox, 6, 5, 10, 11, 5, 10, com.infinityraider.agricraft.init.AgriBlocks.CHANNEL.getDefaultState(), com.infinityraider.agricraft.init.AgriBlocks.CHANNEL.getDefaultState(), false);
        for(int x=6;x<=11;x++) {
            this.generateStructureIrrigationChannel(world, boundingBox, x, 5, 10);
        }
        //place sprinklers
        this.generateStructureSprinkler(world, boundingBox, 5, 4, 10);
        this.generateStructureSprinkler(world, boundingBox, 11, 4, 10);
        //place seed analyzer
        this.generateStructureSeedAnalyzer(world, boundingBox, 11, 2, 4, AgriForgeDirection.SOUTH);
        this.spawnVillagers(world, boundingBox, 3, 1, 3, 1);
        return true;
    }

    //place a tank
    protected boolean generateStructureWoodenTank(World world, StructureBoundingBox boundingBox, int x, int y, int z, boolean multiBlockify) {
        int xCoord = this.getXWithOffset(x, z);
        int yCoord = this.getYWithOffset(y);
        int zCoord = this.getZWithOffset(x, z);
        if (boundingBox.isVecInside(new Vec3i(xCoord, yCoord, zCoord))) {
            BlockPos pos = new BlockPos(xCoord, yCoord, zCoord);
            world.setBlockState(pos, com.infinityraider.agricraft.init.AgriBlocks.TANK.getDefaultState(), 2);
            TileEntityTank tank = (TileEntityTank) world.getTileEntity(pos);
            if (tank == null) {
                tank = new TileEntityTank();
                world.setTileEntity(pos, tank);
            }
            tank.setMaterial(new ItemStack(Blocks.PLANKS, 1, 0));
            if(multiBlockify) {
                tank.getMultiBlockManager().onBlockPlaced(world, pos, tank);
                AgriCore.getLogger("AgriCraft").debug("Creating Multiblock at (" + xCoord + ", " + yCoord + ", " + zCoord + ")");
            }
            return true;
        }
        else {
            return false;
        }
    }

    //place an irrigation channel
    protected boolean generateStructureIrrigationChannel(World world, StructureBoundingBox boundingBox, int x, int y, int z) {
        int xCoord = this.getXWithOffset(x, z);
        int yCoord = this.getYWithOffset(y);
        int zCoord = this.getZWithOffset(x, z);
        if (boundingBox.isVecInside(new Vec3i(xCoord, yCoord, zCoord))) {
            BlockPos pos = new BlockPos(xCoord, yCoord, zCoord);
            world.setBlockState(pos, com.infinityraider.agricraft.init.AgriBlocks.CHANNEL.getDefaultState(), 2);
            TileEntityChannel channel = (TileEntityChannel) world.getTileEntity(pos);
            if (channel!=null) {
                channel.setMaterial(new ItemStack(Blocks.PLANKS, 1, 0));
            }
            return true;
        }
        else {
            return false;
        }
    }

    //place a sprinkler
    protected boolean generateStructureSprinkler(World world, StructureBoundingBox boundingBox, int x, int y, int z) {
        int xCoord = this.getXWithOffset(x, z);
        int yCoord = this.getYWithOffset(y);
        int zCoord = this.getZWithOffset(x, z);
        if (boundingBox.isVecInside(new Vec3i(xCoord, yCoord, zCoord))) {
            BlockPos pos = new BlockPos(xCoord, yCoord, zCoord);
            world.setBlockState(pos, com.infinityraider.agricraft.init.AgriBlocks.SPRINKLER.getDefaultState(), 2);
            return true;
        }
        else {
            return false;
        }
    }
}