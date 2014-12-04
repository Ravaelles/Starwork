package starwork.presidents;

import starwork.economy.building.BuildingMajor;
import starwork.economy.workers.WorkerMajor;
import starwork.economy.workers.gathering.GathererSergeant;
import starwork.main.Painter;
import starwork.main.Starwork;
import starwork.units.UnitCounter;

public class TimePresident {

	private static int frame = 0;

	// =========================================================

	public static void frame() {
		if (frame % 5 == 0) {
			WorkerMajor.giveOrders();
		}
		if (frame % 4 == 0) {
			GathererSergeant.giveOrders();
		}
		if (frame % 6 == 0) {
			UnitCounter.recalculateUnits();
		}
		if (frame % 9 == 0) {
			BuildingMajor.giveOrders();
		}

		// =========================================================

		Painter.paintAll();

		frame++;
	}

	public static int getTimeFrames() {
		return Starwork.getGame().getFrameCount();
	}

	public static int getTimeSeconds() {
		return Starwork.getGame().getFrameCount() / 24;
	}

}
