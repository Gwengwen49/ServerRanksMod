package fr.gwengwen49.server_grades;

import fr.gwengwen49.server_grades.commands.Commands;
import fr.gwengwen49.server_grades.mixin.ServerPlayerEntityMixin;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerLoginConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;

import java.io.File;
import java.util.List;
import java.util.Map;

public class ServerGradesMod implements DedicatedServerModInitializer {


    @Override
    public void onInitializeServer() {
        Commands.initialize();
            File file = new File("grades\\data");
            if(!file.exists()){
                if(file.mkdirs()) System.out.println("directory created !!!!!!!!!");
                else System.out.println("directory not created !!!!!!!!!");
            }
            else {
            }
            System.out.println("initialize");
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            if(!PlayerGrades.playerExists(handler.player.getGameProfile().getName())){
                PlayerGrades.addPlayer(handler.player.getGameProfile().getName(), Map.of());
            }
        });
        PlayerGrades.initialize();
    }
}
