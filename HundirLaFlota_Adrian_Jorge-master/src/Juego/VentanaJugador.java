package Juego;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.Color;
import javax.swing.border.LineBorder;
import javax.swing.JTextField;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class VentanaJugador extends JFrame {

	private JPanel contentPane;
    public static JLabel misBarcos ;
    public static JLabel barcosRival;
    public static JTextField edt_fila;
    public static JTextField edt_columna;
    public static JButton btn_enviar;
    public static JLabel txt_turno;
    public static JLabel txt_misAtaques;
    public static JLabel txt_ataquesRival;
    
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaJugador frame = new VentanaJugador();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public VentanaJugador() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 878, 544);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		misBarcos = new JLabel("");
		misBarcos.setAlignmentX(Component.CENTER_ALIGNMENT);
		misBarcos.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		misBarcos.setFont(new Font("Tahoma", Font.BOLD, 45));
		misBarcos.setBackground(new Color(0, 255, 0));
		misBarcos.setBounds(20, 23, 411, 324);
		contentPane.add(misBarcos);
		
		barcosRival = new JLabel("");
		barcosRival.setFont(new Font("Tahoma", Font.BOLD, 45));
		barcosRival.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		barcosRival.setBackground(Color.CYAN);
		barcosRival.setBounds(441, 23, 411, 324);
		contentPane.add(barcosRival);
		
		JLabel lblNewLabel = new JLabel("Tus Barcos");
		lblNewLabel.setBounds(20, 0, 65, 24);
		contentPane.add(lblNewLabel);
		
		JLabel lblBarcosRival = new JLabel("Barcos Rival");
		lblBarcosRival.setBounds(441, 0, 113, 24);
		contentPane.add(lblBarcosRival);
		
		JLabel lblNewLabel_1 = new JLabel("Fila");
		lblNewLabel_1.setBounds(20, 383, 46, 14);
		contentPane.add(lblNewLabel_1);
		
		edt_fila = new JTextField();
		edt_fila.setBounds(20, 397, 86, 20);
		contentPane.add(edt_fila);
		edt_fila.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel("Columna");
		lblNewLabel_2.setBounds(20, 424, 46, 14);
		contentPane.add(lblNewLabel_2);
		
		edt_columna = new JTextField();
		edt_columna.setColumns(10);
		edt_columna.setBounds(20, 442, 86, 20);
		contentPane.add(edt_columna);
		
		btn_enviar = new JButton("¡ATACA!");
		btn_enviar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ClienteJugador.barcosRival.setColumna(Integer.valueOf(VentanaJugador.edt_columna.getText())-1);
				ClienteJugador.barcosRival.setFila(Integer.valueOf(VentanaJugador.edt_fila.getText())-1);
				ClienteJugador.ok =  ClienteJugador.comprobarMiTablero(ClienteJugador.barcosRival);
			}
		});
		btn_enviar.setBounds(20, 473, 89, 23);
		contentPane.add(btn_enviar);
		
		txt_turno = new JLabel("TURNO");
		txt_turno.setBounds(137, 358, 281, 40);
		contentPane.add(txt_turno);
		
		txt_misAtaques = new JLabel("aqui va tus ataques");
		txt_misAtaques.setBounds(122, 400, 365, 38);
		contentPane.add(txt_misAtaques);
		
		txt_ataquesRival = new JLabel("aqui va ataques del rival");
		txt_ataquesRival.setBounds(116, 445, 357, 38);
		contentPane.add(txt_ataquesRival);
	}
}
