package it.unibo.scotyard.model.game;

import it.unibo.scotyard.model.players.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Players {
    private final GameMode gameMode;
    private final MisterX misterX;
    private final Detective detective;
    private final List<Bobby> bobbies;
    private final List<Player> turnOrder;

    public Players(
            final GameMode gameMode, final MisterX misterX, final Detective detective, final List<Bobby> bobbies) {
        this.gameMode = gameMode;
        this.misterX = misterX;
        this.detective = detective;
        this.bobbies = bobbies;
        this.turnOrder = createTurnOrder(misterX, detective, bobbies);
    }

    public MisterX getMisterX() {
        return misterX;
    }

    public Detective getDetective() {
        return detective;
    }

    public List<Bobby> getBobbies() {
        return bobbies;
    }

    public Player getUserPlayer() {
        if (gameMode == GameMode.MISTER_X) {
            return misterX;
        } else {
            return detective;
        }
    }

    public Player getComputerPlayer() {
        if (gameMode == GameMode.MISTER_X) {
            return detective;
        } else {
            return misterX;
        }
    }

    public Stream<Player> getSeekers() {
        return Stream.concat(Stream.of(detective), bobbies.stream());
    }

    public List<Player> getTurnOrder() {
        return turnOrder;
    }

    public int getPlayersCount() {
        return turnOrder.size();
    }

    private static List<Player> createTurnOrder(
            final MisterX misterX, final Detective detective, final List<Bobby> bobbies) {
        return Stream.concat(Stream.of(misterX, detective), bobbies.stream()).collect(Collectors.toList());
    }
}
