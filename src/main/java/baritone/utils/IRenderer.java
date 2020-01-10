/*
 * This file is part of Baritone.
 *
 * Baritone is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Baritone is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Baritone.  If not, see <https://www.gnu.org/licenses/>.
 */

package baritone.utils;

import baritone.api.BaritoneAPI;
import baritone.api.Settings;
import baritone.api.utils.Helper;
import baritone.utils.accessor.IEntityRenderManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.AxisAlignedBB;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

public interface IRenderer {

    Tessellator tessellator = Tessellator.getInstance();
    BufferBuilder buffer = tessellator.getBuffer();
    IEntityRenderManager renderManager = (IEntityRenderManager) Helper.mc.getRenderManager();
    Settings settings = BaritoneAPI.getSettings();

    static void glColor(Color color, float alpha) {
        float[] colorComponents = color.getColorComponents(null);
        RenderSystem.color4f(colorComponents[0], colorComponents[1], colorComponents[2], alpha);
    }

    static void startLines(Color color, float alpha, float lineWidth, boolean ignoreDepth) {
        RenderSystem.enableBlend();
        RenderSystem.disableLighting();
        RenderSystem.blendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ZERO);
        glColor(color, alpha);
        RenderSystem.lineWidth(lineWidth);
        RenderSystem.disableTexture();
        RenderSystem.depthMask(false);

        if (ignoreDepth) {
            RenderSystem.disableDepthTest();
        }
    }

    static void startLines(Color color, float lineWidth, boolean ignoreDepth) {
        startLines(color, .4f, lineWidth, ignoreDepth);
    }

    static void endLines(boolean ignoredDepth) {
        if (ignoredDepth) {
            RenderSystem.enableDepthTest();
        }

        RenderSystem.depthMask(true);
        RenderSystem.enableTexture();
        RenderSystem.enableLighting();
        RenderSystem.disableBlend();
    }

    static void drawAABB(AxisAlignedBB aabb) {
        AxisAlignedBB toDraw = aabb.offset(-renderManager.renderPosX(), -renderManager.renderPosY(), -renderManager.renderPosZ());

        buffer.begin(GL_LINES, DefaultVertexFormats.POSITION);
        // bottom
        buffer.func_225582_a_(toDraw.minX, toDraw.minY, toDraw.minZ).endVertex();
        buffer.func_225582_a_(toDraw.maxX, toDraw.minY, toDraw.minZ).endVertex();
        buffer.func_225582_a_(toDraw.maxX, toDraw.minY, toDraw.minZ).endVertex();
        buffer.func_225582_a_(toDraw.maxX, toDraw.minY, toDraw.maxZ).endVertex();
        buffer.func_225582_a_(toDraw.maxX, toDraw.minY, toDraw.maxZ).endVertex();
        buffer.func_225582_a_(toDraw.minX, toDraw.minY, toDraw.maxZ).endVertex();
        buffer.func_225582_a_(toDraw.minX, toDraw.minY, toDraw.maxZ).endVertex();
        buffer.func_225582_a_(toDraw.minX, toDraw.minY, toDraw.minZ).endVertex();
        // top
        buffer.func_225582_a_(toDraw.minX, toDraw.maxY, toDraw.minZ).endVertex();
        buffer.func_225582_a_(toDraw.maxX, toDraw.maxY, toDraw.minZ).endVertex();
        buffer.func_225582_a_(toDraw.maxX, toDraw.maxY, toDraw.minZ).endVertex();
        buffer.func_225582_a_(toDraw.maxX, toDraw.maxY, toDraw.maxZ).endVertex();
        buffer.func_225582_a_(toDraw.maxX, toDraw.maxY, toDraw.maxZ).endVertex();
        buffer.func_225582_a_(toDraw.minX, toDraw.maxY, toDraw.maxZ).endVertex();
        buffer.func_225582_a_(toDraw.minX, toDraw.maxY, toDraw.maxZ).endVertex();
        buffer.func_225582_a_(toDraw.minX, toDraw.maxY, toDraw.minZ).endVertex();
        // corners
        buffer.func_225582_a_(toDraw.minX, toDraw.minY, toDraw.minZ).endVertex();
        buffer.func_225582_a_(toDraw.minX, toDraw.maxY, toDraw.minZ).endVertex();
        buffer.func_225582_a_(toDraw.maxX, toDraw.minY, toDraw.minZ).endVertex();
        buffer.func_225582_a_(toDraw.maxX, toDraw.maxY, toDraw.minZ).endVertex();
        buffer.func_225582_a_(toDraw.maxX, toDraw.minY, toDraw.maxZ).endVertex();
        buffer.func_225582_a_(toDraw.maxX, toDraw.maxY, toDraw.maxZ).endVertex();
        buffer.func_225582_a_(toDraw.minX, toDraw.minY, toDraw.maxZ).endVertex();
        buffer.func_225582_a_(toDraw.minX, toDraw.maxY, toDraw.maxZ).endVertex();
        tessellator.draw();
    }

    static void drawAABB(AxisAlignedBB aabb, double expand) {
        drawAABB(aabb.grow(expand, expand, expand));
    }
}
