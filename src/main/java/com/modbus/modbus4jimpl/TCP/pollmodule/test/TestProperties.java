package com.modbus.modbus4jimpl.TCP.pollmodule.test;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class TestProperties {

    private List<TcpMaster> tcpMasters;

    @Data
    public static class TcpMaster {
        private String host;
        private int port;
        private List<Map<String,String>> subscribeList;

        public TcpMaster() {
        }
    }
}
