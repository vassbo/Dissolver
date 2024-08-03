package net.vassbo.vanillaemc.render;

import org.joml.Quaternionf;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.vassbo.vanillaemc.VanillaEMC;
import net.vassbo.vanillaemc.entity.CrystalEntity;

@Environment(EnvType.CLIENT)
public class CrystalEntityRenderer extends EntityRenderer<CrystalEntity> {
	private static final Identifier TEXTURE = Identifier.of(VanillaEMC.MOD_ID, "textures/entity/crystal_entity.png");
	private static final RenderLayer CRYSTAL_RENDER = RenderLayer.getEntityCutoutNoCull(TEXTURE);
	private static final float SINE_45_DEGREES = (float)Math.sin(Math.PI / 4);
	// private static final String GLASS = "glass";
	// private static final String BASE = "base";
	private final ModelPart core;
	private final ModelPart frame;
	// private final ModelPart bottom;
	private final float SCALE = 0.8F;
	private final float SPEED = 0.5F;

	public CrystalEntityRenderer(EntityRendererFactory.Context context) {
		super(context);
		ModelPart modelPart = context.getPart(EntityModelLayers.END_CRYSTAL);
		this.frame = modelPart.getChild("glass");
		this.core = modelPart.getChild(EntityModelPartNames.CUBE);
		// this.bottom = modelPart.getChild("base");
		
		this.shadowRadius = 0.0F;
		this.shadowOpacity = 0.0F;
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		modelPartData.addChild("glass", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F), ModelTransform.NONE);
		modelPartData.addChild(EntityModelPartNames.CUBE, ModelPartBuilder.create().uv(32, 0).cuboid(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F), ModelTransform.NONE);
		return TexturedModelData.of(modelData, 64, 32);
	}

	public void render(CrystalEntity crystalEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
		matrixStack.push();
		float j = ((float)crystalEntity.crystalAge + g) * SPEED;
		VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(CRYSTAL_RENDER);
		matrixStack.push();
		
		int k = OverlayTexture.DEFAULT_UV;
		
		matrixStack.scale(SCALE, SCALE, SCALE);
		for (int index = 0; index < 3; index++) {
			matrixStack.scale(0.8F, 0.8F, 0.8F);
			matrixStack.multiply(new Quaternionf().setAngleAxis((float) (Math.PI / 3), SINE_45_DEGREES, 0.0F, SINE_45_DEGREES));
			matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(j));
			this.frame.render(matrixStack, vertexConsumer, i, k);
		}

		// matrixStack.scale(0.875F, 0.875F, 0.875F);
		// matrixStack.multiply(new Quaternionf().setAngleAxis((float) (Math.PI / 3), SINE_45_DEGREES, 0.0F, SINE_45_DEGREES));
		// matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(j));
		// this.frame.render(matrixStack, vertexConsumer, i, k);

		matrixStack.scale(0.7F, 0.7F, 0.7F);
		// // matrixStack.push();
		// // matrixStack.multiply(test, 0.0F, 0.0F, 0.0F);
		// // matrixStack.multiply(test2, 0.0F, 0.0F, 0.0F);
		// for (int index = 0; index < 3; index++) {
		// 	// Quaternionf newStack = new Quaternionf(matrixStack.peek());
		// 	// matrixStack.scale(index, k, index);
		// 	matrixStack.push();
		// 	// float ANGLE = SINE_45_DEGREES * (index);
		// 	Quaternionf test = new Quaternionf().setAngleAxis((float) (Math.PI / 3), SINE_45_DEGREES, 0.0F, SINE_45_DEGREES);
		// 	Quaternionf test2 = RotationAxis.POSITIVE_Y.rotationDegrees(j);
		// 	// Quaternionf test2 = RotationAxis.POSITIVE_Y.rotationDegrees(j + (index * 30));
		// 	// Quaternionf test3 = RotationAxis.POSITIVE_X.rotationDegrees(j + (index * 30));
		// 	matrixStack.multiply(test, 0.0F + (index * 2), 0.0F, 0.0F);
		// 	matrixStack.multiply(test2, 0.0F + (index * 2), 0.0F, 0.0F);
		// 	// matrixStack.multiply(test3, 0.0F, 0.0F, 0.0F);
		// 	this.core.render(matrixStack, vertexConsumer, i, k);
		// 	matrixStack.pop();
		// }

		matrixStack.multiply(new Quaternionf().setAngleAxis((float) (Math.PI / 3), SINE_45_DEGREES, 0.0F, SINE_45_DEGREES));
		matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(j));
		this.core.render(matrixStack, vertexConsumer, i, k);

		matrixStack.pop();
		matrixStack.pop();

		super.render(crystalEntity, f, g, matrixStack, vertexConsumerProvider, i);
	}

	public Identifier getTexture(CrystalEntity crystalEntity) {
		return TEXTURE;
	}

	public boolean shouldRender(CrystalEntity crystalEntity, Frustum frustum, double d, double e, double f) {
		return super.shouldRender(crystalEntity, frustum, d, e, f); // || crystalEntity.getBeamTarget() != null;
	}
}
