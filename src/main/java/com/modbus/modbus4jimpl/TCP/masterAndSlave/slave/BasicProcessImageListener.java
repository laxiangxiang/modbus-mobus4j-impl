package com.modbus.modbus4jimpl.TCP.masterAndSlave.slave;

import com.serotonin.modbus4j.ProcessImageListener;

/**
 * slave端数据变化时调用
 */
public class BasicProcessImageListener implements ProcessImageListener {
    @Override
    public void coilWrite(int offset, boolean oldValue, boolean newValue) {
        System.out.println("Coil at " + offset + " was set from " + oldValue + " to " + newValue);
    }

    @Override
    public void holdingRegisterWrite(int offset, short oldValue, short newValue) {
        System.out.println("HoldRegister at " + offset + " was set from " + oldValue + " to " + newValue);
    }
}
