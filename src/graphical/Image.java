package graphical;
	
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

	public class Image extends JLabel{
		
	private static final long serialVersionUID = 1L;
	public BufferedImage[] vector;


	public Image(){
		String current = System.getProperty("user.dir");
		
		File dir = new File(current + "/src/cards");
		File[] files = dir.listFiles();
		BufferedImage[] vec = new BufferedImage[files.length];
		for (int i = 0; i < files.length; i++)
		{
		   try
		   {
		     vec[i] = ImageIO.read(files[i]);
		   } catch (IOException ex){}
		}
		vector =  vec;
	}
	
	
	public JLabel addImg(JFrame frame, JPanel panel, int a,int b, int c, int d, Color color, int i){
		
		JLabel picLabel = new JLabel(new ImageIcon(vector[i]));
		panel.setBackground(color);
		panel.setBounds(a, b, c, d);
		frame.getContentPane().add(panel);
		panel.add(picLabel);
		return picLabel;
	}
	
	public void removeImg(JLabel picLabel){
		picLabel.setIcon(null);
	    // **IMPORTANT** to call revalidate() to cause JLabel to resize and be repainted.
	    picLabel.revalidate();
	}	
	

}
