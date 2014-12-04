package bwapi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game {

	public List<Force> getForces() {
		return getForces_native(pointer);
	}

	public List<Player> getPlayers() {
		return getPlayers_native(pointer);
	}

	public List<Unit> getAllUnits() {
		return getAllUnits_native(pointer);
	}

	public List<Unit> getMinerals() {
		return getMinerals_native(pointer);
	}

	public List<Unit> getGeysers() {
		return getGeysers_native(pointer);
	}

	public List<Unit> getNeutralUnits() {
		return getNeutralUnits_native(pointer);
	}

	public List<Unit> getStaticMinerals() {
		return getStaticMinerals_native(pointer);
	}

	public List<Unit> getStaticGeysers() {
		return getStaticGeysers_native(pointer);
	}

	public List<Unit> getStaticNeutralUnits() {
		return getStaticNeutralUnits_native(pointer);
	}

	public List<Bullet> getBullets() {
		return getBullets_native(pointer);
	}

	public List<Position> getNukeDots() {
		return getNukeDots_native(pointer);
	}

	public Force getForce(int forceID) {
		return getForce_native(pointer, forceID);
	}

	public Player getPlayer(int playerID) {
		return getPlayer_native(pointer, playerID);
	}

	public Unit getUnit(int unitID) {
		return getUnit_native(pointer, unitID);
	}

	public Unit indexToUnit(int unitIndex) {
		return indexToUnit_native(pointer, unitIndex);
	}

	public Region getRegion(int regionID) {
		return getRegion_native(pointer, regionID);
	}

	public GameType getGameType() {
		return getGameType_native(pointer);
	}

	public int getLatency() {
		return getLatency_native(pointer);
	}

	public int getFrameCount() {
		return getFrameCount_native(pointer);
	}

	public int getFPS() {
		return getFPS_native(pointer);
	}

	public double getAverageFPS() {
		return getAverageFPS_native(pointer);
	}

	public Position getMousePosition() {
		return getMousePosition_native(pointer);
	}

	public boolean getMouseState(MouseButton button) {
		return getMouseState_native(pointer, button);
	}

	public boolean getMouseState(int button) {
		return getMouseState_native(pointer, button);
	}

	public boolean getKeyState(Key key) {
		return getKeyState_native(pointer, key);
	}

	public boolean getKeyState(int key) {
		return getKeyState_native(pointer, key);
	}

	public Position getScreenPosition() {
		return getScreenPosition_native(pointer);
	}

	public void setScreenPosition(int x, int y) {
		setScreenPosition_native(pointer, x, y);
	}

	public void setScreenPosition(Position p) {
		setScreenPosition_native(pointer, p);
	}

	public void pingMinimap(int x, int y) {
		pingMinimap_native(pointer, x, y);
	}

	public void pingMinimap(Position p) {
		pingMinimap_native(pointer, p);
	}

	public boolean isFlagEnabled(int flag) {
		return isFlagEnabled_native(pointer, flag);
	}

	public void enableFlag(int flag) {
		enableFlag_native(pointer, flag);
	}

	public List<Unit> getUnitsOnTile(int tileX, int tileY) {
		return getUnitsOnTile_native(pointer, tileX, tileY);
	}

	public List<Unit> getUnitsInRectangle(int left, int top, int right, int bottom) {
		return getUnitsInRectangle_native(pointer, left, top, right, bottom);
	}

	public List<Unit> getUnitsInRectangle(Position topLeft, Position bottomRight) {
		return getUnitsInRectangle_native(pointer, topLeft, bottomRight);
	}

	public List<Unit> getUnitsInRadius(Position center, int radius) {
		return getUnitsInRadius_native(pointer, center, radius);
	}

	public Error getLastError() {
		return getLastError_native(pointer);
	}

	public boolean setLastError(Error e) {
		return setLastError_native(pointer, e);
	}

	public int mapWidth() {
		return mapWidth_native(pointer);
	}

	public int mapHeight() {
		return mapHeight_native(pointer);
	}

	public String mapFileName() {
		return mapFileName_native(pointer);
	}

	public String mapPathName() {
		return mapPathName_native(pointer);
	}

	public String mapName() {
		return mapName_native(pointer);
	}

	public String mapHash() {
		return mapHash_native(pointer);
	}

	public boolean isWalkable(int walkX, int walkY) {
		return isWalkable_native(pointer, walkX, walkY);
	}

	public boolean isBuildable(int tileX, int tileY) {
		return isBuildable_native(pointer, tileX, tileY);
	}

	public boolean isBuildable(int tileX, int tileY, boolean includeBuildings) {
		return isBuildable_native(pointer, tileX, tileY, includeBuildings);
	}

	public boolean isBuildable(TilePosition position) {
		return isBuildable_native(pointer, position);
	}

	public boolean isBuildable(TilePosition position, boolean includeBuildings) {
		return isBuildable_native(pointer, position, includeBuildings);
	}

	public boolean isVisible(int tileX, int tileY) {
		return isVisible_native(pointer, tileX, tileY);
	}

	public boolean isVisible(TilePosition position) {
		return isVisible_native(pointer, position);
	}

	public boolean isExplored(int tileX, int tileY) {
		return isExplored_native(pointer, tileX, tileY);
	}

	public boolean isExplored(TilePosition position) {
		return isExplored_native(pointer, position);
	}

	public boolean hasCreep(int tileX, int tileY) {
		return hasCreep_native(pointer, tileX, tileY);
	}

	public boolean hasCreep(TilePosition position) {
		return hasCreep_native(pointer, position);
	}

	public boolean hasPower(int tileX, int tileY) {
		return hasPower_native(pointer, tileX, tileY);
	}

	public boolean hasPower(int tileX, int tileY, UnitType unitType) {
		return hasPower_native(pointer, tileX, tileY, unitType);
	}

	public boolean hasPower(TilePosition position) {
		return hasPower_native(pointer, position);
	}

	public boolean hasPower(TilePosition position, UnitType unitType) {
		return hasPower_native(pointer, position, unitType);
	}

	public boolean hasPower(int tileX, int tileY, int tileWidth, int tileHeight) {
		return hasPower_native(pointer, tileX, tileY, tileWidth, tileHeight);
	}

	public boolean hasPower(int tileX, int tileY, int tileWidth, int tileHeight, UnitType unitType) {
		return hasPower_native(pointer, tileX, tileY, tileWidth, tileHeight, unitType);
	}

	public boolean hasPower(TilePosition position, int tileWidth, int tileHeight) {
		return hasPower_native(pointer, position, tileWidth, tileHeight);
	}

	public boolean hasPower(TilePosition position, int tileWidth, int tileHeight, UnitType unitType) {
		return hasPower_native(pointer, position, tileWidth, tileHeight, unitType);
	}

	public boolean hasPowerPrecise(int x, int y) {
		return hasPowerPrecise_native(pointer, x, y);
	}

	public boolean hasPowerPrecise(int x, int y, UnitType unitType) {
		return hasPowerPrecise_native(pointer, x, y, unitType);
	}

	public boolean hasPowerPrecise(Position position) {
		return hasPowerPrecise_native(pointer, position);
	}

	public boolean hasPowerPrecise(Position position, UnitType unitType) {
		return hasPowerPrecise_native(pointer, position, unitType);
	}

	public boolean canBuildHere(Unit builder, TilePosition position, UnitType type) {
		return canBuildHere_native(pointer, builder, position, type);
	}

	public boolean canBuildHere(Unit builder, TilePosition position, UnitType type, boolean checkExplored) {
		return canBuildHere_native(pointer, builder, position, type, checkExplored);
	}

	public boolean canMake(Unit builder, UnitType type) {
		return canMake_native(pointer, builder, type);
	}

	public boolean canResearch(Unit unit, TechType type) {
		return canResearch_native(pointer, unit, type);
	}

	public boolean canUpgrade(Unit unit, UpgradeType type) {
		return canUpgrade_native(pointer, unit, type);
	}

	public List<TilePosition> getStartLocations() {
		return getStartLocations_native(pointer);
	}

	public void printf(String cstr_format) {
		printf_native(pointer, cstr_format);
	}

	public void sendText(String cstr_format) {
		sendText_native(pointer, cstr_format);
	}

	public void sendTextEx(boolean toAllies, String cstr_format) {
		sendTextEx_native(pointer, toAllies, cstr_format);
	}

	public void changeRace(Race race) {
		changeRace_native(pointer, race);
	}

	public boolean isInGame() {
		return isInGame_native(pointer);
	}

	public boolean isMultiplayer() {
		return isMultiplayer_native(pointer);
	}

	public boolean isBattleNet() {
		return isBattleNet_native(pointer);
	}

	public boolean isPaused() {
		return isPaused_native(pointer);
	}

	public boolean isReplay() {
		return isReplay_native(pointer);
	}

	public void startGame() {
		startGame_native(pointer);
	}

	public void pauseGame() {
		pauseGame_native(pointer);
	}

	public void resumeGame() {
		resumeGame_native(pointer);
	}

	public void leaveGame() {
		leaveGame_native(pointer);
	}

	public void restartGame() {
		restartGame_native(pointer);
	}

	public void setLocalSpeed() {
		setLocalSpeed_native(pointer);
	}

	public void setLocalSpeed(int speed) {
		setLocalSpeed_native(pointer, speed);
	}

	public boolean issueCommand(List<Unit> units, UnitCommand command) {
		return issueCommand_native(pointer, units, command);
	}

	public List<Unit> getSelectedUnits() {
		return getSelectedUnits_native(pointer);
	}

	public Player self() {
		return self_native(pointer);
	}

	public Player enemy() {
		return enemy_native(pointer);
	}

	public Player neutral() {
		return neutral_native(pointer);
	}

	public List<Player> allies() {
		return allies_native(pointer);
	}

	public List<Player> enemies() {
		return enemies_native(pointer);
	}

	public List<Player> observers() {
		return observers_native(pointer);
	}

	public void setTextSize() {
		setTextSize_native(pointer);
	}

	public void setTextSize(int size) {
		setTextSize_native(pointer, size);
	}

	public void drawText(int ctype, int x, int y, String cstr_format) {
		drawText_native(pointer, ctype, x, y, cstr_format);
	}

	public void drawTextMap(int x, int y, String cstr_format) {
		drawTextMap_native(pointer, x, y, cstr_format);
	}

	public void drawTextMouse(int x, int y, String cstr_format) {
		drawTextMouse_native(pointer, x, y, cstr_format);
	}

	public void drawTextScreen(int x, int y, String cstr_format) {
		drawTextScreen_native(pointer, x, y, cstr_format);
	}

	public void drawBox(int ctype, int left, int top, int right, int bottom, Color color) {
		drawBox_native(pointer, ctype, left, top, right, bottom, color);
	}

	public void drawBox(int ctype, int left, int top, int right, int bottom, Color color, boolean isSolid) {
		drawBox_native(pointer, ctype, left, top, right, bottom, color, isSolid);
	}

	public void drawBoxMap(int left, int top, int right, int bottom, Color color) {
		drawBoxMap_native(pointer, left, top, right, bottom, color);
	}

	public void drawBoxMap(int left, int top, int right, int bottom, Color color, boolean isSolid) {
		drawBoxMap_native(pointer, left, top, right, bottom, color, isSolid);
	}

	public void drawBoxMouse(int left, int top, int right, int bottom, Color color) {
		drawBoxMouse_native(pointer, left, top, right, bottom, color);
	}

	public void drawBoxMouse(int left, int top, int right, int bottom, Color color, boolean isSolid) {
		drawBoxMouse_native(pointer, left, top, right, bottom, color, isSolid);
	}

	public void drawBoxScreen(int left, int top, int right, int bottom, Color color) {
		drawBoxScreen_native(pointer, left, top, right, bottom, color);
	}

	public void drawBoxScreen(int left, int top, int right, int bottom, Color color, boolean isSolid) {
		drawBoxScreen_native(pointer, left, top, right, bottom, color, isSolid);
	}

	public void drawTriangle(int ctype, int ax, int ay, int bx, int by, int cx, int cy, Color color) {
		drawTriangle_native(pointer, ctype, ax, ay, bx, by, cx, cy, color);
	}

	public void drawTriangle(int ctype, int ax, int ay, int bx, int by, int cx, int cy, Color color, boolean isSolid) {
		drawTriangle_native(pointer, ctype, ax, ay, bx, by, cx, cy, color, isSolid);
	}

	public void drawTriangleMap(int ax, int ay, int bx, int by, int cx, int cy, Color color) {
		drawTriangleMap_native(pointer, ax, ay, bx, by, cx, cy, color);
	}

	public void drawTriangleMap(int ax, int ay, int bx, int by, int cx, int cy, Color color, boolean isSolid) {
		drawTriangleMap_native(pointer, ax, ay, bx, by, cx, cy, color, isSolid);
	}

	public void drawTriangleMouse(int ax, int ay, int bx, int by, int cx, int cy, Color color) {
		drawTriangleMouse_native(pointer, ax, ay, bx, by, cx, cy, color);
	}

	public void drawTriangleMouse(int ax, int ay, int bx, int by, int cx, int cy, Color color, boolean isSolid) {
		drawTriangleMouse_native(pointer, ax, ay, bx, by, cx, cy, color, isSolid);
	}

	public void drawTriangleScreen(int ax, int ay, int bx, int by, int cx, int cy, Color color) {
		drawTriangleScreen_native(pointer, ax, ay, bx, by, cx, cy, color);
	}

	public void drawTriangleScreen(int ax, int ay, int bx, int by, int cx, int cy, Color color, boolean isSolid) {
		drawTriangleScreen_native(pointer, ax, ay, bx, by, cx, cy, color, isSolid);
	}

	public void drawCircle(int ctype, int x, int y, int radius, Color color) {
		drawCircle_native(pointer, ctype, x, y, radius, color);
	}

	public void drawCircle(int ctype, int x, int y, int radius, Color color, boolean isSolid) {
		drawCircle_native(pointer, ctype, x, y, radius, color, isSolid);
	}

	public void drawCircleMap(int x, int y, int radius, Color color) {
		drawCircleMap_native(pointer, x, y, radius, color);
	}

	public void drawCircleMap(int x, int y, int radius, Color color, boolean isSolid) {
		drawCircleMap_native(pointer, x, y, radius, color, isSolid);
	}

	public void drawCircleMouse(int x, int y, int radius, Color color) {
		drawCircleMouse_native(pointer, x, y, radius, color);
	}

	public void drawCircleMouse(int x, int y, int radius, Color color, boolean isSolid) {
		drawCircleMouse_native(pointer, x, y, radius, color, isSolid);
	}

	public void drawCircleScreen(int x, int y, int radius, Color color) {
		drawCircleScreen_native(pointer, x, y, radius, color);
	}

	public void drawCircleScreen(int x, int y, int radius, Color color, boolean isSolid) {
		drawCircleScreen_native(pointer, x, y, radius, color, isSolid);
	}

	public void drawEllipse(int ctype, int x, int y, int xrad, int yrad, Color color) {
		drawEllipse_native(pointer, ctype, x, y, xrad, yrad, color);
	}

	public void drawEllipse(int ctype, int x, int y, int xrad, int yrad, Color color, boolean isSolid) {
		drawEllipse_native(pointer, ctype, x, y, xrad, yrad, color, isSolid);
	}

	public void drawEllipseMap(int x, int y, int xrad, int yrad, Color color) {
		drawEllipseMap_native(pointer, x, y, xrad, yrad, color);
	}

	public void drawEllipseMap(int x, int y, int xrad, int yrad, Color color, boolean isSolid) {
		drawEllipseMap_native(pointer, x, y, xrad, yrad, color, isSolid);
	}

	public void drawEllipseMouse(int x, int y, int xrad, int yrad, Color color) {
		drawEllipseMouse_native(pointer, x, y, xrad, yrad, color);
	}

	public void drawEllipseMouse(int x, int y, int xrad, int yrad, Color color, boolean isSolid) {
		drawEllipseMouse_native(pointer, x, y, xrad, yrad, color, isSolid);
	}

	public void drawEllipseScreen(int x, int y, int xrad, int yrad, Color color) {
		drawEllipseScreen_native(pointer, x, y, xrad, yrad, color);
	}

	public void drawEllipseScreen(int x, int y, int xrad, int yrad, Color color, boolean isSolid) {
		drawEllipseScreen_native(pointer, x, y, xrad, yrad, color, isSolid);
	}

	public void drawDot(int ctype, int x, int y, Color color) {
		drawDot_native(pointer, ctype, x, y, color);
	}

	public void drawDotMap(int x, int y, Color color) {
		drawDotMap_native(pointer, x, y, color);
	}

	public void drawDotMouse(int x, int y, Color color) {
		drawDotMouse_native(pointer, x, y, color);
	}

	public void drawDotScreen(int x, int y, Color color) {
		drawDotScreen_native(pointer, x, y, color);
	}

	public void drawLine(int ctype, int x1, int y1, int x2, int y2, Color color) {
		drawLine_native(pointer, ctype, x1, y1, x2, y2, color);
	}

	public void drawLineMap(int x1, int y1, int x2, int y2, Color color) {
		drawLineMap_native(pointer, x1, y1, x2, y2, color);
	}

	public void drawLineMouse(int x1, int y1, int x2, int y2, Color color) {
		drawLineMouse_native(pointer, x1, y1, x2, y2, color);
	}

	public void drawLineScreen(int x1, int y1, int x2, int y2, Color color) {
		drawLineScreen_native(pointer, x1, y1, x2, y2, color);
	}

	public void getScreenBuffer() {
		getScreenBuffer_native(pointer);
	}

	public int getLatencyFrames() {
		return getLatencyFrames_native(pointer);
	}

	public int getLatencyTime() {
		return getLatencyTime_native(pointer);
	}

	public int getRemainingLatencyFrames() {
		return getRemainingLatencyFrames_native(pointer);
	}

	public int getRemainingLatencyTime() {
		return getRemainingLatencyTime_native(pointer);
	}

	public int getRevision() {
		return getRevision_native(pointer);
	}

	public boolean isDebug() {
		return isDebug_native(pointer);
	}

	public boolean isLatComEnabled() {
		return isLatComEnabled_native(pointer);
	}

	public void setLatCom(boolean isEnabled) {
		setLatCom_native(pointer, isEnabled);
	}

	public int getReplayFrameCount() {
		return getReplayFrameCount_native(pointer);
	}

	public void setGUI() {
		setGUI_native(pointer);
	}

	public void setGUI(boolean enabled) {
		setGUI_native(pointer, enabled);
	}

	public int getAPM() {
		return getAPM_native(pointer);
	}

	public int getAPM(boolean includeSelects) {
		return getAPM_native(pointer, includeSelects);
	}

	public boolean setMap(String cstr_mapFileName) {
		return setMap_native(pointer, cstr_mapFileName);
	}

	public void setFrameSkip() {
		setFrameSkip_native(pointer);
	}

	public void setFrameSkip(int frameSkip) {
		setFrameSkip_native(pointer, frameSkip);
	}

	public boolean hasPath(Position source, Position destination) {
		return hasPath_native(pointer, source, destination);
	}

	public boolean setAlliance(Player player, boolean allied) {
		return setAlliance_native(pointer, player, allied);
	}

	public boolean setAlliance(Player player) {
		return setAlliance_native(pointer, player);
	}

	public boolean setAlliance(Player player, boolean allied, boolean alliedVictory) {
		return setAlliance_native(pointer, player, allied, alliedVictory);
	}

	public boolean setVision(Player player) {
		return setVision_native(pointer, player);
	}

	public boolean setVision(Player player, boolean enabled) {
		return setVision_native(pointer, player, enabled);
	}

	public void setCommandOptimizationLevel() {
		setCommandOptimizationLevel_native(pointer);
	}

	public void setCommandOptimizationLevel(int level) {
		setCommandOptimizationLevel_native(pointer, level);
	}

	public Region getRegionAt(int x, int y) {
		return getRegionAt_native(pointer, x, y);
	}

	public Region getRegionAt(Position position) {
		return getRegionAt_native(pointer, position);
	}

	public int getLastEventTime() {
		return getLastEventTime_native(pointer);
	}

	public boolean setReplayVision(Player player) {
		return setReplayVision_native(pointer, player);
	}

	public boolean setReplayVision(Player player, boolean enabled) {
		return setReplayVision_native(pointer, player, enabled);
	}

	public boolean setRevealAll() {
		return setRevealAll_native(pointer);
	}

	public boolean setRevealAll(boolean reveal) {
		return setRevealAll_native(pointer, reveal);
	}

	private static Map<Long, Game> instances = new HashMap<Long, Game>();

	private Game(long pointer) {
		this.pointer = pointer;
	}

	private static Game get(long pointer) {
		if (pointer == 0) {
			return null;
		}
		Game instance = instances.get(pointer);
		if (instance == null) {
			instance = new Game(pointer);
			instances.put(pointer, instance);
		}
		return instance;
	}

	private long pointer;

	private native List<Force> getForces_native(long pointer);

	private native List<Player> getPlayers_native(long pointer);

	private native List<Unit> getAllUnits_native(long pointer);

	private native List<Unit> getMinerals_native(long pointer);

	private native List<Unit> getGeysers_native(long pointer);

	private native List<Unit> getNeutralUnits_native(long pointer);

	private native List<Unit> getStaticMinerals_native(long pointer);

	private native List<Unit> getStaticGeysers_native(long pointer);

	private native List<Unit> getStaticNeutralUnits_native(long pointer);

	private native List<Bullet> getBullets_native(long pointer);

	private native List<Position> getNukeDots_native(long pointer);

	private native Force getForce_native(long pointer, int forceID);

	private native Player getPlayer_native(long pointer, int playerID);

	private native Unit getUnit_native(long pointer, int unitID);

	private native Unit indexToUnit_native(long pointer, int unitIndex);

	private native Region getRegion_native(long pointer, int regionID);

	private native GameType getGameType_native(long pointer);

	private native int getLatency_native(long pointer);

	private native int getFrameCount_native(long pointer);

	private native int getFPS_native(long pointer);

	private native double getAverageFPS_native(long pointer);

	private native Position getMousePosition_native(long pointer);

	private native boolean getMouseState_native(long pointer, MouseButton button);

	private native boolean getMouseState_native(long pointer, int button);

	private native boolean getKeyState_native(long pointer, Key key);

	private native boolean getKeyState_native(long pointer, int key);

	private native Position getScreenPosition_native(long pointer);

	private native void setScreenPosition_native(long pointer, int x, int y);

	private native void setScreenPosition_native(long pointer, Position p);

	private native void pingMinimap_native(long pointer, int x, int y);

	private native void pingMinimap_native(long pointer, Position p);

	private native boolean isFlagEnabled_native(long pointer, int flag);

	private native void enableFlag_native(long pointer, int flag);

	private native List<Unit> getUnitsOnTile_native(long pointer, int tileX, int tileY);

	private native List<Unit> getUnitsInRectangle_native(long pointer, int left, int top, int right, int bottom);

	private native List<Unit> getUnitsInRectangle_native(long pointer, Position topLeft, Position bottomRight);

	private native List<Unit> getUnitsInRadius_native(long pointer, Position center, int radius);

	private native Error getLastError_native(long pointer);

	private native boolean setLastError_native(long pointer, Error e);

	private native int mapWidth_native(long pointer);

	private native int mapHeight_native(long pointer);

	private native String mapFileName_native(long pointer);

	private native String mapPathName_native(long pointer);

	private native String mapName_native(long pointer);

	private native String mapHash_native(long pointer);

	private native boolean isWalkable_native(long pointer, int walkX, int walkY);

	private native boolean isBuildable_native(long pointer, int tileX, int tileY);

	private native boolean isBuildable_native(long pointer, int tileX, int tileY, boolean includeBuildings);

	private native boolean isBuildable_native(long pointer, TilePosition position);

	private native boolean isBuildable_native(long pointer, TilePosition position, boolean includeBuildings);

	private native boolean isVisible_native(long pointer, int tileX, int tileY);

	private native boolean isVisible_native(long pointer, TilePosition position);

	private native boolean isExplored_native(long pointer, int tileX, int tileY);

	private native boolean isExplored_native(long pointer, TilePosition position);

	private native boolean hasCreep_native(long pointer, int tileX, int tileY);

	private native boolean hasCreep_native(long pointer, TilePosition position);

	private native boolean hasPower_native(long pointer, int tileX, int tileY);

	private native boolean hasPower_native(long pointer, int tileX, int tileY, UnitType unitType);

	private native boolean hasPower_native(long pointer, TilePosition position);

	private native boolean hasPower_native(long pointer, TilePosition position, UnitType unitType);

	private native boolean hasPower_native(long pointer, int tileX, int tileY, int tileWidth, int tileHeight);

	private native boolean hasPower_native(long pointer, int tileX, int tileY, int tileWidth, int tileHeight,
			UnitType unitType);

	private native boolean hasPower_native(long pointer, TilePosition position, int tileWidth, int tileHeight);

	private native boolean hasPower_native(long pointer, TilePosition position, int tileWidth, int tileHeight,
			UnitType unitType);

	private native boolean hasPowerPrecise_native(long pointer, int x, int y);

	private native boolean hasPowerPrecise_native(long pointer, int x, int y, UnitType unitType);

	private native boolean hasPowerPrecise_native(long pointer, Position position);

	private native boolean hasPowerPrecise_native(long pointer, Position position, UnitType unitType);

	private native boolean canBuildHere_native(long pointer, Unit builder, TilePosition position, UnitType type);

	private native boolean canBuildHere_native(long pointer, Unit builder, TilePosition position, UnitType type,
			boolean checkExplored);

	private native boolean canMake_native(long pointer, Unit builder, UnitType type);

	private native boolean canResearch_native(long pointer, Unit unit, TechType type);

	private native boolean canUpgrade_native(long pointer, Unit unit, UpgradeType type);

	private native List<TilePosition> getStartLocations_native(long pointer);

	private native void printf_native(long pointer, String cstr_format);

	private native void sendText_native(long pointer, String cstr_format);

	private native void sendTextEx_native(long pointer, boolean toAllies, String cstr_format);

	private native void changeRace_native(long pointer, Race race);

	private native boolean isInGame_native(long pointer);

	private native boolean isMultiplayer_native(long pointer);

	private native boolean isBattleNet_native(long pointer);

	private native boolean isPaused_native(long pointer);

	private native boolean isReplay_native(long pointer);

	private native void startGame_native(long pointer);

	private native void pauseGame_native(long pointer);

	private native void resumeGame_native(long pointer);

	private native void leaveGame_native(long pointer);

	private native void restartGame_native(long pointer);

	private native void setLocalSpeed_native(long pointer);

	private native void setLocalSpeed_native(long pointer, int speed);

	private native boolean issueCommand_native(long pointer, List<Unit> units, UnitCommand command);

	private native List<Unit> getSelectedUnits_native(long pointer);

	private native Player self_native(long pointer);

	private native Player enemy_native(long pointer);

	private native Player neutral_native(long pointer);

	private native List<Player> allies_native(long pointer);

	private native List<Player> enemies_native(long pointer);

	private native List<Player> observers_native(long pointer);

	private native void setTextSize_native(long pointer);

	private native void setTextSize_native(long pointer, int size);

	private native void drawText_native(long pointer, int ctype, int x, int y, String cstr_format);

	private native void drawTextMap_native(long pointer, int x, int y, String cstr_format);

	private native void drawTextMouse_native(long pointer, int x, int y, String cstr_format);

	private native void drawTextScreen_native(long pointer, int x, int y, String cstr_format);

	private native void drawBox_native(long pointer, int ctype, int left, int top, int right, int bottom, Color color);

	private native void drawBox_native(long pointer, int ctype, int left, int top, int right, int bottom, Color color,
			boolean isSolid);

	private native void drawBoxMap_native(long pointer, int left, int top, int right, int bottom, Color color);

	private native void drawBoxMap_native(long pointer, int left, int top, int right, int bottom, Color color,
			boolean isSolid);

	private native void drawBoxMouse_native(long pointer, int left, int top, int right, int bottom, Color color);

	private native void drawBoxMouse_native(long pointer, int left, int top, int right, int bottom, Color color,
			boolean isSolid);

	private native void drawBoxScreen_native(long pointer, int left, int top, int right, int bottom, Color color);

	private native void drawBoxScreen_native(long pointer, int left, int top, int right, int bottom, Color color,
			boolean isSolid);

	private native void drawTriangle_native(long pointer, int ctype, int ax, int ay, int bx, int by, int cx, int cy,
			Color color);

	private native void drawTriangle_native(long pointer, int ctype, int ax, int ay, int bx, int by, int cx, int cy,
			Color color, boolean isSolid);

	private native void drawTriangleMap_native(long pointer, int ax, int ay, int bx, int by, int cx, int cy, Color color);

	private native void drawTriangleMap_native(long pointer, int ax, int ay, int bx, int by, int cx, int cy,
			Color color, boolean isSolid);

	private native void drawTriangleMouse_native(long pointer, int ax, int ay, int bx, int by, int cx, int cy,
			Color color);

	private native void drawTriangleMouse_native(long pointer, int ax, int ay, int bx, int by, int cx, int cy,
			Color color, boolean isSolid);

	private native void drawTriangleScreen_native(long pointer, int ax, int ay, int bx, int by, int cx, int cy,
			Color color);

	private native void drawTriangleScreen_native(long pointer, int ax, int ay, int bx, int by, int cx, int cy,
			Color color, boolean isSolid);

	private native void drawCircle_native(long pointer, int ctype, int x, int y, int radius, Color color);

	private native void drawCircle_native(long pointer, int ctype, int x, int y, int radius, Color color,
			boolean isSolid);

	private native void drawCircleMap_native(long pointer, int x, int y, int radius, Color color);

	private native void drawCircleMap_native(long pointer, int x, int y, int radius, Color color, boolean isSolid);

	private native void drawCircleMouse_native(long pointer, int x, int y, int radius, Color color);

	private native void drawCircleMouse_native(long pointer, int x, int y, int radius, Color color, boolean isSolid);

	private native void drawCircleScreen_native(long pointer, int x, int y, int radius, Color color);

	private native void drawCircleScreen_native(long pointer, int x, int y, int radius, Color color, boolean isSolid);

	private native void drawEllipse_native(long pointer, int ctype, int x, int y, int xrad, int yrad, Color color);

	private native void drawEllipse_native(long pointer, int ctype, int x, int y, int xrad, int yrad, Color color,
			boolean isSolid);

	private native void drawEllipseMap_native(long pointer, int x, int y, int xrad, int yrad, Color color);

	private native void drawEllipseMap_native(long pointer, int x, int y, int xrad, int yrad, Color color,
			boolean isSolid);

	private native void drawEllipseMouse_native(long pointer, int x, int y, int xrad, int yrad, Color color);

	private native void drawEllipseMouse_native(long pointer, int x, int y, int xrad, int yrad, Color color,
			boolean isSolid);

	private native void drawEllipseScreen_native(long pointer, int x, int y, int xrad, int yrad, Color color);

	private native void drawEllipseScreen_native(long pointer, int x, int y, int xrad, int yrad, Color color,
			boolean isSolid);

	private native void drawDot_native(long pointer, int ctype, int x, int y, Color color);

	private native void drawDotMap_native(long pointer, int x, int y, Color color);

	private native void drawDotMouse_native(long pointer, int x, int y, Color color);

	private native void drawDotScreen_native(long pointer, int x, int y, Color color);

	private native void drawLine_native(long pointer, int ctype, int x1, int y1, int x2, int y2, Color color);

	private native void drawLineMap_native(long pointer, int x1, int y1, int x2, int y2, Color color);

	private native void drawLineMouse_native(long pointer, int x1, int y1, int x2, int y2, Color color);

	private native void drawLineScreen_native(long pointer, int x1, int y1, int x2, int y2, Color color);

	private native void getScreenBuffer_native(long pointer);

	private native int getLatencyFrames_native(long pointer);

	private native int getLatencyTime_native(long pointer);

	private native int getRemainingLatencyFrames_native(long pointer);

	private native int getRemainingLatencyTime_native(long pointer);

	private native int getRevision_native(long pointer);

	private native boolean isDebug_native(long pointer);

	private native boolean isLatComEnabled_native(long pointer);

	private native void setLatCom_native(long pointer, boolean isEnabled);

	private native int getReplayFrameCount_native(long pointer);

	private native void setGUI_native(long pointer);

	private native void setGUI_native(long pointer, boolean enabled);

	private native int getAPM_native(long pointer);

	private native int getAPM_native(long pointer, boolean includeSelects);

	private native boolean setMap_native(long pointer, String cstr_mapFileName);

	private native void setFrameSkip_native(long pointer);

	private native void setFrameSkip_native(long pointer, int frameSkip);

	private native boolean hasPath_native(long pointer, Position source, Position destination);

	private native boolean setAlliance_native(long pointer, Player player, boolean allied);

	private native boolean setAlliance_native(long pointer, Player player);

	private native boolean setAlliance_native(long pointer, Player player, boolean allied, boolean alliedVictory);

	private native boolean setVision_native(long pointer, Player player);

	private native boolean setVision_native(long pointer, Player player, boolean enabled);

	private native void setCommandOptimizationLevel_native(long pointer);

	private native void setCommandOptimizationLevel_native(long pointer, int level);

	private native Region getRegionAt_native(long pointer, int x, int y);

	private native Region getRegionAt_native(long pointer, Position position);

	private native int getLastEventTime_native(long pointer);

	private native boolean setReplayVision_native(long pointer, Player player);

	private native boolean setReplayVision_native(long pointer, Player player, boolean enabled);

	private native boolean setRevealAll_native(long pointer);

	private native boolean setRevealAll_native(long pointer, boolean reveal);

}
