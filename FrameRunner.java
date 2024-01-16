import javax.swing.JFrame;

public class FrameRunner {

  public static void main(String[] args) {
    JFrame f = new JFrame("Put Frame Title Here"); 
    ImageEditorPanel p = new ImageEditorPanel();
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.add(p);
    f.pack();
    f.setVisible(true);
    p.setFocusable(true);
    p.requestFocusInWindow();
    p.run();

  }
}
