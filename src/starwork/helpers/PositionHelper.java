package starwork.helpers;

import bwapi.Position;
import bwapi.Unit;

public class PositionHelper {

	public static double getDistanceBetween(Unit unit1, Unit unit2) {
		if (unit1 == null || unit2 == null) {
			return 0;
		}
		return getDistanceBetween(unit1.getPosition(), unit2.getPosition());
	}
	
	public static double getDistanceBetween(Position pos, Unit unit) {
		if (pos == null || unit == null) {
			return 0;
		}
		return getDistanceBetween(pos, unit.getPosition());
	}
	
	public static double getDistanceBetween(Unit unit, Position pos) {
		if (pos == null || unit == null) {
			return 0;
		}
		return getDistanceBetween(pos, unit.getPosition());
	}
	
	public static double getDistanceBetween(Position pos1, Position pos2) {
		if (pos1 == null || pos2 == null) {
			return 0;
		}
		return pos1.getDistance(pos2) / 32;
	}

}
