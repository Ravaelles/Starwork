package starwork.units;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import starwork.utils.RUtilities;
import bwapi.Position;
import bwapi.Unit;

public class Units {

	private ArrayList<Unit> units = new ArrayList<>();

	// =====================================================================

	public Units() {
	}

	// =====================================================================
	// Basic functionality methods

	public Units addUnit(Unit unitToAdd) {
		units.add(unitToAdd);
		return this;
	}

	public Units addUnits(Collection<Unit> unitsToAdd) {
		units.addAll(unitsToAdd);
		return this;
	}

	public Units removeUnits(Collection<Unit> unitsToRemove) {
		units.removeAll(unitsToRemove);
		return this;
	}

	public Units removeUnit(Unit unitToRemove) {
		units.remove(unitToRemove);
		return this;
	}

	public int size() {
		return units.size();
	}

	public boolean isEmpty() {
		return units.isEmpty();
	}

	public Unit first() {
		return isEmpty() ? null : units.get(0);
	}

	// =========================================================
	// Special methods

	public void shuffle() {
		Collections.shuffle(units);
	}

	public Unit getRandom() {
		return (Unit) RUtilities.getRandomListElement(units);
	}

	public void sortByDistanceTo(final Position position,
			final boolean nearestFirst) {
		Collections.sort(units, new Comparator<Unit>() {

			@Override
			public int compare(Unit u1, Unit u2) {
				return u1.getPosition().getDistance(position) < u2
						.getPosition().getDistance(position) ? (nearestFirst ? -1
						: 1)
						: (nearestFirst ? 1 : -1);
			}

		});
	}

	// =====================================================================
	// Override methods

	@Override
	public String toString() {
		String string = "Units (" + units.size() + "):\n";

		for (Unit unit : units) {
			string += "   - " + unit.getType() + " (ID:" + unit.getID() + ")\n";
		}

		return string;
	}

	// =====================================================================
	// Getters

	public Collection<Unit> list() {
		ArrayList<Unit> copy = new ArrayList<Unit>();
		copy.addAll(units);
		return copy;
	}

}
