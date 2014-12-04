package starwork.economy.constructing;

import starwork.units.SelectUnits;
import starwork.units.Units;
import bwapi.Position;
import bwapi.Unit;
import bwapi.UnitType;

public class BuilderSelector {

	public static Unit getOptimalBuilder(Position buildTile) {
		return getOptimalBuilder(buildTile, null);
	}

	public static Unit getOptimalBuilder(Position buildTile, UnitType buildingType) {

		// Define which workers could construct a building (filter out
		// repairers, other builders
		// etc)
		Units freeWorkers = getFreeWorkers();

		// Return the closest builder to the tile
		Unit builder = SelectUnits.from(freeWorkers).nearestTo(buildTile);

		return builder;
	}

	public static Unit getRandomWorker() {
		for (Unit unit : SelectUnits.ourWorkers().list()) {
			if (!unit.isConstructing()) {
				return unit;
			}
		}
		return null;
	}

	// =========================================================

	private static Units getFreeWorkers() {
		Units freeWorkers = new Units();
		for (Unit worker : SelectUnits.ourWorkers().list()) {
			if (!worker.isConstructing() && !worker.isRepairing()) {

				// if (!WorkerManager.isProfessionalRepairer(worker)) {
				// if (!worker.equals(WorkerManager.getGuyToChaseOthers()) ||
				// TerranOffensiveBunker.isStrategyActive()) {
				freeWorkers.addUnit(worker);
				// }
				// }
			}
		}
		return freeWorkers;
	}

}
