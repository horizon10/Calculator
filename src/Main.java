import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


public class Main {
    public static void main(String[] args)  {

        //Ekran boyutunu ayarlama
        JFrame jFrame =new JFrame("Calculator");
        jFrame.setBounds(800,300,350,450);

        //Yazı tipi ayarlama
        Font f1=new Font("Arial",Font.BOLD,15);
        Font f2=new Font("Arial",Font.BOLD,40);

        //Renkleri ve ikon ayarları
        jFrame.getContentPane().setBackground(Color.darkGray);
        jFrame.setResizable(false);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ImageIcon icon=new ImageIcon("icons/475497.png");
        jFrame.setIconImage(icon.getImage());

        //Tuşların text'lerini for döngüsü ile atamak için JButton array'i
        JButton[] buttons=new JButton[16];
        String[] str=new String[]{"9","8","7","->","6","5","4","x","3","2","1","/","+","0","-","="};

        //Tuşları atama
        int x=0,y=60,a=0;
        for(int i=0;i<4;i++){
            for(int j=0;j<4;j++){
                buttons[i+a]=new JButton(String.valueOf(str[i+a]));
                buttons[i+a].setFont(f1);
                buttons[i+a].setBackground(Color.white);
                buttons[i+a].setBounds(50+ x, 90 + y, 50, 50);
                jFrame.add(buttons[i+a]);
                x += 60;
                a++;
            }
            a-=1;
            x=0;
            y+=60;
        }


        //panel ayarları
        JTextField textField= new JTextField();
        //sağdan sola doğru yazmak için
        textField.setHorizontalAlignment(JTextField.RIGHT);
        textField.setBounds(50,60,230,50);
        textField.setFont(f2);
        jFrame.add(textField);


        //textAreada yazma
        for ( int i = 0; i < 16; i++) {
            if ((i != 3) && (i != 15)) {
                buttons[i].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        textField.setText(textField.getText()+e.getActionCommand());
                    }
                });
            }
        }

        //silme işlemi
        buttons[3].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String currentText = textField.getText();
                int textLength = currentText.length();

                if (textLength > 0) {
                    // Son karakteri sil
                    textField.setText("");
                }
                }
            });

        //Hesaplama

        buttons[15].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String ifade =textField.getText();
                System.out.println(ifade);
                double sonuc = hesapla(ifade);
                System.out.println("Sonuç: " + sonuc);
                textField.setText(String.valueOf(sonuc));

            }
        });



        jFrame.setLayout(null);
        jFrame.setVisible(true);
    }
    public static double hesapla(String ifade) {
        Stack<Double> sayilar = new Stack<>();
        Stack<Character> islemler = new Stack<>();

        for (int i = 0; i < ifade.length(); i++) {
            char karakter = ifade.charAt(i);
            if (Character.isDigit(karakter) || (karakter == '-' && (i == 0 || !Character.isDigit(ifade.charAt(i - 1))))) {
                StringBuilder sayi = new StringBuilder();
                if (karakter == '-') {
                    sayi.append('-');
                    i++;
                }
                while (i < ifade.length() && (Character.isDigit(ifade.charAt(i)))) {
                    sayi.append(ifade.charAt(i));
                    i++;
                }
                sayilar.push(Double.parseDouble(sayi.toString()));
                i--;
            } else if (karakter == '+' || karakter == '-' || karakter == 'x' || karakter == '/') {
                while (!islemler.isEmpty() && oncelikDegeri(islemler.peek()) >= oncelikDegeri(karakter)) {
                    hesaplaVeItmesiniYap(sayilar, islemler);
                }
                islemler.push(karakter);
            }
        }

        while (!islemler.isEmpty()) {
            hesaplaVeItmesiniYap(sayilar, islemler);
        }

        return sayilar.pop();
    }

    public static int oncelikDegeri(char isaret) {
        if (isaret == '+' || isaret == '-') {
            return 1;
        } else if (isaret == 'x' || isaret == '/') {
            return 2;
        }
        return 0;
    }

    public static void hesaplaVeItmesiniYap(Stack<Double> sayilar, Stack<Character> islemler) {
        double sayi2 = sayilar.pop();
        double sayi1 = sayilar.pop();
        char isaret = islemler.pop();
        double sonuc = 0;
        switch (isaret) {
            case '+':
                sonuc = sayi1 + sayi2;
                break;
            case '-':
                sonuc = sayi1 - sayi2;
                break;
            case 'x':
                sonuc = sayi1 * sayi2;
                break;
            case '/':
                if (sayi2 != 0) {
                    sonuc = sayi1 / sayi2;
                } else {
                    throw new ArithmeticException("Sıfıra bölme hatası!");
                }
                break;
        }
            sayilar.push(sonuc);
    }
}

