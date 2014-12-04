package bwapi;

import java.util.HashMap;
import java.util.Map;

import starwork.map.AbstractPosition;

/**
 * Positions are measured in pixels and are the highest resolution.
 */
public class Position extends AbstractPosition {

	// =========================================================
	// === StarworkMirror ======================================
	// =========================================================

	public Position(AbstractPosition position) {
		this.x = position.getX();
		this.y = position.getY();
	}

	// =========================================================
	// === END OF StarworkMirror ===============================
	// =========================================================

	private int x, y;

	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		return "[" + x + ", " + y + "]";
	}

	public native boolean isValid();

	public native Position makeValid();

	public native double getDistance(Position position);

	public native int getApproxDistance(Position position);

	public native double getLength();

	public native boolean hasPath(Position position);

	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y;
	}

	public static Position Invalid;

	public static Position None;

	public static Position Unknown;

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Position))
			return false;

		Position position = (Position) o;

		if (x != position.x)
			return false;
		if (y != position.y)
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = x;
		result = 31 * result + y;
		return result;
	}

	private static Map<Long, Position> instances = new HashMap<Long, Position>();

	private Position(long pointer) {
		this.pointer = pointer;
	}

	private static Position get(long pointer) {
		Position instance = instances.get(pointer);
		if (instance == null) {
			instance = new Position(pointer);
			instances.put(pointer, instance);
		}
		return instance;
	}

	private long pointer;
}