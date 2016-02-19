package net.kyau.afterhours.client.renderer;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.ChunkRenderContainer;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderList;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VboRenderList;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.chunk.IRenderChunkFactory;
import net.minecraft.client.renderer.chunk.ListChunkFactory;
import net.minecraft.client.renderer.chunk.VboChunkFactory;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.IRenderHandler;

import org.lwjgl.opengl.GL11;

public class RenderSkyVoid extends IRenderHandler {

  private static final ResourceLocation locationNebulaPng = new ResourceLocation("afterhours:textures/environment/nebula.png");
  private static final ResourceLocation locationForcefieldPng = new ResourceLocation("textures/misc/forcefield.png");
  private final TextureAtlasSprite[] destroyBlockIcons = new TextureAtlasSprite[10];

  private final TextureManager renderEngine;
  private final RenderManager renderManager;
  private VertexFormat vertexBufferFormat;

  private ChunkRenderContainer renderContainer;
  IRenderChunkFactory renderChunkFactory;
  boolean vboEnabled;
  /** The star GL Call list */
  private int starGLCallList = -1;
  /** The star GL Call list */
  private int starGLCallList2 = -1;
  /** OpenGL sky list */
  private int glSkyList = -1;
  /** OpenGL sky list 2 */
  private int glSkyList2 = -1;
  /** A reference to the Minecraft object. */
  private final Minecraft mc;

  private VertexBuffer skyVBO;
  private VertexBuffer sky2VBO;
  private VertexBuffer starVBO;
  private VertexBuffer star2VBO;

  WorldClient world;

  public RenderSkyVoid() {
    this.mc = Minecraft.getMinecraft();
    this.world = mc.theWorld;
    this.renderManager = mc.getRenderManager();
    this.renderEngine = mc.getTextureManager();
    this.renderEngine.bindTexture(locationForcefieldPng);
    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
    GlStateManager.bindTexture(0);
    this.updateDestroyBlockIcons();
    this.vboEnabled = OpenGlHelper.useVbo();

    if (this.vboEnabled) {
      this.renderContainer = new VboRenderList();
      this.renderChunkFactory = new VboChunkFactory();
    } else {
      this.renderContainer = new RenderList();
      this.renderChunkFactory = new ListChunkFactory();
    }

    this.vertexBufferFormat = new VertexFormat();
    this.vertexBufferFormat.addElement(new VertexFormatElement(0, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUsage.POSITION, 3));
    this.generateStars();
    this.generateStars2();
    this.generateSky();
    this.generateSky2();
  }

  private void updateDestroyBlockIcons() {
    TextureMap texturemap = this.mc.getTextureMapBlocks();

    for (int i = 0; i < this.destroyBlockIcons.length; ++i) {
      this.destroyBlockIcons[i] = texturemap.getAtlasSprite("minecraft:blocks/destroy_stage_" + i);
    }
  }

  @Override
  public void render(float partialTicks, WorldClient world, Minecraft mc) {
    GlStateManager.pushMatrix();
    GlStateManager.color(1f, 1f, 1f, 0.5f);
    renderSkyVanilla(partialTicks, 2);
    GlStateManager.popMatrix();
  }

  public void renderSkyVanilla(float partialTicks, int pass) {
    // net.minecraftforge.client.IRenderHandler renderer = this;
    // if (renderer != null) {
    // renderer.render(partialTicks, world, mc);
    // return;
    // }
    GlStateManager.disableTexture2D();
    Vec3 vec3 = world.getSkyColor(this.mc.getRenderViewEntity(), partialTicks);
    float f = (float) vec3.xCoord;
    float f1 = (float) vec3.yCoord;
    float f2 = (float) vec3.zCoord;

    if (pass != 2) {
      float f3 = (f * 30.0F + f1 * 59.0F + f2 * 11.0F) / 100.0F;
      float f4 = (f * 30.0F + f1 * 70.0F) / 100.0F;
      float f5 = (f * 30.0F + f2 * 70.0F) / 100.0F;
      f = f3;
      f1 = f4;
      f2 = f5;
    }

    GlStateManager.color(f, f1, f2);
    Tessellator tessellator = Tessellator.getInstance();
    WorldRenderer worldrenderer = tessellator.getWorldRenderer();
    GlStateManager.depthMask(false);
    GlStateManager.enableFog();
    GlStateManager.color(f, f1, f2);

    if (this.vboEnabled) {
      this.skyVBO.bindBuffer();
      GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
      GL11.glVertexPointer(3, GL11.GL_FLOAT, 12, 0L);
      this.skyVBO.drawArrays(7);
      this.skyVBO.unbindBuffer();
      GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
    } else {
      GlStateManager.callList(this.glSkyList);
    }

    GlStateManager.disableFog();
    GlStateManager.disableAlpha();
    GlStateManager.enableBlend();
    GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
    RenderHelper.disableStandardItemLighting();
    float[] afloat = world.provider.calcSunriseSunsetColors(world.getCelestialAngle(partialTicks), partialTicks);

    if (afloat != null) {
      GlStateManager.disableTexture2D();
      GlStateManager.shadeModel(7425);
      GlStateManager.pushMatrix();
      GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
      GlStateManager.rotate(MathHelper.sin(world.getCelestialAngleRadians(partialTicks)) < 0.0F ? 180.0F : 0.0F, 0.0F, 0.0F, 1.0F);
      GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
      float f6 = afloat[0];
      float f7 = afloat[1];
      float f8 = afloat[2];

      if (pass != 2) {
        float f9 = (f6 * 30.0F + f7 * 59.0F + f8 * 11.0F) / 100.0F;
        float f10 = (f6 * 30.0F + f7 * 70.0F) / 100.0F;
        float f11 = (f6 * 30.0F + f8 * 70.0F) / 100.0F;
        f6 = f9;
        f7 = f10;
        f8 = f11;
      }

      worldrenderer.begin(6, DefaultVertexFormats.POSITION_COLOR);
      worldrenderer.pos(0.0D, 100.0D, 0.0D).color(f6, f7, f8, afloat[3]).endVertex();
      int j = 16;

      for (int l = 0; l <= 16; ++l) {
        float f21 = (float) l * (float) Math.PI * 2.0F / 16.0F;
        float f12 = MathHelper.sin(f21);
        float f13 = MathHelper.cos(f21);
        worldrenderer.pos((double) (f12 * 120.0F), (double) (f13 * 120.0F), (double) (-f13 * 40.0F * afloat[3])).color(afloat[0], afloat[1], afloat[2], 0.0F).endVertex();
      }

      tessellator.draw();
      GlStateManager.popMatrix();
      GlStateManager.shadeModel(7424);
    }

    GlStateManager.enableTexture2D();
    GlStateManager.scale(2.0F, 2.0F, 2.0F);
    GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0);
    GlStateManager.pushMatrix();
    float f16 = 1.0F - world.getRainStrength(partialTicks);
    GlStateManager.color(1.0F, 1.0F, 1.0F, f16);
    GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
    GlStateManager.rotate(world.getCelestialAngle(partialTicks) * 360.0F, 1.0F, 0.0F, 0.0F);
    float f17 = 20.0F;
    this.renderEngine.bindTexture(locationNebulaPng);
    worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
    worldrenderer.pos((double) (-f17), -100.0D, (double) f17).tex(0.0D, 0.0D).endVertex();
    worldrenderer.pos((double) f17, -100.0D, (double) f17).tex(1.0D, 0.0D).endVertex();
    worldrenderer.pos((double) f17, -100.0D, (double) (-f17)).tex(1.0D, 1.0D).endVertex();
    worldrenderer.pos((double) (-f17), -100.0D, (double) (-f17)).tex(0.0D, 1.0D).endVertex();
    tessellator.draw();

    GlStateManager.scale(1.0F, 1.0F, 1.0F);
    GlStateManager.disableTexture2D();
    float f15 = world.getStarBrightness(partialTicks) * f16;

    if (f15 > 0.0F) {
      // GlStateManager.color(0F, 0.20392156F, 0.22745098F, 1.0F);
      GlStateManager.color(f15 - 0.4F, f15 - 0.4F, f15 - 0.4F, f15);

      if (this.vboEnabled) {
        // LogHelper.info("Rendering Stars #1");
        this.starVBO.bindBuffer();
        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glVertexPointer(3, GL11.GL_FLOAT, 12, 0L);
        this.starVBO.drawArrays(7);
        this.starVBO.unbindBuffer();
        GlStateManager.color(0F, 0.4549019607843137F, 0.4666666666666667F, 1.0F);
        // GlStateManager.color(f15 - 0.6F, f15 - 0.6F, f15 - 0.6F, f15);
        // LogHelper.info("Rendering Stars #2");
        this.star2VBO.bindBuffer();
        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glVertexPointer(3, GL11.GL_FLOAT, 12, 0L);
        this.star2VBO.drawArrays(7);
        this.star2VBO.unbindBuffer();
        GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
      } else {
        GlStateManager.callList(this.starGLCallList);
        GlStateManager.color(0F, 0.5F, 1.0F, 0.7F);
        GlStateManager.callList(this.starGLCallList2);
      }
    }

    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    GlStateManager.disableBlend();
    GlStateManager.enableAlpha();
    GlStateManager.enableFog();
    GlStateManager.popMatrix();
    GlStateManager.disableTexture2D();
    GlStateManager.color(0.0F, 0.0F, 0.0F);
    double d0 = this.mc.thePlayer.getPositionEyes(partialTicks).yCoord - world.getHorizon();

    if (d0 < 0.0D) {
      GlStateManager.pushMatrix();
      GlStateManager.translate(0.0F, 12.0F, 0.0F);

      if (this.vboEnabled) {
        this.sky2VBO.bindBuffer();
        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glVertexPointer(3, GL11.GL_FLOAT, 12, 0L);
        this.sky2VBO.drawArrays(7);
        this.sky2VBO.unbindBuffer();
        GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
      } else {
        GlStateManager.callList(this.glSkyList2);
      }

      GlStateManager.popMatrix();
      float f18 = 1.0F;
      float f19 = -((float) (d0 + 65.0D));
      float f20 = -1.0F;
      worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
      worldrenderer.pos(-1.0D, (double) f19, 1.0D).color(0, 0, 0, 255).endVertex();
      worldrenderer.pos(1.0D, (double) f19, 1.0D).color(0, 0, 0, 255).endVertex();
      worldrenderer.pos(1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
      worldrenderer.pos(-1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
      worldrenderer.pos(-1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
      worldrenderer.pos(1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
      worldrenderer.pos(1.0D, (double) f19, -1.0D).color(0, 0, 0, 255).endVertex();
      worldrenderer.pos(-1.0D, (double) f19, -1.0D).color(0, 0, 0, 255).endVertex();
      worldrenderer.pos(1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
      worldrenderer.pos(1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
      worldrenderer.pos(1.0D, (double) f19, 1.0D).color(0, 0, 0, 255).endVertex();
      worldrenderer.pos(1.0D, (double) f19, -1.0D).color(0, 0, 0, 255).endVertex();
      worldrenderer.pos(-1.0D, (double) f19, -1.0D).color(0, 0, 0, 255).endVertex();
      worldrenderer.pos(-1.0D, (double) f19, 1.0D).color(0, 0, 0, 255).endVertex();
      worldrenderer.pos(-1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
      worldrenderer.pos(-1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
      worldrenderer.pos(-1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
      worldrenderer.pos(-1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
      worldrenderer.pos(1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
      worldrenderer.pos(1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
      tessellator.draw();
    }

    if (world.provider.isSkyColored()) {
      GlStateManager.color(f * 0.2F + 0.04F, f1 * 0.2F + 0.04F, f2 * 0.6F + 0.1F);
    } else {
      GlStateManager.color(f, f1, f2);
    }

    GlStateManager.pushMatrix();
    GlStateManager.translate(0.0F, -((float) (d0 - 16.0D)), 0.0F);
    GlStateManager.callList(this.glSkyList2);
    GlStateManager.popMatrix();
    GlStateManager.enableTexture2D();
    GlStateManager.depthMask(true);
  }

  private void renderSky(WorldRenderer worldRendererIn, float p_174968_2_, boolean p_174968_3_) {
    int i = 64;
    int j = 6;
    worldRendererIn.begin(7, DefaultVertexFormats.POSITION);

    for (int k = -384; k <= 384; k += 64) {
      for (int l = -384; l <= 384; l += 64) {
        float f = (float) k;
        float f1 = (float) (k + 64);

        if (p_174968_3_) {
          f1 = (float) k;
          f = (float) (k + 64);
        }

        worldRendererIn.pos((double) f, (double) p_174968_2_, (double) l).endVertex();
        worldRendererIn.pos((double) f1, (double) p_174968_2_, (double) l).endVertex();
        worldRendererIn.pos((double) f1, (double) p_174968_2_, (double) (l + 64)).endVertex();
        worldRendererIn.pos((double) f, (double) p_174968_2_, (double) (l + 64)).endVertex();
      }
    }
  }

  private void generateSky2() {
    Tessellator tessellator = Tessellator.getInstance();
    WorldRenderer worldrenderer = tessellator.getWorldRenderer();

    if (this.sky2VBO != null) {
      this.sky2VBO.deleteGlBuffers();
    }

    if (this.glSkyList2 >= 0) {
      GLAllocation.deleteDisplayLists(this.glSkyList2);
      this.glSkyList2 = -1;
    }

    if (this.vboEnabled) {
      this.sky2VBO = new VertexBuffer(this.vertexBufferFormat);
      this.renderSky(worldrenderer, -16.0F, true);
      worldrenderer.finishDrawing();
      worldrenderer.reset();
      this.sky2VBO.bufferData(worldrenderer.getByteBuffer());
    } else {
      this.glSkyList2 = GLAllocation.generateDisplayLists(1);
      GL11.glNewList(this.glSkyList2, GL11.GL_COMPILE);
      this.renderSky(worldrenderer, -16.0F, true);
      tessellator.draw();
      GL11.glEndList();
    }
  }

  private void generateSky() {
    Tessellator tessellator = Tessellator.getInstance();
    WorldRenderer worldrenderer = tessellator.getWorldRenderer();

    if (this.skyVBO != null) {
      this.skyVBO.deleteGlBuffers();
    }

    if (this.glSkyList >= 0) {
      GLAllocation.deleteDisplayLists(this.glSkyList);
      this.glSkyList = -1;
    }

    if (this.vboEnabled) {
      this.skyVBO = new VertexBuffer(this.vertexBufferFormat);
      this.renderSky(worldrenderer, 16.0F, false);
      worldrenderer.finishDrawing();
      worldrenderer.reset();
      this.skyVBO.bufferData(worldrenderer.getByteBuffer());
    } else {
      this.glSkyList = GLAllocation.generateDisplayLists(1);
      GL11.glNewList(this.glSkyList, GL11.GL_COMPILE);
      this.renderSky(worldrenderer, 16.0F, false);
      tessellator.draw();
      GL11.glEndList();
    }
  }

  private void generateStars2() {
    Tessellator tessellator = Tessellator.getInstance();
    WorldRenderer worldrenderer = tessellator.getWorldRenderer();

    if (this.star2VBO != null) {
      this.star2VBO.deleteGlBuffers();
    }

    if (this.starGLCallList2 >= 0) {
      GLAllocation.deleteDisplayLists(this.starGLCallList2);
      this.starGLCallList2 = -1;
    }

    if (this.vboEnabled) {
      this.star2VBO = new VertexBuffer(this.vertexBufferFormat);
      this.renderStars(worldrenderer, 93175L, 500);
      worldrenderer.finishDrawing();
      worldrenderer.reset();
      this.star2VBO.bufferData(worldrenderer.getByteBuffer());
    } else {
      this.starGLCallList2 = GLAllocation.generateDisplayLists(1);
      GlStateManager.pushMatrix();
      GL11.glNewList(this.starGLCallList2, GL11.GL_COMPILE);
      this.renderStars(worldrenderer, 93175L, 500);
      tessellator.draw();
      GL11.glEndList();
      GlStateManager.popMatrix();
    }
  }

  private void generateStars() {
    Tessellator tessellator = Tessellator.getInstance();
    WorldRenderer worldrenderer = tessellator.getWorldRenderer();

    if (this.starVBO != null) {
      this.starVBO.deleteGlBuffers();
    }

    if (this.starGLCallList >= 0) {
      GLAllocation.deleteDisplayLists(this.starGLCallList);
      this.starGLCallList = -1;
    }

    if (this.vboEnabled) {
      this.starVBO = new VertexBuffer(this.vertexBufferFormat);
      this.renderStars(worldrenderer, 10842L, 1500);
      worldrenderer.finishDrawing();
      worldrenderer.reset();
      this.starVBO.bufferData(worldrenderer.getByteBuffer());
    } else {
      this.starGLCallList = GLAllocation.generateDisplayLists(1);
      GlStateManager.pushMatrix();
      GL11.glNewList(this.starGLCallList, GL11.GL_COMPILE);
      this.renderStars(worldrenderer, 10842L, 1500);
      tessellator.draw();
      GL11.glEndList();
      GlStateManager.popMatrix();
    }
  }

  private void renderStars(WorldRenderer worldRendererIn, long rand, int starCount) {
    Random random = new Random(rand);
    worldRendererIn.begin(7, DefaultVertexFormats.POSITION);

    for (int i = 0; i < starCount; ++i) {
      double d0 = (double) (random.nextFloat() * 2.0F - 1.0F);
      double d1 = (double) (random.nextFloat() * 2.0F - 1.0F);
      double d2 = (double) (random.nextFloat() * 2.0F - 1.0F);
      double d3 = (double) (0.15F + random.nextFloat() * 0.1F);
      double d4 = d0 * d0 + d1 * d1 + d2 * d2;

      if (d4 < 1.0D && d4 > 0.01D) {
        d4 = 1.0D / Math.sqrt(d4);
        d0 = d0 * d4;
        d1 = d1 * d4;
        d2 = d2 * d4;
        double d5 = d0 * 100.0D;
        double d6 = d1 * 100.0D;
        double d7 = d2 * 100.0D;
        double d8 = Math.atan2(d0, d2);
        double d9 = Math.sin(d8);
        double d10 = Math.cos(d8);
        double d11 = Math.atan2(Math.sqrt(d0 * d0 + d2 * d2), d1);
        double d12 = Math.sin(d11);
        double d13 = Math.cos(d11);
        double d14 = random.nextDouble() * Math.PI * 2.0D;
        double d15 = Math.sin(d14);
        double d16 = Math.cos(d14);

        for (int j = 0; j < 4; ++j) {
          double d17 = 0.0D;
          double d18 = (double) ((j & 2) - 1) * d3;
          double d19 = (double) ((j + 1 & 2) - 1) * d3;
          double d20 = 0.0D;
          double d21 = d18 * d16 - d19 * d15;
          double d22 = d19 * d16 + d18 * d15;
          double d23 = d21 * d12 + 0.0D * d13;
          double d24 = 0.0D * d12 - d21 * d13;
          double d25 = d24 * d9 - d22 * d10;
          double d26 = d22 * d9 + d24 * d10;
          worldRendererIn.pos(d5 + d25, d6 + d23, d7 + d26).endVertex();
        }
      }
    }
  }
}
