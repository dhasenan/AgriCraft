package com.infinityraider.agricraft.plugins.jei;

import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;
import com.infinityraider.agricraft.AgriCraft;
import com.infinityraider.agricraft.api.v1.AgriApi;
import com.infinityraider.agricraft.api.v1.genetics.IAgriMutation;
import com.infinityraider.agricraft.content.AgriItemRegistry;

import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class AgriMutationRecipeCategory implements IRecipeCategory<IAgriMutation> {

    public static final ResourceLocation ID = new ResourceLocation(AgriCraft.instance.getModId(), "gui/mutation");

    public final IAgriDrawable icon;
    public final IAgriDrawable background;

    public static void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(AgriApi.getMutationRegistry().all(), ID);
    }

    public static void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(AgriItemRegistry.getInstance().crop_sticks_wood), AgriMutationRecipeCategory.ID);
        registration.addRecipeCatalyst(new ItemStack(AgriItemRegistry.getInstance().crop_sticks_iron), AgriMutationRecipeCategory.ID);
        registration.addRecipeCatalyst(new ItemStack(AgriItemRegistry.getInstance().crop_sticks_obsidian), AgriMutationRecipeCategory.ID);
    }

    public AgriMutationRecipeCategory(IRecipeCategoryRegistration registration) {
        this.icon = JeiPlugin.createAgriDrawable(new ResourceLocation(AgriCraft.instance.getModId(), "textures/item/debugger.png"), 0, 0, 16, 16, 16, 16);
        this.background = JeiPlugin.createAgriDrawable(new ResourceLocation(AgriCraft.instance.getModId(), "textures/gui/jei/crop_mutation.png"), 0, 0, 128, 128, 128, 128);
    }

    @Override
    public ResourceLocation getUid() {
        return ID;
    }

    @Override
    public Class<IAgriMutation> getRecipeClass() {
        return IAgriMutation.class;
    }

    @Override
    public String getTitle() {
        return I18n.format("agricraft.gui.mutation");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setIngredients(IAgriMutation mutation, IIngredients ingredients) {
        ingredients.setInputLists(AgriPlantIngredient.TYPE,
                mutation.getParents().stream().map(ImmutableList::of).collect(Collectors.toList()));
        ingredients.setOutputLists(AgriPlantIngredient.TYPE, ImmutableList.of(ImmutableList.of(mutation.getChild())));
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IAgriMutation mutation, IIngredients ingredients) {
        // Denote that this is a shapeless recipe.
        layout.setShapeless();

        // Setup Recipe Parents
        layout.getIngredientsGroup(AgriPlantIngredient.TYPE).init(0, true, 24, 39);
        layout.getIngredientsGroup(AgriPlantIngredient.TYPE).init(1, true, 86, 39);

        // Setup Recipe Child
        layout.getIngredientsGroup(AgriPlantIngredient.TYPE).init(2, true, 55, 65);

        // Setup Recipe Requirements
        layout.getIngredientsGroup(AgriPlantIngredient.TYPE).init(3, true, 55, 86);
        layout.getIngredientsGroup(AgriPlantIngredient.TYPE).init(4, false, 55, 39);

        // Register Recipe Elements
        layout.getIngredientsGroup(AgriPlantIngredient.TYPE).set(ingredients);
    }

}
