package bwta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import starwork.map.AbstractPosition;
import bwapi.Position;

/**
 * A region is a partition of the map with a polygon boundary, and is connected
 * to other regions via <a href="Chokepoint.html">chokepoints</a>.
 */
public class Region extends AbstractPosition {

	// =========================================================
	// === StarworkMirror ======================================
	// =========================================================

	@Override
	public int getX() {
		return getCenter().getX();
	}

	@Override
	public int getY() {
		return getCenter().getY();
	}

	public static Region getRegionFor(AbstractPosition position) {
		double minDist = 99999;
		Region bestRegion = null;

		for (Region region : instances.values()) {
			double dist = region.distanceTo(position);
			if (dist < minDist) {
				bestRegion = region;
			}
		}

		return bestRegion;
	}

	// =========================================================
	// === END OF StarworkMirror ======================================
	// =========================================================
	/**
	 * Returns the polygon border of the region.
	 */
	public Polygon getPolygon() {
		return getPolygon_native(pointer);
	}

	/**
	 * Returns the center of the region.
	 */
	public Position getCenter() {
		return getCenter_native(pointer);
	}

	/**
	 * Returns the set of chokepoints adjacent to the region.
	 */
	public List<Chokepoint> getChokepoints() {
		return getChokepoints_native(pointer);
	}

	/**
	 * Returns the set of base locations in the region.
	 */
	public List<BaseLocation> getBaseLocations() {
		return getBaseLocations_native(pointer);
	}

	/**
	 * Returns true if its possible to walk from this region to the given
	 * region.
	 */
	public boolean isReachable(Region region) {
		return isReachable_native(pointer, region);
	}

	/**
	 * Returns the set of regions reachable from this region.
	 */
	public List<Region> getReachableRegions() {
		return getReachableRegions_native(pointer);
	}

	private static Map<Long, Region> instances = new HashMap<Long, Region>();

	private Region(long pointer) {
		this.pointer = pointer;
	}

	private static Region get(long pointer) {
		Region instance = instances.get(pointer);
		if (instance == null) {
			instance = new Region(pointer);
			instances.put(pointer, instance);
		}
		return instance;
	}

	private long pointer;

	private native Polygon getPolygon_native(long pointer);

	private native Position getCenter_native(long pointer);

	private native List<Chokepoint> getChokepoints_native(long pointer);

	private native List<BaseLocation> getBaseLocations_native(long pointer);

	private native boolean isReachable_native(long pointer, Region region);

	private native List<Region> getReachableRegions_native(long pointer);

}
