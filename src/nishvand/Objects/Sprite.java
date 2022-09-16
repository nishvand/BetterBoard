package nishvand.Objects;

import nishvand.Cache;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.ArrayList;
import java.util.Random;

public abstract class Sprite {
    protected int pointer, shadowPointer;
    protected Image getCachedImage(){ return (Image) Cache.get(pointer); } // получение изображения из кеша
    protected void setImage(Image image){ Cache.set(image, pointer); } // изменение изображения в кэше
    protected Image getCachedShadow(){ return (Image) Cache.get(shadowPointer); } // получение тени из кеша
    protected void setShadow(Image image){ Cache.set(image, shadowPointer); } // изменение тени в кэше

    protected ArrayList<Image> frames; // фреймы для анимированных текстур
    protected int counter = 0; // указатель на следующий кадр для анимации
    protected boolean animated = false; // анимированно?
    public boolean alpha = false;
    protected static Random rnd = new Random(); // рандом для случайного вращения
    public int offsetX = 0, offsetY = 0, shadowOffsetX = 0, shadowOffsetY = 0; // позиция в кадре
    public boolean collision = false; // имеет ли коллизию
    public String attributes = "";

    // конструкторы объекта
    public Sprite(Image image) {
        construct(image,0,0,false, false);
    }
    public Sprite(Image image, int offsetX, int offsetY, boolean collision) { construct(image, offsetX, offsetY, collision, false); }
    public Sprite(Image image, int offsetX, int offsetY, boolean collision, boolean animated) { construct(image, offsetX, offsetY, collision, animated); }
    public Sprite(Image image, int offsetX, int offsetY) { construct(image, offsetX, offsetY, false, false); }

    // простые методы по работе с графикой
    public int getWidth() { //получаем ширину картинки
        return getCachedImage().getWidth(null);
    }
    public int getHeight() { //получаем высоту картинки
        return getCachedImage().getHeight(null);
    }
    public Image getImage(){ return getCachedImage(); }
    public void upscale(int x){ setImage(cover((BufferedImage) getCachedImage(), x));}
    public static Image upscale(Image img, int x){ return cover((BufferedImage) img, x); }
    public void addAttribute(String attribute) { attributes += " " + attribute; }
    public boolean hasAttribute(String attribute) { return attributes.contains(attribute); }

    public void draw(Graphics g, int x, int y) { //рисуем картинку
        g.drawImage(getCachedImage(), x, y,null);
    }

    public void drawShadow(Graphics g, int x, int y){
        g.drawImage(getCachedShadow(), x + shadowOffsetX, y + shadowOffsetY,null);
    }

    public void createShadow(){
        int width = getWidth() + 64, height = getHeight() + 64;
        BufferedImage shadow = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        shadow.createGraphics();
        Graphics2D g = (Graphics2D) shadow.getGraphics();

        int AX = height - getWidth();
        int AY = height - getHeight();
        int AE = getWidth();

        int[] x = new int[]{0, 0, AX, width, AE};
        int[] y = new int[]{0, getHeight(), height, AY, 0};

        shadowOffsetX = -AX;
        shadowOffsetY = -AY;

        g.setColor(new Color(0,0,0, 96));
        g.fillPolygon(x, y, x.length);
        g.dispose();
        shadowPointer = Cache.put(shadow);
    }

    public int distance(int x, int y, int x1, int y1) {
        int AC = x1 - x;
        int BC = y1 - y;
        return (int) Math.sqrt(AC*AC + BC*BC);
    }

    public abstract void onTouch();
    public abstract void onHit();
    public abstract void onTick();

    private void construct(Image image, int offsetX, int offsetY, boolean collision, boolean animated) { // конструктор
        pointer = Cache.put(image);
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.collision = collision;
        this.animated = animated;
        this.frames = new ArrayList<>();

        if (animated) { // если анимированно, то
            slice(); // разрезаем текстуру на фреймы
            setImage(frames.get(0)); // и устанавливаем первый фрейм
        }
    }

    public void updateTexture() { // обновление текстуры для анимированных объектов
        if(!animated) return; // если не спрайт не анимирован то завершаем функцию
        if(counter < frames.size()) {
            setImage(frames.get(counter)); // ставим изображение
        } else { counter = -1; } // если counter уходит за границы массива делаем его = -1

        counter++; // и прибавляем на 1
    }

    protected void slice() { // разрез текстуры для анимированных объектов
        int verticalTimes = getHeight() / getWidth(); // количество кадров (предполагается что каждый кадр имеет соотношение сторон 1:1)
        for (int i = 0; i < verticalTimes; i++) {
            BufferedImage temp = new BufferedImage(getWidth(), getWidth(), BufferedImage.TYPE_INT_ARGB); // делаем кадр
            temp.createGraphics(); // создаем для него графику
            Graphics g = temp.getGraphics();
            g.drawImage(getCachedImage(), 0, -(i * getWidth()), null); // отрисовываем часть оригинала
            g.dispose(); // рендер
            frames.add(temp); // добавляем фрейм
        }
    }

    public void center() { // центрация изображения около точки 0 0
        offsetX = -(getHeight() / 2);
        offsetY = -(getWidth() / 2);
    }

    // создание матрицы картинок
    public void createMatrix(int matrixSize) { createMatrix(matrixSize, false); }
    public void createMatrix(int matrixSize, boolean randomRotate){
        if(animated) { // удостоверяемся что этот спрайт не анимирован
            System.out.println("use createAnimatedMatrix() instead of createMatrix() on animated sprites");
            return;
        }

        final int width = getWidth();
        final int height = getHeight();

        // создаем большое изображение
        BufferedImage img = new BufferedImage(getWidth()*matrixSize, getHeight()*matrixSize, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();

        for (int i = 0; i < matrixSize * height; i += height) {
            for (int j = 0; j < matrixSize * width; j += width) {
                if(randomRotate) // вращаем текстурку если надо и отрисовываем
                    g.drawImage(rotate((BufferedImage) getCachedImage(), (double)rnd.nextInt(4) * 90), j , i , null);
                else // или просто отрисовываем
                    g.drawImage(getCachedImage(), j , i , null);
            }
        }

        setImage(img); // заменяем текущее изображение матрицой
    }

    // тоже самое но для анимированных объектов
    // вообще не уверен что работает
    public void createAnimatedMatrix(int matrixSize) { createAnimatedMatrix(matrixSize, false); }
    public void createAnimatedMatrix(int matrixSize, boolean randomRotate){
        final int width = getWidth();
        final int height = getHeight();

        for (int f = 0; f < frames.size(); f++){
            BufferedImage img = new BufferedImage(getWidth() * matrixSize, getWidth()*matrixSize, BufferedImage.TYPE_INT_ARGB);
            Graphics g = img.getGraphics();

            for (int i = 0; i < matrixSize * height; i += height) {
                for (int j = 0; j < matrixSize * width; j += width) {
                    if(randomRotate)
                        g.drawImage(rotate((BufferedImage) frames.get(f), (double)rnd.nextInt(4) * 90), j , i , null);
                    else
                        g.drawImage(frames.get(f), j , i , null);
                }
            }
            // кто то это вообще читать будет?
            frames.set(f, img);
        }
    }

    // вращение изображения ( код из интернета )
    public static BufferedImage rotate(BufferedImage bimg, Double angle) {
        double sin = Math.abs(Math.sin(Math.toRadians(angle))),
                cos = Math.abs(Math.cos(Math.toRadians(angle)));
        int w = bimg.getWidth();
        int h = bimg.getHeight();
        int neww = (int) Math.floor(w*cos + h*sin),
                newh = (int) Math.floor(h*cos + w*sin);
        BufferedImage rotated = new BufferedImage(neww, newh, bimg.getType());
        Graphics2D graphic = rotated.createGraphics();
        graphic.translate((neww-w)/2, (newh-h)/2);
        graphic.rotate(Math.toRadians(angle), w / 2.0, h / 2.0);
        graphic.drawRenderedImage(bimg, null);
        graphic.dispose();
        return rotated;
    }

    // конвертация изображения в массив ( тоже код из интернета )
    protected static int[][] convertToPixels(BufferedImage image) {
        final byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        final int width = image.getWidth();
        final int height = image.getHeight();
        final boolean hasAlphaChannel = image.getAlphaRaster() != null;

        int[][] result = new int[height][width];
        if (hasAlphaChannel) {
            final int pixelLength = 4;
            for (int pixel = 0, row = 0, col = 0; pixel + 3 < pixels.length; pixel += pixelLength) {
                int argb = 0;
                argb += (((int) pixels[pixel] & 0xff) << 24); // alpha
                argb += ((int) pixels[pixel + 1] & 0xff); // blue
                argb += (((int) pixels[pixel + 2] & 0xff) << 8); // green
                argb += (((int) pixels[pixel + 3] & 0xff) << 16); // red
                result[row][col] = argb;
                col++;
                if (col == width) {
                    col = 0;
                    row++;
                }
            }
        } else {
            final int pixelLength = 3;
            for (int pixel = 0, row = 0, col = 0; pixel + 2 < pixels.length; pixel += pixelLength) {
                int argb = 0;
                argb -= 16777216; // 255 alpha
                argb += ((int) pixels[pixel] & 0xff); // blue
                argb += (((int) pixels[pixel + 1] & 0xff) << 8); // green
                argb += (((int) pixels[pixel + 2] & 0xff) << 16); // red
                result[row][col] = argb;
                col++;
                if (col == width) {
                    col = 0;
                    row++;
                }
            }
        }
        return result;
    }

    // увеличение текстуры в range раз
    public static BufferedImage cover(BufferedImage image, double range) {
        int[][] pixels = convertToPixels(image); // получаем массив
        int width = image.getWidth();
        int height = image.getHeight();

        // создаем увеличенное изображение
        BufferedImage imageResult = new BufferedImage((int)(width * range), (int)(height * range), BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < width * range; x ++){ // пройдемся по каждому пикселю большей картинки
            for (int y = 0; y < height * range; y++) {
                imageResult.setRGB(x, y, pixels[y / (int) range][x / (int) range]); // и установим пиксель из массива
            }
        }

        return imageResult;
    }
}