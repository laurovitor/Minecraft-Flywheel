package dev.engine_room.flywheel.lib.vertex;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.renderpearl.api.vertex.VertexFormat;

public class FlywheelVertexFormats {
	/// Basically the same as {@link DefaultVertexFormat#BLOCK} but with the normals included
	public static final VertexFormat BLOCK_VERTEX_FORMAT = DefaultVertexFormat.BLOCK;
}
