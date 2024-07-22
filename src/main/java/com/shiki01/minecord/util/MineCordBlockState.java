package com.shiki01.minecord.util;

import com.shiki01.minecord.client.gui.MineCordButtonState;
import net.minecraft.core.BlockPos;
import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MineCordBlockState implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private MineCordButtonState[][] states;
    private boolean signalState = false;
    private String message = "";

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

    public MineCordBlockState(MineCordButtonState[][] states) {
        this.states = states;
    }

    public static void saveToFile(Map<BlockPos, MineCordBlockState> blockStates, String filePath) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath))) {
            out.writeObject(blockStates.size());
            for (Map.Entry<BlockPos, MineCordBlockState> entry : blockStates.entrySet()) {
                out.writeInt(entry.getKey().getX());
                out.writeInt(entry.getKey().getY());
                out.writeInt(entry.getKey().getZ());
                out.writeObject(entry.getValue());
            }
        }
    }

    public static Map<BlockPos, MineCordBlockState> loadFromFile(String fileName) throws IOException, ClassNotFoundException {
        File file = new File(fileName);
        if (!file.exists() || file.length() == 0) {
            MineCordLogger.logger.error("File does not exist or is empty");
            return new HashMap<>();
        }
        
        MineCordLogger.logger.info(String.valueOf(file));

        try (FileInputStream fis = new FileInputStream(file);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            int size = ois.readInt();
            Map<BlockPos, MineCordBlockState> blockStates = new HashMap<>();
            for (int i = 0; i < size; i++) {
                int x = ois.readInt();
                int y = ois.readInt();
                int z = ois.readInt();
                BlockPos pos = new BlockPos(x, y, z);
                MineCordBlockState state = (MineCordBlockState) ois.readObject();
                blockStates.put(pos, state);
            }
            MineCordLogger.logger.info(blockStates.toString());
            return blockStates;
        } catch (IOException | ClassNotFoundException e) {
            MineCordLogger.logger.error("Failed to load block states", e);
            return new HashMap<>();
        }
    }

    public void setBlockPos(BlockPos blockPos) {
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
}