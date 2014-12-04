package bwapi;

import java.util.HashMap;
import java.util.Map;

import starwork.map.AbstractPosition;

/**
 * Build Tiles - each build tile is a 4x4 square of walk tiles, or a 32x32
 * square of pixels. These are called build tiles because buildability data is
 * available at this resolution, and correspond to the tiles seen in game. For
 * example, a Command Center occupies an area of 4x3 build tiles.
 */
public class TilePosition extends AbstractPosition {

	// =========================================================
	// === StarworkMirror ======================================
	// =========================================================

	public TilePosition(Position position) {
		this.x = position.getTx();
		this.y = position.getTy();
	}

	@Override
	public int getX() {
		return getTx() * 32;
	}

	@Override
	public int getY() {
		return getTy() * 32;
	}

	// =========================================================
	// === END OF StarworkMirror ===============================
	// =========================================================

	private int x, y;

	public TilePosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		return "[" + x + ", " + y + "]";
	}

	public native boolean hasPath(TilePosition position);

	public native boolean isValid();

	public native TilePosition makeValid();

	public native double getDistance(TilePosition position);

	public native double getLength();

	@Override
	public int getTx() {
		return x;
	}

	@Override
	public int getTy() {
		return y;
	}

	public static TilePosition Invalid;

	public static TilePosition None;

	public static TilePosition Unknown;

	private static Map<Long, TilePosition> instances = new HashMap<Long, TilePosition>();

	private TilePosition(long pointer) {
		this.pointer = pointer;
	}

	private static TilePosition get(long pointer) {
		TilePosition instance = instances.get(pointer);
		if (instance == null) {
			instance = new TilePosition(pointer);
			instances.put(pointer, instance);
		}
		return instance;
	}

	private long pointer;

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof TilePosition))
			return false;

		TilePosition that = (TilePosition) o;

		if (x != that.x)
			return false;
		if (y != that.y)
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = x;
		result = 31 * result + y;
		return result;
	}
}