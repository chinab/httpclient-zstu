package httpclient.gui;

import httpclient.engine.ui.AuthorisationForm;
import httpclient.gui.handlers.AuthParametersHandler;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class AuthenticationDialog extends javax.swing.JDialog
        implements AuthorisationForm {

    private AuthParametersHandler handler;
    java.awt.Frame parent;

    public AuthenticationDialog(java.awt.Frame parent) {
        super(parent, true);
        this.parent = parent;
        
        initComponents();
    }

    public void setVisible(AuthParametersHandler handler, String realm){
        this.setLocationRelativeTo(parent);
        this.handler = handler;
        jRealm.setText(realm);
        jLogin.setText("");
        jPassword.setText("");
        jLogin.requestFocus();
        this.setVisible(true);
        
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLogin = new javax.swing.JTextField();
        jPassword = new javax.swing.JPasswordField();
        jButtonOK = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jRealm = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Authentication");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        setResizable(false);

        jButtonOK.setText("OK");
        jButtonOK.setOpaque(false);
        jButtonOK.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonOKMouseClicked(evt);
            }
        });
        jButtonOK.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jButtonOKKeyReleased(evt);
            }
        });

        jButtonCancel.setText("Cancel");
        jButtonCancel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonCancelMouseClicked(evt);
            }
        });
        jButtonCancel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jButtonCancelKeyReleased(evt);
            }
        });

        jLabel1.setText("Login");

        jLabel2.setText("Password");

        jRealm.setFont(new java.awt.Font("Tahoma", 0, 10));
        jRealm.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jRealm.setText("Realm");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jRealm, javax.swing.GroupLayout.DEFAULT_SIZE, 193, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jButtonOK, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLogin, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPassword, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jButtonCancel, jButtonOK});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jRealm, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonCancel)
                    .addComponent(jButtonOK))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void setAuthParameters(){
        handler.setAuthParameters(jLogin.getText(),
                String.valueOf(jPassword.getPassword()));
    }

    private void jButtonCancelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonCancelMouseClicked
        this.setVisible(false);
    }//GEN-LAST:event_jButtonCancelMouseClicked
    private void jButtonCancelKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButtonCancelKeyReleased
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            this.setVisible(false);
        }
    }//GEN-LAST:event_jButtonCancelKeyReleased
    private void jButtonOKMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonOKMouseClicked
        setAuthParameters();
        this.setVisible(false);
    }//GEN-LAST:event_jButtonOKMouseClicked
    private void jButtonOKKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButtonOKKeyReleased
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            setAuthParameters();
            this.setVisible(false);
        }
    }//GEN-LAST:event_jButtonOKKeyReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonOK;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JTextField jLogin;
    private javax.swing.JPasswordField jPassword;
    private javax.swing.JLabel jRealm;
    // End of variables declaration//GEN-END:variables

}
