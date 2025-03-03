package com.whilter.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;

public final class NetworkUtil {

    public static Collection<String> getAllInterfacesIp() throws SocketException {
        Collection<String> ipList = new ArrayList<>();
        Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
        while (e.hasMoreElements()) {
            NetworkInterface n = e.nextElement();
            Enumeration<InetAddress> ee = n.getInetAddresses();
            while (ee.hasMoreElements()) {
                InetAddress i = ee.nextElement();
                ipList.add(i.getHostAddress());
            }
        }
        return ipList;
    }

    public static String getIpForHost(String hostname) throws UnknownHostException {
        InetAddress address = InetAddress.getByName(hostname);
        return address.getHostAddress();
    }

    public static boolean isThisSever(String host) throws UnknownHostException, SocketException {
        if (host.equals("0.0.0.0")) return true;
        String ip = NetworkUtil.getIpForHost(host);
        return NetworkUtil.getAllInterfacesIp().contains(ip);
    }

    public static String getHostName() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostName();
    }
}
