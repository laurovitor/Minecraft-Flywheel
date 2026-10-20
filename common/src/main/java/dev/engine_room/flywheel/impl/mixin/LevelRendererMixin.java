package dev.engine_room.flywheel.impl.mixin;

import org.joml.Matrix4f;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.systems.RenderSystem;

import dev.engine_room.flywheel.api.visualization.VisualizationManager;
import dev.engine_room.flywheel.impl.FlwImplXplat;
import dev.engine_room.flywheel.impl.event.RenderContextImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.state.level.LevelRenderState;

@Mixin(value = LevelRenderer.class, priority = 1001) // Higher priority to go after Sodium
abstract class LevelRendererMixin {
	@Shadow
	@Final
	private RenderBuffers renderBuffers;

	@Shadow
	@Final
	private LevelRenderState levelRenderState;

	@Unique
	@Nullable
	private RenderContextImpl flywheel$renderContext;

	// In 26.3 the view matrix is ready immediately before LevelRenderer submits features.
	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;submitFeatures(Lnet/minecraft/client/renderer/state/level/LevelRenderState;Lnet/minecraft/client/renderer/SubmitNodeCollector;Z)V"))
	private void flywheel$beginRender(CallbackInfo ci) {
		ClientLevel level = Minecraft.getInstance().level;
		if (level == null) {
			return;
		}
		CameraRenderState cameraState = levelRenderState.cameraRenderState;
		Matrix4f modelView = new Matrix4f(RenderSystem.getModelViewStack());
		Matrix4f projection = new Matrix4f(cameraState.projectionMatrix);
		flywheel$renderContext = RenderContextImpl.create((LevelRenderer) (Object) this, level, renderBuffers,
				modelView, projection, cameraState, levelRenderState, levelRenderState.worldPartialTicks);
		VisualizationManager manager = VisualizationManager.get(level);
		if (manager != null) {
			manager.renderDispatcher().onStartLevelRender(flywheel$renderContext);
		}
	}

	@Inject(method = "render", at = @At("RETURN"))
	private void flywheel$endRender(CallbackInfo ci) {
		flywheel$renderContext = null;
	}

	@Inject(method = "invalidateCompiledGeometry", at = @At("RETURN"))
	private void flywheel$reload(CallbackInfo ci) {
		ClientLevel level = Minecraft.getInstance().level;
		if (level != null) {
			FlwImplXplat.INSTANCE.dispatchReloadLevelRendererEvent(level);
		}
	}

	@Inject(method = "submitFeatures", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;submitBlockEntities(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/state/level/LevelRenderState;Lnet/minecraft/client/renderer/SubmitNodeCollector;)V"))
	private void flywheel$beforeBlockEntities(CallbackInfo ci) {
		if (flywheel$renderContext != null) {
			VisualizationManager manager = VisualizationManager.get(Minecraft.getInstance().level);
			if (manager != null) {
				manager.renderDispatcher().afterSubmitEntities(flywheel$renderContext);
			}
		}
	}
}
