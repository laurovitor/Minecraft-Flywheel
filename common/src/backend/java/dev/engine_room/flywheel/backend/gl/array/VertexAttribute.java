package dev.engine_room.flywheel.backend.gl.array;

import com.mojang.renderpearl.backend.opengl.GlConst;

public sealed interface VertexAttribute {
	enum DataType {
		BYTE(GlConst.GL_BYTE, Byte.BYTES),
		UNSIGNED_BYTE(GlConst.GL_UNSIGNED_BYTE, Byte.BYTES),
		SHORT(GlConst.GL_SHORT, Short.BYTES),
		UNSIGNED_SHORT(GlConst.GL_UNSIGNED_SHORT, Short.BYTES),
		INT(GlConst.GL_INT, Integer.BYTES),
		UNSIGNED_INT(GlConst.GL_UNSIGNED_INT, Integer.BYTES),
		FLOAT(GlConst.GL_FLOAT, java.lang.Float.BYTES);

		private final int glEnum;
		private final int byteWidth;

		DataType(int glEnum, int byteWidth) {
			this.glEnum = glEnum;
			this.byteWidth = byteWidth;
		}

		public int glEnum() {
			return glEnum;
		}

		public int byteWidth() {
			return byteWidth;
		}
	}

	int byteWidth();

	/**
	 * A bindable attribute in a vertex array.
	 *
	 * @param type       The type of the attribute, e.g. GL_FLOAT.
	 * @param size       The number of components in the attribute, e.g. 3 for a vec3.
	 * @param normalized Whether the data is normalized.
	 */
	record Float(DataType type, int size, boolean normalized) implements VertexAttribute {
		@Override
		public int byteWidth() {
			return size * type.byteWidth();
		}
	}

	/**
	 * A bindable attribute in a vertex array.
	 *
	 * @param type The type of the attribute, e.g. GL_INT.
	 * @param size The number of components in the attribute, e.g. 3 for a vec3.
	 */
	record Int(DataType type, int size) implements VertexAttribute {
		@Override
		public int byteWidth() {
			return size * type.byteWidth();
		}
	}
}
