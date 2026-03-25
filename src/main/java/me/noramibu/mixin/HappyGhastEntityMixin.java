package me.noramibu.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.happyghast.HappyGhast;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.minecart.MinecartChest;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.Team;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HappyGhast.class)
public abstract class HappyGhastEntityMixin {
    @Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
    private void mobInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        HappyGhast ghast = (HappyGhast) (Object) this;
        ItemStack itemStack = player.getItemInHand(hand);
        if (itemStack.is(Items.CHEST_MINECART) && ghast.getPassengers().size() < 3 && !ghast.getItemBySlot(EquipmentSlot.BODY).isEmpty()) {
            if (!ghast.level().isClientSide()) {
                MinecartChest chestMinecart = new MinecartChest(EntityType.CHEST_MINECART, ghast.level());
                chestMinecart.setInitialPos(ghast.getX(), ghast.getY(), ghast.getZ());
                ghast.level().addFreshEntity(chestMinecart);
                chestMinecart.startRiding(ghast);
                if (!player.getAbilities().instabuild) {
                    itemStack.shrink(1);
                }
                if (ghast.level() instanceof ServerLevel serverLevel) {
                    Scoreboard scoreboard = serverLevel.getScoreboard();
                    PlayerTeam team = scoreboard.getPlayerTeam("NoCollision");
                    if (team == null) {
                        team = scoreboard.addPlayerTeam("NoCollision");
                        team.setCollisionRule(Team.CollisionRule.NEVER);
                    } else if (team.getCollisionRule() != Team.CollisionRule.NEVER) {
                        team.setCollisionRule(Team.CollisionRule.NEVER);
                    }
                    scoreboard.addPlayerToTeam(chestMinecart.getScoreboardName(), team);
                }
            }
            cir.setReturnValue(InteractionResult.SUCCESS);
        }
    }
}
