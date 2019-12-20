package com.modbus.modbus4jimpl.TCP.pollmodule.util;

import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.exception.ErrorResponseException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.locator.BaseLocator;
import com.serotonin.modbus4j.msg.*;

/**
 * @author shdq-fjy
 */
public class WriteUtil {

    /**
     * 写单个（线圈）开关量数据
     * @param slaveId     slave的ID
     * @param offset 位置
     * @param writeValue  值
     * @return 是否写入成功
     */
    public static boolean writeCoil(ModbusMaster master,int slaveId, int offset, boolean writeValue) throws ModbusTransportException {
        // 创建请求
        WriteCoilRequest request = new WriteCoilRequest(slaveId, offset, writeValue);
        // 发送请求并获取响应对象
        WriteCoilResponse response = (WriteCoilResponse) master.send(request);
        return !response.isException();
    }

    /**
     * 写多个开关量数据（线圈）
     *
     * @param slaveId     slaveId
     * @param offset 开始位置
     * @param bdata       写入的数据
     * @return 是否写入成功
     */
    public static boolean writeCoils(ModbusMaster master,int slaveId, int offset, boolean[] bdata) throws ModbusTransportException {
        // 创建请求
        WriteCoilsRequest request = new WriteCoilsRequest(slaveId, offset, bdata);
        // 发送请求并获取响应对象
        WriteCoilsResponse response = (WriteCoilsResponse) master.send(request);
        return !response.isException();

    }

    /***
     *  保持寄存器写单个
     *
     * @param slaveId slaveId
     * @param offset 开始位置
     * @param writeValue 写入的数据
     */
    public static boolean writeRegister(ModbusMaster master,int slaveId, int offset, short writeValue) throws ModbusTransportException {
        // 创建请求对象
        WriteRegisterRequest request = new WriteRegisterRequest(slaveId, offset, writeValue);
        // 发送请求并获取响应对象
        WriteRegisterResponse response = (WriteRegisterResponse) master.send(request);
        return !response.isException();
    }

    /**
     * 保持寄存器写入多个模拟量数据
     *
     * @param slaveId     modbus的slaveID
     * @param offset 起始位置偏移量值
     * @param sdata       写入的数据
     * @return 返回是否写入成功
     */
    public static boolean writeRegisters(ModbusMaster master,int slaveId, int offset, short[] sdata) throws ModbusTransportException {
        // 创建请求对象
        WriteRegistersRequest request = new WriteRegistersRequest(slaveId, offset, sdata);
        // 发送请求并获取响应对象
        WriteRegistersResponse response = (WriteRegistersResponse) master.send(request);
        return !response.isException();
    }

    /**
     * 根据类型写数据（如:写入Float类型的模拟量、Double类型模拟量、整数类型Short、Integer、Long）
     *
     * @param value    写入值
     * @param dataType com.serotonin.modbus4j.code.DataType
     */
    public static void writeHoldingRegister(ModbusMaster master,int slaveId, int offset, Number value, int dataType) throws ModbusTransportException, ErrorResponseException {
        // 类型
        BaseLocator<Number> locator = BaseLocator.holdingRegister(slaveId, offset, dataType);
        master.setValue(locator, value);
    }
}
