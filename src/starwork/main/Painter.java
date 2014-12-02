package starwork.main;

import starwork.helpers.UnitHelper;
import starwork.presidents.TimePresident;
import starwork.units.SelectUnits;
import starwork.utils.CodeProfiler;
import starwork.utils.RUtilities;
import bwapi.Color;
import bwapi.Game;
import bwapi.Position;
import bwapi.Unit;

public class Painter {

	public static final boolean FULL_DEBUG = true;
	public static boolean errorOcurred = false;
	public static String errorOcurredDetails = "";

	private static int messageCounter = 1;
	private static int debugMessageCounter = 1;
	private static int mainMessageRowCounter = 0;

	public static int ourDeaths = 0;
	public static int enemyDeaths = 0;

	private static Starwork starwork = null;
	private static Game game = null;

	// =========================================================

	private static void paintDebugMessages(Starwork starwork) {
		debugMessageCounter = 0;

		// String armyRelativeStrenghString;
		// int relativeStrength = StrengthComparison.getOurRelativeStrength();
		// if (relativeStrength != -1) {
		// armyRelativeStrenghString = RUtilities.assignStringForValue(
		// relativeStrength,
		// 130,
		// 70,
		// new String[] { BWColor.getToStringHex(Color.Red),
		// BWColor.getToStringHex(Color.Yellow),
		// BWColor.getToStringHex(Color.Green) })
		// + relativeStrength + "%";
		// } else {
		// armyRelativeStrenghString = BWColor.getToStringHex(Color.Grey)
		// + "Enemy forces unknown";
		// }
		//
		// paintDebugMessage(starwork, BWColor.getToStringHex(Color.White) +
		// "Our army vs. Enemy army: ",
		// armyRelativeStrenghString);

		// =========================================================

		// String supplyString = StrengthComparison.getOurSupply()
		// + " / "
		// + (StrengthComparison.getEnemySupply() == 0 ? "Enemy supply unknown"
		// : StrengthComparison.getEnemySupply());
		// String supplyColor = BWColor.getToStringHex(Color.Grey);
		// if (StrengthComparison.getEnemySupply() != 0
		// && StrengthComparison.getOurSupply() >
		// StrengthComparison.getEnemySupply()) {
		// supplyColor = BWColor.getToStringHex(Color.Green);
		// } else if (StrengthComparison.getOurSupply() <
		// StrengthComparison.getEnemySupply()) {
		// supplyColor = BWColor.getToStringHex(Color.Red);
		// }
		//
		// paintDebugMessage(starwork, BWColor.getToStringHex(Color.White) +
		// "Our & enemy supply used: ",
		// supplyColor + supplyString);

		// =========================================================
		// paintDebugMessage(starwork, BWColor.getToStringHex(Color.Grey) +
		// "======================",
		// "=======================");
		//
		// paintDebugMessage(
		// starwork,
		// BWColor.getToStringHex(Color.White) + "Known enemy buildings: ",
		// (MapExploration.getEnemyBuildingsDiscovered().isEmpty() ? (BWColor
		// .getToStringHex(Color.Red) + "None") : (BWColor
		// .getToStringHex(Color.Green) + "Yes")));
		//
		// paintDebugMessage(
		// starwork,
		// BWColor.getToStringHex(Color.White) + "Calculated enemy position: ",
		// (MapExploration.getCalculatedEnemyBaseLocation() == null ? (BWColor
		// .getToStringHex(Color.Red) + "Unknown") : (BWColor
		// .getToStringHex(Color.Green) + MapExploration
		// .getCalculatedEnemyBaseLocation().toStringLocation())));

		// =========================================================

		// paintDebugMessage(starwork, "Circling phase: ",
		// ExplorerCirclingEnemyBase.get_circlingEnemyBasePhase());

		// MapPoint nextBase = TerranCommandCenter.findTileForNextBase(false);
		// if (nextBase != null) {
		// paintDebugMessage(starwork, "Next base",
		// "X: " + nextBase.getTx() + ", Y: " + nextBase.getTy());
		// }
	}

	public static void paintAll() {
		Painter.starwork = Starwork.getInstance();
		Painter.game = Starwork.getGame();

		CodeProfiler.startMeasuring("Painting");

		int oldMainMessageRowCounter = mainMessageRowCounter;
		mainMessageRowCounter = 0;

		// MapPoint assimilator = Constructing.findTileForAssimilator();
		// if (assimilator != null) {
		// starwork.getBwapi().drawBox(assimilator.getX(), assimilator.getY(),
		// 3 * 32, 2 * 32, Color.Teal, false);
		// }

		if (FULL_DEBUG) {
			paintTimeConsumption(starwork);
			paintOffensivePoint(starwork);
			paintBuildingsToConstructPosition(starwork);
			paintSpeculatedEnemyTanksPositions();
		}
		paintUnitsDetails(starwork);

		if (FULL_DEBUG) {
			paintValuesOverUnits(starwork);
		}

		// // Draw regions
		// for (Region region : starwork.getBwapi().getMap().getRegions()) {
		// int[] bounds = region.getCoordinates();
		// starwork.getBwapi().drawBox(bounds[0] - bounds[2],
		// bounds[1] - bounds[3], 2 * bounds[2], 2 * bounds[3],
		// Color.Teal, false);
		// starwork.getBwapi()
		// .drawText(
		// region.getCenterX(),
		// region.getCenterY(),
		// String.format("Region [%d]", region
		// .getChokePoints().size()), false);
		// }

		// // Draw next building place
		// MapPoint buildTile = ProtossPylon.findTileNearPylonForNewBuilding();
		// if (buildTile != null) {
		// starwork.getBwapi().drawCircle(buildTile.getX() - 64,
		// buildTile.getX() - 48, 50, Color.Teal, false);
		// }

		// starwork.getBwapi()
		// .drawText(
		// region.getCenterX(),
		// region.getCenterY(),
		// String.format("Region [%d]", region
		// .getChokePoints().size()), false);

		// Draw choke points
		paintChokePoints(starwork);

		// Draw where to attack
		paintAttackLocation(starwork);

		// Statistics
		paintStatistics(starwork);

		// Aditional messages for debug purpose
		paintDebugMessages(starwork);

		if (Painter.errorOcurred) {
			String string = "!!! EXCEPTION (" + errorOcurredDetails + ") !!!";
			Starwork.getGame().printf(string);
			// drawText(new Point(320 - string.length() * 3, 100),
			// BWColor.getToStringHex(Color.Red) + string, true);
		}

		// ========
		mainMessageRowCounter = oldMainMessageRowCounter;

		CodeProfiler.endMeasuring("Painting");
	}

	// =========================================================

	private static final int timeConsumptionLeftOffset = 575;
	private static final int timeConsumptionTopOffset = 30;
	private static final int timeConsumptionBarMaxWidth = 50;
	private static final int timeConsumptionBarHeight = 14;
	private static final int timeConsumptionYInterval = 16;

	private static void paintSpeculatedEnemyTanksPositions() {
		// for (MapPointInstance enemyTank :
		// EnemyTanksManager.getSpeculatedTankPositions()) {
		//
		// // Draw base position as rectangle
		// starwork.getBwapi().drawBox(enemyTank.getX() - 12, enemyTank.getY() -
		// 12,
		// enemyTank.getX() + 25, enemyTank.getY() + 25, Color.Red, false);
		//
		// // Draw string "Base"
		// starwork.getBwapi().drawText(enemyTank.getX() - 11, enemyTank.getY()
		// - 6,
		// BWColor.getToStringHex(Color.Red) + "Tank", false);
		// }
	}

	private static void paintOffensivePoint(Starwork starwork) {
		// MapPoint offensivePoint = TerranOffensiveBunker.getOffensivePoint();
		// if (offensivePoint != null) {
		//
		// // Draw base position as rectangle
		// starwork.getBwapi().drawBox(offensivePoint.getX(),
		// offensivePoint.getY(),
		// offensivePoint.getX() + 2 * 32, offensivePoint.getY() + 2 * 32,
		// Color.Orange,
		// false, false);
		//
		// // Draw string
		// starwork.getBwapi().drawText(offensivePoint.getX() + 3,
		// offensivePoint.getY() + 3,
		// BWColor.getToStringHex(Color.Orange) + "Offensive", false);
		// }
	}

	private static void paintTimeConsumption(Starwork starwork) {
		int counter = 0;
		double maxValue = RUtilities.getMaxElement(CodeProfiler
				.getAspectsTimeConsumption().values());

		// System.out.println();
		// for (double val : TimeMeasurer.getAspectsTimeConsumption().values())
		// {
		// System.out.println("   " + val);
		// }

		// System.out.println(TimeMeasurer.getAspectsTimeConsumption().keySet().size());
		for (String aspectTitle : CodeProfiler.getAspectsTimeConsumption()
				.keySet()) {
			int x = timeConsumptionLeftOffset;
			int y = timeConsumptionTopOffset + timeConsumptionYInterval
					* counter++;

			int value = CodeProfiler.getAspectsTimeConsumption()
					.get(aspectTitle).intValue();

			// Draw aspect time consumption bar
			int barWidth = (int) (timeConsumptionBarMaxWidth * value / maxValue);
			if (barWidth < 3) {
				barWidth = 3;
			}
			if (barWidth > timeConsumptionBarMaxWidth) {
				barWidth = timeConsumptionBarMaxWidth;
			}
			// System.out.println("   " + aspectTitle + " x:" + x + ", y:" + y +
			// "  ## " + barWidth);
			paintBox(x, y, x + barWidth, y + timeConsumptionBarHeight,
					Color.White, true);
			paintBox(x, y, x + timeConsumptionBarMaxWidth, y
					+ timeConsumptionBarHeight, Color.Black, false);

			// Draw aspect label
			// Color.Yellow +
			paintText(x + 2, y - 1, aspectTitle);
		}
	}

	private static void paintValuesOverUnits(Starwork starwork) {
		// String text;
		// String cooldown;
		// double strength;
		//
		// for (Unit unit : starwork.getSelf().getUnits()) {
		// UnitType type = unit.getType();
		// if (type.isBuilding() || type.isSpiderMine() || type.isWorker()) {
		// continue;
		// }
		//
		// // ==========================
		// // Strength evaluation
		// // strength = StrengthEvaluator.calculateStrengthRatioFor(unit);
		// strength = unit.getStrengthRatio();
		// if (!type.isBuilding() && strength !=
		// StrengthRatio.STRENGTH_RATIO_FULLY_SAFE) {
		// strength -= 1; // make +/- values display
		// text = (strength > 0 ? (BWColor.getToStringHex(Color.Green) + "+") :
		// (BWColor
		// .getToStringHex(Color.Red) + "")) + String.format("%.1f", strength);
		// paintText(unit.getX() - 7, unit.getY() + 30, text, false);
		// }
		//
		// // ==========================
		// // Cooldown
		// if (unit.getGroundWeaponCooldown() > 0) {
		// int cooldownWidth = 20;
		// int cooldownHeight = 4;
		// int cooldownLeft = unit.getX() - cooldownWidth / 2;
		// int cooldownTop = unit.getY() + 23;
		// cooldown = BWColor.getToStringHex(Color.Yellow) + "("
		// + unit.getGroundWeaponCooldown() + ")";
		//
		// // Paint box
		// int cooldownProgress = cooldownWidth * unit.getGroundWeaponCooldown()
		// / (unit.getType().getGroundWeapon().getDamageCooldown() + 1);
		// paintBox(cooldownLeft, cooldownTop, cooldownLeft + cooldownProgress,
		// cooldownTop + cooldownHeight, Color.Red, true);
		//
		// // Paint box borders
		// paintBox(cooldownLeft, cooldownTop, cooldownLeft + cooldownWidth,
		// cooldownTop
		// + cooldownHeight, Color.Black, false);
		//
		// // Paint label
		// paintText(cooldownLeft + cooldownWidth - 4, cooldownTop, cooldown,
		// false);
		// }
		// }
	}

	private static void paintAttackLocation(Starwork starwork) {
		// JNIBWAPI game = starwork.getBwapi();
		// if (StrategyManager.getTargetUnit() != null) {
		// paintCircle(StrategyManager.getTargetUnit().getX(), StrategyManager
		// .getTargetUnit().getY(), 33, Color.Red, false);
		// paintCircle(StrategyManager.getTargetUnit().getX(), StrategyManager
		// .getTargetUnit().getY(), 32, Color.Red, false);
		// paintCircle(StrategyManager.getTargetUnit().getX(), StrategyManager
		// .getTargetUnit().getY(), 3, Color.Red, true);
		// }
		//
		// if (NukeHandling.nuclearDetectionPoint != null) {
		// MapPoint nuclearPoint = NukeHandling.nuclearDetectionPoint;
		// paintCircle(nuclearPoint.getX(), nuclearPoint.getY(), 20, Color.Red,
		// false,
		// false);
		// paintCircle(nuclearPoint.getX(), nuclearPoint.getY(), 18, Color.Red,
		// false,
		// false);
		// paintCircle(nuclearPoint.getX(), nuclearPoint.getY(), 16, Color.Red,
		// false,
		// false);
		// paintCircle(nuclearPoint.getX(), nuclearPoint.getY(), 14, Color.Red,
		// false,
		// false);
		// }
	}

	private static void paintBuildingsToConstructPosition(Starwork starwork) {
		// MapPoint buildingPlace;
		//
		// // =========================================================
		// // Paint next BASE position
		// // building = TerranCommandCenter.findTileForNextBase(false);
		// buildingPlace = TerranCommandCenter.get_cachedNextBaseTile();
		// if (buildingPlace != null) {
		//
		// // Draw base position as rectangle
		// starwork.getBwapi().drawBox(buildingPlace.getX(),
		// buildingPlace.getY(),
		// buildingPlace.getX() + 4 * 32, buildingPlace.getY() + 3 * 32,
		// Color.Teal,
		// false, false);
		//
		// // Draw string "Base"
		// starwork.getBwapi().drawText(buildingPlace.getX() + 3,
		// buildingPlace.getY() + 3,
		// BWColor.getToStringHex(Color.Green) + "Next base", false);
		//
		// Unit baseBuilder = BuildingManager.getNextBaseBuilder();
		// String builder = baseBuilder != null ?
		// (BWColor.getToStringHex(Color.White) + "#" + baseBuilder
		// .getID()) : (BWColor.getToStringHex(Color.Red) + "Unassigned");
		//
		// // Draw string with builder ID
		// starwork.getBwapi().drawText(buildingPlace.getX() + 3,
		// buildingPlace.getY() + 15,
		// BWColor.getToStringHex(Color.Green) + "Builder ID: " + builder,
		// false);
		// }
		//
		// // =========================================================
		// // Paint next BUILDING position
		// buildingPlace =
		// Constructing.findTileForStandardBuilding(UnitTypes.Terran_Barracks);
		// if (buildingPlace != null) {
		//
		// // Draw base position as rectangle
		// starwork.getBwapi().drawBox(buildingPlace.getX(),
		// buildingPlace.getY(),
		// buildingPlace.getX() + 4 * 32, buildingPlace.getY() + 3 * 32,
		// Color.White,
		// false, false);
		//
		// // Draw string
		// starwork.getBwapi().drawText(buildingPlace.getX() + 3,
		// buildingPlace.getY() + 3,
		// BWColor.getToStringHex(Color.Grey) + "Potential barracks", false);
		// }
		//
		// // =========================================================
		// // Paint next BUNKER position
		// if (TerranBunker.getNumberOfUnits() == 0) {
		// MapPoint building = null;
		// building = TerranBunker.findTileForBunker();
		// if (building != null) {
		// starwork.getBwapi().drawBox(building.getX(), building.getY(),
		// building.getX() + 2 * 32,
		// building.getY() + 2 * 32, Color.Teal, false);
		// starwork.getBwapi()
		// .drawText(building.getX() + 10, building.getY() + 30, "Bunker",
		// false);
		// }
		// }
	}

	private static void paintChokePoints(Starwork starwork) {
		// for (ChokePoint choke : MapExploration.getChokePoints()) {
		// starwork.getBwapi().drawCircle(choke.getCenterX(),
		// choke.getCenterY(),
		// (int) choke.getRadius(), Color.Black, false);
		// }
	}

	private static void paintUnitsDetails(Starwork starwork) {
		for (Unit u : starwork.getSelf().getUnits()) {
			boolean isBuilding = u.getType().isBuilding();
			if (FULL_DEBUG && !isBuilding && u.isCompleted()) {
				paintUnit(starwork, u);
			}

			// IS BUILDING: display action name that's currently pending.
			else if (isBuilding) {
				paintBuilding(starwork, u);
			}
		}
	}

	private static void paintUnit(Starwork starwork, Unit unit) {
		Position pos = unit.getPosition();

		// Paint go to place for unit, if manually specified
		// if (u.getPainterGoTo() != null) {
		// MapPoint goTo = u.getPainterGoTo();
		// game.drawLine(u.getX(), u.getY(), goTo.getX(), goTo.getY(),
		// Color.Grey, false);
		// }

		if (unit.isGatheringMinerals()) {
			paintCircle(pos.getX(), pos.getY(), 12, Color.Blue, false);
		} else if (unit.isGatheringGas()) {
			paintCircle(pos.getX(), pos.getY(), 12, Color.Green, false);
		} else if (unit.isMoving() && !unit.isConstructing()) {
			paintCircle(pos.getX(), pos.getY(), 12, Color.Grey, false);

			// if (PositionHelper.getDistanceBetween(pos,
			// unit.getTargetPosition()) <= 15) {
			// game.drawLine(pos.getX(), pos.getY(), pos.getTargetX(),
			// pos.getTargetY(), Color.Grey,
			// false);
			// }
		} else if (unit.isRepairing()) {
			paintCircle(pos.getX(), pos.getY(), 12, Color.Purple, false);
			paintCircle(pos.getX(), pos.getY(), 11, Color.Purple, false);
			paintCircle(pos.getX(), pos.getY(), 10, Color.Purple, false);
		} else if (unit.isConstructing()) {
			paintCircle(pos.getX(), pos.getY(), 12, Color.Orange, false);
			paintCircle(pos.getX(), pos.getY(), 11, Color.Orange, false);
		} else if (unit.isStuck()) {
			paintCircle(pos.getX(), pos.getY(), 12, Color.Teal, false);
			paintCircle(pos.getX(), pos.getY(), 11, Color.Teal, false);
			paintCircle(pos.getX(), pos.getY(), 10, Color.Teal, false);
			paintCircle(pos.getX(), pos.getY(), 9, Color.Teal, false);
		}

		// ATTACKING: Display red circle around unit and paint a
		// line to the target
		else if (unit.isAttacking()) {
			paintCircle(pos.getX(), pos.getY(), 12, Color.Red, false);
			paintCircle(pos.getX(), pos.getY(), 11, Color.Red, false);
		}

		// HEALED unit, draw Red Cross on white background
		else if (unit.isBeingHealed()) {
			paintBox(pos.getX() - 8, pos.getY() - 8, pos.getX() + 8,
					pos.getY() + 8, Color.White, true);
			paintBox(pos.getX() - 5, pos.getY() - 2, pos.getX() + 5,
					pos.getY() + 2, Color.Red, true);
			paintBox(pos.getX() - 2, pos.getY() - 5, pos.getX() + 2,
					pos.getY() + 5, Color.Red, true);
		}

		// IDLE unit, draw question mark
		else if (unit.isIdle() && !unit.getType().isBuilding()) {
			paintTextMap(unit, getToStringHex(Color.Yellow) + "?");
		}

		// =========================================================
		// Worker ID
		if (unit.getType().isWorker()) {
			paintTextMap(unit, getToStringHex(Color.Grey) + "#" + unit.getID());
		}

		// =========================================================

		// ACTION LABEL: display action like #RUN, #LOAD
		// if (unit.hasAiOrder()) {
		// paintText(pos.getX() - pos.getAiOrderString().length() * 3,
		// pos.getY(),
		// BWColor.getToStringHex(Color.White) + pos.getAiOrderString(), false);
		// }

		// FLYERS: paint nearest AntiAir enemy unit.
		// if (unit.getType().isFlyer()) {
		// if (pos.getEnemyNearbyAA() != null) {
		// int enemyX = pos.getEnemyNearbyAA().getX();
		// int enemyY = pos.getEnemyNearbyAA().getY();
		// paintCircle(enemyX, enemyY, 20, Color.Yellow, false);
		// game.drawLine(pos.getX(), pos.getY(), enemyX, enemyY, Color.Yellow,
		// false);
		// }
		// }

		// =========================================================
		// Paint unit connection which represent enemy unit that our unit is
		// RUNNING from
		// if (pos.getLastTimeRunFromEnemyUnit() != null
		// && starwork.getTimeSeconds() + 2 <=
		// pos.getLastTimeRunFromEnemyTime()) {
		// Unit enemyUnit = pos.getLastTimeRunFromEnemyUnit();
		// game.drawLine(pos.getX(), pos.getY(), enemyUnit.getX(),
		// enemyUnit.getY(), BWColor.BROWN,
		// false);
		// }
	}

	private static void paintBuilding(Starwork starwork, Unit u) {
		Position pos = u.getPosition();

		// Paint HEALTH for BUNKERS
		if (UnitHelper.isDefensiveBuilding(u)) {
			paintBuildingHealth(u);
		}

		// CONSTRUCTING: display building name
		if (u.getType().isBuilding()
				&& (u.isConstructing() || u.isBeingConstructed())) {
			paintConstructionProgress(u);
		}

		// TRAINING
		if (u.isTraining()) {
			paintTraining(u);
		}

		// int enemiesNearby = starwork.countUnitsEnemyInRadius(u, 11);
		// if (enemiesNearby > 0) {
		// String string = enemiesNearby + " enemies";
		// paintText(u.getX() - string.length() * 4, u.getY(),
		// BWColor.getToStringHex(Color.Red) + string, false);
		// }
		// if (u.getType().isBunker()) {
		// int repairers =
		// BuildingRepairManager.countNumberOfRepairersForBuilding(u);
		// if (repairers > 0) {
		// String repairersString = repairers + " repairers";
		// paintText(u.getX() - repairersString.length() * 4, u.getY() + 10,
		// BWColor.getToStringHex(Color.Orange) + repairersString, false);
		// }
		//
		// int specialCaseRepairers =
		// BuildingRepairManager.getSpecialCaseRepairers(u);
		// if (specialCaseRepairers > 0) {
		// String repairersString = specialCaseRepairers + " required";
		// paintText(u.getX() - repairersString.length() * 4, u.getY() + 20,
		// BWColor.getToStringHex(Color.Orange) + repairersString, false);
		// }
		// }
	}

	private static void paintTraining(Unit unit) {
		int labelMaxWidth = 100;
		int labelHeight = 10;
		int labelLeft = unit.getPosition().getX() - labelMaxWidth / 2;
		int labelTop = unit.getPosition().getY() + 5;

		// int unitBuildTime = unit.getType().getBuildTime();
		// int timeElapsed = starwork.getFrames() -
		// unit.getLastTimeTrainStarted();
		// double progress = (double) timeElapsed / unitBuildTime;
		// int labelProgress = (int) (1 + 99 * progress);
		// String color = RUtilities.assignStringForValue(
		// progress,
		// 1.0,
		// 0.0,
		// new String[] { BWColor.getToStringHex(Color.Red),
		// BWColor.getToStringHex(Color.Yellow),
		// BWColor.getToStringHex(Color.Green) });
		// stringToDisplay = color + labelProgress + "%";

		int operationProgress = 1;
		Unit trainedUnit = unit.getBuildUnit();
		String trainedUnitString = "";
		if (trainedUnit != null) {
			operationProgress = UnitHelper.getHP(unit) * 100
					/ UnitHelper.getMaxHP(trainedUnit);
			trainedUnitString = UnitHelper.getNameShort(trainedUnit);
		}

		// Paint box
		paintBox(labelLeft, labelTop, labelLeft + labelMaxWidth
				* operationProgress / 100, labelTop + labelHeight, Color.White,
				true);

		// Paint box borders
		paintBox(labelLeft, labelTop, labelLeft + labelMaxWidth, labelTop
				+ labelHeight, Color.Black, false);

		// =========================================================
		// Display label

		paintTextMap(unit, -4 * trainedUnitString.length(), 16,
				getToStringHex(Color.White) + trainedUnitString);
	}

	private static void paintBuildingHealth(Unit unit) {
		int labelMaxWidth = 56;
		int labelHeight = 6;
		int labelLeft = unit.getPosition().getX() - labelMaxWidth / 2;
		int labelTop = unit.getPosition().getY() + 13;

		double hpRatio = (double) UnitHelper.getHP(unit)
				/ UnitHelper.getMaxHP(unit);
		int hpProgress = (int) (1 + 99 * hpRatio);

		Color color = Color.Green;
		if (hpRatio < 0.66) {
			color = Color.Yellow;
			if (hpRatio < 0.33) {
				color = Color.Red;
			}
		}

		// Paint box
		paintBox(labelLeft, labelTop, labelLeft + labelMaxWidth * hpProgress
				/ 100, labelTop + labelHeight, color, true);

		// Paint box borders
		paintBox(labelLeft, labelTop, labelLeft + labelMaxWidth, labelTop
				+ labelHeight, Color.Black, false);

		// if (unit.getType().isBunker()) {
		//
		// }
	}

	private static void paintConstructionProgress(Unit unit) {
		String stringToDisplay;

		int labelMaxWidth = 56;
		int labelHeight = 6;
		int labelLeft = unit.getPosition().getX() - labelMaxWidth / 2;
		int labelTop = unit.getPosition().getY() + 13;

		double progress = (double) UnitHelper.getHP(unit)
				/ UnitHelper.getMaxHP(unit);
		int labelProgress = (int) (1 + 99 * progress);
		// String color = RUtilities.assignStringForValue(
		// progress,
		// 1.0,
		// 0.0,
		// new String[] { Color.Red,
		// Color.Yellow,
		// Color.Green });
		stringToDisplay = labelProgress + "%";

		// Paint box
		paintBox(labelLeft, labelTop, labelLeft + labelMaxWidth * labelProgress
				/ 100, labelTop + labelHeight, Color.Blue, true);

		// Paint box borders
		paintBox(labelLeft, labelTop, labelLeft + labelMaxWidth, labelTop
				+ labelHeight, Color.Black, false);

		// Paint label
		paintTextMap(labelLeft + labelMaxWidth / 2 - 8, labelTop - 3,
				stringToDisplay);

		// Display name of unit
		String name = (unit.getBuildUnit().getType().c_str() + "").replace(
				"Terran_", "");
		paintTextMap(unit, -25, -4, name);
	}

	@SuppressWarnings("static-access")
	private static void paintStatistics(Starwork starwork) {
		if (SelectUnits.our().firstBase() == null) {
			return;
		}

		int time = TimePresident.getFrameCounter();
		paintMainMessage(starwork, "Time: " + (time / 30) + "s"); // (" + time + ")"
		paintMainMessage(starwork, "Killed: " + enemyDeaths);
		paintMainMessage(starwork, "Lost: " + ourDeaths);
		// if (StrategyManager.getTargetUnit() != null) {
		// Unit attack = StrategyManager.getTargetUnit();
		// paintMainMessage(starwork,
		// "Attack target: " + attack.getName() + " ## visible:" +
		// attack.isVisible()
		// + ", exists:" + attack.isExists() + ", HP:" + attack.getHP());
		// }

		if (FULL_DEBUG) {
			paintMainMessage(starwork, "--------------------");
			paintMainMessage(starwork, "Enemy: "
					+ starwork.getEnemy().getRace().c_str());
			// paintMainMessage(starwork, "HQs: " +
			// UnitCounter.getNumberOfUnitsCompleted(UnitManager.BASE));

			// if (UnitCounter.getNumberOfUnitsCompleted(UnitManager.BASE) > 0)
			// paintMainMessage(starwork,
			// "Barracks: " +
			// UnitCounter.getNumberOfUnitsCompleted(UnitManager.BASE));
			//
			// if
			// (UnitCounter.getNumberOfUnitsCompleted(UnitTypes.Terran_Bunker) >
			// 0)
			// paintMainMessage(
			// starwork,
			// "Bunkers: "
			// +
			// UnitCounter.getNumberOfUnitsCompleted(UnitTypes.Terran_Bunker));
			//
			// if
			// (UnitCounter.getNumberOfUnitsCompleted(UnitTypes.Terran_Factory)
			// > 0)
			// paintMainMessage(
			// starwork,
			// "Factories: "
			// +
			// UnitCounter.getNumberOfUnitsCompleted(UnitTypes.Terran_Factory));
			//
			// paintMainMessage(starwork, "--------------------");
			//
			// paintMainMessage(
			// starwork,
			// "SCVs: ("
			// + UnitCounter.getNumberOfUnitsCompleted(UnitManager.WORKER)
			// + " / "
			// + TerranCommandCenter.getOptimalMineralGatherersAtBase(starwork
			// .getFirstBase()) + ")");
			//
			// paintMainMessage(
			// starwork,
			// "Gath. gas: ("
			// +
			// TerranCommandCenter.getNumberOfGasGatherersForBase(starwork.getFirstBase())
			// + ")");
			//
			// UnitTypes type;
			//
			// // type = UnitTypes.Terran_Marine;
			// // if (UnitCounter.getNumberOfUnitsCompleted(type) > 0)
			// // paintMainMessage(starwork, "Marines: " +
			// // UnitCounter.getNumberOfUnitsCompleted(type));
			// //
			// // type = UnitTypes.Terran_Medic;
			// // if (UnitCounter.getNumberOfUnitsCompleted(type) > 0)
			// // paintMainMessage(starwork, "Medics: " +
			// // UnitCounter.getNumberOfUnitsCompleted(type));
			// //
			// // type = UnitTypes.Terran_Firebat;
			// // if (UnitCounter.getNumberOfUnitsCompleted(type) > 0)
			// // paintMainMessage(starwork, "Firebats: " +
			// // UnitCounter.getNumberOfUnitsCompleted(type));
			//
			// int infantry = UnitCounter.getNumberOfInfantryUnitsCompleted();
			// if (infantry > 0)
			// paintMainMessage(starwork, "Infantry: " + infantry);
			//
			// type = UnitTypes.Terran_Vulture;
			// if (UnitCounter.getNumberOfUnitsCompleted(type) > 0)
			// paintMainMessage(starwork, "Vultures: " +
			// UnitCounter.getNumberOfUnitsCompleted(type));
			//
			// type = UnitTypes.Terran_Goliath;
			// if (UnitCounter.getNumberOfUnitsCompleted(type) > 0)
			// paintMainMessage(starwork, "Goliaths: " +
			// UnitCounter.getNumberOfUnitsCompleted(type));
			//
			// type = UnitTypes.Terran_Siege_Tank_Siege_Mode;
			// if (UnitCounter.getNumberOfUnitsCompleted(type) > 0)
			// paintMainMessage(starwork, "Tanks: " +
			// UnitCounter.getNumberOfUnitsCompleted(type));
			//
			// paintMainMessage(starwork, "--------------------");
			//
			// String minUnitsString = "";
			// if (StrategyManager.getMinBattleUnits() > 0) {
			// minUnitsString += " (min. " + StrategyManager.getMinBattleUnits()
			// + ")";
			// }
			// paintMainMessage(starwork, "Battle units: " +
			// UnitCounter.getNumberOfBattleUnits()
			// + minUnitsString);
			//
			// String buildArmy = "";
			// if (ArmyCreationManager.weShouldBuildBattleUnits()) {
			// buildArmy = "true";
			// } else {
			// buildArmy = "# FALSE #";
			// }
			// paintMainMessage(starwork, "Build army: " + buildArmy);
			//
			// boolean attackPending = StrategyManager.isAttackPending();
			// paintMainMessage(starwork, "Attack ready: " + (attackPending ?
			// "YES" : "no"));
			//
			// if (attackPending) {
			// paintMainMessage(
			// starwork,
			// "(distance allowed: "
			// + (int) StrategyManager.getAllowedDistanceFromSafePoint() + ")");
			// }
			//
			// paintMainMessage(starwork, "--------------------");
		}

		if (TimePresident.getFrameCounter() % 10 == 0) {

		}

		// for (UnitTypes type :
		// ShouldBuildCache.getBuildingsThatShouldBeBuild()) {
		// paintMainMessage(starwork,
		// "-> "
		// + type.name().substring(0, Math.min(17, type.name().length()))
		// .toUpperCase().replace("TERRAN_", "") + ": true");
		// }
	}

	private static void paintMainMessage(Starwork starwork, String string) {
		// string = "\u001F" + string;
		string = getToStringHex(Color.White) + string;
		paintText(5, 12 * mainMessageRowCounter++, string);
	}

	// =========================================================

	public static void message(Starwork starwork, String txt,
			boolean displayCounter) {
		game.sendText((displayCounter ? ("(" + messageCounter++ + ".) ") : "")
				+ txt);
		// game.((displayCounter ? ("(" + messageCounter++ + ".) ") : "") +
		// txt);
	}

	public static void message(Starwork starwork, String txt) {
		message(starwork, txt, true);
	}

	// public static void messageBuild(Starwork starwork, UnitTypes type) {
	// String building = "#" + UnitType.getUnitTypesByID(type.ordinal()).name();
	//
	// message(starwork, "Trying to build " + building);
	// }

	public static void errorOccured(String errorString) {
		Painter.errorOcurred = true;
		Painter.errorOcurredDetails = errorString;
	}

	private static void paintDebugMessage(Starwork starwork, String message,
			Object value) {
		String valueString = "ERROR";
		if (value instanceof Boolean) {
			if (((boolean) value) == true) {
				valueString = "TRUE";
			} else {
				valueString = "false";
			}
		} else {
			valueString = value + "";
		}

		message += ": " + valueString;

		starwork.getGame().drawText(2, 318 - message.length() * 3,
				3 + 10 * debugMessageCounter++, message);
	}

	// =========================================================

	private static void paintText(int x, int y, String message) {
		starwork.getGame().drawTextScreen(x, y, message);
	}

	private static void paintTextMap(Unit unit, String message) {
		starwork.getGame().drawTextScreen(unit.getPosition().getX(),
				unit.getPosition().getY(), message);
	}

	private static void paintTextMap(Unit unit, int dx, int dy, String message) {
		starwork.getGame().drawTextScreen(unit.getPosition().getX() + dx,
				unit.getPosition().getY() + dy, message);
	}

	private static void paintTextMap(int x, int y, String message) {
		starwork.getGame().drawTextScreen(x, y, message);
	}

	// private static void paintText(String message) {
	// starwork.getGame().drawText(2,
	// 318 - message.length() * 3, 3 + 10 * debugMessageCounter++,
	// message);
	// }

	private static void paintCircle(int x, int y, int radius, Color color,
			boolean fill) {
		starwork.getGame().drawCircleMap(x, y, radius, color, fill);
	}

	private static void paintBox(int x, int y, int width, int height,
			Color color, boolean fill) {
		int ctype = 2;
		starwork.getGame().drawBoxMap(x, y, width, height, color, fill);
	}

	private static String getToStringHex(Color color) {
		String string = "";
		return string;
	}

}
