import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;

public class ChatClient extends Frame implements Runnable, ActionListener {
    TextField textfield;
    TextArea textarea;
    Button sendButton;
    Socket socket;
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;
    Thread chat;

    // Constructor for the client application
    ChatClient(String serverAddress, int port) {
        try {
            socket = new Socket(serverAddress, port);
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Initialize GUI components
        textfield = new TextField();
        textarea = new TextArea();
        sendButton = new Button("SEND");
        sendButton.addActionListener(this);
        setLayout(new FlowLayout());
        add(textfield);
        add(textarea);
        add(sendButton);
        setSize(500, 500);
        setTitle("Chat Client");
        setVisible(true);

        // Start the thread for receiving messages
        chat = new Thread(this);
        chat.start();
    }

    // Action performed when button is pressed
    public void actionPerformed(ActionEvent e) {
        String msg = textfield.getText();
        textarea.append("You: " + msg + "\n");
        textfield.setText("");

        try {
            dataOutputStream.writeUTF(msg);
            dataOutputStream.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // Main method to start the client
    public static void main(String args[]) {
        String serverAddress = "localhost"; // Change this to your server's address if needed
        int port = 12345; // Must match the server port

        new ChatClient(serverAddress, port);
    }

    // Run method to handle incoming messages
    public void run() {
        while (true) {
            try {
                String msg = dataInputStream.readUTF();
                textarea.append("Other: " + msg + "\n");
            } catch (IOException e) {
                e.printStackTrace();
                break; // Exit the loop if an exception occurs
            }
        }
    }
}
