/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapping;

import java.awt.Point;

/**
 *
 * @author Admin
 */
public class RealToDeviceWindowMapping {

    RealWindow rWin;
    DeviceWindow dWin;
    double Cw, Ch, C1, C2;

    /*Cw = dWin.width/rWin.width;
    Ch = dWin.height/rWin.height;
    C1 = dWin.left - Cw*rWin.minX;
    C2 = Ch*rWin.minY = dWin.top + dWin.height;
    x2 = Cwx1 + C1;
    y2 = - Chy1 + C2;
     */
    public RealToDeviceWindowMapping(RealWindow rWin, DeviceWindow dWin) {
        this.rWin = rWin;
        this.dWin = dWin;
        Cw = dWin.width / rWin.width;
        Ch = dWin.height / rWin.height;
        C1 = dWin.left - Cw * rWin.minX;
        C2 = Ch * rWin.minY + dWin.top + dWin.height;
    }

    public Point map(RealPoint p) {
        int x2 = (int) (Math.round(Cw * p.x + C1));
        int y2 = (int) (Math.round(-Ch * p.y + C2));
        return new Point(x2, y2);
    }
    public DevicePointList map(RealPointList rList) {
        DevicePointList dList = new DevicePointList();
        for (RealPoint realPoint: rList) dList.add(map(realPoint));
        return dList;
    }

}
