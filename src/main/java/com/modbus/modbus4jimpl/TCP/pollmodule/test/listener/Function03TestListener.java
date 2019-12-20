package com.modbus.modbus4jimpl.TCP.pollmodule.test.listener;


import com.modbus.modbus4jimpl.TCP.pollmodule.core.MonitoredDataItem;
import com.modbus.modbus4jimpl.TCP.pollmodule.core.MonitoredDataItemListener;

/**
 * @author shdq-fjy
 */
public class Function03TestListener implements MonitoredDataItemListener {

    private short oldValue;

    @Override
    public void onDataChange(MonitoredDataItem var1, Object var2) {
        short[] shorts = (short[]) var2;
        shortArray(var1,shorts);
    }

    @Override
    public void booleanArray(MonitoredDataItem var1, boolean[] var2) {

    }

    @Override
    public void shortArray(MonitoredDataItem var1, short[] var2) {
        String nodName = var1.getSubscribeSlaveNode().getNodeName();
        System.out.println(nodName+":");
        for (short i : var2) {
            System.out.println(i);
            if (oldValue == 0){
                oldValue = i;
                break;
            }else {
                if (i - oldValue > 1){
                    System.out.println("has wrong...");
                }
                oldValue = i;
            }
        }
    }
}
