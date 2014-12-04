package starwork.economy;

import starwork.main.Starwork;

public class EconomyMinister {

	public static boolean canAfford(int minerals) {
		return Starwork.getSelf().minerals() >= minerals;
	}

	public static boolean canAfford(int minerals, int gas) {
		return Starwork.getSelf().minerals() >= minerals
				&& Starwork.getSelf().gas() >= gas;
	}

	public static boolean hasSupply(int supply) {
		return getFreeSupply() >= supply;
	}

	public static int getFreeSupply() {
		return Starwork.getSelf().supplyTotal()
				- Starwork.getSelf().supplyUsed();
	}

	public static int getTotalSupply() {
		return Starwork.getSelf().supplyTotal();
	}
}
