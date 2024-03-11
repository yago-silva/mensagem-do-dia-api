package com.mensagemdodia.domain;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import org.apache.commons.compress.utils.IOUtils;

public class ImageMediaEditor {

    public static byte[] addPhraseToImage(BufferedImage image, String mainText, String secondaryText) throws IOException {
        image = addBlur(image);
        addOpaqueBackgrounds(image);
        addPhraseContent(image, mainText, secondaryText);
        addSiteRef(image);

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", outStream);
        InputStream is = new ByteArrayInputStream(outStream.toByteArray());

        return IOUtils.toByteArray(is);
    }

    private static BufferedImage addBlur(BufferedImage image) {
        int radius = 5;
        int size = radius * 2 + 1;
        float weight = 1.0f / (size * size);
        float[] data = new float[size * size];

        for (int i = 0; i < data.length; i++) {
            data[i] = weight;
        }

        Kernel kernel = new Kernel(size, size, data);
        ConvolveOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
        return op.filter(image, null);
    }

    private static void addOpaqueBackgrounds(BufferedImage image) {
        Graphics graphics = image.getGraphics();
        graphics.setColor(new Color(233, 30, 90, 127));
        graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
        graphics.create();

        Graphics graphics2 = image.getGraphics();
        graphics2.setColor(new Color(0, 0, 0, 63));
        graphics2.fillRect(0, 0, image.getWidth(), image.getHeight());
        graphics2.create();
    }

    private static void addPhraseContent(BufferedImage image, String mainText, String secondaryText) {
        List<String> textLines = new ArrayList<>();

        String nextLine = "";

        String[] split = mainText.split(" ");
        for (var index = 0; index < split.length; index++) {
            nextLine += " " + split[index];

            if (nextLine.length() >= 15) {
                textLines.add(nextLine.trim());
                nextLine = "";
            }
        }

        textLines.add(nextLine.trim());

        Graphics textGraphics = image.getGraphics();

        Font font = new Font(Font.SANS_SERIF, Font.BOLD, 42);

        textGraphics.setFont(font);
        textGraphics.setColor(Color.decode("#FFFFFF"));
        FontMetrics metrics = textGraphics.getFontMetrics(font);
        var lineSpacing = metrics.getHeight() + (metrics.getHeight() / 2);

        var totalLinesOfText = secondaryText == null ? textLines.size() : textLines.size() + 1;

        int positionY = (image.getHeight() - (lineSpacing * totalLinesOfText)) / 2 + metrics.getAscent();

        for (int index = 0; index < textLines.size(); index++) {
            int positionX = (image.getWidth() - metrics.stringWidth(textLines.get(index).toUpperCase())) / 2;
            textGraphics.drawString(textLines.get(index).toUpperCase(), positionX, positionY);
            positionY += lineSpacing;
        }
        textGraphics.create();

        if (secondaryText != null) {
            Graphics authorTextGraphics = image.getGraphics();
            Font authorTextFont = new Font(Font.SANS_SERIF, Font.PLAIN, 20);
            authorTextGraphics.setFont(authorTextFont);
            authorTextGraphics.setColor(Color.decode("#FFFFFF"));

            String authorName = "(" + secondaryText.toUpperCase() + ")";
            FontMetrics authorFontMetrics = authorTextGraphics.getFontMetrics(authorTextFont);
            int positionX = (image.getWidth() - authorFontMetrics.stringWidth(authorName)) / 2;

            authorTextGraphics.drawString(authorName, positionX, positionY);
            authorTextGraphics.create();
        }
    }

    private static void addSiteRef(BufferedImage image) {
        Graphics textGraphics = image.getGraphics();
        Font textFont = new Font(Font.SANS_SERIF, Font.PLAIN, 32);
        textGraphics.setFont(textFont);
        textGraphics.setColor(Color.decode("#FFFFFF"));

        String siteDomain = "www.mensagemdodia.com";

        FontMetrics metrics = textGraphics.getFontMetrics(textFont);
        var lineSpacing = metrics.getHeight() + (metrics.getHeight() / 2);
        int positionX = (image.getWidth() - metrics.stringWidth(siteDomain)) / 2;
        int positionY = (image.getHeight() - lineSpacing);
        textGraphics.drawString(siteDomain, positionX, positionY);
        textGraphics.create();
    }
}
