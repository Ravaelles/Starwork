package starwork.map;

import bwapi.Position;

public abstract class AbstractPosition {

	public abstract int getX();

	public abstract int getY();

	// =========================================================

	public int getTx() {
		return getX() / 32;
	}

	public int getTy() {
		return getY() / 32;
	}

	public double distanceTo(AbstractPosition otherPosition) {
		return distanceTo(otherPosition.getX(), otherPosition.getY());
	}

	public double distanceTo(int x, int y) {
		double dx = x - getX();
		double dy = y - getY();
		return Math.sqrt(dx * dx + dy * dy);
	}

	public Position translate(int dTx, int dTy) {
		return new Position(getTx() + dTx, getTy() + dTy);
	}

}
