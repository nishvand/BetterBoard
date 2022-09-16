package nishvand.Objects;

import java.awt.*;

public abstract class Button extends Sprite{
    public Button(Image image) { super(image); }
    public Button(Image image, int offsetX, int offsetY, boolean collision) { super(image, offsetX, offsetY, collision); }
    public Button(Image image, int offsetX, int offsetY, boolean collision, boolean animated) { super(image, offsetX, offsetY, collision, animated); }
    public Button(Image image, int offsetX, int offsetY) { super(image, offsetX, offsetY); }
    @Override public void onTouch() {}
    @Override public void onTick() {}
    @Override public void onHit() { onClick(); }
    public abstract void onClick();
}
