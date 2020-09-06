package me.glitch.aitecraft.coordsfabric;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents.ServerStarted;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents.ServerStopping;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.WorldSavePath;
import me.glitch.aitecraft.coordsfabric.coord.Coord;

public final class CoordsFileManager implements ServerStarted, ServerStopping {

    private ArrayList<Coord> coordsList;

    public CoordsFileManager(ArrayList<Coord> coordsList)
    {
        this.coordsList = coordsList;
    }

    @Override
    public void onServerStarted(MinecraftServer server)
    {
        this.loadFromSaveFile(server);
    }
    
    @Override
    public void onServerStopping(MinecraftServer server)
    {
        this.writeToSaveFile(server);
    }

    private void loadFromSaveFile(MinecraftServer server) 
    {
        this.coordsList.clear();

        File saveFile = getFile(server);
        if (saveFile.exists()) try {

            FileInputStream saveFileInputStream = new FileInputStream(saveFile);
            ObjectInputStream saveObjectInputStream = new ObjectInputStream(saveFileInputStream);
            
            ArrayList<?> inputList = (ArrayList<?>) saveObjectInputStream.readObject();
            
            for (Object o : inputList) {
                this.coordsList.add((Coord) o);
            }

            saveObjectInputStream.close();
            saveFileInputStream.close();

            System.out.println("[Coords] Loaded " + this.coordsList.size() + " coordinates from file.");

        } catch (IOException | ClassNotFoundException x) {
            System.out.println("[Coords] Error while loading: " + x.toString());
        }
    }

    private void writeToSaveFile(MinecraftServer server) 
    {
        if (this.coordsList.size() > 0) {
            File saveFile = getFile(server);
            try {
                saveFile.createNewFile();

                FileOutputStream saveFileOutputStream = new FileOutputStream(saveFile);
                ObjectOutputStream saveObjectOutputStream = new ObjectOutputStream(saveFileOutputStream);
                saveObjectOutputStream.writeObject(this.coordsList);
                saveObjectOutputStream.close();
                saveFileOutputStream.close();

                System.out.println("[Coords] Saved " + this.coordsList.size() + " coordinates to file.");

            } catch (IOException x) {
                System.out.println("[Coords] Error while saving: " + x.toString());
            }
        }
    }

    private static File getFile(MinecraftServer server) {
		return server.getSavePath(WorldSavePath.ROOT).resolve("coords.sav").toFile();
	}
}
