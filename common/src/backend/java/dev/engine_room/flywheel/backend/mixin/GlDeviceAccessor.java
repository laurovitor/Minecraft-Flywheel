package dev.engine_room.flywheel.backend.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import com.mojang.renderpearl.backend.opengl.DirectStateAccess;
import com.mojang.renderpearl.backend.opengl.FrameBufferCache;

@Mixin(targets = "com.mojang.renderpearl.backend.opengl.GlDevice")
public interface GlDeviceAccessor {
	@Invoker("directStateAccess")
	DirectStateAccess flywheel$directStateAccess();

	@Invoker("frameBufferCache")
	FrameBufferCache flywheel$frameBufferCache();
}
