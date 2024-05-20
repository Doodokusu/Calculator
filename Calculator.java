import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;

public class Calculator extends Frame implements WindowListener, ActionListener {

    GridBagLayout a = new GridBagLayout();
	GridBagConstraints b = new GridBagConstraints();

    Label tf = new Label();

    Frame f = new Frame();
	Panel p = new Panel();

    private String screen = "0";
    DecimalFormat decimalFormat = new DecimalFormat("#.#######");

    public void setGrid(int x, int y, int width, int height) {
        b.gridx = x;
        b.gridy = y;
        b.gridwidth = width;
        b.gridheight = height;
    }

    public Button setButton(String label, int x, int y, int width, int height) {
        Button newButton = new Button(label);
        setGrid(x, y, width, height);
        a.setConstraints(newButton, b);
        p.add(newButton);
        newButton.addActionListener(this);

        return newButton;
    }

    public Calculator() {

        p.setLayout(a);
        b.fill=GridBagConstraints.BOTH;
    	b.insets=new Insets(4,4,4,4);

        this.setGrid(0, 0, 4, 1);
        tf.setText(screen);
        a.setConstraints(tf, b);
        tf.setBackground(Color.WHITE);
        tf.setAlignment(Label.RIGHT);
        p.add(tf);

        setButton("0", 0, 5, 2, 1);
        setButton("1", 0, 4, 1, 1);
        setButton("2", 1, 4, 1, 1);
        setButton("3", 2, 4, 1, 1);
        setButton("4", 0, 3, 1, 1);
        setButton("5", 1, 3, 1, 1);
        setButton("6", 2, 3, 1, 1);
        setButton("7", 0, 2, 1, 1);
        setButton("8", 1, 2, 1, 1);
        setButton("9", 2, 2, 1, 1);

        setButton("/", 1, 1, 1, 1);
        setButton("*", 2, 1, 1, 1);
        setButton("-", 3, 2, 1, 1);
        setButton("+", 3, 3, 1, 1);

        setButton("<-", 3, 1, 1, 1);

        Button bEq = setButton("=", 3, 4, 1, 2);
        bEq.setBackground(new Color(255, 163, 0));

        setButton(",", 2, 5, 1, 1);
        Button bC = setButton("C", 0, 1, 1, 1);
        bC.setBackground(Color.RED);
        
        p.setBackground(Color.DARK_GRAY);
		f.add(p);
		
		f.addWindowListener(this);
		f.pack();
		f.setSize(400,400);
		f.setResizable(false);
		f.setVisible(true);
    }

    public boolean isDecimal(String s) {
        boolean isDecimal = false;
        for(int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (c == ',') {
                isDecimal = true;
            }
            if (isOperator(c)) {
                isDecimal = false;
            }
        }
        return isDecimal;
    }

    public double calculate(String s) {
        double num = 0;
        char operator = '+';
        double last = 0, sum = 0;
        boolean isDecimal = false;
        double decimalMultiplier = 0.1f;
        double tempNum = 0;
        boolean isDivideByNegative = false;
        char isDivideOperator = '*';
    
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (c == ',') {
                isDecimal = true;
            }
            
            if ((c == '/' || c == '*') && i+1 < s.length()) {
                char c1 = s.charAt(i+1);

                if (c1 == '-') {
                    isDivideByNegative = true;
                    if (c == '*')       isDivideOperator = '*';
                    if (c == '/')       isDivideOperator = '/';
                }
            }
            else if (Character.isDigit(c)) {
                if (isDecimal) {
                    num += (c - '0') * decimalMultiplier;
                    decimalMultiplier *= 0.1f; 
                }
                else {
                    num = num * 10 + (c - '0');
                }
            }

            if ((isOperator(c) || i == s.length() - 1) && !isDivideByNegative) {
                if (operator == '+') {
                    sum += last;
                    last = num;
                }
                else if (operator == '-') {
                    sum += last;
                    last = -num;
                }
                else if (operator == '*')           {last *= num;}
                else if (operator == '/')           {last /= num;}
    
                num = 0;
                operator = c;
                isDecimal = false;
                decimalMultiplier = 0.1f;
            }
            else if (isDivideByNegative) {
                isDecimal = false;
                decimalMultiplier = 0.1f;
                last = num;
                int j = i;
                tempNum = 0;
                while (j < s.length()) {
                    char c1 = s.charAt(j);
                    if (c1 == ',') {
                        isDecimal = true;
                    }
                    if(Character.isDigit(c1)) {
                        if (isDecimal) {
                            tempNum += (c1 - '0') * decimalMultiplier;
                            decimalMultiplier *= 0.1f; 
                        }
                        else {
                            tempNum = tempNum * 10 + (c1 - '0');
                        }
                    }

                    j++;
                }
                if (s.charAt(0) == '-') {
                    last *= -1;
                }
                if (isDivideOperator == '*') {
                    last *= (tempNum * -1);
                } else if (isDivideOperator == '/') {
                    last /= (tempNum * -1);
                }
                i = s.length();
                isDivideByNegative = false;
            }
            
        }
    
        return sum += last;
    }
    

    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    private boolean isAddSub(char c) {
        return c == '+' || c =='-';
    }

    private boolean isMulDiv(char c) {
        return c == '*' || c == '/';
    }

    private boolean isNum(char c) {
        return c >= '1' && c <= '9';
    }


    public static void main(String args[]) {
        Calculator uygulama = new Calculator();
    }

    public void actionPerformed(ActionEvent e) {
        String s = e.getActionCommand();

        boolean isDecimal = isDecimal(tf.getText());
        
        if(!s.equals("=") && !s.equals("C") && !s.equals("<-")) {
            if ((tf.getText().equals("0") && s.equals("0"))) {
                screen = "0";
            }            

            else if(tf.getText().charAt(0) == '-' && s.equals("+") && tf.getText().length()==1){
                screen = "+";
            }
            else if(tf.getText().charAt(0) == '+' && s.equals("-") && tf.getText().length()==1){
                screen = "-";
            }
            
            else if ((screen == "2. Sayiyi Giriniz")) {
                screen = s;
            }
            else if (tf.getText().equals("0") && !s.equals("0") && !s.equals(",") && !isMulDiv(s.charAt(0))) {
                screen = "";
                screen += s;
            }
            else if (tf.getText().endsWith("0") && isNum(s.charAt(0)) && tf.getText().length()>2 && isOperator(tf.getText().charAt(tf.getText().length()-2))) {
                String aa = screen.substring(0, screen.length()-1) + s;
                screen = aa;
            }
            else if ((s.equals("*") && tf.getText().endsWith("*")) || 
                     (s.equals("/") && tf.getText().endsWith("/")) ||
                     (s.equals("*") && tf.getText().endsWith("/")) ||
                     (s.equals("/") && tf.getText().endsWith("*")) ||
                     (s.equals("*") && tf.getText().endsWith("+")) ||
                     (s.equals("*") && tf.getText().endsWith("-")) ||
                     (s.equals("/") && tf.getText().endsWith("+")) ||
                     (s.equals("/") && tf.getText().endsWith("-")))    {}
            else if (isDecimal && s.equals(","))                                                {}
            else if (tf.getText().length() < 1 && (s.equals("/") || s.equals("*")))    {}
            else if (tf.getText().length() > 1 && 
                        (s.equals("+") && isOperator(tf.getText().charAt(tf.getText().length()-1))) ||
                        (isMulDiv(s.charAt(0)) && isOperator(tf.getText().charAt(tf.getText().length()-1)))) {}
            else if (s.equals("-") && (tf.getText().endsWith("--") || tf.getText().endsWith("/-") || tf.getText().endsWith("+-")))       {}
            else if (tf.getText().length() < 2 && s.equals("-") && tf.getText().endsWith("-"))                    {}
            else if (((tf.getText().length()==1 && tf.getText()=="0") || (tf.getText().length()>2 && tf.getText().charAt(tf.getText().length()-1)=='0' && isOperator(tf.getText().charAt(tf.getText().length()-2)))) && s.equals("0")) {}
            else if (tf.getText().endsWith(",") && !isNum(s.charAt(0)) && !s.equals("0"))                                              {}
            else if (screen.length() < 2 && isAddSub(s.charAt(0)) && isAddSub(tf.getText().charAt(tf.getText().length()-1))){}
            else if ((!isNum(tf.getText().charAt(tf.getText().length()-1)) && tf.getText().charAt(tf.getText().length()-1) != '0') && s.equals(",")) {
                screen += "0,";
            }

            else {
                screen += s;
            }
            tf.setText(screen);
            
        }

        if(s.equals("<-") && screen!="0") {
            if(screen.length()==1) {
                screen = "0";
            }
            else {
                screen = screen.substring(0, screen.length()-1);
            }
            tf.setText(screen);
        }
    
        if(s.equals("C")) {
            screen = "0";
            tf.setText(screen);
        }
        if(s.equals("=")) {
            for(int i=0; i<screen.length(); i++) {
                if (screen.length()>1 && i != 0 && screen.charAt(i) == '-' && screen.charAt(i-1) == '-') {
                    String aa = screen.substring(0, i-1) + "+" + screen.substring(i+1, screen.length());
                    screen = aa;
                }
            }
            if(isOperator(screen.charAt(screen.length()-1))) {
                screen = "2. Sayiyi Giriniz";
            }
            else {
                double result = calculate(screen);
                if(result == (int)result) {
                    screen = Integer.toString((int)result);
                }
                else {
                    screen = decimalFormat.format(result).replace(",", ",");
                }
            }    
            tf.setText(screen);
        }

    }

    public void windowOpened(WindowEvent e) {}
    public void windowClosing(WindowEvent e) {
        System.exit(0);
    }
    public void windowClosed(WindowEvent e) {}
    public void windowIconified(WindowEvent e) {}
    public void windowDeiconified(WindowEvent e) {}
    public void windowActivated(WindowEvent e) {}
    public void windowDeactivated(WindowEvent e) {}
}