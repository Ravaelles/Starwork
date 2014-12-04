package bwta;

import java.util.HashMap;
import java.util.Map;

import starwork.map.AbstractPosition;
import bwapi.Position;

/**
 * A chokepoint connects exactly two regions.
 */
public class Chokepoint extends AbstractPosition {

	// =========================================================
	// === StarworkMirror ======================================
	// =========================================================

	private boolean isDisabled = false;

	public boolean isDisabled() {
		return isDisabled;
	}

	public void setDisabled(boolean disabled) {
		this.isDisabled = disabled;
	}

	@Override
	public int getX() {
		return getCenter().getX();
	}

	@Override
	public int getY() {
		return getCenter().getY();
	}

	public int getCenterX() {
		return getX();
	}

	public int getCenterY() {
		return getY();
	}

	public boolean belongsToRegion(Region region) {
		return region.getChokepoints().contains(this);
	}

	// =========================================================
	// === END OF StarworkMirror ======================================
	// =========================================================

	/**
	 * Returns the center of the chokepoint.
	 */
	public Position getCenter() {
		return getCenter_native(pointer);
	}

	/**
	 * Returns the width of the chokepoint.
	 */
	public double getWidth() {
		return getWidth_native(pointer);
	}

	private static Map<Long, Chokepoint> instances = new HashMap<Long, Chokepoint>();

	private Chokepoint(long pointer) {
		this.pointer = pointer;
	}

	private static Chokepoint get(long pointer) {
		Chokepoint instance = instances.get(pointer);
		if (instance == null) {
			instance = new Chokepoint(pointer);
			instances.put(pointer, instance);
		}
		return instance;
	}

	private long pointer;

	private native Position getCenter_native(long pointer);

	private native double getWidth_native(long pointer);

}
