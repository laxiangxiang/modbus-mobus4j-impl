package com.modbus.modbus4jimpl.TCP.masterAndSlave.master;

import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.ip.IpParameters;

public class TcpMaster {

    private static ModbusFactory modbusFactory;

    static {
        if (modbusFactory == null) {
            modbusFactory = new ModbusFactory();
        }
    }

    /**
     * 获取master
     * @return master
     */
    public static ModbusMaster getMaster() {
        IpParameters params = new IpParameters();
        params.setHost("localhost");
        params.setPort(502);
        //自己实现slave端时设置
        params.setEncapsulated(true);
        //使用slave模拟软件时设置
//        params.setEncapsulated(false);
        ModbusMaster master = modbusFactory.createTcpMaster(params, false);// TCP 协议
        try {
            //设置超时时间
            master.setTimeout(1000);
            //设置重连次数
            master.setRetries(3);
            //初始化
            master.init();
        } catch (ModbusInitException e) {
            e.printStackTrace();
        }
        return master;
    }
}
