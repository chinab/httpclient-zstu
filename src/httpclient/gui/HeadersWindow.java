package httpclient.gui;

import httpclient.engine.general.HeaderList;
import httpclient.engine.general.Header;
import httpclient.engine.ui.InfoWindow;
import httpclient.engine.response.StartingLine;
import java.awt.Image;
import java.awt.Toolkit;

import java.util.Vector;
import javax.swing.table.DefaultTableModel;

public class HeadersWindow extends javax.swing.JFrame
        implements InfoWindow {

    private Vector<String> columnNames;

    public HeadersWindow() {
        initComponents();
        Image appIcon = Toolkit.getDefaultToolkit().getImage(
                getClass().getResource("/httpclient/icons/application.png"));
        this.setIconImage(appIcon);
        columnNames = new Vector<String>();
        columnNames.add("Name");
        columnNames.add("Value");
    }

    public void updateStatus(StartingLine sl){
        jLabelVersion.setText("HTTP Version: " + sl.getMajorVersion()
                + "." + sl.getMinorVersion());
        jLabelCode.setText("Status Code: " + sl.getStatusCode());
        jLabelMessage.setText("Status Message: " + sl.getReasonPhrase());
    }

    public void updateHeaders(HeaderList headerList){
        Header[] headers = headerList.getAllHeaders();
        int count = headers.length;
        DefaultTableModel a = new DefaultTableModel(columnNames, count);
        jHeadersTable.setModel(a);
        for(int i=0;i<headers.length;i++){
            jHeadersTable.setValueAt(headers[i].getName(), i, 0);
            jHeadersTable.setValueAt(headers[i].getValue(), i, 1);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jHeadersTable = new javax.swing.JTable();
        jLabelCode = new javax.swing.JLabel();
        jLabelMessage = new javax.swing.JLabel();
        jLabelVersion = new javax.swing.JLabel();

        setTitle("Headers");

        jHeadersTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(jHeadersTable);

        jLabelCode.setFont(new java.awt.Font("Verdana", 0, 10));
        jLabelCode.setText("Status Code: ");

        jLabelMessage.setFont(new java.awt.Font("Verdana", 0, 10));
        jLabelMessage.setText("Status Message: ");

        jLabelVersion.setFont(new java.awt.Font("Verdana", 0, 10));
        jLabelVersion.setText("HTTP Version: ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelCode)
                            .addComponent(jLabelMessage)
                            .addComponent(jLabelVersion)))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 393, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelVersion)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelCode)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelMessage)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 299, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable jHeadersTable;
    private javax.swing.JLabel jLabelCode;
    private javax.swing.JLabel jLabelMessage;
    private javax.swing.JLabel jLabelVersion;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables

}
