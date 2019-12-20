package com.modbus.modbus4jimpl.TCP.masterAndSlave.slave;

import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusSlaveSet;
import com.serotonin.modbus4j.exception.ModbusInitException;
import org.springframework.boot.CommandLineRunner;

//springboot项目实现CommandLineRunner类和@Component注解，程序启动时就加载tcp slave

/**
 * todo:用 modbus poll软件连接会报错，连接不上，使用 master Java程序可以读写此slave
 */
//@Component
public class Slave implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        createSlave();
    }

    private void createSlave() {
        //创建modbus工厂
        ModbusFactory modbusFactory = new ModbusFactory();
        //创建TCP服务端
        final ModbusSlaveSet salve = modbusFactory.createTcpSlave(true);
        //向过程影像区添加数据
        salve.addProcessImage(Register.getModscanProcessImage(1));
        //获取寄存器
        salve.getProcessImage(1);
        try {
            salve.start();
        } catch (ModbusInitException e) {
            e.printStackTrace();
        }
    }
}
