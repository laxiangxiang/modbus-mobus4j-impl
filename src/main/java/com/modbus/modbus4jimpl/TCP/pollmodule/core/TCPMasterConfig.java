package com.modbus.modbus4jimpl.TCP.pollmodule.core;

import com.modbus.modbus4jimpl.TCP.pollmodule.util.YamlConverter;
import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.ip.IpParameters;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 采用ScheduledExecutorService线程池来指定时间间隔轮询slave数据地址达到监听效果
 * 测试结果不是太理想，会有漏读现象，减小定时任务时间间隔，效果甚微。
 * 在slave 模拟软件端数据自动增长情况下 publishRate 为1000ms 最佳
 * 如果slave端为固定时间数据刷新，则最好配置为slave端的刷新时间
 * 否则看具体情况配置。
 */
@Slf4j
//@Configuration
public class TCPMasterConfig {

    private static List<MonitoredDataItem> items = new ArrayList<>();

    private static List<ModbusMaster> masters = new ArrayList<>();

    @Autowired
    ModbusTCPTemplate template;

    @PostConstruct
    public void init(){
        getProperties();
        template.start();
    }

    @PreDestroy
    public void destroy(){
        template.destroy();
    }

    @Bean
    public ModbusTCPTemplate createModbusTCPTemplate() {
        this.template = new ModbusTCPTemplate(masters,items);
        return this.template;
    }

    public void getProperties() {
          ModbusProperties properties =  YamlConverter.getInstance().readAndConvert("modbus.yml",ModbusProperties.class);
          resolve(properties);
    }

    /**
     * 获取maste 客户端对象
     * @return
     * @throws ModbusInitException
     */
    private ModbusMaster createMaster(ModbusProperties.TcpMaster tcpMaster) throws ModbusInitException {
            ModbusFactory factory = new ModbusFactory();
            IpParameters parameters = new IpParameters();
            parameters.setHost(tcpMaster.getHost());
            parameters.setPort(tcpMaster.getPort());
            parameters.setEncapsulated(tcpMaster.isEncapsulated());
            ModbusMaster master = factory.createTcpMaster(parameters,tcpMaster.isKeepAlive());
            master.setTimeout(tcpMaster.getTimeout());
            master.setRetries(tcpMaster.getRetries());
            masters.add(master);
            master.init();
            return master;
    }


    private void resolve(ModbusProperties properties){
        List<ModbusProperties.TcpMaster> tcpMasters = properties.getTcpMasters();
        tcpMasters.stream().forEach(tcpMaster -> {
            List<Map<String,String>> subscribeList = tcpMaster.getSubscribeList();
            try {
                ModbusMaster master = createMaster(tcpMaster);
                subscribeList.stream().forEach(stringStringMap -> {
                    String nodeName = stringStringMap.get("nodeName");
                    int slaveId = Integer.valueOf(stringStringMap.get("slaveId"));
                    int offset = Integer.valueOf(stringStringMap.get("offset"));
                    int functionId = Integer.valueOf(stringStringMap.get("functionId"));
                    int numberOfBits = Integer.valueOf(stringStringMap.get("numberOfBits"));
                    int publishRate = Integer.valueOf(stringStringMap.get("publishRate"));
                    String listenerPath = stringStringMap.get("listenerPath");
                    String listenerName = stringStringMap.get("listenerName");
                    SubscribeSlaveNode subscribeSlaveNode = new SubscribeSlaveNode(nodeName,slaveId,offset,functionId,numberOfBits,publishRate);
                    MonitoredDataItemListener listener = createListener(listenerPath,listenerName);
                    MonitoredDataItem item = MonitoredDataItem
                            .builder()
                            .addNode(subscribeSlaveNode)
                            .bindListener(listener)
                            .setMaster(master)
                            .build();
                    items.add(item);
                });
            } catch (ModbusInitException e) {
                e.printStackTrace();
            }
        });
    }

    private MonitoredDataItemListener createListener(String path,String name){
        MonitoredDataItemListener listener;
        String className = name.substring(0,1).toUpperCase()+name.substring(1);
        try {
            Class clazz = Class.forName(path+className);
            listener = (MonitoredDataItemListener) clazz.newInstance();
            log.info("监听器：{} 添加成功",className);
            return listener;
        }catch (ClassNotFoundException | IllegalAccessException | InstantiationException e){
            log.error("实例化监听器失败");
            return null;
        }
    }
}
