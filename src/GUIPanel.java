import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Mateusz on 2016-06-05.
 */
public class GUIPanel extends JFrame {
    private JPanel rootPanel;
    private JTextField txtSys;
    private JButton btnSys;
    private JButton btnGeneruj;

    public GUIPanel(){
        setContentPane(rootPanel);
        setVisible(true);
        setSize(320,100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        btnSys.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc=new JFileChooser();
                fc.setFileFilter(new FileNameExtensionFilter("txt","txt"));
                fc.setAcceptAllFileFilterUsed(false);
                if(fc.showOpenDialog(null)==JFileChooser.APPROVE_OPTION){
                    txtSys.setText(fc.getSelectedFile().getAbsolutePath());
                }
            }
        });

        btnGeneruj.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Fisher f=new Fisher(txtSys.getText());
                JOptionPane.showMessageDialog(null,f.generujWidok());

            }
        });
    }
}
