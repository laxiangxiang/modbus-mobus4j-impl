package com.modbus.modbus4jimpl.TCP.masterAndSlave.master.locator;

import com.modbus.modbus4jimpl.TCP.masterAndSlave.master.TcpMaster;
import com.serotonin.modbus4j.BatchRead;
import com.serotonin.modbus4j.BatchResults;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.code.DataType;
import com.serotonin.modbus4j.exception.ErrorResponseException;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.locator.BaseLocator;

/**
 * modbus读取工具类，采用modbus4j实现
 */
public class Modbus4jReadUtils {

    //获取master
    private static ModbusMaster master = TcpMaster.getMaster();

    /**
     * 读取[01 Coil Status 0x]类型 开关数据
     * @param slaveId
     * @param offset
     * @return
     */
    public static boolean readCoilStatus(int slaveId,int offset) throws ModbusTransportException, ErrorResponseException {
        BaseLocator<Boolean> baseLocator = BaseLocator.coilStatus(slaveId,offset);
        boolean value = master.getValue(baseLocator);
        return value;
    }

    /**
     * 读取[02 Input Status 1x]类型 开关数据
     * @param slaveId
     * @param offset
     * @return
     * @throws ModbusInitException
     * @throws ModbusTransportException
     * @throws ErrorResponseException
     */
    public static boolean readInputStatus(int slaveId,int offset) throws ModbusTransportException, ErrorResponseException {
        BaseLocator<Boolean> baseLocator = BaseLocator.inputStatus(slaveId,offset);
        boolean value = master.getValue(baseLocator);
        return value;
    }

    /**
     * 读取[03 Holding Register类型 2x]模拟量数据
     * @param slaveId
     * @param offset
     * @param dataType 数据类型,来自com.serotonin.modbus4j.code.DataType
     * @return
     */
    public static Number readHoldingRegister(int slaveId,int offset,int dataType) throws ModbusTransportException, ErrorResponseException {
        BaseLocator<Number> baseLocator = BaseLocator.holdingRegister(slaveId,offset,dataType);
        Number value = master.getValue(baseLocator);
        return value;
    }

    /**
     * 读取[04 Input Registers 3x]类型 模拟量数据
     * @param slaveId
     * @param offset
     * @param dataType
     * @return
     * @throws ModbusInitException
     * @throws ModbusTransportException
     * @throws ErrorResponseException
     */
    public static Number readInputRegister(int slaveId,int offset,int dataType) throws ModbusTransportException, ErrorResponseException {
        BaseLocator<Number> baseLocator = BaseLocator.inputRegister(slaveId,offset,dataType);
        Number value = master.getValue(baseLocator);
        return value;
    }

    /**
     * 批量读取使用方法
     * @throws ModbusInitException
     * @throws ModbusTransportException
     * @throws ErrorResponseException
     */
    public static void batchRead() throws ModbusTransportException, ErrorResponseException {
        BatchRead<Integer> batchRead = new BatchRead<>();
        batchRead.addLocator(0,BaseLocator.holdingRegister(1,1, DataType.FOUR_BYTE_FLOAT));
        batchRead.addLocator(1,BaseLocator.inputStatus(1,0));
        batchRead.setContiguousRequests(false);
        BatchResults<Integer> results = master.send(batchRead);
        System.out.println(results.getValue(0));
        System.out.println(results.getValue(1));
    }

    /**
     * 测试
     *
     * @param args
     */
    public static void main(String[] args) {
        try {
            // 01测试
            Boolean v011 = readCoilStatus(1, 0);
            Boolean v012 = readCoilStatus(1, 1);
            Boolean v013 = readCoilStatus(1, 6);
            System.out.println("v011:" + v011);
            System.out.println("v012:" + v012);
            System.out.println("v013:" + v013);
            // 02测试
            Boolean v021 = readInputStatus(2, 0);
            Boolean v022 = readInputStatus(2, 1);
            Boolean v023 = readInputStatus(2, 2);
            System.out.println("v021:" + v021);
            System.out.println("v022:" + v022);
            System.out.println("v023:" + v023);

            // 03测试
            Number v031 = readHoldingRegister(3, 1, DataType.FOUR_BYTE_FLOAT);// 注意,float
            Number v032 = readHoldingRegister(3, 3, DataType.FOUR_BYTE_FLOAT);// 同上
            System.out.println("v031:" + v031);
            System.out.println("v032:" + v032);

            // 04测试
            Number v041 = readInputRegister(4, 0, DataType.FOUR_BYTE_FLOAT);
            Number v042 = readInputRegister(4, 2, DataType.FOUR_BYTE_FLOAT);
            System.out.println("v041:" + v041);
            System.out.println("v042:" + v042);
            // 批量读取
            batchRead();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
