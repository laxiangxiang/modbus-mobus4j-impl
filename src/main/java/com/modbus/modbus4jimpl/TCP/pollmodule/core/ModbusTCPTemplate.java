package com.modbus.modbus4jimpl.TCP.pollmodule.core;

import com.modbus.modbus4jimpl.TCP.pollmodule.util.ReadUtil;
import com.modbus.modbus4jimpl.TCP.pollmodule.util.WriteUtil;
import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.exception.ErrorResponseException;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.ip.IpParameters;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 连接slave ，关闭连接 ，读取 ，写入 操作模板
 * @author shdq-fjy
 */
@Slf4j
public class ModbusTCPTemplate {
    private boolean isListenering = false;
    private List<ModbusMaster> masters;
    private List<MonitoredDataItem> items;
    private static ScheduledExecutorService executor;
    private final static AtomicInteger threadNo = new AtomicInteger(1);
    public ModbusTCPTemplate(List<ModbusMaster> masters, List<MonitoredDataItem> items) {
        this.masters = masters;
        this.items = items;
    }

    /**
     * 连接slave 执行轮询
     */
    public void start() {
        if (!isListenering){
            createExecutor();
            items.stream().map(item -> {
                int publishRate = item.getSubscribeSlaveNode().getPublishRate();
                MonitoredDataItemTask task = new MonitoredDataItemTask(item,publishRate);
                return task;
            }).forEach(task ->
                    executor.scheduleAtFixedRate(task,
                            task.getPublishRate(), task.getPublishRate(), TimeUnit.MILLISECONDS)
            );
            isListenering = true;
        }
    }

    /**
     * 资源销毁
     */
    public void destroy(){
        if (isListenering){
            log.info("execution destruction");
            executor.shutdownNow();
            for (ModbusMaster master : masters) {
                master.destroy();
            }
            isListenering = false;
        }
    }

    /**
     * 读开关量 功能码01 02
     * 01 可读可写
     * 02 只读
     * @param host
     * @param port
     * @param functionId
     * @param slaveId
     * @param offset
     * @param numberOfBits
     * @return
     * @throws ModbusInitException
     * @throws ModbusTransportException
     */
    public boolean[] readSwitch(String host,int port,int functionId,int slaveId, int offset, int numberOfBits) throws ModbusInitException, ModbusTransportException {
        ModbusMaster master = createMaster(host,port);
        boolean[] booleans = null;
        switch (functionId){
            case MonitoredDataItemTask.COIL_STATUS:
                booleans = ReadUtil.readCoilStatus(master,slaveId,offset,numberOfBits);
                break;
            case MonitoredDataItemTask.INPUT_STATUS:
                booleans = ReadUtil.readInputStatus(master,slaveId,offset,numberOfBits);
                break;
            default:
        }
        master.destroy();
        return booleans;
    }

    /**
     * 读模拟量 功能码03 04
     * 03 可读可写
     * 04 只读
     * @param host
     * @param port
     * @param functionId
     * @param slaveId
     * @param offset
     * @param numberOfBits
     * @return
     * @throws ModbusTransportException
     * @throws ModbusInitException
     */
    public short[] readAnalog(String host,int port,int functionId,int slaveId, int offset, int numberOfBits) throws ModbusTransportException, ModbusInitException {
        ModbusMaster master = createMaster(host,port);
        short[] shorts = null;
        switch (functionId){
            case MonitoredDataItemTask.HOLDING_REGISTER:
                shorts = ReadUtil.readHoldingRegister(master,slaveId,offset,numberOfBits);
                break;
            case MonitoredDataItemTask.INPUT_REGISTERS:
                shorts = ReadUtil.readInputRegisters(master,slaveId,offset,numberOfBits);
                break;
            default:
        }
        master.destroy();
        return shorts;
    }


    /**
     * 写开关量 针对 01
     * @param host
     * @param port
     * @param slaveId
     * @param offset
     * @param data
     * @return
     * @throws ModbusInitException
     * @throws ModbusTransportException
     */
    public boolean writeSwitch(String host,int port,int slaveId, int offset, boolean[] data) throws ModbusInitException, ModbusTransportException {
        ModbusMaster master = null;
        boolean result = false;
        if (data != null){
             master = createMaster(host,port);
            if (data.length == 1){
                result = WriteUtil.writeCoil(master,slaveId,offset,data[0]);
            }else {
                result = WriteUtil.writeCoils(master,slaveId,offset,data);
            }
        }
        if (master != null){
            master.destroy();
        }
        return result;
    }

    /**
     * 写模拟量 针对 03
     * @param host
     * @param port
     * @param slaveId
     * @param offset
     * @param data
     * @return
     * @throws ModbusInitException
     * @throws ModbusTransportException
     */
    public boolean writeAnalog(String host,int port,int slaveId, int offset, short[] data) throws ModbusInitException, ModbusTransportException {
        ModbusMaster master = null;
        boolean result = false;
        if (data != null){
             master = createMaster(host,port);
            if (data.length == 1){
                result = WriteUtil.writeRegister(master,slaveId,offset,data[0]);
            }else {
                result = WriteUtil.writeRegisters(master,slaveId,offset,data);
            }
        }
        if (master != null){
            master.destroy();
        }
        return result;
    }

    /**
     * 根据类型写数据（如:写入Float类型的模拟量、Double类型模拟量、整数类型Short、Integer、Long）
     * slave端一个地址占16位，float占2个地址，double占4个地址，short占1个地址，int占2个地址，long占4个地址
     * @param host
     * @param port
     * @param slaveId
     * @param offset
     * @param value
     * @param dataType com.serotonin.modbus4j.code.DataType
     * @throws ModbusInitException
     * @throws ErrorResponseException
     * @throws ModbusTransportException
     */
    public void writeHoldingRegister(String host,int port,int slaveId, int offset, Number value,int dataType) throws ModbusInitException, ErrorResponseException, ModbusTransportException {
        ModbusMaster master = createMaster(host,port);
        WriteUtil.writeHoldingRegister(master,slaveId,offset,value,dataType);
    }

    private ModbusMaster createMaster(String host,int port) throws ModbusInitException {
        ModbusFactory factory = new ModbusFactory();
        IpParameters parameters = new IpParameters();
        parameters.setHost(host);
        parameters.setPort(port);
        parameters.setEncapsulated(false);
        ModbusMaster master = factory.createTcpMaster(parameters,false);
        master.setTimeout(1000);
        master.setRetries(3);
        masters.add(master);
        master.init();
        return master;
    }

    private void createExecutor(){
        int maxThreadNum = Runtime.getRuntime().availableProcessors();
        int needThreadNum = items.size();
        if (maxThreadNum >= needThreadNum){
            maxThreadNum = needThreadNum;
        }
        executor = new ScheduledThreadPoolExecutor(maxThreadNum,
                new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable r) {
                        //创建一个线程，定义名称为"order-thread"
                        Thread th = new Thread(r,"modbus-"+ threadNo.getAndIncrement());
                        //判断如果线程优先级被修改，那么改变优先级状态
                        if(th.getPriority() != Thread.NORM_PRIORITY) {
                            th.setPriority(Thread.NORM_PRIORITY);
                        }
                        th.setDaemon(true);
                        return th;
                    }
                });
    }

    public void batchRead(String host, int port) throws ModbusInitException, ErrorResponseException, ModbusTransportException {
        ModbusMaster master = createMaster(host,port);
        ReadUtil.batchRead(master);
        master.destroy();
    }
}
