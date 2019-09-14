// public void attack(Entity targetEntity) {
//       if (targetEntity.isAttackable()) {
//          if (!targetEntity.handleAttack(this)) {
//             float baseDamage = (float)this.getAttributeInstance(EntityAttributes.ATTACK_DAMAGE).getValue();
//             float damage;
//             if (targetEntity instanceof LivingEntity) {
//                damage = EnchantmentHelper.getAttackDamage(this.getMainHandStack(), ((LivingEntity)targetEntity).getGroup());
//             } else {
//                damage = EnchantmentHelper.getAttackDamage(this.getMainHandStack(), EntityGroup.DEFAULT);
//             }

//             float coolDownNerf = this.getAttackCooldownProgress(0.5F);
//             baseDamage *= 0.2F + coolDownNerf * coolDownNerf * 0.8F;
//             damage *= coolDownNerf;
//             this.resetLastAttackedTicks();
//             if (baseDamage > 0.0F || damage > 0.0F) {
//                boolean cooledDown = coolDownNerf > 0.9F;
//                boolean sprinting = false;
//                int knockBackLvl = 0;
//                int knockBackLvl = knockBackLvl + EnchantmentHelper.getKnockback(this);
//                if (this.isSprinting() && cooledDown) {
//                   this.world.playSound((PlayerEntity)null, this.x, this.y, this.z, SoundEvents.ENTITY_PLAYER_ATTACK_KNOCKBACK, this.getSoundCategory(), 1.0F, 1.0F);
//                   ++knockBackLvl;
//                   sprinting = true;
//                }

//                boolean fallingCrit = cooledDown && this.fallDistance > 0.0F && !this.onGround && !this.isClimbing() && !this.isInsideWater() && !this.hasStatusEffect(StatusEffects.BLINDNESS) && !this.hasVehicle() && targetEntity instanceof LivingEntity;
//                fallingCrit = fallingCrit && !this.isSprinting();
//                if (fallingCrit) {
//                   baseDamage *= 1.5F;
//                }

//                baseDamage += damage;
//                boolean slowingDownAndSword = false;
//                double slowingDown = (double)(this.horizontalSpeed - this.prevHorizontalSpeed);
//                if (cooledDown && !fallingCrit && !sprinting && this.onGround && slowingDown < (double)this.getMovementSpeed()) {
//                   ItemStack itemStack_1 = this.getStackInHand(Hand.MAIN_HAND);
//                   if (itemStack_1.getItem() instanceof SwordItem) {
//                      slowingDownAndSword = true;
//                   }
//                }

//                float targetHealth = 0.0F;
//                boolean hasFireAspect = false;
//                int fireAspectLevel = EnchantmentHelper.getFireAspect(this);
//                if (targetEntity instanceof LivingEntity) {
//                   targetHealth = ((LivingEntity)targetEntity).getHealth();
//                   if (fireAspectLevel > 0 && !targetEntity.isOnFire()) {
//                      hasFireAspect = true;
//                      targetEntity.setOnFireFor(1);
//                   }
//                }

//                Vec3d vec3d_1 = targetEntity.getVelocity();
//                boolean boolean_6 = targetEntity.damage(DamageSource.player(this), baseDamage);
//                if (boolean_6) {
//                   if (knockBackLvl > 0) {
//                      if (targetEntity instanceof LivingEntity) {
//                         ((LivingEntity)targetEntity).takeKnockback(this, (float)knockBackLvl * 0.5F, (double)MathHelper.sin(this.yaw * 0.017453292F), (double)(-MathHelper.cos(this.yaw * 0.017453292F)));
//                      } else {
//                         targetEntity.addVelocity((double)(-MathHelper.sin(this.yaw * 0.017453292F) * (float)knockBackLvl * 0.5F), 0.1D, (double)(MathHelper.cos(this.yaw * 0.017453292F) * (float)knockBackLvl * 0.5F));
//                      }

//                      this.setVelocity(this.getVelocity().multiply(0.6D, 1.0D, 0.6D));
//                      this.setSprinting(false);
//                   }

//                   if (slowingDownAndSword) {
//                      float float_6 = 1.0F + EnchantmentHelper.getSweepingMultiplier(this) * baseDamage;
//                      List<LivingEntity> list_1 = this.world.getEntities(LivingEntity.class, targetEntity.getBoundingBox().expand(1.0D, 0.25D, 1.0D));
//                      Iterator var19 = list_1.iterator();

//                      label166:
//                      while(true) {
//                         LivingEntity livingtargetEntity;
//                         do {
//                            do {
//                               do {
//                                  do {
//                                     if (!var19.hasNext()) {
//                                        this.world.playSound((PlayerEntity)null, this.x, this.y, this.z, SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, this.getSoundCategory(), 1.0F, 1.0F);
//                                        this.method_7263();
//                                        break label166;
//                                     }

//                                     livingtargetEntity = (LivingEntity)var19.next();
//                                  } while(livingtargetEntity == this);
//                               } while(livingtargetEntity == targetEntity);
//                            } while(this.isTeammate(livingtargetEntity));
//                         } while(livingtargetEntity instanceof ArmorStandEntity && ((ArmorStandEntity)livingtargetEntity).isMarker());

//                         if (this.squaredDistanceTo(livingtargetEntity) < 9.0D) {
//                            livingtargetEntity.takeKnockback(this, 0.4F, (double)MathHelper.sin(this.yaw * 0.017453292F), (double)(-MathHelper.cos(this.yaw * 0.017453292F)));
//                            livingtargetEntity.damage(DamageSource.player(this), float_6);
//                         }
//                      }
//                   }

//                   if (targetEntity instanceof ServerPlayerEntity && targetEntity.velocityModified) {
//                      ((ServerPlayerEntity)targetEntity).networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(targetEntity));
//                      targetEntity.velocityModified = false;
//                      targetEntity.setVelocity(vec3d_1);
//                   }

//                   if (fallingCrit) {
//                      this.world.playSound((PlayerEntity)null, this.x, this.y, this.z, SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, this.getSoundCategory(), 1.0F, 1.0F);
//                      this.addCritParticles(targetEntity);
//                   }

//                   if (!fallingCrit && !slowingDownAndSword) {
//                      if (cooledDown) {
//                         this.world.playSound((PlayerEntity)null, this.x, this.y, this.z, SoundEvents.ENTITY_PLAYER_ATTACK_STRONG, this.getSoundCategory(), 1.0F, 1.0F);
//                      } else {
//                         this.world.playSound((PlayerEntity)null, this.x, this.y, this.z, SoundEvents.ENTITY_PLAYER_ATTACK_WEAK, this.getSoundCategory(), 1.0F, 1.0F);
//                      }
//                   }

//                   if (damage > 0.0F) {
//                      this.addEnchantedHitParticles(targetEntity);
//                   }

//                   this.onAttacking(targetEntity);
//                   if (targetEntity instanceof LivingEntity) {
//                      EnchantmentHelper.onUserDamaged((LivingEntity)targetEntity, this);
//                   }

//                   EnchantmentHelper.onTargetDamaged(this, targetEntity);
//                   ItemStack itemStack_2 = this.getMainHandStack();
//                   Entity entity_2 = targetEntity;
//                   if (targetEntity instanceof EnderDragonPart) {
//                      entity_2 = ((EnderDragonPart)targetEntity).owner;
//                   }

//                   if (!this.world.isClient && !itemStack_2.isEmpty() && entity_2 instanceof LivingEntity) {
//                      itemStack_2.postHit((LivingEntity)entity_2, this);
//                      if (itemStack_2.isEmpty()) {
//                         this.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
//                      }
//                   }

//                   if (targetEntity instanceof LivingEntity) {
//                      float float_7 = targetHealth - ((LivingEntity)targetEntity).getHealth();
//                      this.increaseStat(Stats.DAMAGE_DEALT, Math.round(float_7 * 10.0F));
//                      if (fireAspectLevel > 0) {
//                         targetEntity.setOnFireFor(fireAspectLevel * 4);
//                      }

//                      if (this.world instanceof ServerWorld && float_7 > 2.0F) {
//                         int int_3 = (int)((double)float_7 * 0.5D);
//                         ((ServerWorld)this.world).spawnParticles(ParticleTypes.DAMAGE_INDICATOR, targetEntity.x, targetEntity.y + (double)(targetEntity.getHeight() * 0.5F), targetEntity.z, int_3, 0.1D, 0.0D, 0.1D, 0.2D);
//                      }
//                   }

//                   this.addExhaustion(0.1F);
//                } else {
//                   this.world.playSound((PlayerEntity)null, this.x, this.y, this.z, SoundEvents.ENTITY_PLAYER_ATTACK_NODAMAGE, this.getSoundCategory(), 1.0F, 1.0F);
//                   if (hasFireAspect) {
//                      targetEntity.extinguish();
//                   }
//                }
//             }

//          }
//       }
//    }