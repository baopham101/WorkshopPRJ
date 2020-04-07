/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapping;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JOptionPane;
import java.awt.Point;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

/**
 *
 * @author Admin
 */
public class LineChartPanel extends javax.swing.JPanel {

    /**
     *
     * @author Admin
     */
    int leftGap = 40;
    int topGap = 10;
    int rightGap = 40;
    int bottomGsp = 40;
    int innerGap = 30;
    Color currentColor = Color.BLACK;
    DeviceWindow chartArea = null;
    int numberofScale = 4;

    /**
     * Creates new form LineChartPanel
     */
    public LineChartPanel() {
        initComponents();
    }

    public int getLeftGap() {
        return leftGap;
    }

    public void setLeftGap(int leftGap) {
        this.leftGap = leftGap;
    }

    public int getTopGap() {
        return topGap;
    }

    public void setTopGap(int topGap) {
        this.topGap = topGap;
    }

    public int getRightGap() {
        return rightGap;
    }

    public void setRightGap(int rightGap) {
        this.rightGap = rightGap;
    }

    public int getBottomGsp() {
        return bottomGsp;
    }

    public void setBottomGsp(int bottomGsp) {
        this.bottomGsp = bottomGsp;
    }

    public int getInnerGap() {
        return innerGap;
    }

    public void setInnerGap(int innerGap) {
        this.innerGap = innerGap;
    }

    public Color getCurrentColor() {
        return currentColor;
    }

    public void setCurrentColor(Color currentColor) {
        this.currentColor = currentColor;
    }

    public DeviceWindow getChartArea() {
        return chartArea;
    }

    public void setChartArea(DeviceWindow chartArea) {
        this.chartArea = chartArea;
    }

    public int getNumberofScale() {
        return numberofScale;
    }

    public void setNumberofScale(int numberofScale) {
        this.numberofScale = numberofScale;
    }

    void setupChartArea() {
        int left = leftGap + innerGap;
        int top = topGap + innerGap;
        int width = this.getWidth() - leftGap - rightGap - 2 * innerGap;
        int height = this.getHeight() - topGap - bottomGsp - 2 * innerGap;
        try {
            this.chartArea = new DeviceWindow(left, top, width, height);
        } catch (Exception e) {
            String msg = "Device window parameters must be positive integers";
            JOptionPane.showMessageDialog(this, msg);
        }
    }

    void drawAxes() {
        Graphics g = this.getGraphics();
        int arrowLength = 10, arrowWidth = 2;

        int x1, y1, x2, y2;
        try {
        x1 = x2 = this.leftGap;
        y1 = this.getHeight() - this.bottomGsp;
        y2 = this.topGap;
        g.drawLine(x1, y1, x2, y2);

        g.drawLine(x2 - arrowWidth, y2 + arrowLength, x2, y2);
        g.drawLine(x2 + arrowWidth, y2 + arrowLength, x2, y2);

        x1 = this.leftGap;
        x2 = this.getWidth() - this.rightGap;
        y1 = y2 = this.getHeight() - this.bottomGsp;
        g.drawLine(x1, y1, x2, y2);

        g.drawLine(x2, y2, x2 - arrowLength, y2 - arrowWidth);
        g.drawLine(x2, y2, x2 - arrowLength, y2 + arrowWidth);
        }catch(Exception e) {
            JOptionPane.showMessageDialog(this, e);
        }
    }

    void drawLabels(String x_Label, String y_Label) {
        Graphics g = this.getGraphics();
        Font font = g.getFont();
        FontMetrics fm = g.getFontMetrics();
        try {
        int H = fm.getHeight();
        int Lx = fm.stringWidth(x_Label);
        int Ly = fm.stringWidth(y_Label);;
        int x, y;

        x = this.leftGap + this.chartArea.width / 2 + Lx / 2;
        y = this.getHeight() - this.bottomGsp + H + 10;
        g.drawString(x_Label, x, y);

        x = this.leftGap - H / 2 - 10;
        y = this.topGap + this.getHeight() / 2 - Ly / 2;
        Graphics2D g2D = (Graphics2D) g;
        g2D.rotate(-Math.PI / 2, x, y);
        g2D.drawString(y_Label, x, y);
        }catch(Exception e) {
            JOptionPane.showMessageDialog(this, e);
        }
    }

    private void drawX_labelScale(Graphics g, String S, int x, int y) {
        FontMetrics fm = g.getFontMetrics();
        int L = fm.stringWidth(S);
        g.drawString(S, x - L / 2, y + fm.getHeight() + 5);
    }

    private void drawY_LabelScale(Graphics g, String S, int xAxis, int yAxis) {
        FontMetrics fm = g.getFontMetrics();
        int L = fm.stringWidth(S);
        g.drawString(S, xAxis - L - 10, yAxis);
    }

    public void drawScale(RealPointList rList) {
        Graphics g = this.getGraphics();

        int y = this.getHeight() - this.bottomGsp;
        int dx = this.chartArea.width / (numberofScale - 1);
        int x = chartArea.left;
        double deltaX = (rList.maxX - rList.minX) / (numberofScale - 1);
        double xValue = rList.minX;
        for (int i = 0; i < numberofScale; i++) {
            g.drawLine(x, y, x, y + 5);
            if (i == 0 || i ==numberofScale-1)
                this.drawX_labelScale(g, "" + xValue, x, y);
            x +=dx;
            xValue += deltaX;   
        }
        x = this.leftGap;
        y = chartArea.top + chartArea.height;
        int dy = this.chartArea.height/(numberofScale-1);
        double yValue = rList.minY;
        double deltaY = (rList.maxY-rList.minY)/(numberofScale-1);
        for (int i = 0; i < numberofScale; i++) {
            g.drawLine(x, y, x-5, y);
            if (i==0 || i==numberofScale-1)
                this.drawY_LabelScale(g, "" + yValue, x, y);
            y -= dy;
            yValue += deltaY;
        }
    }
    public void drawChart (RealPointList list, double minX, double minY, double maxX, double maxY) {
        RealWindow rWindow = null;
        double width = maxX - minX;
        double height = maxY-minY;
        try {
            rWindow = new RealWindow(minX, minY, width, height);
        }catch (Exception e) {
            String msg = "Parameters of real window must be positive numbers!";
            JOptionPane.showMessageDialog(this, msg);
        }
        if (rWindow != null) {
            RealToDeviceWindowMapping map = new RealToDeviceWindowMapping(rWindow, chartArea);
            DevicePointList pList = map.map(list);
            int n = pList.size();
            if (n>1) {
                {  Graphics g = this.getGraphics();
                    Point p1 = pList.get(0);
                    Point p2;
                    int i =1;
                    while (i <n) {
                        p2 = pList.get(i);
                        g.drawLine(p1.x, p1.y, p2.x, p2.y);
                        p1 = p2;
                        i++;
                    }
                }
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
