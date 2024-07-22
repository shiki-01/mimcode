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
    private final MineCordButtonState[][] states;
    private final boolean signalState = false;
    private final String message = "";
    private BlockPos blockPos; // Transient to avoid serialization issues

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

    public static Map<BlockPos, MineCordBlockState> loadFromFile(String fileName) throws IOException {
        File file = new File(fileName);
        if (!file.exists()) {
            return new HashMap<>();
        }

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
            return blockStates;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }

    public void setBlockPos(BlockPos blockPos) {
        this.blockPos = blockPos;
    }
}