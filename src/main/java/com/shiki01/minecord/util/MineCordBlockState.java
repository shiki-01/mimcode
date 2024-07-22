package com.shiki01.minecord.util;

import com.shiki01.minecord.client.gui.MineCordButtonState;
import net.minecraft.core.BlockPos;
import java.io.*;
import java.util.UUID;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MineCordBlockState implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private MineCordButtonState[][] states;
    private boolean signalState = false;
    private String message = "";

    public MineCordBlockState(MineCordButtonState[][] states, boolean signalState, String message) {
        this.states = states;
        this.signalState = signalState;
        this.message = message;
    }

    public void codeBlockStatus(BlockPos blockPos, Map<BlockPos, MineCordBlockState> blockStates) {
        MineCordBlockState state = blockStates.get(blockPos);
        if (state != null) {
            this.signalState = state.getSignalState();
            this.message = state.getMessage();
            this.states = state.getStates();
        } else {
            this.signalState = false;
            this.message = "";
            this.states = new MineCordButtonState[3][3];
            for (MineCordButtonState[] row : this.states) {
                Arrays.fill(row, MineCordButtonState.DISABLED);
            }
        }
    }

    public static void saveToFile(Map<BlockPos, MineCordBlockState> blockStates, String filePath) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath))) {
            out.writeInt(blockStates.size());
            for (Map.Entry<BlockPos, MineCordBlockState> entry : blockStates.entrySet()) {
                out.writeInt(entry.getKey().getX());
                out.writeInt(entry.getKey().getY());
                out.writeInt(entry.getKey().getZ());
                MineCordBlockState state = entry.getValue();
                out.writeBoolean(state.getSignalState());
                out.writeObject(state.getMessage());
                MineCordButtonState[][] states = state.getStates();
                out.writeInt(states.length);
                for (MineCordButtonState[] row : states) {
                    for (MineCordButtonState cell : row) {
                        out.writeInt(cell.ordinal());
                    }
                }
            }
        }
    }

    public static Map<BlockPos, MineCordBlockState> loadFromFile(String fileName) throws IOException, ClassNotFoundException {
        File file = new File(fileName);
        if (!file.exists() || file.length() == 0) {
            MineCordLogger.logger.error("File does not exist or is empty");
            return new HashMap<>();
        }

        try (FileInputStream fis = new FileInputStream(file);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            int mapSize = ois.readInt();
            Map<BlockPos, MineCordBlockState> blockStates = new HashMap<>(mapSize);
            for (int i = 0; i < mapSize; i++) {
                int x = ois.readInt();
                int y = ois.readInt();
                int z = ois.readInt();
                BlockPos pos = new BlockPos(x, y, z);
                boolean signalState = ois.readBoolean();
                String message = (String) ois.readObject();
                int statesLength = ois.readInt();
                MineCordButtonState[][] states = new MineCordButtonState[statesLength][statesLength];
                for (int j = 0; j < statesLength; j++) {
                    for (int k = 0; k < statesLength; k++) {
                        states[j][k] = MineCordButtonState.values()[ois.readInt()];
                    }
                }
                MineCordBlockState state = new MineCordBlockState(states, signalState, message);
                state.setSignalState(signalState);
                state.setMessage(message);
                blockStates.put(pos, state);
            }
            return blockStates;
        } catch (EOFException e) {
            MineCordLogger.logger.error("Reached end of file unexpectedly", e);
            return new HashMap<>();
        } catch (IOException | ClassNotFoundException e) {
            MineCordLogger.logger.error("Failed to load block states", e);
            return new HashMap<>();
        }
    }

    public String getMessage() {
        return message;
    }

    public MineCordButtonState[][] getStates() {
        return states;
    }

    public boolean getSignalState() {
        return signalState;
    }

    public void setMessage(String value) {
        this.message = value;
    }

    public void setSignalState(boolean signalState) {
        this.signalState = signalState;
    }
}