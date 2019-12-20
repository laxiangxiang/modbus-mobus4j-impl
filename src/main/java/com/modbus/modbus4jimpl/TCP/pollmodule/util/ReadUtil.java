package com.modbus.modbus4jimpl.TCP.pollmodule.util;

import com.serotonin.modbus4j.BatchRead;
import com.serotonin.modbus4j.BatchResults;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.code.DataType;
import com.serotonin.modbus4j.exception.ErrorResponseException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.locator.BaseLocator;
import com.serotonin.modbus4j.msg.*;

/**
 * 01 02 03 04 功能码的读操作
 * @author shdq-fjy
 */
public class ReadUtil {

    /**
     * 读（线圈）开关量数据
     *
     * @param slaveId slaveId
     * @param offset  位置
     * @param numberOfBits 读几个地址位
     * @return 读取值
     */
    public static boolean[] readCoilStatus(ModbusMaster master,int slaveId, int offset, int numberOfBits) throws ModbusTransportException {
        ReadCoilsRequest request = new ReadCoilsRequest(slaveId, offset, numberOfBits);
        ReadCoilsResponse response = (ReadCoilsResponse) master.send(request);
        boolean[] booleans = response.getBooleanData();
        return valueRegroup(numberOfBits, booleans);
    }

    /**
     * 开关数据 读取外围设备输入的开关量
     * @param numberOfBits 读几个地址位
     */
    public static boolean[] readInputStatus(ModbusMaster master,int slaveId, int offset, int numberOfBits) throws ModbusTransportException {
        ReadDiscreteInputsRequest request = new ReadDiscreteInputsRequest(slaveId, offset, numberOfBits);
        ReadDiscreteInputsResponse response = (ReadDiscreteInputsResponse) master.send(request);
        boolean[] booleans = response.getBooleanData();
        return valueRegroup(numberOfBits, booleans);
    }

    /**
     * 读取保持寄存器数据
     *
     * @param slaveId slave Id
     * @param offset  位置
     * @param numberOfBits 读几个地址位
     */
    public static short[] readHoldingRegister(ModbusMaster master,int slaveId, int offset, int numberOfBits) throws ModbusTransportException {
        ReadHoldingRegistersRequest request = new ReadHoldingRegistersRequest(slaveId, offset, numberOfBits);
        ReadHoldingRegistersResponse response = (ReadHoldingRegistersResponse) master.send(request);
        return response.getShortData();
    }

    /**
     * 读取外围设备输入的数据
     *
     * @param slaveId slaveId
     * @param offset  位置
     * @param numberOfBits 读几个地址位
     */
    public static short[] readInputRegisters(ModbusMaster master,int slaveId, int offset, int numberOfBits) throws ModbusTransportException {
        ReadInputRegistersRequest request = new ReadInputRegistersRequest(slaveId, offset, numberOfBits);
        ReadInputRegistersResponse response = (ReadInputRegistersResponse) master.send(request);
        return response.getShortData();
    }

    /**
     * 批量读取 可以批量读取不同寄存器中数据
     */
    public static void batchRead(ModbusMaster master) throws ModbusTransportException, ErrorResponseException {
        BatchRead<Integer> batch = new BatchRead<Integer>();
        batch.addLocator(0, BaseLocator.holdingRegister(1, 1, DataType.TWO_BYTE_INT_SIGNED));
        batch.addLocator(1, BaseLocator.inputStatus(1, 0));
        batch.setContiguousRequests(true);
        BatchResults<Integer> results = master.send(batch);
        System.out.println("batchRead:" + results.getValue(0));
        System.out.println("batchRead:" + results.getValue(1));
    }

    private static boolean[] valueRegroup(int numberOfBits, boolean[] values) {
        boolean[] bs = new boolean[numberOfBits];
        int temp = 1;
        for (boolean b : values) {
            bs[temp - 1] = b;
            temp++;
            if (temp > numberOfBits) {
                break;
            }
        }
        return bs;
    }
}
