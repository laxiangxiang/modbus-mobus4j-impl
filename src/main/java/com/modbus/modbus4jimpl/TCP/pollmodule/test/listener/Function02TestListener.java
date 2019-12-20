package com.modbus.modbus4jimpl.TCP.pollmodule.test.listener;


import com.modbus.modbus4jimpl.TCP.pollmodule.core.MonitoredDataItem;
import com.modbus.modbus4jimpl.TCP.pollmodule.core.MonitoredDataItemListener;

/**
 * @author shdq-fjy
 */
public class Function02TestListener implements MonitoredDataItemListener {

    @Override
    public void onDataChange(MonitoredDataItem var1, Object var2) {
        boolean[] booleans = (boolean[]) var2;
        booleanArray(var1,booleans);
    }

    @Override
    public void booleanArray(MonitoredDataItem var1, boolean[] var2) {
//        String nodeName = var1.getSubscribeSlaveNode().getNodeName();
//        System.out.println(nodeName+":");
//        for (boolean b : var2) {
//            System.out.println(b);
//        }
    }

    @Override
    public void shortArray(MonitoredDataItem var1, short[] var2) {

    }
}
