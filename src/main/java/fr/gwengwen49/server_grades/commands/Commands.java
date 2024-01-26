package fr.gwengwen49.server_grades.commands;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class Commands {

    public static void initialize() {

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            ServerGradeCommand.register(dispatcher);
        });
    }
}
