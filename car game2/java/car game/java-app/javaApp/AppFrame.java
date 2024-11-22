import javax.swing.JFrame;

public class AppFrame extends JFrame {
    AppFrame(){
        initApp();
    }

    private void initApp(){
        //set frame  
        setTitle("CAR GAME");
        setSize(500,700);
        setLocationRelativeTo(null);
        
        AppPanel ap = new AppPanel();
        add(ap);
        // setResizable(false);
        setVisible(true);
    }
}
