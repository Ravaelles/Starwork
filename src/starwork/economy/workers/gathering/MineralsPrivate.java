package starwork.economy.workers.gathering;

import starwork.units.SelectUnits;
import starwork.units.Units;
import starwork.utils.ObjectAmount;
import bwapi.Unit;

public class MineralsPrivate {

	public static void gatherMinerals(Unit worker) {
		Unit mineral = getOptimalMineralForGatherer(worker);
		if (mineral != null) {
			worker.gather(mineral);
		}
	}

	// =========================================================

	private static Unit getOptimalMineralForGatherer(Unit worker) {
		Unit base = SelectUnits.ourBases().nearestTo(worker);

		// Get the minerals that are closes to the base.
		Units minerals = getMineralsNear(base);
		int counter = 0;
		while (minerals.isEmpty()) {
			minerals = getMineralsNear(base, 15 + 10 * counter++);
		}

		// Get workers
		Units workersNearby = SelectUnits.ourWorkers().inRadius(base, 15).units();
		
		ObjectAmount<Unit> mineralsToNumberOfWorkers = new ObjectAmount<Unit>();
		for (Unit mineral : minerals.list()) {
			int numberOfWorkersGatheringIt = calculateHowManyWorkersGather(mineral, workersNearby);
			mineralsToNumberOfWorkers.addAmount(mineral, numberOfWorkersGatheringIt);
//			System.out.println(UnitHelper.toStringFull(unit));
		}
//		System.out.println();
		
		return minerals.getRandom();

//		// Build mapping of number of worker to mineral
//		HashMap<Unit, Integer> workersAtMineral = new HashMap<Unit, Integer>();
//		for (Unit mineralGatherer : workers.list()) {
//			Unit mineral = mineralGatherer.getTarget();
//			System.out.println(mineralGatherer.getTarget());
//			System.out.println(mineralGatherer.getOrderTarget());
//			System.out.println();
//			if (mineral != null && mineral.getType().isMineralField()) {
//				if (workersAtMineral.containsKey(mineral)) {
//					workersAtMineral.put(mineral,
//							workersAtMineral.get(mineral) + 1);
//				} else {
//					workersAtMineral.put(mineral, 1);
//				}
//			}
//		}
//
//		// Get minimal value of gatherers assigned to one mineral
//		int minimumGatherersAssigned = workersAtMineral.isEmpty() ? 0 : 9999;
//		for (Integer value : workersAtMineral.values()) {
//			if (minimumGatherersAssigned > value) {
//				minimumGatherersAssigned = value;
//			}
//		}
//
//		// Get the nearest mineral which has minimumGatherersAssigned
//		minerals.shuffle();
//		for (Unit mineral : minerals.list()) {
//			if (!workersAtMineral.containsKey(mineral)
//					|| workersAtMineral.get(mineral) <= minimumGatherersAssigned) {
//				return mineral;
//			}
//		}
//		return minerals.isEmpty() ? null : minerals.getRandom();
	}

	// =========================================================
	// Auxiliary

	private static Units getMineralsNear(Unit unit) {
		return getMineralsNear(unit, 10);
	}

	private static Units getMineralsNear(Unit unit, int maxDistance) {
		return SelectUnits.minerals().inRadius(unit, maxDistance).units();
	}

	private static int calculateHowManyWorkersGather(Unit mineral, Units workersNeaby) {
		int howMany = 0;
		for (Unit worker : workersNeaby.list()) {
			if (worker.getOrderTarget() == mineral) {
				howMany++;
			}
		}
		return howMany;
	}
	
}
