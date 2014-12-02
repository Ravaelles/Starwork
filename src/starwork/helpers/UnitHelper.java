package starwork.helpers;

import java.lang.reflect.Field;
import java.util.ArrayList;

import starwork.main.Starwork;
import starwork.units.UnitDamages;
import bwapi.Unit;
import bwapi.UnitType;
import bwapi.WeaponType;

public class UnitHelper {

	public static String toStringFull(Unit unit) {
		String string = "";

		string += "#" + unit.getID() + ", ";
		string += "[" + unit.getPosition() + "], ";
		string += unit.getType();
		string += ", Order:" + unit.getOrder();
		string += ", OrderTarget:" + unit.getOrderTarget();
		string += ", OrderTargetPosition" + unit.getOrderTargetPosition();
		// string += ", :" + unit.get;
		// string += ", :" + unit.get;
		// string += ", :" + unit.get;
		// string += ", :" + unit.get;

		return string;
	}

	// =========================================================

	public static boolean isType(Unit unit, UnitType type) {
		return unit.getType() == type;
	}

	public static boolean isType(Unit unit, UnitType[] types) {
		for (UnitType type : types) {
			if (type == unit.getType()) {
				return true;
			}
		}
		return false;
	}

	public static boolean isBuilding(Unit unit) {
		return unit.getType().isBuilding();
	}

	// =========================================================

	public static int getHP(Unit unit) {
		return unit.getHitPoints();
	}

	public static int getMaxHP(Unit unit) {
		return unit.getType().maxHitPoints();
	}

	public static String getNameShort(Unit unit) {
		String name = unit.getType().c_str().replace("Zerg", "")
				.replace("Terran", "").replace("Protoss", "");
		if (name.length() >= 15) {
			return name.substring(0, 15);
		} else {
			return name;
		}
	}

	public static boolean isBunker(Unit unit) {
		return isType(unit, UnitType.Terran_Bunker);
	}

	// public static UnitTypes getUnitTypesObject(Unit unit) {
	// return getUnitTypesByID(ID);
	// }

	public static boolean isGhost(Unit unit) {
		return isType(unit, UnitType.Terran_Ghost);
	}

	public static boolean isGoliath(Unit unit) {
		return isType(unit, UnitType.Terran_Goliath);
	}

	public static boolean isArmy(Unit unit) {
		return !isBuilding(unit) && !isWorker(unit);
	}

	public static boolean isWorker(Unit unit) {
		return isType(unit, UnitType.Protoss_Probe)
				|| isType(unit, UnitType.Zerg_Drone)
				|| isType(unit, UnitType.Terran_SCV);
	}

	public static boolean isBase(Unit unit) {
		return isType(unit, UnitType.Terran_Command_Center)
				|| isType(unit, UnitType.Protoss_Nexus)
				|| isType(unit, UnitType.Zerg_Hatchery)
				|| isType(unit, UnitType.Zerg_Lair)
				|| isType(unit, UnitType.Zerg_Hive);
	}

	public static boolean isOnGeyser(Unit unit) {
		return isType(unit, UnitType.Terran_Refinery)
				|| isType(unit, UnitType.Resource_Vespene_Geyser)
				|| isType(unit, UnitType.Protoss_Assimilator)
				|| isType(unit, UnitType.Zerg_Extractor);
	}

	// @Override
	// public static String toString(Unit unit) {
	// return "UnitType [name=" + name + ", maxHitPoints=" + maxHitPoints +
	// ", armor=" + armor
	// + ", mineralPrice=" + mineralPrice + ", gasPrice=" + gasPrice +
	// ", buildTime="
	// + buildTime + "]";
	// }

	public static boolean isPylon(Unit unit) {
		return isType(unit, UnitType.Protoss_Pylon);
	}

	public static boolean isPhotonCannon(Unit unit) {
		return isType(unit, UnitType.Protoss_Photon_Cannon);
	}

	public static boolean isBarracks(Unit unit) {
		return isType(unit, UnitType.Terran_Barracks);
	}

	public static boolean isFactory(Unit unit) {
		return isType(unit, UnitType.Terran_Factory);
	}

	public static boolean isGateway(Unit unit) {
		return isType(unit, UnitType.Protoss_Gateway);
	}

	public static boolean isDragoon(Unit unit) {
		return isType(unit, UnitType.Protoss_Dragoon);
	}

	public static boolean isSunkenColony(Unit unit) {
		return isType(unit, UnitType.Zerg_Sunken_Colony);
	}

	public static boolean isSporeColony(Unit unit) {
		return isType(unit, UnitType.Zerg_Spore_Colony);
	}

	public static boolean isLarvaOrEgg(Unit unit) {
		return isType(unit, new UnitType[] { UnitType.Zerg_Larva,
				UnitType.Zerg_Egg, });
	}

	public static boolean isLurker(Unit unit) {
		return isType(unit, UnitType.Zerg_Lurker);
	}

	public static boolean isMutalisk(Unit unit) {
		return isType(unit, UnitType.Zerg_Mutalisk);
	}

	public static boolean isTank(Unit unit) {
		return isType(unit, new UnitType[] {
				UnitType.Terran_Siege_Tank_Siege_Mode,
				UnitType.Terran_Siege_Tank_Tank_Mode, });
	}

	public static boolean isTankSieged(Unit unit) {
		return isType(unit, UnitType.Terran_Siege_Tank_Siege_Mode);
	}

	public static boolean isTerranInfantry(Unit unit) {
		return isType(unit, new UnitType[] { UnitType.Terran_Marine,
				UnitType.Terran_Firebat, UnitType.Terran_Medic,
				UnitType.Terran_Ghost, });
	}

	public static boolean canUseStimpacks(Unit unit) {
		return isType(unit, new UnitType[] { UnitType.Terran_Marine,
				UnitType.Terran_Firebat, UnitType.Terran_Ghost, });
	}

	public static boolean isReaver(Unit unit) {
		return isType(unit, UnitType.Protoss_Reaver);
	}

	public static boolean isHighTemplar(Unit unit) {
		return isType(unit, UnitType.Protoss_High_Templar);
	}

	public static boolean isDarkTemplar(Unit unit) {
		return isType(unit, UnitType.Protoss_Dark_Templar);
	}

	public static boolean isZergling(Unit unit) {
		return isType(unit, UnitType.Zerg_Zergling);
	}

	public static boolean isMissileTurret(Unit unit) {
		return isType(unit, UnitType.Terran_Missile_Turret);
	}

	public static boolean isObserver(Unit unit) {
		return isType(unit, UnitType.Protoss_Observer);
	}

	public static boolean isScienceVessel(Unit unit) {
		return isType(unit, UnitType.Terran_Science_Vessel);
	}

	public static boolean isStarport(Unit unit) {
		return isType(unit, UnitType.Terran_Starport);
	}

	public static boolean isScienceFacility(Unit unit) {
		return isType(unit, UnitType.Terran_Science_Facility);
	}

	public static boolean isCarrier(Unit unit) {
		return isType(unit, UnitType.Protoss_Carrier);
	}

	public static boolean isInterceptor(Unit unit) {
		return isType(unit, UnitType.Protoss_Interceptor);
	}

	public static boolean isVulture(Unit unit) {
		return isType(unit, UnitType.Terran_Vulture);
	}

	public static boolean isWraith(Unit unit) {
		return isType(unit, UnitType.Terran_Wraith);
	}

	public static boolean isHydralisk(Unit unit) {
		return isType(unit, UnitType.Zerg_Hydralisk);
	}

	public static boolean isFirebat(Unit unit) {
		return isType(unit, UnitType.Terran_Firebat);
	}

	public static boolean isSpiderMine(Unit unit) {
		return isType(unit, UnitType.Terran_Vulture_Spider_Mine);
	}

	public static boolean isSCV(Unit unit) {
		return isType(unit, UnitType.Terran_SCV);
	}

	public static boolean isMarine(Unit unit) {
		return isType(unit, UnitType.Terran_Marine);
	}

	public static boolean isSupplyDepot(Unit unit) {
		return isType(unit, UnitType.Terran_Supply_Depot);
	}

	public static boolean isFleetBeacon(Unit unit) {
		return isType(unit, UnitType.Protoss_Fleet_Beacon);
	}

	public static boolean isMedic(Unit unit) {
		return isType(unit, UnitType.Terran_Medic);
	}

	public static WeaponType getGroundWeapon(Unit unit) {
		return unit.getType().groundWeapon();
	}

	public static WeaponType getGroundWeapon(UnitType type) {
		return type.groundWeapon();
	}

	public static WeaponType getAirWeapon(UnitType type) {
		return type.airWeapon();
	}

	public static WeaponType getAirWeapon(Unit unit) {
		return unit.getType().airWeapon();
	}

	public static boolean canAttackGround(Unit unit) {
		return getGroundWeapon(unit) != WeaponType.None;
	}

	public static boolean canAttackAir(Unit unit) {
		return getAirWeapon(unit) != WeaponType.None;
	}

	public static int getGroundAttackUnnormalized(Unit unit) {
		return UnitDamages.getGroundAttackUnnormalized(unit.getType());
	}

	public static double getGroundAttackNormalized(Unit unit) {
		return UnitDamages.getGroundAttackNormalized(unit.getType());
	}

	public static boolean canHaveAddOn(Unit unit) {
		return isFactory(unit) || isStarport(unit) || isBase(unit)
				|| isScienceFacility(unit);
	}

	// private static UnitTypes _unitTypes = null;
	//
	// public static UnitTypes getUnitTypes(Unit unit) {
	// if (_unitTypes == null) {
	// _unitTypes = getUnitTypesByID(ID);
	// }
	// return _unitTypes;
	// }

	public static boolean isDefensiveBuilding(Unit unit) {
		return isBuilding(unit) && isBunker(unit) || isPhotonCannon(unit)
				|| isSunkenColony(unit);
	}

	// =========================================================

	public static ArrayList<UnitType> getAllUnitTypes() {
		ArrayList<UnitType> allTypes = new ArrayList<>();

		try {
			for (Field field : UnitType.class.getDeclaredFields()) {
				// System.out.println(field);
				// UnitType unitType = (UnitType) field.get(UnitType.class);
				// field.setAccessible(true);
				// Object object = field.get(null);
				// System.out.println(UnitType.class.getField(field.getName())
				// .getType());
				// UnitType unitType =
				// UnitType.class.getField(field.getName()).;
				// System.out.println(unitType);

				// System.out.println(field.get(null));
				// System.out.println("TEST: " + UnitType.Terran_Academy);
				UnitType unitType = (UnitType) field.get(null);
				allTypes.add(unitType);
			}
		} catch (Exception e) {
			// Ignore
		}

		return allTypes;
	}

	public static boolean isPlayerUnit(Unit unit) {
		return unit.getPlayer() == Starwork.getSelf();
	}

}
