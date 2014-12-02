package starwork.economy.workers.gathering;

import starwork.economy.workers.Workers;
import starwork.units.UnitsKnowledge;
import bwapi.Unit;

public class GathererSergeant extends Workers {

	private static GathererSergeant instance = new GathererSergeant();

	// =========================================================

	public static void giveOrders() {
		for (Unit worker : getInstance().getAssignedWorkers().list()) {
			if (UnitsKnowledge.isUnitAlive(worker)
					&& !worker.isGatheringMinerals()) {
				MineralsPrivate.gatherMinerals(worker);
			}
		}
	}

	// =========================================================

	public static GathererSergeant getInstance() {
		return instance;
	}

}
