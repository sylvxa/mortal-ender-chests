package lol.sylvie.mortalec.mixin;

import com.mojang.authlib.GameProfile;
import lol.sylvie.mortalec.MortalEnderChests;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.PlayerEnderChestContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gamerules.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player {
	public ServerPlayerMixin(Level level, GameProfile gameProfile) {
		super(level, gameProfile);
	}

	@Shadow
	public abstract ServerLevel level();

	@Unique
    private void dropItemFrom(PlayerEnderChestContainer enderChest, int i) {
		this.drop(enderChest.getItem(i), true, false);
		enderChest.setItem(i, ItemStack.EMPTY);
	}

	@Inject(at = @At(value = "INVOKE",
			target = "Lnet/minecraft/server/level/ServerPlayer;dropAllDeathLoot(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;)V",
			shift = At.Shift.AFTER),
			method = "die")
	private void mortalec$dropEnderChestLoot(DamageSource source, CallbackInfo info) {
		if ((!(source.getEntity() instanceof Player) && MortalEnderChests.config.onlyPlayers) ||
				(level().getGameRules().get(GameRules.KEEP_INVENTORY) && MortalEnderChests.config.respectKeepInventory)) {
			return;
		}

		PlayerEnderChestContainer enderChest = this.getEnderChestInventory();
		int enderChestSize = enderChest.items.size();
		long filledSlots = enderChest.items.stream().filter((i) -> !i.isEmpty()).count();
		if (MortalEnderChests.config.dropSlots >= filledSlots) {
			for (int i = 0; i < enderChest.items.size(); i++) {
				dropItemFrom(enderChest, i);
			}
		} else {
			int dropped = 0;
			while (dropped < MortalEnderChests.config.dropSlots) {
				int i = random.nextInt(0, enderChestSize);
				if (enderChest.getItem(i).isEmpty()) continue;
				dropItemFrom(enderChest, i);
				dropped++;
			}
		}
	}
}