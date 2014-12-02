package starwork.main;

import starwork.units.UnitDamages;
import starwork.units.UnitsKnowledge;
import bwapi.DefaultBWListener;
import bwapi.Game;
import bwapi.Mirror;
import bwapi.Player;
import bwapi.Unit;
import bwta.BWTA;

public class Starwork extends DefaultBWListener {

	private static final String VERSION = "0.1";
	private static final int INITIAL_GAME_SPEED = 0;

	// =====================================================================

	private static Mirror mirror = new Mirror();

	private static Game game;

	private static Player self;
	private static Player enemy;
	private static Player neutral;

	private static Starwork starwork;

	// =====================================================================

	@SuppressWarnings("static-access")
	public Starwork() {
		System.out.println("Using Starwork ver. " + VERSION + " framework.");
		this.starwork = this;
	}

	// =====================================================================

	public void start() {
		mirror.getModule().setEventListener(this);
		mirror.startGame();

		UnitDamages.rememberUnitDamageValues();
		// UnitDamages.displayUnitDamages();
	}

	@Override
	public void onUnitCreate(Unit unit) {
		// System.out.println("New unit " + unit.getType());
	}

	@Override
	public void onUnitDestroy(Unit unit) {
		super.onUnitDestroy(unit);
		UnitsKnowledge.unitDestroyed(unit);
	}

	@Override
	public void onUnitDiscover(Unit unit) {
		super.onUnitDiscover(unit);
		UnitsKnowledge.unitDiscovered(unit);
	}

	@Override
	public void onUnitShow(Unit unit) {
		super.onUnitShow(unit);
	}

	@Override
	public void onStart() {
		game = mirror.getGame();
		self = game.self();
		enemy = game.enemy();
		neutral = game.neutral();

		// Use BWTA to analyze map
		// This may take a few minutes if the map is processed first time!
		System.out.print("Analyzing map... ");
		BWTA.readMap();
		BWTA.analyze();
		System.out.println("Map data ready.");
		System.out.println("===============");

		mirror.getGame().setLocalSpeed(INITIAL_GAME_SPEED);
	}

	// =====================================================================

	public static Starwork getInstance() {
		return starwork;
	}

	public static Mirror getMirror() {
		return mirror;
	}

	public static Game getGame() {
		return game;
	}

	public static Player getSelf() {
		return self;
	}

	public static Player getEnemy() {
		return enemy;
	}

	public static Player getNeutral() {
		return neutral;
	}

}