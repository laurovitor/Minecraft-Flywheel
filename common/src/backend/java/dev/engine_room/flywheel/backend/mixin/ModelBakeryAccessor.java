package dev.engine_room.flywheel.backend.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.resources.Identifier;

@Mixin(ModelBakery.class)
public interface ModelBakeryAccessor {
	@Accessor("BREAKING_LOCATIONS")
	static List<Identifier> flywheel$getBreakingLocations() {
		throw new AssertionError();
	}
}
