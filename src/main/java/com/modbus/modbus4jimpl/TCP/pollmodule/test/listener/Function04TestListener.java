package com.modbus.modbus4jimpl.TCP.pollmodule.test.listener;


import com.modbus.modbus4jimpl.TCP.pollmodule.core.MonitoredDataItem;
import com.modbus.modbus4jimpl.TCP.pollmodule.core.MonitoredDataItemListener;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Function04TestListener implements MonitoredDataItemListener {
    @Override
    public void onDataChange(MonitoredDataItem var1, Object var2) {
        short[] value = (short[]) var2;
        shortArray(var1,value);
    }

    @Override
    public void booleanArray(MonitoredDataItem var1, boolean[] var2) {
    }

    @Override
    public void shortArray(MonitoredDataItem var1, short[] var2) {
//        String nodName = var1.getSubscribeSlaveNode().getNodeName();
//        System.out.println(nodName+":");
//        for (short i : var2) {
//            System.out.println(i);
//        }
    }
}
