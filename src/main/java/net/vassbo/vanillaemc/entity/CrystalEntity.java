package net.vassbo.vanillaemc.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

public class CrystalEntity extends Entity {
	public int crystalAge;

	public CrystalEntity(EntityType<? extends CrystalEntity> entityType, World world) {
		super(entityType, world);
		this.intersectionChecked = true;
		this.crystalAge = this.random.nextInt(100000);
	}

	// public CrystalEntity(World world, double x, double y, double z) {
	// 	this(ModEntities.CRYSTAL_ENTITY_TYPE, world);
	// 	this.setPosition(x, y, z);
	// }

	@Override
	protected Entity.MoveEffect getMoveEffect() {
		return Entity.MoveEffect.NONE;
	}

	@Override
	protected void initDataTracker(DataTracker.Builder builder) {
	}

	@Override
	public void tick() {
		this.crystalAge++;
		this.checkBlockCollision();
		this.tickPortalTeleportation();
	}

	@Override
	protected void writeCustomDataToNbt(NbtCompound nbt) {
	}

	@Override
	protected void readCustomDataFromNbt(NbtCompound nbt) {
	}

	@Override
	public boolean canHit() {
		return false;
	}

	@Override
	public boolean shouldRender(double distance) {
		return super.shouldRender(distance);
	}
}
