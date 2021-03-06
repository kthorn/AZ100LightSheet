package org.micromanager.lightsheetcontrol;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author kthorn
 */
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractListModel;
import mmcorej.DeviceType;
import mmcorej.StrVector;
import org.micromanager.LogManager;
import org.micromanager.Studio;
import org.apache.commons.math3.stat.regression.SimpleRegression;

public class LightSheetControlForm extends javax.swing.JFrame {

    private final Studio gui_;
    private final mmcorej.CMMCore mmc_;
    private final LogManager logger_;
    private String stage1_ = "";
    private String stage2_ = "";
    private String multiStageName_ = "";
    private final RPModel rpm_;

    /**
     * Creates new form LightSheetControlForm
     *
     * @param gui
     */
    public LightSheetControlForm(Studio gui) {
        gui_ = gui;
        mmc_ = gui_.core();
        logger_ = gui_.logs();
        rpm_ = new RPModel();
        Iterator<String> stageIter;

        initComponents();
        this.setTitle("Light Sheet Control");
        //Find multi stage device
        StrVector stages = mmc_.getLoadedDevicesOfType(DeviceType.StageDevice);
        stageIter = stages.iterator();
        while (stageIter.hasNext()) {
            String devName = "";
            String devLabel = stageIter.next();
            try {
                devName = mmc_.getDeviceName(devLabel);
            } catch (Exception ex) {
                logger_.logError(ex, "Error when requesting stage name");
            }
            if (devName.equals("Multi Stage")) {
                multiStageName_ = devLabel;
            }
        }
        if (multiStageName_.equals("")) {
            logger_.logError("Cannot find multi stage device");
        }
        try {
            stage1_ = mmc_.getProperty(multiStageName_, "PhysicalStage-1");
        } catch (Exception ex) {
            logger_.logError(ex, "Error when requesting Stage1 name");
        }
        try {
            stage2_ = mmc_.getProperty(multiStageName_, "PhysicalStage-2");
        } catch (Exception ex) {
            logger_.logError(ex, "Error when requesting Stage2 name");
        }
    }

    private class RPModel extends AbstractListModel {

        public ReferencePointList RPlist_;

        private RPModel() {
            this.RPlist_ = new ReferencePointList();
        }

        @Override
        public int getSize() {
            return RPlist_.getNumberOfPoints();
        }

        @Override
        public Object getElementAt(int index) {
            ReferencePoint r = RPlist_.getPoint(index);
            return r.name();
        }

        public void addPoint(ReferencePoint r) {
            RPlist_.addPoint(r);
            fireIntervalAdded(this, RPlist_.getNumberOfPoints(), RPlist_.getNumberOfPoints());
        }

        public void deletePoint(int index) {
            RPlist_.removePoint(index);
            fireIntervalRemoved(this, index, index);
        }

        public ReferencePoint getReferencePoint(int index) {
            ReferencePoint r = RPlist_.getPoint(index);
            return r;
        }

        public void updatePoint(int index, ReferencePoint r) {
            RPlist_.replacePoint(index, r);
            fireContentsChanged(this, index, index);
        }

        public void clearRegions() {
            this.RPlist_ = new ReferencePointList();
            fireIntervalAdded(this, 0, 0);
        }
    }
    
    private void updateStageRelation() {
        //updates slope and offset parameters for the two stages
        if (rpm_.getSize() > 1){ //need at least two points for linear fit
                    //Least Squares regression
        SimpleRegression sr = new SimpleRegression();
        int np = rpm_.getSize();
        for (int n=0; n<np; n++) {
            ReferencePoint RP = rpm_.getReferencePoint(n);
            sr.addData(RP.stage1Position, RP.stage2Position);
        }
            try {
                mmc_.setProperty(multiStageName_, "Scaling-2", sr.getSlope());
                mmc_.setProperty(multiStageName_, "TranslationUm-2", sr.getIntercept());
            } catch (Exception ex) {
                logger_.logError(ex, "LightSheetControl: Error when setting scaling and translation");
            }
        }           
    }                

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFrame1 = new javax.swing.JFrame();
        jScrollPane1 = new javax.swing.JScrollPane();
        RPList = new javax.swing.JList();
        GoToPosButton = new javax.swing.JButton();
        DelAllButton = new javax.swing.JButton();
        DelRPButton = new javax.swing.JButton();
        UpdRPButton = new javax.swing.JButton();
        AddRPButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        javax.swing.GroupLayout jFrame1Layout = new javax.swing.GroupLayout(jFrame1.getContentPane());
        jFrame1.getContentPane().setLayout(jFrame1Layout);
        jFrame1Layout.setHorizontalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jFrame1Layout.setVerticalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        RPList.setModel(rpm_);
        jScrollPane1.setViewportView(RPList);
        RPList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        GoToPosButton.setText("Go to Position");
        GoToPosButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GoToPosButtonActionPerformed(evt);
            }
        });

        DelAllButton.setText("Delete All");
        DelAllButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DelAllButtonActionPerformed(evt);
            }
        });

        DelRPButton.setText("Delete Reference Point");
        DelRPButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DelRPButtonActionPerformed(evt);
            }
        });

        UpdRPButton.setText("Update Reference Point");
        UpdRPButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UpdRPButtonActionPerformed(evt);
            }
        });

        AddRPButton.setText("Add Reference Point");
        AddRPButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddRPButtonActionPerformed(evt);
            }
        });

        jLabel1.setText("Reference Points (1st Stage / 2nd Stage)");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(23, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(GoToPosButton)
                    .addComponent(UpdRPButton)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addComponent(AddRPButton)
                            .addGap(12, 12, 12))
                        .addComponent(DelRPButton, javax.swing.GroupLayout.Alignment.TRAILING))
                    .addComponent(DelAllButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(4, 4, 4)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(AddRPButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(DelRPButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(UpdRPButton)
                        .addGap(7, 7, 7)
                        .addComponent(DelAllButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(GoToPosButton)))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void AddRPButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddRPButtonActionPerformed
        ReferencePoint RP = null;
        try {
            RP = new ReferencePoint(mmc_.getPosition(stage1_), mmc_.getPosition(stage2_));
        } catch (Exception ex) {
            logger_.logError(ex, "Error when requesting stage positionsStage 1 / 2 = " + stage1_ + stage2_);
        }
        rpm_.addPoint(RP);
        updateStageRelation();
    }//GEN-LAST:event_AddRPButtonActionPerformed

    private void DelRPButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DelRPButtonActionPerformed
        rpm_.deletePoint(RPList.getSelectedIndex());
        updateStageRelation();
    }//GEN-LAST:event_DelRPButtonActionPerformed

    private void UpdRPButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UpdRPButtonActionPerformed
                ReferencePoint RP = null;
        try {
            RP = new ReferencePoint(mmc_.getPosition(stage1_), mmc_.getPosition(stage2_));
        } catch (Exception ex) {
            logger_.logError(ex, "Error when requesting stage positions. Stage 1 / 2 = " + stage1_ + stage2_);
        }
        rpm_.updatePoint(RPList.getSelectedIndex(), RP);
        updateStageRelation();
    }//GEN-LAST:event_UpdRPButtonActionPerformed

    private void DelAllButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DelAllButtonActionPerformed
        rpm_.clearRegions();
    }//GEN-LAST:event_DelAllButtonActionPerformed

    private void GoToPosButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GoToPosButtonActionPerformed
       ReferencePoint RP = rpm_.getReferencePoint(RPList.getSelectedIndex());
        try {
            mmc_.setPosition(stage1_, RP.stage1Position);
            mmc_.setPosition(stage2_, RP.stage2Position);
        } catch (Exception ex) {
            logger_.logError(ex, "Error when setting stage positions");
        }
    }//GEN-LAST:event_GoToPosButtonActionPerformed

    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(LightSheetControlForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(LightSheetControlForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(LightSheetControlForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(LightSheetControlForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new LightSheetControlForm().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AddRPButton;
    private javax.swing.JButton DelAllButton;
    private javax.swing.JButton DelRPButton;
    private javax.swing.JButton GoToPosButton;
    private javax.swing.JList RPList;
    private javax.swing.JButton UpdRPButton;
    private javax.swing.JFrame jFrame1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
