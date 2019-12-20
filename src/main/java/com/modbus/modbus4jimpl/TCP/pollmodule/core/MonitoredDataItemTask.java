package com.modbus.modbus4jimpl.TCP.pollmodule.core;

import com.modbus.modbus4jimpl.TCP.pollmodule.util.MonitoredDataCompare;
import com.modbus.modbus4jimpl.TCP.pollmodule.util.ReadUtil;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * 轮询 数据地址 任务包装类
 * @author shdq-fjy
 */
@Slf4j
@Getter
public class MonitoredDataItemTask implements Runnable{

    //[01 Coil Status 0x]类型 开关量
    public final static int COIL_STATUS = 01;
    //[02 Input Status 1x]类型 开关量
    public final static int INPUT_STATUS = 02;
    //[03 Holding Register类型 2x]模拟量
    public final static int HOLDING_REGISTER = 03;
    //[04 Input Registers 3x]类型 模拟量
    public final static int INPUT_REGISTERS = 04;

    private MonitoredDataItem item;

    private int publishRate ;
    public MonitoredDataItemTask(MonitoredDataItem item,int publishRate) {
        this.item = item;
        this.publishRate = publishRate;
    }

    @Override
    public void run() {
        sendRequest();
    }

    private void sendRequest() {
        SubscribeSlaveNode subscribeSlaveNode = item.getSubscribeSlaveNode();
        MonitoredDataItemListener listener = item.getListener();
        ModbusMaster master = item.getMaster();
        int functionId = subscribeSlaveNode.getFunctionId();
        int slaveId = subscribeSlaveNode.getSlaveId();
        int offset = subscribeSlaveNode.getOffset();
        int numberOfBits = subscribeSlaveNode.getNumberOfBits();
        String nodeName = subscribeSlaveNode.getNodeName();
        switch (functionId){
            case COIL_STATUS:
                sendCoilStatusRequest(master,nodeName,slaveId,offset,numberOfBits,listener);
                break;
            case INPUT_STATUS:
                sendInputStatusRequest(master,nodeName,slaveId,offset,numberOfBits,listener);
                break;
            case HOLDING_REGISTER:
                sendHoldingRegisterRequest(master,nodeName,slaveId,offset,numberOfBits,listener);
                break;
            case INPUT_REGISTERS:
                sendInputRegistersRequest(master,nodeName,slaveId,offset,numberOfBits,listener);
                break;
            default:
        }
    }

    private void sendCoilStatusRequest(ModbusMaster master,String nodeName,int slaveId,int offset,int numberOfBits,MonitoredDataItemListener listener){
        boolean[] result;
        try {
            result = ReadUtil.readInputStatus(master,slaveId,offset,numberOfBits);
            if (MonitoredDataCompare.isNewValue(nodeName,result,result.getClass())){
                listener.onDataChange(item, result);
            }
        }catch (ModbusTransportException e){
            log.error("readInputStatus 发生异常：{}",e.getMessage());
        }
    }

    private void sendInputStatusRequest(ModbusMaster master,String nodeName,int slaveId,int offset,int numberOfBits,MonitoredDataItemListener listener){
        boolean[] result;
        try {
            result = ReadUtil.readCoilStatus(master,slaveId,offset,numberOfBits);
            if (MonitoredDataCompare.isNewValue(nodeName,result,result.getClass())){
                listener.onDataChange(item, result);
            }
        }catch (ModbusTransportException e){
            log.error("sendCoilStatusRequest 发生异常：{}",e.getMessage());
        }
    }

    private void sendHoldingRegisterRequest(ModbusMaster master,String nodeName,int slaveId,int offset,int numberOfBits,MonitoredDataItemListener listener){
        short[] result;
        try {
            result = ReadUtil.readHoldingRegister(master,slaveId,offset,numberOfBits);
            if (MonitoredDataCompare.isNewValue(nodeName,result,result.getClass())){
                listener.onDataChange(item, result);
            }
        }catch (ModbusTransportException e){
            log.error("readHoldingRegister 发生异常：{}",e.getMessage());
        }
    }

    private void sendInputRegistersRequest(ModbusMaster master,String nodeName,int slaveId,int offset,int numberOfBits,MonitoredDataItemListener listener){
        short[] result;
        try {
            result = ReadUtil.readInputRegisters(master,slaveId,offset,numberOfBits);
            if (MonitoredDataCompare.isNewValue(nodeName,result,result.getClass())){
                listener.onDataChange(item, result);
            }
        }catch (ModbusTransportException e){
            log.error("readInputRegisters 发生异常：{}",e.getMessage());
        }
    }
}
