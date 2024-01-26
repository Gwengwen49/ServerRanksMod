package fr.gwengwen49.server_grades.mixin;

import com.mojang.authlib.GameProfile;
import fr.gwengwen49.server_grades.Grade;
import fr.gwengwen49.server_grades.PlayerGrades;
import net.fabricmc.fabric.api.biome.v1.BiomeModification;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class ServerPlayerEntityMixin {


    @Shadow
    public GameProfile getGameProfile() {
        return null;
    }

    @Shadow @Final private GameProfile gameProfile;

    @Inject(at = @At("HEAD"), method = "getName")
    public void modifyName(CallbackInfoReturnable<Text> cir) {
    }

    @Inject(at = @At("HEAD"), method = "getDisplayName", cancellable = true)
    public void modifyDisplayName(CallbackInfoReturnable<Text> cir) {
        cir.setReturnValue(Grade.toDisplayable(gameProfile.getName()));
    }

}
