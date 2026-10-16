package dev.engine_room.flywheel.backend.gl;

import com.mojang.renderpearl.backend.api.GpuDeviceBackend;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.renderpearl.backend.opengl.DirectStateAccess;
import com.mojang.renderpearl.backend.opengl.FrameBufferAttachment;

import java.util.List;

import dev.engine_room.flywheel.backend.mixin.GlDeviceAccessor;
import dev.engine_room.flywheel.backend.mixin.GpuDeviceAccessor;

public class GlUtil {
	public static DirectStateAccess getDirectStateAccess() {
		return glDevice().flywheel$directStateAccess();
	}

	public static int getFramebuffer(FrameBufferAttachment color, FrameBufferAttachment depth) {
		GlDeviceAccessor device = glDevice();
		return device.flywheel$frameBufferCache().getFbo(device.flywheel$directStateAccess(), List.of(color), depth);
	}

	private static GlDeviceAccessor glDevice() {
		GpuDeviceBackend backend = ((GpuDeviceAccessor) RenderSystem.getDevice()).flywheel$getBackend();
		return (GlDeviceAccessor) backend;
	}
}
