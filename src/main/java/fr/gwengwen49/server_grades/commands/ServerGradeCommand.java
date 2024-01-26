package fr.gwengwen49.server_grades.commands;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import fr.gwengwen49.server_grades.Grade;
import fr.gwengwen49.server_grades.PlayerGrades;
import net.minecraft.command.argument.GameProfileArgumentType;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

public class ServerGradeCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("grade")
                .then((CommandManager.literal("list"))
                        .executes(context -> getGradesList()))
                .then(CommandManager.literal("create")
                        .then(CommandManager.argument("name", StringArgumentType.word())
                                .executes(context -> executeGradeCreation(context.getArgument("name", String.class),Optional.empty(), Optional.empty(), Optional.empty()))
                                .then(CommandManager.argument("parent", StringArgumentType.word())
                                        .executes(context -> executeGradeCreation(context.getArgument("name", String.class), Optional.empty(), Optional.empty(), Optional.of(context.getArgument("parent", String.class))))
                                        .then(CommandManager.argument("tab_priority", IntegerArgumentType.integer())
                                                .executes(context -> executeGradeCreation(context.getArgument("name", String.class), Optional.empty(), Optional.of(context.getArgument("tab_priority", int.class)), Optional.of(context.getArgument("parent", String.class))))
                                                .then(CommandManager.argument("display_name", StringArgumentType.greedyString())
                                                        .executes(context -> executeGradeCreation(context.getArgument("name", String.class), Optional.of(context.getArgument("display_name", String.class)), Optional.of(context.getArgument("tab_priority", int.class)), Optional.of(context.getArgument("parent", String.class)))))))))
                .then(CommandManager.literal("addPermissions")
                        .then(CommandManager.argument("grade_name", StringArgumentType.word())
                                .then(CommandManager.argument("permissions", StringArgumentType.greedyString())
                                        .executes(context -> executeAddPermissions(context.getArgument("grade_name", String.class), context.getArgument("permissions", String.class))))))
                .then(CommandManager.literal("add")
                        .then(CommandManager.argument("player_name", GameProfileArgumentType.gameProfile())
                                .then(CommandManager.argument("grade_name", StringArgumentType.word())
                                        .then(CommandManager.argument("priority", IntegerArgumentType.integer())
                                                .executes(context -> executeRankPlayer(context.getSource(), context.getArgument("player_name", GameProfile.class), context.getArgument("grade_name", String.class), context.getArgument("priority", Integer.class))))))));

    }

    private static int getGradesList(){
        List<String> gradesNames = new ArrayList<>();
        return gradesNames.size();
    }
    private static int executeGradeCreation(String name, Optional<String> displayName, Optional<Integer> tabPriority, Optional<String> parent) {
        try {
            Grade.create(name, displayName, tabPriority, parent);
        }
        catch (IOException e){
            e.printStackTrace(System.out);
        }
        return 1;
    }
    private static int executeAddPermissions(String gradeName, String permission) {
        Grade grade = Grade.fromFile(gradeName);
        if(grade != null) {
            grade.addPermissions(permission);
        }
        else {

        }
        return 1;
    }

    private static int executeRankPlayer(ServerCommandSource source, GameProfile profile, String grade, int priority){
        try {
            ServerPlayerEntity player = source.getServer().getPlayerManager().getPlayer(profile.getName());
            PlayerGrades.rankPlayer(player, grade, priority);
            source.getServer().getPlayerManager().sendToAll(new PlayerListS2CPacket(EnumSet.of(PlayerListS2CPacket.Action.UPDATE_DISPLAY_NAME, PlayerListS2CPacket.Action.UPDATE_LISTED), source.getServer().getPlayerManager().getPlayerList()));
            System.out.println("1");
        }catch (Exception e){
            e.printStackTrace(System.out);
        }
        return 1;
    }
}
