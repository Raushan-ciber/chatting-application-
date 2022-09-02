import java.net.*;
import javax.swing.*;


import java.awt.BorderLayout;


import java.io.*;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class Client extends JFrame {
    Socket socket;
    BufferedReader br;
    PrintWriter out;

    //Declare components
    private JLabel heading = new JLabel("watsapp");
    private JTextArea messageArea = new JTextArea();
    private JTextField massageInput = new JTextField();
    private Font font = new Font("Roboto",11,20);

    public Client(){
        try{
            System.out.println("sending request to server");
            socket = new Socket("127.0.0.1",7778);
            System.out.println("connection done");

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());
            createGUI();
            handleEvents();

            startReading();
            //startWriting();
        }
        catch(Exception e){
            
        }
    }


    
   
    /**
     * 
     */
    private void handleEvents() {
       massageInput.addKeyListener(new KeyListener(){

        @Override
        public void keyTyped(KeyEvent e) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void keyPressed(KeyEvent e) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void keyReleased(KeyEvent e) {
            // TODO Auto-generated method stub
            if(e.getKeyCode()==10){
                //System.out.println("you pressed enter");
                String contentTosend = massageInput.getText();
                messageArea.append("Me : "+contentTosend+"\n");
                out.println(contentTosend);
                out.flush();
                massageInput.setText("");
                massageInput.requestFocus();
            }
            
        }
        
       });
    }


    private void createGUI(){

         //gui code
        this.setTitle("client Massager[END]");
        this.setSize(600, 700);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        //component code
        heading.setFont(font);
        messageArea.setFont(font);
        massageInput.setFont(font);

        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        heading.setIcon(new ImageIcon("lo.png"));
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        messageArea.setEditable(false);
        massageInput.setHorizontalAlignment(SwingConstants.CENTER);

        //frame layout
        this.setLayout(new BorderLayout());
        //adding component to frame
        this.add(heading,BorderLayout.NORTH);
        JScrollPane jScrollPane = new JScrollPane(messageArea);
        this.add(jScrollPane, BorderLayout.CENTER);
        this.add(massageInput, BorderLayout.SOUTH);

    }
    public void startReading(){
        //thread - read karke data rahega
        Runnable r1 = ()->{
            System.out.println("readerstarted..");
            try{
            while(!socket.isClosed()){
                

                String msg = br.readLine();
                if(msg.equals("exit")){
                    System.out.println("server terminated the chat");
                    JOptionPane.showMessageDialog(this, "Server Terminated the chat");
                    massageInput.setEnabled(false);
                    socket.close();
                    break;
                }
               // System.out.println("Client: "+msg);
               messageArea.append("Server : "+ msg+"\n");
            }
          
        }
            catch(Exception e){
                //e.printStackTrace();
               System.out.println("connection close");
            }
            
        };
        new Thread(r1).start();


    }

    public void startWriting(){
     
        //thread - data user lega and then send karega client ko
        Runnable r2 = ()->{
            System.out.println("writer started..");
            try{ 
        while(!socket.isClosed()){
               
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();
                    out.println(content);
                    out.flush();
                    if(content.equals("exit")){
                        socket.close();
                        break;
                    }


                }
            }
                catch(Exception e){
                   // e.printStackTrace();
                   System.out.println("connection is closed");
                }
           
        };
        new Thread(r2).start();
    }
    public static void main(String[] args) {
        System.out.println("this is client");
        new Client();
    }
}
