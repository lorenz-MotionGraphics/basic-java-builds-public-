import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.zip.InflaterInputStream;

public class main extends JFrame {
    private BufferedImage backgroundImage;
    private Timer animationTimer;
    private int animationOffset = 0;
    private final Color primaryColor = new Color(25, 118, 210);
    private final Color secondaryColor = new Color(156, 39, 176);
    private final Color accentColor = new Color(255, 87, 34);
    private final Color darkBg = new Color(33, 33, 33);
    private final Color cardBg = new Color(66, 66, 66);
    
    public main() {
        setTitle("Furmart Petshop (Desktop)");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Load background image with error handling
        loadBackgroundImage();
        
        // Set up the main content
        setupContent();
    }
    
    private void loadBackgroundImage() {
        try {
            URL imageUrl = new URL("https://i.ibb.co/pB24PWQ5/enhanced-latest-bg.png");
            backgroundImage = ImageIO.read(imageUrl);
        } catch (IOException e) {
            System.err.println("Could not load background image: " + e.getMessage());
            // Create a gradient background as fallback
            backgroundImage = createGradientBackground();
        }
    }
    
    private BufferedImage createGradientBackground() {
        BufferedImage img = new BufferedImage(1000, 600, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = img.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
        GradientPaint gradient = new GradientPaint(0, 0, primaryColor, 1000, 600, secondaryColor);
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, 1000, 600);
        g2d.dispose();
        return img;
    }
    
    private void setupContent() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(darkBg);
        
        // Hero section with background image
        JPanel heroPanel = createHeroSection();
        
        // Feature sections
        JPanel featuresPanel = createFeaturesSection();
        
        // About section
        JPanel aboutPanel = createAboutSection();
        
        // Contact section
        JPanel contactPanel = createContactSection();
        
        // Add all sections
        mainPanel.add(heroPanel);
        mainPanel.add(featuresPanel);
        mainPanel.add(aboutPanel);
        mainPanel.add(contactPanel);
        
        // Create scroll pane with custom styling
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(darkBg);
        
        // Custom scrollbar styling
        customizeScrollBar(scrollPane);
        
        setContentPane(scrollPane);
    }
    
    private JPanel createHeroSection() {
        JPanel heroPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        
        heroPanel.setOpaque(false);
        heroPanel.setLayout(new GridBagLayout());
        heroPanel.setPreferredSize(new Dimension(1000, 600));
        
        // Hero content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        
        // Title
        JLabel titleLabel = new JLabel("Pet Needs & Accessories");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 48));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Add spacing
        contentPanel.add(Box.createVerticalStrut(500));
        contentPanel.add(titleLabel);
        
        heroPanel.add(contentPanel);
        return heroPanel;
    }
    
    private JPanel createFeaturesSection() {
        JPanel sectionPanel = createSectionPanel("Featured Product", 500);

        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setOpaque(false);

        JPanel featuresGrid = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20));
        featuresGrid.setOpaque(false);
        featuresGrid.setBorder(BorderFactory.createEmptyBorder(20, 50, 50, 50));

        JScrollPane scrollPane = new JScrollPane(featuresGrid);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(16);
        scrollPane.getViewport().setBackground(darkBg);

        customizeScrollBar(scrollPane);

        contentWrapper.add(scrollPane, BorderLayout.CENTER);
        sectionPanel.add(contentWrapper, BorderLayout.CENTER);

        JLabel loading = new JLabel("Loading product images...");
        loading.setForeground(Color.LIGHT_GRAY);
        featuresGrid.add(loading);

        FirebaseDatabase rtdb;
            try {
                rtdb = FirestoreConnector.getRealtimeDb();
            } catch (IOException e) {
                e.printStackTrace();
                featuresGrid.removeAll();
                JLabel err = new JLabel("Failed to connect to Realtime DB");
                err.setForeground(Color.RED);
                featuresGrid.add(err);
                featuresGrid.revalidate();
                featuresGrid.repaint();
                return sectionPanel;
            }

            DatabaseReference ref = rtdb.getReference("productImages");

            ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                SwingUtilities.invokeLater(() -> {
                    featuresGrid.removeAll();
                    if (!snapshot.exists() || !snapshot.hasChildren()) {
                        JLabel none = new JLabel("No product images found.");
                        none.setForeground(Color.LIGHT_GRAY);
                        featuresGrid.add(none);
                        featuresGrid.revalidate();
                        featuresGrid.repaint();
                        return;
                    }

                    for (DataSnapshot child : snapshot.getChildren()) {
                        String compressedData = null;
                        try {
                            Object cdObj = child.child("compressedData").getValue();
                        if (cdObj != null) compressedData = cdObj.toString();
                        } catch (Exception ex) {
                            compressedData = null;
                        }

                        JPanel card = createFeatureCard("Product", "");
                        card.setPreferredSize(new Dimension(250, 250));
                        card.setLayout(new BorderLayout());
                        card.setOpaque(false);

                        JLabel imgLabel = new JLabel("No image", SwingConstants.CENTER);
                        imgLabel.setForeground(Color.LIGHT_GRAY);

                        String fileName = "";
                        Object fnObj = child.child("fileName").getValue();
                        if (fnObj != null) fileName = fnObj.toString();
                        JLabel info = new JLabel("<html><center>" + escapeHtml(fileName) + "</center></html>", SwingConstants.CENTER);
                        info.setForeground(new Color(200, 200, 200));
                        info.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

                        card.add(imgLabel, BorderLayout.CENTER);
                        card.add(info, BorderLayout.SOUTH);
                        featuresGrid.add(card);

                    if (compressedData != null && !compressedData.isEmpty()) {
                        final String cd = compressedData;
                            new Thread(() -> {
                                try {
                                    byte[] decoded = Base64.getDecoder().decode(cd);

                                    ByteArrayInputStream bais = new ByteArrayInputStream(decoded);
                                    InflaterInputStream iis = new InflaterInputStream(bais);
                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();

                                    byte[] buffer = new byte[4096];
                                    int len;
                                    while ((len = iis.read(buffer)) != -1) {
                                        baos.write(buffer, 0, len);
                                    }
                                    iis.close();

                                    byte[] decompressedBytes = baos.toByteArray();

                                    System.out.println("Decompressed bytes length: " + decompressedBytes.length);
                                        for (int i = 0; i < Math.min(12, decompressedBytes.length); i++) {
                                            System.out.printf("%02X ", decompressedBytes[i]);
                                        }
                                        System.out.println();

                                    BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(decompressedBytes));
                                        if (bufferedImage != null) {
                                            int maxW = 220;
                                            int w = bufferedImage.getWidth();
                                            int h = bufferedImage.getHeight();
                                            int newW = Math.min(w, maxW);
                                            int newH = (int) ((double)newW / w * h);
                                            Image scaled = bufferedImage.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);

                                        SwingUtilities.invokeLater(() -> {
                                            imgLabel.setIcon(new ImageIcon(scaled));
                                            imgLabel.setText("");
                                            card.revalidate();
                                            card.repaint();
                                        });
                                    } else {
                                        SwingUtilities.invokeLater(() -> imgLabel.setText("Invalid image data after decompression"));
                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    SwingUtilities.invokeLater(() -> imgLabel.setText("Failed to decode image"));
                                }
                            }).start();
                        }
                    }
                    featuresGrid.revalidate();
                    featuresGrid.repaint();
                });
            }

            @Override
            public void onCancelled(DatabaseError error) {
                SwingUtilities.invokeLater(() -> {
                    featuresGrid.removeAll();
                    JLabel err = new JLabel("Failed to load images: " + error.getMessage());
                    err.setForeground(Color.RED);
                    featuresGrid.add(err);
                    featuresGrid.revalidate();
                    featuresGrid.repaint();
                });
            }
        });
          return sectionPanel;
        }

    private String escapeHtml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\n","<br>");
    }
    
    private JPanel createAboutSection() {
        JPanel sectionPanel = createSectionPanel("About Us", 500);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));

        JLabel description = new JLabel("<html><div style='line-height: 1.6;'>Loading...</div></html>");
        description.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        description.setForeground(new Color(220, 220, 220));
        description.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(description);

        sectionPanel.add(contentPanel, BorderLayout.CENTER);

        // Fetch Firestore data in background
        new Thread(() -> {
            try {
                Firestore db = FirestoreConnector.getDb();

                // Blocking get in background thread
                DocumentSnapshot doc = db.collection("business_info")
                                     .document("e3zpesGI8fFB3Ai2Z1Ao")
                                     .get()
                                     .get();

                if (doc.exists()) {
                    java.util.function.Function<String, String> getText = field -> {
                        Object value = doc.get(field);
                    if (value instanceof String) {
                        return (String) value;
                    } else if (value instanceof java.util.List) {
                        @SuppressWarnings("unchecked")
                        java.util.List<String> list = (java.util.List<String>) value;
                        return String.join("<br>", list);
                    }
                    return "Not available";
                };

                String text = "<html><div style='line-height: 1.6;'>" +
                        "<b>Mission:</b> " + getText.apply("mission") + "<br><br>" +
                        "<b>Vision:</b> " + getText.apply("vision") + "<br><br>" +
                        "<b>Story:</b> " + getText.apply("story") + "<br><br>" +
                        "<b>Team:</b> " + getText.apply("team") +
                        "</div></html>";

                SwingUtilities.invokeLater(() -> description.setText(text));
            } else {
                SwingUtilities.invokeLater(() -> description.setText("No data found."));
            }
        } catch (Exception e) {
            SwingUtilities.invokeLater(() -> description.setText("Error loading About Us: " + e.getMessage()));
        }
    }).start();

    return sectionPanel;
}

    private JPanel createContactSection() {
        JPanel sectionPanel = createSectionPanel("Get In Touch", 300);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setOpaque(false);
        
        JButton emailButton = createStyledButton("Email Us", primaryColor);
        JButton phoneButton = createStyledButton("Call Us", secondaryColor);
        
        buttonPanel.add(emailButton);
        buttonPanel.add(Box.createHorizontalStrut(20));
        buttonPanel.add(phoneButton);
        
        sectionPanel.add(buttonPanel, BorderLayout.CENTER);
        return sectionPanel;
    }
    
    private JPanel createSectionPanel(String title, int height) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(darkBg);
        panel.setPreferredSize(new Dimension(1000, height));
        panel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(80, 80, 80)));
        
        // Section title
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(40, 0, 20, 0));
        
        panel.add(titleLabel, BorderLayout.NORTH);
        return panel;
    }
    
    private JPanel createFeatureCard(String title, String description) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw rounded rectangle background
                RoundRectangle2D roundedRectangle = new RoundRectangle2D.Float(
                    0, 0, getWidth(), getHeight(), 20, 20);
                g2d.setColor(cardBg);
                g2d.fill(roundedRectangle);
                
                // Add subtle border
                g2d.setColor(new Color(100, 100, 100));
                g2d.setStroke(new BasicStroke(1));
                g2d.draw(roundedRectangle);
                
                g2d.dispose();
            }
        };
        
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));
        card.setPreferredSize(new Dimension(250, 200));
        
        // Add hover effect
        addHoverEffect(card);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel descLabel = new JLabel("<html><div style='text-align: center;'>" + description + "</div></html>");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descLabel.setForeground(new Color(200, 200, 200));
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(15));
        card.add(descLabel);
        
        return card;
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Button background
                RoundRectangle2D roundedRectangle = new RoundRectangle2D.Float(
                    0, 0, getWidth(), getHeight(), 25, 25);
                
                if (getModel().isPressed()) {
                    g2d.setColor(bgColor.darker());
                } else if (getModel().isRollover()) {
                    g2d.setColor(bgColor.brighter());
                } else {
                    g2d.setColor(bgColor);
                }
                
                g2d.fill(roundedRectangle);
                g2d.dispose();
                
                // Draw text
                super.paintComponent(g);
            }
        };
        
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        return button;
    }
    
    private void addHoverEffect(JPanel card) {
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(accentColor, 2),
                    BorderFactory.createEmptyBorder(28, 18, 28, 18)
                ));
                card.repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                card.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));
                card.repaint();
            }
        });
    }
    
    private void customizeScrollBar(JScrollPane scrollPane) {
        scrollPane.getVerticalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(100, 100, 100);
                this.trackColor = new Color(50, 50, 50);
            }
            
            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }
            
            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }
            
            private JButton createZeroButton() {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                button.setMinimumSize(new Dimension(0, 0));
                button.setMaximumSize(new Dimension(0, 0));
                return button;
            }
        });
    }
    
    public static void main(String[] args) {
        // Set system look and feel
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, fall back to default
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        
        SwingUtilities.invokeLater(() -> {
            new main().setVisible(true);
        });
    }
}