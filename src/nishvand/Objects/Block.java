package nishvand.Objects;

import nishvand.Engine;
import nishvand.Main;

import java.awt.*;

public class Block extends Sprite{
    public Block (Image image) {
        super(image,0,0,false, false);
    }
    public Block (Image image, int offsetX, int offsetY, boolean collision) { super(image, offsetX, offsetY, collision, false); }
    public Block (Image image, int offsetX, int offsetY, boolean collision, boolean animated) { super(image, offsetX, offsetY, collision, animated); }
    public Block (Image image, int offsetX, int offsetY) { super(image, offsetX, offsetY, false, false); }

    @Override public void onTouch() {}
    @Override public void onTick() {}
    @Override public void onHit() {}
}