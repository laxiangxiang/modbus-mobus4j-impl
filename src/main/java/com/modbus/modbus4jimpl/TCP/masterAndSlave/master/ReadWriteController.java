package com.modbus.modbus4jimpl.TCP.masterAndSlave.master;

import com.serotonin.modbus4j.code.DataType;
import com.serotonin.modbus4j.exception.ErrorResponseException;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReadWriteController {

    @Autowired
    private Modbus4jReadUtils readUtils;

    @Autowired
    private Modbus4jWriteUtils writeUtils;

    //调试可用
    @RequestMapping("/read")
    public void read() throws ModbusTransportException, ModbusInitException, ErrorResponseException {
        //读取自己实现的slave使用
//        boolean[] booleans = readUtils.readCoilStatus(1,0,1);
//        for (boolean aBoolean : booleans) {
//            System.out.println(aBoolean);
//        }
//        short[] shorts = readUtils.readHoldingRegister(1,0,10);
//        for (short aShort : shorts) {
//            System.out.println(aShort);
//        }
        //读取slave模拟软件使用
        boolean b = com.modbus.client.modbus4jmaster.modbus4j.TCP.Mdobus4jReadUtils.readCoilStatus(1,1);
        System.out.println(b);
        Number number = com.modbus.client.modbus4jmaster.modbus4j.TCP.Mdobus4jReadUtils.readHoldingRegister(1,0,DataType.TWO_BYTE_INT_SIGNED);
        System.out.println(number.intValue());
    }

    //调试可用
    @RequestMapping("write")
    public void write() throws ModbusTransportException, ErrorResponseException, ModbusInitException {
        //写自己实现的slave使用
//        writeUtils.writeCoil(1,0,false);
//        writeUtils.writeHoldingRegister(1,0,-2, DataType.TWO_BYTE_INT_SIGNED);
        //写slave模拟软件使用
        com.modbus.client.modbus4jmaster.modbus4j.TCP.Modbus4jWriteUtils.writeCoils(1,0, new boolean[]{false, true,true});
        com.modbus.client.modbus4jmaster.modbus4j.TCP.Modbus4jWriteUtils.writeRegisters(1,0,new short[]{9,8,7,6,5,4,3,2,1});
    }
}
