import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

class MyEditor implements ActionListener {
    JFrame jf;
    JLabel j1;
    JTextField jtf;
    JTextArea jta, jta1;
    JButton jbcompiler, jbrun;
    JScrollPane jsp, jsp1;
    Runtime r;
    String str = "";
    String fname = "";
    String result = "";
    String result1 = "";

    MyEditor() {
        jf = new JFrame("My Editor");
        jf.setLayout(null);

        j1 = new JLabel("Enter java Class Name");
        j1.setBounds(20, 20, 130, 25);

        jtf = new JTextField();
        jtf.setBounds(180, 20, 230, 25);

        jta = new JTextArea(50, 50);
        jta.addFocusListener(new MyFocusListener(this));
        jta1 = new JTextArea(50, 50);

        jta.setFont(new Font("Verdana", Font.PLAIN, 15));   // fixed font
        jta1.setFont(new Font("Verdana", Font.PLAIN, 15));

        jsp = new JScrollPane(jta);
        jsp1 = new JScrollPane(jta1);

        jsp.setBounds(50, 60, 320, 150);
        jsp1.setBounds(50, 270, 320, 150);

        jf.add(jsp);
        jf.add(jsp1);

        jbcompiler = new JButton("Compile");
        jbrun = new JButton("RUN");

        jbcompiler.setBounds(100, 230, 80, 25);
        jbrun.setBounds(280, 230, 80, 25);

        jf.add(j1);
        jf.add(jtf);
        r = Runtime.getRuntime();

        jf.add(jbcompiler);
        jf.add(jbrun);

        jbcompiler.addActionListener(this);
        jbrun.addActionListener(this);

        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setSize(550, 550);
        jf.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == jbcompiler) {
            str = "";
            result = "";   // reset
            if (!jtf.getText().equals("")) {
                try {
                    fname = jtf.getText().trim() + ".java";
                    FileWriter fw = new FileWriter(fname);
                    String s1 = jta.getText();
                    PrintWriter pw = new PrintWriter(fw);
                    pw.println(s1);
                    pw.flush();
                    pw.close();

                    Process error = r.exec("C:\\Program Files\\Java\\jdk-24\\bin\\javac.exe -d . " + fname);
                    BufferedReader err = new BufferedReader(new InputStreamReader(error.getErrorStream()));

                    String temp;
                    while ((temp = err.readLine()) != null) {
                        result += temp + "\n";
                    }

                    if (result.equals("")) {
                        jta1.setText("Compilation successful! " + fname);
                    } else {
                        jta1.setText(result);
                    }
                    err.close();
                } catch (Exception e1) {
                    System.out.println(e1);
                }
            } else {
                jta1.setText("Please Enter the java program name!");
            }
        } else if (e.getSource() == jbrun) {
            result = "";
            result1 = "";
            try {
                String fn = jtf.getText().trim();
                Process p = r.exec("C:\\Program Files\\Java\\jdk-24\\bin\\java -cp . " + fn);

                BufferedReader output = new BufferedReader(new InputStreamReader(p.getInputStream()));
                BufferedReader error = new BufferedReader(new InputStreamReader(p.getErrorStream()));

                String temp;
                while ((temp = output.readLine()) != null) {
                    result += temp + "\n";
                }

                while ((temp = error.readLine()) != null) {
                    result1 += temp + "\n";
                }

                output.close();
                error.close();

                jta1.setText(result + "\n" + result1);
            } catch (Exception e2) {
                System.out.println(e2);
            }
        }
    }

    public static void main(String arg[]) {
        new MyEditor();
    }
}

class MyFocusListener extends FocusAdapter {
    MyEditor e;

    MyFocusListener(MyEditor e) {
        this.e = e;
    }

    public void focusGained(FocusEvent fe) {
        String str = e.jtf.getText().trim();
        e.jta.setText(
            "public class " + str + "\n" +
            "{\n" +
            "    public static void main(String... s)\n" +
            "    {\n" +
            "        // Your code here\n" +
            "    }\n" +
            "}"
        );
    }
}
