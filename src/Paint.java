import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Objects;
import java.util.Random;

/**
 * HW 12 - Paint
 *
 * <p> Purdue University -- CS 180 -- 2021</p>
 *
 * @author Jin Yan Purdue CS
 *
 * @version Nov 16th, 2021
 */

public class Paint extends JComponent implements Runnable {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Paint());
    }

    Image image;
    Graphics2D graphics2D;
    int curX;
    int curY;
    int oldX;
    int oldY;
    JButton clrButton;
    JButton fillButton;
    JButton eraseButton;
    JButton randomButton;
    JButton hexButton;
    JButton rgbButton;
    JTextField hexText;
    JTextField rText;
    JTextField gText;
    JTextField bText;
    Paint paint;


    @Override
    public void run() {
        JFrame frame = new JFrame();
        frame.setTitle("Paint");

        Container content = frame.getContentPane();
        content.setLayout(new BorderLayout());
        paint = new Paint();
        content.add(paint, BorderLayout.CENTER);

        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);

        clrButton = new JButton("Clear");
        fillButton = new JButton("Fill");
        eraseButton = new JButton("Erase");
        randomButton = new JButton("Random");
        hexButton = new JButton("Hex");
        rgbButton = new JButton("RGB");
        hexText = new JTextField("#", 10);
        rText = new JTextField(5);
        gText = new JTextField(5);
        bText = new JTextField(5);

        JPanel panelTop = new JPanel();
        panelTop.add(clrButton);
        clrButton.addActionListener(actionListener);
        panelTop.add(fillButton);
        fillButton.addActionListener(actionListener);
        panelTop.add(eraseButton);
        eraseButton.addActionListener(actionListener);
        panelTop.add(randomButton);
        randomButton.addActionListener(actionListener);
        content.add(panelTop, BorderLayout.NORTH);

        JPanel panelBottom = new JPanel();
        panelBottom.add(hexButton);
        hexButton.addActionListener(actionListener);
        panelBottom.add(rgbButton);
        rgbButton.addActionListener(actionListener);
        panelBottom.add(hexText);
        panelBottom.add(rText);
        panelBottom.add(gText);
        panelBottom.add(bText);
        content.add(panelBottom, BorderLayout.SOUTH);


    }

    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == clrButton) {
                paint.clrBackground();
                hexText.setText("#");
                rText.setText("");
                gText.setText("");
                bText.setText("");
            }
            if (e.getSource() == fillButton) {
                paint.fillBackground();
                hexText.setText("#");
                rText.setText("");
                gText.setText("");
                bText.setText("");
            }
            if (e.getSource() == eraseButton) {
                paint.eraseBackground();
            }
            if (e.getSource() == randomButton) {
                Random random = new Random();
                int r = random.nextInt(256);
                int g = random.nextInt(256);
                int b = random.nextInt(256);
                Color rgbColor = new Color(r, g, b);
                paint.setPenColor(rgbColor);
                hexText.setText(String.format("#%02x%02x%02x", r, g, b));
                rText.setText(Integer.toString(r));
                gText.setText(Integer.toString(g));
                bText.setText(Integer.toString(b));
            }
            if (e.getSource() == hexButton) {
                try {
                    Color color = Color.decode(hexText.getText());
                    paint.setPenColor(color);
                    rText.setText(Integer.toString(color.getRed()));
                    gText.setText(Integer.toString(color.getGreen()));
                    bText.setText(Integer.toString(color.getBlue()));

                } catch (NumberFormatException i) {
                    System.out.println(i);
                    JOptionPane.showMessageDialog(null, "Not a valid Hex Value",
                            "Error", JOptionPane.INFORMATION_MESSAGE);
                }
            }
            if (e.getSource() == rgbButton) {

                try {
                    if(Objects.equals(rText.getText(), "")) {
                        rText.setText("0");
                    }
                    if(Objects.equals(gText.getText(), "")) {
                        gText.setText("0");
                    }
                    if(Objects.equals(bText.getText(), "")) {
                        bText.setText("0");
                    }
                    int r = Integer.parseInt(rText.getText()) ;
                    int g = Integer.parseInt(gText.getText()) ;
                    int b = Integer.parseInt(bText.getText()) ;
                    Color rgbColor = new Color(r, g, b);
                    paint.setPenColor(rgbColor);
                    hexText.setText(String.format("#%02x%02x%02x", r, g, b));

                } catch (Exception i) {
                    System.out.println(i);
                    JOptionPane.showMessageDialog(null, "Not a valid RGB Value",
                            "Error", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
    };

    public void clrBackground() {
        graphics2D.setPaint(Color.white);
        graphics2D.fillRect(0, 0, getSize().width, getSize().height);
        graphics2D.setPaint(Color.black);
        repaint();
    }

    public void setPenColor(Color color) {
        graphics2D.setPaint(color);
    }

    public void eraseBackground() {
        graphics2D.setPaint(graphics2D.getBackground());
    }

    public void fillBackground() {
        graphics2D.setBackground(graphics2D.getColor());
        graphics2D.setPaint(graphics2D.getBackground());
        graphics2D.fillRect(0, 0, getSize().width, getSize().height);
        graphics2D.setPaint(Color.black);
        repaint();
    }

    protected void paintComponent(Graphics g) {
        if (image == null) {
            image = createImage(getSize().width, getSize().height);

            graphics2D = (Graphics2D) image.getGraphics();

            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            graphics2D.setPaint(Color.white);
            graphics2D.fillRect(0, 0, getSize().width, getSize().height);
            graphics2D.setPaint(Color.black);
            repaint();
        }
        g.drawImage(image, 0, 0, null);
    }

    public Paint() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                oldX = e.getX();
                oldY = e.getY();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                curX = e.getX();
                curY = e.getY();

                graphics2D.drawLine(oldX, oldY, curX, curY);

                repaint();
                oldX = curX;
                oldY = curY;

            }
        });
    }





}