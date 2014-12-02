package starwork.units;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import starwork.helpers.UnitHelper;
import starwork.utils.RUtilities;
import bwapi.UnitType;

public class UnitDamages {

	private static HashMap<UnitType, Integer> groundAttackValues = new HashMap<>();
	private static HashMap<UnitType, Double> groundAttackValuesPerSec = new HashMap<>();

	public static void rememberUnitDamageValues() {
		for (UnitType unitType : UnitHelper.getAllUnitTypes()) {
			// System.out.println(unitType);
			if (!unitType.c_str().startsWith("Terran")
					&& !unitType.c_str().startsWith("Protoss")
					&& !unitType.c_str().startsWith("Zerg")) {
				continue;
			}

			try {
				int groundAttackValue = UnitHelper.getGroundWeapon(unitType)
						.damageAmount();

				if (unitType == UnitType.Protoss_Zealot
						|| unitType == UnitType.Terran_Firebat) {
					groundAttackValue *= 2;
				}

				if (unitType == UnitType.Zerg_Sunken_Colony) {
					groundAttackValue /= 2;
				}

				groundAttackValues.put(unitType, groundAttackValue);

				if (groundAttackValue == 0) {
					groundAttackValuesPerSec.put(unitType, (double) 0);
				} else {
					double groundAttackValueNormalized = ((double) 30
							* groundAttackValue / UnitHelper.getGroundWeapon(
							unitType).damageCooldown());
					groundAttackValueNormalized = Double.parseDouble(String
							.format("%.1f", groundAttackValueNormalized,
									Locale.ENGLISH).replace(',', '.'));
					groundAttackValuesPerSec.put(unitType,
							groundAttackValueNormalized);
				}

			} catch (Exception e) {
				System.err.println(unitType.c_str() + ": " + e.getMessage());
			}
		}

		// displayUnitDamages();
	}

	public static void displayUnitDamages() {
		System.out.println("");
		System.out.println("=====================================");
		System.out.println("ATTACK VALUES OF UNITS:");

		Map<UnitType, Integer> sorted = RUtilities.sortByValue(
				groundAttackValues, false);
		for (UnitType type : sorted.keySet()) {
			System.out.println(type.c_str() + ":  " + sorted.get(type));
		}

		System.out.println("");

		Map<UnitType, Double> sorted2 = RUtilities.sortByValue(
				groundAttackValuesPerSec, false);
		System.out.println("ATTACK VALUES OF UNITS PER SEC:");
		for (UnitType type : sorted2.keySet()) {
			System.out.println(type.c_str() + ":  " + sorted2.get(type));
		}

		System.out.println("");

		System.out.println("RATIO DAMAGE / COST:");
		HashMap<UnitType, Double> pricesMap = new HashMap<>();
		for (UnitType type : sorted2.keySet()) {
			double cost = type.mineralPrice() + type.gasPrice() * 1.5;
			double value = sorted2.get(type) < 1 ? 0 : (1000 * sorted2
					.get(type) / cost);
			pricesMap.put(type, value);
		}

		Map<UnitType, Double> sorted3 = RUtilities
				.sortByValue(pricesMap, false);
		for (UnitType type : sorted3.keySet()) {
			System.out.println(type.c_str() + ":  " + sorted3.get(type));
		}
	}

	public static int getGroundAttackUnnormalized(UnitType type) {
		return groundAttackValues.get(type);
	}

	public static double getGroundAttackNormalized(UnitType type) {
		if (groundAttackValuesPerSec.containsKey(type)) {
			// System.out.println("#############################");
			// System.out.println(type + " type:" + type.getName());
			// System.out.println("#############################");
			return groundAttackValuesPerSec.get(type);
		} else {
			return 0;
		}
	}

}
