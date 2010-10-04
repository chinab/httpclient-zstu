package httpclient.gui;

import httpclient.engine.PreferenceList;

public class OptionsDialog extends javax.swing.JDialog {

    private MainWindow parent;
    private PreferenceList preferences;
    public OptionsDialog(MainWindow parent, PreferenceList preferences, boolean modal) {
        super(parent, modal);
        this.preferences = preferences;
        this.parent = parent;
        initComponents();

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jRadioButtonGET = new javax.swing.JRadioButton();
        jRadioButtonHEAD = new javax.swing.JRadioButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jRadioButtonPOST = new javax.swing.JRadioButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Options");
        setResizable(false);

        buttonGroup1.add(jRadioButtonGET);
        jRadioButtonGET.setSelected(true);
        jRadioButtonGET.setText("GET");

        buttonGroup1.add(jRadioButtonHEAD);
        jRadioButtonHEAD.setText("HEAD");

        jLabel1.setText("Method:");

        jLabel2.setText("Path to file with form:");

        jTextField1.setText("index.html");

        jButton1.setText("OK");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });

        buttonGroup1.add(jRadioButtonPOST);
        jRadioButtonPOST.setText("POST");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jRadioButtonPOST)
                    .addComponent(jLabel1)
                    .addComponent(jRadioButtonGET)
                    .addComponent(jRadioButtonHEAD)
                    .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)
                    .addComponent(jLabel2)
                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jRadioButtonGET)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButtonHEAD)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jRadioButtonPOST)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
        if(jRadioButtonGET.isSelected()){
            preferences.setRequestType(PreferenceList.HTTP_REQUEST_GET);
        } else if (jRadioButtonHEAD.isSelected()){
            preferences.setRequestType(PreferenceList.HTTP_REQUEST_HEAD);
        } else if (jRadioButtonPOST.isSelected()){
            preferences.setRequestType(PreferenceList.HTTP_REQUEST_POST);
            preferences.setPathToPOSTFile(jTextField1.getText());
        }
        this.dispose();
    }//GEN-LAST:event_jButton1MouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JRadioButton jRadioButtonGET;
    private javax.swing.JRadioButton jRadioButtonHEAD;
    private javax.swing.JRadioButton jRadioButtonPOST;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables

}
