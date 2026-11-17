package com.mystic.thither.eclipse.client.sky;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mystic.thither.utils.Reference;
import com.mystic.thither.eclipse.worldgen.biomes.EclipseZones;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.FogType;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public class EclipseDimensionSky extends DimensionSpecialEffects {
    private static final ResourceLocation SUN_PHASES = ResourceLocation.fromNamespaceAndPath(Reference.MODID, "textures/eclipse/sky/sun_phases.png");
    private static final ResourceLocation MOON_PHASES = ResourceLocation.fromNamespaceAndPath(Reference.MODID, "textures/eclipse/sky/moon_phases.png");

    public EclipseDimensionSky() {
        super(192.0F, true, SkyType.NORMAL, false, false);
    }

    @Override
    public Vec3 getBrightnessDependentFogColor(Vec3 color, float brightness) {
        return color.multiply(brightness * 0.94F + 0.06F, brightness * 0.94F + 0.06F, brightness * 0.91F + 0.09F);
    }

    @Override
    public boolean isFoggyAt(int x, int z) {
        return false;
    }

    @Override
    public boolean renderSky(ClientLevel level, int ticks, float partialTick, PoseStack poseStack, Camera camera, Matrix4f projectionMatrix, boolean isFoggy, Runnable setupFog) {
        Vec3 skyColor = level.getSkyColor(camera.getPosition(), partialTick);
        RenderSystem.depthMask(false);
        RenderSystem.setShaderColor((float) skyColor.x, (float) skyColor.y, (float) skyColor.z, 1.0F);
        RenderSystem.setShader(GameRenderer::getPositionShader);
        RenderSystem.enableBlend();

        float rainVisibility = 1.0F - level.getRainLevel(partialTick);
        poseStack.pushPose();
        poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(-90.0F));
        poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(level.getTimeOfDay(partialTick) * 360.0F));
        Matrix4f celestial = poseStack.last().pose();

        RenderSystem.disableDepthTest();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, SUN_PHASES);
        drawPhase(celestial, 30.0F, 100.0F, level.getMoonPhase());

        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, rainVisibility);
        RenderSystem.setShaderTexture(0, MOON_PHASES);
        drawPhase(celestial, 20.0F, -100.0F, level.getMoonPhase());
        RenderSystem.enableDepthTest();

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
        poseStack.popPose();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.depthMask(true);
        return true;
    }

    private static void drawPhase(Matrix4f matrix, float size, float y, int phase) {
        int column = phase % 4;
        int row = phase / 4 % 2;
        float padU = 0.5F / 128.0F;
        float padV = 0.5F / 64.0F;
        float u0 = column / 4.0F + padU;
        float v0 = row / 2.0F + padV;
        float u1 = (column + 1) / 4.0F - padU;
        float v1 = (row + 1) / 2.0F - padV;
        BufferBuilder buffer = Tesselator.getInstance().getBuilder();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        if (y > 0.0F) {
            buffer.vertex(matrix, -size, y, -size).uv(u0, v0).endVertex();
            buffer.vertex(matrix, size, y, -size).uv(u1, v0).endVertex();
            buffer.vertex(matrix, size, y, size).uv(u1, v1).endVertex();
            buffer.vertex(matrix, -size, y, size).uv(u0, v1).endVertex();
        } else {
            buffer.vertex(matrix, -size, y, size).uv(u1, v1).endVertex();
            buffer.vertex(matrix, size, y, size).uv(u0, v1).endVertex();
            buffer.vertex(matrix, size, y, -size).uv(u0, v0).endVertex();
            buffer.vertex(matrix, -size, y, -size).uv(u1, v0).endVertex();
        }
        BufferUploader.drawWithShader(buffer.end());
    }
}
