package starwork.economy.workers;

import starwork.units.UnitDuties;
import starwork.units.UnitDuty;
import starwork.units.Units;
import bwapi.Unit;

public abstract class Workers {

	// private ArrayList<Unit> assignedWorkers = new ArrayList<>();
	private Units assignedWorkers = new Units();

	public Workers() {
	}

	public void assignWorker(Unit worker, UnitDuty duty) {
		assignedWorkers.addUnit(worker);
		UnitDuties.setUnitDuty(worker, duty);
		UnitDuties.setUnitAssignment(worker, assignedWorkers);
	}

	public void removeAssignedWorker(Unit worker) {
		assignedWorkers.removeUnit(worker);
	}

	public Units getAssignedWorkers() {
		return assignedWorkers;
	}

}
