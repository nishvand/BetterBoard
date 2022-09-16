package nishvand.Objects;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Light extends Sprite {
    @Override public void onTouch() {}
    @Override public void onHit() {}
    @Override public void onTick() {}

    // типичные конструкторы
    public Light(Image image) { super(image); }
    public Light(Image image, int offsetX, int offsetY) { super(image, offsetX, offsetY); }
    public Light(int size) { super(null); construct(size,0,0, 0, null); }
    public Light(int size, int offsetX, int offsetY) { super(null); construct(size, offsetX, offsetY, 0, null); }
    public Light(int size, int offsetX, int offsetY, int brightness) { super(null); construct(size, offsetX, offsetY, brightness, null); }
    public Light(int size, int offsetX, int offsetY, int brightness, Color c) { super(null); construct(size, offsetX, offsetY, brightness, c); }

    // создание света
    private void construct(int size, int offsetX, int offsetY, int brightness, Color c) {
        alpha = true;
        super.setImage(new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB)); // создаем изображение
        super.offsetX = offsetX;
        super.offsetY = offsetY;
        Graphics g = super.getCachedImage().getGraphics(); // получаем графику изображения
        if(c == null) c = new Color(255, 255, 255);

        for(int i = 0; i < size; i++){
            for (int j = 0; j < size; j++){
                float x = (float)i / size * 2.0f - 1.0f; // переводим координаты в float от -1 до 1
                float y = (float)j / size * 2.0f - 1.0f; // то есть центром будет являться 0 0

                // вычисляем альфа канал по дальности от центра
                int alpha = Math.min(Math.min(Math.max((int)((x*x + y*y) * 255), 1), 255) + brightness, 255);

                g.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 255 - alpha)); // выбираем цвет с инвертированным alpha
                if(alpha != 0) { // если alpha не равен нулю то ставим точку
                    g.drawLine(i, j, i, j);
                }
            }
        }

        g.dispose();
    }
}