
package cz.cvut.fel.skardada.chess;

import javax.swing.JFileChooser;

/**
 * MainView is a class of view and is a java.swing window application. It displays game options for game configuration.
 * @author Adam Škarda
 */
public class MainView extends javax.swing.JFrame {

    private String savePath;
    private String stylePath;
    private String p1time;
    private String p2time;
    private String p1incrementTime;
    private String p2incrementTime;
    private PlayerColors p1color;
    private PlayerColors p2color;
    private PossibleOpponents p1Type;
    private PossibleOpponents p2Type;
    private String p1name;
    private String p2name;
    private boolean ready = false;
    private boolean normalStart = false;
    private boolean manualSetup = false;
    private boolean pgnView = false;
    private boolean loadGame = false;
    private final JFileChooser saveDialog;

    /**
     * Creates new form mainView
     */
    public MainView() {
        initComponents();
        this.saveDialog = new JFileChooser();
    }

    private boolean validateInputs() {
        boolean noErrors = true;

        //TODO sort THIS code to validate functions of elements
        //set Player Names
        //Player 1
        String errorPrompt = "Give me your Name!";
        if (p1NameChooser.getText() == null || p1NameChooser.getText().equals("") || p1NameChooser.getText().equals(errorPrompt)) {
            p1NameChooser.setText(errorPrompt);
            noErrors = false;
        } else {
            this.p1name = p1NameChooser.getText();
        }

        //Player 2
        if (p2NameChooser.getText() == null || p2NameChooser.getText().equals("") || p2NameChooser.getText().equals(errorPrompt)) {
            p2NameChooser.setText(errorPrompt);
            noErrors = false;
        } else {
            this.p2name = p2NameChooser.getText();
        }

        //set Player Types
        if (p1TypeChooser.getSelectedItem() == p2TypeChooser.getSelectedItem() && p1TypeChooser.getSelectedItem() == PossibleOpponents.internet) {
            noErrors = false;
        } else {
            this.p1Type = (PossibleOpponents) p1TypeChooser.getSelectedItem();
            this.p2Type = (PossibleOpponents) p2TypeChooser.getSelectedItem();
        }

        //set Player Colors
        if (p1Color.isSelected()) {
            p1color = PlayerColors.white;
            p2color = PlayerColors.black;
        } else {
            p1color = PlayerColors.black;
            p2color = PlayerColors.white;
        }

        // Player 1
        int timeStringPartNumber = 3;
        String timeErrorMsg = "hh:mm:ss";
        if (p1TimeChooser.getText().equals("")) {
            p1TimeChooser.setText(timeErrorMsg);
            noErrors = false;
        }
        String[] timeInfo = p1TimeChooser.getText().split(":");
        if (timeInfo.length != timeStringPartNumber) {
            p1TimeChooser.setText(timeErrorMsg);
            noErrors = false;
        } else {
            for (String info : timeInfo) {
                try {
                    Integer.parseInt(info);
                } catch (NumberFormatException ex) {
                    p1TimeChooser.setText(timeErrorMsg);
                    noErrors = false;
                }
            }
            p1time = p1TimeChooser.getText();
        }

        // Player 2
        if (p2TimeChooser.getText().equals("")) {
            p2TimeChooser.setText(timeErrorMsg);
            noErrors = false;
        }
        timeInfo = p2TimeChooser.getText().split(":");
        if (timeInfo.length != timeStringPartNumber) {
            p2TimeChooser.setText(timeErrorMsg);
            noErrors = false;
        } else {
            for (String info : timeInfo) {
                try {
                    Integer.parseInt(info);
                } catch (NumberFormatException ex) {
                    p2TimeChooser.setText(timeErrorMsg);
                    noErrors = false;
                }
            }
            p2time = p2TimeChooser.getText();
        }

        //set Player increment
        //Player 1
        if (p1IncrementChooser.getText().equals("")) {
            p1IncrementChooser.setText(timeErrorMsg);
            noErrors = false;
        }
        String[] incrementInfo = p1IncrementChooser.getText().split(":");
        if (incrementInfo.length != timeStringPartNumber) {
            p1IncrementChooser.setText(timeErrorMsg);
            noErrors = false;
        } else {
            for (String info : timeInfo) {
                try {
                    Integer.parseInt(info);
                } catch (NumberFormatException ex) {
                    p1IncrementChooser.setText(timeErrorMsg);
                    noErrors = false;
                }
            }
            p1incrementTime = p1IncrementChooser.getText();
        }
        //Player 2
        if (p2IncrementChooser.getText().equals("")) {
            p2IncrementChooser.setText(timeErrorMsg);
            noErrors = false;
        }
        incrementInfo = p2IncrementChooser.getText().split(":");
        if (incrementInfo.length != timeStringPartNumber) {
            p2IncrementChooser.setText(timeErrorMsg);
            noErrors = false;
        } else {
            for (String info : timeInfo) {
                try {
                    Integer.parseInt(info);
                } catch (NumberFormatException ex) {
                    p2IncrementChooser.setText(timeErrorMsg);
                    noErrors = false;
                }
            }
            p2incrementTime = p2IncrementChooser.getText();
        }
        return noErrors;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        playerColor = new javax.swing.ButtonGroup();
        p1TypeChooser = new javax.swing.JComboBox<>();
        p1NameChooser = new javax.swing.JTextField();
        p2NameChooser = new javax.swing.JTextField();
        p2TypeChooser = new javax.swing.JComboBox<>();
        startButton = new javax.swing.JButton();
        p1TimeChooser = new javax.swing.JTextField();
        p2TimeChooser = new javax.swing.JTextField();
        p2IncrementChooser = new javax.swing.JTextField();
        p1IncrementChooser = new javax.swing.JTextField();
        p1Color = new javax.swing.JRadioButton();
        p2Color = new javax.swing.JRadioButton();
        player2TimeLabel = new javax.swing.JLabel();
        player1TimeLabel = new javax.swing.JLabel();
        player1Increment = new javax.swing.JLabel();
        player2Increment = new javax.swing.JLabel();
        player2NameLabel = new javax.swing.JLabel();
        player1NameLabel = new javax.swing.JLabel();
        loadGameButton = new javax.swing.JButton();
        setUpGameButton = new javax.swing.JButton();
        viewPgnButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Super Duper Chess\n");

        p1TypeChooser.setModel(new javax.swing.DefaultComboBoxModel<>(PossibleOpponents.values()));
        p1TypeChooser.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                p1TypeChooserItemStateChanged(evt);
            }
        });

        p1NameChooser.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        p1NameChooser.setText("Player 1");

        p2NameChooser.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        p2NameChooser.setText("Player 2");

        p2TypeChooser.setModel(new javax.swing.DefaultComboBoxModel<>(PossibleOpponents.values()));
        p2TypeChooser.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                p2TypeChooserItemStateChanged(evt);
            }
        });

        startButton.setBackground(new java.awt.Color(0, 204, 51));
        startButton.setFont(new java.awt.Font("Arial Black", 1, 48)); // NOI18N
        startButton.setText("Start");
        startButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startButtonActionPerformed(evt);
            }
        });

        p1TimeChooser.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        p1TimeChooser.setText("00:10:00");
        p1TimeChooser.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                p1TimeChooserPropertyChange(evt);
            }
        });

        p2TimeChooser.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        p2TimeChooser.setText("00:10:00");

        p2IncrementChooser.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        p2IncrementChooser.setText("00:00:05");

        p1IncrementChooser.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        p1IncrementChooser.setText("00:00:05");

        playerColor.add(p1Color);
        p1Color.setSelected(true);
        p1Color.setText(PlayerColors.white.toString());

        playerColor.add(p2Color);
        p2Color.setText(PlayerColors.white.toString());
        p2Color.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        p2Color.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        player2TimeLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        player2TimeLabel.setText("Player 2 Time");

        player1TimeLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        player1TimeLabel.setText("Player 1 Time");

        player1Increment.setText("Player 1 Increment");

        player2Increment.setText("Player 2 Increment");

        player2NameLabel.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        player2NameLabel.setText("Player 2 Name");
        player2NameLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);

        player1NameLabel.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        player1NameLabel.setText("Player 1 Name");

        loadGameButton.setText("Load Saved Game");
        loadGameButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadGameButtonActionPerformed(evt);
            }
        });

        setUpGameButton.setText("Set Up Pieces");
        setUpGameButton.setToolTipText("");
        setUpGameButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setUpGameButtonActionPerformed(evt);
            }
        });

        viewPgnButton.setText("View PGN Game");
        viewPgnButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewPgnButtonActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
        jLabel1.setText("Super Duper Chess");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(150, 150, 150)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(loadGameButton, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(setUpGameButton, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(56, 56, 56)
                        .addComponent(viewPgnButton, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(player1TimeLabel)
                            .addComponent(player1Increment)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(p1TypeChooser, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(p1NameChooser, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(p1IncrementChooser, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(p1Color, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(p1TimeChooser, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(player1NameLabel))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(383, 383, 383)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(player2Increment, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(p2TimeChooser, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(p2IncrementChooser, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(92, 92, 92)
                                .addComponent(startButton, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(p2Color, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                            .addComponent(player2TimeLabel)
                                            .addGap(9, 9, 9))
                                        .addComponent(p2TypeChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(player2NameLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(p2NameChooser, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addGap(150, 150, 150))
            .addGroup(layout.createSequentialGroup()
                .addGap(267, 267, 267)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 479, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                        .addComponent(setUpGameButton, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(viewPgnButton, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(loadGameButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(player1NameLabel)
                    .addComponent(player2NameLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(p2NameChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(p1NameChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(p1TypeChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(p2TypeChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(p2Color)
                    .addComponent(p1Color))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(player1TimeLabel)
                    .addComponent(player2TimeLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(p2TimeChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(3, 3, 3))
                    .addComponent(p1TimeChooser, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(25, 25, 25)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(p1IncrementChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(p2IncrementChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(player1Increment, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(player2Increment, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(149, 149, 149))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 63, Short.MAX_VALUE)
                        .addComponent(startButton, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(35, 35, 35))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void startButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startButtonActionPerformed

        //set ready flag for controller
        if (this.validateInputs()) {
            this.ready = true;
            this.normalStart = true;
        } else {
            this.ready = false;
        }
    }//GEN-LAST:event_startButtonActionPerformed

    private void p2TypeChooserItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_p2TypeChooserItemStateChanged

    }//GEN-LAST:event_p2TypeChooserItemStateChanged

    private void p1TypeChooserItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_p1TypeChooserItemStateChanged

    }//GEN-LAST:event_p1TypeChooserItemStateChanged

    private void p1TimeChooserPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_p1TimeChooserPropertyChange

    }//GEN-LAST:event_p1TimeChooserPropertyChange

    private void setUpGameButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setUpGameButtonActionPerformed

        if (this.validateInputs()) {
            this.ready = true;
            this.manualSetup = true;
        } else {
            this.ready = false;
        }
    }//GEN-LAST:event_setUpGameButtonActionPerformed

    private void loadGameButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadGameButtonActionPerformed
        this.ready = true;
        this.loadGame = true;
    }//GEN-LAST:event_loadGameButtonActionPerformed

    private void viewPgnButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewPgnButtonActionPerformed
        this.ready = true;
        this.pgnView = true;
    }//GEN-LAST:event_viewPgnButtonActionPerformed

    /**
     *
     * @return returns save game path
     */
    public String getSavePath() {
        return savePath;
    }

    /**
     *
     * @return returns stylePath
     */
    public String getStylePath() {
        return stylePath;
    }

    /**
     *
     * @return returns player 1 time
     */
    public String getP1time() {
        return p1time;
    }

    /**
     *
     * @return returns normalStart flag
     */
    public boolean isNormalStart() {
        return normalStart;
    }

    /**
     *
     * @return returns manualSetup flag
     */
    public boolean isManualSetup() {
        return manualSetup;
    }

    /**
     *
     * @return
     */
    public boolean isPgnView() {
        return pgnView;
    }

    /**
     *
     * @return returns loadGame flag
     */
    public boolean isLoadGame() {
        return loadGame;
    }

    /**
     *
     * @return returns player 2 time as string
     */
    public String getP2time() {
        return p2time;
    }

    /**
     *
     * @return returns player 1 name
     */
    public String getP1name() {
        return p1name;
    }

    /**
     *
     * @return returns player 1 increment
     */
    public String getP1incrementTime() {
        return p1incrementTime;
    }

    /**
     *
     * @return returns player 2 incrment
     */
    public String getP2incrementTime() {
        return p2incrementTime;
    }

    /**
     *
     * @return returns player 1 color
     */
    public PlayerColors getP1color() {
        return p1color;
    }

    /**
     *
     * @return returns player 2 color
     */
    public PlayerColors getP2color() {
        return p2color;
    }

    /**
     * sets a flag to indicate if normal start is selected
     * @param normalStart
     */
    public void setNormalStart(boolean normalStart) {
        this.normalStart = normalStart;
    }

    /**
     *
     * @param manualSetup
     */
    public void setManualSetup(boolean manualSetup) {
        this.manualSetup = manualSetup;
    }

    /**
     * sets flag to indicate if pgn is viewed
     * @param pgnView
     */
    public void setPgnView(boolean pgnView) {
        this.pgnView = pgnView;
    }

    /**
     * sets load game flag to indicate if game is loaded
     * @param loadGame 
     */
    public void setLoadGame(boolean loadGame) {
        this.loadGame = loadGame;
    }

    /**
     *
     * @return returns player 1 type
     */
    public PossibleOpponents getP1Type() {
        return p1Type;
    }

    /**
     *
     * @return returns player 2 type
     */
    public PossibleOpponents getP2Type() {
        return p2Type;
    }

    /**
     *
     * @return returns name of player 2
     */
    public String getP2name() {
        return p2name;
    }

    /**
     * sets ready state
     * @param ready 
     */
    public void setReady(boolean ready) {
        this.ready = ready;
    }

    /**
     *
     * @return returns ready state 
     */
    public boolean isReady() {
        return ready;
    }

    /**
     * opens saveDialog for user path input
     * @return returns chosen path to file
     */
    public String showOpenDialog() {
        int k = saveDialog.showOpenDialog(this);
        if (k != JFileChooser.APPROVE_OPTION) {
            return "";
        }
        String path = saveDialog.getSelectedFile().getAbsolutePath();
        return path;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton loadGameButton;
    private javax.swing.JRadioButton p1Color;
    private javax.swing.JTextField p1IncrementChooser;
    private javax.swing.JTextField p1NameChooser;
    private javax.swing.JTextField p1TimeChooser;
    private javax.swing.JComboBox<PossibleOpponents> p1TypeChooser;
    private javax.swing.JRadioButton p2Color;
    private javax.swing.JTextField p2IncrementChooser;
    private javax.swing.JTextField p2NameChooser;
    private javax.swing.JTextField p2TimeChooser;
    private javax.swing.JComboBox<PossibleOpponents> p2TypeChooser;
    private javax.swing.JLabel player1Increment;
    private javax.swing.JLabel player1NameLabel;
    private javax.swing.JLabel player1TimeLabel;
    private javax.swing.JLabel player2Increment;
    private javax.swing.JLabel player2NameLabel;
    private javax.swing.JLabel player2TimeLabel;
    private javax.swing.ButtonGroup playerColor;
    private javax.swing.JButton setUpGameButton;
    private javax.swing.JButton startButton;
    private javax.swing.JButton viewPgnButton;
    // End of variables declaration//GEN-END:variables
}
