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

	// TODO: Auto-generated Javadoc
/**
	 * The Class Image.
	 */
	public class Image extends JLabel{
	
		
		/** The Constant serialVersionUID. 
		 * This serialVersionUID represents the class version, and it should be incremented if the current version of the class is not backwards compatible with its previous version.*/
	private static final long serialVersionUID = 1L;
	
	/** The vector. 
	 * vector is a vector of BufferdImage that stores all the images needed to play the game.*/
	public BufferedImage[] vector;

	/**
	 * Instantiates a new image.
	 * The constructor builds the vector of BufferedImage.
	 * It checks the current directory and then searches on a specific directory inside the current one for the wanted files.
	 * These files are stored along a vector of File used to then fill the vector of BufferedImage.
	 * The images are stored in this vector because of memory allocation
	 * Since it is better to load the files just one time to the vector and then use them from there than load the files every time they are needed
	 */
	
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
	
	
	//This method is used to associate a new ImageIcon to a new JLabel.
	//Given a frame and a panel on it placed on (a, b) with size (b, c) and with background color "color", a new image is created on a label added to the panel.
	/**
	 * Adds the img.
	 * This method is used to associate a new ImageIcon to a new JLabel.
	 * Given a frame and a panel on it placed on (a, b) with size (b, c) and with background color "color", a new image is created on a label added to the panel.
	 * The image is chosen from the position "i" of the vector "vector" 
	 * 
	 * @param frame the frame
	 * @param panel the panel
	 * @param a the a
	 * @param b the b
	 * @param c the c
	 * @param d the d
	 * @param color the color
	 * @param i the i
	 * @return the j label
	 */
	public JLabel addImg(JFrame frame, JPanel panel, int a,int b, int c, int d, Color color, int i){
		JLabel picLabel = new JLabel(new ImageIcon(vector[i]));
		panel.setBackground(color);
		panel.setBounds(a, b, c, d);
		frame.getContentPane().add(panel);
		panel.add(picLabel);
		return picLabel;
	}

}