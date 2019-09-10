package com.biom4st3r.minerpg.impl.abilities;

import com.biom4st3r.minerpg.api.RPGAbility;
import com.biom4st3r.minerpg.util.RPGPlayer;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.EvokerFangsEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Direction.Axis;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;

public class EvokerFangsAbility extends RPGAbility {

    public EvokerFangsAbility(Identifier id, int coolDownDuration) {
        super(id, coolDownDuration);
        
    }

    @Override
    public void applyCost(RPGPlayer player) {
        // TODO Auto-generated method stub

    }

    protected void castSpell(LivingEntity source, Vec3d target) 
    {
        // LivingEntity target;
        double minHeight = Math.min(target.y, source.y);
        double maxHeight = Math.max(target.y, source.y) + 1.0D;
        float yaw = (float) MathHelper.atan2(target.z - source.z, target.x - source.x);
        int int_2;
        if (source.squaredDistanceTo(target) < 9.0D) 
        {
            float float_3;
            for (int_2 = 0; int_2 < 5; ++int_2) 
            {
                float_3 = yaw + (float) int_2 * 3.1415927F * 0.4F;
                this.conjureFangs(source, source.x + (double) MathHelper.cos(float_3) * 1.5D,
                        source.z + (double) MathHelper.sin(float_3) * 1.5D, minHeight, maxHeight, float_3, 0);
            }

            for (int_2 = 0; int_2 < 8; ++int_2) 
            {
                float_3 = yaw + (float) int_2 * 3.1415927F * 2.0F / 8.0F + 1.2566371F;
                this.conjureFangs(source, source.x + (double) MathHelper.cos(float_3) * 2.5D,
                        source.z + (double) MathHelper.sin(float_3) * 2.5D, minHeight, maxHeight, float_3, 3);
            }
        } 
        else 
        {
            for (int_2 = 0; int_2 < 16; ++int_2) 
            {
                double double_3 = 1.25D * (double) (int_2 + 1);
                int warnup = 1 * int_2;
                this.conjureFangs(
                    source,
                    source.x + (double) MathHelper.cos(yaw) * double_3,
                    source.z + (double) MathHelper.sin(yaw) * double_3, 
                    minHeight, 
                    maxHeight, 
                    yaw, 
                    warnup);
            }
        }

    }

    protected void conjureFangs(LivingEntity source, double xPos, double zPos, double minHeight, double maxHeight, float yaw, int warmup) 
    {
        BlockPos pos = new BlockPos(xPos, maxHeight, zPos);
        boolean validPos = false;
        double yOffset = 0.0D;
        do {
            BlockPos belowPos = pos.down();
            BlockState posBlockState = source.world.getBlockState(belowPos);
            if (posBlockState.isSideSolidFullSquare(source.world, belowPos, Direction.UP)) {
                if (!source.world.isAir(pos)) {
                    BlockState blockState_2 = source.world.getBlockState(pos);
                    VoxelShape voxelShape_1 = blockState_2.getCollisionShape(source.world, pos);
                    if (!voxelShape_1.isEmpty()) {
                        yOffset = voxelShape_1.getMaximum(Axis.Y);
                    }
                }
                validPos = true;
                break;
            }
            pos = pos.down();
        } while (pos.getY() >= MathHelper.floor(minHeight) - 1);

        if (validPos) {
            source.world.spawnEntity(new EvokerFangsEntity(source.world, xPos, (double) pos.getY() + yOffset,
                    zPos, yaw, warmup, source));
        }

    }

    @Override
    public boolean hasCost(RPGPlayer player) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean doAbility(RPGPlayer player) {
        if(!player.getRPGAbilityComponent().isOnCooldown(this))
        {
            double distance = 20f;
            Vec3d source = player.getPlayer().getCameraPosVec(1);
            Vec3d rot = player.getPlayer().getRotationVec(1);
            Vec3d end = source.add(rot.x * distance, 0, rot.z * distance);
            // RayTraceContext rtc = new RayTraceContext(source, rot, ShapeType.COLLIDER, FluidHandling.NONE, player.getPlayer());
            // player.getPlayer().world.rayTrace(rtc);
            castSpell(player.getPlayer(), end);
            player.getRPGAbilityComponent().addCooldown(this);
        }
        return false;
    }

    @Override
    public Type getType() {
        // TODO Auto-generated method stub
        return Type.LEFT_CLICK;
    }
    
}