package starwork.economy.building;

import bwapi.Unit;
import bwapi.UnitType;

public class BasePrivate {

	public static UnitType WORKER = UnitType.Terran_SCV;

	public static void act(Unit base) {
		if (!base.isTraining()) {
			if (shouldTrainWorker(base)) {
				base.train(WORKER);
			}
		}
	}

	// =========================================================

	private static boolean shouldTrainWorker(Unit base) {
		return true;
	}

}
