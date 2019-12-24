package com.modbus.modbus4jimpl.TCP.masterAndSlave.master.locator;

import com.modbus.modbus4jimpl.TCP.masterAndSlave.master.TcpMaster;
import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.code.DataType;
import com.serotonin.modbus4j.exception.ErrorResponseException;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.locator.BaseLocator;
import com.serotonin.modbus4j.msg.*;

/**
 * modbus写入工具类
 */
public class Modbus4jWriteUtils {

    //获取master
    private static ModbusMaster master = TcpMaster.getMaster();

    /**
     * 写 [01 Coil Status(0x)]写一个 function ID = 5
     * @param slaveId
     * @param writeOffset
     * @param writeValue
     * @return
     */
    public static boolean writeCoil(int slaveId,int writeOffset,boolean writeValue) throws ModbusTransportException{
        WriteCoilRequest request = new WriteCoilRequest(slaveId,writeOffset,writeValue);
        WriteCoilResponse response = (WriteCoilResponse) master.send(request);
        if (response.isException()){
            return false;
        }else {
            return true;
        }
    }

    /**
     * 写[01 Coil Status(0x)] 写多个 function ID = 15
     * @param slaveId
     * @param startOffset
     * @param bdata
     * @return
     */
    public static boolean writeCoils(int slaveId,int startOffset,boolean[] bdata) throws ModbusTransportException {
        WriteCoilsRequest request = new WriteCoilsRequest(slaveId,startOffset,bdata);
        WriteCoilsResponse response = (WriteCoilsResponse) master.send(request);
        if (response.isException()){
            return false;
        }else {
            return true;
        }
    }

    /**
     * 写[03 Holding Register(4x)] 写一个 function ID = 6
     * @param slaveId
     * @param writeOffset
     * @param writeValue
     * @return
     * @throws ModbusTransportException
     * @throws ModbusInitException
     */
    public static boolean writeRegister(int slaveId,int writeOffset,short writeValue) throws ModbusTransportException{
        WriteRegisterRequest registerRequest = new WriteRegisterRequest(slaveId,writeOffset,writeValue);
        WriteRegisterResponse registerResponse = (WriteRegisterResponse) master.send(registerRequest);
        if (registerResponse.isException()){
            return false;
        }else {
            return true;
        }
    }

    /**
     * 写入[03 Holding Register(4x)]写多个 function ID=16
     * @param slaveId
     * @param startOffset
     * @param sdata
     * @return
     * @throws ModbusTransportException
     * @throws ModbusInitException
     */
    public static boolean writeRegisters(int slaveId,int startOffset,short[] sdata) throws ModbusTransportException{
        WriteRegistersRequest registersRequest = new WriteRegistersRequest(slaveId,startOffset,sdata);
        WriteRegistersResponse registersResponse = (WriteRegistersResponse) master.send(registersRequest);
        if (registersResponse.isException()){
            return false;
        }else {
            return true;
        }
    }

    /**
     * 写入数字类型的模拟量（如:写入Float类型的模拟量、Double类型模拟量、整数类型Short、Integer、Long）
     *
     * @param slaveId
     * @param offset
     * @param value 写入值,Number的子类,例如写入Float浮点类型,Double双精度类型,以及整型short,int,long
     * @param dataType com.serotonin.modbus4j.code.DataType
     * @throws ModbusTransportException
     * @throws ModbusInitException
     * @throws ErrorResponseException
     */
    public static void writeHoldingRegister(int slaveId,int offset,Number value,int dataType) throws ModbusTransportException, ErrorResponseException {
        BaseLocator<Number> locator = BaseLocator.holdingRegister(slaveId,offset,dataType);
        master.setValue(locator,value);
    }

    public static void main(String[] args) {
        try {
            //@formatter:off
            // 测试01
//          boolean t01 = writeCoil(1, 0, true);
//          System.out.println("T01:" + t01);

            // 测试02
//          boolean t02 = writeCoils(1, 0, new boolean[] { true, false, true });
//          System.out.println("T02:" + t02);

            // 测试03
//          short v = -3;
//          boolean t03 = writeRegister(3, 0, v);
//          System.out.println("T03:" + t03);
            // 测试04
//          boolean t04 = writeRegisters(3, 0, new short[] { -3, 3, 9 });
//          System.out.println("t04:" + t04);

            //写模拟量
            writeHoldingRegister(3,0, 10.1f, DataType.FOUR_BYTE_FLOAT);

            //@formatter:on
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
