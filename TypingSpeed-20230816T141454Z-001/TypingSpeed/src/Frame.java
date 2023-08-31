import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Random;
//author@TechVidvan
@SuppressWarnings("serial")
public class Frame extends JFrame implements ActionListener, KeyListener
{
	String passage=""; 
	String typedPass=""; 
	String message=""; 
	//author@TechVidvan
	int typed=0; //typed stores till which character the user has typed
	int count=0;
	int WPM;
	//author@TechVidvan
	double start; 
	double end; 
	double elapsed;
	double seconds;
	boolean running; 
	boolean ended; //Whether the typing test has ended or not
	final int SCREEN_WIDTH;
	final int SCREEN_HEIGHT;
	final int DELAY=100; 
	JButton button; 
	Timer timer;
	JLabel label; 
	public Frame()//author@TechVidvan
	{
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		SCREEN_WIDTH=720;
		SCREEN_HEIGHT=400;
		this.setSize(SCREEN_WIDTH,SCREEN_HEIGHT);
		this.setVisible(true); 
		this.setLocationRelativeTo(null); 
		button=new JButton("Start");
		button.setFont(new Font("Georgia",Font.BOLD,30));
		button.setForeground(Color.BLUE);
		button.setVisible(true);
		button.addActionListener(this);
		button.setFocusable(false);
		label=new JLabel();
		label.setText("Click the Start Button to begin the test");
		label.setFont(new Font("Georgia",Font.BOLD,30));
		label.setVisible(true);
		label.setOpaque(true);
		label.setHorizontalAlignment(JLabel.CENTER); 
		label.setBackground(Color.lightGray);
		this.add(button, BorderLayout.SOUTH);
		this.add(label, BorderLayout.NORTH);
		this.getContentPane().setBackground(Color.WHITE);
		this.addKeyListener(this);
		this.setFocusable(true); 
		this.setResizable(false);
		this.setTitle("Typing Test");
		this.revalidate(); 
	}
	@Override
	public void paint(Graphics g)	
	{
		super.paint(g);
		draw(g); 
	}
	public void draw(Graphics g)
	{
		g.setFont(new Font("Georgia", Font.BOLD, 25));
		
		if(running)
		{
			//This will put our passage on the screen 
			if(passage.length()>1) 
			{
				g.drawString(passage.substring(0,50), g.getFont().getSize(), g.getFont().getSize()*5);
				g.drawString(passage.substring(50,100), g.getFont().getSize(), g.getFont().getSize()*7);
				g.drawString(passage.substring(100,150), g.getFont().getSize(), g.getFont().getSize()*9);
				g.drawString(passage.substring(150,200), g.getFont().getSize(), g.getFont().getSize()*11);
			}
			
			//Displaying correctly typed passage in White
			g.setColor(Color.WHITE);			
			if(typedPass.length()>0)
			{ 
				if(typed<50) //if the typed index is in the first line
					g.drawString(typedPass.substring(0,typed), g.getFont().getSize(),g.getFont().getSize()*5); //From the first letter to the currently typed one in green
				else
					g.drawString(typedPass.substring(0,50), g.getFont().getSize(),g.getFont().getSize()*5); //If the typed character exceeds 50 we can directly show the whole line in green
			}	
			if(typedPass.length()>50)
			{
				if(typed<100) 
					g.drawString(typedPass.substring(50,typed), g.getFont().getSize(),g.getFont().getSize()*7);
				else
					g.drawString(typedPass.substring(50,100), g.getFont().getSize(),g.getFont().getSize()*7);
			}
			if(typedPass.length()>100)
			{
				if(typed<150) 
					g.drawString(typedPass.substring(100,typed), g.getFont().getSize(),g.getFont().getSize()*9);
				else
					g.drawString(typedPass.substring(100,150), g.getFont().getSize(),g.getFont().getSize()*9);
			}
			if(typedPass.length()>150)
				g.drawString(typedPass.substring(150,typed), g.getFont().getSize(),g.getFont().getSize()*11);
			running=false; 
		}
		if(ended)
		{
			if(WPM<=40)
				message="You are an Average Typist";
			else if(WPM>40 && WPM<=60)
				message="You are a Good Typist";
			else if(WPM>60 && WPM<=100)
				message="You are an Excellent Typist";
			else
				message="You are an Elite Typist";
			
			FontMetrics metrics=getFontMetrics(g.getFont());
			g.setColor(Color.BLUE);
			g.drawString("Typing Test Completed!", (SCREEN_WIDTH-metrics.stringWidth("Typing Test Completed!"))/2, g.getFont().getSize()*6);
			
			g.setColor(Color.BLACK);
			g.drawString("Typing Speed: "+WPM+" Words Per Minute", (SCREEN_WIDTH-metrics.stringWidth("Typing Speed: "+WPM+" Words Per Minute"))/2, g.getFont().getSize()*9);
			g.drawString(message, (SCREEN_WIDTH-metrics.stringWidth(message))/2, g.getFont().getSize()*11);
			
			timer.stop();
			ended=false; 
		}
	}
	
	@Override
	public void keyTyped(KeyEvent e) //keyTyped uses the key Character which can identify capital and lowercase difference in keyPressed it takes unicode so it also considers shift which creates a problem
	{
		if(passage.length()>1)
		{
			if(count==0)
				start=LocalTime.now().toNanoOfDay();
			else if(count==200) //Once all 200 characters are typed we will end the time and calculate time elapsed
			{
				end=LocalTime.now().toNanoOfDay();
				elapsed=end-start;
				seconds=elapsed/1000000000.0; 
				WPM=(int)(((200.0/5)/seconds)*60); //number of character by 5 is one word by seconds is words per second * 60 WPM
				ended=true;
				running=false;
				count++;
			}
			char[] pass=passage.toCharArray();
			if(typed<200)
			{
				running=true;
				if(e.getKeyChar()==pass[typed]) 
				{
					typedPass=typedPass+pass[typed]; //To the typed Passage we are adding what is currently typed
					typed++;
					count++; 
				} //If the typed character is not equal to the current position it will not add it to the checked 
			}
		}
	}
	//author@TechVidvan
	@Override
	public void keyPressed(KeyEvent e) 
	{
	}
	//author@TechVidvan
	@Override
	public void keyReleased(KeyEvent e) 
	{	
	}
	//author@TechVidvan
	@Override 
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getSource()==button)
		{
			passage=getPassage();
			timer=new Timer(DELAY,this);
			timer.start();
			running=true;
			ended=false;
			
			typedPass="";
			message="";
			
			typed=0;
			count=0;
		}
		if(running)
			repaint();
		if(ended)
			repaint();
	}
	public static String getPassage()
	{
		ArrayList<String> Passages=new ArrayList<String>();
		String pas1="There are usually about 200 words in a paragraph, but this can vary widely. Most paragraphs focus on a single idea that's expressed with an introductory sentence, then followed by two or more supporting sentences about the idea.";
		String pas2="Your only chance of survival, if you are sincerely smitten, lies in hiding this fact from the woman you love, of feigning a casual detachment under all circumstances.";
		String pas3="Pennies saved one and two at a time by bulldozing the grocer and the vegetable man and the butcher until one’s cheeks burned with the silent imputation of parsimony that such close dealing implied. ";
		String pas4="At that moment he had a thought that he'd never imagine he'd consider. \"I could just cheat,\" he thought, \"and that would solve the problem.\" He tried to move on from the thought but it was persistent.";
		String pas5="Out of another, I get a lovely view of the bay and a little private wharf belonging to the estate. There is a beautiful shaded lane that runs down there from the house. ";
		String pas6="What have you noticed today? I noticed that if you outline the eyes, nose, and mouth on your face with your finger, you make an \"I\" which makes perfect sense, but is something I never noticed before. What have you noticed today?";
		String pas7="Balloons are pretty and come in different colors, different shapes, different sizes, and they can even adjust sizes as needed. But don't make them too big or they might just pop, and then bye-bye balloon. It'll be gone and lost for the rest of mankind.";
		String pas8="He picked up the burnt end of the branch and made a mark on the stone. Day 52 if the marks on the stone were accurate. He couldn't be sure. Day and nights had begun to blend together creating confusion, but he knew it was a long time. Much too long.";
		String pas9="You know that tingly feeling you get on the back of your neck sometimes? I just got that feeling when talking with her. You know I don't believe in sixth senses, but there is something not right with her. I don't know how I know, but I just do.";
		String pas10="It seemed like it should have been so simple. There was nothing inherently difficult with getting the project done. It was simple and straightforward enough that even a child should have been able to complete it on time, but that wasn't the case.";
		
		Passages.add(pas1);
		Passages.add(pas2);
		Passages.add(pas3);
		Passages.add(pas4);
		Passages.add(pas5);
		Passages.add(pas6);
		Passages.add(pas7);
		Passages.add(pas8);
		Passages.add(pas9); 
		Passages.add(pas10);
		
		Random rand=new Random();
		int place=(rand.nextInt(10));
		
		String toReturn=Passages.get(place).substring(0,200); 
		if(toReturn.charAt(199)==32) 
		{
			toReturn=toReturn.strip(); 
			toReturn=toReturn+"."; 
		}
		return(toReturn); 
	}
}