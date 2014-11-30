package starwork.main;
import bwapi.DefaultBWListener;
import bwapi.Game;
import bwapi.Mirror;
import bwapi.Player;
import bwapi.Unit;
import bwapi.UnitType;
import bwta.BWTA;

public class Starwork extends DefaultBWListener {
	
	private static final String VERSION = "0.1";
	
	// =====================================================================

    private static Mirror mirror = new Mirror();

    private static Game game;

    private static Player self;
    private static Player enemy;
    private static Player neutral;
    
    // =====================================================================

    private static Starwork starwork;
    
    // =====================================================================

    private void run() {
        mirror.getModule().setEventListener(this);
        mirror.startGame();
    }

    @Override
    public void onUnitCreate(Unit unit) {
//    	System.out.println("New unit " + unit.getType());
    }

    @Override
    public void onStart() {
        game = mirror.getGame();
        self = game.self();
        enemy = game.enemy();
        neutral = game.neutral();

        //Use BWTA to analyze map
        //This may take a few minutes if the map is processed first time!
        System.out.println("Analyzing map...");
        BWTA.readMap();
        BWTA.analyze();
        System.out.println("Map data ready");

    }

    @Override
    public void onFrame() {
        game.setTextSize(10);
        game.drawTextScreen(10, 10, "Playing as " + self.getName() + " - " + self.getRace());

        StringBuilder units = new StringBuilder("My units:\n");

        //iterate through my units
        for (Unit myUnit : self.getUnits()) {
            units.append(myUnit.getType()).append(" ").append(myUnit.getTilePosition()).append("\n");

            //if there's enough minerals, train an SCV
            if (myUnit.getType() == UnitType.Terran_Command_Center && self.minerals() >= 50) {
                myUnit.train(UnitType.Terran_SCV);
            }

            //if it's a drone and it's idle, send it to the closest mineral patch
            if (myUnit.getType().isWorker() && myUnit.isIdle()) {
                Unit closestMineral = null;

                //find the closest mineral
                for (Unit neutralUnit : game.neutral().getUnits()) {
                    if (neutralUnit.getType().isMineralField()) {
                        if (closestMineral == null || myUnit.getDistance(neutralUnit) < myUnit.getDistance(closestMineral)) {
                            closestMineral = neutralUnit;
                        }
                    }
                }

                //if a mineral patch was found, send the drone to gather it
                if (closestMineral != null) {
                    myUnit.gather(closestMineral, false);
                }
            }
        }

        //draw my units on screen
        game.drawTextScreen(10, 25, units.toString());
    }
    
    // =====================================================================

//    private static void main(String[] args) {
//        new StarWork().run();
//    }
    
    public void start() {
    	System.out.println("StarWork ver. " + VERSION + " started.");
    	
    	starwork = new Starwork();
    	starwork.run();
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