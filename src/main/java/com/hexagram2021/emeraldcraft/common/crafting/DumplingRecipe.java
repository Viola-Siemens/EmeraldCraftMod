package com.hexagram2021.emeraldcraft.common.crafting;

import com.hexagram2021.emeraldcraft.common.items.capabilities.FarciFoodStorage;
import com.hexagram2021.emeraldcraft.common.register.ECItemTags;
import com.hexagram2021.emeraldcraft.common.register.ECItems;
import com.hexagram2021.emeraldcraft.common.register.ECRecipeSerializer;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

import static com.hexagram2021.emeraldcraft.common.util.RegistryHelper.getRegistryName;

public class DumplingRecipe extends CustomRecipe {
	public DumplingRecipe(CraftingBookCategory category) {
		super(category);
	}

	@Override
	public boolean matches(CraftingContainer craftingContainer, Level level) {
		boolean dough = false;
		boolean mince = false;
		int count = 0;
		for(int i = 0; i < craftingContainer.getContainerSize(); ++i) {
			ItemStack itemstack = craftingContainer.getItem(i);
			if(itemstack.isEmpty()) {
				continue;
			}
			if(itemstack.is(ECItemTags.WHEAT_DOUGH) && !dough) {
				dough = true;
			} else if(itemstack.is(ECItemTags.MINCE) && !mince) {
				mince = true;
			} else if(count < 2 && itemstack.is(ECItemTags.VEGETABLE_FILLINGS)) {
				count += 1;
			} else {
				return false;
			}
		}
		return dough && count > 0;
	}

	@Override
	public ItemStack assemble(CraftingContainer craftingContainer, RegistryAccess registryAccess) {
		ItemStack ret = new ItemStack(ECItems.RAW_DUMPLING.get(), 4);

		ListTag listTag = new ListTag();
		for(int i = 0; i < craftingContainer.getContainerSize(); ++i) {
			ItemStack itemstack = craftingContainer.getItem(i);
			if(itemstack.is(ECItemTags.MINCE) || itemstack.is(ECItemTags.VEGETABLE_FILLINGS)) {
				listTag.add(StringTag.valueOf(getRegistryName(itemstack.getItem()).toString()));
			}
		}
		CompoundTag tag = ret.getOrCreateTag();
		tag.put(FarciFoodStorage.TAG_FILLINGS, listTag);
		ret.setTag(tag);
		return ret;
	}

	@Override
	public boolean canCraftInDimensions(int wid, int hgt) {
		return wid >= 2 && hgt >= 2;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ECRecipeSerializer.CRAFTING_DUMPLING_SERIALIZER.get();
	}
}
