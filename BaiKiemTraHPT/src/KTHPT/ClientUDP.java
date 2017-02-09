
package KTHPT;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;


public class ClientUDP {        
    private final int port; 
    private Scanner reader;    
    private DatagramSocket clientSocket = null;
    private InetAddress server = null;
    private DatagramPacket inputPacket,outputPacket;
    
    public ClientUDP(String server,int port) throws IOException {
        this.server = InetAddress.getByName(server);
        this.port = port;
    }
    
    private void run() throws IOException{              
        mainMenu();
    }

    private void mainMenu() throws IOException {
        int choosedValue;
        byte[] inputData = new byte[65536];
        inputPacket = new DatagramPacket(inputData, inputData.length);
        byte[] sendData = new byte[65536];
        
        do {            
            clientSocket = new DatagramSocket();
            reader = new Scanner(System.in);
            System.out.println("[1] - Nhap du lieu tu ban phim: ");
            System.out.println("[2] - Nhap du lieu tu file data.txt ");
            System.out.println("[3] - Ket thuc");
            System.out.print("Lua chon: ");
            choosedValue = reader.nextInt();
            reader.nextLine();
                        
            String MSSV = "";
            if (choosedValue == 2) {
               reader = new Scanner(new BufferedReader(new FileReader("sinhvien.txt")));               
            }
            
            while (choosedValue !=3) {
                if (choosedValue == 1) {
                    System.out.print("Nhap vao MSSV(Nhap 'ket thuc' de thoat): ");
                    MSSV = reader.nextLine();
                    
                    sendData = MSSV.getBytes();
                    outputPacket = new DatagramPacket(sendData, sendData.length,server,port);
                    
                    
                    clientSocket.send(outputPacket);                    
                   
                    if (!MSSV.equalsIgnoreCase("ket thuc")) {                        
                        
                        clientSocket.receive(inputPacket);
                        
                                              
                        String returnedValue = new String(inputPacket.getData());
                       
                        System.out.println("Ket qua: " + returnedValue.trim());
                    }else{
                        break;  
                    }
                }
                
                if (choosedValue ==2) {
                    while (reader.hasNext()) {
                        
                        MSSV = reader.nextLine();
                        sendData = MSSV.getBytes();
                        outputPacket = new DatagramPacket(sendData, sendData.length,server,port);
                    
                       
                        clientSocket.send(outputPacket);
                            
                        if (!MSSV.equalsIgnoreCase("ket thuc")) {
                           
                            clientSocket.receive(inputPacket);

                            
                            String returnedValue = new String(inputPacket.getData());
                          
                            System.out.println("Ket qua: " + returnedValue.trim());
                        }
                    }
                    
                    if (MSSV.equalsIgnoreCase("ket thuc")) {
                        
                        break; 
                    }
                }                
            }
            
        } while (choosedValue !=3);
    }
    
    public static void main(String[] args) throws IOException {
        ClientUDP client = new ClientUDP("localhost", 9090);
        client.run();
    }
        
}
