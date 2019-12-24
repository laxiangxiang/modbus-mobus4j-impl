package com.modbus.modbus4jimpl.TCP.masterAndSlave.master;

import com.modbus.modbus4jimpl.TCP.masterAndSlave.master.request.Modbus4jReadUtils;
import com.modbus.modbus4jimpl.TCP.masterAndSlave.master.request.Modbus4jWriteUtils;
import com.serotonin.modbus4j.code.DataType;
import com.serotonin.modbus4j.exception.ErrorResponseException;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 不管是使用自己实现的slave 还是使用 slave模拟软件，下面两个包 locator 和 request 下的读写工具类都可用。
 * 注意在使用slave模拟软件时，获取的master要设置属性 Encapsulated 为 false，否则会报socket错误。
 * 在使用自己的编写的salve时，获取master要设置属性 Encapsulated 为true，否则会报 ShouldNeverHappenException: wha 异常
 */
@RestController
public class ReadWriteController {

    //调试可用
    @RequestMapping("/read")
    public void read() throws ModbusTransportException, ErrorResponseException {
        boolean b = com.modbus.modbus4jimpl.TCP.masterAndSlave.master.locator.Modbus4jReadUtils.readCoilStatus(1,0);
        System.out.println(b);
        Number number = com.modbus.modbus4jimpl.TCP.masterAndSlave.master.locator.Modbus4jReadUtils.readHoldingRegister(1,0,10);
        System.out.println(number);

//        boolean[] booleans = Modbus4jReadUtils.readCoilStatus(1,0,1);
//        System.out.println(booleans[0]);
//        short[] shorts = Modbus4jReadUtils.readHoldingRegister(1,0,DataType.TWO_BYTE_INT_SIGNED);
//        System.out.println(shorts[0]);
    }

    //调试可用
    @RequestMapping("write")
    public void write() throws ModbusTransportException, ErrorResponseException, ModbusInitException {

        com.modbus.modbus4jimpl.TCP.masterAndSlave.master.locator.Modbus4jWriteUtils.writeCoil(1,0,false);
        com.modbus.modbus4jimpl.TCP.masterAndSlave.master.locator.Modbus4jWriteUtils.writeHoldingRegister(1,0,-2, DataType.TWO_BYTE_INT_SIGNED);

//        Modbus4jWriteUtils.writeCoils(1,0, new boolean[]{false, true,true});
//        Modbus4jWriteUtils.writeRegisters(1,0,new short[]{9,8,7,6,5,4,3,2,1});
    }
}
