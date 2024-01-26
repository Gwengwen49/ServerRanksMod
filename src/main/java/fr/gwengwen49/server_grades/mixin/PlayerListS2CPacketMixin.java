package fr.gwengwen49.server_grades.mixin;

import com.mojang.authlib.GameProfile;
import fr.gwengwen49.server_grades.Grade;
import net.fabricmc.fabric.impl.client.keybinding.KeyBindingRegistryImpl;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.*;
import net.minecraft.util.Colors;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.*;
import java.util.Collection;
import java.util.EnumSet;

@Mixin(PlayerListS2CPacket.Entry.class)
public class PlayerListS2CPacketMixin {

    @Shadow @Final private @Nullable GameProfile profile;

    @Inject(at = @At("HEAD"), method = "displayName", cancellable = true)
    public void modifyEntryData(CallbackInfoReturnable<Text> cir){
        cir.setReturnValue(Grade.toDisplayable(profile.getName()));
    }
}
