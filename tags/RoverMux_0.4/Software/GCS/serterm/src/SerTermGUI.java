/*
 * SerialGUI.java
 *
 * Created on November 5, 2007, 3:45 PM
 *
 * @author  Goldscott
 * @author Michael Shimniok http://www.bot-thoughts.com/
 * 
 * Modified UI and equipped with a simple automatic file download protocol
 * 
 * Icon 
 * Author   : mattahan
 * HomePage : http://mattahan.deviantart.com
 * License  : Free for personal non-commercial use, Includes a link back to author site. Zombie.jpg
 */

//import java.io.FileDescriptor;
import gnu.io.*;
import java.awt.Color;
import java.io.*;
import java.util.Enumeration;
import java.util.TooManyListenersException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.text.*;
//import javax.swing.JOptionPane;

public class SerTermGUI extends javax.swing.JFrame implements SerialPortEventListener{
    
    /** Creates new form SerialGUI */
    public SerTermGUI() {
        initComponents();
        getPorts();
        displayFormat = RxFormat.ASCII;
        setBaud();
        StyleConstants.setForeground(alertStyle, Color.RED);
        StyleConstants.setBold(alertStyle, true);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jOptionPane1 = new javax.swing.JOptionPane();
        rxformat = new javax.swing.ButtonGroup();
        textbar = new javax.swing.JTextField();
        portBox = new javax.swing.JComboBox();
        portToggle = new javax.swing.JToggleButton();
        clearButton = new javax.swing.JButton();
        SendButton = new javax.swing.JButton();
        baudBox = new javax.swing.JComboBox();
        jScrollPane2 = new javax.swing.JScrollPane();
        text = new javax.swing.JTextArea();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Serial Terminal with file download");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        textbar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textbarActionPerformed(evt);
            }
        });

        portBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Select Port" }));
        portBox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
                portBoxPopupMenuWillBecomeVisible(evt);
            }
        });
        portBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                portBoxActionPerformed(evt);
            }
        });

        portToggle.setText("Open Port");
        portToggle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                portToggleActionPerformed(evt);
            }
        });

        clearButton.setText("Clear Terminal");
        clearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearButtonActionPerformed(evt);
            }
        });

        SendButton.setText("Send");
        SendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SendButtonActionPerformed(evt);
            }
        });

        baudBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "115200", "57600", "38400", "19200", "14400", "9600", "4800", "2400", "1200" }));
        baudBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                baudBoxActionPerformed(evt);
            }
        });

        text.setColumns(20);
        text.setEditable(false);
        text.setFont(new java.awt.Font("Courier New", 0, 13)); // NOI18N
        text.setRows(5);
        text.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                textKeyTyped(evt);
            }
        });
        jScrollPane2.setViewportView(text);

        jButton1.setText("Set Dowload Path...");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane2)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(textbar)
                            .add(layout.createSequentialGroup()
                                .add(clearButton)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 109, Short.MAX_VALUE)
                                .add(jButton1)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                .add(baudBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 103, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                .add(portBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 114, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, portToggle, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, SendButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 351, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(textbar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(SendButton))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(portBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(portToggle)
                    .add(clearButton)
                    .add(baudBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jButton1))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void SendButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SendButtonActionPerformed
        //Send button is the same action as hitting Enter on the textbar
        textbarActionPerformed(evt);
    }//GEN-LAST:event_SendButtonActionPerformed

    private void clearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearButtonActionPerformed
        //remove all text from the window
        text.setText("");
        text.setCaretPosition(0);
        text.getCaret().setVisible(true); // cursor
        text.requestFocus();
}//GEN-LAST:event_clearButtonActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        //when user closes, make sure to close open ports and open I/O streams
        if (portIdentifier.isCurrentlyOwned()) { //if port open, close port
            portToggle.setText("Open Port");
            if (inputStream != null) //close input stream
                try {inputStream.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            if (outputStream != null) //close output stream
                try {outputStream.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            serialPort.removeEventListener();
            if (serialPort != null) serialPort.close();
            open=false;
            append(">> Port "+portName+" is now closed.\n", alertStyle );
        }
    }//GEN-LAST:event_formWindowClosing

    private void portToggleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_portToggleActionPerformed
        //only open valid port. portList[0]="select port" - not a valid port
        //if ((String)portBox.getSelectedItem() == portList[0]) {
        if (portBox.getSelectedIndex() == 0) {//.getSelectedItem().equals(portList[0])) {
            append(">> Must Select Valid Port.\n", alertStyle );
            portToggle.setSelected(open);
            //JOptionPane.showMessageDialog(this, "Must Select Valid Port.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        //if port open, close port & I/O streams
        else if (portIdentifier.isCurrentlyOwned()) { 
            portToggle.setText("Open Port");
            portBox.setEnabled(true);
            baudBox.setEnabled(true);
                //close input stream
            if (inputStream != null) {
                try { inputStream.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            //close output stream
            if (outputStream != null) {
                try { outputStream.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            //close serial port
            System.out.println("closing serial port.");
            serialPort.removeEventListener();
            if (serialPort != null) {
                serialPort.close();
            }
            System.out.println("closed serial port.");

            open=false;
            append(">> Port "+portName+" is now closed.\n", alertStyle );
        } else {//else port is closed, so open it
            portToggle.setText("Close Port");
            portBox.setEnabled(false);
            baudBox.setEnabled(false);
            append(">> Opening Port: " +portName+", Baud Rate: "+baudRate+"\n", alertStyle );
            try {
                connect(portName);
            }
            catch ( Exception e ) {
                e.printStackTrace();
            }
            try {
                serialPort.addEventListener(this);
            } catch (TooManyListenersException ex) {
                ex.printStackTrace();
            }
            serialPort.notifyOnDataAvailable(true);
            append(">> Port opened.\n", alertStyle );
            text.requestFocus();
        }
        System.out.println("end of toggle function");
    }//GEN-LAST:event_portToggleActionPerformed

    //open serial port
    void connect ( String portName ) throws Exception
    {
        //make sure port is not currently in use
        portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        if ( portIdentifier.isCurrentlyOwned() ) {
            System.out.println("Error: Port is currently in use");
        }
        else {
            //create CommPort and identify available serial/parallel ports
            commPort = portIdentifier.open(this.getClass().getName(),2000);
            serialPort = (SerialPort) commPort;//cast all to serial
            //set baudrate, 8N1 stopbits, no parity
            serialPort.setSerialPortParams(baudRate,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
            //start I/O streams
            inputStream = serialPort.getInputStream();
            outputStream = serialPort.getOutputStream();
            open=true;
            
            }
        }
    
    private void setBaud() {
        String newbaud = baudBox.getSelectedItem().toString();//get text from user
        //do simple check to make sure baudrate is valid
        baudRate=Integer.valueOf(newbaud).intValue();
    }
    
    private void portBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_portBoxActionPerformed
        portName = (String)portBox.getSelectedItem();
    }//GEN-LAST:event_portBoxActionPerformed

    private void textbarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textbarActionPerformed
        String text = textbar.getText();    //get text from field
        textbar.selectAll();                //highlight text so it can be easily overwritten
        //if serial port open, write to serial port
        if (open==true) {
            if (!text.equals("+++") && displayFormat == RxFormat.ASCII){
                text=text+"\r"; //append carriage return to text (except for +++ for XBee)
            }        
            try {
                outputStream.write(text.getBytes()); //write to serial port
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }//GEN-LAST:event_textbarActionPerformed

    private void baudBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_baudBoxActionPerformed
        //only change baud when port is closed
        setBaud();
    }//GEN-LAST:event_baudBoxActionPerformed

    private void textKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textKeyTyped
        char c = evt.getKeyChar();
        if (open == true) {
            try {
                if (c == '\n') c = '\r';
                outputStream.write(c); //write to serial port
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }//GEN-LAST:event_textKeyTyped

    private void portBoxPopupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_portBoxPopupMenuWillBecomeVisible
        getPorts(); // update the list of com ports dynamically
    }//GEN-LAST:event_portBoxPopupMenuWillBecomeVisible
    
    private void append(String str, SimpleAttributeSet style) {
        text.append(str);
        text.setCaretPosition(text.getText().length());
        text.getCaret().setVisible(true); // cursor
    }
    
    // Populates the combobox model with a list of availble ports
    // Updates combobox model dynamically. Call from popupWillBeVisible event
    private void getPorts() {
        Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();
        DefaultComboBoxModel model = (DefaultComboBoxModel) portBox.getModel();
        model.removeAllElements();
        model.addElement("Select Ports");
        while ( portEnum.hasMoreElements() ) {
            portIdentifier = (CommPortIdentifier) portEnum.nextElement();
            if (portIdentifier.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                model.addElement(portIdentifier.getName());
            }
        }
    }
    
    //serial event: when data is received from serial port
    //display the data on the terminal
    @Override
    public void serialEvent(SerialPortEvent event) {
        switch(event.getEventType()) {
            case SerialPortEvent.DATA_AVAILABLE:
                byte[] buffer = new byte[MAX_DATA];   //create a buffer (enlarge if buffer overflow occurs)
                byte[] buffer2 = new byte[MAX_DATA];
                int numBytes;   //how many bytes read (smaller than buffer)

                switch (displayFormat) {
                    case ASCII: {
                        try {   //read the input stream and store to buffer, count number of bytes read
                            while ((numBytes=inputStream.read(buffer)) > 0) {

                                // File Transfer is initiated with ^A^Bfilename.ext^C^B
                                // with the file contents sent byte by byte until ^D
                                int j = 0;
                                for (int i=0; i < numBytes; i++) {
                                    char c = (char) (buffer[i]&0x00FF);
                                    if (c == '\b') {                        // backspace is a special situation
                                        // for some reason this is removing one character from the model
                                        // but removing two characters from the display.
                                        Document myDoc = text.getDocument();
                                        try {
                                            myDoc.remove(myDoc.getLength()-1, 1);
                                        } catch (BadLocationException ex) {
                                            Logger.getLogger(SerTermGUI.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                        text.setCaretPosition(text.getText().length()-1);
                                    } else if (protoState == ParseState.IDLE && c == 1) { // Control-A "Start of Heading"
                                        protoState = ParseState.ARMED;
                                        System.out.println("Received CTRL_A");
                                    } else if (protoState == ParseState.ARMED && c == 2) { // Control-B "Start of Text"
                                        protoState = ParseState.FILENAME;
                                        filename = new StringBuilder(downloadPath);
                                        System.out.println("Received CTRL_B");
                                    } else if (protoState == ParseState.FILENAME) {
                                        if (c == 3) { // Control-C "End of Text"
                                            protoState = ParseState.STARTED;
                                            System.out.println("Received CTRL_C " + filename);
                                            // open file
                                            try{
                                                // test if filename is bad?
                                                fstream = new FileWriter(filename.toString());
                                                out = new BufferedWriter(fstream);
                                                append(">> Receiving "+filename.toString()+"\n", alertStyle);
                                            } catch (Exception e){//Catch exception if any
                                                System.err.println("Error: " + e.getMessage());
                                            }
                                        } else {
                                            filename.append(c); // add the actual filename that we're being given
                                            // stop after length limit?
                                        }
                                    } else if (protoState == ParseState.STARTED) {
                                        if (c == 4) { // Control-D "End of Transmission"
                                            append(">> Transfer complete.\n", alertStyle);
                                            // close the file
                                            out.close();
                                            protoState = ParseState.IDLE;
//                                            str = str.substring(str.lastIndexOf(4)+1); // trim off file contents to ^D
                                        } else {
                                            // write the character to the file
                                            out.write(c);
                                        }
                                    } else {
                                        // need an abort timer or something
                                        protoState = ParseState.IDLE;
                                        buffer2[j++] = buffer[i];
                                    }
                                }
                                
                                String str = new String(buffer2).substring(0,j);
                                if (protoState == ParseState.IDLE && j > 0) {
                                    //convert to string of size numBytes
                                    switch (lineMode) {
                                        case CR :
                                            str=str.replace('\r', '\n'); //replace CR with Newline
                                            break;
                                        case LF :
                                            break;
                                        case CRLF :
                                            str=str.replace("\r\n", "\n");
                                            break;
                                        default :
                                            break;
                                    }
                                    str=str.replace('\r','\n'); //replace CR with Newline
                                    append(str, null);
                                }
                            }
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        break;
                    }
                    /*
                    case INT16: {
                        readall(inputStream, buffer, 2); //put two bytes in buffer
                        int16value = 256*(int)buffer[1] + (int)buffer[0];
//                        textWin.append(int16value + "\n");        //write to terminal
                        break;
                    }*/
                }
                //scroll terminal to bottom
                text.setCaretPosition(text.getText().length());
        break;
        }
    }

    //fill buffer with numBytes bytes from is
    public void readall(InputStream is, byte[] buffer, int numBytes) {
        int tempRead=0;
        while( tempRead < numBytes ) {
            try {
                tempRead = tempRead + is.read(buffer, tempRead, numBytes - tempRead);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return;
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SerTermGUI().setVisible(true);
            }
        });
    }

    //constants
    static final int MAX_PORTS = 20;    //maximum number of ports to look for
    static final int MAX_DATA = 64;//maximum length of serial data received
    private enum ParseState { IDLE, ARMED, FILENAME, STARTED; };
    private enum LineMode { CRLF, CR, LF; };
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton SendButton;
    private javax.swing.JComboBox baudBox;
    private javax.swing.JButton clearButton;
    private javax.swing.JButton jButton1;
    private javax.swing.JOptionPane jOptionPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JComboBox portBox;
    private javax.swing.JToggleButton portToggle;
    private javax.swing.ButtonGroup rxformat;
    private javax.swing.JTextArea text;
    private javax.swing.JTextField textbar;
    // End of variables declaration//GEN-END:variables
    private String[] tempPortList, portList; //list of ports for combobox dropdown
    private String portName;
    private CommPort commPort;
    private SerialPort serialPort;
    private CommPortIdentifier portIdentifier = null;
    private InputStream inputStream;
    private OutputStream outputStream;
    private int baudRate=115200;
    private boolean open=false;
    public enum RxFormat {ASCII, INT16;}
    private RxFormat displayFormat;

    private ParseState protoState = ParseState.IDLE;
    private StringBuilder filename;
    private FileWriter fstream;
    private BufferedWriter out;
    private SimpleAttributeSet alertStyle = new SimpleAttributeSet();
    private StyledDocument doc;
    private LineMode lineMode = LineMode.CRLF;
    // TODO: user input for download path
    // TODO: save preferences (com port, baud, download path, etc
    private StringBuilder downloadPath = new StringBuilder().append(System.getProperty("user.home").toString()).append("/Downloads/");//.append("\\My Documents\\Downloads\\");

}
