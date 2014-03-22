package enchiridion.api.pages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.oredict.OreDictionary;

import org.lwjgl.opengl.GL11;
import org.w3c.dom.Element;

import enchiridion.BookLogHandler;
import enchiridion.api.GuiGuide;
import enchiridion.api.GuideHandler;
import enchiridion.api.XMLHelper;

public abstract class PageParser {
	public static RenderBlocks renderer;
	protected static RenderItem itemRenderer = (RenderItem) RenderManager.instance.entityRenderMap.get(EntityItem.class);
	protected static final ResourceLocation elements = new ResourceLocation("books", "textures/gui/guide_elements.png");
	public static HashMap<String, PageParser> parsers = new HashMap();
	
	public String node;
	protected String bookID;
	protected FontRenderer font;
	protected GuiGuide gui;
	protected boolean left;
	protected int x, y;
	protected float size;
	
	public void init(GuiGuide gui, int x, int y, boolean left) {
		this.bookID = gui.xml;
		this.gui = gui;
		this.left = left;
		this.x = x;
		this.y = y;
		this.font = gui.getFont();
	}
	
	public void resize(Element xml) {
		x += XMLHelper.getAttribAsInteger(xml, "x", 0);
		y += XMLHelper.getAttribAsInteger(xml, "y", 0);
		size = XMLHelper.getAttribAsFloat(xml, "size", 1F);
		x = (int) ((x / size) * 1F);
		GL11.glScalef(size, size, size);
	}
	
	public abstract void read(Element xml);
	public abstract void parse();
	
	public void drawFluidStack(int x, int y, Icon icon, int width, int height) {
		if (icon == null) {
			return;
		}

		double minU = icon.getMinU();
		double maxU = icon.getMaxU();
		double minV = icon.getMinV();
		double maxV = icon.getMaxV();

		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(x + 0, y + height, gui.getZLevel(), minU, minV + (maxV - minV) * height / 16.0D);
		tessellator.addVertexWithUV(x + width, y + height, gui.getZLevel(), minU + (maxU - minU) * width / 16.0D, minV + (maxV - minV) * height / 16.0D);
		tessellator.addVertexWithUV(x + width, y + 0, gui.getZLevel(), minU + (maxU - minU) * width / 16.0D, minV);
		tessellator.addVertexWithUV(x + 0, y + 0, gui.getZLevel(), minU, minV);
		tessellator.draw();
	}
	
	protected void drawItemStack(ItemStack stack, int x, int y) {
		if(stack == null || stack.getItem() == null) return;
		
		try {
            if(renderer == null) {
                renderer = Minecraft.getMinecraft().renderGlobal.globalRenderBlocks;
            }
		} catch(Exception e) {
			e.printStackTrace();
		}	
		
		try {
	        GL11.glTranslatef(0.0F, 0.0F, 32.0F);
	        FontRenderer font = null;
	        if (stack != null) font = stack.getItem().getFontRenderer(stack);
	        if (font == null) font = gui.getFont();
	        Minecraft mc = Minecraft.getMinecraft();
	        if (!ForgeHooksClient.renderInventoryItem(renderer, mc.getTextureManager(), stack, itemRenderer.renderWithColor, itemRenderer.zLevel, (float)x, (float)y)) {
	        	itemRenderer.renderItemIntoGUI(font, gui.getMC().getTextureManager(), stack, x, y, false);
	        }
	        
	        GL11.glDisable(GL11.GL_LIGHTING);
		} catch (Exception e) {
			//e.printStackTrace();
			try {				
				ArrayList<ItemStack> ores = OreDictionary.getOres(OreDictionary.getOreID(stack));
				ItemStack stack2 = ores.get(GuideHandler.rand.nextInt(ores.size()));
				GL11.glTranslatef(0.0F, 0.0F, 32.0F);
		        FontRenderer font = null;
		        if (stack2 != null) font = stack2.getItem().getFontRenderer(stack2);
		        if (font == null) font = gui.getFont();
		        Minecraft mc = Minecraft.getMinecraft();
		        if (!ForgeHooksClient.renderInventoryItem(renderer, mc.getTextureManager(), stack2, itemRenderer.renderWithColor, itemRenderer.zLevel, (float)x, (float)y)) {
		        	itemRenderer.renderItemIntoGUI(font, gui.getMC().getTextureManager(), stack2, x, y, false);
		        }
			} catch (Exception e2) {
				BookLogHandler.log(Level.WARNING, "Rendering failed when trying to render an item!" + stack);
				//e2.printStackTrace();
			}
		} 
    }

	public static void registerHandler(String xml, PageParser handler) {
		PageParser.parsers.put(xml, handler);
	}
}