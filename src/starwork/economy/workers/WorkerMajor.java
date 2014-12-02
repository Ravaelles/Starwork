package starwork.economy.workers;

import starwork.economy.workers.gathering.GathererSergeant;
import starwork.units.SelectUnits;
import starwork.units.UnitDuty;
import starwork.units.Units;
import bwapi.Unit;

public class WorkerMajor {

	public static void giveOrders() {
		handleIdleUnits();
	}

	// =========================================================

	private static void handleIdleUnits() {
		Units idle = SelectUnits.ourWorkers().idle().units();
		for (Unit unit : idle.list()) {
			GathererSergeant.getInstance()
					.assignWorker(unit, UnitDuty.MINERALS);
		}
	}

}
