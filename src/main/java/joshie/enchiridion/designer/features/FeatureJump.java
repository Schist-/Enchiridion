package joshie.enchiridion.designer.features;

import static joshie.enchiridion.designer.DesignerHelper.drawRect;
import static joshie.enchiridion.designer.DesignerHelper.getGui;
import joshie.enchiridion.designer.DesignerHelper;
import joshie.enchiridion.helpers.ClientHelper;

import com.google.gson.annotations.Expose;

public class FeatureJump extends FeatureWithText {
    @Expose
    public String jumpTo = "2";
    @Expose
    public String texture = "";

    private FeatureImage image = null;

    @Override
    public String getTextField() {
        return jumpTo;
    }

    @Override
    public void setTextField(String str) {
        this.jumpTo = str;
    }

    @Override
    public void drawFeature() {
        if (getGui().canEdit) {
            drawRect(left - 4, top - 4, right, top, 0xFF000000);
            drawRect(right, top - 4, right + 4, bottom, 0xFF000000);
            drawRect(left - 4, top, left, bottom + 4, 0xFF000000);
            drawRect(left, bottom, right + 4, bottom + 4, 0xFF000000);
        }

        if (!texture.equals("")) {
            if (isOverFeature(DesignerHelper.getGui().mouseX, DesignerHelper.getGui().mouseY)) {
                if (image == null) image = new FeatureImage(this).setPath(texture);
                image.drawFeature();
            }
        }

        //Draw The Search stuff
        if (isSelected) {
            DesignerHelper.drawRect(-102, -55, -100, -37, 0xFFFFFFFF);
            DesignerHelper.drawRect(0, -55, 2, -37, 0xFFFFFFFF);
            DesignerHelper.drawRect(-102, -57, 2, -55, 0xFFFFFFFF);
            DesignerHelper.drawRect(-100, -55, 0, -37, 0xFF000000);
            DesignerHelper.drawRect(-100, -37, 0, -39, 0xFFFFFFFF);
            DesignerHelper.drawSplitString(getText(), -95, -50, 250, 0xFFFFFFFF);
        }
    }

    @Override
    public void click(int x, int y) {
        if ((DesignerHelper.getGui().canEdit && ClientHelper.isShiftPressed()) || !DesignerHelper.getGui().canEdit) {
            if (isOverFeature(x, y)) {
                isSelected = false;
                clearSelected();

                if (jumpTo != null) {
                    try {
                        int jump = Integer.parseInt(jumpTo) - 1;
                        DesignerHelper.getGui().setPage(jump);
                    } catch (Exception e) {
                        DesignerHelper.getGui().setPage(jumpTo);
                        clearSelected();
                    }
                }
            }

            return;
        }

        super.click(x, y);
    }
}
