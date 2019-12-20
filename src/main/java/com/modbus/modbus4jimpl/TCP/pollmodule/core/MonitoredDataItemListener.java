package com.modbus.modbus4jimpl.TCP.pollmodule.core;

/**
 * @author shdq-fjy
 */
public interface MonitoredDataItemListener {

    void onDataChange(MonitoredDataItem var1, Object var2);

    void booleanArray(MonitoredDataItem var1, boolean[] var2);

    void shortArray(MonitoredDataItem var1, short[] var2);
}
