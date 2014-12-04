package bwta;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import starwork.map.AbstractPosition;
import bwapi.Position;
import bwapi.TilePosition;

/**
 * A <a href="BaseLocation.html">BaseLocation</a> a position on the map where it
 * makes sense to place a base (i.e. near minerals).
 */
public class BaseLocation extends AbstractPosition {

	// =========================================================
	// === StarworkMirror ======================================
	// =========================================================

	@Override
	public int getX() {
		return getPosition().getX();
	}

	@Override
	public int getY() {
		return getPosition().getY();
	}

	public static Collection<BaseLocation> getAllBaseLocations() {
		return instances.values();
	}

	// =========================================================
	// === END OF StarworkMirror ===============================
	// =========================================================

	/**
	 * Returns the position of the center of the base location.
	 */
	public Position getPosition() {
		return getPosition_native(pointer);
	}

	/**
	 * Returns the tile position of the base location.
	 */
	public TilePosition getTilePosition() {
		return getTilePosition_native(pointer);
	}

	/**
	 * Returns the region the base location is in.
	 */
	public Region getRegion() {
		return getRegion_native(pointer);
	}

	/**
	 * Returns the total mineral resource count of all accessible mineral
	 * patches.
	 */
	public int minerals() {
		return minerals_native(pointer);
	}

	/**
	 * Returns the total gas resource count of all accessible vespene geysers.
	 */
	public int gas() {
		return gas_native(pointer);
	}

	/**
	 * Returns the ground (walking) distance to the given base location. If its
	 * impossible to reach the given base location from the current one, this
	 * will return a negative value.
	 */
	public double getGroundDistance(BaseLocation other) {
		return getGroundDistance_native(pointer, other);
	}

	/**
	 * Returns the air (flying) distance to the given base location.
	 */
	public double getAirDistance(BaseLocation other) {
		return getAirDistance_native(pointer, other);
	}

	/**
	 * Returns true if the base location not in not reachable by ground from any
	 * other base location.
	 */
	public boolean isIsland() {
		return isIsland_native(pointer);
	}

	/**
	 * Returns true if the base location is mineral-only.
	 */
	public boolean isMineralOnly() {
		return isMineralOnly_native(pointer);
	}

	/**
	 * Returns true if the base location is a start location.
	 */
	public boolean isStartLocation() {
		return isStartLocation_native(pointer);
	}

	private static Map<Long, BaseLocation> instances = new HashMap<Long, BaseLocation>();

	private BaseLocation(long pointer) {
		this.pointer = pointer;
	}

	private static BaseLocation get(long pointer) {
		BaseLocation instance = instances.get(pointer);
		if (instance == null) {
			instance = new BaseLocation(pointer);
			instances.put(pointer, instance);
		}
		return instance;
	}

	private long pointer;

	private native Position getPosition_native(long pointer);

	private native TilePosition getTilePosition_native(long pointer);

	private native Region getRegion_native(long pointer);

	private native int minerals_native(long pointer);

	private native int gas_native(long pointer);

	private native double getGroundDistance_native(long pointer, BaseLocation other);

	private native double getAirDistance_native(long pointer, BaseLocation other);

	private native boolean isIsland_native(long pointer);

	private native boolean isMineralOnly_native(long pointer);

	private native boolean isStartLocation_native(long pointer);

}
