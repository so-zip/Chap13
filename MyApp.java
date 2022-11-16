package chap13;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class MyApp extends JFrame {
	private JLabel text1 = new JLabel("0");
	private JLabel text2 = new JLabel("0");
	
	public MyApp() {
		super("스레드 연습");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container c = getContentPane();
		c.setLayout(null);
		
		text1.setSize(100, 40);
		text1.setLocation(50, 50);
		
		text2.setSize(100, 40);
		text2.setLocation(170, 50);
		
		c.add(text1);
		text1.setFont(new Font("Gothic", Font.ITALIC, 30));
		c.add(text2);
		text2.setFont(new Font("Gothic", Font.ITALIC, 30));
		
		setSize(300, 300);
		setVisible(true); // 하나의 스레드를 생성
		System.out.println(Thread.currentThread().getName());
		
		TimerThread th1 = new TimerThread("Sojin", text1, 1000); // 스레드를 생성했으면 다른 스레드와는 별개로 돌리고싶은 코드를 작성해야 한다.
		th1.start(); // 스레드를 RUNNABLE 상태로 만들어주는 함수
		
		TimerThread th2 = new TimerThread("Kitae", text2, 100); // 스레드를 생성했으면 다른 스레드와는 별개로 돌리고싶은 코드를 작성해야 한다.
		th2.start(); // 스레드를 RUNNABLE 상태로 만들어주는 함수
		
		//actionEvent를 붙일 수 있는 3가지 -> JTextField에 엔터, 메뉴바, JButton
		
		text1.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				System.out.println("Mouse Pressed : " + Thread.currentThread().getName());
				if(th1.isAlive())
					th1.interrupt();
				else
					System.out.println(th1.getName() + "은(는) 종료하였습니다." + th1.getCount());
			}
		});
	}
	//스레드 상태 RUNNABLE : 돌아가고 있는 상태, BLOCK : 누군가 오기를 기다리는 상태->오면 알려줌, TERMINATED : 이미 죽은 상태
	class TimerThread extends Thread {
		private JLabel text = null;
		private int delay = 0;
		private int count = 0;
		
		public TimerThread(String name, JLabel text, int delay) {
			super(name);
			this.text = text;
			this.delay = delay;
			System.out.println("생성자:" + Thread.currentThread().getName()); // main 스레드가 실행하는 코드이다.
		}
		
		public int getCount() { return count; }
		
		@Override
		public void run() { // 스레드 코드, run함수는 직접 부르는 함수가 아님..! -> JVM이 자동적으로 바로 실행하는 코드
			System.out.println(Thread.currentThread().getName()); // name 스레드가 실행하는 코드이다.
			while(true) {
				try {
					Thread.sleep(delay); // 1초동안 중단 -> 함수가 기울어졌다는건 static 함수라는 뜻
					count++;
					text.setText(Integer.toString(count));
					/*
					 if(Thread.currentThread().getName().equals("Sojin")) {
						int x = (int)(Math.random()*text.getParent().getWidth());
						int y = (int)(Math.random()*text.getParent().getHeight());
						text.setLocation(x, y);
					}
					*/
				} catch (InterruptedException e) {
					System.out.println("interrupt 받았어요..");
					return; // JVM한테 이제 종료한다는 신호를 줌 -> 스레드 TERMINATED 상태로 전환
				}
			}
		}
	}
	
	public static void main(String[] args) {
		System.out.println(Thread.currentThread().getName() + Thread.currentThread().getPriority()); // JVM이 자동적으로 붙여줌
		new MyApp(); // 메인스레드가 종료되면 스레드를 TERMINATED 상태로 변경
		//Thread.currentThread().getPriority() -> 우선순위를 알려줌. main 스레드는 항상 우선순위 5. JAVA는 철저히 우선순위가 가장 높은 스레드를 실행
	}

}
