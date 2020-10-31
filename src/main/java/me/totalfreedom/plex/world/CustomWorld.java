package me.totalfreedom.plex.world;

import me.totalfreedom.plex.Plex;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.generator.ChunkGenerator;

import java.util.Objects;

public class CustomWorld extends WorldCreator
{
    private static Plex plugin = Plex.get();

    private final CustomChunkGenerator chunks;

    private CustomWorld(String name, CustomChunkGenerator generator)
    {
        super(name);
        this.chunks = generator;
        this.generator(this.chunks);
    }

    @Override
    public ChunkGenerator generator()
    {
        return chunks;
    }

    public World generate()
    {
        return this.createWorld();
    }

    public static World generateConfigFlatWorld(String name)
    {
        if (!plugin.config.contains("worlds." + name))
            return null;
        CustomWorld customWorld = new CustomWorld(name, new ConfigurationChunkGenerator(name))
        {
            @Override
            public World generate()
            {
                boolean addFeatures = Bukkit.getWorld(name) == null;
                World world = super.generate();
                if (addFeatures)
                {
                    Block block = world.getBlockAt(0, world.getHighestBlockYAt(0, 0) + 1, 0);
                    block.setType(Material.OAK_SIGN);
                    BlockState state = block.getState();
                    if (state instanceof Sign)
                    {
                        Sign sign = (Sign) state;
                        sign.setLine(1, Objects.requireNonNull(plugin.config.getString("worlds." + name + ".name")));
                        sign.setLine(2, "- 0, 0 -");
                        sign.update();
                    }
                }
                return world;
            }
        };
        return customWorld.generate();
    }
}