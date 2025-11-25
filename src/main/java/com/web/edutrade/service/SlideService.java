package com.web.edutrade.service;

import com.web.edutrade.model.Slide;
import com.web.edutrade.model.StockData;
import com.web.edutrade.repo.SlideRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class SlideService {

    @Autowired
    private SlideRepo slideRepo;

    @Autowired
    private StockDataService stockDataService;

    /**
     * Tạo hình ảnh với dữ liệu chứng khoán
     */
    public String createStockImage(Slide slide, StockData stockData) {
        try {
            int width = 1920;
            int height = 400;

            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = image.createGraphics();

            // Enable anti-aliasing
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

            // Load ảnh nền nếu có
            boolean hasBackground = false;
            if (slide.getPath() != null && !slide.getPath().isEmpty()) {
                try {
                    String[] possiblePaths = {
                            "src/main/resources/static" + slide.getPath(),
                            "static" + slide.getPath(),
                            slide.getPath()
                    };

                    for (String path : possiblePaths) {
                        File bgFile = new File(path);
                        if (bgFile.exists()) {
                            BufferedImage bgImage = ImageIO.read(bgFile);
                            g2d.drawImage(bgImage, 0, 0, width, height, null);
                            hasBackground = true;
                            break;
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Cannot load background image: " + e.getMessage());
                }
            }

            if (!hasBackground) {
                drawGradientBackground(g2d, width, height);
            }

            // Vẽ overlay tối
            g2d.setColor(new Color(0, 0, 0, 160));
            g2d.fillRect(0, 0, width, height);

            // Vẽ dữ liệu chứng khoán
            drawStockData(g2d, stockData, width, height);

            g2d.dispose();

            // Lưu file
            String filename = "stock_" + stockData.getSymbol() + "_" + System.currentTimeMillis() + ".png";
            String outputPath = "src/main/resources/static/img/slides/" + filename;
            File outputFile = new File(outputPath);
            outputFile.getParentFile().mkdirs();

            ImageIO.write(image, "PNG", outputFile);

            return "/img/slides/" + filename;

        } catch (Exception e) {
            System.err.println("Error creating stock image: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error creating stock image", e);
        }
    }

    private void drawGradientBackground(Graphics2D g2d, int width, int height) {
        GradientPaint gradient = new GradientPaint(
                0, 0, new Color(30, 60, 114),
                0, height, new Color(42, 82, 152)
        );
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, width, height);
    }

    private void drawStockData(Graphics2D g2d, StockData stockData, int width, int height) {
        boolean isPositive = stockData.getChange() >= 0;
        Color changeColor = isPositive ? new Color(0, 200, 100) : new Color(220, 53, 69);

        int centerX = width / 2;
        int centerY = height / 2;

        // Vẽ mã chứng khoán và giá
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 80));
        String symbolText = stockData.getSymbol() + " - " + String.format("%.2f", stockData.getPrice());
        FontMetrics fm1 = g2d.getFontMetrics();
        int symbolWidth = fm1.stringWidth(symbolText);
        g2d.drawString(symbolText, centerX - symbolWidth / 2, centerY - 30);

        // Vẽ thay đổi
        g2d.setFont(new Font("Arial", Font.BOLD, 40));
        g2d.setColor(changeColor);
        String changeText = String.format("%s%.2f (%s%.2f%%)",
                isPositive ? "+" : "", stockData.getChange(),
                isPositive ? "+" : "", stockData.getChangePercent());
        FontMetrics fm2 = g2d.getFontMetrics();
        int changeWidth = fm2.stringWidth(changeText);
        g2d.drawString(changeText, centerX - changeWidth / 2, centerY + 35);

        drawInfoBox(g2d, stockData, 50, 50, isPositive);
        drawTimestamp(g2d, stockData.getTimestamp(), width, height);
    }

    private void drawInfoBox(Graphics2D g2d, StockData stockData, int x, int y, boolean isPositive) {
        g2d.setColor(new Color(0, 0, 0, 200));
        g2d.fillRoundRect(x, y, 350, 180, 15, 15);

        Color borderColor = isPositive ? new Color(0, 200, 100) : new Color(220, 53, 69);
        g2d.setColor(borderColor);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRoundRect(x, y, 350, 180, 15, 15);

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.PLAIN, 20));

        int textX = x + 20;
        int textY = y + 35;
        int lineHeight = 35;

        g2d.drawString("Mã CK: " + stockData.getSymbol(), textX, textY);
        g2d.drawString("Giá: " + String.format("%.2f", stockData.getPrice()), textX, textY + lineHeight);
        g2d.drawString("Thay đổi: " + String.format("%+.2f", stockData.getChange()), textX, textY + lineHeight * 2);
        g2d.drawString("KL: " + formatVolume(stockData.getVolume()), textX, textY + lineHeight * 3);
    }

    private void drawTimestamp(Graphics2D g2d, Date timestamp, int width, int height) {
        g2d.setColor(new Color(200, 200, 200));
        g2d.setFont(new Font("Arial", Font.PLAIN, 18));
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String timeStr = sdf.format(timestamp);
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(timeStr);
        g2d.drawString(timeStr, width - textWidth - 30, height - 20);
    }

    private String formatVolume(long volume) {
        if (volume >= 1000000) {
            return String.format("%.1fM", volume / 1000000.0);
        } else if (volume >= 1000) {
            return String.format("%.1fK", volume / 1000.0);
        }
        return String.valueOf(volume);
    }

    @Transactional
    public void updateSlideWithStock(Long slideId, String stockSymbol) {
        Slide slide = slideRepo.findById(slideId)
                .orElseThrow(() -> new RuntimeException("Slide not found with id: " + slideId));

        StockData stockData = stockDataService.getStockData(stockSymbol);
        String newImagePath = createStockImage(slide, stockData);

        slide.setPath(newImagePath);
        slide.setCount(stockSymbol + " - " + String.format("%.2f", stockData.getPrice()));
        slide.setDes(String.format("%+.2f (%+.2f%%)",
                stockData.getChange(),
                stockData.getChangePercent()));
        slide.setStockSymbol(stockSymbol);
        slide.setLastUpdated(new Date());

        slideRepo.save(slide);

        System.out.println("✅ Updated slide #" + slideId + " with " + stockSymbol);
    }

    @Transactional
    public int autoUpdateAllSlides() {
        List<Slide> slides = slideRepo.findByAutoUpdateTrue();
        int updated = 0;

        System.out.println("🔄 Starting auto-update for " + slides.size() + " slides...");

        for (Slide slide : slides) {
            try {
                if (slide.getStockSymbol() != null && !slide.getStockSymbol().isEmpty()) {
                    updateSlideWithStock(slide.getId(), slide.getStockSymbol());
                    updated++;
                }
            } catch (Exception e) {
                System.err.println("❌ Failed to update slide #" + slide.getId() + ": " + e.getMessage());
            }
        }

        System.out.println("✅ Auto-update completed: " + updated + " slides updated");
        return updated;
    }

    public List<Slide> findAll() {
        return slideRepo.findAll();
    }

    public Slide findById(Long id) {
        return slideRepo.findById(id).orElse(null);
    }

    public Slide save(Slide slide) {
        return slideRepo.save(slide);
    }

    public void delete(Long id) {
        slideRepo.deleteById(id);
    }



}
