/*
   Copyright 2023-2023 Huynh Tien

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
package me.hsgamer.gamesinthebox.game.simple.feature;

import me.hsgamer.gamesinthebox.game.feature.PointFeature;
import me.hsgamer.gamesinthebox.game.feature.TopFeature;
import me.hsgamer.gamesinthebox.game.simple.SimpleGameArena;
import me.hsgamer.gamesinthebox.planner.feature.PluginFeature;
import me.hsgamer.minigamecore.base.Feature;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public class SimpleUpdateFeature implements Feature {
    private final Plugin plugin;
    private final SimpleGameArena arena;
    private BukkitTask task;

    public SimpleUpdateFeature(SimpleGameArena arena) {
        this.plugin = arena.getPlanner().getFeature(PluginFeature.class).getPlugin();
        this.arena = arena;
    }

    public void initState() {
        arena.getFeature(DescriptiveHologramFeature.class).initHologram();
        task = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this::updateState, 0L, 20L);
    }

    private void updateState() {
        arena.getFeature(PointFeature.class).takeTopSnapshot();
        arena.getFeature(TopFeature.class).setTop(arena.getFeature(PointFeature.class).getTopSnapshotAsStringPair());
        arena.getFeature(DescriptiveHologramFeature.class).updateHologram();
    }

    public void clearState() {
        if (task != null) {
            task.cancel();
        }
        arena.getFeature(PointFeature.class).clearPoints();
        arena.getFeature(DescriptiveHologramFeature.class).clearHologram();
    }

    @Override
    public void clear() {
        if (task != null) {
            task.cancel();
        }
    }
}
