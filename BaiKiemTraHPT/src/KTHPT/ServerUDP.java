
package KTHPT;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;


public class ServerUDP {
    private DatagramSocket serverSocket = null;
    private final int port;
    private DatagramPacket inputPacket,outputPacket;
    private Scanner reader; 
    
    public ServerUDP(int port) {        
        this.port = port;
    }
    
    private void run() throws Exception{
        
        serverSocket = new DatagramSocket(port);
        System.out.println("Server dang mo tai port: " +
                    serverSocket.getLocalPort() + " ...");
        byte[] inputData = new byte[65536];
        inputPacket = new DatagramPacket(inputData, inputData.length);
        byte[] outputData = new byte[65536];
        while (true) {
            serverSocket.receive(inputPacket);
            String MSSV = new String(inputPacket.getData(),0,inputPacket.getLength());
            System.out.println("MSSV nhan duoc: "+MSSV);
            if (!MSSV.trim().equalsIgnoreCase("ket thuc")) {
                outputData = xulyInputData(MSSV);
                InetAddress clientIP = inputPacket.getAddress();
                int clientPort = inputPacket.getPort();
                outputPacket = new DatagramPacket(outputData, outputData.length,clientIP,clientPort);
                serverSocket.send(outputPacket);
            }            
        }
    }
    
    private byte[] xulyInputData(String inputData) throws FileNotFoundException{
        String ketqua = checkKetQua(Integer.parseInt(inputData));
        return ketqua.getBytes();
    }
    
    private String checkKetQua(int mssv) throws FileNotFoundException{
        reader = new Scanner(new BufferedReader(new FileReader("database.txt")));
        String SV="";
        String kq="";
        int testMSV=0;
        while(reader.hasNext())
        {
            SV=reader.nextLine();
            String[] sV= new String[4];
            sV=SV.split("\\s");
            if(Integer.parseInt(sV[0])==mssv)
            {
                testMSV=1;
                if(4<=Integer.parseInt(sV[2])) kq="Sinh vien: "+sV[1]+"\n Diem lan 1: "+sV[2]+"\n Ket qua: Qua mon";
                else if(4<=Integer.parseInt(sV[3])) kq="Sinh vien: "+sV[1]+"\n Diem lan 1: "+sV[2]+"\n Diem lan 2: "+sV[3]+"\n Ket qua: Qua mon";
                else kq="Sinh vien: "+sV[1]+"\n Diem lan 1: "+sV[2]+"\n Diem lan 2: "+sV[3]+"\n Ket qua: Khong qua mon";
            }
        }
        if(testMSV==1) return kq;
        else return "Khong tim thay sinh vien co MSSV: "+mssv;
    }
    public static void main(String[] args) throws Exception {
        int port = 9090;
        ServerUDP server = new ServerUDP(port);
        server.run();
    }
}
